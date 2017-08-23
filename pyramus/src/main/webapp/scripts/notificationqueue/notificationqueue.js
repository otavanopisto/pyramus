(function() {

  $.widget("custom.notificationQueue", {
    options : {
      'hide': {
        'effect': 'blind',
        'options': {
          'duration': 1000,
          'easing': 'easeOutBounce'
        }
      },
      'severity-info': {
        'class': 'notification-queue-item-info',
        'timeout': 5000
      },
      'severity-warn': {
        'class': 'notification-queue-item-warn'
      },
      'severity-error': {
        'class': 'notification-queue-item-error'
      },
      'severity-fatal': {
        'class': 'notification-queue-item-fatal'
      },
      'severity-success': {
        'class': 'notification-queue-item-success',
        'timeout': 5000
      },
      'severity-loading': {
        'class': 'notification-queue-item-loading'
      }
    },
    
    _create: function () {
      $('.notification-queue-item').each($.proxy(function (index, item) {
        this._setupItem(item);
      }, this));
    },
    
    notification: function (severity, message) {
      var severityOption = this.options['severity-' + severity];
      if (severityOption) {
        return this._setupItem($('<div>')
          .data('severity', severity)
          .addClass('notification-queue-item')
          .addClass(severityOption['class'])
          .append($('<span>').html(message))
          .append($('<a>').addClass('notification-queue-item-close').attr('href', 'javascript:void(null)'))
          .appendTo($(this.element).find('.notification-queue-items')));
      } else {
        throw new Error("Severity " + severity + " is undefined");
      }
    },
    
    remove: function (item) {
      this._hide($(item));
    },
    
    _hide : function(element){
      element.hide(this.options.hide.effect, this.options.hide.options);
    },
    
    _setupItem: function (item) {
      var severityOption = this.options['severity-' + $(item).data('severity')];
      if (severityOption && severityOption.timeout) {
        setTimeout($.proxy(function () {
          $(item).hide(this.options.hide.effect, this.options.hide.options);
        }, this), severityOption.timeout);
      } 
      
      $(item).find('a.notification-queue-item-close').click($.proxy(this._onRemoveClick, this));
      
      return $(item);
    },
    
    _onRemoveClick: function (event, data) {
      this._hide($(event.target).closest('.notification-queue-item'));
    },
    
    _destroy : function() {
      
    }
  });
  
  $(document).ready(function() {
    $('.notification-queue').notificationQueue();
  });
  
}).call(this);