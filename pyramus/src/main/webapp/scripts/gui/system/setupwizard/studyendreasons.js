function s4() {
  return Math.floor((1 + Math.random()) * 0x10000)
             .toString(16)
             .substring(1);
}

function guid() {
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
         s4() + '-' + s4() + s4() + s4();
}

function addStudyEndReasonsTableRow() {
  var table = getIxTableById('studyEndReasonsTable');
  var rowIndex = table.addRow([ '', '', '', '', guid() ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noStudyEndReasonsAddedMessageContainer').setStyle({
    display : 'none'
  });
  updateParentDropdownBoxesAndDeleteButtons();
  table.hideCell(rowIndex, table.getNamedColumnIndex('deleteButton'));
}

function updateParentDropdownBoxesAndDeleteButtons() {
  var table = getIxTableById('studyEndReasonsTable');
  var selectController = IxTableControllers.getController('select');
  var buttonController = IxTableControllers.getController('button');
  for (var i = 0; i < table.getRowCount(); i++) {
    var selectHandlerInstance = table.getCellEditor(i, 2);
    selectController.removeAllOptions(selectHandlerInstance);
    selectController.addOption(selectHandlerInstance, "", "-");

    for (var j = 0; j < table.getRowCount(); j++) {
      var currentReasonId = table.getCellValue(j, 4);
      var currentReasonName = table.getCellValue(j, 1);
      if (i != j) {
        selectController.addOption(selectHandlerInstance, currentReasonId, currentReasonName);
      }
    }

    var isTopLevel = false;

    // reasons with children can not have parents, and have disabled delete buttons
    for (var j = 0; j < table.getRowCount(); j++) {
      var currentReasonId = table.getCellValue(i, 4);
      var possibleChildReasonsParentId = table.getCellValue(j, 1);
      if (currentReasonId == possibleChildReasonsParentId) {
        var buttonHandlerInstance = table.getCellEditor(i, table.getNamedColumnIndex('deleteButton'));
        buttonController.disableEditor(buttonHandlerInstance);
        isTopLevel = true;
        break;
      }
    }
    if (!isTopLevel) {
      for ( var j = 0; j < table.getRowCount(); j++) {
        var currentReasonId = table.getCellValue(i, 4);
        var possibleParentReasonId = table.getCellValue(j, 4);
        var possibleParentReasonsParentId = table.getCellValue(j, 1);
        var possibleParentReasonName = table.getCellValue(j, 1);
        if ((currentReasonId != possibleParentReasonId) && (!possibleParentReasonsParentId)) {
          selectController.addOption(selectHandlerInstance, possibleParentReasonId, possibleParentReasonName);
        }
      }
    }
  }
}

function onLoad(event) {
  var studyEndReasonsTable = new IxTable($('studyEndReasonsTableContainer'), {
    id : "studyEndReasonsTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("system.setupwizard.studyendreasons.studyEndReasonsTableEditTooltip"),
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
          header : getLocale().getText("system.setupwizard.studyendreasons.studyEndReasonsTableNameHeader"),
          left : 38,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("system.setupwizard.studyendreasons.parentReasonHeader"),
          left : 8 + 22 + 8 + 300 + 8,
          right : 8 + 22 + 8,
          dataType : 'select',
          editable : false,
          paramName : 'parentGuid',
          dynamicOptions : true
        }, {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : getLocale().getText("system.setupwizard.studyendreasons.studyEndReasonsTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noStudyEndReasonsAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : false
        }, {
          dataType : 'hidden',
          paramName : 'guid'
        } ]
  });

  updateParentDropdownBoxesAndDeleteButtons();
}
