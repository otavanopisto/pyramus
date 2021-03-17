<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="settings.editWorklistTemplate.pageTitle"/></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
  </head>
  <body onload="new IxProtoTabs($('tabs'));" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="settings.editWorklistTemplate.pageTitle"/></h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">
          <fmt:message key="settings.editWorklistTemplate.tabLabelBasic"/>
        </a>
      </div>
      
      <form action="editworklisttemplate.json" method="post" ix:jsonform="true" ix:useglasspane="true">

        <input type="hidden" name="templateId" value="${worklistTemplate.id}"></input>
  
        <div id="basic" class="tabContent">
          
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editWorklistTemplate.typeTitle"/>
            </jsp:include> 
            <select name="templateType" class="required">
              <option value="DEFAULT" <c:if test="${worklistTemplate.templateType eq 'DEFAULT'}">selected="selected"</c:if>><fmt:message key="settings.editWorklistTemplate.defaultType"/></option>
              <option value="COURSE_ASSESSMENT" <c:if test="${worklistTemplate.templateType eq 'COURSE_ASSESSMENT'}">selected="selected"</c:if>><fmt:message key="settings.editWorklistTemplate.assessmentType"/></option>
              <option value="GRADE_RAISE" <c:if test="${worklistTemplate.templateType eq 'GRADE_RAISE'}">selected="selected"</c:if>><fmt:message key="settings.editWorklistTemplate.gradeType"/></option>
            </select>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editWorklistTemplate.descriptionTitle"/>
            </jsp:include> 
            <input type="text" name="description" class="required" size="60" value="${fn:escapeXml(worklistTemplate.description)}"/>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editWorklistTemplate.priceTitle"/>
            </jsp:include> 
            <input type="text" name="price" class="required" size="20" value="${fn:escapeXml(worklistTemplate.price)}"/>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editWorklistTemplate.factorTitle"/>
            </jsp:include> 
            <input type="text" name="factor" class="required" size="20" value="${fn:escapeXml(worklistTemplate.factor)}"/>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editWorklistTemplate.rightsTitle"/>
            </jsp:include> 
            <input type="checkbox" name="dateEditable" value="1" <c:if test="${dateEditable}">checked="checked"</c:if>/><fmt:message key="settings.editWorklistTemplate.dateEditable"/><br/>
            <input type="checkbox" name="descriptionEditable" value="1" <c:if test="${descriptionEditable}">checked="checked"</c:if>/><fmt:message key="settings.editWorklistTemplate.descriptionEditable"/><br/>
            <input type="checkbox" name="priceEditable" value="1" <c:if test="${priceEditable}">checked="checked"</c:if>/><fmt:message key="settings.editWorklistTemplate.priceEditable"/><br/>
            <input type="checkbox" name="factorEditable" value="1" <c:if test="${factorEditable}">checked="checked"</c:if>/><fmt:message key="settings.editWorklistTemplate.factorEditable"/><br/>
            <input type="checkbox" name="removable" value="1" <c:if test="${worklistTemplate.removable}">checked="checked"</c:if>/><fmt:message key="settings.editWorklistTemplate.removable"/><br/>
          </div>
        
        </div>
        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="<fmt:message key="settings.editWorklistTemplate.saveButton"/>">
        </div>
      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>