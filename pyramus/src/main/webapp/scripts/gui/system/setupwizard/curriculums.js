function addCurriculumTableRow() {
  getIxTableById('curriculumsTable').addRow([ '', '' ]);
  $('noCurriculumsAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  new IxTable($('curriculumsTable'), {
    id : "curriculumsTable",
    columns : [{
      header : getLocale().getText("system.setupwizard.curriculums.curriculumsTableNameHeader"),
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
      tooltip : getLocale().getText("system.setupwizard.curriculums.curriculumsTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noCurriculumsAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton'
    }]
  });
  
  addCurriculumTableRow();
}