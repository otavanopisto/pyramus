function addEducationTypesTableRow() {
  getIxTableById('educationTypesTable').addRow([ '', '', '', -1, 1 ]);
  
  $('noEducationTypesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  var educationTypesTable = new IxTable($('educationTypesTable'), {
    id : "educationTypesTable",
    columns : [
        {
          header : getLocale().getText("settings.educationTypes.educationTypesTableCodeHeader"),
          left : 8,
          width : 75,
          dataType : 'text',
          editable : true,
          paramName : 'code',
          required : true
        },
        {
          header : getLocale().getText("settings.educationTypes.educationTypesTableNameHeader"),
          left : 91,
          right : 48,
          dataType : 'text',
          editable : true,
          paramName : 'name',
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
          paramName : 'removeButton'
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
