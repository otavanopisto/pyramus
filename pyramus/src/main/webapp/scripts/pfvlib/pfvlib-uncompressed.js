if ((typeof fi) == 'undefined') {
  /**
   * @namespace fi package
   */
  var fi = {};
};if ((typeof fi.internetix) == 'undefined') {
  /**
   * @namespace fi.internetix package
   */
  fi.internetix = {};
};if ((typeof fi.internetix.validation) == 'undefined') {
  /**
   * @namespace fi.internetix.validation package
   */
  fi.internetix.validation = {};
};if ((typeof fi.internetix.validation.validator) == 'undefined') {
  /**
   * @namespace fi.internetix.validation.validator package
   */
  fi.internetix.validation.validator = {};
};/*
 * Base class for all field validators
 *  
 * Copyright (C) 2011 Antti Leppä / Otavan Opisto
 * http://www.otavanopisto.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 3 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fi.internetix.validation.FieldValidator = Class.create(
  /** @lends fi.internetix.validation.FieldValidator# */ 
  {
  /**
  * Class constructor
  * @class Base class for all field validators
  * @constructs
  */
  initialize : function() {},
  /**
   * Validates a field and returns it's status
   * 
   * @param field field to be validated
   * @returns 
   *   <table>
   *     <tr>
   *       <td>fi.internetix.validation.FieldValidator#STATUS_UNKNOWN</td><td>&nbsp;&nbsp;If field cannot be validated</td>
   *     </tr>
   *     <tr>
   *       <td>fi.internetix.validation.FieldValidator#STATUS_VALID</td><td>&nbsp;&nbsp;If field is valid</td>
   *     </tr>
   *     <tr>
   *       <td>fi.internetix.validation.FieldValidator#INVALID</td><td>&nbsp;&nbsp;If field is invalid</td>
   *     </tr>
   *   </table>
   */
  validate: function (field) {},
  /**
   * Returns field type
   * 
   * @returns
   *   <table>
   *     <tr>
   *       <td>fi.internetix.validation.FieldValidator.TYPE_NORMAL</td><td>&nbsp;&nbsp;For normal fields</td>
   *     </tr>
   *     <tr>
   *       <td>fi.internetix.validation.FieldValidator.TYPE_LINKED</td><td>&nbsp;&nbsp;For linked fields</td>
   *     </tr>
   *   </table>
   *   
   */
  getType: function () {
    return null;
  },
  /**
   * Returns class name of elements validator validates
   */
  getClassName: function () {
  },
  /**
   * @private   
   */
  _getFieldValue: function (field) {
    if (field.type == 'checkbox')
      return field.checked ? '0' : '1';
    else
      return field.value;
  }
});

