var addresses = JSDATA["addresses"].evalJSON();
var phoneNumbers = JSDATA["phoneNumbers"].evalJSON();
var emails = JSDATA["emails"].evalJSON();
var contactTypes = JSDATA["contactTypes"].evalJSON();
var variableKeys = JSDATA["variableKeys"].evalJSON();

function addAddressTableRow(addressTable) {
  addressTable.addRow([ -1, '', contactTypes[0].id, '', '', '', '', '', '', '' ]);
}

function addEmailTableRow() {
  getIxTableById('emailTable').addRow([ -1, '', contactTypes[0].id, '', '', '' ]);
}

function addPhoneTableRow(phoneTable) {
  phoneTable.addRow([ -1, '', contactTypes[0].id, '', '', '' ]);
}

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
  var addressTable = new IxTable($('addressTable'), {
    id : "addressTable",
    columns : [ {
      dataType : 'hidden',
      left : 0,
      width : 0,
      paramName : 'addressId'
    }, {
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : true,
      paramName : 'defaultAddress',
      tooltip : getLocale().getText("settings.editSchool.addressTableDefaultTooltip"),
    }, {
      header : getLocale().getText("settings.editSchool.addressTableTypeHeader"),
      left : 30,
      width : 150,
      dataType : 'select',
      editable : true,
      paramName : 'contactTypeId',
      options : (function() {
        var result = [];
        for ( var i = 0, l = contactTypes.length; i < l; i++) {
          result.push({
            text : contactTypes[i].name,
            value : contactTypes[i].id
          });
        }
        return result;
      })()
    }, {
      header : getLocale().getText("settings.editSchool.addressTableNameHeader"),
      left : 188,
      width : 150,
      dataType : 'text',
      editable : true,
      paramName : 'name'
    }, {
      header : getLocale().getText("settings.editSchool.addressTableStreetHeader"),
      left : 344,
      width : 150,
      dataType : 'text',
      editable : true,
      paramName : 'street'
    }, {
      header : getLocale().getText("settings.editSchool.addressTablePostalCodeHeader"),
      left : 502,
      width : 100,
      dataType : 'text',
      editable : true,
      paramName : 'postal'
    }, {
      header : getLocale().getText("settings.editSchool.addressTableCityHeader"),
      left : 610,
      width : 150,
      dataType : 'text',
      editable : true,
      paramName : 'city'
    }, {
      header : getLocale().getText("settings.editSchool.addressTableCountryHeader"),
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
      tooltip : getLocale().getText("settings.editSchool.addressTableAddTooltip"),
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
      tooltip : getLocale().getText("settings.editSchool.addressTableRemoveTooltip"),
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

  for ( var i = 0, l = addresses.length; i < l; i++) {
    addressTable.addRow([ 
        addresses[i].id, 
        addresses[i].defaultAddress, 
        addresses[i].contactTypeId, 
        jsonEscapeHTML(addresses[i].name),
        jsonEscapeHTML(addresses[i].streetAddress), 
        jsonEscapeHTML(addresses[i].postalCode), 
        jsonEscapeHTML(addresses[i].city), 
        jsonEscapeHTML(addresses[i].country), 
        '',
        '' ]);
  }

  if (addressTable.getRowCount() == 0) {
    addAddressTableRow(addressTable);
    addressTable.setCellValue(0, 1, true);
  }

  var emailTable = new IxTable($('emailTable'), {
    id : "emailTable",
    columns : [ {
      dataType : 'hidden',
      left : 0,
      width : 0,
      paramName : 'emailId'
    }, {
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : true,
      paramName : 'defaultAddress',
      tooltip : getLocale().getText("settings.editSchool.emailTableDefaultTooltip"),
    }, {
      header : getLocale().getText("settings.editSchool.emailTableTypeHeader"),
      width : 150,
      left : 30,
      dataType : 'select',
      editable : true,
      paramName : 'contactTypeId',
      options : (function() {
        var result = [];
        for ( var i = 0, l = contactTypes.length; i < l; i++) {
          result.push({
            text : contactTypes[i].name,
            value : contactTypes[i].id
          });
        }
        return result;
      })()
    }, {
      header : getLocale().getText("settings.editSchool.emailTableAddressHeader"),
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
      tooltip : getLocale().getText("settings.editSchool.emailTableAddTooltip"),
      onclick : function(event) {
        addEmailTableRow(event.tableComponent);
      }
    }, {
      width : 30,
      left : 396,
      dataType : 'button',
      paramName : 'removeButton',
      hidden : true,
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("settings.editSchool.emailTableRemoveTooltip"),
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

  for ( var i = 0, l = emails.length; i < l; i++) {
    emailTable.addRow([ emails[i].id, emails[i].defaultAddress, emails[i].contactTypeId, jsonEscapeHTML(emails[i].address), '', '' ]);
  }

  if (emailTable.getRowCount() == 0) {
    addEmailTableRow();
    emailTable.setCellValue(0, 1, true);
  }

  var phoneTable = new IxTable($('phoneTable'), {
    id : "phoneTable",
    columns : [ {
      dataType : 'hidden',
      left : 0,
      width : 0,
      paramName : 'phoneId'
    }, {
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : true,
      paramName : 'defaultNumber',
      tooltip : getLocale().getText("settings.editSchool.phoneTableDefaultTooltip"),
    }, {
      header : getLocale().getText("settings.editSchool.phoneTableTypeHeader"),
      width : 150,
      left : 30,
      dataType : 'select',
      editable : true,
      paramName : 'contactTypeId',
      options : (function() {
        var result = [];
        for ( var i = 0, l = contactTypes.length; i < l; i++) {
          result.push({
            text : contactTypes[i].name,
            value : contactTypes[i].id
          });
        }
        return result;
      })()
    }, {
      header : getLocale().getText("settings.editSchool.phoneTableNumberHeader"),
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
      tooltip : getLocale().getText("settings.editSchool.phoneTableAddTooltip"),
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
      tooltip : getLocale().getText("settings.editSchool.phoneTableRemoveTooltip"),
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

  for ( var i = 0, l = phoneNumbers.length; i < l; i++) {
    phoneTable.addRow([ phoneNumbers[i].id, phoneNumbers[i].defaultNumber, phoneNumbers[i].contactTypeId, jsonEscapeHTML(phoneNumbers[i].number), '', '' ]);
  }

  if (phoneTable.getRowCount() == 0) {
    addPhoneTableRow(phoneTable);
    phoneTable.setCellValue(0, 1, true);
  }

  var variablesTable = new IxTable($('variablesTable'), {
    id : "variablesTable",
    columns : [ {
      left : 8,
      width : 30,
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