function doSearch(page) {
  var searchForm = $("searchForm");
  JSONRequest.request("resources/searchresources.json", {
    parameters : {
      activeTab : searchForm.activeTab.value,
      name : searchForm.name.value,
      tags : searchForm.tags.value,
      simpleQuery : searchForm.simpleQuery.value,
      resourceType : searchForm.resourceType.value,
      resourceCategory : searchForm.resourceCategory.value,
      page : page
    },
    onSuccess : function(jsonResponse) {
      var resultsTable = getIxTableById('resourcesTable');
      resultsTable.detachFromDom();
      resultsTable.deleteAllRows();
      var results = jsonResponse.results;
      for ( var i = 0; i < results.length; i++) {
        var resourceType  = "";
        if (results[i].resourceType == 'MATERIAL_RESOURCE') {
          resourceType = getLocale().getText("resources.searchResources.resourceType_MATERIAL_RESOURCE");
        } else if (results[i].resourceType == 'WORK_RESOURCE') {
          resourceType = getLocale().getText("resources.searchResources.resourceType_WORK_RESOURCE");
        }
        resultsTable.addRow([ jsonEscapeHTML(results[i].name), resourceType, jsonEscapeHTML(results[i].resourceCategoryName),
            '', '', results[i].resourceType, results[i].id ]);
      }
      resultsTable.reattachToDom();
      getSearchNavigationById('searchResultsNavigation').setTotalPages(jsonResponse.pages);
      getSearchNavigationById('searchResultsNavigation').setCurrentPage(jsonResponse.page);
      $('searchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
      $('searchResultsWrapper').setStyle({
        display : ''
      });
    }
  });
}

function onSearchResources(event) {
  Event.stop(event);
  doSearch(0);
}

function setupTags() {
  JSONRequest.request("tags/getalltags.json", {
    onSuccess : function(jsonResponse) {
      new Autocompleter.Local("tags", "tags_choices", jsonResponse.tags, {
        tokens : [ ',', '\n', ' ' ]
      });
    }
  });
}

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  $('searchForm').activeTab.value = tabControl.getActiveTab();
  tabControl.addListener(function(event) {
    if ((event.action == 'tabActivated') || (event.action == 'tabInitialized')) {
      $('searchForm').activeTab.value = event.name;
      $('activeTab').value = event.name;
    }
  });

  setupTags();

  new IxSearchNavigation($('searchResultsPagesContainer'), {
    id : 'searchResultsNavigation',
    maxNavigationPages : 19,
    onclick : function(event) {
      doSearch(event.page);
    }
  });
  var resultsTable = new IxTable(
      $('searchResultsTableContainer'),
      {
        id : "resourcesTable",
        columns : [
            {
              header : getLocale().getText("resources.searchResources.resourcesTableNameHeader"),
              left : 8,
              right : 476,
              dataType : 'text',
              editable : false,
              paramName : 'name'
            },
            {
              header : getLocale().getText("resources.searchResources.resourcesTypeNameHeader"),
              right : 268,
              width : 200,
              dataType : 'text',
              editable : false
            },
            {
              header : getLocale().getText("resources.searchResources.resourcesCategoryNameHeader"),
              width : 200,
              right : 60,
              dataType : 'text',
              editable : false
            },
            {
              width : 30,
              right : 30,
              dataType : 'button',
              imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
              tooltip : getLocale().getText("resources.searchResources.resourceTableEditResourceTooltip"),
              onclick : function(event) {
                var table = event.tableComponent;
                var resourceId = table.getCellValue(event.row, table.getNamedColumnIndex('resourceId'));
                var resourceType = table.getCellValue(event.row, table.getNamedColumnIndex('resourceType'));
                if (resourceType == 'MATERIAL_RESOURCE') {
                  redirectTo(GLOBAL_contextPath + '/resources/editmaterialresource.page?resource=' + resourceId);
                } else {
                  redirectTo(GLOBAL_contextPath + '/resources/editworkresource.page?resource=' + resourceId);
                }
              }
            },
            {
              width : 30,
              right : 0,
              dataType : 'button',
              imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
              tooltip : getLocale().getText("resources.searchResources.resourceTableArchiveResourceTooltip"),
              onclick : function(event) {
                var table = event.tableComponent;
                var resourceId = table.getCellValue(event.row, table.getNamedColumnIndex('resourceId'));
                var resourceName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
                var url = GLOBAL_contextPath
                    + "/simpledialog.page?localeId=resources.searchResources.resourceArchiveConfirmDialogContent&localeParams="
                    + encodeURIComponent(resourceName);

                var dialog = new IxDialog(
                    {
                      id : 'confirmRemoval',
                      contentURL : url,
                      centered : true,
                      showOk : true,
                      showCancel : true,
                      autoEvaluateSize : true,
                      title : getLocale().getText("resources.searchResources.resourceArchiveConfirmDialogTitle"),
                      okLabel : getLocale().getText("resources.searchResources.resourceArchiveConfirmDialogOkLabel"),
                      cancelLabel : getLocale().getText("resources.searchResources.resourceArchiveConfirmDialogCancelLabel")
                    });

                dialog.addDialogListener(function(event) {
                  var dlg = event.dialog;

                  switch (event.name) {
                    case 'okClick':
                      JSONRequest.request("resources/archiveresource.json", {
                        parameters : {
                          resource : resourceId
                        },
                        onSuccess : function(jsonResponse) {
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
              dataType : 'hidden',
              paramName : 'resourceType'
            }, {
              dataType : 'hidden',
              paramName : 'resourceId'
            } ]
      });
};