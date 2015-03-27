function addCourseTypesTableRow() {
  getIxTableById('courseTypesTable').addRow([ '', '']);
  $('noCourseTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('courseTypesTable'), {
    id : "courseTypesTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.coursetypes.courseTypesTableNameHeader"),
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
      tooltip : getLocale().getText("system.setupwizard.coursetypes.courseTypesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noCourseTypesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
  
  addCourseTypesTableRow();
}
