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
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      };
    </script>
    
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <div id="loginFormContainer"> 
      <div class="genericFormContainer" id="loginForm"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#login">
            <fmt:message key="users.login.tabLabelLogin"/>
          </a>
        </div>
        
        <div id="login" class="tabContent">
          <div class="internalLoginProviderWrapper">
            <form action="login.json" method="post" ix:jsonform="true">
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="users.login.usernameTitle"/>
                  <jsp:param name="helpLocale" value="users.login.usernameHelp"/>
                </jsp:include>     
                <input type="text" name="username" class="required" size="25"/>
              </div>
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="users.login.passwordTitle"/>
                  <jsp:param name="helpLocale" value="users.login.passwordHelp"/>
                </jsp:include>     
                <input type="password" name="password" class="required" size="25">
              </div>
              <div class="genericFormSubmitSection">
                <input type="submit" name="login" value="<fmt:message key="users.login.loginButton"/>">
              </div>
            </form>
          </div>
          
          <div class="externalLoginProviderWrapper">
          <c:forEach var="externalProvider" items="${externalProviders}">
            <div class="login-wrapper google">
              <div class="login-icon"></div>
              <div class="login-container"><a class="login-link" href="?external=${externalProvider.name}">${externalProvider.name}</a></div>
            </div>
            
          </c:forEach>
          </div>
          <div class="clear"></div>
        </div>
      </div>
    </div>

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>