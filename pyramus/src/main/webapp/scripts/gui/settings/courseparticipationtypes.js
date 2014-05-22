var courseParticipationTypes = JSDATA["courseParticipationTypes"].evalJSON();
var initialCourseParticipationType = JSDATA["initialCourseParticipationType"].evalJSON();


function addCourseParticipationTypesTableRow() {
  var table = getIxTableById('courseParticipationTypesTable');
  var rowIndex = table.addRow([ '', false, '', '', '', -1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  if (rowIndex == 0) {
    table.setCellValue(0, table.getNamedColumnIndex("initialType"), true);
  }

  $('noCourseParticipationTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var courseParticipationTypesTable = new IxTable($('courseParticipationTypesTable'), {
    id : "courseParticipationTypesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.courseParticipationTypes.courseParticipationTypesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
          }
        },
        {
          header : getLocale().getText("settings.courseParticipationTypes.courseParticipationTypesTableInitialTypeHeader"),
          left : 38,
          width : 80,
          dataType : 'checkbox',
          editable : false,
          paramName : 'initialType'
        },
        {
          header : getLocale().getText("settings.courseParticipationTypes.courseParticipationTypesTableNameHeader"),
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
          tooltip : getLocale().getText("settings.courseParticipationTypes.courseParticipationTypesTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var courseParticipationTypeId = table.getCellValue(event.row, table.getNamedColumnIndex('courseParticipationTypeId'));
            var courseParticipationTypeName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath
                + "/simpledialog.page?localeId=settings.courseParticipationTypes.courseParticipationTypeArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(courseParticipationTypeName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.courseParticipationTypes.courseParticipationTypeArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.courseParticipationTypes.courseParticipationTypeArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.courseParticipationTypes.courseParticipationTypeArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivecourseparticipationtype.json", {
                    parameters : {
                      courseParticipationTypeId : courseParticipationTypeId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('courseParticipationTypesTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.courseParticipationTypes.courseParticipationTypesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noCourseParticipationTypesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'courseParticipationTypeId'
        } ]
  });

  courseParticipationTypesTable.addListener("cellValueChange", function(event) {
    if ((event.value == true) && !this._settingValue) {
      var initialTypeColumn = event.tableComponent.getNamedColumnIndex("initialType");

      if (initialTypeColumn == event.column) {
        this._settingValue = true;
        try {
          for ( var i = 0, l = event.tableComponent.getRowCount(); i < l; i++) {
            if (i != event.row) {
              event.tableComponent.setCellValue(i, initialTypeColumn, false);
            } else {
              event.tableComponent.setCellValue(i, initialTypeColumn, true);
            }
          }
        } finally {
          this._settingValue = false;
        }
      }
    }
  });

  var rows = new Array();
  for ( var i = 0, l = courseParticipationTypes.length; i < l; i++) {
    rows.push([ '',
                courseParticipationTypes[i].id == initialCourseParticipationType.id, 
                jsonEscapeHTML(courseParticipationTypes[i].name), 
                '', 
                '',
                courseParticipationTypes[i].id ]);
  }
  courseParticipationTypesTable.addRows(rows);

  if (courseParticipationTypesTable.getRowCount() > 0) {
    $('noCourseParticipationTypesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
