<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="settings.reportCategories.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/settings/reportcategories.js">
    </script>
  </head>
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="settings.reportCategories.pageTitle"/></h1>
    
    <div id="manageReportCategoriesFormContainer"> 
	    <div class="genericFormContainer"> 
	      <form action="savereportcategories.json" method="post" ix:jsonform="true" ix:useglasspane="true">
	  
	        <div class="tabLabelsContainer" id="tabs">
	          <a class="tabLabel" href="#manageReportCategories">
	            <fmt:message key="settings.reportCategories.tabLabelReportCategories"/>
	          </a>
	        </div>
	        
            <div id="manageReportCategories" class="tabContentixTableFormattedData">
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" onclick="addReportCategoriesTableRow();"><fmt:message key="settings.reportCategories.addReportCategoryLink"/></span>
              </div>

              <div id="noReportCategoriesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
                <span>
                 <fmt:message key="settings.reportCategories.noReportCategoriesAddedPrefix"/> <span onclick="addReportCategoriesTableRow();" class="genericTableAddRowLink"> <fmt:message key="settings.reportCategories.noReportCategoriesAddedClickHereLink"/></span>.</span>
              </div>

              <div id="reportCategoriesTableContainer"></div>
            </div>
  	  
            <div class="genericFormSubmitSectionOffTab">
              <input type="submit" class="formvalid" value="<fmt:message key="settings.reportCategories.saveButton"/>">
            </div>

	      </form>
	    </div>
	  </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>