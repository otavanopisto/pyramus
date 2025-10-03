/**
 * 
 */

var _typedContactInfoEditors = new Hash();

function getContactInfoEditorById(id) {
  return _typedContactInfoEditors.get(id);
};

function getContactInfoEditors() {
  return _typedContactInfoEditors.values();
};

function initializeContactInfoView(container, contactInfos) {
  if (contactInfos) {
    contactInfos.forEach((contactInfo) => {
      var contactInfoContainer = new Element("div", { className: "genericViewInfoWapper" });
      container.appendChild(contactInfoContainer);
      var sectionContainer = new Element("div", { className: "genericFormSection" });
      contactInfoContainer.appendChild(sectionContainer);
      
      var titleContainer = new Element("div", { className: "genericFormTitle" });
      sectionContainer.appendChild(titleContainer);
      
      titleContainer.appendChild(new Element("div", { className: "genericFormTitleText" }).update(contactInfo.typeName));
      
      var dataContainer = new Element("div", { className: "genericViewFormDataText" });
      sectionContainer.appendChild(dataContainer);
      
      if (contactInfo.allowStudyDiscussions) {
        dataContainer.appendChild(new Element("div", { className: "contactInfoAllowStudyDiscussions" }).update(getLocale().getText("typedcontactinfo.allowedStudyDiscussions")));
      }

      if (contactInfo.addresses) {
        contactInfo.addresses.forEach((address) => {
          dataContainer.appendChild(new Element("div").update(address.name));
          dataContainer.appendChild(new Element("div").update(address.streetAddress));
          dataContainer.appendChild(new Element("div").update(address.city));
          dataContainer.appendChild(new Element("div").update(address.postalCode));
          dataContainer.appendChild(new Element("div").update(address.country));
        });
      }
            
      if (contactInfo.emails) {
        contactInfo.emails.forEach((email) => {
          var emailDiv = new Element("div");
          emailDiv.appendChild(new Element("a", { href: "mailto:" + email.address }).update(email.address));
          dataContainer.appendChild(emailDiv);
        });
      }
      
      if (contactInfo.phoneNumbers) {
        contactInfo.phoneNumbers.forEach((phoneNumber) => {
          dataContainer.appendChild(new Element("div").update(phoneNumber.number));
        });
      }
    });
  }
}


function initializeContactInfoEditor(container, contactTypes, contactInfos) {
  var editor = new IxTypedContactInfoEditor(container, contactTypes, contactInfos);
  if (container.id) {
    _typedContactInfoEditors.set(container.id, editor);
  }
  return editor;
}

