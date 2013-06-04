<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
	  <title><fmt:message key="resources.searchResources.pageTitle"/></title>

	  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
	  
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/resources/searchresources.js">
    </script>
  
  </head> 
  <body onload="onLoad(event);">
	  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
	  
	  <h1 class="genericPageHeader"><fmt:message key="resources.searchResources.pageTitle" /></h1>
	  
	  <div id="searchResourcesSearchFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#basic">
            <fmt:message key="resources.searchResources.tabLabelBasic"/>
          </a>
          <a class="tabLabel" href="#advanced">
            <fmt:message key="resources.searchResources.tabLabelAdvanced"/>
          </a>
        </div>
        
        <form method="post" id="searchForm" onsubmit="onSearchResources(event);">
          <input type="hidden" name="activeTab" id="activeTab" value="basic"/>
        
          <div id="basic" class="tabContent">
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResources.basicQueryTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResources.basicQueryTitle"/>
              </jsp:include>                
              <input type="text" name="simpleQuery" class="basicSearchQueryField" size="40">
            </div>
      
            <div class="genericFormSubmitSection">
              <input type="submit" name="query" value="<fmt:message key="resources.searchResources.searchButton"/>">
            </div>
          </div>

          <div id="advanced" class="tabContent">
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResources.advancedSearchNameTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResources.advancedSearchNameHelp"/>
              </jsp:include>          
              <input type="text" name="name" size="40"/>
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResources.advancedSearchTagsTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResources.advancedSearchTagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResources.advancedSearchTypeTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResources.advancedSearchTypeHelp"/>
              </jsp:include>          
              <div class="searchResourcesResourceTypeContainer">
                <select name="resourceType">
                  <option></option>
                  <c:forEach var="resourceType" items="${resourceTypes}">
                    <option value="${resourceType}"><fmt:message key="resources.searchResources.resourceType_${resourceType}"/></option>
                  </c:forEach>
                </select>
              </div>
            </div>
      
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.searchResources.advancedSearchCategoryTitle"/>
                <jsp:param name="helpLocale" value="resources.searchResources.advancedSearchCategoryHelp"/>
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
      
            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="resources.searchResources.searchButton"/>">
            </div>
		      </div>
		    </form>
	    </div>
	  </div>
	     
    <div id="searchResultsWrapper" style="display:none;">
      <div class="searchResultsTitle"><fmt:message key="resources.searchResources.resultsTitle"/></div>
      <div id="searchResultsContainer" class="searchResultsContainer">
        <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
        <div id="searchResultsTableContainer"></div>
        <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
      </div>
    </div>
	  
	  <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
	</body>
</html>


