
function addTermsTableRow() {
  var table = getIxTableById('termsTable');
  var rowIndex = table.addRow([ '', '', '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noTermsAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  
  var academicTerms = JSDATA["academicTerms"].evalJSON();
  
  tabControl = new IxProtoTabs($('tabs'));

  var termsTable = new IxTable($('termsTable'), {
    id : "termsTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.academicTerms.academicTermTableEditAcademicTermTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.academicTerms.termsTableNameHeader"),
          left : 38,
          width : 250,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("settings.academicTerms.termsTableStartDateHeader"),
          left : 296,
          width : 150,
          dataType : 'date',
          editable : false,
          paramName : 'startDate',
          required : true
        },
        {
          header : getLocale().getText("settings.academicTerms.termsTableEndDateHeader"),
          left : 438,
          right : 30,
          dataType : 'date',
          editable : false,
          paramName : 'endDate',
          required : true
        },
        {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.academicTerms.termsTableArchiveRowTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var termId = table.getCellValue(event.row, table.getNamedColumnIndex('termId'));
            var termName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.academicTerms.termArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(termName);
            var archivedRowIndex;

            archivedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.academicTerms.termArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.academicTerms.termArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.academicTerms.termArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archiveacademicterm.json", {
                    parameters : {
                      academicTermId : termId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('termsTable').deleteRow(archivedRowIndex);
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
          tooltip : getLocale().getText("settings.academicTerms.termsTableRemoveRowTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noTermsAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'termId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = academicTerms.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(academicTerms[i].name), academicTerms[i].startDate, academicTerms[i].endDate, '', '', academicTerms[i].id, 0 ]);
  }

  termsTable.addRows(rows);

  if (termsTable.getRowCount() > 0) {
    $('noTermsAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
