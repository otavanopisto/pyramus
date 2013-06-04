var educationTypes = JSDATA["educationTypes"].evalJSON();


function addEducationSubtypesTableRow() {
  var table = getIxTableById('educationSubtypesTable');
  var rowIndex = table.addRow([ '', '', '', '', '', '', -1, 1 ]);
  var educationTypeColumn = table.getNamedColumnIndex('educationTypeId');
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noEducationSubtypesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var educationSubtypesTable = new IxTable($('educationSubtypesTable'), {
    id : "educationSubtypesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.educationSubtypes.educationSubtypesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var educationTypeColumn = table.getNamedColumnIndex('educationTypeId');
            var existingSubtype = table.getCellValue(event.row, table.getNamedColumnIndex('educationSubtypeId')) != -1;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              if (!existingSubtype || i != educationTypeColumn) {
                table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
              }
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.educationSubtypes.educationSubtypesTableEducationTypeHeader"),
          left : 38,
          width : 300,
          dataType : 'select',
          editable : false,
          paramName : 'educationTypeId',
          options : (function() {
            var result = [];
            for ( var i = 0; i < educationTypes.length; i++) {
              result.push({
                text : educationTypes[i].name,
                value : educationTypes[i].id
              });
            }
            return result;
          })()
        },
        {
          header : getLocale().getText("settings.educationSubtypes.educationSubtypesTableNameHeader"),
          left : 338,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("settings.educationSubtypes.educationSubtypesTableCodeHeader"),
          left : 646,
          right : 46,
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
          tooltip : getLocale().getText("settings.educationSubtypes.educationSubtypesTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var educationSubtypeId = table.getCellValue(event.row, table.getNamedColumnIndex('educationSubtypeId'));
            var educationSubtypeName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.educationSubtypes.educationSubtypeArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(educationSubtypeName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.educationSubtypes.educationSubtypeArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.educationSubtypes.educationSubtypeArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.educationSubtypes.educationSubtypeArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archiveeducationsubtype.json", {
                    parameters : {
                      educationSubtypeId : educationSubtypeId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('educationSubtypesTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.educationSubtypes.educationSubtypesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noEducationSubtypesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'educationSubtypeId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0; i < educationTypes.length; i++) {
    var educationType = educationTypes[i];
    for ( var j = 0; j < educationType.subtypes.length; j++) {
      var educationSubtype = educationType.subtypes[j];
      rows.push([ '', educationType.id, jsonEscapeHTML(educationSubtype.name), jsonEscapeHTML(educationSubtype.code), '', '', educationSubtype.id, 0 ]);
    }
  }
  educationSubtypesTable.addRows(rows);

  if (educationSubtypesTable.getRowCount() > 0) {
    $('noEducationSubtypesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
