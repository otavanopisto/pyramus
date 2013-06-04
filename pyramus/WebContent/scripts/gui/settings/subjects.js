
var subjects = JSDATA["subjects"].evalJSON();
var educationTypes = JSDATA["educationTypes"].evalJSON();

function addSubjectsTableRow() {
  var table = getIxTableById('subjectsTable');
  var rowIndex = table.addRow([ '', '', '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noSubjectsAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var subjectsTable = new IxTable($('subjectsTable'), {
    id : "subjectsTable",
    columns : [
        {
          left : 8,
          width : 22,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.subjects.subjectsTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            table.setCellEditable(event.row, table.getNamedColumnIndex('code'), table.isCellEditable(event.row, table.getNamedColumnIndex('code')) == false);
            table.setCellEditable(event.row, table.getNamedColumnIndex('name'), table.isCellEditable(event.row, table.getNamedColumnIndex('name')) == false);
            table.setCellEditable(event.row, table.getNamedColumnIndex('educationTypeId'), table.isCellEditable(event.row, table
                .getNamedColumnIndex('educationTypeId')) == false);

            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.subjects.subjectsTableCodeHeader"),
          left : 8 + 22 + 8,
          width : 100,
          dataType : 'text',
          editable : false,
          paramName : 'code'
        },
        {
          header : getLocale().getText("settings.subjects.subjectsTableNameHeader"),
          left : 8 + 22 + 8 + 100 + 8,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true,
          sortAttributes : {
            sortAscending : {
              toolTip : getLocale().getText("generic.sort.ascending"),
              sortAction : IxTable_ROWSTRINGSORT
            },
            sortDescending : {
              toolTip : getLocale().getText("generic.sort.descending"),
              sortAction : IxTable_ROWSTRINGSORT
            }
          }
        },
        {
          header : getLocale().getText("settings.subjects.educationTypeHeader"),
          left : 8 + 22 + 8 + 100 + 8 + 300 + 8,
          right : 8 + 22 + 8,
          dataType : 'select',
          editable : false,
          paramName : 'educationTypeId',
          options : (function() {
            var result = [ {
              text : '-',
              value : ''
            } ];
            for ( var i = 0, l = educationTypes.length; i < l; i++) {
              result.push({
                text : educationTypes[i].name,
                value : educationTypes[i].id
              });
            }
            return result;
          })(),
          sortAttributes : {
            sortAscending : {
              toolTip : getLocale().getText("generic.sort.ascending"),
              sortAction : IxTable_ROWSELECTSORT
            },
            sortDescending : {
              toolTip : getLocale().getText("generic.sort.descending"),
              sortAction : IxTable_ROWSELECTSORT
            }
          }
        },
        {
          right : 8,
          width : 22,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.subjects.subjectsTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var subjectId = table.getCellValue(event.row, table.getNamedColumnIndex('subjectId'));
            var subjectName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.subjects.subjectArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(subjectName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.subjects.subjectArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.subjects.subjectArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.subjects.subjectArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivesubject.json", {
                    parameters : {
                      subjectId : subjectId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('subjectsTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.subjects.subjectsTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noSubjectsAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'subjectId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = subjects.length; i < l; i++) {
    var subject = subjects[i];
    rows.push([ '', jsonEscapeHTML(subject.code), jsonEscapeHTML(subject.name), subject.educationTypeId, '', '', subject.id, 0 ]);
  }
  subjectsTable.addRows(rows);

  if (subjectsTable.getRowCount() > 0) {
    $('noSubjectsAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
