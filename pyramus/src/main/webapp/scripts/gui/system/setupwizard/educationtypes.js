function addEducationTypesTableRow() {
  var table = getIxTableById('educationTypesTable');
  var rowIndex = table.addRow([ '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noEducationTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
}

function onLoad(event) {
  var educationTypesTable = new IxTable($('educationTypesTable'), {
    id : "educationTypesTable",
    columns : [
        {
          header : getLocale().getText("settings.educationTypes.educationTypesTableNameHeader"),
          left : 8,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("settings.educationTypes.educationTypesTableCodeHeader"),
          left : 316,
          right : 48,
          dataType : 'text',
          editable : false,
          paramName : 'code',
          required : true
        },{
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : getLocale().getText("settings.educationTypes.educationTypesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noEducationTypesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : false
        }, {
          dataType : 'hidden',
          paramName : 'educationTypeId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  if (educationTypesTable.getRowCount() > 0) {
    $('noEducationTypesAddedMessageContainer').setStyle({
      display : 'none'
    });
  } else {
    addEducationTypesTableRow();
  }
}
