function addCourseParticipationTypesTableRow() {
  var table = getIxTableById('courseParticipationTypesTable');
  var rowIndex = table.addRow([ '', false, '', '' ]);
  for (var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  if (rowIndex == 0) {
    table.setCellValue(0, table.getNamedColumnIndex("initialType"), true);
  }

  $('noCourseParticipationTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  var courseParticipationTypesTable = new IxTable($('courseParticipationTypesTable'), {
    id : "courseParticipationTypesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("system.setupwizard.courseparticipationtypes.courseParticipationTypesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for (var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
          }
        }, {
          header : getLocale().getText("system.setupwizard.courseparticipationtypes.courseParticipationTypesTableInitialTypeHeader"),
          left : 38,
          width : 80,
          dataType : 'checkbox',
          editable : false,
          paramName : 'initialType'
        }, {
          header : getLocale().getText("system.setupwizard.courseparticipationtypes.courseParticipationTypesTableNameHeader"),
          left : 234,
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
