var contactTypes = JSDATA["contactTypes"].evalJSON();
var variableKeys = JSDATA["variableKeys"].evalJSON();

function addEmailTableRow() {
  getIxTableById('emailTable').addRow([ '', contactTypes[0].id, '', '', '' ]);
};

function addPhoneTableRow() {
  getIxTableById('phoneTable').addRow([ '', contactTypes[0].id, '', '', '' ]);
};

function addAddressTableRow() {
  getIxTableById('addressTable').addRow([ '', contactTypes[0].id, '', '', '', '', '', '', '' ]);
};

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
  // E-mail address

  var emailTable = new IxTable($('emailTable'), {
    id : "emailTable",
    columns : [ {
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : true,
      paramName : 'defaultAddress',
      tooltip : getLocale().getText("settings.createSchool.emailTableDefaultTooltip"),
    }, {
      header : getLocale().getText("settings.createSchool.emailTableTypeHeader"),
      width : 150,
      left : 30,
      dataType : 'select',
      editable : true,
      paramName : 'contactTypeId',
      options : (function() {
        var result = [];
        for ( var i = 0, l = contactTypes.length; i < l; i++) {
          result.push({
            text : jsonEscapeHTML(contactTypes[i].name),
            value : contactTypes[i].id
          });
        }
        return result;
      })()
    }, {
      header : getLocale().getText("settings.createSchool.emailTableAddressHeader"),
      left : 188,
      width : 200,
      dataType : 'text',
      editable : true,
      paramName : 'email',
      editorClassNames : 'email'
    }, {
      width : 30,
      left : 396,
      dataType : 'button',
      paramName : 'addButton',
      hidden : true,
      imgsrc : GLOBAL_contextPath + '/gfx/list-add.png',
      tooltip : getLocale().getText("settings.createSchool.emailTableAddTooltip"),
      onclick : function(event) {
        addEmailTableRow();
      }
    }, {
      width : 30,
      left : 396,
      dataType : 'button',
      paramName : 'removeButton',
      hidden : true,
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("settings.createSchool.emailTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
      }
    } ]
  });
  emailTable.addListener("rowAdd", function(event) {
    var emailTable = event.tableComponent;
    var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
    emailTable.showCell(event.row, emailTable.getNamedColumnIndex(enabledButton));
  });
  addEmailTableRow();
  emailTable.setCellValue(0, 0, true);

  // Addresses

  var addressTable = new IxTable($('addressTable'), {
    id : "addressTable",
    columns : [ {
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : true,
      paramName : 'defaultAddress',
      tooltip : getLocale().getText("settings.createSchool.addressTableDefaultTooltip"),
    }, {
      header : getLocale().getText("settings.createSchool.addressTableTypeHeader"),
      left : 30,
      width : 150,
      dataType : 'select',
      editable : true,
      paramName : 'contactTypeId',
      options : (function() {
        var result = [];
        for ( var i = 0, l = contactTypes.length; i < l; i++) {
          result.push({
            text : jsonEscapeHTML(contactTypes[i].name),
            value : contactTypes[i].id
          });
        }
        return result;
      })()
    }, {
      header : getLocale().getText("settings.createSchool.addressTableNameHeader"),
      left : 188,
      width : 150,
      dataType : 'text',
      editable : true,
      paramName : 'name'
    }, {
      header : getLocale().getText("settings.createSchool.addressTableStreetHeader"),
      left : 344,
      width : 150,
      dataType : 'text',
      editable : true,
      paramName : 'street'
    }, {
      header : getLocale().getText("settings.createSchool.addressTablePostalCodeHeader"),
      left : 502,
      width : 100,
      dataType : 'text',
      editable : true,
      paramName : 'postal'
    }, {
      header : getLocale().getText("settings.createSchool.addressTableCityHeader"),
      left : 610,
      width : 150,
      dataType : 'text',
      editable : true,
      paramName : 'city'
    }, {
      header : getLocale().getText("settings.createSchool.addressTableCountryHeader"),
      left : 768,
      width : 100,
      dataType : 'text',
      editable : true,
      paramName : 'country'
    }, {
      width : 30,
      left : 874,
      dataType : 'button',
      paramName : 'addButton',
      hidden : true,
      imgsrc : GLOBAL_contextPath + '/gfx/list-add.png',
      tooltip : getLocale().getText("settings.createSchool.addressTableAddTooltip"),
      onclick : function(event) {
        addAddressTableRow(event.tableComponent);
      }
    }, {
      width : 30,
      left : 874,
      dataType : 'button',
      paramName : 'removeButton',
      hidden : true,
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("settings.createSchool.addressTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
      }
    } ]
  });
  addressTable.addListener("rowAdd", function(event) {
    var addressTable = event.tableComponent;
    var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
    addressTable.showCell(event.row, addressTable.getNamedColumnIndex(enabledButton));
  });
  addAddressTableRow();
  addressTable.setCellValue(0, 0, true);

  // Phone numbers

  var phoneTable = new IxTable($('phoneTable'), {
    id : "phoneTable",
    columns : [ {
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : true,
      paramName : 'defaultNumber',
      tooltip : getLocale().getText("settings.createSchool.phoneTableDefaultTooltip"),
    }, {
      header : getLocale().getText("settings.createSchool.phoneTableTypeHeader"),
      width : 150,
      left : 30,
      dataType : 'select',
      editable : true,
      paramName : 'contactTypeId',
      options : (function() {
        var result = [];
        for ( var i = 0, l = contactTypes.length; i < l; i++) {
          result.push({
            text : jsonEscapeHTML(contactTypes[i].name),
            value : contactTypes[i].id
          });
        }
        return result;
      })()
    }, {
      header : getLocale().getText("settings.createSchool.phoneTableNumberHeader"),
      left : 188,
      width : 200,
      dataType : 'text',
      editable : true,
      paramName : 'phone'
    }, {
      width : 30,
      left : 396,
      dataType : 'button',
      paramName : 'addButton',
      hidden : true,
      imgsrc : GLOBAL_contextPath + '/gfx/list-add.png',
      tooltip : getLocale().getText("settings.createSchool.phoneTableAddTooltip"),
      onclick : function(event) {
        addPhoneTableRow(event.tableComponent);
      }
    }, {
      width : 30,
      left : 396,
      dataType : 'button',
      paramName : 'removeButton',
      hidden : true,
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("settings.createSchool.phoneTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
      }
    } ]
  });
  phoneTable.addListener("rowAdd", function(event) {
    var phoneTable = event.tableComponent;
    var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
    phoneTable.showCell(event.row, phoneTable.getNamedColumnIndex(enabledButton));
  });
  addPhoneTableRow();
  phoneTable.setCellValue(0, 0, true);

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