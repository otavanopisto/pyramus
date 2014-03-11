function addLanguagesTableRow() {
  getIxTableById('languagesTable').addRow([ '', '', '']);
  $('noLanguagesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('languagesTable'), {
    id : "languagesTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.languages.languagesTableCodeHeader"),
      left : 8,
      width: 75,
      dataType : 'text',
      paramName : 'code',
      required : true,
      editable: true
    }, {
      header : getLocale().getText("system.setupwizard.languages.languagesTableNameHeader"),
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
      tooltip : getLocale().getText("system.setupwizard.languages.languagesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noLanguagesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
}