/** @class **/
Object.extend(fi.internetix.validation.FieldValidator, 
  /** @lends fi.internetix.validation.FieldValidator# */   
  {
  /**
   * Invalid status
   */
  STATUS_UNDEFINED: -1,
  /**
   * Unknown status
   */
  STATUS_UNKNOWN: 0,
  /**
   * Invalid status
   */
  STATUS_INVALID: 1,
  /**
   * Valid status
   */
  STATUS_VALID: 2,

  /**
   * Normal validator
   */
  TYPE_NORMAL:0,
  /**
   * Mandatory validator
   */
  TYPE_MANDATORY:1,
  /**
   * Linked validator
   */
  TYPE_LINKED:2    
});fi.internetix.validation.LinkedFieldValidator = Class.create(fi.internetix.validation.FieldValidator, 
  /** @lends fi.internetix.validation.LinkedFieldValidator# **/   
  {
  /**
  * Class constructor
  * @class Constructs this class
  * @extends fi.internetix.validation.FieldValidator
  * @constructs
  * @param $super super class 
  */
  initialize : function($super) {
    $super();
  },
  getLinkedField: function (field) {},
  getType: function ($super) {
    return fi.internetix.validation.FieldValidator.TYPE_LINKED;
  }
});/*
 * Field validator vault
 *  
 * Copyright (C) 2011 Antti Leppä / Otavan Opisto
 * http://www.otavanopisto.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 3 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fi.internetix.validation.FieldValidatorVault = 
  /** @lends fi.internetix.validation.FieldValidatorVault# */ 
  {
  /**
   * @class Static class for storing field validators
   * @constructs
   */  
  initialize: function () { }, 
  /**
   * Registers a validator
   * 
   * @param validator validator
   */
  registerValidator: function (validator) {
    this._validators.set(validator.getClassName(), validator);
  },
  /**
   * Returns all registred validators
   * 
   * @returns all registred validators
   */
  getValidators: function () {
    return this._validators.values();
  },
  _validators: new Hash()
};fi.internetix.validation.ValidationDelegator = Class.create(
  /** @lends fi.internetix.validation.ValidationDelegator# */ 
  {
  /**
  * Class constructor
  * @class Validation delegator for single field
  * @constructs
  */
  initialize : function(field) {
    this._field = field;
    this._fieldChangeListener = this._onFieldChange.bindAsEventListener(this);
  
    if (field.type == 'checkbox') {
      Event.observe(field, 'click', this._fieldChangeListener);
    } else {
      Event.observe(field, 'keyup', this._fieldChangeListener);
      Event.observe(field, 'change', this._fieldChangeListener);
    }
    this._validators = new Array();
  },
  /**
   * Class deinitialization
   */
  deinitialize: function () {
    this._validators.clear();
    
    if (this._field.type == 'checkbox') {
      Event.stopObserving(this._field, 'click', this._fieldChangeListener);
    } else {
      Event.stopObserving(this._field, 'keyup', this._fieldChangeListener);
      Event.stopObserving(this._field, 'change', this._fieldChangeListener);
    }
    
    delete this._validators;
    this._validators = undefined;
    this._field = undefined;
  },
  /**
   * Adds new validator for field
   * 
   * @param validator validator
   */
  addValidator: function (validator) {
    this._validators.push(validator);
  },
  /**
   * Validates field
   * 
   * @param requiredCheckAsUnknown whether required should be interpret as unknown
   */
  validate: function (requiredCheckAsUnknown) {
    this._validate(requiredCheckAsUnknown, this._validators, fi.internetix.validation.FieldValidator.STATUS_UNKNOWN);
  },
  /**
   * Resets field validity before validation
   * 
   * @param requiredCheckAsUnknown whether required should be interpret as unknown
   */
  forceValidate: function (requiredCheckAsUnknown) {
    this._field._validity = undefined;
    this.validate(requiredCheckAsUnknown);
  },
  /**
   * Returns field validity status
   * @returns field validity status
   */
  getStatus: function () {
    return this._field._validity == undefined ? fi.internetix.validation.FieldValidator.STATUS_UNDEFINED : this._field._validity;
  },
  /**
   * Retruns whether field is mandatory or not
   * 
   * @returns {Boolean} true if field is mandatory
   */
  isMandatory: function () {
    for (var i = this._validators.length - 1; i >= 0; i--) {
      if (this._validators[i].getType() == fi.internetix.validation.FieldValidator.TYPE_MANDATORY)
        return true;
    }
    
    return false;
  },
  /**
   * Returns whether field is inside form  
   * 
   * @param formElement form
   * @returns {Boolean} whether field is inside form  
   */
  insideForm: function (formElement) {
    return this._field.form == formElement;
  },
  _isVisible: function () {
    var e = $(this._field);
    
    if (e.getAttribute('type') == 'hidden')
      return false;
    
    while (e.parentNode) {
      if (Object.isFunction(e.visible) && !e.visible())
        return false;
      
      e = $(e.parentNode);
    }
    
    return true;
  },
  _validate: function (requiredCheckAsUnknown, validators, initialStatus) {
    var oldStatus = this.getStatus();
    var status = initialStatus;
    
    if (this._isVisible()) {
      for (var i = 0, l = validators.length; i < l; i++) {
        if (status != fi.internetix.validation.FieldValidator.STATUS_INVALID) {
          switch (validators[i].validate(this._field)) {
            case fi.internetix.validation.FieldValidator.STATUS_INVALID:
              if (requiredCheckAsUnknown && (validators[i].getType() == fi.internetix.validation.FieldValidator.TYPE_MANDATORY))
                status = fi.internetix.validation.FieldValidator.STATUS_UNKNOWN;
              else
                status = fi.internetix.validation.FieldValidator.STATUS_INVALID;
            break;
            case fi.internetix.validation.FieldValidator.STATUS_VALID:
              status = fi.internetix.validation.FieldValidator.STATUS_VALID;
            break;
          }
        } else {
          break;
        }
      }
      
      if (status != fi.internetix.validation.FieldValidator.STATUS_UNKNOWN) {
        var linkedValidators = this._getLinkedValidators(validators);
        for (var i = 0, l = linkedValidators.length; i < l; i++) {
          var linkedField = linkedValidators[i].getLinkedField(this._field);
          var fieldDelegator = fi.internetix.validation.ValidationDelegatorVault.getDelegator(linkedField);
          fieldDelegator._linkedValidate(requiredCheckAsUnknown, status, linkedValidators[i]);
        }
      }
    } else {
      // Invisible elements are always "valid"
      status = fi.internetix.validation.FieldValidator.STATUS_VALID;
    }
    
    if (oldStatus != status) {
      this._field._validity = status;
      
      switch (status) {
        case fi.internetix.validation.FieldValidator.STATUS_UNKNOWN:
          if (oldStatus == fi.internetix.validation.FieldValidator.STATUS_VALID)
            this._field.removeClassName('valid');
          else 
            this._field.removeClassName('invalid');
          __formValidationHook($(this._field.form), this.isMandatory());          
        break;
        case fi.internetix.validation.FieldValidator.STATUS_INVALID:
          if (oldStatus == fi.internetix.validation.FieldValidator.STATUS_VALID)
            this._field.removeClassName('valid');
          this._field.addClassName('invalid');
          __formValidationHook($(this._field.form), true);
        break;
        case fi.internetix.validation.FieldValidator.STATUS_VALID:
          if (oldStatus != fi.internetix.validation.FieldValidator.STATUS_VALID)
            this._field.removeClassName('invalid');
          this._field.addClassName('valid');
          __formValidationHook($(this._field.form), false);
        break;
      }
    } 
  },
  _getLinkedValidators: function (validators) {
    var result = new Array();
    for (var i = 0, l = validators.length; i < l; i++) {
      var validator = validators[i];
      if (validator.getType() == fi.internetix.validation.FieldValidator.TYPE_LINKED) 
        result.push(validator);
    }
    
    return result;
  },
  _linkedValidate: function (requiredCheckAsUnknown, status, linkedValidator) {
    this._validate(requiredCheckAsUnknown, this._validators.without(linkedValidator), status);
  },
  _onFieldChange: function (event) {
    this.validate(false);
  }
});

