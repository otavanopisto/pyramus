<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/glasspane_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${jsonformSupportIncluded != true}">
    <script type="text/javascript">
      function initializeJSONForms() {
        var forms = $$("form[ix:jsonform='true']");
        for (var i = 0; i < forms.length; i++) {
          var form = forms[i];
          Event.observe(form, "submit", function (event) {
            Event.stop(event);
            var formElement = Event.element(event);
            if (formElement.getAttribute('ix:useglasspane') == 'true') {
              formElement._globalGlassPane = new IxGlassPane(document.body, { });
              formElement._globalGlassPane.show();
            }
            
            var formData = formElement.serialize(true);

            var url = '';
            var formAction = formElement.getAttribute("action");
            if (formAction[0] == '/') {
              url = formAction.substr(1); 
            }
            else {
              var browserURL = window.location.pathname.substring(GLOBAL_contextPath.length + 1);
              var tokenizedURL = browserURL.split('/');
              tokenizedURL.pop(); // remove the current page

              // tokenizedURL.shift(); // remove the beginning slash (/)
              // tokenizedURL.shift(); // remove the application name (pyramus)

              if (tokenizedURL.length > 0) {
                for (var j = 0; j < tokenizedURL.length; j++) {
                  url += tokenizedURL[j] + '/';
                }
              } 

              url = url + formAction;
            }
            
            document.fire("ix:jsonformPost");

            var refererAnchor = ''; 
            if (window.location.hash && window.location.hash.length > 1)
              refererAnchor = window.location.hash.substring(1);
            
            JSONRequest.request(url, {
              parameters: Object.extend(formData||{}, {
                __refererAnchor: refererAnchor
              }),
              onSuccess: function(jsonResponse) {
                var formSuccessEvent = Event.fire(document, "ix:jsonformSuccess", {
                  jsonResponse: jsonResponse,
                  formElement: formElement
                });
                
                if (!formSuccessEvent.stopped) {
                  if (jsonResponse.redirectURL) {
                    redirectTo(jsonResponse.redirectURL);
                  } else {
                    if (formElement._globalGlassPane) {
                      formElement._globalGlassPane.hide();
                      delete formElement._globalGlassPane;
                      formElement._globalGlassPane = undefined;
                    }
                  }
                }
              },
              onFailure: function(errorMessage, errorCode, isHttpError, jsonResponse) {
                document.fire("ix:jsonformFailure");
                
                if (formElement._globalGlassPane) {
                  formElement._globalGlassPane.hide();
                  delete formElement._globalGlassPane;
                  formElement._globalGlassPane = undefined;
                }
                
                try { 
                  // Some browsers encode URL:s as UTF-8; some as Latin-1. Make sure the encoding is consistent.
                  // Also, escape the /, :, & etc characters in error messages.
                  errorMessage = escape(errorMessage);
                  var contentURL = GLOBAL_contextPath + '/jsonerror.page?errorCode=' + errorCode + "&isHttpError=" + (isHttpError ? 1 : 0) + "&errorMessage=" + errorMessage;
      
                  if (!isHttpError)
                    contentURL += "&errorLevel=" + jsonResponse.errorLevel;
                  else
                    contentURL += "&errorLevel=4";
      
                  var dialog = new IxDialog({
                    id: 'jsonErrorDialog',
                    contentURL: contentURL,
                    centered : true,
                    showOk : true,
                    autoEvaluateSize: true,
                    title : '<fmt:message key="generic.jsonErrorDialog.dialogTitle"/>',
                    okLabel : '<fmt:message key="generic.jsonErrorDialog.dialogOkLabel"/>'
                  });
                  
                  dialog.open();
                } catch (e) {
                  alert(e);
                }
              }
            });
          });
        }
      }
      
      Event.observe(window, "load", function (event) {
        initializeJSONForms();
      });
    </script>
    
    <c:set scope="request" var="jsonformSupportIncluded" value="true"/>
  </c:when>
</c:choose>