(function() {
    angular.module('myApp.services').factory('authService', AuthService);
    AuthService.$inject = ['$http', '$q'];

    function AuthService($http, $q) {
        return {
            checkLogin : checkLogin,
            checkSellerLogin: checkSellerLogin
        };

        function checkLogin(){
            var def = $q.defer();
            $http.get('auth/check')
                .success(function (data) {
                    console.log("check login");
                    def.resolve(data);
                }).catch(function (data, code) {
                    console.log(data);
                    def.reject(data);
                });

            return def.promise;
        }

        function checkSellerLogin(){
            var def = $q.defer();
            $http.get('auth/seller_check')
                .success(function (data) {
                    console.log("check login");
                    def.resolve(data);
                }).catch(function (data, code) {
                    console.log(data);
                    def.reject(data);
                });

            return def.promise;
        }
    }

}());