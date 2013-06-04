var __ixDateFields = new Array(); 

IxDateField = Class.create(
  {
  initialize : function(options) {
    var element = options.element;
    this._paramName = element.name;
    
    this._inputTextChangeListener = this._onInputTextChange.bindAsEventListener(this);
    this._inputTextKeyUpListener = this._onInputTextKeyUp.bindAsEventListener(this);
    this._todayButtonClickListener = this._onTodayButtonClick.bindAsEventListener(this);
    this._openButtonClickListener = this._onOpenButtonClick.bindAsEventListener(this);
    
    var idAttr = element.getAttribute("ix:datefieldid");
    if (idAttr)
      this._id = idAttr;
    else {
      this._id = 'ixdf-' + new Date().getTime();
    }
    
    __ixDateFields.push(this);
    
    var classNames = $w(element.className);
    var value = element.value;
    
    this._inputText = new Element("input", {id: this._paramName + "-text", maxlength: 10, className: "ixDateFieldText"});
    for (var i = 0, l = classNames.length; i < l; i++) {
      this._inputText.addClassName(classNames[i]);
    }
    
    var parent = element.parentNode;
    var nextSibling = element.nextSibling;
    
    if (Prototype.Browser.IE) {
      this._timestampInput = new Element("input", {type: "hidden", name: this._paramName, value: value});
      $(element).remove();
    } else {
      this._timestampInput = element;
      this._timestampInput.type = 'hidden';
      this._timestampInput.removeAttribute('ix:datefieldid');
      this._timestampInput.removeClassName("ixDateField");
      this._timestampInput.className = '';
    }
    
    this._openButton = new Element("div", { className: "ixDateFieldOpenButton", title: getLocale().getText("generic.dateField.openButtonTooltip") });
    this._todayButton = new Element("div", { className: "ixDateFieldTodayButton", title: getLocale().getText("generic.dateField.todayButtonTooltip")});
    
    Event.observe(this._openButton, "click", this._openButtonClickListener);
    Event.observe(this._todayButton, "click", this._todayButtonClickListener);
    Event.observe(this._inputText, "change", this._inputTextChangeListener);
    Event.observe(this._inputText, "keyup", this._inputTextKeyUpListener);
    
    this._domNode = new Element("div", {className: 'ixDateField', "ix:datefieldid": this._id});
    this._domNode.appendChild(this._timestampInput);
    this._domNode.appendChild(this._inputText);
    this._domNode.appendChild(this._openButton);
    this._domNode.appendChild(this._todayButton);
    
    if (nextSibling) {
      parent.insertBefore(this._domNode, nextSibling);
    } else {
      parent.appendChild(this._domNode);
    }
    
    var language = 'en';
    var dateFormat;
    var dateSeparator;
    
    if (Object.isFunction(getLocale)) { 
      language = getLocale().getLanguage();
      var pattern = getLocale().getDateFormat(false).getPattern();
      
      if (pattern.indexOf('/') > 0) {
        dateSeparator = '/';
      } else if (pattern.indexOf('.') > 0) {
        dateSeparator = '.';
      } else if (pattern.indexOf('-') > 0) {
        dateSeparator = '-';
      }
      
      if (dateSeparator) {
        var components = pattern.split(dateSeparator);
        dateFormat = new Array();
        for (var i = 0, l = components.length; i < l; i++) {
          var component = components[i].toLowerCase();
          switch (component) {
            case 'yy':
              component = 'yyyy';
            break;
            case 'm':
              component = 'mm';
            break;
            case 'd':
              component = 'dd';
            break;
          } 
          
          dateFormat.push(component);
        }
      }
    }
    
    // date format defaults to international date format yyyy-mm-dd (ISO 8601)

    if (!dateFormat)
      dateFormat = ['yyyy','mm','dd'];
    if (!dateSeparator)
      dateSeparator = '-';
    
    var _this = this;
    var _DatePicker = Class.create(DatePicker, {
      getDatePickerFormatter: function () {
        if (!this._df) {
          this._initCurrentDate(); 
        }
        return this._df;
      },
      setCurrentDate: function (date) {
        this._maybeRedrawMonth([date.getUTCMonth(), date.getUTCFullYear()]);
      },
      _getCell: function (year, month, date) {
        var cellId = $A([this._id_datepicker, this._df.dateToString(year, month + 1, date, '-')]).join('-');
        return $(cellId);
      },
      _buildCalendar: function ($super) {
        $super();
        
        var selectedTs = _this.getTimestamp();
        if (!_this._isEmptyTimestamp(selectedTs)) {
          var selectedDate = new Date(selectedTs);
          var cell = this._getCell(selectedDate.getUTCFullYear(), selectedDate.getUTCMonth(), selectedDate.getUTCDate());
          if (cell) {
            cell.addClassName("selectedDate");
          }
        }
      },
      _initCurrentDate: function ($super) {
        if (!this._df) {
          $super();
        }
      }
    });
    
    this._datePicker = new _DatePicker({
      relative: this._inputText,
      keepFieldEmpty:true,
      relativePosition: false,
      language: language,
      topOffset: 25,
      zindex: 99999,
      leftOffset: 0,
      dateFormat: [dateFormat, dateSeparator],
      afterClose: function () {
        if (_this._inputText.value) { 
          var selectedDate = _this._datePicker.getDatePickerFormatter().match(_this._inputText.value);    
          var date = new Date();
          date.setUTCFullYear(selectedDate[0], selectedDate[1] - 1, selectedDate[2]);
          date.setUTCHours(0, 0, 0, 0);
          _this.setTimestamp(date.getTime());
        } else {
          _this.clearValue();
        }
      },
      clickCallback: function () {
        var relativeDimensions = $(_this._datePicker._relative).getDimensions();
        var relativePosition =$(_this._datePicker._relative).cumulativeOffset();
       
        var pickerDimensions = $(_this._datePicker._div).getDimensions();
        var pickerRight = relativePosition.left + pickerDimensions.width;
        
        var viewportWidth = document.viewport.getWidth();
        
        if (viewportWidth < pickerRight) {
          _this._datePicker.setPosition(relativePosition.top, relativePosition.left - pickerDimensions.width + relativeDimensions.width);
        } else {
          _this._datePicker.setPosition(relativePosition.top, relativePosition.left);
        }
        
        var currentTs = _this.getTimestamp();
        
        if (!_this._isEmptyTimestamp(currentTs))
          _this._datePicker.setCurrentDate(new Date(currentTs));
      }
    });
    
    if (!this._isEmptyTimestamp(value)) {
      this.setTimestamp(value);
    }
    
    initializeElementValidation(this._inputText);
  },
  hasValidValue: function () {
    return this._datePicker.getDatePickerFormatter().match(this._inputText.value) != false; 
  },
  getId : function() {
    return this._id;
  },
  setTimestamp : function(timestamp) {
    if (!this._isEmptyTimestamp(timestamp)) {
      var date = new Date();
      date.setTime(timestamp);
      this._inputText.value = this._datePicker.getDatePickerFormatter().dateToString(date.getFullYear(), date.getMonth() + 1, date.getDate());
      date.setUTCFullYear(date.getFullYear(), date.getMonth(), date.getDate());
      date.setUTCHours(0, 0, 0, 0);
      this._timestampInput.value = date.getTime();
      this._inputText.validate();
    } else {
      this.clearValue();
    }
    
    this.fire("change", {
      dateFieldComponent: this
    });
  },
  clearValue: function () {
    this._inputText.value = '';
    this._timestampInput.value = '';
  },
  getTimestamp : function() {
    var timestamp = this._timestampInput.value;
    if (!this._isEmptyTimestamp(timestamp))
      return new Number(this._timestampInput.value);
    else
      return '';
  },
  getTimestampNode: function () {
    return this._timestampInput;
  },
  disable: function () {
    this._inputText.disabled = true;
    this._inputText.setAttribute("disabled", "disabled");
  },
  enable: function () {
    this._inputText.disabled = false;
    this._inputText.removeAttribute("disabled");
  },
  isDisabled: function () {
    return this._inputText.disabled;
  },
  getDOMNode: function () {
    return this._domNode;
  },
  destroy: function() {
    var nextSibling = this._domNode.nextSibling;
    var parent = this._domNode.parentNode;
    if (Prototype.Browser.IE) {
      var inputElement = new Element("input", {type: "text", "ix:datefieldid": this.getId(), className: "ixDateField"});
      if (nextSibling) {
        parent.insertBefore(inputElement, nextSibling);
      } else {
        parent.appendChild(inputElement);
      }
      this._timestampInput.remove();
    } else {
      this._timestampInput.type = 'text';
      this._timestampInput.setAttribute('ix:datefieldid', this.getId());
      this._timestampInput.addClassName("ixDateField");
      
      if (nextSibling) {
        parent.insertBefore(this._timestampInput, nextSibling);
      } else {
        parent.appendChild(this._timestampInput);
      }
    }
    
    Event.stopObserving(this._openButton, "click", this._openButtonClickListener);
    Event.stopObserving(this._todayButton, "click", this._todayButtonClickListener);
    Event.stopObserving(this._inputText, "change", this._inputTextChangeListener);
    Event.stopObserving(this._inputText, "keyup", this._inputTextKeyUpListener);
    
    this._domNode.remove();
  },
  _isEmptyTimestamp: function (timestamp) {
    if (!timestamp) {
      if ((timestamp === 0)||(timestamp === '0'))  
        return false;
      else
        return true;
    } else {
      return false;
    }
  },
  _checkInputTextChange: function () {
    var selectedDate = this._datePicker.getDatePickerFormatter().match(this._inputText.value);
    
    if (selectedDate) {   
      var date = new Date();
      date.setUTCFullYear(selectedDate[0], selectedDate[1] - 1, selectedDate[2]);
      date.setUTCHours(0, 0, 0, 0);
      this._timestampInput.value = date.getTime();
    } else {
      this._timestampInput.value = '';
    } 
  },
  _onTodayButtonClick: function (event) {
    if (!this.isDisabled())
      this.setTimestamp(new Date().getTime());
  },
  _onOpenButtonClick: function (event) {
    if (!this.isDisabled())
      this._datePicker.click();
  },
  _onInputTextChange: function (event) {
     this._checkInputTextChange(); 
  },
  _onInputTextKeyUp: function (event) {
    this._checkInputTextChange();
  }
});

Object.extend(IxDateField.prototype,fni.events.FNIEventSupport);

function replaceDateField(field, options) {
  var dateField = new IxDateField(Object.extend({ element: field }, options||{}));
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
    dateFields = $$("input.ixDateField");
  else
    dateFields = container.select("input.ixDateField");
  
  for (var i = 0; i < dateFields.length; i++) {
    replaceDateField(dateFields[i]);

    if (Object.isFunction(Element.validate))
      Element.validate(dateFields[i]);
  }
}
