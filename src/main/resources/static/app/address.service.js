(function() {
  angular.module('myApp.services').factory('addressService', addressService);
  addressService.$inject = ['$http', '$q'];

  function addressService($http, $q) {
    return {
      getAddressById : getAddressById
    };

    function getAddressById(id) {
      return $http.get('/address/'+id)
        .then(getAddressByIdComplete)
        .catch(getAddressByIdFailed);

      function getAddressByIdComplete(response) {
        return response.data;
      }

      function getAddressByIdFailed(e) {
        console.log('XHR Failed for getAddressById.' + e.message);
        return $q.reject(e);
      }
    }
  }

}());