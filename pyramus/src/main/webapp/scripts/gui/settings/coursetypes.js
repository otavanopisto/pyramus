var courseTypes = JSDATA["courseTypes"].evalJSON();

function addCourseTypesTableRow() {
  var table = getIxTableById('courseTypesTable');
  var rowIndex = table.addRow([ '', '', '', '', -1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  $('noCourseTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
  
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var courseTypesTable = new IxTable($('courseTypesTable'), {
    id : "courseTypesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.courseTypes.courseTypesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
          }
        },
        {
          header : getLocale().getText("settings.courseTypes.courseTypesTableNameHeader"),
          left : 38,
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
          tooltip : getLocale().getText("settings.courseTypes.courseTypesTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var courseTypeId = table.getCellValue(event.row, table.getNamedColumnIndex('courseTypeId'));
            var courseTypeName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.courseTypes.courseTypeArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(courseTypeName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.courseTypes.courseTypeArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.courseTypes.courseTypeArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.courseTypes.courseTypeArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivecoursetype.json", {
                    parameters : {
                      courseTypeId : courseTypeId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('courseTypesTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.courseTypes.courseTypesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noCourseTypesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'courseTypeId'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = courseTypes.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(courseTypes[i].name), '', '', courseTypes[i].id ]);
  }
  courseTypesTable.addRows(rows);

  if (courseTypesTable.getRowCount() > 0) {
    $('noCourseTypesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}