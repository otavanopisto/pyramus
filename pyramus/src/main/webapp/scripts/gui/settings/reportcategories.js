var reportCategories = JSDATA["reportCategories"].evalJSON();

var deletedRowIndex;

function addReportCategoriesTableRow() {
  var table = getIxTableById('reportCategoriesTable');
  var rowIndex = table.addRow([ '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noReportCategoriesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('deleteButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var reportCategoriesTable = new IxTable($('reportCategoriesTableContainer'), {
    id : "reportCategoriesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.reportCategories.reportCategoriesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.reportCategories.reportCategoriesTableNameHeader"),
          left : 38,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          left : 346,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.reportCategories.reportCategoriesTableDeleteTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var reportCategoryId = table.getCellValue(event.row, table.getNamedColumnIndex('reportCategoryId'));
            var reportCategoryName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.reportCategories.reportCategoryDeleteConfirmDialogContent&localeParams="
                + encodeURIComponent(reportCategoryName);

            deletedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.reportCategories.reportCategoryDeleteConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.reportCategories.reportCategoryDeleteConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.reportCategories.reportCategoryDeleteConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/deletereportcategory.json", {
                    parameters : {
                      reportCategory : reportCategoryId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('reportCategoriesTable').deleteRow(deletedRowIndex);
                    }
                  });
                break;
              }
            });

            dialog.open();
          },
          paramName : 'deleteButton'
        }, {
          left : 346,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : getLocale().getText("settings.reportCategories.reportCategoriesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noReportCategoriesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'reportCategoryId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = reportCategories.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(reportCategories[i].name), '', '', reportCategories[i].id, 0]);
  }
  reportCategoriesTable.addRows(rows);

  if (reportCategoriesTable.getRowCount() > 0) {
    $('noReportCategoriesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
