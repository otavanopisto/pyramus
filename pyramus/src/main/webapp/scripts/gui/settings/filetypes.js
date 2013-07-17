var fileTypes = JSDATA["fileTypes"].evalJSON();

function addTableRow() {
  var table = getIxTableById('fileTypesTable');
  var rowIndex = table.addRow([ '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noFileTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var fileTypesTable = new IxTable($('fileTypesTable'), {
    id : "fileTypesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.fileTypes.tableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.fileTypes.tableNameHeader"),
          left : 38,
          width : 300,
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
          tooltip : getLocale().getText("settings.fileTypes.tableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var fileTypeId = table.getCellValue(event.row, table.getNamedColumnIndex('id'));
            var fileTypeName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.fileTypes.archiveConfirmDialogContent&localeParams="
                + encodeURIComponent(fileTypeName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.fileTypes.archiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.fileTypes.archiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.fileTypes.archiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivefiletype.json", {
                    parameters : {
                      fileTypeId : fileTypeId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('fileTypesTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.fileTypes.tableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noFileTypesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'id'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = fileTypes.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(fileTypes[i].name), '', '', fileTypes[i].id, 0 ]);
  }
  fileTypesTable.addRows(rows);

  if (fileTypesTable.getRowCount() > 0) {
    $('noFileTypesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
