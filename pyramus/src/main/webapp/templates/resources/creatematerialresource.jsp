<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="resources.createMaterialResource.pageTitle"/></title>
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
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/resources/creatematerialresource.js">
    </script>
    
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="resources.createMaterialResource.pageTitle" /></h1>
  
    <div id="createMaterialResourceCreateFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#createMaterialResource">
            <fmt:message key="resources.createMaterialResource.tabLabetCreateMaterialResource"/>
          </a>
        </div>
        
        <form action="creatematerialresource.json" method="post" ix:jsonform="true">
          <div id="createMaterialResource" class="tabContent">
		        <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="resources.createMaterialResource.nameTitle"/>
                    <jsp:param name="helpLocale" value="resources.createMaterialResource.nameHelp"/>
                  </jsp:include>
		  		    <input type="text" class="required" name="name" size="40"/>
		  	    </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="resources.createMaterialResource.tagsTitle"/>
                <jsp:param name="helpLocale" value="resources.createMaterialResource.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
		        
		        <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="resources.createMaterialResource.categoryTitle"/>
                    <jsp:param name="helpLocale" value="resources.createMaterialResource.categoryHelp"/>
                  </jsp:include>
		          <select name="category">           
		            <c:forEach var="category" items="${categories}">
		              <option value="${category.id}"> ${category.name}  </option> 
		            </c:forEach>
		          </select>
		        </div>
		        
		        <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="resources.createMaterialResource.unitCostTitle"/>
                    <jsp:param name="helpLocale" value="resources.createMaterialResource.unitCostHelp"/>
                  </jsp:include>
		          <input type="text" name="unitCost" value="0" class="numberField" size="15"/>
		        </div>
			    </div>
			    <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="resources.createMaterialResource.saveButton"/>">
          </div>
			  </form>
			</div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>