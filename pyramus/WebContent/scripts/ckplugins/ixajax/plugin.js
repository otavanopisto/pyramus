(function() {
  var createXMLHttpRequest = function() {
    // In IE, using the native XMLHttpRequest for local files may throw
    // "Access is Denied" errors.
    if ( !CKEDITOR.env.ie || location.protocol != 'file:' )
      try { return new XMLHttpRequest(); } catch(e) {}

    try { return new ActiveXObject( 'Msxml2.XMLHTTP' ); } catch (e) {}
    try { return new ActiveXObject( 'Microsoft.XMLHTTP' ); } catch (e) {}

    return null;
  }; 
  
  CKEDITOR.plugins.add('ixajax', {
    onLoad : function() {
      /* TODO: Why ajax.js is not bundled in compressed ckeditor file ? */
      CKEDITOR.scriptLoader.load(CKEDITOR.basePath + '_source/core/ajax.js', function () {
        CKEDITOR.tools.extend(CKEDITOR.ajax, {
          post: function(url, parameters, callback, asynchronous) {
            var transport = createXMLHttpRequest();
            transport.open('POST', url, asynchronous||true);           
            // transport.setRequestHeader('Accept', 'text/javascript');
            transport.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            transport.setRequestHeader('Connection', 'Close');
            
            var head = '';
            for (var parameter in parameters) {
              if ((typeof parameter) == "string")
                head += (head.length == 0 ? '' : '&') + (parameter + '=' + parameters[parameter]);
            }
            
            if (asynchronous != true) {
              transport.onreadystatechange = function(event) {
                if (transport.readyState == 4)
                  callback(transport);
              }
            }
            
            transport.send(head);
            
            if (asynchronous == false) 
              return transport;
          }
        }, true);
      });
    },
    init : function(editor) {
      
    }
  });

})();