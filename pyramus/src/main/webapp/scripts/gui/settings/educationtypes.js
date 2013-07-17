var educationTypes = JSDATA["educationTypes"].evalJSON();

function addEducationTypesTableRow() {
  var table = getIxTableById('educationTypesTable');
  var rowIndex = table.addRow([ '', '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noEducationTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var educationTypesTable = new IxTable($('educationTypesTable'), {
    id : "educationTypesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.educationTypes.educationTypesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.educationTypes.educationTypesTableNameHeader"),
          left : 38,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("settings.educationTypes.educationTypesTableCodeHeader"),
          left : 346,
          right : 44,
          dataType : 'text',
          editable : false,
          paramName : 'code',
          required : true
        },
        {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.educationTypes.educationTypesTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var educationTypeId = table.getCellValue(event.row, table.getNamedColumnIndex('educationTypeId'));
            var educationTypeName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.educationTypes.educationTypeArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(educationTypeName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.educationTypes.educationTypeArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.educationTypes.educationTypeArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.educationTypes.educationTypeArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archiveeducationtype.json", {
                    parameters : {
                      educationTypeId : educationTypeId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('educationTypesTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.educationTypes.educationTypesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noEducationTypesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'educationTypeId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = educationTypes.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(educationTypes[i].name), jsonEscapeHTML(educationTypes[i].code), '', '', educationTypes[i].id, 0 ]);
  }

  educationTypesTable.addRows(rows);

  if (educationTypesTable.getRowCount() > 0) {
    $('noEducationTypesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
