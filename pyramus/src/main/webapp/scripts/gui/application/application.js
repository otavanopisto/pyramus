(function() {

  var applicationSections = $('.form-section');

  $(document).ready(function() {
    
    $.fn.serializeObject = function() {
      var o = {};
      var a = this.serializeArray();
      $.each(a, function() {
        if (o[this.name] !== undefined) {
          if (!o[this.name].push) {
            o[this.name] = [o[this.name]];
          }
          o[this.name].push(this.value || '');
        }
        else {
          o[this.name] = this.value || '';
        }
      });
      return o;
    };    

    // File uploaders
    
    $('.field-attachments-uploader').each(function() {
      var fileInput = $(this).find('input');
      var fileSelector = $(this).find('.field-attachments-selector');
      fileSelector.on('click', function() {
        fileInput.click();
      });
      fileInput.on('change', function() {
        var files = fileInput[0].files;
        for (var i = 0; i < files.length; i++) {
          uploadAttachment(files[i]);
        }
        
        
        
        
        
      });
    });
    
    // Dynamic data
    
    $('select[data-source]').each(function() {
      var field = this;
      $.ajax({
        async: false,
        url: $(field).attr('data-source'),
        type: "GET",
        contentType: "application/json; charset=utf-8",
        success: function(result) {
          for (var i = 0; i < result.length; i++) {
            var option = $('<option>').attr('value', result[i].value).text(result[i].text);
            $(field).append(option);
            if ($(field).attr('data-preselect') && result[i].text == $(field).attr('data-preselect')) {
              $(option).prop('selected', true);
            }
          }
        }
      });
    });
    
    // Section checks
    
    $('select[name="field-line"]').on('change', function() {
      var line = $(this).val();
      var option =  $(this).find('option:selected');
      var isLocalLine = $(option).attr('data-local-line');
      $('#field-studyprogramme-id').val($(option).attr('data-studyprogramme'));
      $('.section-other-studies').attr('data-skip', !isLocalLine);
      $('.section-attachments').attr('data-skip', !isLocalLine);
    });
    $('input[name="field-birthday"]').on('change', function() {
      var birthday = $(this).val();
      var years = moment().diff(moment(birthday, "D.M.YYYY"), 'years');
      var line = $('select[name="field-line"]').val();
      var isLocalLine = line == 'nettilukio' || line == 'nettipk' || line == 'lahilukio' && line == 'bandilinja';
      $('.section-underage').attr('data-skip', !isLocalLine || years >= 18);
    });
    
    // Custom validators
    
    Parsley.addValidator('birthdayFormat', {
      requirementType: 'string',
      validateString: function(value) {
        return value && moment(value, 'D.M.YYYY').isValid() && value.lastIndexOf('.') == value.length - 5;
      },
      messages: {
        fi: 'Päivämäärän muoto on virheellinen'
      }
    });

    Parsley.addValidator('ssnEndFormat', {
      requirementType: 'string',
      validateString: function(value) {
        return value && value.length == 4 && /^[a-zA-Z0-9]{4}/.test(value);
      },
      messages: {
        fi: 'Henkilötunnuksen loppuosan muoto on virheellinen'
      }
    });
    
    Parsley.addValidator('requiredIfShown', {
      requirementType: 'string',
      validateString: function(value, requirement, event) {
        var element = event.element;
        if ($(element).is(':visible')) { 
          if (!value || value.trim().length == 0) {
            return false;
          }
        }
        return true;
      },
      messages: {
        fi: 'Tämä kenttä on pakollinen'
      }
    });
    
    // Dependencies
    
    $('[data-dependencies]').change(function() {
      var name = $(this).attr('name');
      var value = $(this).is(':checkbox') ? $(this).is(':checked') ? $(this).val() : '' : $(this).val();
      $('.field-container[data-dependent-field="' + name + '"]').each(function() {
        var show = false;
        var values = $(this).attr('data-dependent-values').split(',');
        for (var i = 0; i < values.length; i++) {
          show = values[i] == value;
          if (show) {
            break;
          }
        }
        $(this).toggle(show);
      });
    });
    
    // Form navigation

    $('.button-previous-section').click(function() {
      var newIndex = currentIndex() - 1;
      while ($(applicationSections[newIndex]).attr('data-skip') == 'true') {
        newIndex--;
      }
      navigateTo($(applicationSections[newIndex]));
    });

    $('.button-next-section').click(function() {
      // TODO enable section validation
      //if ($('.application-form').parsley().validate({group: 'block-' + currentIndex()})) {
        var newIndex = currentIndex() + 1;  
        while ($(applicationSections[newIndex]).attr('data-skip') == 'true') {
          newIndex++;
        }
        navigateTo($(applicationSections[newIndex]));
      //}
    });

    // TODO Temporary validation hack
    $('.application-logo-header').click(function() {
      $('.application-form').parsley().validate({group: 'block-' + currentIndex()});
    });

    $('.button-save-application').click(function() {
      // TODO Disable UI, show saving message 
      var data = JSON.stringify($('.application-form').serializeObject());
      $.ajax({
        url: "/1/application/saveapplication",
        type: "POST",
        data: data, 
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(response) {
          $('#edit-info-last-name').text($('#field-last-name').val());
          $('#edit-info-reference-code').text(response.referenceCode);
          $('#edit-info-email').text($('#field-email').val());
          navigateTo('.section-done');
        },
        error: function() {
          // TODO Navigate to section-error (implement)
          console.log('error');
        }
      });
    });
    
    $('.button-edit-application').click(function() {
      if ($('.application-form').parsley().validate()) {
        var data = JSON.stringify($('.application-form').serializeObject());
        $.ajax({
          url: "/1/application/getapplicationid",
          type: "POST",
          data: data, 
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          success: function(response) {
            window.location.replace(window.location.href + '?applicationId=' + response.applicationId);
          },
          error: function() {
            // TODO Navigate to section-error (implement)
            console.log('error');
          }
        });
      }
    });

    $(applicationSections).each(function(index, section) {
      $(section).find(':input').attr('data-parsley-group', 'block-' + index);
    });
    navigateTo($(applicationSections[0]));

    // Previously stored data
    
    var existingApplication = $('#field-application-id').attr('data-preload');
    if (existingApplication) {
      preloadApplication($('#field-application-id').val());
    }
  });

  function navigateTo(section) {
    $('.form-section.current').removeClass('current');
    $(section).addClass('current');
    $('.form-navigation').toggle(!$(section).hasClass('section-done'));
    $('.button-previous-section').toggle(!$(section).hasClass('section-line'));
    $('.button-next-section').toggle(!$(section).hasClass('section-summary'));
    $('.button-save-application').toggle($(section).hasClass('section-summary'));
  }
  
  function currentIndex() {
    return $(applicationSections).index($(applicationSections).filter('.current')); 
  }

  function preloadApplication() {
    var applicationId = $('#field-application-id').val();
    $.ajax({
      url: "/1/application/getapplicationdata/" + applicationId,
      type: "GET",
      contentType: "application/json; charset=utf-8",
      success: function(result) {
        for (var key in result) {
          if (key.startsWith('field-attachments-')) {
            continue;
          }
          var formElement = $('[name="' + key + '"]');
          if (formElement.length) {
            if ($(formElement).is('select')) {
              $('option', $(formElement)).each(function() {
                if (this.value == result[key]) {
                  $(this).prop('selected', true);
                }
              });
            }
            else if ($(formElement).is('textarea')) {
              $(formElement).val(result[key]);
            }
            else {
              switch ($(formElement).attr("type")) {
              case 'text':
              case 'email':
              case 'hidden':
                $(formElement).val(result[key]);
                break;
              case 'radio':
              case 'checkbox':
                var values = Array.isArray(result[key]) ? result[key] : [result[key]];
                $(formElement).each(function() {
                  for (var i = 0; i < values.length; i++) {
                    if ($(this).attr('value') == values[i]) {
                      $(this).prop('checked', true);
                    }
                  }
                });
                break;
              }
            }
            $(formElement).trigger('change');
          }
        }
        preloadApplicationAttachments(result);
      }
    });
  }
  
  function preloadApplicationAttachments(result) {
    var applicationId = $('#field-application-id').val();
    var fileContainer = $('#field-attachments-files');
    var files = result['field-attachments-file'];
    if (files && files.length) {
      for (var i = 0; i < files.length; i++) {
        var fileElement = $('.application-file.template').clone();
        fileElement.removeClass('template');
        fileElement.find('.application-file-progress').remove();
        fileContainer.append(fileElement);
        fileElement.show();
        fileElement.find('.application-file-link').attr('href', '/1/application/getattachment/' + applicationId + '?attachment=' + files[i]);
        fileElement.find('.application-file-link').text(files[i]);
        $('.field-attachments-uploader').append($('<input>').attr({type: 'hidden', name: 'field-attachments-file', 'value': files[i]}));
      }
    }
  }
  
  function uploadAttachment(file) {
    var applicationId = $('#field-application-id').val();
    var fileContainer = $('.field-attachments-files'); 
    var formData = new FormData();
    formData.append('file', file);
    formData.append('name', file.name);
    formData.append('applicationId', applicationId);

    var fileElement = $('.application-file.template').clone();
    fileElement.removeClass('template');
    fileContainer.append(fileElement);
    fileElement.find('.application-file-name a').text(file.name);
    var fileProgressElement = fileElement.find('.application-file-progress').progressbar({
      value: 0
    });
    fileElement.show();

    $.ajax({
      url: '/1/application/createattachment',
      type: 'POST',
      processData: false,
      contentType: false,
      data: formData,
      xhr: function() {
        myXhr = $.ajaxSettings.xhr();
        if (myXhr.upload) {
          myXhr.upload.addEventListener('progress', function(evt) {
            if (evt.lengthComputable) {
              var percentComplete = (evt.loaded / evt.total) * 100;
              fileProgressElement.progressbar('value', percentComplete);
            }
          }, false);
        }
        return myXhr;
      },
      success: function(data) {
        fileElement.find('.application-file-link').attr('href', '/1/application/getattachment/' + applicationId + '?attachment=' + file.name);
        fileElement.find('.application-file-progress').remove();
        // TODO Delete file
        $('.field-attachments-uploader').append($('<input>').attr({type: 'hidden', name: 'field-attachments-file', 'value': file.name}));
      },
      error: function(err) {
        // TODO Show error message for file
        console.log('error sending attachment');
        fileElement.find('.application-file-progress').remove();
      }
    });
  }
  
}).call(this);