/** Initialize the page when it's loaded.
 * 
 * @param event The event responsible of calling this function.
 */
function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  new IxSearchNavigation($('studentSearchResultsPagesContainer'), {
    id : 'studentSearchResultsNavigation',
    maxNavigationPages : 9,
    onclick : function(event) {
      doStudentSearch(event.page);
    }
  });
  new IxSearchNavigation($('projectSearchResultsPagesContainer'), {
    id : 'projectSearchResultsNavigation',
    maxNavigationPages : 9,
    onclick : function(event) {
      doProjectSearch(event.page);
    }
  });
  new IxTable($('studentSearchResultsTableContainer'), {
    id : 'studentSearchResultsTable',
    columns : [ {
      left : 10,
      right : 10,
      header : getLocale().getText("projects.createStudentProject.studentTableNameHeader"),
      dataType : 'text',
      editable : false,
      onclick : function(event) {
        var table = event.tableComponent;
        table.setActiveRows([ event.row ]);
        $('selectedStudentId').value = table.getCellValue(event.row, table.getNamedColumnIndex('studentId'));
        $('createStudentProjectButton').disabled = false;
      },
      paramName : 'name'
    }, {
      dataType : 'hidden',
      paramName : 'studentId'
    } ]
  });
  new IxTable($('projectSearchResultsTableContainer'), {
    id : 'projectSearchResultsTable',
    columns : [ {
      left : 10,
      right : 10,
      header : getLocale().getText("projects.createStudentProject.projectTableNameHeader"),
      dataType : 'text',
      editable : false,
      onclick : function(event) {
        var table = event.tableComponent;
        table.setActiveRows([ event.row ]);
        $('selectedProjectId').value = table.getCellValue(event.row, table.getNamedColumnIndex('projectId'));
      },
      paramName : 'name'
    }, {
      dataType : 'hidden',
      paramName : 'projectId'
    } ]
  });
};

/**
 * Performs the search of students and displays the results of the given page.
 * 
 * @param page
 *          The results page to be shown after the search
 */
function doStudentSearch(page) {
  var searchForm = $("searchStudentsForm");
  JSONRequest.request("students/searchstudents.json", {
    parameters : {
      query : searchForm.text.value,
      page : page
    },
    onSuccess : function(jsonResponse) {
      var resultsTable = getIxTableById('studentSearchResultsTable');
      resultsTable.detachFromDom();
      resultsTable.deleteAllRows();
      var results = jsonResponse.results;
      for ( var i = 0; i < results.length; i++) {
        var name = results[i].lastName + ", " + results[i].firstName;
        resultsTable.addRow([ jsonEscapeHTML(name), results[i].id ]);
      }
      resultsTable.reattachToDom();
      getSearchNavigationById('studentSearchResultsNavigation').setTotalPages(jsonResponse.pages);
      getSearchNavigationById('studentSearchResultsNavigation').setCurrentPage(jsonResponse.page);
      $('studentSearchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
      $('studentSearchResultsWrapper').setStyle({
        display : ''
      });
      $('createStudentProjectButton').disabled = true;
    }
  });
}

/**
 * Performs the search of projects and displays the results of the given page.
 * 
 * @param page
 *          The results page to be shown after the search
 */
function doProjectSearch(page) {
  var searchForm = $("searchProjectsForm");
  JSONRequest.request("projects/searchprojects.json", {
    parameters : {
      text : searchForm.text.value,
      page : page
    },
    onSuccess : function(jsonResponse) {
      var resultsTable = getIxTableById('projectSearchResultsTable');
      resultsTable.detachFromDom();
      resultsTable.deleteAllRows();
      var results = jsonResponse.results;
      for ( var i = 0; i < results.length; i++) {
        resultsTable.addRow([ jsonEscapeHTML(results[i].name), results[i].id ]);
      }
      resultsTable.reattachToDom();
      getSearchNavigationById('projectSearchResultsNavigation').setTotalPages(jsonResponse.pages);
      getSearchNavigationById('projectSearchResultsNavigation').setCurrentPage(jsonResponse.page);
      $('projectSearchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
      $('projectSearchResultsWrapper').setStyle({
        display : ''
      });
    }
  });
}

/**
 * Invoked when the user submits the student search form. We cancel the submit event and delegate the work to the
 * doProjectSearch method.
 * 
 * @param event
 *          The search form submit event
 */
function onSearchStudents(event) {
  Event.stop(event);
  doStudentSearch(0);
}

/**
 * Invoked when the user submits the project search form. We cancel the submit event and delegate the work to the
 * doProjectSearch method.
 * 
 * @param event
 *          The search form submit event
 */
function onSearchProjects(event) {
  Event.stop(event);
  doProjectSearch(0);
}
