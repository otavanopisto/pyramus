/**
 * Performs the search and displays the results of the given page.
 * 
 * @param page
 *          The results page to be shown after the search
 */
function doSearch(page) {
  var searchForm = $("searchForm");
  JSONRequest.request("settings/searchschools.json", {
    parameters : {
      text : searchForm.text.value,
      page : page
    },
    onSuccess : function(jsonResponse) {
      var resultsTable = getIxTableById('searchResultsTable');
      resultsTable.detachFromDom();
      resultsTable.deleteAllRows();
      var results = jsonResponse.results;
      for ( var i = 0; i < results.length; i++) {
        resultsTable.addRow([ jsonEscapeHTML(results[i].name), jsonEscapeHTML(results[i].fieldName), '', '', '', results[i].id ]);
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

/**
 * Invoked when the user submits the search form. We cancel the submit event and delegate the work to the doSearch method.
 * 
 * @param event
 *          The search form submit event
 */
function onSearchSchools(event) {
  Event.stop(event);
  doSearch(0);
}

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  new IxSearchNavigation($('searchResultsPagesContainer'), {
    id : 'searchResultsNavigation',
    maxNavigationPages : 19,
    onclick : function(event) {
      doSearch(event.page);
    }
  });
  var searchResultsTable = new IxTable($('searchResultsTableContainer'), {
    id : 'searchResultsTable',
    columns : [
        {
          paramName : 'name',
          left : 8,
          dataType : 'text',
          editable : false
        },
        {
          paramName : 'schoolFieldName',
          right : 68,
          width : 180,
          dataType : 'text',
          editable : false
        },
        {
          width : 30,
          right : 60,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/eye.png',
          tooltip : getLocale().getText("settings.searchSchools.schoolsTableViewSchoolTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var schoolId = table.getCellValue(event.row, table.getNamedColumnIndex('schoolId'));
            redirectTo(GLOBAL_contextPath + '/settings/viewschool.page?school=' + schoolId);
          }
        },
        {
          width : 30,
          right : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.searchSchools.schoolsTableEditSchoolTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var schoolId = table.getCellValue(event.row, table.getNamedColumnIndex('schoolId'));
            redirectTo(GLOBAL_contextPath + '/settings/editschool.page?school=' + schoolId);
          }
        },
        {
          width : 30,
          right : 0,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.searchSchools.schoolsTableArchiveSchoolTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var schoolId = table.getCellValue(event.row, table.getNamedColumnIndex('schoolId'));
            var schoolName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.searchSchools.schoolArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(schoolName);

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.searchSchools.schoolArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.searchSchools.schoolArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.searchSchools.schoolArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              var dlg = event.dialog;

              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archiveschool.json", {
                    parameters : {
                      schoolId : schoolId
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
          paramName : 'schoolId'
        } ]
  });
};