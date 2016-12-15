(function (angular) {
  angular.module("myApp.controllers", ['ngAnimate']);
  angular.module("myApp.services", []);
  angular.module("myApp.routes", ['ui.router']);
  angular.module("myApp.wechat", ['ngStorage']);
  angular.module("myApp.market", []);
  angular.module("myApp", ["myApp.controllers", "myApp.services", "myApp.routes", "myApp.wechat", "myApp.market"]);

  angular.module("myApp").value('sellerOpenId', 'oSHnAv1oFo5Ekt6_g18O94eM8y7c');

  //服务器端nginx api-rabbit 映射
  angular.module("myApp").value('apiRabbit', 'api-rabbit');

  //微店market相关配置
  angular.module("myApp").value('market', {
    market_id: '2n9obwhwcxkpcorboed',
    product_key: '2n9q0shz1t7pdy7hg7a',
    product_base_id: '2n9v53vhbdu1gpzd6tu'
  });

}(angular));