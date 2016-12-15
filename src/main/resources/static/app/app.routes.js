(function () {
  'use strict';
  angular
      .module('myApp.routes')
      .config(config);

  function config($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/home/');

    $stateProvider
        .state('home', {
          url: '/home',
          templateUrl: 'products/products_1.html'
        })
        .state('consult', {
          url: '/consult',
          templateUrl: 'consult.html'
        })
        .state('market-reprize', {
          url: '/market-reprize',
          templateUrl: 'market-reprize.html'
        })
  }
})();