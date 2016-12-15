(function () {

  angular
      .module('myApp.controllers')
      .controller('ConsultController', ConsultController);

  ConsultController.$inject = ['$state', '$timeout'];

  function ConsultController($state, $timeout) {

    var vm = this;

    $timeout(function () {
      $.fn.longPress = function (fn) {
        var timeout = undefined;
        var $this = this;
        for (var i = 0; i < $this.length; i++) {
          $this[i].addEventListener('touchstart', function (event) {
            timeout = setTimeout(fn, 400);  //长按时间超过800ms，则执行传入的方法
          }, false);
          $this[i].addEventListener('touchend', function (event) {
            clearTimeout(timeout);  //长按时间少于800ms，不会执行传入的方法
          }, false);
        }
      };

      $('#yang03_left_a').longPress(function () {
        console.log('yang03_left_a');
        $('#yang03_left').css('display', '');
      });

      $('#yang03_right_a').longPress(function () {
        console.log('yang03_right_a');
        $('#yang03_right').css('display', '');
      });

      $('#yang03_left_a').click(function () {
        console.log('yang03_left_a');
        $('#yang03_left').css('display', '');
      });

      $('#yang03_right_a').click(function () {
        console.log('yang03_right_a');
        $('#yang03_right').css('display', '');
      });

      $('.pop_close').click(function () {
        $('#yang03_left').css('display', 'none');
        $('#yang03_right').css('display', 'none');
      });
    }, 0);

  }
}());