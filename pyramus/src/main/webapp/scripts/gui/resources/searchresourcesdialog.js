/**
 * Convenience method to return the row index of the given resource in the given resource table.
 * 
 * @param tableId
 *          The table identifier
 * @param resourceId
 *          The resource identifier
 * 
 * @return The row index of the given resource in the given resource table. Returns -1 if not found.
 */
function getResourceRowIndex(tableId, resourceId) {
  var table = getIxTableById(tableId);
  if (table) {
    for ( var i = 0; i < table.getRowCount(); i++) {
      var tableResourceId = table.getCellValue(i, table.getNamedColumnIndex('resourceId'));
      if (tableResourceId == resourceId) {
        return i;
      }
    }
  }
  return -1;
}

/**
 * Performs the search and displays the results of the given page.
 * 
 * @param page
 *          The results page to be shown after the search
 */
function doSearch(page) {
  var searchResourcesForm = $("searchResourcesForm");
  JSONRequest.request("resources/searchresources.json", {
    parameters : {
      name : searchResourcesForm.name.value,
      resourceType : searchResourcesForm.resourceType.value,
      resourceCategory : searchResourcesForm.resourceCategory.value,
      page : page
    },
    onSuccess : function(jsonResponse) {
      var resultsTable = getIxTableById('searchResultsTable');
      resultsTable.detachFromDom();
      resultsTable.deleteAllRows();
      var results = jsonResponse.results;
      for ( var i = 0; i < results.length; i++) {
        resultsTable
            .addRow([ jsonEscapeHTML(results[i].name), results[i].id, results[i].unitCost, results[i].hourlyCost ]);
        var rowIndex = getResourceRowIndex('resourcesTable', results[i].id);
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
 * Invoked when the user submits the search form. We cancel the submit event and delegate the work to the doSearch
 * method.
 * 
 * @param event
 *          The search form submit event
 */
function onSearchResources(event) {
  Event.stop(event);
  doSearch(0);
}

/**
 * Returns the identifiers of the resources selected in this dialog.
 * 
 * @return The resources selected in this dialog
 */
function getResults() {
  var results = new Array();
  var table = getIxTableById('resourcesTable');
  for ( var i = 0; i < table.getRowCount(); i++) {
    var resourceName = table.getCellValue(i, table.getNamedColumnIndex('name'));
    var resourceId = table.getCellValue(i, table.getNamedColumnIndex('resourceId'));
    var resourceUnitCost = table.getCellValue(i, table.getNamedColumnIndex('unitCost'));
    var resourceHourlyCost = table.getCellValue(i, table.getNamedColumnIndex('hourlyCost'));
    results.push({
      name : resourceName,
      id : resourceId,
      unitCost : resourceUnitCost,
      hourlyCost : resourceHourlyCost
    });
  }
  return {
    resources : results
  };
}

/**
 * Called when this dialog loads. Initializes the search navigation and resource tables.
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
        table.disableRow(event.row);
        var resourceId = table.getCellValue(event.row, table.getNamedColumnIndex('resourceId'));
        var resourceName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
        var resourceUnitCost = table.getCellValue(event.row, table.getNamedColumnIndex('unitCost'));
        var resourceHourlyCost = table.getCellValue(event.row, table.getNamedColumnIndex('hourlyCost'));
        getIxTableById('resourcesTable').addRow([ resourceName, resourceId, resourceUnitCost, resourceHourlyCost ]);
      }
    }, {
      dataType : 'hidden',
      paramName : 'resourceId'
    }, {
      dataType : 'hidden',
      paramName : 'unitCost'
    }, {
      dataType : 'hidden',
      paramName : 'hourlyCost'
    } ]
  });
  searchResultsTable.domNode.addClassName("modalDialogSearchResultsIxTable");

  var resourcesTable = new IxTable($('resourcesTableContainer'), {
    id : 'resourcesTable',
    columns : [ {
      left : 8,
      right : 8,
      dataType : 'text',
      editable : false,
      selectable : false,
      paramName : 'name',
      onclick : function(event) {
        var table = event.tableComponent;
        var resourceId = table.getCellValue(event.row, table.getNamedColumnIndex('resourceId'));
        table.deleteRow(event.row);
        var rowIndex = getResourceRowIndex('searchResultsTable', resourceId);
        if (rowIndex != -1) {
          var resultsTable = getIxTableById('searchResultsTable');
          resultsTable.enableRow(rowIndex);
        }
      }
    }, {
      dataType : 'hidden',
      paramName : 'resourceId'
    }, {
      dataType : 'hidden',
      paramName : 'unitCost'
    }, {
      dataType : 'hidden',
      paramName : 'hourlyCost'
    } ]
  });
  resourcesTable.domNode.addClassName("modalDialogResourcesIxTable");

  $('searchResourcesForm').name.focus();
}