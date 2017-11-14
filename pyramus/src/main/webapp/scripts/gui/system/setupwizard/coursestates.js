function addCourseStatesTableRow() {
  var table = getIxTableById('courseStatesTable');
  var rowIndex = table.addRow([ false, '', '' ]);
  for (var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  if (rowIndex == 0) {
    table.setCellValue(0, table.getNamedColumnIndex("initialState"), true);
  }

  $('noCourseStatesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('courseStatesTable'), {
    id : "courseStatesTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.coursestates.courseStatesTableInitialStateHeader"),
      left : 8,
      width : 100,
      dataType : 'radiobutton',
      editable : false,
      paramName : 'initialState'
    }, {
      header : getLocale().getText("system.setupwizard.coursestates.courseStatesTableNameHeader"),
      left : 107,
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
      tooltip : getLocale().getText("system.setupwizard.coursestates.courseStatesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noCourseStatesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
  
  addCourseStatesTableRow();
}
