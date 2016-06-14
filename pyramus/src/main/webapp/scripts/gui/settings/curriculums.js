var curriculums = JSDATA["curriculums"].evalJSON();

var deletedRowIndex;

function addCurriculumsTableRow() {
  var table = getIxTableById('curriculumsTable');
  var rowIndex = table.addRow([ '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noCurriculumsAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var curriculumsTable = new IxTable($('curriculumsTableContainer'), {
    id : "curriculumsTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.curriculums.curriculumsTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.curriculums.curriculumsTableNameHeader"),
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
          tooltip : getLocale().getText("settings.curriculums.curriculumsTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var curriculumId = table.getCellValue(event.row, table.getNamedColumnIndex('curriculumId'));
            var name = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.curriculums.curriculumArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(name);

            deletedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.curriculums.curriculumArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.curriculums.curriculumArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.curriculums.curriculumArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivecurriculum.json", {
                    parameters : {
                      curriculumId : curriculumId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('curriculumsTable').deleteRow(deletedRowIndex);
                    }
                  });
                break;
              }
            });

            dialog.open();
          },
          paramName : 'archiveButton'
        }, {
          left : 346,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : getLocale().getText("settings.curriculums.curriculumsTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noCurriculumsAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'curriculumId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = curriculums.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(curriculums[i].name), '', '', curriculums[i].id, 0]);
  }
  curriculumsTable.addRows(rows);

  if (curriculumsTable.getRowCount() > 0) {
    $('noCurriculumsAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
