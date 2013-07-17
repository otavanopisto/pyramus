<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="settings.editGradingScale.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/settings/editgradingscale.js">
    </script>
  
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="settings.editGradingScale.pageTitle"/></h1>
    
    <form action="editgradingscale.json" method="post" ix:jsonform="true">
      <div id="editGradingScaleEditFormContainer">
  	    <div class="genericFormContainer"> 
	  	  <input type="hidden" name="gradingScaleId" value="${gradingScale.id}"/>
	        
	      <div class="tabLabelsContainer" id="tabs">
	        <a class="tabLabel" href="#basic">
              <fmt:message key="settings.editGradingScale.basicTabTitle"/>
            </a>
            <a class="tabLabel" href="#grades">
              <fmt:message key="settings.editGradingScale.gradesTabTitle"/>
            </a>
          </div>
	  
          <div id="basic" class="tabContent">
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="settings.editGradingScale.nameTitle"/>
                <jsp:param name="helpLocale" value="settings.editGradingScale.nameHelp"/>
              </jsp:include> 
              <input type="text" name="name" class="required" value="${fn:escapeXml(gradingScale.name)}" size="40"/>
            </div>
            
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="settings.editGradingScale.descriptionTitle"/>
                <jsp:param name="helpLocale" value="settings.editGradingScale.descriptionHelp"/>
              </jsp:include> 
            </div>
            <textarea ix:cktoolbar="gradingScaleDescription" name="description" ix:ckeditor="true">${gradingScale.description}</textarea>
          </div>
        </div>
	  
        <div id="grades" class="tabContentixTableFormattedData">
          <div class="genericTableAddRowContainer">
            <span class="genericTableAddRowLinkContainer" onclick="addGrade();"><fmt:message key="settings.editGradingScale.addGradeLink"/></span>
          </div>
              
          <div id="noGradesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
            <span><fmt:message key="settings.editGradingScale.noGradesAddedPreFix"/> <span onclick="addGrade();" class="genericTableAddRowLink"><fmt:message key="settings.editGradingScale.noGradesAddedClickHereLink"/></span>.</span>
          </div>
          <div id="gradesTableContainer"></div>
        </div>
	  
  	  </div>
    
	  <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" name="editgradingscale" value="<fmt:message key="settings.editGradingScale.saveButton"/>">
      </div>
	</form>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>