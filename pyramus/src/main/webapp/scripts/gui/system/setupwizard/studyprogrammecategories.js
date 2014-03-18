var studyProgrammeCategories = JSDATA["studyProgrammeCategories"].evalJSON();
var educationTypes = JSDATA["educationTypes"].evalJSON();


function addStudyProgrammeCategoriesTableRow() {
  var table = getIxTableById('studyProgrammeCategoriesTable');
  var rowIndex = table.addRow([ '', '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noStudyProgrammeCategoriesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  var studyProgrammeCategoriesTable = new IxTable($('studyProgrammeCategoriesTableContainer'), {
    id : "studyProgrammeCategoriesTable",
    columns : [
        {
          left : 0,
          width : 0,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.studyProgrammeCategories.studyProgrammeCategoriesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          },
          hidden: true
        },
        {
          header : getLocale().getText("settings.studyProgrammeCategories.studyProgrammeCategoriesTableNameHeader"),
          left : 8,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("settings.studyProgrammeCategories.educationTypeHeader"),
          left : 8 + 300 + 8,
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
                text : jsonEscapeHTML(educationTypes[i].name),
                value : educationTypes[i].id
              });
            }
            return result;
          })()
        },
        {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.studyProgrammeCategories.studyProgrammeCategoriesTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var studyProgrammeCategoryId = table.getCellValue(event.row, table.getNamedColumnIndex('studyProgrammeCategoryId'));
            var studyProgrammeCategoryName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath
                + "/simpledialog.page?localeId=settings.studyProgrammeCategories.studyProgrammeCategoryArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(studyProgrammeCategoryName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.studyProgrammeCategories.studyProgrammeCategoryArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.studyProgrammeCategories.studyProgrammeCategoryArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.studyProgrammeCategories.studyProgrammeCategoryArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivestudyprogrammecategory.json", {
                    parameters : {
                      studyProgrammeCategory : studyProgrammeCategoryId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('studyProgrammeCategoriesTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.studyProgrammeCategories.studyProgrammeCategoriesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noStudyProgrammeCategoriesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'studyProgrammeCategoryId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = studyProgrammeCategories.length; i < l; i++) {
    var studyProgrammeCategory = studyProgrammeCategories[i];
    rows.push([ '', jsonEscapeHTML(studyProgrammeCategory.name), studyProgrammeCategory.educationTypeId, '', '', studyProgrammeCategory.id, 0 ]);
  }
  studyProgrammeCategoriesTable.addRows(rows);
  
  addStudyProgrammeCategoriesTableRow();

  if (studyProgrammeCategoriesTable.getRowCount() > 0) {
    $('noStudyProgrammeCategoriesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
