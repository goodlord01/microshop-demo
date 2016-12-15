(function ($) {

  var _tips = '<div class="zepto-tips-wrap">' +
    '<div class="zepto-tips-<%=type%>">' +
    '<div class="zepto-tips-content">' +
    '<i><%=icon%></i><%=content%>' +
    '</div></div></div>';

  var defaults = {
    content: '',
    duration: 3000,
    type: 'info'
  };

  function Tips(element, option) {
    var self = this;
    this.element = $(element);
    this.elementHeight = $(element).height();
    this.option = option;

    $(element).css({
      "-webkit-transform": "scale(0)"
    });

    setTimeout(function () {
      $(element).css({
        "-webkit-transition": "all .5s"
      });

      self.show();
    }, 50);
  }

  Tips.prototype = {
    constructor: Tips,
    show: function () {
      var self = this;
      self.element.trigger($.Event("tips:show"));
      self.element.css({
        "-webkit-transform": "scale(1)"
      });
      if (self.option.duration > 0) {
        setTimeout(function () {
          self.hide();
        }, self.option.duration);
      }
    },
    hide: function () {
      var self = this;
      self.element.trigger($.Event("tips:hide"));
      self.element.css({
        "-webkit-transform": "scale(0)"
      });
      setTimeout(function () {
        self.element.remove();
      }, 500);
    }
  };

  function plugin(option) {

    var context = $.extend({}, defaults, typeof option === 'object' && option);

    var content = context.content.replace(/\\/g, '\\\\').replace(/[\r\t\n]/g, "").replace(/'/g, "\\'");
    var str = _tips.replace(/[\r\t\n]/g, "").replace('<%=type%>', context.type).replace('<%=content%>', content);

    if (context.type === 'success') {
      str = str.replace('<%=icon%>', '&#xe657;');
    }
    else if (context.type === 'warning') {
      str = str.replace('<%=icon%>', '&#xe663;');
    }
    else {
      str = str.replace('<%=icon%>', '&#xe6e5;');
    }

    var $this = $(str).appendTo("body");

    return $this.each(function () {

      var element = $(this);

      var data = element.data('fz.' + "tips");

      if (!data) element.data('fz.' + "tips",
        (data = new Tips(this, $.extend({}, defaults, typeof option === 'object' && option))

        ));

      if (typeof option === 'string') data[option]();
    });
  }

  $.tips = plugin;

})(window.Zepto);



