function addStudentEducationalLevelsTableRow() {
  getIxTableById('studentEducationalLevelsTable').addRow([ '', '']);
  $('noStudentEducationalLevelsAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('studentEducationalLevelsTable'), {
    id : "studentEducationalLevelsTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.schoolfields.studentEducationalLevelsTableNameHeader"),
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
      tooltip : getLocale().getText("system.setupwizard.nationalities.studentEducationalLevelsTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noStudentEducationalLevelsAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
}
