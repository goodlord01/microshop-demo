(function () {

  angular
      .module('myApp.controllers').directive('imageonload', function () {
    return {
      restrict: 'A', link: function (scope, element, attrs) {
        element.bind('load', function () {
          //call the function that was passed
          scope.$apply(attrs.imageonload);
        });
      }
    };
  });

  angular
      .module('myApp.controllers')
      .controller('ProductsController', ProductsController);

  ProductsController.$inject = ['$state', '$location', '$window', 'wechatService',
    '$scope', 'marketService', 'market', 'authService'];

  function ProductsController($state, $location, $window, wechatService,
                              $scope, marketService, market, authService) {

    var vm = this;

    vm.showConsultPage = false;
    vm.showBarCode = false;
    vm.showSupportDialog = false;
    vm.showPaymentDialog = false;
    vm.showShare = false;
    vm.showMarket = false;
    vm.showPrize = false;
    vm.showNoPrize = false;
    vm.showFans = true;

    vm.thumbUp = thumbUp;
    vm.thumbExist = true;

    vm.switchTab = switchTab;
    vm.currentTab = 4;

    vm.tabs = [{name: '咨询', img: 'care'}, {name: '关注', img: 'wechat'}, {name: '分享', img: 'share'}];

    vm.numbers = [];

    vm.highlightTab = highlightTab;

    vm.showDrawRecords = false;
    vm.drawRecordsNum = 0;
    vm.drawPrizedAmount = 0;
    vm.drawRecords = [];
    vm.marketImageLoadedFlag = false;
    vm.productImageLoadedFlag = false;

    vm.currentUser = null;

    // activate();

    vm.marketImageLoaded = function () {
      vm.marketImageLoadedFlag = true;
    };

    vm.productImageLoaded = function () {
      vm.productImageLoadedFlag = true;
    };

    $scope.$on('wechat-ready', function (e, d) {
      wechatService.share();
    });

    function activate() {
      checkThumberExist();

      var thumbs = thumbsService.thumbsData.data;
      if (thumbs.length > 0) {
        vm.thumbs = thumbs.slice(0, 6);
        console.log('load thumbs from cache');
      } else {
        getThumbs().then(function () {
          console.log('getThumbs success');
        }).catch(function (e) {
          console.log('getThumbs error is ' + e.message);
        });
      }
    }

    function checkThumberExist() {
      thumbsService.checkExist().then(function (data) {
        console.log('user exist ', data);
        vm.thumbExist = data;
      })
    }

    function convertNumbers(num_str) {
      var number = parseInt(num_str, 10);
      do {
        vm.numbers.splice(0, 0, number % 10);
        number = parseInt(number / 10);
      } while (number > 0);
    }


    function getThumbs() {
      return thumbsService.getThumbs()
          .then(function (data) {
            convertNumbers(data.length);
            data = data || [];
            vm.thumbs = data.slice(0, 6);
          });
    }

    function thumbUp() {
      if (vm.thumbExist) return;
      if (vm.currentUser) {
        var thumb = {
          sellers_openid: sellerOpenId,
          openid: vm.currentUser.openid
        };

        marketService.postThumbs(thumb).then(function (data) {
          $state.go("thumbs");
        }).catch(function (e) {
        });
      }
      else {
        $window.location.href = wechatService.getCodeRedirectUrl(siteUrl, null, siteUrl + "#/thumbs/" + vm.productId, true, sellerOpenId);
        console.log('redirect to url ' + $window.location.href);
      }
    }

    function highlightTab(i) {
      var highlight = vm.currentTab == i;
      if (i == 0) {
        highlight &= vm.showConsultPage;
      } else if (i == 1) {
        highlight &= vm.showBarCode;
      } else {
        highlight &= vm.showShare;
      }
      return highlight;
    }

    function switchTab(i) {
      vm.currentTab = i;
      if (i == 0) {
        vm.showConsultPage = true;
        goConsult();
      } else if (i == 1) {
        vm.showBarCode = true;
        vm.showShare = false;

      } else {
        vm.showShare = true;
        vm.showBarCode = false;
      }
    }

    vm.getWechatAddress = function () {
      // vm.address = {
      // name: 'min',
      // phone: '137777777',
      // zipCode: '544545',
      // country: '中国',
      // province: '浙江',
      // city: '杭州',
      // address: '文二西路387号建材城地下室'
      // };
      // return;

      wechatService.getWechatAddress().then(function (data) {
        vm.address = {
          name: data.userName,
          phone: data.telNumber,
          zip_code: data.postalCode,
          country: data.countryName,
          province: data.provinceName,
          city: data.cityName,
          address: data.detailInfo
        }
      });
    };

    function disablePageScroll() {
      //$('html,body').css('overflow', 'hidden');
      //$('html,body').css('height', '100%');
      $('html,body').css('top', -$(window).scrollTop());
      $('html,body').css('position', 'fixed');
    }

    function enablePageScroll() {
      //$('html,body').css('overflow', 'visible');
      $('html,body').css('position', null);
      $(window).scrollTop(Math.abs(parseInt($('html,body').css('top'))));
      $('html,body').css('top', null);
    }

    $scope.$watch('vm.showMarket', function (newValue, oldValue, scope) {
      console.log('new value is ' + newValue + ' old value is ' + oldValue);

      if (newValue === true) {
        disablePageScroll();
      }
      else {
        enablePageScroll();
      }
    });

    $scope.$watchGroup(['vm.showShare', 'vm.showPrize', 'vm.showNoPrize'], function (newValue, oldValue, scope) {
      if (angular.equals(newValue, oldValue)) {
        return;
      }
      if (newValue.indexOf(true) != -1) {
        disablePageScroll();
      }
      else {
        enablePageScroll();
      }
    });

    $(window).scroll(function () {
      if (document.body.scrollTop >= (document.body.scrollHeight - window.screen.availHeight)) {
        $scope.$apply(function () {
        });
      }
    });

    var isFirstLoad = false;

    $scope.$on('$stateChangeSuccess', function (event, to, toParams, from, fromParams) {
      //save the previous state in a rootScope variable so that it's accessible from everywhere
      console.log('previous state is ' + from.name + ' to is ' + to.name);
      if (from.name == '' && to.name == 'home') {
        isFirstLoad = true;
      }
      // showMarketingDraw();
    });


    function showMarketingDraw() {
      if ($state.params.scanned == 'scanned' || !isFirstLoad) {
        vm.showMarket = false;
      } else {
        vm.showMarket = true;
        $location.$$absUrl = $location.absUrl() + 'scanned';

        if (!$location.search()['isWeChatAuth']) {
          marketService.getTop10MarketingPrizeWeChatList().then(function (data) {
            vm.showMarket = true;
            if (data && data.prize_count > 0) {
              vm.showDrawRecords = true;
              vm.drawRecordsNum = data.prize_count;
              vm.drawRecords = data.wechat_prize;

              setInterval(function () {
                var firstDiv = $('#market .table-body > div:nth-child(1)');
                var tableBody = $('#market .table-body');
                var allDivs = $('#market .table-body > div');

                if (allDivs.length >= 5) {
                  firstDiv.animate({
                    marginTop: -firstDiv.height()
                  }, 500, function () {
                    firstDiv.css({marginTop: "0px"});
                    tableBody.append(firstDiv);
                  });
                }
              }, 1500);
            }
            else {
              vm.showDrawRecords = false;
            }
          }).catch(function (e) {
            console.log('error get top 10 marketing prize ' + e);
          });
        }
      }
    }

    vm.getPrizeClick = function () {
      marketService.getCurrentUser().then(function (data) {
        if (data && data.name) {
          vm.currentUser = data;
          checkDrawRecords(data);
        }
        else {
          $window.location.href = wechatService.getCodeRedirectUrl(siteUrl, null, siteUrl + "#/home?isWeChatAuth=1", true, sellerOpenId);
        }
      }).catch(function (e) {
      });
    };

    if ($location.search()['isWeChatAuth']) {
      marketService.getCurrentUser().then(function (data) {
        if (data) {
          vm.currentUser = data;
          checkDrawRecords(data);
        }
      }).catch(function (e) {
      });
    }

    function checkDrawRecords(user) {

      marketService.getMktDrawRecordByProductKeyAndUser(user).then(function (data) {

        if (data) {
          if (data.mkt_draw_record && !data.mkt_draw_record.isPrized) {
            //已拆过奖，但未中奖，领券页面
            $state.go("market-reprize");
          }
          else {
            if (data.mkt_draw_prize.status_code === 'submit' || data.mkt_draw_prize.status_code === 'paid') {
              //已拆过奖且已经中奖，且已经领奖，领券页面
              $state.go("market-reprize");
            }
            else if (data.mkt_draw_prize.status_code === 'created') {
              //已拆过奖，已经中奖，但未兑奖 data.mkt_draw_prize.amount
              vm.drawPrizedAmount = data.mkt_draw_prize.amount;
              vm.showMarket = false;
              vm.showPrize = true;
            }
          }
        }
        else {
          //未抽奖

          var postObj = {
            product_base_id: market.product_base_id,
            product_key: market.product_key,
            marketing_id: market.market_id,
            ysid: marketService.getCookie('YSID'),
            oauth_openid: user.openid,
            prize_account_name: user.name
          };

          marketService.postRandomPrizeAmount(postObj).then(function (data) {

            vm.showMarket = false;

            //save data

            if (data) {
              //已中奖，data.mkt_draw_prize.amount
              vm.drawPrizedAmount = data.amount;
              vm.showPrize = true;
            }
            else {
              //未中奖
              vm.showNoPrize = true;
            }
          }).catch(function (e) {
            vm.showMarket = false;

            window.Zepto.tips({
              content: '拆红包失败！',
              duration: 2000,
              type: "warning"
            });
          });
        }

      }).catch(function (e) {
      });
    }

    vm.getPrizeReturnClick = function () {

      marketService.sendWeChatRedPackets().then(function (data) {

        vm.showPrize = false;

        if (data) {
          window.Zepto.tips({
            content: '红包领取成功！',
            duration: 2000,
            type: "success"
          });
        }
        else {
          window.Zepto.tips({
            content: '红包领取失败！',
            duration: 2000,
            type: "warning"
          });
        }

      }).catch(function (e) {

        vm.showPrize = false;

        window.Zepto.tips({
          content: '红包领取失败！',
          duration: 2000,
          type: "warning"
        });

      });
    };

    vm.getPrizeReturnClickAndThumb = function () {

      marketService.sendWeChatRedPackets().then(function (data) {

        vm.showPrize = false;

        if (data) {
          var thumb = {
            sellers_openid: sellerOpenId,
            openid: vm.currentUser.openid
          };

          marketService.postThumbs(thumb).then(function (data) {
            window.Zepto.tips({
              content: '红包领取并点赞成功！',
              duration: 2000,
              type: "success"
            });
          }).catch(function (e) {
            window.Zepto.tips({
              content: '红包领取成功！',
              duration: 2000,
              type: "success"
            });
          });
        }
        else {
          window.Zepto.tips({
            content: '红包领取失败！',
            duration: 2000,
            type: "warning"
          });
        }
      }).catch(function (e) {

        vm.showPrize = false;

        window.Zepto.tips({
          content: '红包领取失败！',
          duration: 2000,
          type: "warning"
        });
      });

    };

    function goConsult() {
      $state.go("consult");
    }

    function isWeChat() {
      var ua = window.navigator.userAgent.toLowerCase();
      if (ua.match(/MicroMessenger/i) == 'micromessenger') {
        return true;
      } else {
        return false;
      }
    }

  }
}());