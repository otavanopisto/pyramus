document.observe("dom:loaded", function(event) {
  var ckEditors = $$("textarea[ix:ckeditor='true']");
  for ( var i = 0, l = ckEditors.length; i < l; i++) {
    var formElement = ckEditors[i].form;
    if (formElement) {
      Event.observe(formElement, "submit", function (event) {
        var formElement = Event.element(event);
        var formEditors = formElement.select("textarea[ix:ckeditor='true']");
        for (var j = 0, l = formEditors.length; j < l; j++)
          CKEDITOR.instances[formEditors[j].name].updateElement();
      });
    }
    
    var options = {
      language: document.getCookie('pyramusLocale')        
    };
    var toolbar = ckEditors[i].getAttribute('ix:cktoolbar');
    if (toolbar) {
      options = Object.extend(options, {
        toolbar: toolbar
      });
    }
    
    CKEDITOR.replace(ckEditors[i].name, options);
  }
});