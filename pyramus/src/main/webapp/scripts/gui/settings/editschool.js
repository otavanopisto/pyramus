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
  
  var contactInfos = JSDATA["contactInfos"].evalJSON();
  initializeContactInfoEditor($('contactInfos'), 'contactInfos', contactTypes, contactInfos);
  
  var variablesTable = new IxTable($('variablesTable'), {
    id : "variablesTable",
    columns : [ {
      left : 8,
      width : 22,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
      tooltip : getLocale().getText("settings.editSchool.variablesTableEditTooltip"),
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
      left : 8 + 22 + 8,
      width : 200,
      dataType : 'text',
      editable : false,
      paramName : 'name'
    }, {
      left : 8 + 22 + 8 + 200 + 8,
      width : 750,
      dataType : 'text',
      editable : false,
      paramName : 'value'
    } ]
  });
  variablesTable.detachFromDom();
  for ( var i = 0, l = variableKeys.length; i < l; i++) {
    var variableValue = variableKeys[i].variableValue != null ? jsonEscapeHTML(variableKeys[i].variableValue) : undefined;
    var rowNumber = variablesTable.addRow([ '', jsonEscapeHTML(variableKeys[i].variableKey), jsonEscapeHTML(variableKeys[i].variableName), variableValue]);
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