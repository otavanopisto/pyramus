(function() {
  'use strict';

  document.observe("dom:loaded", function() {
    if (window.messages && window.messages.length) {
      for (var i = 0, l = window.messages.length; i < l; i++) {
        var message = window.messages[i];
        var errorMessage = encodeURIComponent(message.message);
        var contentURL = GLOBAL_contextPath + '/jsonerror.page?errorCode=-1&isHttpError=false&errorLevel=4&errorMessage=' + errorMessage;
        
        var dialog = new IxDialog({
          id: 'jsonErrorDialog',
          contentURL: contentURL,
          centered : true,
          showOk : true,
          autoEvaluateSize: true,
          title : getLocale().getText("generic.jsonErrorDialog.dialogTitle"),
          okLabel : getLocale().getText("generic.jsonErrorDialog.dialogOkLabel")
        });
        
        dialog.open();
      }
    }  
  });

                  
}).call(this);