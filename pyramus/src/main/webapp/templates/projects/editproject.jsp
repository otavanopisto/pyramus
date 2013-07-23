<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="projects.editProject.pageTitle">
        <fmt:param value="${project.name}"/>
      </fmt:message>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/datefield_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/projects/editproject.js">
    </script>
  </head>
  <body onLoad="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">
      <fmt:message key="projects.editProject.pageTitle">
        <fmt:param value="${project.name}"/>
      </fmt:message>
    </h1>
    
    <form id="projectForm" action="editproject.json" method="post" ix:jsonform="true" ix:useglasspane="true">
      <input type="hidden" name="version" value="${project.version}"/>
      
      <div id="editProjectEditFormContainer"> 
        <div class="genericFormContainer"> 
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="projects.editProject.tabLabelBasic"/>
            </a>
            <a class="tabLabel" href="#modules">
              <fmt:message key="projects.editProject.tabLabelModules"/>
            </a>
          </div>

          <!--  Basic tab -->

          <div id="basic" class="tabContent">
            <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>

            <input type="hidden" name="project" value="${project.id}"/>
            
            <!--  TODO italic tags to css -->

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editProject.createdTitle"/>
                <jsp:param name="helpLocale" value="projects.editProject.createdHelp"/>
              </jsp:include>
              <span><i>${project.creator.fullName} <fmt:formatDate type="both" value="${project.created}"/></i></span>    
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editProject.modifiedTitle"/>
                <jsp:param name="helpLocale" value="projects.editProject.modifiedHelp"/>
              </jsp:include>
              <span><i>${project.lastModifier.fullName} <fmt:formatDate type="both" value="${project.lastModified}"/></i></span>    
            </div>

            <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="projects.editProject.nameTitle"/>
                  <jsp:param name="helpLocale" value="projects.editProject.nameHelp"/>
                </jsp:include>
              <input type="text" class="required" name="name" value="${fn:escapeXml(project.name)}" size="40"/>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editProject.tagsTitle"/>
                <jsp:param name="helpLocale" value="projects.editProject.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40" value="${fn:escapeXml(tags)}"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
        
            <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="projects.editProject.descriptionTitle"/>
                  <jsp:param name="helpLocale" value="projects.editProject.descriptionHelp"/>
                </jsp:include>
              <textarea ix:cktoolbar="projectDescription" name="description" ix:ckeditor="true">${project.description}</textarea>
            </div>
  
            <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="projects.editProject.optionalStudiesTitle"/>
                  <jsp:param name="helpLocale" value="projects.editProject.optionalStudiesHelp"/>
                </jsp:include>
              <input type="text" name="optionalStudiesLength" class="required" value="${project.optionalStudiesLength.units}" size="15"/>
              <select name="optionalStudiesLengthTimeUnit">           
                <c:forEach var="optionalStudiesLengthTimeUnit" items="${optionalStudiesLengthTimeUnits}">
                  <option value="${optionalStudiesLengthTimeUnit.id}" <c:if test="${project.optionalStudiesLength.unit.id == optionalStudiesLengthTimeUnit.id}">selected="selected"</c:if>>${optionalStudiesLengthTimeUnit.name}</option> 
                </c:forEach>
              </select>
            </div>
          </div>

         <!--  Modules tab -->
          
          <div id="modules" class="tabContentixTableFormattedData">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="openSearchModulesDialog();"><fmt:message key="projects.editProject.addModuleLink"/></span>
            </div>
            
            <div id="noModulesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="projects.editProject.noModulesAddedPreFix"/> <span onclick="openSearchModulesDialog();" class="genericTableAddRowLink"><fmt:message key="projects.editProject.noModulesAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="modulesContainer">
              <div id="modulesTableContainer"></div>
            </div>

            <div id="editProjectModulesTotalContainer">
              <fmt:message key="projects.editProject.modulesTotal"/> <span id="editProjectModulesTotalValue"></span>
            </div>

          </div>

        </div>
      </div>
      
      <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" value="<fmt:message key="projects.editProject.saveButton"/>">
      </div>

    </form>
  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>