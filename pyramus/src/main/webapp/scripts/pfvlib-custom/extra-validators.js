DateFieldTextValidator = Class.create(fi.internetix.validation.FieldValidator, {
  initialize : function($super) {
    $super();
  },
  validate: function ($super, field) {
    var value = this._getFieldValue(field);
    if (value) {
      var fieldId = field.parentNode.getAttribute('ix:datefieldid');
      var component = fieldId ? getIxDateField(fieldId) : null;
      if (component) {
        return component.hasValidValue() ? fi.internetix.validation.FieldValidator.STATUS_VALID : fi.internetix.validation.FieldValidator.STATUS_INVALID;
      } else {
        return fi.internetix.validation.FieldValidator.STATUS_UNKNOWN;
      }
    } else {
      return fi.internetix.validation.FieldValidator.STATUS_UNKNOWN;
    }
  },
  getType: function ($super) {
    return fi.internetix.validation.FieldValidator.TYPE_NORMAL;
  },
  getClassName: function () {
    return 'ixDateFieldText';
  }
});

TableAutoCompleteSelectFieldValidator = Class.create(fi.internetix.validation.FieldValidator, {
  initialize : function($super) {
    $super();
  },
  validate: function ($super, field) {
    var idField = $(field.parentNode).down('input.ixTableCellEditorAutoCompleteSelectId');
    
    if (field.value) {
      if ((idField.value && (parseInt(idField.value) >= 0))) 
        return fi.internetix.validation.FieldValidator.STATUS_VALID;
      else 
        return fi.internetix.validation.FieldValidator.STATUS_INVALID;
    } else {
      return fi.internetix.validation.FieldValidator.STATUS_UNKNOWN;
    }
  },
  getType: function ($super) {
    return fi.internetix.validation.FieldValidator.TYPE_NORMAL;
  },
  getClassName: function () {
    return 'ixTableCellEditorAutoCompleteSelectText';
  }
});

fi.internetix.validation.FieldValidatorVault.registerValidator(new DateFieldTextValidator());
fi.internetix.validation.FieldValidatorVault.registerValidator(new TableAutoCompleteSelectFieldValidator());