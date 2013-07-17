var schoolFields = JSDATA["schoolFields"].evalJSON();


function addRow() {
  var table = getIxTableById('schoolFieldsTable');
  var rowIndex = table.addRow([ '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noSchoolFieldsAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var table = new IxTable($('schoolFieldsTable'), {
    id : "schoolFieldsTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.manageSchoolFields.schoolFieldsTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.manageSchoolFields.schoolFieldsTableNameHeader"),
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
          tooltip : getLocale().getText("settings.manageSchoolFields.schoolFieldsTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var schoolFieldId = table.getCellValue(event.row, table.getNamedColumnIndex('id'));
            var name = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.manageSchoolFields.schoolFieldsArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(name);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.manageSchoolFields.archiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.manageSchoolFields.archiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.manageSchoolFields.archiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archiveschoolfield.json", {
                    parameters : {
                      schoolFieldId : schoolFieldId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('schoolFieldsTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.manageSchoolFields.schoolFieldsTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noSchoolFieldsAddedMessageContainer').setStyle({
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
  for ( var i = 0, l = schoolFields.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(schoolFields[i].name), '', '', schoolFields[i].id, 0 ]);
  }

  table.addRows(rows);

  if (table.getRowCount() > 0) {
    $('noSchoolFieldsAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
