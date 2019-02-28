var studyEndReasons = JSDATA["studyEndReasons"].evalJSON();
var reasonsInUse = JSDATA["reasonsInUse"].evalJSON();

var deletedRowIndex;

function addStudyEndReasonsTableRow() {
  var table = getIxTableById('studyEndReasonsTable');
  var rowIndex = table.addRow([ '', '', '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noStudyEndReasonsAddedMessageContainer').setStyle({
    display : 'none'
  });
  updateParentDropdownBoxesAndDeleteButtons();
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('deleteButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('editPropertiesButton'));
}

function updateParentDropdownBoxesAndDeleteButtons() {
  var table = getIxTableById('studyEndReasonsTable');
  var selectController = IxTableControllers.getController('select');
  var buttonController = IxTableControllers.getController('button');
  
  var parentReasonIdColumn = table.getNamedColumnIndex('parentReasonId');
  var studyEndReasonIdColumn = table.getNamedColumnIndex('studyEndReasonId');
  
  for (var i = 0; i < table.getRowCount(); i++) {
    var selectHandlerInstance = table.getCellEditor(i, parentReasonIdColumn);
    selectController.removeAllOptions(selectHandlerInstance);
    selectController.addOption(selectHandlerInstance, "", "-");
    var isTopLevel = false;

    // reasons with that are in use have disabled delete buttons
    for (var j = 0; j < reasonsInUse.length; j++) {
      var buttonHandlerInstance = table.getCellEditor(i, table.getNamedColumnIndex('deleteButton'));
      if (reasonsInUse[j].id == table.getCellValue(i, studyEndReasonIdColumn)) {
        buttonController.disableEditor(buttonHandlerInstance);
        break;
      } else {
        buttonController.enableEditor(buttonHandlerInstance);
      }
    }

    // reasons with children can not have parents, and have disabled delete buttons
    for (var j = 0; j < table.getRowCount(); j++) {
      var currentReasonId = table.getCellValue(i, studyEndReasonIdColumn);
      var possibleChildReasonsParentId = table.getCellValue(j, parentReasonIdColumn);
      if (currentReasonId == possibleChildReasonsParentId) {
        var buttonHandlerInstance = table.getCellEditor(i, table.getNamedColumnIndex('deleteButton'));
        buttonController.disableEditor(buttonHandlerInstance);
        isTopLevel = true;
        break;
      }
    }
    if (!isTopLevel) {
      for ( var j = 0; j < table.getRowCount(); j++) {
        var currentReasonId = table.getCellValue(i, studyEndReasonIdColumn);
        var possibleParentReasonId = table.getCellValue(j, studyEndReasonIdColumn);
        var possibleParentReasonsParentId = table.getCellValue(j, parentReasonIdColumn);
        var possibleParentReasonName = table.getCellValue(j, 1);
        if ((currentReasonId != possibleParentReasonId) && (!possibleParentReasonsParentId)) {
          selectController.addOption(selectHandlerInstance, possibleParentReasonId, possibleParentReasonName);
        }
      }
    }
  }
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var studyEndReasonsTable = new IxTable($('studyEndReasonsTableContainer'), {
    id : "studyEndReasonsTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.studyEndReasons.studyEndReasonsTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
            updateParentDropdownBoxesAndDeleteButtons();
          }
        },
        {
          header : getLocale().getText("settings.studyEndReasons.studyEndReasonsTableNameHeader"),
          left : 38,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("settings.studyEndReasons.parentReasonHeader"),
          left : 8 + 22 + 8 + 300 + 8,
          right : 8 + 22 + 8 + 22 + 8,
          dataType : 'select',
          editable : false,
          paramName : 'parentReasonId',
          dynamicOptions : true
        }, {
          width: 30,
          right: 8 + 22 + 8,
          dataType: 'button',
          paramName : 'editPropertiesButton',
          imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip: getLocale().getText("settings.studyEndReasons.editPropertiesTooltip"),
          onclick: function (event) {
            var table = event.tableComponent;
            var studyEndReasonId = table.getCellValue(event.row, table.getNamedColumnIndex('studyEndReasonId'));
            
            var dialog = new IxDialog({
              id : 'editStudyEndReasonPropertiesDialog',
              contentURL : GLOBAL_contextPath + '/settings/studyendreasonproperties.page?studyEndReasonId=' + studyEndReasonId,
              centered : true,
              showOk : true,
              showCancel : true,
              title: getLocale().getText("settings.studyEndReasons.editPropertiesDialog.title"),
              okLabel: getLocale().getText("generic.dialog.save"),
              cancelLabel: getLocale().getText("generic.dialog.cancel")
            });

            dialog.addDialogListener(function(event) {
              var dlg = event.dialog;
              switch (event.name) {
                case 'okClick':
                  event.preventDefault(true);

                  var contentDoc = dlg.getContentDocument();
                  var uploadForm = contentDoc.getElementById("editStudyEndReasonPropertiesForm");
                  var formData = uploadForm.serialize(true);

                  dlg.disableOkButton();

                  JSONRequest.request("settings/savestudyendreasonproperties.json", {
                    parameters: formData,
                    onSuccess: function (jsonResponse) {
                    },
                    onFailure: function(errorMessage, errorCode, isHttpError, jsonResponse) {
                      alert(errorMessage);
                    }
                  });   
          
                  dlg.close();
                break;
              }
            });
            
            dialog.setSize("400px", "300px");
            dialog.open();
          }
        }, {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.studyEndReasons.studyEndReasonsTableDeleteTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var studyEndReasonId = table.getCellValue(event.row, table.getNamedColumnIndex('studyEndReasonId'));
            var studyEndReasonName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.studyEndReasons.studyEndReasonDeleteConfirmDialogContent&localeParams="
                + encodeURIComponent(studyEndReasonName);

            deletedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.studyEndReasons.studyEndReasonDeleteConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.studyEndReasons.studyEndReasonDeleteConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.studyEndReasons.studyEndReasonDeleteConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/deletestudyendreason.json", {
                    parameters : {
                      studyEndReason : studyEndReasonId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('studyEndReasonsTable').deleteRow(deletedRowIndex);
                      updateParentDropdownBoxesAndDeleteButtons();
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
          tooltip : getLocale().getText("settings.studyEndReasons.studyEndReasonsTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noStudyEndReasonsAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'studyEndReasonId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = [];
  for ( var i = 0, l = studyEndReasons.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(studyEndReasons[i].name), studyEndReasons[i].parentId, '', '', '', studyEndReasons[i].id, 0 ]);
  }
  studyEndReasonsTable.addRows(rows);
  updateParentDropdownBoxesAndDeleteButtons();

  if (studyEndReasonsTable.getRowCount() > 0) {
    $('noStudyEndReasonsAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
