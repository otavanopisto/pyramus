var categories = JSDATA["categories"].evalJSON();

function addStudyProgrammesTableRow() {
  var table = getIxTableById('studyProgrammesTable');
  var rowIndex = table.addRow([ '', '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noStudyProgrammesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  var studyProgrammesTable = new IxTable($('studyProgrammesTable'), {
    id : "studyProgrammesTable",
    columns : [
        {
          left : 8,
          width : 22,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("system.setupwizard.studyprogrammes.studyProgrammesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            table.setCellEditable(event.row, table.getNamedColumnIndex('code'), table.isCellEditable(event.row, table.getNamedColumnIndex('code')) == false);
            table.setCellEditable(event.row, table.getNamedColumnIndex('name'), table.isCellEditable(event.row, table.getNamedColumnIndex('name')) == false);

            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("system.setupwizard.studyprogrammes.studyProgrammesTableNameHeader"),
          left : 38,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          header : getLocale().getText("system.setupwizard.studyprogrammes.studyProgrammesTableCategoryHeader"),
          width : 200,
          left : 346,
          dataType : 'select',
          editable : false,
          paramName : 'category',
          options : (function() {
            var result = [];
            for ( var i = 0, l = categories.length; i < l; i++) {
              result.push({
                text : categories[i].name,
                value : categories[i].id
              });
            }
            return result;
          })()
        },
        {
          header : getLocale().getText("system.setupwizard.studyprogrammes.studyProgrammesTableCodeHeader"),
          left : 554,
          right : 44,
          dataType : 'text',
          editable : false,
          paramName : 'code'
        },
        {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : getLocale().getText("system.setupwizard.studyprogrammes.studyProgrammesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noStudyProgrammesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : false
        }, {
          dataType : 'hidden',
          paramName : 'studyProgrammeId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });
}
