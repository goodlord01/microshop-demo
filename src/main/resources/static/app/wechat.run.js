/**
 * Created by yan on 11/8/2016.
 */
(function () {
  angular.module('myApp.wechat')
      .run(wechatRun);
  wechatRun.$inject = ['$window', '$location', '$rootScope', 'wechatService'];

  function wechatRun($window, $location, $rootScope, wechatService) {
    var url = $location.$$absUrl.split('#')[0];
    var path = $location.path();
    wechatService.getConfig(url, path).then(function (data) {
      $window.wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: data.appId, // 必填，公众号的唯一标识
        timestamp: data.timestamp,// 必填，生成签名的时间戳
        nonceStr: data.noncestr, // 必填，生成签名的随机串
        signature: data.signature,// 必填，签名，见附录1
        jsApiList: ['checkJsApi', 'openAddress', 'chooseWXPay', 'onMenuShareTimeline', 'onMenuShareAppMessage']
      });

      $window.wx.ready(function () {
            $window.wx.checkJsApi({
              jsApiList: [
                'openAddress', 'chooseWXPay', 'onMenuShareTimeline', 'onMenuShareAppMessage'
              ],
              success: function (res) {
                $rootScope.$broadcast('wechat-ready');
              },
              error: function (data) {
                alert("请使用微信浏览器");
              }
            });

            $window.wx.error(function (res) {

              console.log(res);
              // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

            });
          }
      );

    }).catch(function (error) {

    })
  }
}());
