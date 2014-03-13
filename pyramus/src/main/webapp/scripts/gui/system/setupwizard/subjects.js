var subjects = JSDATA["subjects"].evalJSON();
var educationTypes = JSDATA["educationTypes"].evalJSON();

function addSubjectsTableRow() {
  getIxTableById('subjectsTable').addRow([ '', '', -1, '' ]);
  $('noSubjectsAddedMessageContainer').setStyle({
    display : 'none'
  });
}

function onLoad(event) {
  var subjectsTable = new IxTable($('subjectsTable'), {
    id : "subjectsTable",
    columns : [
      {
        header : getLocale().getText("settings.subjects.subjectsTableCodeHeader"),
        left : 8,
        width : 100,
        dataType : 'text',
        editable : true,
        paramName : 'code'
      }, {
        header : getLocale().getText("settings.subjects.subjectsTableNameHeader"),
        left : 8 + 100 + 8,
        width : 300,
        dataType : 'text',
        editable : true,
        paramName : 'name',
        required : true
      }, {
        header : getLocale().getText("settings.subjects.educationTypeHeader"),
        left : 8 + 100 + 8 + 300 + 8,
        right : 8 + 22 + 8 + 8,
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
      }, {
        right : 8,
        width : 30,
        dataType : 'button',
        imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
        tooltip : getLocale().getText("settings.subjects.subjectsTableRemoveTooltip"),
        onclick : function(event) {
          event.tableComponent.deleteRow(event.row);
          if (event.tableComponent.getRowCount() == 0) {
            $('noSubjectsAddedMessageContainer').setStyle({
              display : ''
            });
          }
        },
        paramName : 'removeButton'
      } 
    ]
  });

  var rows = new Array();
  for ( var i = 0, l = subjects.length; i < l; i++) {
    rows.push([ '',
                jsonEscapeHTML(subjects[i].code),
                jsonEscapeHTML(subjects[i].name),
                subjects[i].educationTypeId,
                '',
                subjects[i].id,
                0 ]);
  }

  subjectsTable.addRows(rows);

  if (subjectsTable.getRowCount() > 0) {
    $('noSubjectsAddedMessageContainer').setStyle({
      display : 'none'
    });
  } else {
    addSubjectsTableRow(); 
  }
}
