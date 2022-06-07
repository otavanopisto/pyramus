var deletedRowIndex;
var subjectChoices = JSDATA["subjectChoices"].evalJSON(); 
var subjectChoiceKeys = JSDATA["subjectChoiceKeys"].evalJSON(); 
var subjects = JSDATA["subjects"].evalJSON();
var passFailGradeOptions = JSDATA["passFailGradeOptions"].evalJSON();
var studentId = JSDATA["studentId"].evalJSON();
var studentName = JSDATA["studentName"];
var educationType = JSDATA["educationType"];

function addSubjectChoicesTableRow() {
  var table = getIxTableById('subjectChoicesTable');
  var rowIndex = table.addRow([ '', '', '', '', '', '', studentId, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noSubjectChoicesAddedMessageContainer').setStyle({
    display : 'none'
  });
  updateTable(undefined);
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('deleteButton'));
}

function addPassFailGradeOptionsTableRow() {
  var table = getIxTableById('passFailGradeOptionsTable');
  var rowIndex = table.addRow([ '', '', '', '', studentId, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noPassFailGradeOptionsAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('deleteButton'));
}

function updateTable(event) {
  var table = getIxTableById('subjectChoicesTable');
  var selectController = IxTableControllers.getController('select');
  var lukioAidinkieliId;
  var lukioMatematiikkaId;
  var lukioSmerkintaId;
  var lukioUskontoId;
  for (var i=0, l=subjectChoiceKeys.length; i<l; i++) {
    if (subjectChoiceKeys[i].key === "lukioAidinkieli") {
      lukioAidinkieliId = subjectChoiceKeys[i].id + '';
    }
    if (subjectChoiceKeys[i].key === "lukioMatematiikka") {
      lukioMatematiikkaId = subjectChoiceKeys[i].id + '';
    }
    if (subjectChoiceKeys[i].key === "lukioUskonto") {
      lukioUskontoId = subjectChoiceKeys[i].id + '';
    }
    if (subjectChoiceKeys[i].key === "lukioSmerkinta") {
      lukioSmerkintaId = subjectChoiceKeys[i].id + '';
    }
  }
  
  for (var i=0, l=table.getRowCount(); i<l; i++) {
    if (table.getCellValue(i, 1) !== table.getCellValue(i, 5)) { // skip unchanged lines
      var selectHandlerInstance = table.getCellEditor(i, 2);
      selectController.removeAllOptions(selectHandlerInstance);
      switch (table.getCellValue(i, 1)) {
        case lukioAidinkieliId:
          if (educationType === 'peruskoulu') {
            selectController.addOption(selectHandlerInstance, "äi", "äi (Suomi)");
            selectController.addOption(selectHandlerInstance, "s2", "s2 (Suomi toisena kielenä)");
          } else {
            selectController.addOption(selectHandlerInstance, "ÄI", "ÄI (Suomi)");
            selectController.addOption(selectHandlerInstance, "S2", "S2 (Suomi toisena kielenä)");
          }
        break;
        case lukioMatematiikkaId:
          if (educationType === 'peruskoulu') {
            selectController.addOption(selectHandlerInstance, "", "Ei käytössä peruskoululaisille");
          } else {
            selectController.addOption(selectHandlerInstance, "MAA", "MAA (Pitkä matematiikka)");
            selectController.addOption(selectHandlerInstance, "MAB", "MAB (Lyhyt matematiikka)");
          }
        break;
        case lukioUskontoId:
          if (educationType === 'peruskoulu') {
            selectController.addOption(selectHandlerInstance, "ue", "ue (Uskonto, evankelisluterilainen)");
            selectController.addOption(selectHandlerInstance, "uo", "uo (Uskonto, ortodoksinen)");
            selectController.addOption(selectHandlerInstance, "et", "et (Elämänkatsomustieto)");
          } else {
            selectController.addOption(selectHandlerInstance, "UE", "UE (Uskonto, evankelisluterilainen)");
            selectController.addOption(selectHandlerInstance, "UO", "UO (Uskonto, ortodoksinen)");
            selectController.addOption(selectHandlerInstance, "UI", "UI (Uskonto, islam)");
            selectController.addOption(selectHandlerInstance, "UK", "UK (Uskonto, katolinen)");
            selectController.addOption(selectHandlerInstance, "UJ", "UJ (Uskonto, juutalainen)");
            selectController.addOption(selectHandlerInstance, "UX", "UX (Uskonto, muu)");
            selectController.addOption(selectHandlerInstance, "ET", "ET (Elämänkatsomustieto)");
          }
        break;
        case lukioSmerkintaId:
          for (var i=0, l=subjects.length; i<l; i++) {
            selectController.addOption(selectHandlerInstance, subjects[i].id + "", subjects[i].name);
          }
        break;
        default:
          if (educationType === 'peruskoulu') {
            selectController.addOption(selectHandlerInstance, "ena", "Englanti (ena)");
            selectController.addOption(selectHandlerInstance, "eab", "Espanja (eab)");
            selectController.addOption(selectHandlerInstance, "iab", "Italia (iab)");
            selectController.addOption(selectHandlerInstance, "raa", "Ranska (raa)");
            selectController.addOption(selectHandlerInstance, "rab", "Ranska (rab)");
            selectController.addOption(selectHandlerInstance, "rua", "Ruotsi (rua)");
            selectController.addOption(selectHandlerInstance, "rub", "Ruotsi (rub)");
            selectController.addOption(selectHandlerInstance, "saa", "Saksa (saa)");
            selectController.addOption(selectHandlerInstance, "sab", "Saksa (sab)");
            selectController.addOption(selectHandlerInstance, "vea", "Venäjä (vea)");
            selectController.addOption(selectHandlerInstance, "veb", "Venäjä (veb)");
            selectController.addOption(selectHandlerInstance, "smb", "Saame (smb)");
          } else {
            selectController.addOption(selectHandlerInstance, "ENA", "Englanti (ENA)");
            selectController.addOption(selectHandlerInstance, "EAB2", "Espanja (EAB2)");
            selectController.addOption(selectHandlerInstance, "EAB3", "Espanja (EAB3)");
            selectController.addOption(selectHandlerInstance, "IAB2", "Italia (IAB2)");
            selectController.addOption(selectHandlerInstance, "IAB3", "Italia (IAB3)");
            selectController.addOption(selectHandlerInstance, "RAA", "Ranska (RAA)");
            selectController.addOption(selectHandlerInstance, "RAB2", "Ranska (RAB2)");
            selectController.addOption(selectHandlerInstance, "RAB3", "Ranska (RAB3)");
            selectController.addOption(selectHandlerInstance, "RUA", "Ruotsi (RUA)");
            selectController.addOption(selectHandlerInstance, "RUB", "Ruotsi (RUB)");
            selectController.addOption(selectHandlerInstance, "RUB1", "Ruotsi (RUB1)");
            selectController.addOption(selectHandlerInstance, "RUB3", "Ruotsi (RUB3)");
            selectController.addOption(selectHandlerInstance, "SAA", "Saksa (SAA)");
            selectController.addOption(selectHandlerInstance, "SAB2", "Saksa (SAB2)");
            selectController.addOption(selectHandlerInstance, "SAB3", "Saksa (SAB3)");
            selectController.addOption(selectHandlerInstance, "VEA", "Venäjä (VEA)");
            selectController.addOption(selectHandlerInstance, "VEB2", "Venäjä (VEB2)");
            selectController.addOption(selectHandlerInstance, "VEB3", "Venäjä (VEB3)");
            selectController.addOption(selectHandlerInstance, "SMB3", "Saame (SMB3)");
            selectController.addOption(selectHandlerInstance, "LAB3", "Latina (LAB3)");
            selectController.addOption(selectHandlerInstance, "ARB3", "Arabia (ARB3)");
            selectController.addOption(selectHandlerInstance, "JPB3", "Japani (JPB3)");
            selectController.addOption(selectHandlerInstance, "KXB3", "Muu kieli (KXB3)");
          }
        break;
      }
      table.setCellValue(i, 5, table.getCellValue(i, 1));
    }
  }
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));
  
  $("pageHeader").innerHTML = "Ainevalinnat - " + studentName;

  var subjectChoicesTable = new IxTable($('subjectChoicesTableContainer'), {
    id : "subjectChoicesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : "Muokkaa",
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : "Aine",
          left : 38,
          width : 300,
          dataType : 'select',
          editable : false,
          paramName : 'subject',
          required : true,
          options : (function() {
            var result = [];
            for (var i=0, l=subjectChoiceKeys.length; i<l; i++) {
              result.push({
                text: subjectChoiceKeys[i].name,
                value: subjectChoiceKeys[i].id
              });
            }
            return result;
          })()
        },
        {
          header : "Valinta",
          left : 8 + 22 + 8 + 300 + 8,
          right : 8 + 22 + 8 + 4,
          dataType : 'select',
          editable : false,
          paramName : 'choice',
          options : [],
          dynamicOptions : true
        },
        {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : "Poista",
          onclick : function(event) {
            var table = event.tableComponent;
            var subjectChoiceKey = table.getCellValue(event.row, table.getNamedColumnIndex('subject'));
            var subjectChoiceValue = table.getCellValue(event.row, table.getNamedColumnIndex('choice'));
            var url = GLOBAL_contextPath + "/simpledialog.page?message=Poistetaanko%20ainevalinta";

            deletedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : "Poistetaanko ainevalinta?",
              okLabel : "OK",
              cancelLabel : "Peruuta",
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("lukio/deletesubjectchoice.json", {
                    parameters : {
                      studentId : studentId,
                      subjectChoiceKey : subjectChoiceKey,
                      subjectChoiceValue : subjectChoiceValue
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('subjectChoicesTable').deleteRow(deletedRowIndex);
                    }
                  });
                break;
              }
            });

            dialog.open();
          },
          paramName : 'deleteButton'
        }, {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : "Poista",
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noSubjectChoicesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'previousValue'
        }, {
          dataType : 'hidden',
          paramName : 'studentId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });
  
  var passFailGradeOptionsTable = new IxTable($('passFailGradeOptionsTableContainer'), {
    id : "passFailGradeOptionsTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : "Muokkaa",
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : "Merkintä",
          left : 38,
          right: 30 + 8 + 8,
          dataType : 'select',
          editable : false,
          paramName : 'subject',
          required : true,
          options : (function() {
            var result = [];
            for (var i=0, l=subjects.length; i<l; i++) {
              result.push({
                text: subjects[i].name,
                value: subjects[i].id
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
          tooltip : "Poista",
          onclick : function(event) {
            var table = event.tableComponent;
            var subject = table.getCellValue(event.row, table.getNamedColumnIndex('subject'));
            var url = GLOBAL_contextPath + "/simpledialog.page?message=Poistetaanko%20S-merkintä";

            deletedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : "Poistetaanko S-merkintä?",
              okLabel : "OK",
              cancelLabel : "Peruuta",
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("lukio/deletepassfailgradeoption.json", {
                    parameters : {
                      subject: subject,
                      studentId: studentId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('passFailGradeOptionsTable').deleteRow(deletedRowIndex);
                    }
                  });
                break;
              }
            });

            dialog.open();
          },
          paramName : 'deleteButton'
        }, {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : "Poista",
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noSubjectChoicesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'studentId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });
  
  subjectChoicesTable.addListener("cellValueChange", updateTable);

  var rows = [];
  for ( var i = 0, l = subjectChoices.length; i < l; i++) {
    rows.push([ '', subjectChoices[i].keyId, subjectChoices[i].value, '', '', subjectChoices[i].value, studentId, 0 ]);
  }
  subjectChoicesTable.addRows(rows);
 
  rows = [];
  for ( var i = 0, l = passFailGradeOptions.length; i < l; i++) {
    rows.push([ '', passFailGradeOptions[i].value, '', '', studentId, 0 ]);
  }
  passFailGradeOptionsTable.addRows(rows);
 
  if (subjectChoicesTable.getRowCount() > 0) {
    $('noSubjectChoicesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
  
  if (passFailGradeOptionsTable.getRowCount() > 0) {
    $('noPassFailGradeOptionsAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}