function __formValidationHook(formElement, isInvalid) {
  if (formElement) {
    formElement = $(formElement);
    var formValidButton = formElement.down(".formvalid");
    if (formValidButton) {
      var valid = !isInvalid;

      if (valid) {
        var delegators = fi.internetix.validation.ValidationDelegatorVault.getFormDelegators(formElement);
        for ( var i = 0, l = delegators.length; i < l; i++) {
          switch (delegators[i].getStatus()) {
            case fi.internetix.validation.FieldValidator.STATUS_INVALID:
              valid = false;
            break;
            case fi.internetix.validation.FieldValidator.STATUS_UNKNOWN:
              if (delegators[i].isMandatory()) {
                valid = false;
              }
            break;
          }

          if (valid == false) {
            break;
          }
        }
      }

      if (valid)
        formValidButton.removeAttribute("disabled");
      else
        formValidButton.setAttribute("disabled", "disabled");
    }
  }
};fi.internetix.validation.ValidationDelegatorVault =
  /** @lends fi.internetix.validation.ValidationDelegatorVault# */ 
  {
  /**
   * @class Static class for storing validation delegators
   * @constructs
   */  
  initialize: function () { }, 
  getDelegator: function (field) {
    return this._delegators.get(this._generateFieldName(field));
  },
  setDelegator: function (field, validationDelegator) {
    if (!this.getDelegator(field)) {
      this._delegators.set(this._generateFieldName(field), validationDelegator);
    } 
  },
  releaseDelegator: function (field) {
    var delegator = this.getDelegator(field);
    if (delegator) {
      this._delegators.unset(this._generateFieldName(field));
      delegator.deinitialize();
      delete delegator;
    }
  },
  getDelegators: function () {
    return this._delegators.values();
  },
  getFormDelegators: function (formElement) {
    var delegators = new Array();
    
    var allDelegators = this.getDelegators();
    for (var i = 0, l = allDelegators.length; i < l; i++) {
      if (allDelegators[i].insideForm(formElement))
        delegators.push(allDelegators[i]);
    }
    
    return delegators;
  },
  _generateFieldName: function (field) {
    return field.identify();
  },
  _delegators: new Hash()
};/**
 * Initializes validation for container.
 * 
 * @param container container. If left undefined document.body will be used
 */
