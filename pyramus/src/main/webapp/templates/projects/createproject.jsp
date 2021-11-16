<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="projects.createProject.pageTitle" /></title>
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
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/projects/createproject.js">
    </script>
  </head>
  <body onLoad="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="projects.createProject.pageTitle" /></h1>
    
    <form id="projectForm" action="createproject.json" method="post" ix:jsonform="true" ix:useglasspane="true">
      <div id="createProjectCreateFormContainer"> 
        <div class="genericFormContainer"> 
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="projects.createProject.tabLabelBasic"/>
            </a>
            <a class="tabLabel" href="#modules">
              <fmt:message key="projects.createProject.tabLabelModules"/>
            </a>
          </div>
          
          <!--  Basic tab -->

          <div id="basic" class="tabContent">
            <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="projects.createProject.nameTitle"/>
                  <jsp:param name="helpLocale" value="projects.createProject.nameHelp"/>
                </jsp:include>
                <input type="text" name="name" class="required" size="40"/>
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.createProject.tagsTitle"/>
                <jsp:param name="helpLocale" value="projects.createProject.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
        
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.createProject.descriptionTitle"/>
                <jsp:param name="helpLocale" value="projects.createProject.descriptionHelp"/>
              </jsp:include>
              <textarea ix:cktoolbar="projectDescription" name="description" ix:ckeditor="true"></textarea>
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.createProject.optionalStudiesTitle"/>
                <jsp:param name="helpLocale" value="projects.createProject.optionalStudiesHelp"/>
              </jsp:include>
              <input type="text" name="optionalStudiesLength" class="required" size="15"/>
              <select name="optionalStudiesLengthTimeUnit">           
                <c:forEach var="optionalStudiesLengthTimeUnit" items="${optionalStudiesLengthTimeUnits}">
                  <option value="${optionalStudiesLengthTimeUnit.id}">${optionalStudiesLengthTimeUnit.name}</option> 
                </c:forEach>
              </select>
            </div>
          </div>
          
          <!--  Modules tab -->
          
          <div id="modules" class="tabContentixTableFormattedData">
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.createProject.modulesTitle"/>
                <jsp:param name="helpLocale" value="projects.createProject.modulesHelp"/>
              </jsp:include>
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" onclick="openSearchModulesDialog();"><fmt:message key="projects.createProject.addModuleLink"/></span>
              </div>
              
              <div id="noModulesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
                <span><fmt:message key="projects.createProject.noModulesAddedPreFix"/> <span onclick="openSearchModulesDialog();" class="genericTableAddRowLink"><fmt:message key="projects.createProject.noModulesAddedClickHereLink"/></span>.</span>
              </div>
            
              <div id="modulesContainer">
                <div id="modulesTableContainer"></div>
              </div>

              <div id="createProjectModulesTotalContainer" style="display:none;">
                <fmt:message key="projects.createProject.modulesTotal"/> <span id="createProjectModulesTotalValue"></span>
              </div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.createProject.subjectCoursesTitle"/>
                <jsp:param name="helpLocale" value="projects.createProject.subjectCoursesHelp"/>
              </jsp:include>

              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" onclick="addSubjectCourseTableRow();"><fmt:message key="projects.createProject.addSubjectCourseLink"/></span>
              </div>

              <div id="subjectCoursesContainer">
                <div id="subjectCoursesTableContainer"></div>
              </div>
  
              <div id="createProjectSubjectCoursesTotalContainer" style="display:none;">
                <fmt:message key="projects.createProject.modulesTotal"/> <span id="createProjectSubjectCoursesTotalValue"></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" value="<fmt:message key="projects.createProject.saveButton"/>">
      </div>

    </form>
  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>