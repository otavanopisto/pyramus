function addCourseUserRoleTableRow() {
  getIxTableById('courseUserRolesTable').addRow([ '', '' ]);
  $('noCourseUserRolesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('courseUserRolesTable'), {
    id : "courseUserRolesTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.courseuserroles.courseUserRolesTableNameHeader"),
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
      tooltip : getLocale().getText("system.setupwizard.courseuserroles.courseUserRolesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noCourseUsersAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
  
  addCourseUserRoleTableRow();
}