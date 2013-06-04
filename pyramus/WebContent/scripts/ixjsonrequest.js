/** @class A JSON request object. Usage:
 * <pre>
 * JSONRequest.request("json/handler.json", 
 *     {onComplete: function (transport) {
 *         alert("Request Completed!");
 *     }
 * });
 * </pre>
 */
JSONRequest = {
  /** Create and send a new JSON request.
   * 
   * @param requestHandler The URL of the request handler.
   * @param options An object containing the following properties:
   * <dl>
   *   <dt><code>parameters</code></dt>
   *   <dd>Parameters hash passed to Ajax.Request.</dd>
   *   <dt><code>method</code></dt>
   *   <dd>HTTP verb used in request, defaults to "post"</dd>
   *   <dt><code>onComplete</code></dt>
   *   <dd>An event handler (<code>function (transport) {}</code>)
   *   invoked when the request completes.</dd>
   */
  request: function (requestHandler, options) {
  
    var opts = options ? options : {};
    var params = $H(opts.parameters ? opts.parameters : {});
    
    var paramsKeys = params.keys();
    for (var i = 0, l = paramsKeys.length; i < l; i++) {
      var key = paramsKeys[i];
      var value = params.get(key);
      if ((typeof value) == 'object')
        params.set(key, value.toString());
    }
    
    new Ajax.Request(GLOBAL_contextPath + '/' + requestHandler, {  
      method: opts.method ? opts.method : 'post',
      parameters: params,
      onSuccess: function(transport){
        if (opts.onComplete) {
          opts.onComplete(transport);
        }
      
        try {
          var jsonResponse = transport.responseText.evalJSON();
          if (jsonResponse.statusCode == 0) { 
            if (opts.onSuccess) {
              opts.onSuccess(jsonResponse);
            }
          }
          else {
            var errorMessage = "";
            
            for (var i = 0, l = jsonResponse.messages.length; i < l; i++) {
              if (errorMessage != "")
                errorMessage += "\n";
              
              errorMessage += jsonResponse.messages[i].message;
            }
            
            JSONRequest._handleError(opts.onFailure, errorMessage, jsonResponse.statusCode, false, jsonResponse);
          }
        }
        catch (e) {
          JSONRequest._handleError(opts.onFailure, e, -1, false, jsonResponse);
        }
      },
      onFailure: function(transport) {
        if (opts.onComplete) {
          opts.onComplete(transport);
        }
        
        JSONRequest._handleError(opts.onFailure, transport.responseText, transport.status, true, undefined);
      },
      asynchronous: opts.asynchronous === false ? false : true        
    });
  },
  _handleError: function (customErrorHandler, errorMessage, errorCode, isHttpError, jsonResponse) {
    if (customErrorHandler) {
      customErrorHandler.call(window, errorMessage, errorCode, isHttpError, jsonResponse);
    }
    else {
      alert(errorMessage);
    }
  }
};