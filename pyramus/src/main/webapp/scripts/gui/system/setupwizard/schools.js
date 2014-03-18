function addSchoolsTableRow() {
  var table = getIxTableById('schoolsTable');
  var rowIndex = table.addRow([ '', '', '', '' ]);
  for (var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  $('noSchoolsAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  var schoolFields = JSDATA["schoolFields"].evalJSON();
  var schoolFieldOptions = [schoolFields.length];
  
  for (var i = 0, l = schoolFields.length; i < l; i++) {
    schoolFieldOptions.push({
      text: schoolFields[i].name,
      value: schoolFields[i].id
    });
  }
  
  new IxTable($('schoolsTable'), {
    id : "schoolsTable",
    columns : [ {
      header : getLocale().getText("system.setupwizard.schools.schoolsTableCodeHeader"),
      left : 8,
      width : 75,
      dataType : 'text',
      paramName : 'code'
    }, {
      header : getLocale().getText("system.setupwizard.schools.schoolsTableFieldHeader"),
      left : 91,
      width: 300,
      dataType : 'select',
      paramName : 'field',
      required : true,
      options: schoolFieldOptions
    }, {
      header : getLocale().getText("system.setupwizard.schools.schoolsTableNameHeader"),
      left : 399,
      right : 46,
      dataType : 'text',
      paramName : 'name',
      required : true
    }, {
      right : 8,
      width : 30,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("system.setupwizard.schools.schoolsTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noSchoolsAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
  
  
  addSchoolsTableRow();
}
