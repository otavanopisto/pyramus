var categories = JSDATA["categories"].evalJSON();
var organizations = JSDATA["organizations"].evalJSON();
var studyProgrammes = JSDATA["studyProgrammes"].evalJSON();


function addStudyProgrammesTableRow() {
  var table = getIxTableById('studyProgrammesTable');
  var rowIndex = table.addRow([ '', '', '', '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noStudyProgrammesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var studyProgrammeTable = new IxTable($('studyProgrammesTableContainer'), {
    id : "studyProgrammesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.studyProgrammes.studyProgrammesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.studyProgrammes.studyProgrammesTableNameHeader"),
          left : 38,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("terms.organization"),
          width : 200,
          left : 346,
          dataType : 'select',
          editable : false,
          paramName : 'organization',
          options : (function() {
            var result = [];
            for ( var i = 0, l = organizations.length; i < l; i++) {
              result.push({
                text : organizations[i].name,
                value : organizations[i].id
              });
            }
            return result;
          })()
        },
        {
          header : getLocale().getText("settings.studyProgrammes.studyProgrammesTableCategoryHeader"),
          width : 200,
          left : 346 + 200 + 8,
          dataType : 'select',
          editable : false,
          paramName : 'category',
          options : (function() {
            var result = [];
            for ( var i = 0, l = categories.length; i < l; i++) {
              result.push({
                text : categories[i].name,
                value : categories[i].id
              });
            }
            return result;
          })()
        },
        {
          header : getLocale().getText("settings.studyProgrammes.studyProgrammesTableCodeHeader"),
          left : 346 + 200 + 8 + 200 + 8,
          right : 8 + 22 + 8 + 100 + 8,
          dataType : 'text',
          editable : false,
          paramName : 'code'
        },
        {
          header : getLocale().getText("settings.studyProgrammes.studyProgrammesTableHasEvaluationFeesHeader"),
          width: 100,
          right : 8 + 22 + 8,
          dataType : 'checkbox',
          editable : false,
          paramName : 'hasEvaluationFees'
        },
        {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.studyProgrammes.studyProgrammesTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var studyProgrammeId = table.getCellValue(event.row, table.getNamedColumnIndex('studyProgrammeId'));
            var studyProgrammeName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.studyProgrammes.studyProgrammeArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(studyProgrammeName);

            var archivedRowIndex;
            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.studyProgrammes.studyProgrammeArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.studyProgrammes.studyProgrammeArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.studyProgrammes.studyProgrammeArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivestudyprogramme.json", {
                    parameters : {
                      studyProgrammeId : studyProgrammeId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('studyProgrammesTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.studyProgrammes.studyProgrammesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noStudyProgrammesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'studyProgrammeId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for (var i = 0, l = studyProgrammes.length; i < l; i++) {
    var studyProgramme = studyProgrammes[i];
    var hasEvaluationFees = studyProgramme.hasEvaluationFees == true ? "1" : "";
    rows.push([ '', jsonEscapeHTML(studyProgramme.name), studyProgramme.organizationId, studyProgramme.categoryId, studyProgramme.code, hasEvaluationFees, '', '', studyProgramme.id, 0 ]);
  }
  studyProgrammeTable.addRows(rows);

  if (studyProgrammeTable.getRowCount() > 0) {
    $('noStudyProgrammesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
