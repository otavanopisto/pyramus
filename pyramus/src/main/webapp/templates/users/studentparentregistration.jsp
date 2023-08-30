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
    </script>
    
  </head>
  <body onload="onLoad(event);">
    
    <h1>Luo huoltajatunnukset</h1>
    <div>${hash}</div>
    
    <c:choose>
      <c:when test="${valid}">
        <form action="parentregister.json" method="post" ix:jsonform="true">
          <input type="hidden" name="hash" value="${hash}"/>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="students.editStudent.ssecIdTitle"/>
              <jsp:param name="helpLocale" value="students.editStudent.ssecIdHelp"/>
            </jsp:include>
            <input type="input" name="ssn-confirm" autocomplete="new-password" size="25">
          </div>

          <div id="editUserCredentialsContainer">
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.usernameTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.usernameHelp"/>
              </jsp:include>                  
              <input type="text" name="username" autocomplete="new-username" size="30">
            </div>
                
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.password1Title"/>
                <jsp:param name="helpLocale" value="students.editStudent.password1Help"/>
              </jsp:include>     
              <input type="password" name="password1" autocomplete="new-password" class="equals equals-password2" size="25">
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.password2Title"/>
                <jsp:param name="helpLocale" value="students.editStudent.password2Help"/>
              </jsp:include>
              <input type="password" name="password2" autocomplete="new-password" class="equals equals-password1" size="25">
            </div>
          </div>

          <div class="genericFormSubmitSection">
            <input type="submit" name="login" value="<fmt:message key="users.login.loginButton"/>">
          </div>
        </form>    
      </c:when>
      
      <c:otherwise>
        <div>Rekisteröitymislinkki ei ole enää voimassa tai se on virheellinen. Ota yhteyttä tai jotain.</div>
      </c:otherwise>
    </c:choose>
    
  </body>
</html>