<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="users.login.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/message_adapter_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      };
      
      function toggleLogin() {
        $('createGuardianCredentialsContainer').hide();
        $('loginGuardianCredentialsContainer').show();
        $('parentRegisterCredentialType').setValue('LOGIN');
      }
    </script>
    
  </head>
  <body onload="onLoad(event);">
    <div>${hash}</div>
    
    <h1><fmt:message key="studentparents.parentRegistration.pageTitle"/></h1>

    <c:choose>
      <c:when test="${invalidLogin}">
        <p>
          <fmt:message key="studentparents.parentRegistration.invalidLoginMessage"/>
        </p>
      </c:when>
      <c:otherwise>
        <form action="parentregister.json" method="post" ix:jsonform="true">
          <input type="hidden" name="hash" value="${hash}"/>
    
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="studentparents.parentRegistration.ssecConfirmationTitle"/>
              <jsp:param name="helpLocale" value="studentparents.parentRegistration.ssecConfirmationHelp"/>
            </jsp:include>
            <input type="input" name="ssn-confirm" autocomplete="new-ssn-confirm" required="required" size="25">
          </div>
    
          <p>
            <c:choose>
              <c:when test="${loggedUserId != null}">
                <input type="hidden" name="type" id="parentRegisterCredentialType" value="LOGGEDIN"/>
                
                <fmt:message key="studentparents.parentRegistration.loggedInMessagePre"/>
                <span style="font-size: 1.2em; font-weight: bold;">${loggedUserName}</span>.
                <fmt:message key="studentparents.parentRegistration.loggedInMessagePost"/>
              </c:when>
              <c:otherwise>
                <input type="hidden" name="type" id="parentRegisterCredentialType" value="CREATE"/>
                <div id="createGuardianCredentialsContainer">
                  <div>
                    <fmt:message key="studentparents.parentRegistration.alreadyRegistered"/>
                    <a href="#" onclick="toggleLogin();"><fmt:message key="studentparents.parentRegistration.alreadyRegisteredLoginLinkLabel"/></a>.
                  </div>
          
                  <div class="genericFormSection">  
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="studentparents.parentRegistration.userNameTitle"/>
                      <jsp:param name="helpLocale" value="studentparents.parentRegistration.userNameHelp"/>
                    </jsp:include>                  
                    <input type="text" name="new-username" autocomplete="new-username" size="30">
                  </div>
                      
                  <div class="genericFormSection">  
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="studentparents.parentRegistration.password1Title"/>
                      <jsp:param name="helpLocale" value="studentparents.parentRegistration.password1Help"/>
                    </jsp:include>     
                    <input type="password" name="new-password1" autocomplete="new-password" class="equals equals-new-password2" size="25">
                  </div>
          
                  <div class="genericFormSection">
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="studentparents.parentRegistration.password2Title"/>
                      <jsp:param name="helpLocale" value="studentparents.parentRegistration.password2Help"/>
                    </jsp:include>
                    <input type="password" name="new-password2" autocomplete="new-password" class="equals equals-new-password1" size="25">
                  </div>
                </div>
    
                <div id="loginGuardianCredentialsContainer" style="display: none;">
                  <div><fmt:message key="studentparents.parentRegistration.loginLabel"/></div>
                
                  <div>
                    <label for="username"><fmt:message key="studentparents.parentRegistration.userNameTitle"/></label>
                    <input type="text" name="username" required="required" size="30">
                  </div>

                  <div>
                    <label for="username"><fmt:message key="studentparents.parentRegistration.password1Title"/></label>
                    <input type="password" name="password" required="required" size="25">
                  </div>                  
                </div>
              </c:otherwise>
            </c:choose>
          </p>
    
          <div class="genericFormSubmitSection">
            <input type="submit" name="login" value="<fmt:message key="studentparents.parentRegistration.submitButtonLabel"/>">
          </div>
        </form>
      </c:otherwise>
    </c:choose>
  </body>
</html>