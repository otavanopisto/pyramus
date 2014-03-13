var educationTypes = JSDATA["educationTypes"].evalJSON();

function addEducationSubtypesTableRow() {
  getIxTableById('educationSubtypesTable').addRow([ -1, '', '', '', -1, 1 ]);
  $('noEducationSubtypesAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  var educationSubtypesTable = new IxTable($('educationSubtypesTable'), {
    id : "educationSubtypesTable",
    columns : [
      {
        header : getLocale().getText("system.setupwizard.educationsubtypes.educationTypeHeader"),
        left : 8,
        width : 150,
        dataType : 'select',
        editable : true,
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
        })()
      },
      {
        header : getLocale().getText("system.setupwizard.educationsubtypes.educationSubtypesTableCodeHeader"),
        left : 8 + 150 + 8,
        width : 150,
        dataType : 'text',
        editable : true,
        paramName : 'code'
      },
      {
        header : getLocale().getText("system.setupwizard.educationsubtypes.educationSubtypesTableNameHeader"),
        left : 8 + 150 + 8 + 150 + 8,
        right : 8 + 30 + 8,
        dataType : 'text',
        editable : true,
        paramName : 'name',
        required : true
      }, {
        right : 8,
        width : 30,
        dataType : 'button',
        imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
        tooltip : getLocale().getText("system.setupwizard.educationsubtypes.educationSubtypesTableRemoveTooltip"),
        onclick : function(event) {
          event.tableComponent.deleteRow(event.row);
          if (event.tableComponent.getRowCount() == 0) {
            $('noEducationSubtypesAddedMessageContainer').setStyle({
              display : ''
            });
          }
        },
        paramName : 'removeButton'
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