function initializeValidation(container) {
  var delegators = new Array();
  var c = $(container || document.body);
  var validators = fi.internetix.validation.FieldValidatorVault.getValidators();
  for ( var i = 0, l = validators.length; i < l; i++) {
    var fields = c.select('.' + validators[i].getClassName());
    for ( var j = 0, le = fields.length; j < le; j++) {
      var field = fields[j];
      var delegator = fi.internetix.validation.ValidationDelegatorVault.getDelegator(field);
      if (!delegator) {
        delegator = new fi.internetix.validation.ValidationDelegator(field);
        fi.internetix.validation.ValidationDelegatorVault.setDelegator(field, delegator);
      }

      delegator.addValidator(validators[i]);
      delegators.push(delegator);
    }
  }
  ;

  var uniqueDelegators = delegators.uniq();
  for ( var i = 0, l = uniqueDelegators.length; i < l; i++)
    uniqueDelegators[i].validate(true);

  delete delegators;
  delete uniqueDelegators;
};

/**
 * Initializes validation for single element
 * 
 * @param element element
 */
function initializeElementValidation(element) {
  var delegator = fi.internetix.validation.ValidationDelegatorVault.getDelegator(element);
  if (!delegator) {
    delegator = new fi.internetix.validation.ValidationDelegator(element);
    fi.internetix.validation.ValidationDelegatorVault.setDelegator(element, delegator);
  }

  var validators = fi.internetix.validation.FieldValidatorVault.getValidators();
  for ( var i = 0, l = validators.length; i < l; i++) {
    var validator = validators[i];
    if (element.hasClassName(validator.getClassName()))
      delegator.addValidator(validator);
  }

  delegator.validate(true);
};

/**
 * Deinitializes validation in container
 * 
 * @param container container. If left undefined document.body will be used
 */
function deinitializeValidation(container) {
  var c = container || document.body;
  for ( var i = 0, l = c.childNodes.length; i < l; i++) {
    if (c.childNodes[i].nodeType == 1) {
      deinitializeValidation(c.childNodes[i]);
      fi.internetix.validation.ValidationDelegatorVault.releaseDelegator(c.childNodes[i]);
    }
  }
  ;
}

/**
 * Revalidates all elements
 * 
 * @param requiredCheckAsUnknown whether required should be interpret as unknown
 */
function revalidateAll(requiredCheckAsUnknown) {
  var delegators = fi.internetix.validation.ValidationDelegatorVault.getDelegators();
  for ( var i = 0, l = delegators.length; i < l; i++) {
    delegators[i].validate(requiredCheckAsUnknown);
  }
};

/**
 * Force revalidates all elements
 * 
 * @param requiredCheckAsUnknown whether required should be interpret as unknown
 */
function forceRevalidateAll(requiredCheckAsUnknown) {
  var delegators = fi.internetix.validation.ValidationDelegatorVault.getDelegators();
  for ( var i = 0, l = delegators.length; i < l; i++) {
    delegators[i].forceValidate(requiredCheckAsUnknown);
  }
};

