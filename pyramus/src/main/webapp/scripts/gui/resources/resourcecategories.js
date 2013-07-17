var resourceCategories = JSDATA["resourceCategories"].evalJSON();

function addResourceCategoriesTableRow() {
  var table = getIxTableById('resourceCategoriesTable');
  var rowIndex = table.addRow([ '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noResourceCategoriesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var resourceCategoriesTable = new IxTable(
      $('resourceCategoriesTable'),
      {
        id : "resourceCategoriesTable",
        columns : [
            {
              left : 8,
              width : 30,
              dataType : 'button',
              imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
              tooltip : getLocale().getText("resources.resourceCategories.resourceCategoriesTableEditTooltip"),
              onclick : function(event) {
                var table = event.tableComponent;
                for ( var i = 0; i < table.getColumnCount(); i++) {
                  table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
                }
                table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
              }
            },
            {
              header : getLocale().getText("resources.resourceCategories.resourceCategoriesTableNameHeader"),
              left : 38,
              width : 750,
              dataType : 'text',
              editable : false,
              paramName : 'name',
              required : true
            },
            {
              right : 8,
              width : 30,
              dataType : 'button',
              imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
              tooltip : getLocale().getText("resources.resourceCategories.resourceCategoriesTableArchiveTooltip"),
              onclick : function(event) {
                var table = event.tableComponent;
                var resourceCategoryId = table.getCellValue(event.row, table.getNamedColumnIndex('resourceCategoryId'));
                var resourceCategoryName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
                var url = GLOBAL_contextPath
                    + "/simpledialog.page?localeId=resources.resourceCategories.resourceCategoryArchiveConfirmDialogContent&localeParams="
                    + encodeURIComponent(resourceCategoryName);

                var archivedRowIndex;
                archivedRowIndex = event.row;

                var dialog = new IxDialog(
                    {
                      id : 'confirmRemoval',
                      contentURL : url,
                      centered : true,
                      showOk : true,
                      showCancel : true,
                      autoEvaluateSize : true,
                      title : getLocale().getText("resources.resourceCategories.resourceCategoryArchiveConfirmDialogTitle"),
                      okLabel : getLocale().getText("resources.resourceCategories.resourceCategoryArchiveConfirmDialogOkLabel"),
                      cancelLabel : getLocale().getText("resources.resourceCategories.resourceCategoryArchiveConfirmDialogCancelLabel")
                    });

                dialog.addDialogListener(function(event) {
                  switch (event.name) {
                    case 'okClick':
                      JSONRequest.request("resources/archiveresourcecategory.json", {
                        parameters : {
                          resourceCategoryId : resourceCategoryId
                        },
                        onSuccess : function(jsonResponse) {
                          getIxTableById('resourceCategoriesTable').deleteRow(archivedRowIndex);
                        }
                      });
                    break;
                  }
                });

                dialog.open();
              },
              paramName : 'archiveButton',
              hidden : true
            }, {
              right : 8,
              width : 30,
              dataType : 'button',
              imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
              tooltip : getLocale().getText("resources.resourceCategories.resourceCategoriesTableRemoveTooltip"),
              onclick : function(event) {
                event.tableComponent.deleteRow(event.row);
                if (event.tableComponent.getRowCount() == 0) {
                  $('noResourceCategoriesAddedMessageContainer').setStyle({
                    display : ''
                  });
                }
              },
              paramName : 'removeButton',
              hidden : true
            }, {
              dataType : 'hidden',
              paramName : 'resourceCategoryId'
            }, {
              dataType : 'hidden',
              paramName : 'modified'
            } ]
      });

  var rowIndex;
  for ( var i = 0; i < resourceCategories.length; i++) {
    var r = resourceCategories[i];
    rowIndex = resourceCategoriesTable.addRow([ '', jsonEscapeHTML(r.name), '', '', r.id, 0 ]);
    resourceCategoriesTable.showCell(rowIndex, resourceCategoriesTable.getNamedColumnIndex('archiveButton'));
  }
  if (resourceCategoriesTable.getRowCount() > 0) {
    $('noResourceCategoriesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
