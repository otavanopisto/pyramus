<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="settings.createSchool.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/settings/createschool.js">
    </script>
    
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="settings.createSchool.pageTitle" /></h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">
          <fmt:message key="settings.createSchool.tabLabelBasic"/>
        </a>
      </div>
      
      <form action="createschool.json" method="post" ix:jsonform="true" ix:useglasspane="true">
        <div id="basic" class="tabContent">

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.codeTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.codeHelp"/>
          </jsp:include>           
          <input type="text" name="code" class="required" size="20"/>
        </div>

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.nameTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.nameHelp"/>
          </jsp:include>           
          <input type="text" name="name" class="required" size="40"/>
        </div>
        
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.fieldTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.fieldHelp"/>
          </jsp:include> 

          <select name="schoolFieldId">
            <option value="-1"></option>
            <c:forEach var="field" items="${schoolFields}">
              <option value="${field.id}">${fn:escapeXml(field.name)}</option>
            </c:forEach>
          </select>
        </div>

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.tagsTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.tagsHelp"/>
          </jsp:include>
          <input type="text" id="tags" name="tags" size="40"/>
          <div id="tags_choices" class="autocomplete_choices"></div>
        </div>

        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.addressesTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.addressesHelp"/>
          </jsp:include>                                         
          <div id="addressTable"></div>
        </div>

        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.emailTableEmailsTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.emailTableEmailsHelp"/>
          </jsp:include>                                         
          <div id="emailTable"></div>
        </div>

        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.phoneNumbersTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.phoneNumbersHelp"/>
          </jsp:include>                                         
          <div id="phoneTable"></div>
        </div>

        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.variablesTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.variablesHelp"/>
          </jsp:include>           
          <div id="variablesTable"></div>
        </div>

      </div>
      <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" value="<fmt:message key="settings.createSchool.saveButton"/>">
      </div>
    </form>
  </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>