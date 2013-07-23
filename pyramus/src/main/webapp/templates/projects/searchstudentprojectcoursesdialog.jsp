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

      function getCourseRowIndex(tableId, courseId) {
        var table = getIxTableById(tableId);
        if (table) {
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableCourseId = table.getCellValue(i, table.getNamedColumnIndex('courseId'));
            if (tableCourseId == courseId) {
              return i;
            }
          }
        }
        return -1;
      }

      function doSearch(page) {
        var searchCoursesForm = $("searchCoursesForm");
        JSONRequest.request("projects/searchstudentprojectcourses.json", {
          parameters: {
            name: searchCoursesForm.name.value,
            tags: searchCoursesForm.tags.value,
            page: page,
            maxResults: 10
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('searchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              var courseName = results[i].name;
              
              if (results[i].nameExtension) {
                courseName += " (" + results[i].nameExtension + ")";
              }
              if (results[i].studentCount > 0) {
                if (results[i].maxStudentCount > 0)
                  courseName += " (" + results[i].studentCount + "/" + results[i].maxStudentCount + ")";
                else
                  courseName += " (" + results[i].studentCount + ")";
              }
              
              resultsTable.addRow([courseName.escapeHTML(), results[i].beginDate, results[i].endDate, results[i].moduleId, results[i].id]);
              var rowIndex = getCourseRowIndex('coursesTable', results[i].id);
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
      function onSearchCourses(event) {
        Event.stop(event);
        doSearch(0);
      }

      function getResults() {
        var results = new Array();
        var table = getIxTableById('coursesTable');
        for (var i = 0; i < table.getRowCount(); i++) {
          var courseName = table.getCellValue(i, table.getNamedColumnIndex('name'));
          var courseId = table.getCellValue(i, table.getNamedColumnIndex('courseId'));
          var beginDate = table.getCellValue(i, table.getNamedColumnIndex('beginDate'));
          var endDate = table.getCellValue(i, table.getNamedColumnIndex('endDate'));
          var moduleId = table.getCellValue(i, table.getNamedColumnIndex('moduleId'));
          
          results.push({
            name: courseName,
            beginDate: beginDate,
            endDate: endDate,
            moduleId: moduleId,
            id: courseId
          });
        }
        return {
          courses: results
        };
      }

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
              var courseName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
              var beginDate = table.getCellValue(event.row, table.getNamedColumnIndex('beginDate'));
              var endDate = table.getCellValue(event.row, table.getNamedColumnIndex('endDate'));
              var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              var courseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
              getIxTableById('coursesTable').addRow([courseName, beginDate, endDate, moduleId, courseId]);
            }
          }, {
            dataType: 'hidden',
            paramName: 'beginDate'
          }, {
            dataType: 'hidden',
            paramName: 'endDate'
          }, {
            dataType: 'hidden',
            paramName: 'moduleId'
          }, {
            dataType: 'hidden',
            paramName: 'courseId'
          }]
        });
        searchResultsTable.domNode.addClassName("modalDialogSearchResultsIxTable");
        
        var coursesTable = new IxTable($('coursesTableContainer'), {
          id: 'coursesTable',
          columns : [ {
            left: 8,
            right: 8,
            dataType: 'text',
            editable: false,
            selectable: false,
            paramName: 'name',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
              table.deleteRow(event.row);
              var rowIndex = getCourseRowIndex('searchResultsTable', courseId);
              if (rowIndex != -1) {
                var resultsTable = getIxTableById('searchResultsTable');
                resultsTable.enableRow(rowIndex);
              }
            }
          }, {
            dataType: 'hidden',
            paramName: 'beginDate'
          }, {
            dataType: 'hidden',
            paramName: 'endDate'
          }, {
            dataType: 'hidden',
            paramName: 'moduleId'
          }, {
            dataType: 'hidden',
            paramName: 'courseId'
          }]
        });
        coursesTable.domNode.addClassName("modalDialogCoursesIxTable");

        $('searchCoursesForm').name.focus();
      }
    </script>

  </head>
  <body onload="onLoad(event);">

    <div id="searchCoursesDialogSearchContainer" class="modalSearchContainer">
      <div class="modalSearchTabLabel"><fmt:message key="projects.searchStudentProjectCoursesDialog.searchTitle"/></div> 
      <div class="modalSearchTabContent">
	      <div class="genericFormContainer"> 
	        
	        <form id="searchCoursesForm" method="post" onsubmit="onSearchCourses(event);">

	          <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="projects.searchStudentProjectCoursesDialog.nameTitle"/>
                  <jsp:param name="helpLocale" value="projects.searchStudentProjectCoursesDialog.nameHelp"/>
                </jsp:include>
	            <input type="text" name="name" size="40"/>
	          </div>

            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.searchStudentProjectCoursesDialog.tagsTitle"/>
                <jsp:param name="helpLocale" value="projects.searchStudentProjectCoursesDialog.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
	
	          <div class="genericFormSubmitSection">
	            <input type="submit" value="<fmt:message key="projects.searchStudentProjectCoursesDialog.searchButton"/>">
	          </div>
	    
	        </form>
	      </div>
      </div>
      
      <div id="searchResultsContainer" class="modalSearchResultsContainer">
        <div class="modalSearchResultsTabLabel"><fmt:message key="projects.searchStudentProjectCoursesDialog.searchResultsTitle"/></div>
        <div id="modalSearchResultsStatusMessageContainer" class="modalSearchResultsMessageContainer"></div>    
        <div id="searchResultsTableContainer" class="modalSearchResultsTabContent"></div>
        <div id="modalSearchResultsPagesContainer" class="modalSearchResultsPagesContainer"></div>
      </div>
      
    </div>
    
    <div id="coursesContainer" class="modalSelectedItemsContainer">
      <div class="modalSelectedItemsTabLabel"><fmt:message key="projects.searchStudentProjectCoursesDialog.selectedCoursesTitle"/></div>
      <div id="coursesTableContainer" class="modalSelectedItemsTabContent"></div>
    </div>

  </body>
</html>