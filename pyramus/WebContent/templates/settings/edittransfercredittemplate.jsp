<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="settings.editTransferCreditTemplate.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/settings/edittransfercredittemplate.js">
    </script>
  </head>
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="settings.editTransferCreditTemplate.pageTitle"/></h1>
    
    <div id="editTransferCreditTemplateFormContainer"> 
	    <div class="genericFormContainer"> 
	      <form action="edittransfercredittemplate.json" method="post" ix:jsonform="true" ix:useglasspane="true">
	        <input type="hidden" value="${transferCreditTemplate.id}" name="transferCreditTemplateId"/>
	  
	        <div class="tabLabelsContainer" id="tabs">
	          <a class="tabLabel" href="#transferCreditTemplate">
	            <fmt:message key="settings.editTransferCreditTemplate.tabLabelTransferCreditTemplate"/>
	          </a>
	        </div>
          
          <div id="transferCreditTemplate" class="tabContentixTableFormattedData">
          
	          <div class="genericFormSection">
	            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
	              <jsp:param name="titleLocale" value="settings.editTransferCreditTemplate.nameTitle"/>
	              <jsp:param name="helpLocale" value="settings.editTransferCreditTemplate.nameHelp"/>
	            </jsp:include>
	                    
                <input type="text" name="name" class="required" value="${fn:escapeXml(transferCreditTemplate.name)}" size="40">
	          </div>
          
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addCoursesTableRow();"><fmt:message key="settings.editTransferCreditTemplate.addCourseLink"/></span>
            </div>
              
            <div id="noCoursesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="settings.editTransferCreditTemplate.noCoursesAddedPreFix"/> <span onclick="addCoursesTableRow();" class="genericTableAddRowLink"><fmt:message key="settings.editTransferCreditTemplate.noCoursesAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="coursesTable"></div>
          </div>
	  
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="settings.editTransferCreditTemplate.saveButton"/>">
          </div>

	      </form>
	    </div>
	  </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>