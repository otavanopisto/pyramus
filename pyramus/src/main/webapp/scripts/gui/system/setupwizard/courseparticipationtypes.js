function addCourseParticipationTypesTableRow() {
  var table = getIxTableById('courseParticipationTypesTable');
  var rowIndex = table.addRow([ false, '', '' ]);
  if (rowIndex == 0) {
    table.setCellValue(0, table.getNamedColumnIndex("initialType"), true);
  }

  $('noCourseParticipationTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('courseParticipationTypesTable'), {
    id : "courseParticipationTypesTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.courseparticipationtypes.courseParticipationTypesTableInitialTypeHeader"),
      left : 8,
      width : 100,
      dataType : 'checkbox',
      editable : true,
      paramName : 'initialType'
    }, {
      header : getLocale().getText("system.setupwizard.courseparticipationtypes.courseParticipationTypesTableNameHeader"),
      left : 108,
      right : 46,
      dataType : 'text',
      editable : true,
      paramName : 'name',
      required : true
    }, {
      right : 8,
      width : 30,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("system.setupwizard.courseparticipationtypes.courseParticipationTypesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noCourseParticipationTypesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    } ]
  });
}