IxTypedContactInfoEditor = Class.create({
  initialize : function (container, contactTypes, contactInfos) {
    this._container = container;
    this._contactTypes = contactTypes;
    this._containerId = container.id;
    this._editorId = container.id;

    container.classList.add("typedContactInfosEditor");

    this._addRowClickListener = this._onAddRowClick.bindAsEventListener(this);

    // The container id is used as a prefix for all the variables    
    var variableNamePrefix = container.id;
  
    var contactInfoListElem = new Element("div", { className: "typedContactInfosEditorList" });
    var contactInfoCountElem = new Element("input", { type: "hidden", name: variableNamePrefix + ".rowCount", value: "0" });
    var addNewRowElem = new Element("span", { className: "genericTableAddRowLinkContainer" }).update(getLocale().getText("typedcontactinfo.addNewContact"));
  
    addNewRowElem.onclick = this._addRowClickListener;
    
    if (container.id) {
      contactInfoListElem.setAttribute("data-typedContactInfosEditor-id", container.id);
    }
  
    container.appendChild(addNewRowElem);
    container.appendChild(contactInfoListElem);
    container.appendChild(contactInfoCountElem);
  
    this._contactInfoCountElem = contactInfoCountElem;
    this._contactInfoListElem = contactInfoListElem;
    
    if (contactInfos) {
      contactInfos.forEach((contactInfo) => {
        this.addRow(contactInfo);
      });

      this._updateRowCountHidden();
    }
  },
  addRow: function (data) {
    var container = this._contactInfoListElem;
    // TODO generoi tämä paremmin sitten kun rivejä voi poistaa, muuten ei toimi
    var contactInfoIndex = container.childElementCount;
    var contactInfoID = this._containerId + "." + contactInfoIndex;
    
    var rowElem = new Element("div", { id: contactInfoID, className: "genericViewInfoWapper" });
    container.appendChild(rowElem);

    rowElem.appendChild(this._initializeContactInfoTitle(getLocale().getText("typedcontactinfo.contactType")));
    
    var contactInfoIdElem = new Element("input", { type: "hidden", name: contactInfoID + ".id", value: (data ? data.id : "-1") });
    rowElem.appendChild(contactInfoIdElem);
    
    var typeSelector = new Element("select", { name: contactInfoID + ".contactTypeId" });
    for ( var i = 0, l = this._contactTypes.length; i < l; i++) {
      var contactType = this._contactTypes[i];
      typeSelector.appendChild(new Element("option", { value: contactType.id }).update(jsonEscapeHTML(contactType.name)));
    }
    if (data && data.typeId) {
      typeSelector.value = data.typeId;
    }
    
    rowElem.appendChild(typeSelector);
    
    var addresses = new Element("div", { id: contactInfoID + ".addresses" });
    var emails = new Element("div", { id: contactInfoID + ".emails" });
    var phonenumbers = new Element("div", { id: contactInfoID + ".phonenumbers" });

    rowElem.appendChild(this._initializeContactInfoTitle(getLocale().getText("contactinfo.addresses")));
    rowElem.appendChild(addresses);
    rowElem.appendChild(this._initializeContactInfoTitle(getLocale().getText("contactinfo.emails")));
    rowElem.appendChild(emails);
    rowElem.appendChild(this._initializeContactInfoTitle(getLocale().getText("contactinfo.phoneNumbers")));
    rowElem.appendChild(phonenumbers);
    
    
    var emailTable = new IxTable($(contactInfoID + ".emails"), {
      id : contactInfoID + ".emailTable",
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
        tooltip : getLocale().getText("settings.createSchool.emailTableDefaultTooltip"),
      }, {
        header : getLocale().getText("settings.createSchool.emailTableAddressHeader"),
        left : 30,
        width : 200,
        dataType : 'text',
        editable : true,
        paramName : 'email',
        editorClassNames : 'email'
      }, {
        width : 30,
        left : 30 + 200 + 8,
        dataType : 'button',
        paramName : 'addButton',
        hidden : true,
        imgsrc : GLOBAL_contextPath + '/gfx/list-add.png',
        tooltip : getLocale().getText("settings.createSchool.emailTableAddTooltip"),
        onclick : function(event) {
          event.tableComponent.addRow([ '-1', '', '', '', '' ]);
        }
      }, {
        width : 30,
        left : 30 + 200 + 8,
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
    
    if (data && data.emails && data.emails.length) {
      data.emails.forEach((email) => {
        var rowind = emailTable.addRow([ 
          email.id, 
          '', 
          email.address || '', 
          '', 
          '' 
        ]);
        emailTable.setCellValue(rowind, emailTable.getNamedColumnIndex("defaultAddress"), email.defaultAddress);
      });
    }
    else {
      emailTable.addRow([ '-1', '', '', '', '' ]);
      emailTable.setCellValue(0, emailTable.getNamedColumnIndex("defaultAddress"), true);
    }

    // Addresses

    var addressTable = new IxTable($(contactInfoID + ".addresses"), {
      id : contactInfoID + ".addressTable",
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
        tooltip : getLocale().getText("settings.createSchool.addressTableDefaultTooltip"),
      }, {
        header : getLocale().getText("settings.createSchool.addressTableNameHeader"),
        left : 30,
        width : 150,
        dataType : 'text',
        editable : true,
        paramName : 'name'
      }, {
        header : getLocale().getText("settings.createSchool.addressTableStreetHeader"),
        left : 30 + 150 + 8,
        width : 150,
        dataType : 'text',
        editable : true,
        paramName : 'street'
      }, {
        header : getLocale().getText("settings.createSchool.addressTablePostalCodeHeader"),
        left : 30 + 150 + 8 + 150 + 8,
        width : 100,
        dataType : 'text',
        editable : true,
        paramName : 'postal'
      }, {
        header : getLocale().getText("settings.createSchool.addressTableCityHeader"),
        left : 30 + 150 + 8 + 150 + 8 + 100 + 8,
        width : 150,
        dataType : 'text',
        editable : true,
        paramName : 'city'
      }, {
        header : getLocale().getText("settings.createSchool.addressTableCountryHeader"),
        left : 30 + 150 + 8 + 150 + 8 + 100 + 8 + 150 + 8,
        width : 100,
        dataType : 'text',
        editable : true,
        paramName : 'country'
      }, {
        width : 30,
        left : 30 + 150 + 8 + 150 + 8 + 100 + 8 + 150 + 8 + 100 + 8,
        dataType : 'button',
        paramName : 'addButton',
        hidden : true,
        imgsrc : GLOBAL_contextPath + '/gfx/list-add.png',
        tooltip : getLocale().getText("settings.createSchool.addressTableAddTooltip"),
        onclick : function(event) {
          event.tableComponent.addRow([ '-1', '', '', '', '', '', '', '', '' ]);
        }
      }, {
        width : 30,
        left : 30 + 150 + 8 + 150 + 8 + 100 + 8 + 150 + 8 + 100 + 8,
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

    if (data && data.addresses && data.addresses.length) {
      data.addresses.forEach((address) => {
        var rowind = addressTable.addRow([ 
          address.id, 
          '', 
          address.name || '', 
          address.streetAddress || '', 
          address.postalCode || '', 
          address.city || '', 
          address.country || '', 
          '', 
          '' 
        ]);
        addressTable.setCellValue(rowind, addressTable.getNamedColumnIndex("defaultAddress"), address.defaultAddress);
      });
    }
    else {
      addressTable.addRow([ '-1', '', '', '', '', '', '', '', '' ]);
      addressTable.setCellValue(0, addressTable.getNamedColumnIndex("defaultAddress"), true);
    }

    // Phone numbers

    var phoneTable = new IxTable($(contactInfoID + ".phonenumbers"), {
      id : contactInfoID + ".phoneTable",
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
        tooltip : getLocale().getText("settings.createSchool.phoneTableDefaultTooltip"),
      }, {
        header : getLocale().getText("settings.createSchool.phoneTableNumberHeader"),
        left : 30,
        width : 200,
        dataType : 'text',
        editable : true,
        paramName : 'phone'
      }, {
        width : 30,
        left : 30 + 200 + 8,
        dataType : 'button',
        paramName : 'addButton',
        hidden : true,
        imgsrc : GLOBAL_contextPath + '/gfx/list-add.png',
        tooltip : getLocale().getText("settings.createSchool.phoneTableAddTooltip"),
        onclick : function(event) {
          event.tableComponent.addRow([ '-1', '', '', '', '' ]);
        }
      }, {
        width : 30,
        left : 30 + 200 + 8,
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
    
    if (data && data.phoneNumbers && data.phoneNumbers.length) {
      data.phoneNumbers.forEach((phoneNumber) => {
        var rowind = phoneTable.addRow([ 
          phoneNumber.id, 
          '', 
          phoneNumber.number || '', 
          '', 
          '' 
        ]);
        phoneTable.setCellValue(rowind, phoneTable.getNamedColumnIndex("defaultNumber"), phoneNumber.defaultNumber);
      });
    }
    else {
      phoneTable.addRow([ '-1', '', '', '', '' ]);
      phoneTable.setCellValue(0, phoneTable.getNamedColumnIndex("defaultNumber"), true);
    }

    this._updateRowCountHidden();
  },
  
  getData: function () {
    var data = [];
    
    var children = this._contactInfoListElem.children;
    for (var childind = 0; childind < children.length; childind++) {
      var contactInfoRowElem = children[childind];
      var contactInfoUIId = contactInfoRowElem.id;
      
      var contactInfoId = document.querySelector(`input[name="${contactInfoUIId}.id"]`).value;
      var contactTypeId = document.querySelector(`select[name="${contactInfoUIId}.contactTypeId"]`).value;
      
      var addressTable = getIxTableById(`${contactInfoUIId}.addressTable`);
      var emailTable = getIxTableById(`${contactInfoUIId}.emailTable`);
      var phoneTable = getIxTableById(`${contactInfoUIId}.phoneTable`);
      
      var addresses = [];
      var emails = [];
      var phoneNumbers = [];

      for (var row = 0; row < addressTable.getRowCount(); row++) {
        addresses.push({
          "id": addressTable.getCellValue(row, addressTable.getNamedColumnIndex('addressId')),
          "defaultAddress": addressTable.getCellValue(row, addressTable.getNamedColumnIndex('defaultAddress')),
          "name": addressTable.getCellValue(row, addressTable.getNamedColumnIndex('name')),
          "streetAddress": addressTable.getCellValue(row, addressTable.getNamedColumnIndex('street')),
          "postalCode": addressTable.getCellValue(row, addressTable.getNamedColumnIndex('postal')),
          "city": addressTable.getCellValue(row, addressTable.getNamedColumnIndex('city')),
          "country": addressTable.getCellValue(row, addressTable.getNamedColumnIndex('country')),
        });
      }
      
      for (var row = 0; row < emailTable.getRowCount(); row++) {
        emails.push({
          "id": emailTable.getCellValue(row, emailTable.getNamedColumnIndex('emailId')),
          "address": emailTable.getCellValue(row, emailTable.getNamedColumnIndex('email')),
          "defaultAddress": emailTable.getCellValue(row, emailTable.getNamedColumnIndex('defaultAddress'))
        });
      }

      for (var row = 0; row < phoneTable.getRowCount(); row++) {
        phoneNumbers.push({
          "id": phoneTable.getCellValue(row, phoneTable.getNamedColumnIndex('phoneId')),
          "number": phoneTable.getCellValue(row, phoneTable.getNamedColumnIndex('phone')),
          "defaultNumber": phoneTable.getCellValue(row, phoneTable.getNamedColumnIndex('defaultNumber'))
        });
      }

      data.push({
        "id": contactInfoId,
        "typeId": contactTypeId,
        "addresses": addresses,
        "emails": emails,
        "phoneNumbers": phoneNumbers
      });
    }
    
    return data;
  },
  
  setData: function (data) {
    this.clearRows();
    
    for (var row = 0; row < data.length; row++) {
      this.addRow(data[row]);
    }
  },
  
  clearRows: function () {
    var container = this._contactInfoListElem;
    while (container.firstChild) {
      container.removeChild(container.lastChild);
    }
  },
  
  _initializeContactInfoTitle: function (title) {
    var genericFormTitle = new Element("div", { className: "genericFormTitle" });
    var genericFormTitleText = new Element("div", { className: "genericFormTitleText" }).update(jsonEscapeHTML(title));
    genericFormTitle.appendChild(genericFormTitleText);
    return genericFormTitle;
  },

  _onAddRowClick: function (event) {
    this.addRow();
  },
  _updateRowCountHidden: function () {
    this._contactInfoCountElem.value = this._contactInfoListElem.childElementCount;
  }

});