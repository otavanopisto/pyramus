var addresses = JSDATA["addresses"].evalJSON();
var phoneNumbers = JSDATA["phoneNumbers"].evalJSON();
var emails = JSDATA["emails"].evalJSON();
var variableKeys = JSDATA["variableKeys"].evalJSON();

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  var variablesTable = new IxTable($('variablesTable'), {
    id : "variablesTable",
    columns : [ {
      dataType : 'hidden',
      editable : false,
      paramName : 'key'
    }, {
      left : 0,
      width : 150,
      dataType : 'text',
      editable : false,
      paramName : 'name'
    }, {
      left : 150,
      width : 750,
      dataType : 'text',
      editable : false,
      paramName : 'value'
    } ]
  });
  var addressTable = new IxTable($('addressTable'), {
    id : "addressTable",
    columns : [ {
      left : 0,
      width : 150,
      dataType : 'text',
      editable : false,
      paramName : 'contactTypeName'
    }, {
      left : 150,
      width : 150,
      dataType : 'text',
      editable : false,
      paramName : 'name'
    }, {
      left : 300,
      width : 150,
      dataType : 'text',
      editable : false,
      paramName : 'street'
    }, {
      left : 450,
      width : 150,
      dataType : 'text',
      editable : false,
      paramName : 'postal'
    }, {
      left : 600,
      width : 150,
      dataType : 'text',
      editable : false,
      paramName : 'city'
    }, {
      left : 750,
      width : 150,
      dataType : 'text',
      editable : false,
      paramName : 'country'
    } ]
  });
  var emailTable = new IxTable($('emailTable'), {
    id : "emailTable",
    columns : [{
        left : 0,
        width : 30,
        dataType : 'radiobutton',
        editable : false,
        paramName : 'defaultAddress'
      }, {
        left : 30,
        width : 150,
        dataType : 'text',
        editable : false,
        paramName : 'contactTypeName'
      }, {
        left : 150,
        width : 150,
        dataType : 'text',
        editable : false,
        paramName : 'email'
    }]
  });
  var phoneTable = new IxTable($('phoneNumbersTable'), {
    id : "phoneNumbersTable",
    columns : [{
      left : 0,
      width : 30,
      dataType : 'radiobutton',
      editable : false,
      paramName : 'defaultNumber'
    }, {
      width : 150,
      left : 30,
      dataType : 'select',
      editable : false,
      paramName : 'contactTypeName'
    }, {
      left : 188,
      width : 200,
      dataType : 'text',
      editable : false,
      paramName : 'phone'
    } ]
  });
  
  var emailRows = new Array();
  for (var i = 0, l = emails.length; i < l; i++) {
		emailRows.push([ emails[i].defaultAddress, emails[i].contactTypeName,
				emails[i].address ]);
  }
  emailTable.addRows(emailRows);
  
  var phoneRows = new Array();
  for (var i = 0, l = phoneNumbers.length; i < l; i++) {
	phoneRows.push([
	               phoneNumbers[i].defaultNumber, 
	               phoneNumbers[i].contactTypeName,
	               phoneNumbers[i].number
	               ]);
  }
  phoneTable.addRows(phoneRows);
  
  addressTable.detachFromDom();
  for (var i=0, l=addresses.length; i<l; i++) {
    var address = addresses[i];
    addressTable.addRow([
      address.contactTypeName,
      address.name,
      address.streetAddress,
      address.postalCode,
      address.city,
      address.country
    ]);
  }
  addressTable.reattachToDom();
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