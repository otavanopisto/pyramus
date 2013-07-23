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
    
    <script type="text/javascript">
    
	    function setupTags() {
	      JSONRequest.request("tags/getalltags.json", {
	        onSuccess: function (jsonResponse) {
	          new Autocompleter.Local("tags", "tags_choices", jsonResponse.tags, {
	            tokens: [',', '\n', ' ']
	          });
	        }
	      });   
	    }

      /**
       * Convenience method to return the row index of the given module in the given module table.
       *
       * @param tableId The table identifier
       * @param moduleId The module identifier
       *
       * @return The row index of the given module in the given module table. Returns -1 if not found.
       */
      function getModuleRowIndex(tableId, moduleId) {
        var table = getIxTableById(tableId);
        if (table) {
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableModuleId = table.getCellValue(i, table.getNamedColumnIndex('moduleId'));
            if (tableModuleId == moduleId) {
              return i;
            }
          }
        }
        return -1;
      }

      /**
       * Performs the search and displays the results of the given page.
       *
       * @param page The results page to be shown after the search
       */
      function doSearch(page) {
        var searchModulesForm = $("searchModulesForm");
        JSONRequest.request("projects/searchmodules.json", {
          parameters: {
            name: searchModulesForm.name.value,
            projectName: searchModulesForm.project.value,
            tags: searchModulesForm.tags.value,
            page: page,
            maxResults: 10
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('searchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              resultsTable.addRow([results[i].name.escapeHTML(), results[i].id]);
              var rowIndex = getModuleRowIndex('modulesTable', results[i].id);
              if (rowIndex != -1) {
                resultsTable.disableRow(resultsTable.getRowCount() - 1);
              } 
            }
            resultsTable.reattachToDom();
            getSearchNavigationById('searchResultsNavigation').setTotalPages(jsonResponse.pages);
            getSearchNavigationById('searchResultsNavigation').setCurrentPage(jsonResponse.page);
            $('modalSearchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
          } 
        });
      }
      
      /**
       * Invoked when the user submits the search form. We cancel the submit event
       * and delegate the work to the doSearch method.
       *
       * @param event The search form submit event
       */
      function onSearchModules(event) {
        Event.stop(event);
        doSearch(0);
      }

      /**
       * Returns the identifiers of the modules selected in this dialog.
       *
       * @return The modules selected in this dialog
       */
      function getResults() {
        var results = new Array();
        var table = getIxTableById('modulesTable');
        for (var i = 0; i < table.getRowCount(); i++) {
          var moduleName = table.getCellValue(i, table.getNamedColumnIndex('name'));
          var moduleId = table.getCellValue(i, table.getNamedColumnIndex('moduleId'));
          results.push({
            name: moduleName,
            id: moduleId});
        }
        return {
          modules: results
        };
      }

      /**
       * Called when this dialog loads. Initializes the search navigation and module tables.
       *
       * @param event The page load event
       */
      function onLoad(event) {
        new IxSearchNavigation($('modalSearchResultsPagesContainer'), {
          id: 'searchResultsNavigation',
          maxNavigationPages: 9,
          onclick: function(event) {
            doSearch(event.page);
          }
        });
        
        setupTags();

        var searchResultsTable = new IxTable($('searchResultsTableContainer'), {
          id: 'searchResultsTable',
          columns : [ {
            left: 8,
            right: 8,
            dataType: 'text',
            editable: false,
            selectable: false,
            paramName: 'name',
            onclick: function (event) {
              var table = event.tableComponent;
              table.disableRow(event.row);
              var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              var moduleName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
              getIxTableById('modulesTable').addRow([moduleName, moduleId]);
            }
          }, {
            dataType: 'hidden',
            paramName: 'moduleId'
          }]
        });
        searchResultsTable.domNode.addClassName("modalDialogSearchResultsIxTable");
        
        var modulesTable = new IxTable($('modulesTableContainer'), {
          id: 'modulesTable',
          columns : [ {
            left: 8,
            right: 8,
            dataType: 'text',
            editable: false,
            selectable: false,
            paramName: 'name',
            onclick: function (event) {
              var table = event.tableComponent;
              var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              table.deleteRow(event.row);
              var rowIndex = getModuleRowIndex('searchResultsTable', moduleId);
              if (rowIndex != -1) {
                var resultsTable = getIxTableById('searchResultsTable');
                resultsTable.enableRow(rowIndex);
              }
            }
          }, {
            dataType: 'hidden',
            paramName: 'moduleId'
          }]
        });
        modulesTable.domNode.addClassName("modalDialogModulesIxTable");

        $('searchModulesForm').name.focus();
      }
    </script>

  </head>
  <body onload="onLoad(event);">

    <div id="searchModulesDialogSearchContainer" class="modalSearchContainer">
      <div class="modalSearchTabLabel"><fmt:message key="projects.searchModulesDialog.searchTitle"/></div> 
      <div class="modalSearchTabContent">
	      <div class="genericFormContainer"> 
	        
	        <form id="searchModulesForm" method="post" onsubmit="onSearchModules(event);">

	          <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="projects.searchModulesDialog.nameTitle"/>
                  <jsp:param name="helpLocale" value="projects.searchModulesDialog.nameHelp"/>
                </jsp:include>
	            <input type="text" name="name" size="40"/>
	          </div>
	          
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.searchModulesDialog.projectTitle"/>
                <jsp:param name="helpLocale" value="projects.searchModulesDialog.projectHelp"/>
              </jsp:include>
              <input type="text" name="project" size="40"/>
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.searchModulesDialog.tagsTitle"/>
                <jsp:param name="helpLocale" value="projects.searchModulesDialog.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
	
            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="projects.searchModulesDialog.searchButton"/>">
            </div>
      
          </form>
        </div>
      </div>
      
      <div id="searchResultsContainer" class="modalSearchResultsContainer">
        <div class="modalSearchResultsTabLabel"><fmt:message key="projects.searchModulesDialog.searchResultsTitle"/></div>
        <div id="modalSearchResultsStatusMessageContainer" class="modalSearchResultsMessageContainer"></div>    
        <div id="searchResultsTableContainer" class="modalSearchResultsTabContent"></div>
        <div id="modalSearchResultsPagesContainer" class="modalSearchResultsPagesContainer"></div>
      </div>
      
    </div>
    
    <div id="modulesContainer" class="modalSelectedItemsContainer">
      <div class="modalSelectedItemsTabLabel"><fmt:message key="projects.searchModulesDialog.selectedModulesTitle"/></div>
      <div id="modulesTableContainer" class="modalSelectedItemsTabContent"></div>
    </div>

  </body>
</html>