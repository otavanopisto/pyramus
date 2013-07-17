<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="projects.createStudentProject.pageTitle" /></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/projects/createstudentproject.js">
    </script>
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>

    <h1 class="genericPageHeader"><fmt:message key="projects.createStudentProject.pageTitle" /></h1>

    <div id="createStudentProjectCreateFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#createStudentProject">
            <fmt:message key="projects.createStudentProject.tabLabelCreateStudentProject"/>
          </a>
        </div>
        
        <div id="createStudentProject" class="tabContent">
        
          <div class="genericInfoMessageContainer">
            <span><fmt:message key="projects.createStudentProject.instructionMessage"/></span>
          </div>
          
          <div id="createStudentProjectStudentContainer">
  
            <!--  Search students section -->
          
            <div id="createStudentProjectSearchStudentTab"><fmt:message key="projects.createStudentProject.searchStudentTabLabel"/></div>
            <div class="genericFormContainer" id="searchStudentProjectsStudentSearchFormContainer">
              <form id="searchStudentsForm" method="post" onsubmit="onSearchStudents(event);">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="projects.createStudentProject.textTitle"/>
                    <jsp:param name="helpLocale" value="projects.createStudentProject.textHelp"/>
                  </jsp:include>
              
                  <input type="text" name="text" size="40"/>
                </div>
                <div class="genericFormSubmitSection">
                  <input type="submit" value="<fmt:message key="projects.createStudentProject.searchStudentsButton"/>">
                </div>
              </form>
              
              <div id="studentSearchResultsWrapper" style="display:none;">
	              <div class="createStudentProjectSearchResultsTitle"><fmt:message key="projects.createStudentProject.searchStudentsResultsTitle"/></div>
	              <div id="studentSearchResultsContainer" class="createStudentProjectSearchResultsContainer">
	                <div id="studentSearchResultsStatusMessageContainer" class="createStudentProjectSearchResultsMessageContainer"></div>
	                <div id="studentSearchResultsTableContainer"></div>
	                <div id="studentSearchResultsPagesContainer" class="createStudentProjectSearchResultsPagesContainer"></div>
	              </div>
	            </div>
              
            </div>
          </div>
    
          <div id="createStudentProjectProjectContainer">

            <!--  Search projects section -->

            <div id="createStudentProjectSearchProjectTab"><fmt:message key="projects.createStudentProject.searchProjectTabLabel"/></div>
            <div class="genericFormContainer" id="searchStudentProjectsProjectsSearchFormContainer">
              <form id="searchProjectsForm" method="post" onsubmit="onSearchProjects(event);">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="projects.createStudentProject.textTitle"/>
                    <jsp:param name="helpLocale" value="projects.createStudentProject.textHelp"/>
                  </jsp:include>
                  <input type="text" name="text" size="40"/>
                </div>
                <div class="genericFormSubmitSection">
                  <input type="submit" value="<fmt:message key="projects.createStudentProject.searchProjectsButton"/>">
                </div>
              </form>
              
              <div id="projectSearchResultsWrapper" style="display:none;">
	              <div class="createStudentProjectSearchResultsTitle"><fmt:message key="projects.createStudentProject.searchProjectsResultsTitle"/></div>
	              <div id="projectSearchResultsContainer" class="createStudentProjectSearchResultsContainer">
	                <div id="projectSearchResultsStatusMessageContainer" class="createStudentProjectSearchResultsMessageContainer"></div>
	                <div id="projectSearchResultsTableContainer"></div>
	                <div id="projectSearchResultsPagesContainer" class="createStudentProjectSearchResultsPagesContainer"></div>
	              </div>
	            </div>
            
            </div>
          </div>
          <div class="columnClear"></div>
        </div>

        <!--  Create student project section -->          

        <div class="genericFormSubmitSectionOffTab">
          <form id="createStudentProjectForm" method="post" action="createstudentproject.json" ix:jsonform="true" ix:useglasspane="true">
            <input id="selectedStudentId" type="hidden" name="studentId" value="-1"/>
            <input id="selectedProjectId" type="hidden" name="projectId" value="-1"/>
            <input id="createStudentProjectButton" type="submit" disabled="disabled" value="<fmt:message key="projects.createStudentProject.createStudentProjectButton"/>"/>
          </form>
        </div>
      </div>
    </div>
    

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>