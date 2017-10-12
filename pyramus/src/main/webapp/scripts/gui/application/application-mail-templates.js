(function() {

  document.observe('dom:loaded', function() {
    
    $('template-button-delete').hide();
    
    CKEDITOR.replace('template-field-content', {
      toolbar: [
        ['Cut','Copy','Paste','PasteText'],
        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
        ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
        ['NumberedList','BulletedList'],
        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
        ['Link','Unlink'],
        ['Table','SpecialChar']
      ],
      entities_latin: false,
      removePlugins: 'elementspath' 
    });
    
    new Ajax.Request('/applications/listmailtemplates.json', {
      method: 'get',
      onSuccess: function(response) {
        var templates = response.responseText.evalJSON().mailTemplates;
        for (var i = 0; i < templates.length; i++) {
          var template = new Element('option', {
            'value': templates[i].id
          }).update(templates[i].name);
          $('template-field-id').insert(template);
        }
      }
    });
    
    $('template-field-id').observe('change', function() {
      var id = $('template-field-id').getValue();
      if (id) {
        new Ajax.Request('/applications/getmailtemplate.json', {
          method: 'get',
          parameters: {
            id: id
          },
          onSuccess: function(response) {
            var template = response.responseText.evalJSON();
            $('template-field-line').setValue(template.line);
            $('template-field-name').setValue(template.name);
            $('template-author').update(template.author);
            $('template-author-container').show();
            $('template-field-subject').setValue(template.subject);
            CKEDITOR.instances['template-field-content'].setData(template.content);
            $('template-button-delete').show();
          }
        });
      }
      else {
        $('template-field-line').setValue('');
        $('template-field-name').setValue('');
        $('template-author-container').hide();
        $('template-field-subject').setValue('');
        CKEDITOR.instances['template-field-content'].setData('');
        $('template-button-delete').hide();
      }
    });
    
    $('template-button-save').observe('click', function() {
      CKEDITOR.instances['template-field-content'].updateElement();      
      new Ajax.Request('/applications/savemailtemplate.json', {
        method: 'post',
        parameters: {
          id: $('template-field-id').getValue(),
          line: $('template-field-line').getValue(),
          name: $('template-field-name').getValue(),
          subject: $('template-field-subject').getValue(),
          content:$('template-field-content').getValue()
        },
        onSuccess: function(transport, json){
          var templateJson = response.responseText.evalJSON();
          var template = new Element('option', {
            'value': templateJson.id
          }).update(templateJson.name);
          $('template-field-id').insert(template);
          $('template-author').update(templateJson.author);
          $('template-author-container').show();
        }
      });
    })

    $('template-button-delete').observe('click', function(event) {
      Event.stop(event);
      var dialog = new IxDialog({
        id : 'confirmRemoval',
        contentURL : GLOBAL_contextPath + '/simpledialog.page?message=' + encodeURIComponent('Oletko varma, että haluat poistaa tämän sähköpostipohjan?'),
        centered : true,
        showOk : true,
        showCancel : true,
        autoEvaluateSize : true,
        title : 'Sähköpostipohjan poisto',
        okLabel : 'Poista',
        cancelLabel : 'Peruuta'
      });
      dialog.addDialogListener(function(event) {
        if (event.name == 'okClick') {
          new Ajax.Request('/applications/archivemailtemplate.json', {
            method: 'post',
            parameters: {
              id: $('template-field-id').getValue()
            },
            onSuccess: function(response) {
              $('template-field-id').options.item($('template-field-id').selectedIndex).remove();
              $('template-field-id').dispatchEvent(new Event('change'));
            }
          });
        }
      });
      dialog.open();
    })
  });
  
}).call(this);