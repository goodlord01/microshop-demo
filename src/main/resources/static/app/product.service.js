(function() {
  angular.module('myApp.services').factory('productService', productService);
  productService.$inject = ['$http', '$q'];

  function productService($http, $q) {
    var productsData = {data:[]};
    var serviceTerms = {data:[[],[]]};
    return {
      getProducts: getProducts,
      getProductById : getProductById,
      getServiceTerm: getServiceTerm,
      productsData : productsData,
      serviceTerms : serviceTerms
    };

    function getProducts(sellerOpenId) {
      return $http.get('product/seller/'+sellerOpenId)
        .then(getProductsComplete)
        .catch(getProductsFailed);

      function getProductsComplete(response) {
        productsData.data = response.data;
        return response.data;
      }

      function getProductsFailed(e) {
        console.log('XHR Failed for getProducts.' + e.message);
        return $q.reject(e);
      }
    }

    function getProductById(id) {
      return $http.get('product/'+id)
        .then(getProductByIdComplete)
        .catch(getProductByIdFailed);

      function getProductByIdComplete(response) {
        return response.data;
      }

      function getProductByIdFailed(e) {
        console.log('XHR Failed for getProductById.' + e.message);
        return $q.reject(e);
      }
    }

    function getServiceTerm(id) {
      return $http.get('product/'+id+'/term')
        .then(getServiceTermComplete)
        .catch(getServiceTermFailed);

      function getServiceTermComplete(response) {
        serviceTerms.data[id-1] = response.data;
        return response.data;
      }

      function getServiceTermFailed(e) {
        console.log('XHR Failed for getServiceTerm.' + e.message);
        return $q.reject(e);
      }
    }
  }

}());