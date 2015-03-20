var timeUnits = JSDATA["timeUnits"].evalJSON();
var baseTimeUnit = JSDATA["baseTimeUnit"].evalJSON();

function addTimeUnitsTableRow() {
  var table = getIxTableById('timeUnitsTable');
  var rowIndex = table.addRow([ '', false, 0, '', '', '', '', -1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  if (rowIndex == 0) {
    table.setCellValue(0, table.getNamedColumnIndex("baseUnit"), true);
    table.setCellValue(0, table.getNamedColumnIndex("baseUnits"), 1);
    table.hideCell(0, table.getNamedColumnIndex("baseUnits"));
  }

  $('noTimeUnitsAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var timeUnitsTable = new IxTable($('timeUnitsTable'), {
    id : "timeUnitsTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.timeUnits.timeUnitsTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
          }
        },
        {
          header : getLocale().getText("settings.timeUnits.timeUnitsTableBaseUnitHeader"),
          left : 38,
          width : 80,
          dataType : 'checkbox',
          editable : false,
          paramName : 'baseUnit'
        },
        {
          header : getLocale().getText("settings.timeUnits.timeUnitsTableBaseUnitsHeader"),
          left : 126,
          width : 100,
          dataType : 'number',
          editable : false,
          paramName : 'baseUnits',
          required : true
        },
        {
          header : getLocale().getText("settings.timeUnits.timeUnitsTableSymbolHeader"),
          left : 234,
          width : 30,
          dataType : 'text',
          editable : false,
          paramName : 'symbol',
          required : true
        },
        {
          header : getLocale().getText("settings.timeUnits.timeUnitsTableNameHeader"),
          left : 272,
          right : 46,
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
          tooltip : getLocale().getText("settings.timeUnits.timeUnitsTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var timeUnitId = table.getCellValue(event.row, table.getNamedColumnIndex('timeUnitId'));
            var timeUnitName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.timeUnits.timeUnitArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(timeUnitName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.timeUnits.timeUnitArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.timeUnits.timeUnitArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.timeUnits.timeUnitArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivetimeunit.json", {
                    parameters : {
                      timeUnitId : timeUnitId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('timeUnitsTable').deleteRow(archivedRowIndex);
                      saveFormDraft();
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
          tooltip : getLocale().getText("settings.timeUnits.timeUnitsTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noTimeUnitsAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'timeUnitId'
        } ]
  });

  timeUnitsTable.addListener("cellValueChange", function(event) {
    if ((event.value == true) && !this._settingValue) {
      var baseUnitColumn = event.tableComponent.getNamedColumnIndex("baseUnit");
      var baseUnitsColumn = event.tableComponent.getNamedColumnIndex("baseUnits");

      if (baseUnitColumn == event.column) {
        this._settingValue = true;
        try {
          for ( var i = 0, l = event.tableComponent.getRowCount(); i < l; i++) {
            if (i != event.row) {
              event.tableComponent.setCellValue(i, baseUnitColumn, false);
              event.tableComponent.showCell(i, baseUnitsColumn);
            } else {
              event.tableComponent.setCellValue(i, baseUnitColumn, true);
              event.tableComponent.hideCell(i, baseUnitsColumn);
            }
          }
        } finally {
          this._settingValue = false;
        }
      }
    }
  });

  var rowIndex;
  timeUnitsTable.detachFromDom();
  for ( var i = 0, l = timeUnits.length; i < l; i++) {
    var timeUnit = timeUnits[i];
    rowIndex = timeUnitsTable.addRow([ '', timeUnit.id == baseTimeUnit.id, timeUnit.baseUnits, jsonEscapeHTML(timeUnit.symbol), jsonEscapeHTML(timeUnit.name), '', '', timeUnit.id ]);
    if (timeUnit.id == baseTimeUnit.id) {
      timeUnitsTable.hideCell(rowIndex, timeUnitsTable.getNamedColumnIndex("baseUnits"));
    }
    timeUnitsTable.showCell(rowIndex, timeUnitsTable.getNamedColumnIndex('archiveButton'));
  }
  timeUnitsTable.reattachToDom();

  if (timeUnitsTable.getRowCount() > 0) {
    $('noTimeUnitsAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}