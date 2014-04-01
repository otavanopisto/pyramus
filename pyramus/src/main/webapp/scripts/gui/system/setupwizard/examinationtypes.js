function addExaminationTypesTableRow() {
  getIxTableById('examinationTypesTable').addRow([ '', '']);
  $('noExaminationTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('examinationTypesTable'), {
    id : "examinationTypesTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.examinationtypes.examinationTypesTableNameHeader"),
      left : 8,
      right: 48,
      dataType : 'text',
      paramName : 'name',
      required : true,
      editable: true
    }, {
      right : 8,
      width : 30,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("system.setupwizard.examinationtypes.examinationTypesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noExaminationTypesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
  
  addExaminationTypesTableRow();
}
