<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="projects.searchStudentProjects.pageTitle" /></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        new IxSearchNavigation($('searchResultsPagesContainer'), {
          id: 'searchResultsNavigation',
          maxNavigationPages: 19,
          onclick: function(event) {
            doStudentProjectSearch(event.page);
          }
        });
        new IxTable($('searchResultsTableContainer'), {
          id: 'searchStudentProjectsResultsTable',
          columns: [ {
            header: '<fmt:message key="projects.searchStudentProjects.studentProjectTableProjectNameHeader"/>',
            left: 8,
            right : 8 + 22 + 8 + 22 + 8 + 300 + 8,
            dataType: 'text',
            editable: false,
            paramName: 'projectName' 
          }, {
            header: '<fmt:message key="projects.searchStudentProjects.studentProjectTableStudentNameHeader"/>',
            dataType: 'text',
            width: 300,
            right : 8 + 22 + 8 + 22 + 8,
            editable: false,
            paramName: 'studentName' 
          }, {
            width: 22,
            right : 8 + 22 + 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="projects.searchStudentProjects.studentProjectTableEditStudentProjectTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var studentProjectId = table.getCellValue(event.row, table.getNamedColumnIndex('studentProjectId'));
              redirectTo(GLOBAL_contextPath + '/projects/editstudentproject.page?studentproject=' + studentProjectId);
            } 
          }, {
            width: 22,
            right : 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="projects.searchStudentProjects.studentProjectTableArchiveProjectTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var studentProjectId = table.getCellValue(event.row, table.getNamedColumnIndex('studentProjectId'));
              var projectName = table.getCellValue(event.row, table.getNamedColumnIndex('projectName'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=projects.searchStudentProjects.studentProjectArchiveConfirmDialogContent&localeParams=" + encodeURIComponent(projectName);
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="projects.searchStudentProjects.studentProjectArchiveConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="projects.searchStudentProjects.studentProjectArchiveConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="projects.searchStudentProjects.studentProjectArchiveConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener(function(event) {
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("projects/archivestudentproject.json", {
                      parameters: {
                        studentProjectId: studentProjectId
                      },
                      onSuccess: function (jsonResponse) {                        
                        var currentPage = getSearchNavigationById('searchResultsNavigation').getCurrentPage();
                        doStudentProjectSearch(currentPage);
                      }
                    });
                  break;
                }
              });
            
              dialog.open();
            } 
          }, {
            dataType: 'hidden',
            paramName: 'studentProjectId'
          }]
        });
      };
  
      /**
       * Performs the search of student projects and displays the results of the given page.
       *
       * @param page The results page to be shown after the search
       */
      function doStudentProjectSearch(page) {
        var searchForm = $("searchStudentProjectsForm");
        JSONRequest.request("projects/searchstudentprojects.json", {
          parameters: {
            project: searchForm.project.value,
            student: searchForm.student.value,
            page: page
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('searchStudentProjectsResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              var name = results[i].studentLastName + ", " + results[i].studentFirstName;
              resultsTable.addRow([results[i].studentProjectName.escapeHTML(), name.escapeHTML(), "", "", results[i].id]);
            }
            resultsTable.reattachToDom();
            getSearchNavigationById('searchResultsNavigation').setTotalPages(jsonResponse.pages);
            getSearchNavigationById('searchResultsNavigation').setCurrentPage(jsonResponse.page);
            $('searchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
            $('searchResultsWrapper').setStyle({
              display: ''
            });
          } 
        });
      }

      /**
       * Invoked when the user submits the student project search form. We cancel the submit event
       * and delegate the work to the doStudentProjectSearch method.
       *
       * @param event The search form submit event
       */
      function onSearchStudentProjects(event) {
        Event.stop(event);
        doStudentProjectSearch(0);
      }

    </script>
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>

    <h1 class="genericPageHeader"><fmt:message key="projects.searchStudentProjects.pageTitle" /></h1>

    <div id="searchStudentProjectSearchFormContainer"> 
      <div class="genericFormContainer"> 

        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#searchStudentProjects">
            <fmt:message key="projects.searchStudentProjects.tabLabelSearchStudentProjects"/>
          </a>
        </div>
        
        <div id="searchStudentProjects" class="tabContent">
			    <div class="genericFormContainer" id="searchStudentProjectsSearchFormContainer">
			      <form id="searchStudentProjectsForm" method="post" onsubmit="onSearchStudentProjects(event);">
			        <div class="genericFormSection">
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="projects.searchStudentProjects.projectTitle"/>
                        <jsp:param name="helpLocale" value="projects.searchStudentProjects.projectHelp"/>
                      </jsp:include>
                      <input type="text" name="project" size="40"/>
			        </div>
	              <div class="genericFormSection">
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="projects.searchStudentProjects.studentTitle"/>
                      <jsp:param name="helpLocale" value="projects.searchStudentProjects.studentHelp"/>
                    </jsp:include>
	                <input type="text" name="student" size="40"/>
	              </div>
			        <div class="genericFormSubmitSection">
			          <input type="submit" value="<fmt:message key="projects.searchStudentProjects.searchButton"/>">
			        </div>
			      </form>
			    </div>
				</div>
        <div id="searchResultsWrapper" style="display:none;">
          <div class="searchResultsTitle"><fmt:message key="projects.searchStudentProjects.resultsTitle"/></div>
          <div id="searchResultsContainer" class="searchResultsContainer">
            <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
            <div id="searchResultsTableContainer"></div>
            <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
          </div>
        </div>
			</div>
		</div>
    

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>