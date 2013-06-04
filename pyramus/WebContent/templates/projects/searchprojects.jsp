<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title><fmt:message key="projects.searchProjects.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        new IxSearchNavigation($('searchResultsPagesContainer'), {
          id: 'searchResultsNavigation',
          maxNavigationPages: 19,
          onclick: function(event) {
            doSearch(event.page);
          }
        });
        new IxTable($('searchResultsTableContainer'), {
          id: 'searchResultsTable',
          columns : [ {
            header : '<fmt:message key="projects.searchProjects.projectTableNameHeader"/>',
            left: 8,
            right: 8 + 22 + 8 + 22 + 8 + 22 + 8,
            dataType: 'text',
            paramName: 'projectName',
            editable: false
          }, {
            width: 22,
            right: 8 + 22 + 8 + 22 + 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="projects.searchProjects.projectTableViewProjectTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var projectId = table.getCellValue(event.row, table.getNamedColumnIndex('projectId'));
              redirectTo(GLOBAL_contextPath + '/projects/viewproject.page?project=' + projectId);
            }
          }, {
            width: 22,
            right: 8 + 22 + 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="projects.searchProjects.projectTableEditProjectTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var projectId = table.getCellValue(event.row, table.getNamedColumnIndex('projectId'));
              redirectTo(GLOBAL_contextPath + '/projects/editproject.page?project=' + projectId);
            }
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="projects.searchProjects.projectTableArchiveProjectTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var projectId = table.getCellValue(event.row, table.getNamedColumnIndex('projectId'));
              var projectName = table.getCellValue(event.row, table.getNamedColumnIndex('projectName'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=projects.searchProjects.projectArchiveConfirmDialogContent&localeParams=" + encodeURIComponent(projectName);
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="projects.searchProjects.projectArchiveConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="projects.searchProjects.projectArchiveConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="projects.searchProjects.projectArchiveConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener(function(event) {
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("projects/archiveproject.json", {
                      parameters: {
                        projectId: projectId
                      },
                      onSuccess: function (jsonResponse) {
                        var currentPage = getSearchNavigationById('searchResultsNavigation').getCurrentPage();
                        doSearch(currentPage);
                      }
                    });
                  break;
                }
              });
            
              dialog.open();
            }
          }, {
            dataType: 'hidden',
            paramName: 'projectId'
          }]
        });
      };

      /**
       * Performs the search and displays the results of the given page.
       *
       * @param page The results page to be shown after the search
       */
      function doSearch(page) {
        var searchForm = $("searchForm");
        JSONRequest.request("projects/searchprojects.json", {
          parameters: {
            text: searchForm.text.value,
            page: page
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('searchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              resultsTable.addRow([results[i].name.escapeHTML(), '', '', '', results[i].id]);
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
       * Invoked when the user submits the search form. We cancel the submit event
       * and delegate the work to the doSearch method.
       *
       * @param event The search form submit event
       */
      function onSearchProjects(event) {
        Event.stop(event);
        doSearch(0);
      }

    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="projects.searchProjects.pageTitle" /></h1>
    
    <div id="searchProjectsSearchFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#searchProjects">
            <fmt:message key="projects.searchProjects.tabLabelSearchProjects"/>
          </a>
        </div>
        
        <div id="searchProjects" class="tabContent">
		      <form id="searchForm" method="post" onsubmit="onSearchProjects(event);">
		  
		        <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="projects.searchProjects.textTitle"/>
                    <jsp:param name="helpLocale" value="projects.searchProjects.textHelp"/>
                  </jsp:include>
		          <input type="text" name="text" size="40"/>
		        </div>
		        
		        <div class="genericFormSubmitSection">
		          <input type="submit" value="<fmt:message key="projects.searchProjects.searchButton"/>">
		        </div>
		  
		      </form>
			  </div>
			</div>
    </div>
    
    <div id="searchResultsWrapper" style="display:none;">
      <div class="searchResultsTitle"><fmt:message key="projects.searchProjects.resultsTitle"/></div>
      <div id="searchResultsContainer" class="searchResultsContainer">
        <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
        <div id="searchResultsTableContainer"></div>
        <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
      </div>
    </div>
  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>