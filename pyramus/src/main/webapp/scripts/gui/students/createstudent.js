var contactTypes = JSDATA["contactTypes"].evalJSON();
var variableKeys = JSDATA["variableKeys"].evalJSON();

var emails_values = JSDATA["createstudent_emails"] ? JSDATA["createstudent_emails"].evalJSON() : undefined;
var address_values = JSDATA["createstudent_addresses"] ? JSDATA["createstudent_addresses"].evalJSON() : undefined;
var phone_values = JSDATA["createstudent_phones"] ? JSDATA["createstudent_phones"].evalJSON() : undefined;

function addEmailTableRow(values) {
  getIxTableById('emailTable').addRow(values || [ '', contactTypes[0].id, '', '', '' ]);
};

function addPhoneTableRow(values) {
  getIxTableById('phoneTable').addRow(values || [ '', contactTypes[0].id, '', '', '' ]);
};

function addAddressTableRow(values) {
  getIxTableById('addressTable').addRow(values || [ '', contactTypes[0].id, '', '', '', '', '', '', '' ]);
};

function addLodgingPeriodTableRow(lodgingPeriodTable) {
  lodgingPeriodTable.addRow([-1, '', '', '', '']);
}

function setupTags() {
  JSONRequest.request("tags/getalltags.json", {
  onSuccess: function (jsonResponse) {
    new Autocompleter.Local("tags", "tags_choices", jsonResponse.tags, {
        tokens: [',', '\n', ' ']
    });
  }
  });   
}

