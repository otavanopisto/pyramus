function addStudentActivityTypesTableRow() {
  var table = getIxTableById('studentActivityTypesTable');
  var rowIndex = table.addRow([ '', '' ]);
  for (var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  $('noStudentActivityTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('studentActivityTypesTable'), {
    id : "studentActivityTypesTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.studentactivitytypes.studentActivityTypesTableNameHeader"),
      left : 8,
      right : 46,
      dataType : 'text',
      editable : false,
      paramName : 'name',
      required : true
    }, {
      right : 8,
      width : 30,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("system.setupwizard.studentactivitytypes.studentActivityTypesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noStudentActivityTypesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
}
