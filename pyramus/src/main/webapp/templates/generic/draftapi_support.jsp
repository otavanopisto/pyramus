<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${draftAPISupportIncluded != true}">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/draftapi/draftapi.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/draftapi/draftui.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/draftapi/draft.js"></script>
    <c:set scope="request" var="draftAPISupportIncluded" value="true"/>
    
    <script type="text/javascript">
      Event.observe(window, "load", function (event) {
        var bodyElement = document.getElementsByTagName('body')[0];
        if (bodyElement.getAttribute('ix:enabledrafting') == 'true') {
          var jsonForms = bodyElement.select('form[ix:jsonform="true"]'); 
          if (jsonForms.length == 1) {
            var draftSavedContainer = new Element("div");

            jsonForms[0].insert({
              top: draftSavedContainer
            });

            initDrafting({
              draftSavedTextContainer: draftSavedContainer,
              draftSavingTextContainer: bodyElement
            });

            JSONRequest.request("drafts/retrieveformdraft.json", {
              onSuccess: function (jsonResponse) {
                if (!jsonResponse.draftDeleted) {
                  var draftModified = jsonResponse.draftModified;
                  // TODO: Date formatting
                  var date = new Date(draftModified.time);
                  var dateStr = date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear() + ' ' + date.getHours().toPaddedString(2) + ':' + date.getMinutes().toPaddedString(2);
                  var url = GLOBAL_contextPath + "/simpledialog.page?localeId=generic.draft.confirmDraftRestoreDialogContent&localeParams=" + encodeURIComponent(dateStr);

                  var dialog = new IxDialog({
                    id : 'confirmDraftRestore',
                    contentURL: url,
                    centered : true,
                    showOk : true,  
                    showCancel : true,
                    autoEvaluateSize: true,
                    title : '<fmt:message key="generic.draft.confirmDraftRestoreDialogTitle"/>',
                    okLabel : '<fmt:message key="generic.draft.confirmDraftRestoreDialogRestoreButton"/>',
                    cancelLabel : '<fmt:message key="generic.draft.confirmDraftRestoreDialogDiscardButton"/>'
                  });

                  dialog.addDialogListener(function(event) {
                    var startDrafting = false;
                    
                    switch (event.name) {
                      case 'okClick':
                        var draftData = jsonResponse.draftData;
                        DRAFTAPI.restoreFormDraft(draftData);
                        DRAFTUI.restoreDraftEnd(draftModified.time);
                        startDrafting = true;
                      break;
                      case 'cancelClick':
                        deleteFormDraft(function() {
                          redirectTo(window.location.href);
                        });
                        startDrafting = false;
                      break;
                    }

                    if (startDrafting) {
                      storeLatestDraftDataHash(DRAFTAPI.createFormDraft());
                      startDraftSaving();
                    }
                  });
                
                  dialog.open();
                } else {
                  startDraftSaving();
                }
              } 
            });

            Event.observe(document, "ix:jsonformSuccess", function (event) {
              Event.stop(event);
              __STOPDRAFTING = true;
              var jsonResponse = event.memo.jsonResponse;
              var formElement = event.memo.formElement;
              
              deleteFormDraft(function () {
                if (jsonResponse.redirectURL) {
                  redirectTo(jsonResponse.redirectURL);
                } else {
                  if (formElement._globalGlassPane) {
                    formElement._globalGlassPane.hide();
                    delete formElement._globalGlassPane;
                    formElement._globalGlassPane = undefined;
                  }
                } 
              });
            });
          } else {
            alert('Cannot initialize drafting without jsonform');
          }
        }
      });
       
    </script>
        
  </c:when>
</c:choose>