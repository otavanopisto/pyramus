<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/resources/searchresourcesdialog.js">
    </script>

  </head>
  <body onload="onLoad(event);">

    <div id="searchResourcesDialogSearchContainer" class="modalSearchContainer">
      <div class="modalSearchTabLabel"><fmt:message key="resources.searchResourcesDialog.searchTitle"/></div> 
      <div class="modalSearchTabContent">
	    <div class="genericFormContainer"> 
          <form id="searchResourcesForm" method="post" onsubmit="onSearchResources(event);">
            <div class="genericFormSection columnLeft">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResourcesDialog.nameTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResourcesDialog.nameHelp"/>
              </jsp:include>          
              <input type="text" name="name" size="35"/>
            </div>
          
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResourcesDialog.resourceTypeTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResourcesDialog.resourceTypeHelp"/>
              </jsp:include>          
              <div class="searchResourcesResourceTypeContainer">
                <select name="resourceType">
                  <option></option>
                  <c:forEach var="resourceType" items="${resourceTypes}">
                    <option value="${resourceType}"><fmt:message key="resources.searchResourcesDialog.resourceType_${resourceType}"/></option>
                  </c:forEach>
                </select>
              </div>
            </div>
      
            <div class="columnClear">
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="resources.searchResourcesDialog.resourceCategoryTitle"/>
                  <jsp:param name="helpLocale" value="resources.searchResourcesDialog.resourceCategoryHelp"/>
                </jsp:include>          
                <div class="searchResourcesResourceCategoryContainer">
                  <select name="resourceCategory">
                    <option></option>
                    <c:forEach var="resourceCategory" items="${resourceCategories}">
                      <option value="${resourceCategory.id}">${resourceCategory.name}</option>
                    </c:forEach>
                  </select>
                </div>
              </div>
            </div>
  
	          <div class="genericFormSubmitSection">
	            <input type="submit" value="<fmt:message key="resources.searchResourcesDialog.searchButton"/>"/>
	          </div>
	    
	        </form>
	      </div>
      </div>
      
      <div id="searchResultsContainer" class="modalSearchResultsContainer">
        <div class="modalSearchResultsTabLabel"><fmt:message key="resources.searchResourcesDialog.searchResultsTitle"/></div>
        <div id="modalSearchResultsStatusMessageContainer" class="modalSearchResultsMessageContainer"></div>    
        <div id="searchResultsTableContainer" class="modalSearchResultsTabContent"></div>
        <div id="modalSearchResultsPagesContainer" class="modalSearchResultsPagesContainer"></div>
      </div>
      
    </div>
    
    <div id="resourcesContainer" class="modalSelectedItemsContainer">
      <div class="modalSelectedItemsTabLabel"><fmt:message key="resources.searchResourcesDialog.selectedResourcesTitle"/></div>
      <div id="resourcesTableContainer" class="modalSelectedItemsTabContent"></div>
    </div>

  </body>
</html>