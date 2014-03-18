function addSchoolFieldsTableRow() {
  getIxTableById('schoolFieldsTable').addRow([ '', '']);
  $('noSchoolFieldsAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('schoolFieldsTable'), {
    id : "schoolFieldsTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.schoolfields.schoolFieldsTableNameHeader"),
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
      tooltip : getLocale().getText("system.setupwizard.schoolfields.schoolFieldsTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noSchoolFieldsAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
  
  addSchoolFieldsTableRow();
}
