<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="resources.editWorkResource.pageTitle"/></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/resources/setuptags.js">
    </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/resources/editworkresource.js">
    </script>
  </head>
  <body onload="onLoad(event)" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="resources.editWorkResource.pageTitle" /></h1>
    
    <form action="editworkresource.json" method="post" ix:jsonform="true">
      <div id="editWorkResourceEditFormContainer">
  	    <div class="genericFormContainer"> 
	  	  
	        <input type="hidden" name="resource" value="${resource.id}"/>
	        <input type="hidden" name="version" value="${resource.version}"/>
	      
	        <div class="tabLabelsContainer" id="tabs">
	          <a class="tabLabel" href="#editWorkResource">
	            <fmt:message key="resources.editWorkResource.tabLabelEditWorkResource"/>
	          </a>
	        </div>
	      
	        <div id="editWorkResource" class="tabContent">
			      <div class="genericFormSection">
	            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
	              <jsp:param name="titleLocale" value="resources.editWorkResource.nameTitle"/>
	              <jsp:param name="helpLocale" value="resources.editWorkResource.nameHelp"/>
	            </jsp:include>          
	     		    <input type="text" class="required" name="name" value="${fn:escapeXml(resource.name)}" size="40"/>
		 	      </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.editWorkResource.tagsTitle"/>
                <jsp:param name="helpLocale" value="resources.editWorkResource.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" value="${fn:escapeXml(tags)}" size="40"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
		        
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.editWorkResource.categoryTitle"/>
                <jsp:param name="helpLocale" value="resources.editWorkResource.categoryHelp"/>
              </jsp:include>          
              <select name="category">           
                <c:forEach var="category" items="${categories}">
                  <c:choose>
                    <c:when test="${category.id eq resource.category.id}">
                      <option value="${category.id}" selected="selected">${category.name}</option> 
                    </c:when>
                    <c:otherwise>
                      <option value="${category.id}">${category.name}</option> 
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </div>
              
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.editWorkResource.hourlyCostTitle"/>
                <jsp:param name="helpLocale" value="resources.editWorkResource.hourlyCostHelp"/>
              </jsp:include>          
              <input type="text" name="hourlyCost" value="${resource.hourlyCost.amount}" class="numberField" size="15"/>
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.editWorkResource.costPerUseTitle"/>
                <jsp:param name="helpLocale" value="resources.editWorkResource.costPerUseHelp"/>
              </jsp:include>          
              <input type="text" name="costPerUse" value="${resource.costPerUse.amount}" class="numberField" size="15"/>
            </div>
          </div>
  	    </div>
	    </div>
	    <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" value="<fmt:message key="resources.editWorkResource.saveButton"/>">
      </div>
    </form>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>