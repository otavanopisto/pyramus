var courseStates = JSDATA["courseStates"].evalJSON();
var initialCourseState = JSDATA["initialCourseState"].evalJSON();


function addCourseStatesTableRow() {
  var table = getIxTableById('courseStatesTable');
  var rowIndex = table.addRow([ '', false, '', '', '', -1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  if (rowIndex == 0) {
    table.setCellValue(0, table.getNamedColumnIndex("initialState"), true);
  }

  $('noCourseStatesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var courseStatesTable = new IxTable($('courseStatesTable'), {
    id : "courseStatesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.courseStates.courseStatesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
          }
        },
        {
          header : getLocale().getText("settings.courseStates.courseStatesTableInitialStateHeader"),
          left : 38,
          width : 80,
          dataType : 'checkbox',
          editable : false,
          paramName : 'initialState'
        },
        {
          header : getLocale().getText("settings.courseStates.courseStatesTableNameHeader"),
          left : 234,
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
          tooltip : getLocale().getText("settings.courseStates.courseStatesTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var courseStateId = table.getCellValue(event.row, table.getNamedColumnIndex('courseStateId'));
            var courseStateName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.courseStates.courseStateArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(courseStateName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.courseStates.courseStateArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.courseStates.courseStateArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.courseStates.courseStateArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivecoursestate.json", {
                    parameters : {
                      courseStateId : courseStateId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('courseStatesTable').deleteRow(archivedRowIndex);
                      saveFormDraft();
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
          tooltip : getLocale().getText("settings.courseStates.courseStatesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noCourseStatesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'courseStateId'
        } ]
  });

  courseStatesTable.addListener("cellValueChange", function(event) {
    if ((event.value == true) && !this._settingValue) {
      var initialStateColumn = event.tableComponent.getNamedColumnIndex("initialState");

      if (initialStateColumn == event.column) {
        this._settingValue = true;
        try {
          for ( var i = 0, l = event.tableComponent.getRowCount(); i < l; i++) {
            if (i != event.row) {
              event.tableComponent.setCellValue(i, initialStateColumn, false);
            } else {
              event.tableComponent.setCellValue(i, initialStateColumn, true);
            }
          }
        } finally {
          this._settingValue = false;
        }
      }
    }
  });

  var rows = new Array();
  for ( var i = 0, l = courseStates.length; i < l; i++) {
    rows.push([ '', courseStates[i].id == initialCourseState.id, jsonEscapeHTML(courseStates[i].name), '', '', courseStates[i].id ]);
  }
  courseStatesTable.addRows(rows);

  if (courseStatesTable.getRowCount() > 0) {
    $('noCourseStatesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
