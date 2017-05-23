IxDateField = Class.create({
  initialize : function(options) {
    var element = options.element;
  
    this._paramName = element.name;
    
    var idAttr = element.getAttribute("ix:datefieldid");
    if (idAttr)
      this._id = idAttr;
    else
      this._id = null;
    
    this._dayInput = new Element("input", {id: this._paramName + "_-dd", name: this._paramName + "_-dd", type: 'text', maxlength: 2, className: 'ixDateFieldDay'});
    this._monthInput = new Element("input", {id: this._paramName + "_-mm", name: this._paramName + "_-mm", type: 'text', maxlength: 2, className: 'ixDateFieldMonth'});
    this._yearInput = new Element("input", {id: this._paramName + '_', name: this._paramName + '_', type: 'text', maxlength: 4, className: 'ixDateFieldYear split-date'});
    this._timestampInput = new Element("input", {id: this._paramName, name: this._paramName, type: 'hidden'});
   
    this._dayFieldValueChangeListener = this._onDayFieldValueChange.bindAsEventListener(this);
    this._monthFieldValueChangeListener = this._onMonthFieldValueChange.bindAsEventListener(this);
    this._yearFieldValueChangeListener = this._onYearFieldValueChange.bindAsEventListener(this);
    this._timestampFieldValueChangeListener = this._onTimestampFieldValueChange.bindAsEventListener(this);
    
    Event.observe(this._dayInput, "change", this._dayFieldValueChangeListener);
    Event.observe(this._monthInput, "change", this._monthFieldValueChangeListener);
    Event.observe(this._yearInput, "change", this._yearFieldValueChangeListener);

    // Fixes Chrome bugs
    if (Prototype.Browser.WebKit) {
      Event.observe(this._dayInput, "mouseup", function (e) { Event.stop(e); }.bindAsEventListener(this));
      Event.observe(this._monthInput, "mouseup", function (e) { Event.stop(e); }.bindAsEventListener(this));
      Event.observe(this._yearInput, "mouseup", function (e) { Event.stop(e); }.bindAsEventListener(this));
    }
    
    this._updatingTimestamp = false;
    
    this._domNode = new Element("div", {className: 'ixDateField'});
    this._domNode.appendChild(this._timestampInput);
    this._domNode.appendChild(this._dayInput);
    this._domNode.appendChild(this._monthInput);
    this._domNode.appendChild(this._yearInput);
    
    this.replaceNode(element);
  },
  getParamName : function() {
    return this._paramName;
  },
  getId : function() {
    return this._id;
  },
  setDay : function(day) {
    this._dayInput.value = day;
  },
  setMonth : function(month) {
    this._monthInput.value = month;
  },
  setYear : function(year) {
    this._yearInput.value = year;
  },
  setTimestamp : function(timestamp) {
    var date = new Date();
    date.setTime(timestamp);
    this._yearInput.value = date.getFullYear();
    this._monthInput.value = (date.getMonth() + 1).toPaddedString(2);
    this._dayInput.value = (date.getDate()).toPaddedString(2);

    date.setUTCFullYear(this.getYear());
    date.setUTCMonth(this.getMonth() - 1);
    date.setUTCDate(this.getDay());
    date.setUTCHours(0);
    date.setUTCMilliseconds(0);
    date.setUTCMinutes(0);
    date.setUTCSeconds(0);
    this._timestampInput.value = 
      this.getYear() + '-' + (this.getMonth()).toPaddedString(2) + '-' + (this.getDay()).toPaddedString(2);
  },
  getDay : function() {
    return this._dayInput.value == '' ? NaN : new Number(this._dayInput.value);
  },
  getMonth : function() {
    return this._monthInput.value == '' ? NaN : new Number(this._monthInput.value);
  },
  getYear : function() {
    return this._yearInput.value == '' ? NaN : new Number(this._yearInput.value);
  },
  getTimestamp : function() {
    return this._timestampInput.value == NaN ? '' : new Number(this._timestampInput.value);
  },
  getTimestampNode: function () {
    return this._timestampInput;
  },
  disable: function () {
    this._dayInput.disabled = true;
    this._monthInput.disabled = true;
    this._yearInput.disabled = true;
  },
  enable: function () {
    this._dayInput.disabled = false;
    this._monthInput.disabled = false;
    this._yearInput.disabled = false;
  },
  getDOMNode: function () {
    return this._domNode;
  },
  getDayField: function () {
    return this._dayInput;
  },
  getMonthField: function () {
    return this._monthInput;
  },
  getYearField: function () {
    return this._yearInput;
  },
  parseValue : function(dateStr) {
    // yyyy-mm-dd
    var yyyy = new Number(dateStr.substring(0, 4));
    var mm = new Number(dateStr.substring(5, 7));
    var dd = new Number(dateStr.substring(8, 10));
    
    if (!isNaN(yyyy) && !isNaN(mm) && !isNaN(dd)) {
      try {
        var date = new Date();
        
        date.setUTCFullYear(yyyy.valueOf());
        date.setUTCMonth(mm.valueOf() - 1);
        date.setUTCDate(dd.valueOf());
        date.setUTCHours(0);
        date.setUTCMilliseconds(0);
        date.setUTCMinutes(0);
        date.setUTCSeconds(0);
  
        return date;
      } catch (err) {
        return null;
      }
    }      
    else
      return null;
  },
  replaceNode : function(node) {
    var parent = node.parentNode;
    parent.insertBefore(this._domNode, node);
    
    if (node.value) {
      var date = this.parseValue(node.value);
      if (date != null) {
        this.setTimestamp(date.getTime());
      }
    }
    
    parent.removeChild(node);
    datePickerController.create(this._yearInput);
  },
  destroy: function() {
    datePickerController.datePickers[this._yearInput.id].destroy();
    datePickerController.datePickers[this._yearInput.id] = undefined;
    delete datePickerController.datePickers[this._yearInput.id];
    this._domNode.remove();
    this._datePicker = undefined;
  },
  _onDayFieldValueChange : function(event) {
    this._updateTimestampField();
  },
  _onMonthFieldValueChange : function(event) {
    this._updateTimestampField();
  },
  _onYearFieldValueChange : function(event) {
    this._updateTimestampField();
  },
  _onTimestampFieldValueChange: function (event) {
    if (this._updatingTimestamp != true) {
      this._updatingTimestamp = true;
      try {
        this.setTimestamp(this._timestampInput.value);
      } finally {
        this._updatingTimestamp = false;
      }
    }
  },
  _updateTimestampField : function() {
    this._updatingTimestamp = true;
    try {
      var year = this.getYear();
      var month = this.getMonth();
      var day = this.getDay();
      if (isNaN(year) || isNaN(month) || isNaN(day)) {
        this._timestampInput.value = '';
      }
      else {
        var date = new Date();
        date.setUTCFullYear(this.getYear());
        date.setUTCMonth(this.getMonth() - 1);
        date.setUTCDate(this.getDay());
        date.setUTCHours(0);
        date.setUTCMilliseconds(0);
        date.setUTCMinutes(0);
        date.setUTCSeconds(0);
        this._timestampInput.value = this.getYear() + '-' + (this.getMonth()).toPaddedString(2) + '-' + (this.getDay()).toPaddedString(2);
      }
    } finally {
      this._updatingTimestamp = false;
    }
    
    this.fire("change", {
      dateFieldComponent: this
    });
  }
});

var __ixDateFields = new Array(); 

Object.extend(IxDateField.prototype,fni.events.FNIEventSupport);

function replaceDateField(field) {
  var dateField = new IxDateField({ element: field });
    __ixDateFields.push(dateField);
  return dateField;
}

function getIxDateFields() {
  return __ixDateFields;
}

function getIxDateField(id) {
  var fields = getIxDateFields();
  
  for (var i = 0, l = fields.length; i < l; i++) {
    if (fields[i].getId() && (fields[i].getId() == id)) {
      return fields[i];
    }
  }
  
  return null;
}

function replaceDateFields(container) {
  var dateFields;
  
  if (!container)
    dateFields = $$("input[ix:datefield='true']");
  else
    dateFields = container.select("input[ix:datefield='true']");
  
  for (var i = 0; i < dateFields.length; i++) {
    replaceDateField(dateFields[i]);
  }
}
