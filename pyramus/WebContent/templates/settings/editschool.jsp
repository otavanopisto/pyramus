<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="settings.editSchool.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/settings/editschool.js">
    </script>
    
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="settings.editSchool.pageTitle" /></h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">
          <fmt:message key="settings.editSchool.tabLabelBasic"/>
        </a>
      </div>
      
      <form action="editschool.json" method="post" ix:jsonform="true" ix:useglasspane="true">

        <input type="hidden" name="schoolId" value="${school.id}"></input>
  
        <div id="basic" class="tabContent">
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editSchool.codeTitle"/>
              <jsp:param name="helpLocale" value="settings.editSchool.codeHelp"/>
            </jsp:include> 
            <input type="text" name="code" class="required" size="20" value="${fn:escapeXml(school.code)}"/>
          </div>
  
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editSchool.nameTitle"/>
              <jsp:param name="helpLocale" value="settings.editSchool.nameHelp"/>
            </jsp:include> 

            <input type="text" name="name" class="required" size="40" value="${fn:escapeXml(school.name)}"/>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editSchool.fieldTitle"/>
              <jsp:param name="helpLocale" value="settings.editSchool.fieldHelp"/>
            </jsp:include> 

            <select name="schoolFieldId">
              <option value="-1"></option>
              <c:forEach var="field" items="${schoolFields}">
                <c:choose>
                  <c:when test="${field.id eq school.field.id}">
                    <option value="${field.id}" selected="selected">${fn:escapeXml(field.name)}</option>
                  </c:when>
                  <c:otherwise>
                    <option value="${field.id}">${fn:escapeXml(field.name)}</option>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              <c:if test="${school.field.archived}">
                <option value="${school.field.id}" selected="selected">${fn:escapeXml(school.field.name)} ***</option>
              </c:if>
            </select>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editSchool.tagsTitle"/>
              <jsp:param name="helpLocale" value="settings.editSchool.tagsHelp"/>
            </jsp:include>
            <input type="text" id="tags" name="tags" size="40" value="${fn:escapeXml(tags)}"/>
            <div id="tags_choices" class="autocomplete_choices"></div>
          </div>

          <div class="genericFormSection">                
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editSchool.addressesTitle"/>
              <jsp:param name="helpLocale" value="settings.editSchool.addressesHelp"/>
            </jsp:include>
            <div id="addressTable"></div>
          </div>

          <div class="genericFormSection">               
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editSchool.emailTableEmailsTitle"/>
              <jsp:param name="helpLocale" value="settings.editSchool.emailTableEmailsHelp"/>
            </jsp:include>
            <div id="emailTable"></div>
          </div>

          <div class="genericFormSection">                
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editSchool.phoneNumbersTitle"/>
              <jsp:param name="helpLocale" value="settings.editSchool.phoneNumbersHelp"/>
            </jsp:include>
            <div id="phoneTable"></div>
          </div>

          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editSchool.variablesTitle"/>
              <jsp:param name="helpLocale" value="settings.editSchool.variablesHelp"/>
            </jsp:include> 
            <div id="variablesTable"></div>
          </div>

        </div>
        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="<fmt:message key="settings.editSchool.saveButton"/>">
        </div>
      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>