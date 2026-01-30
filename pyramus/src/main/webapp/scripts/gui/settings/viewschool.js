var variableKeys = JSDATA["variableKeys"].evalJSON();

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  
  var contactInfos = JSDATA["contactInfos"].evalJSON();
  initializeContactInfoView($('contactInfos'), contactInfos);
  
  var variablesTable = new IxTable($('variablesTable'), {
    id : "variablesTable",
    columns : [ {
      dataType : 'hidden',
      editable : false,
      paramName : 'key'
    }, {
      left : 8,
      width : 200,
      dataType : 'text',
      editable : false,
      paramName : 'name'
    }, {
      left : 8 + 200 + 8,
      width : 750,
      dataType : 'text',
      editable : false,
      paramName : 'value'
    } ]
  });

  variablesTable.detachFromDom();
  for ( var i = 0, l = variableKeys.length; i < l; i++) {
    var rowNumber = variablesTable.addRow([ jsonEscapeHTML(variableKeys[i].variableKey), jsonEscapeHTML(variableKeys[i].variableName),
                                            jsonEscapeHTML(variableKeys[i].variableValue) ]);
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
    variablesTable.setCellDataType(rowNumber, 2, dataType);
  }
  variablesTable.reattachToDom();
};