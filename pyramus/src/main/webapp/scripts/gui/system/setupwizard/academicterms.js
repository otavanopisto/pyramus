
function addTermsTableRow() {
  var table = getIxTableById('termsTable');
  var rowIndex = table.addRow([ '', '', '', '',]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noTermsAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  var termsTable = new IxTable($('termsTable'), {
    id : "termsTable",
    columns : [
        {
          header : getLocale().getText("system.setupwizard.academicterms.termsTableNameHeader"),
          left : 38,
          width : 250,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("system.setupwizard.academicterms.termsTableStartDateHeader"),
          left : 296,
          width : 150,
          dataType : 'date',
          editable : false,
          paramName : 'startDate',
          required : true
        },
        {
          header : getLocale().getText("system.setupwizard.academicterms.termsTableEndDateHeader"),
          left : 438,
          right : 30,
          dataType : 'date',
          editable : false,
          paramName : 'endDate',
          required : true
        }, {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : getLocale().getText("system.setupwizard.academicterms.termsTableRemoveRowTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noTermsAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : false
        } ]
  });

  addTermsTableRow();
}
