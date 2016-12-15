(function () {
  'use strict';

  angular
    .module('myApp')
    .directive('whenScrolled', productItems);

  function productItems($document) {
    return function(scope, elm, attr) {
      $document.bind('scroll', function() {
        var raw = $document[0].body;
        // console.log('raw.scrollTop is '+raw.scrollTop+' window.screen.availHeight is '+window.screen.availHeight+ ' raw.scrollHeight is '+raw.scrollHeight);
        if (raw.scrollTop + window.screen.availHeight >= raw.scrollHeight) {
          scope.$apply(attr.whenScrolled);
        }
      });
    };
  }

  /*
   * Get notified when height changes and change margin-top
   */
  angular
    .module('myApp').directive( 'emHeightTarget', function() {
    return {
      link: function( scope, elem, attrs ) {

        scope.$watch( '__height', function( newHeight, oldHeight ) {
          console.log('new height is '+newHeight+ ' old is '+ oldHeight);
          // elem.attr( 'style', 'margin-top: ' + (58 + newHeight) + 'px' );
        } );
      }
    }
  } );

  /*
   * Checks every $digest for height changes
   */
  angular
    .module('myApp').directive( 'emHeightSource', function() {

      return {
        link: function( scope, elem, attrs ) {

          scope.$watch( function() {
            console.log('elem is '+elem[0]+' elem height is ' + elem[0].offsetHeight);
            scope.__height = elem[0].offsetHeight;
          } );
        }
      }

    } );
})();