function initStudentLodgingPeriodsTable() {
  var lodgingPeriodsTable = new IxTable($('lodgingPeriodsTableContainer'), {
    id : "lodgingPeriodsTable",
    columns : [{
      dataType : 'hidden',
      left : 0,
      width : 0,
      paramName : 'id'
    }, {
      header: getLocale().getText("students.createStudent.lodgingPeriodsTable.begin"),
      left : 8,
      width: 160,
      dataType : 'date',
      editable: true,
      paramName: 'begin'
    }, {
      header: getLocale().getText("students.createStudent.lodgingPeriodsTable.end"),
      left : 168,
      width : 160,
      dataType: 'date',
      editable: true,
      paramName: 'end'
    },{
      width: 30,
      left: 8 + 160 + 8 + 160 + 8,
      dataType: 'button',
      paramName: 'addButton',
      hidden: true,
      imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
      tooltip: getLocale().getText("students.createStudent.lodgingPeriodsTable.addTooltip"),
      onclick: function (event) {
        addLodgingPeriodTableRow(event.tableComponent);
      }
    }, {
      width: 30,
      left: 8 + 160 + 8 + 160 + 8,
      dataType: 'button',
      paramName: 'removeButton',
      hidden: true,
      imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip: getLocale().getText("students.createStudent.lodgingPeriodsTable.removeTooltip"),
      onclick: function (event) {
        event.tableComponent.deleteRow(event.row);
        
        if (event.tableComponent.getRowCount() == 0) {
          $('noLodgingPeriodsAddedMessageContainer').setStyle({
            display : ''
          });
        }
      }
    }]
  });
  
  lodgingPeriodsTable.addListener("rowAdd", function (event) {
    var table = event.tableComponent; 
    var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
    lodgingPeriodsTable.showCell(event.row, table.getNamedColumnIndex(enabledButton));
  
    if (table.getRowCount() > 0) {
      $('noLodgingPeriodsAddedMessageContainer').setStyle({
        display : 'none'
      });
    }
  });
  
  return lodgingPeriodsTable;
}
    

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));

  initStudentLodgingPeriodsTable();
  
  // E-mail address

  var emailTable = new IxTable($('emailTable'), {
    id : "emailTable",
    columns : [ {
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : true,
      paramName : 'defaultAddress',
      tooltip : getLocale().getText("students.createStudent.emailTableDefaultTooltip"),
    }, {
      header : getLocale().getText("students.createStudent.emailTableTypeHeader"),
      width : 150,
      left : 30,
      dataType : 'select',
      editable : true,
      paramName : 'contactTypeId',
      options : (function() {
        var results = [];
        for ( var i = 0, l = contactTypes.length; i < l; i++) {
          results.push({
            text : contactTypes[i].name,
            value : contactTypes[i].id
          });
        }
        return results;
      })()
    }, {
      header : getLocale().getText("students.createStudent.emailTableAddressHeader"),
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
      tooltip : getLocale().getText("students.createStudent.emailTableAddTooltip"),
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
      tooltip : getLocale().getText("students.createStudent.emailTableRemoveTooltip"),
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
  
  if (emails_values && emails_values.length > 0) {
    for (var i = 0; i < emails_values.length; i++) {
      addEmailTableRow([emails_values[i].defaultAddress, 
                        emails_values[i].contactType != undefined ? emails_values[i].contactType.id : '', 
                        emails_values[i].address, '', '']);
    }
  } else {
    addEmailTableRow();
    emailTable.setCellValue(0, 0, true);
  }

  // Addresses

  var addressTable = new IxTable($('addressTable'), {
    id : "addressTable",
    columns : [ {
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : true,
      paramName : 'defaultAddress',
      tooltip : getLocale().getText("students.createStudent.addressTableDefaultTooltip"),
    }, {
      header : getLocale().getText("students.createStudent.addressTableTypeHeader"),
      left : 30,
      width : 150,
      dataType : 'select',
      editable : true,
      paramName : 'contactTypeId',
      options : (function() {
        var results = [];
        for ( var i = 0, l = contactTypes.length; i < l; i++) {
          results.push({
            text : contactTypes[i].name,
            value : contactTypes[i].id
          });
        }
        return results;
      })()
    }, {
      header : getLocale().getText("students.createStudent.addressTableNameHeader"),
      left : 188,
      width : 150,
      dataType : 'text',
      editable : true,
      paramName : 'name'
    }, {
      header : getLocale().getText("students.createStudent.addressTableStreetHeader"),
      left : 344,
      width : 150,
      dataType : 'text',
      editable : true,
      paramName : 'street'
    }, {
      header : getLocale().getText("students.createStudent.addressTablePostalCodeHeader"),
      left : 502,
      width : 100,
      dataType : 'text',
      editable : true,
      paramName : 'postal'
    }, {
      header : getLocale().getText("students.createStudent.addressTableCityHeader"),
      left : 610,
      width : 150,
      dataType : 'text',
      editable : true,
      paramName : 'city'
    }, {
      header : getLocale().getText("students.createStudent.addressTableCountryHeader"),
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
      tooltip : getLocale().getText("students.createStudent.addressTableAddTooltip"),
      onclick : function(event) {
        addAddressTableRow();
      }
    }, {
      width : 30,
      left : 874,
      dataType : 'button',
      paramName : 'removeButton',
      hidden : true,
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("students.createStudent.addressTableRemoveTooltip"),
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

  if (address_values && address_values.length > 0) {
    for (var i = 0; i < address_values.length; i++) {
      addAddressTableRow([address_values[i].defaultAddress, 
                          address_values[i].contactType != undefined ? address_values[i].contactType.id : '', 
                          address_values[i].name != undefined ? address_values[i].name : '', 
                          address_values[i].streetAddress, 
                          address_values[i].postalCode, 
                          address_values[i].city, 
                          address_values[i].country, '', '']);
    }
  } else {
    addAddressTableRow();
    addressTable.setCellValue(0, 0, true);
  }
  
  // Phone numbers

  var phoneTable = new IxTable($('phoneTable'), {
    id : "phoneTable",
    columns : [ {
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : true,
      paramName : 'defaultNumber',
      tooltip : getLocale().getText("students.createStudent.phoneTableDefaultTooltip"),
    }, {
      header : getLocale().getText("students.createStudent.phoneTableTypeHeader"),
      width : 150,
      left : 30,
      dataType : 'select',
      editable : true,
      paramName : 'contactTypeId',
      options : (function() {
        var results = [];
        for ( var i = 0, l = contactTypes.length; i < l; i++) {
          results.push({
            text : contactTypes[i].name,
            value : contactTypes[i].id
          });
        }
        return results;
      })()
    }, {
      header : getLocale().getText("students.createStudent.phoneTableNumberHeader"),
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
      tooltip : getLocale().getText("students.createStudent.phoneTableAddTooltip"),
      onclick : function(event) {
        addPhoneTableRow();
      }
    }, {
      width : 30,
      left : 396,
      dataType : 'button',
      paramName : 'removeButton',
      hidden : true,
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("students.createStudent.phoneTableRemoveTooltip"),
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

  if (phone_values && phone_values.length > 0) {
    for (var i = 0; i < phone_values.length; i++) {
      addPhoneTableRow([phone_values[i].defaultNumber, 
                        phone_values[i].contactType != undefined ? phone_values[i].contactType.id : '', 
                        phone_values[i].number, 
                        '', '']);
    }
  } else {
    addPhoneTableRow();
    phoneTable.setCellValue(0, 0, true);
  }
  
  // Student variables

  // Variables
  if (variableKeys.length > 0) {
    var variablesTable = new IxTable($('variablesTableContainer'), {
      id : "variablesTable",
      columns : [ {
        left : 8,
        width : 30,
        dataType : 'button',
        imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
        tooltip : getLocale().getText("students.createStudent.variablesTableEditTooltip"),
        onclick : function(event) {
          var table = event.tableComponent;
          var valueColumn = table.getNamedColumnIndex('value');
          var editable = table.isCellEditable(event.row, valueColumn) == false;
          table.setCellEditable(event.row, valueColumn, table.isCellEditable(event.row, valueColumn) == false);
          table.setCellValue(event.row, table.getNamedColumnIndex('edited'), editable ? "1" : "0");
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
      }, {
        dataType: 'hidden',
        editable: false,
        paramName: 'edited'
      } ]
    });

    var valueColumnNumber = variablesTable.getNamedColumnIndex('value');
    variablesTable.detachFromDom();
    for ( var i = 0, l = variableKeys.length; i < l; i++) {
      var rowNumber = variablesTable.addRow([ '', jsonEscapeHTML(variableKeys[i].variableKey),
                                              jsonEscapeHTML(variableKeys[i].variableName), '', 0 ]);
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
      variablesTable.setCellDataType(rowNumber, valueColumnNumber, dataType);
    }
    variablesTable.reattachToDom();
  }
  
  setupTags();
};