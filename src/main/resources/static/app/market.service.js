(function () {
  angular.module('myApp.market')
      .factory('marketService', Service);
  Service.$inject = ['$http', '$q', '$window', '$location', 'apiRabbit', 'market'];

  function Service($http, $q, $window, $location, apiRabbit, market) {
    return {
      postUserEvent: postUserEvent,
      getCookie: getCookie,
      getTop10MarketingPrizeWeChatList: getTop10MarketingPrizeWeChatList,
      getMktDrawRecordByProductKeyAndUser: getMktDrawRecordByProductKeyAndUser,
      postRandomPrizeAmount: postRandomPrizeAmount,
      getCurrentUser: getCurrentUser,
      postThumbs: postThumbs,
      sendWeChatRedPackets: sendWeChatRedPackets
    };

    function getTop10MarketingPrizeWeChatList() {
      var def = $q.defer();

      $http.get('/' + apiRabbit + '/marketing/drawPrize/' + market.market_id + '/top10')
          .success(function (data) {
            def.resolve(data);
          })
          .catch(function (data, code) {
            def.reject(data);
          });

      return def.promise;
    }

    function getMktDrawRecordByProductKeyAndUser(user) {
      var def = $q.defer();

      $http.get('/' + apiRabbit + '/marketing/draw/' + market.product_key + '/user/' + getCookie('YSID') + '?oauth_openid=' + user.openid)
          .success(function (data) {
            def.resolve(data);
          })
          .catch(function (data, code) {
            def.reject(data);
          });

      return def.promise;
    }

    function sendWeChatRedPackets() {
      var def = $q.defer();

      $http.get('/' + apiRabbit + '/marketing/draw/' + market.product_key + '/prize/' + getCookie('YSID'))
          .success(function (data) {
            def.resolve(data);
          })
          .catch(function (data, code) {
            def.reject(data);
          });

      return def.promise;
    }

    function postRandomPrizeAmount(mktDraw) {
      var def = $q.defer();

      $http.post('/' + apiRabbit + '/marketing/drawPrize/' + market.market_id + '/random', mktDraw)
          .success(function (data) {
            def.resolve(data);
          })
          .catch(function (data, code) {
            def.reject(data);
          });

      return def.promise;
    }

    function postThumbs(thumb) {
      var def = $q.defer();

      $http.post('thumbs', thumb)
          .success(function (data) {
            def.resolve(data);
          })
          .catch(function (data, code) {
            def.reject(data);
          });

      return def.promise;
    }

    function getCurrentUser() {
      var def = $q.defer();

      $http.get('customer/current')
          .success(function (data) {
            def.resolve(data);
          })
          .catch(function (data, code) {
            def.reject(data);
          });

      return def.promise;
    }

    function postUserEvent(value) {
      var def = $q.defer();

      var postObject = {
        scan_record_id: '',
        product_key: market.product_key,
        type_code: 'duplicate_award',
        value: value,
        product_base_id: ''
      };

      $http.post('/' + apiRabbit + '/user/event', postObject)
          .success(function (data) {
            def.resolve(data);
          })
          .catch(function (data, code) {
            def.reject(data);
          });

      return def.promise;
    }

    function getCookie(name) {
      if (!name) {
        return null;
      }

      return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*" + encodeURIComponent(name).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1")) || null;
    }
  }
}());
