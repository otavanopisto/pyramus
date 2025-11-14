var contactTypes = JSDATA["contactTypes"].evalJSON();
var variableKeys = JSDATA["variableKeys"].evalJSON();

function setupTags() {
  JSONRequest.request("tags/getalltags.json", {
    onSuccess : function(jsonResponse) {
      new Autocompleter.Local("tags", "tags_choices", jsonResponse.tags, {
        tokens : [ ',', '\n', ' ' ]
      });
    }
  });
}

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  setupTags();
  
  initializeContactInfoEditor($('contactInfos'), contactTypes);
  
  // Variables

  var variablesTable = new IxTable($('variablesTable'), {
    id : "variablesTable",
    columns : [ {
      left : 8,
      width : 30,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
      tooltip : getLocale().getText("settings.createSchool.variablesTableEditTooltip"),
      onclick : function(event) {
        var table = event.tableComponent;
        var valueColumn = table.getNamedColumnIndex('value');
        table.setCellEditable(event.row, valueColumn, table.isCellEditable(event.row, valueColumn) == false);
      }
    }, {
      dataType : 'hidden',
      editable : false,
      paramName : 'key'
    }, {
      left : 38,
      width : 150,
      dataType : 'text',
      editable : false,
      paramName : 'name'
    }, {
      left : 188,
      width : 750,
      dataType : 'text',
      editable : false,
      paramName : 'value'
    } ]
  });

  variablesTable.detachFromDom();
  for ( var i = 0, l = variableKeys.length; i < l; i++) {
    var rowNumber = variablesTable.addRow([ '', jsonEscapeHTML(variableKeys[i].variableKey), jsonEscapeHTML(variableKeys[i].variableName), '' ]);
    var dataType;
    switch (variableKeys[i].variableType) {
      case 'NUMBER':
        dataType = 'number';
      break;
      case 'DATE':
        dataType = 'date';
      break;
      case 'BOOLEAN':
        dataType = 'checkbox';
      break;
      default:
        dataType = 'text';
      break;
    }
    variablesTable.setCellDataType(rowNumber, 3, dataType);
  }
  variablesTable.reattachToDom();
};