/**
 * Created by yan on 11/8/2016.
 */
(function () {
    angular.module('myApp.wechat')
        .factory('wechatService', Service);
    Service.$inject = ['$http', '$q', '$window', '$localStorage', '$location'];

    function Service($http, $q, $window, $localStorage, $location) {
        return {
            getConfig: getConfig,
            getPayConfig: getPayConfig,
            getPayConfigWithOrderId: getPayConfigWithOrderId,
            getUser: getUser,
            getUnifiedOrder: getUnifiedOrder,
            getWechatAddress: getWechatAddress,
            getCodeRedirectUrl: getCodeRedirectUrl,
            pay: pay,
            share: share
        };

        function getConfig(url, path) {
            var def = $q.defer();
            var nonceStr = undefined;
            if ($localStorage.wechat && path == '/pay') {
                nonceStr = $localStorage.wechat.nonceStr;
            }
            var configUrl = 'wechat/config?url=' + encodeURIComponent(url);
            if(nonceStr)
                configUrl += '&nonce_str=' + nonceStr;
            $http.get( configUrl)
                .success(function (data) {
                    console.log("get wechat config success");
                    $localStorage.wechat = {
                        appId: data.appId,
                        timestamp: data.timestamp,
                        nonceStr: data.noncestr
                    };
                    def.resolve(data);
                }).catch(function (data, code) {
                    console.log(data);
                    def.reject(data);
                });

            return def.promise;
        }

        function getPayConfig(id) {
            var def = $q.defer();
            $http.get('wechat/pay/config?pre_pay_id=' + id + '&nonce_str=' + $localStorage.wechat.nonceStr + '&timestamp=' + $localStorage.wechat.timestamp)
                .success(function (data) {
                    console.log("get wechat pay config success");
                    $localStorage.wechat = undefined;
                    def.resolve(data);
                }).catch(function (data, code) {
                    console.log(data);
                    def.reject(data);
                });

            return def.promise;
        }

        function getPayConfigWithOrderId(id) {
            var def = $q.defer();
            $http.post('wechat/pay/' + id + '?nonce_str=' + $localStorage.wechat.nonceStr + '&timestamp=' + $localStorage.wechat.timestamp, null)
                .success(function (data) {
                    console.log("get wechat pay config success");
                   // $localStorage.wechat = undefined;
                    def.resolve(data);
                }).catch(function (data, code) {
                    console.log(data);
                    def.reject(data);
                });

            return def.promise;
        }


        function getCodeRedirectUrl(siteUrl, orderId, returnUrl, needUserInfo, sellerOpenId) {
            var lastParams = "&response_type=code&scope=snsapi_base&state=base#wechat_redirect";
            if (needUserInfo)
                lastParams = "&response_type=code&scope=snsapi_userinfo&state=user#wechat_redirect";

            var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + $localStorage.wechat.appId;

            if (orderId)
                url += '&redirect_uri=' + encodeURIComponent(siteUrl + 'wechat/handlecode?order_id=' + orderId +
                        '&nonce_str=' + $localStorage.wechat.nonceStr + '&return_url=' + encodeURIComponent(siteUrl + '#/pay')) + lastParams;
            else if (needUserInfo)
                url += '&redirect_uri=' + encodeURIComponent(siteUrl + 'wechat/handlecode?seller_openid=' + sellerOpenId + '&return_url=' + encodeURIComponent(returnUrl)) + lastParams;
            else
                url += '&redirect_uri=' + encodeURIComponent(siteUrl + 'wechat/handlecode?return_url=' + encodeURIComponent(returnUrl)) + lastParams;

            return url;
        }

        function getUser(code, state) {
            var def = $q.defer();
            $http.get('wechat/getuser?code=' + code + "&state=" + state)
                .success(function (data) {
                    console.log("get wechat user success");
                    def.resolve(data);
                }).catch(function (data, code) {
                    console.log(data);
                    def.reject(data);
                });

            return def.promise;
        }

        function getUnifiedOrder(orderId, openId) {
            var def = $q.defer();
            $http.get('wechat/unified?order_id=' + orderId + "&openid=" + openId)
                .success(function (data) {
                    console.log("get wechat user unifiedOrder");
                    def.resolve(data);
                }).catch(function (data, code) {
                    console.log(data);
                    def.reject(data);
                });

            return def.promise;
        }

        function getWechatAddress() {
            var def = $q.defer();
           // alert("begin get address");
            $window.wx.openAddress({
                success: function (data) {
                    // 用户成功拉出地址
                    console.log(data);
                    def.resolve(data);
                },
                cancel: function (data) {
                    // 用户取消拉出地址
                   // alert("拉出用户地址错误")
                    def.reject(data);
                },
                trigger: function (data) {
                    alert('用户开始拉出地址');
                }
            });
            return def.promise;

        }

        function pay(data) {
            var def = $q.defer();
            $window.wx.chooseWXPay({
                timestamp: data.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                nonceStr: data.nonceStr, // 支付签名随机串，不长于 32 位
                package: data.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                signType: data.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                paySign: data.sign, // 支付签名
                success: function (data) {
                    // 支付成功后的回调函数
                    def.resolve(data);
                },
                fail: function (data) {
                    //支付失败
                    def.reject(data);
                },
                cancel: function (data) {
                    //支付取消
                    def.reject(data);
                }
            });
            return def.promise;
        }

        function share() {
            var siteUrl = $location.$$absUrl.split('#')[0].split('?')[0];
            $window.wx.onMenuShareTimeline({
                title: '为了健康，关注宝哥', // 分享标题
                link: siteUrl, // 分享链接
                imgUrl: siteUrl + 'assets/img/products/share.png', // 分享图标
                success: function () {
                    // 用户确认分享后执行的回调函数
                },
                cancel: function () {
                    // 用户取消分享后执行的回调函数
                }
            });

            $window.wx.onMenuShareAppMessage({
                title: '为了健康，关注宝哥', // 分享标题
                link: siteUrl, // 分享链接
                imgUrl: siteUrl + 'assets/img/products/share.png', // 分享图标
                desc: '朋友说：宝哥人亲切，靠谱！',
                type: '', // 分享类型,music、video或link，不填默认为link
                dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                success: function () {
                    // 用户确认分享后执行的回调函数
                },
                cancel: function () {
                    // 用户取消分享后执行的回调函数
                }
            });
        }

    }
}());