Element.addMethods({
  validate : function(element, requiredCheckAsUnknown, forceRevalidate) {
    var delegator = fi.internetix.validation.ValidationDelegatorVault.getDelegator(element);
    if (delegator) {
      if (!forceRevalidate)
        delegator.validate(requiredCheckAsUnknown);
      else
        delegator.forceValidate(requiredCheckAsUnknown);
    }
  }
});/*
 * Email field validator
 *  
 * Copyright (C) 2011 Antti Leppä / Otavan Opisto
 * http://www.otavanopisto.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 3 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fi.internetix.validation.validator.EmailFieldValidator = Class.create(fi.internetix.validation.FieldValidator, 
  /** @lends fi.internetix.validation.validator.EmailFieldValidator# **/   
  {
  /**
  * Class constructor
  * @class Constructs this class
  * @extends fi.internetix.validation.FieldValidator
  * @constructs
  * @param $super super class 
  */
  initialize : function($super) {
    $super();
    this._validEmailMask = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
  },
  validate: function ($super, field) {
    var value = this._getFieldValue(field);
    
    if (value) {
      return this._validEmailMask.test(value.strip()) ? fi.internetix.validation.FieldValidator.STATUS_VALID : fi.internetix.validation.FieldValidator.STATUS_INVALID;
    } else {
      return fi.internetix.validation.FieldValidator.STATUS_UNKNOWN;
    }
  },
  getType: function ($super) {
    return fi.internetix.validation.FieldValidator.TYPE_NORMAL;
  },
  getClassName: function () {
    return 'email';
  }
});

fi.internetix.validation.FieldValidatorVault.registerValidator(new fi.internetix.validation.validator.EmailFieldValidator());/*
 * Equals field validator
 *  
 * Copyright (C) 2011 Antti Leppä / Otavan Opisto
 * http://www.otavanopisto.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 3 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fi.internetix.validation.validator.EqualsFieldValidator = Class.create(fi.internetix.validation.LinkedFieldValidator,
  /** @lends fi.internetix.validation.validator.EqualsFieldValidator# **/   
  {
  /**
  * Class constructor
  * @class Constructs this class
  * @extends fi.internetix.validation.LinkedFieldValidator
  * @constructs
  * @param $super super class 
  */
  initialize : function($super) {
    $super();
  },
  validate: function ($super, field) {
    var equalsField = this.getLinkedField(field);
    if (equalsField) {
      var value1 = this._getFieldValue(field);
      var value2 = this._getFieldValue(equalsField);
      if (value1) 
        return value1 == value2 ? fi.internetix.validation.FieldValidator.STATUS_VALID : fi.internetix.validation.FieldValidator.STATUS_INVALID;
    } 
    
    return fi.internetix.validation.FieldValidator.STATUS_UNKNOWN;
  },
  getLinkedField: function (field) {
    var fieldClassNames = $w(field.className);
    for (var i = 0, l = fieldClassNames.length; i < l; i++) {
      if (fieldClassNames[i].startsWith('equals-')) {
        var equalsFields = document.getElementsByName(fieldClassNames[i].substring(7));
        if (equalsFields.length == 1) {
          return equalsFields[0];
        }
      }
    }
    
    return null;
  },
  getClassName: function () {
    return 'equals';
  }
});

fi.internetix.validation.FieldValidatorVault.registerValidator(new fi.internetix.validation.validator.EqualsFieldValidator());/*
 * Float field validator
 *  
 * Copyright (C) 2011 Antti Leppä / Otavan Opisto
 * http://www.otavanopisto.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 3 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fi.internetix.validation.validator.FloatValidValidator = Class.create(fi.internetix.validation.FieldValidator, 
  /** @lends fi.internetix.validation.validator.FloatValidValidator# **/   
  {
  /**
  * Class constructor
  * @class Constructs this class
  * @extends fi.internetix.validation.FieldValidator
  * @constructs
  * @param $super super class 
  */
  initialize : function($super) {
    $super();
    this._validFloatMask = /(^[0-9]+[,\.][0-9]+$)|(^[0-9]+$)/;
  },
  validate: function ($super, field) {
    var value = this._getFieldValue(field);
    if (value) {
      return this._validFloatMask.test(value) ? fi.internetix.validation.FieldValidator.STATUS_VALID : fi.internetix.validation.FieldValidator.STATUS_INVALID;
    } else {
      return fi.internetix.validation.FieldValidator.STATUS_UNKNOWN;
    }
  },
  getType: function ($super) {
    return fi.internetix.validation.FieldValidator.TYPE_NORMAL;
  },
  getClassName: function () {
    return 'float';
  }
});

