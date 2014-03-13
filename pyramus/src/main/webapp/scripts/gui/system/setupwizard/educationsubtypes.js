var educationTypes = JSDATA["educationTypes"].evalJSON();

function addEducationSubtypesTableRow() {
  var table = getIxTableById('educationSubtypesTable');
  var rowIndex = table.addRow([ '', -1, '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noEducationSubtypesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('archiveButton'));
}

function onLoad(event) {
  var educationSubtypesTable = new IxTable($('educationSubtypesTable'), {
    id : "educationSubtypesTable",
    columns : [
        {
          left : 8,
          width : 22,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.educationSubtypes.educationSubtypesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            table.setCellEditable(event.row, table.getNamedColumnIndex('code'), table.isCellEditable(event.row, table.getNamedColumnIndex('code')) == false);
            table.setCellEditable(event.row, table.getNamedColumnIndex('name'), table.isCellEditable(event.row, table.getNamedColumnIndex('name')) == false);

            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.educationSubtypes.educationTypeHeader"),
          left : 8 + 22 + 8,
          width : 150,
          dataType : 'select',
          editable : false,
          paramName : 'educationTypeId',
          options : (function() {
            var result = [ {
              text : '-',
              value : ''
            } ];
            for ( var i = 0, l = educationTypes.length; i < l; i++) {
              result.push({
                text : educationTypes[i].name,
                value : educationTypes[i].id
              });
            }
            return result;
          })(),
          sortAttributes : {
            sortAscending : {
              toolTip : getLocale().getText("generic.sort.ascending"),
              sortAction : IxTable_ROWSELECTSORT
            },
            sortDescending : {
              toolTip : getLocale().getText("generic.sort.descending"),
              sortAction : IxTable_ROWSELECTSORT
            }
          }
        },
        {
          header : getLocale().getText("settings.educationSubtypes.educationSubtypesTableCodeHeader"),
          left : 8 + 22 + 8 + 150 + 8,
          width : 150,
          dataType : 'text',
          editable : false,
          paramName : 'code'
        },
        {
          header : getLocale().getText("settings.educationSubtypes.educationSubtypesTableNameHeader"),
          left : 8 + 22 + 8 + 150 + 8 + 150 + 8,
          right : 8 + 30 + 8,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true,
          sortAttributes : {
            sortAscending : {
              toolTip : getLocale().getText("generic.sort.ascending"),
              sortAction : IxTable_ROWSTRINGSORT
            },
            sortDescending : {
              toolTip : getLocale().getText("generic.sort.descending"),
              sortAction : IxTable_ROWSTRINGSORT
            }
          }
        }, {
          right : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : getLocale().getText("settings.educationSubtypes.educationSubtypesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noEducationSubtypesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : false
        }, {
          dataType : 'hidden',
          paramName : 'educationSubtypeId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0; i < educationTypes.length; i++) {
    var educationType = educationTypes[i];
    for ( var j = 0; j < educationType.subtypes.length; j++) {
      var educationSubtype = educationType.subtypes[j];
      rows.push([ '', educationType.id, jsonEscapeHTML(educationSubtype.name), jsonEscapeHTML(educationSubtype.code), '', educationSubtype.id, 0 ]);
    }
  }
  educationSubtypesTable.addRows(rows);

  if (educationSubtypesTable.getRowCount() > 0) {
    $('noEducationSubtypesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
