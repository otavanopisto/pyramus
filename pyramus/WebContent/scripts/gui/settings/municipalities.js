var municipalities = JSDATA["municipalities"].evalJSON();


function addMunicipalityTableRow() {
  var table = getIxTableById('municipalitiesTable');
  var rowIndex = table.addRow([ '', '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noMunicipalitiesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var municipalitiesTable = new IxTable($('municipalitiesTable'), {
    id : "municipalitiesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.municipalities.municipalitiesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.municipalities.municipalitiesTableNameHeader"),
          left : 38,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("settings.municipalities.municipalitiesTableCodeHeader"),
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
          tooltip : getLocale().getText("settings.municipalities.municipalitiesTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var municipalityId = table.getCellValue(event.row, table.getNamedColumnIndex('id'));
            var municipalityName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.municipalities.municipalityArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(municipalityName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.municipalities.municipalityArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.municipalities.municipalityArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.municipalities.municipalityArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivemunicipality.json", {
                    parameters : {
                      municipalityId : municipalityId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('municipalitiesTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.municipalities.municipalitiesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noMunicipalitiesAddedMessageContainer').setStyle({
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
  for ( var i = 0, l = municipalities.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(municipalities[i].name), municipalities[i].code, '', '', municipalities[i].id, 0 ]);
  }
  municipalitiesTable.addRows(rows);

  if (municipalitiesTable.getRowCount() > 0) {
    $('noMunicipalitiesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
