/**
 * Performs the search and displays the results of the given page.
 * 
 * @param page
 *          The results page to be shown after the search
 */
function doSearch(page) {
  var searchModulesForm = $("searchModulesForm");
  JSONRequest.request("modules/searchmodules.json", {
    parameters : {
      activeTab: "advanced",
      name : searchModulesForm.name.value,
      subject: searchModulesForm.subject.value,
      curriculum: searchModulesForm.curriculum.value,
      maxResults: 8,
      page : page
    },
    onSuccess : function(jsonResponse) {
      var resultsTable = getIxTableById('searchResultsTable');
      resultsTable.detachFromDom();
      resultsTable.deleteAllRows();
      var results = jsonResponse.results;
      for (var i = 0; i < results.length; i++) {
        resultsTable.addRow([ jsonEscapeHTML(results[i].name), results[i].id ]);
      }
      resultsTable.reattachToDom();
      getSearchNavigationById('searchResultsNavigation').setTotalPages(jsonResponse.pages);
      getSearchNavigationById('searchResultsNavigation').setCurrentPage(jsonResponse.page);
      $('modalSearchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
    }
  });
}

/**
 * Invoked when the user submits the search form. We cancel the submit event and delegate the work to the doSearch
 * method.
 * 
 * @param event
 *          The search form submit event
 */
function onSearchModules(event) {
  Event.stop(event);
  doSearch(0);
  courseModuleChangeSelect(undefined, undefined);
}

/**
 * Returns the identifiers of the modules selected in this dialog.
 * 
 * @return The modules selected in this dialog
 */
function getResults() {
  return {
    selectedModule : window.___courseModuleChangeSelectedModule
  };
}

window.___courseModuleChangeSelectedModule = undefined;

function courseModuleChangeSelect(row, module) {
  var table = getIxTableById('searchResultsTable')
  
  if (typeof row != "undefined")
    table.setActiveRows([ row ]);
  else
    table.setActiveRows([ ]);

  var dialog = getDialog("changeModuleDialog");
  if (module) {
    window.___courseModuleChangeSelectedModule = module;
    dialog.enableOkButton();
  } else
    dialog.disableOkButton();
}

/**
 * Called when this dialog loads. Initializes the search navigation and module tables.
 * 
 * @param event
 *          The page load event
 */
function onLoad(event) {
  new IxSearchNavigation($('modalSearchResultsPagesContainer'), {
    id : 'searchResultsNavigation',
    maxNavigationPages : 9,
    onclick : function(event) {
      doSearch(event.page);
      courseModuleChangeSelect(undefined, undefined);
    }
  });

  var searchResultsTable = new IxTable($('searchResultsTableContainer'), {
    id : 'searchResultsTable',
    columns : [ {
      left : 8,
      right : 8,
      dataType : 'text',
      editable : false,
      selectable : false,
      paramName : 'name',
      onclick : function(event) {
        var table = event.tableComponent;
        var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
        var moduleName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
        courseModuleChangeSelect(event.row, {
          name: moduleName,
          id: moduleId
        });
      }
    }, {
      dataType : 'hidden',
      paramName : 'moduleId'
    } ]
  });
  searchResultsTable.domNode.addClassName("modalDialogSearchResultsIxTable");

  $('searchModulesForm').name.focus();
}