<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="settings.municipalities.pageTitle"/></title>

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

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/settings/municipalities.js">
    </script>
  </head>
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="settings.municipalities.pageTitle"/></h1>
    
    <div id="manageMunicipalitiesFormContainer"> 
	    <div class="genericFormContainer"> 
	      <form action="savemunicipalities.json" method="post" ix:jsonform="true" ix:useglasspane="true">
	  
	        <div class="tabLabelsContainer" id="tabs">
	          <a class="tabLabel" href="#manageMunicipalities">
	            <fmt:message key="settings.municipalities.tabLabelMunicipalities"/>
	          </a>
	        </div>
	        
          <div id="manageMunicipalities" class="tabContentixTableFormattedData">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addMunicipalityTableRow();"><fmt:message key="settings.municipalities.addMunicipalityLink"/></span>
            </div>
              
            <div id="noMunicipalitiesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="settings.municipalities.noMunicipalitiesAddedPreFix"/> <span onclick="addMunicipalityTableRow();" class="genericTableAddRowLink"><fmt:message key="settings.municipalities.noMunicipalitiesAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="municipalitiesTable"></div>
          </div>
	  
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="settings.municipalities.saveButton"/>">
          </div>

	      </form>
	    </div>
	  </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>