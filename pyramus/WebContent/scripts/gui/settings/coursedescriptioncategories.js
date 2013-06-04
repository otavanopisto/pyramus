
var descriptionCategories = JSDATA["descriptionCategories"].evalJSON();

function addRow() {
  var table = getIxTableById('courseDescriptionCategoriesTable');
  var rowIndex = table.addRow([ '', '', '', '', '' ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  $('noneAddedMessageContainer').setStyle({
    display : 'none'
  });

  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var courseDescriptionCategoriesTable = new IxTable($('courseDescriptionCategoriesTable'), {
    id : "courseDescriptionCategoriesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.courseDescriptionCategories.courseDescriptionCategoriesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
          }
        },
        {
          header : getLocale().getText("settings.courseDescriptionCategories.categoryNameTableHeader"),
          left : 38,
          right : 38,
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
          tooltip : getLocale().getText("settings.courseDescriptionCategories.archiveTableTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var categoryId = table.getCellValue(event.row, table.getNamedColumnIndex('categoryId'));
            var categoryName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.courseDescriptionCategories.archiveConfirmDialogContent&localeParams="
                + encodeURIComponent(categoryName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.courseDescriptionCategories.archiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.courseDescriptionCategories.archiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.courseDescriptionCategories.archiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivecoursedescriptioncategory.json", {
                    parameters : {
                      categoryId : categoryId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('courseDescriptionCategoriesTable').deleteRow(archivedRowIndex);
                    }
                  });
                break;
              }
            });

            dialog.open();
          },
          paramName : 'archiveButton'
        }, {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : getLocale().getText("settings.courseDescriptionCategories.removeTableTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noneAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'categoryId'
        } ]
  });

  var rowIndex;
  courseDescriptionCategoriesTable.detachFromDom();
  for ( var i = 0, l = descriptionCategories.length; i < l; i++) {
    rowIndex = courseDescriptionCategoriesTable.addRow([ '', jsonEscapeHTML(descriptionCategories[i].name), '', '', descriptionCategories[i].id ]);
  }
  courseDescriptionCategoriesTable.reattachToDom();

  if (courseDescriptionCategoriesTable.getRowCount() > 0) {
    $('noneAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
