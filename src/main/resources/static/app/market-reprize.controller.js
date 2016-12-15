(function () {

  angular
      .module('myApp.controllers')
      .controller('MarketRePrizeController', MarketRePrizeController);

  MarketRePrizeController.$inject = ['$state', '$timeout', 'marketService'];

  function MarketRePrizeController($state, $timeout, marketService) {

    var vm = this;

    vm.reprize02Click = function () {
      var value = 'http://www.newtank.cn/newtank/act/payw/index.html?channel=zkjckx';
      marketService.postUserEvent(value).then(function () {
        location.href = value;
      }).catch(function () {
        location.href = value;
      });
    };

    vm.reprize03Click = function () {
      var value = 'https://m.csyeye.com/activity/2016/regist_2/index.html?approach=FF2-zhongkejincai4';
      marketService.postUserEvent(value).then(function () {
        location.href = value;
      }).catch(function () {
        location.href = value;
      });
    };

    vm.reprize04Click = function () {
      var value = 'http://game.kkcredit.cn/download/kuaixiao1/kakaTicket';
      marketService.postUserEvent(value).then(function () {
        location.href = value;
      }).catch(function () {
        location.href = value;
      });
    };

    vm.reprize05Click = function () {
      var value = 'https://tth365.com/landing_pages/uc_million_in_months';
      marketService.postUserEvent(value).then(function () {
        location.href = value;
      }).catch(function () {
        location.href = value;
      });
    };
  }
}());