fi.internetix.validation.FieldValidatorVault.registerValidator(new fi.internetix.validation.validator.FloatValidValidator());/*
 * Mask field validator
 *  
 * Copyright (C) 2011 Antti Leppä / Otavan Opisto
 * http://www.otavanopisto.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 3 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fi.internetix.validation.validator.MaskFieldValidator = Class.create(fi.internetix.validation.FieldValidator,
  /** @lends fi.internetix.validation.validator.MaskFieldValidator# **/   
  {
  /**
  * Class constructor
  * @class Constructs this class
  * @extends fi.internetix.validation.FieldValidator
  * @constructs
  * @param $super super class 
  */
  initialize : function($super) {
    $super();
  },
  validate: function ($super, field) {
    var value = this._getFieldValue(field);
    var mask = field.getAttribute("data-validatemask");
    
    if (mask && value) {
      return new RegExp(mask).test(value) ? fi.internetix.validation.FieldValidator.STATUS_VALID : fi.internetix.validation.FieldValidator.STATUS_INVALID;
    } else {
      return fi.internetix.validation.FieldValidator.STATUS_UNKNOWN;
    }
  },
  getType: function ($super) {
    return fi.internetix.validation.FieldValidator.TYPE_NORMAL;
  },
  getClassName: function () {
    return 'mask';
  }
});

fi.internetix.validation.FieldValidatorVault.registerValidator(new fi.internetix.validation.validator.MaskFieldValidator());/*
 * Number field validator
 *  
 * Copyright (C) 2011 Antti Leppä / Otavan Opisto
 * http://www.otavanopisto.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 3 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fi.internetix.validation.validator.NumberValidValidator = Class.create(fi.internetix.validation.FieldValidator,
  /** @lends fi.internetix.validation.validator.NumberValidValidator# **/   
  {
  /**
  * Class constructor
  * @class Constructs this class
  * @extends fi.internetix.validation.FieldValidator
  * @constructs
  * @param $super super class 
  */
  initialize : function($super) {
    $super();
    this._validNumberMask = /^([0-9]*)$/;
  },
  validate: function ($super, field) {
    var value = this._getFieldValue(field);
    if (value) {
      return this._validNumberMask.test(value) ? fi.internetix.validation.FieldValidator.STATUS_VALID : fi.internetix.validation.FieldValidator.STATUS_INVALID;
    } else {
      return fi.internetix.validation.FieldValidator.STATUS_UNKNOWN;
    }
  },
  getType: function ($super) {
    return fi.internetix.validation.FieldValidator.TYPE_NORMAL;
  },
  getClassName: function () {
    return 'number';
  }
});

fi.internetix.validation.FieldValidatorVault.registerValidator(new fi.internetix.validation.validator.NumberValidValidator());/*
 * Required field validator
 *  
 * Copyright (C) 2011 Antti Leppä / Otavan Opisto
 * http://www.otavanopisto.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 3 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fi.internetix.validation.validator.RequiredFieldValidator = Class.create(fi.internetix.validation.FieldValidator,
  /** @lends fi.internetix.validation.validator.RequiredFieldValidator# **/   
  {
  /**
  * Class constructor
  * @class Constructs this class
  * @extends fi.internetix.validation.FieldValidator
  * @constructs
  * @param $super super class 
  */
  initialize : function($super) {
    $super();
  },
  validate: function ($super, field) {
    return this._getFieldValue(field).blank() ? fi.internetix.validation.FieldValidator.STATUS_INVALID : fi.internetix.validation.FieldValidator.STATUS_VALID;
  },
  getType: function ($super) {
    return fi.internetix.validation.FieldValidator.TYPE_MANDATORY;
  },
  getClassName: function () {
    return 'required';
  }
});

fi.internetix.validation.FieldValidatorVault.registerValidator(new fi.internetix.validation.validator.RequiredFieldValidator());