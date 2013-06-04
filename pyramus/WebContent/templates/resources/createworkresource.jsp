<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="resources.createWorkResource.pageTitle"/></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/resources/setuptags.js">
    </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/resources/createworkresource.js">
    </script>
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="resources.createWorkResource.pageTitle" /></h1>
    
    <div id="createWorkResourceCreateFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#createWorkResource">
            <fmt:message key="resources.createWorkResource.tabLabelCreateWorkResource"/>
          </a>
        </div>
        
        <form action="createworkresource.json" method="post" ix:jsonform="true">
          <div id="createWorkResource" class="tabContent">
	        <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="resources.createWorkResource.nameTitle"/>
              <jsp:param name="helpLocale" value="resources.createWorkResource.nameHelp"/>
            </jsp:include>
  		      <input type="text" class="required" name="name" size="40" value="${fn:escapeXml(name)}"/>
		  	  </div>
            
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="resources.createWorkResource.tagsTitle"/>
              <jsp:param name="helpLocale" value="resources.createWorkResource.tagsHelp"/>
            </jsp:include>
            <input type="text" id="tags" name="tags" size="40"/>
            <div id="tags_choices" class="autocomplete_choices"></div>
          </div>
        
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="resources.createWorkResource.categoryTitle"/>
              <jsp:param name="helpLocale" value="resources.createWorkResource.categoryHelp"/>
            </jsp:include>
            <select name="category">           
              <c:forEach var="category" items="${categories}">
                <option value="${category.id}"> ${category.name}  </option> 
              </c:forEach>
            </select>
          </div>
		        
	        <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.createWorkResource.hourlyCostTitle"/>
                <jsp:param name="helpLocale" value="resources.createWorkResource.hourlyCostHelp"/>
              </jsp:include>
	          <input type="text" name="hourlyCost" value="0" class="numberField" size="15"/>
	        </div>
	        
	        <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.createWorkResource.costPerUseTitle"/>
                <jsp:param name="helpLocale" value="resources.createWorkResource.costPerUseHelp"/>
              </jsp:include>
	          <input type="text" name="costPerUse" value="0" class="numberField" size="15"/>
	        </div>
		    </div>
		    <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" name="createlengthunit" value="<fmt:message key="resources.createWorkResource.saveButton"/>">
          </div>
        </form>
	  </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>