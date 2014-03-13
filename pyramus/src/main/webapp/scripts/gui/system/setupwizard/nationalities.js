function addNationalitiesTableRow() {
  getIxTableById('nationalitiesTable').addRow([ '', '', '']);
  $('noNationalitiesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('nationalitiesTable'), {
    id : "nationalitiesTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.nationalities.nationalitiesTableCodeHeader"),
      left : 8,
      width: 75,
      dataType : 'number',
      paramName : 'code',
      required : true,
      editable: true
    }, {
      header : getLocale().getText("system.setupwizard.nationalities.nationalitiesTableNameHeader"),
      left : 91,
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
      tooltip : getLocale().getText("system.setupwizard.nationalities.nationalitiesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noContactTypesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
  
  addNationalitiesTableRow();
}
