function addMunicipalitiesTableRow() {
  getIxTableById('municipalitiesTable').addRow([ '', '', '']);
  $('noMunicipalitiesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('municipalitiesTable'), {
    id : "municipalitiesTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.municipalities.municipalitiesTableCodeHeader"),
      left : 8,
      width: 75,
      dataType : 'text',
      paramName : 'code',
      required : true,
      editable: true
    }, {
      header : getLocale().getText("system.setupwizard.municipalities.municipalitiesTableNameHeader"),
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
      tooltip : getLocale().getText("system.setupwizard.municipalities.municipalitiesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noMunicipalitiesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
  
  addMunicipalitiesTableRow();
}
