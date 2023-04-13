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
          o[this.name].push(this.value||'');
        }
        else {
          o[this.name] = this.value||'';
        }
      });
      return o;
    };
    
    var maxAttachmentSize = 52428800;
    $.ajax({
      url: "/1/applications/attachmentSizeLimit",
      type: "GET",
      contentType: "application/json; charset=utf-8",
      success: function(result) {
        maxAttachmentSize = result;
      }
    });
    
    // Attachments uploader
    
    $('#field-attachments').on('change', function() {
      var filesSize = 0;
      var files = $(this)[0].files;
      $('#field-attachments-files').find('.application-file').each(function() {
        filesSize += parseInt($(this).attr('data-file-size'));
      });
      for (var i = 0; i < files.length; i++) {
       filesSize += files[i].size;
      }
      if (filesSize > maxAttachmentSize) {
        var mb = Math.floor(maxAttachmentSize / 1048576);
        $('.notification-queue').notificationQueue('notification', 'error', 'Liitteiden suurin sallittu yhteiskoko on ' + mb + ' MB');
        return;
      }
      for (var i = 0; i < files.length; i++) {
        uploadAttachment(files[i]);
      }
      $(this).val('');
    });
    
    // Nickname
    
    $('#field-first-names').on('change', function() {
      setupNicknameSelector();
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
            if ($(field).val() == '' && $(field).attr('data-preselect') && result[i].text == $(field).attr('data-preselect')) {
              $(option).prop('selected', true);
            }
          }
        }
      });
    });
    
    // School autocomplete
    
    $.ajax({
      url: '/1/applications/schools',
      type: 'GET',
      contentType: 'application/json; charset=utf-8',
      success: function(result) {
        $('#school-selector').autocomplete({
          delay: 300,
          minLength: 3,
          source: function(request, response) {
            var matcher = new RegExp('^' + $.ui.autocomplete.escapeRegex(request.term), 'i');
            response($.grep(result, function(item) {
              return matcher.test(item);
            }));
          }
        });
      }
    });
    
    // Section checks
    
    $('#field-line').on('change', function() {
      setLine($(this).val());
    });
    $('#field-birthday').on('change', function() {
      var birthday = $(this).val();
      if (birthday) {
        var years = moment().diff(moment(birthday, "D.M.YYYY"), 'years');
        var line = $('select[name="field-line"]').val();
        var hasUnderageSupport = $('#field-line option:selected').attr('data-underage-support') == 'true';
        $('.section-underage').attr('data-skip', !hasUnderageSupport || years >= 18);
        if ($('#field-ssn-end').val()) {
          $('#field-ssn-end').parsley().validate();
        }
      }
      else {
        $('.section-underage').attr('data-skip', 'true');
      }
      var existingApplication = $('#field-application-id').attr('data-preload') == 'true';
      if (existingApplication) {
        $('.section-underage').toggle($('.section-underage').attr('data-skip') != 'true');
      }
      updateProgress();
    });
    
    // Custom validators
    
    Parsley.addValidator('dateFormat', {
      requirementType: 'string',
      validateString: function(value) {
        var dateRegExp = /^(\d{1,2})\.(\d{1,2})\.(\d{4})$/;
        return value && moment(value, 'D.M.YYYY').isValid() && value.match(dateRegExp) != null;
      },
      messages: {
        fi: 'Päivämäärän muoto on virheellinen'
      }
    });
    
    Parsley.addValidator('nickname', {
      requirementType: 'string',
      validateString: function(value) {
        return value != '';
      },
      messages: {
        fi: 'Klikkaa kutsumanimeäsi'
      }
    });
    
    Parsley.addValidator('ssnEndFormat', {
      requirementType: 'string',
      validateString: function(value) {
        return isValidSsnEnd(value, $('#field-line').val() == 'mk');
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
      validateMultiple: function(value, requirement, event) {
        var element = event.element;
        if ($(element).is(':visible')) { 
          if (value.length == 0) {
            return false;
          }
        }
        return true;
      },
      messages: {
        fi: 'Tämä kenttä on pakollinen'
      }
    });

    Parsley.addValidator('requiredEmailIfShown', {
      requirementType: 'string',
      validateString: function(value, requirement, event) {
        var element = event.element;
        if ($(element).is(':visible')) {
          var emailRegExp = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
          if (!value || value.trim().length == 0 || !value.match(emailRegExp)) {
            return false;
          }
        }
        return true;
      },
      validateMultiple: function(value, requirement, event) {
        var element = event.element;
        if ($(element).is(':visible')) { 
          if (value.length == 0) {
            return false;
          }
        }
        return true;
      },
      messages: {
        fi: 'Sähköpostiosoite puuttuu tai on virheellinen. Vain kirjaimet (a-z), numerot (0-9) ja pisteet (.) ovat sallittuja.'
      }
    });

    Parsley.addValidator('emailIfShown', {
      requirementType: 'string',
      validateString: function(value, requirement, event) {
        var element = event.element;
        if ($(element).is(':visible') && value) {
          var emailRegExp = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
          if (!value.match(emailRegExp)) {
            return false;
          }
        }
        return true;
      },
      messages: {
        fi: 'Sähköpostiosoite on virheellinen. Vain kirjaimet (a-z), numerot (0-9) ja pisteet (.) ovat sallittuja. Tämä kenttä ei kuitenkaan ole pakollinen.'
      }
    });

    Parsley.addValidator('emailMatch', {
      requirementType: 'string',
      validateString: function(value) {
        return value == $('#field-email').val();
      },
      messages: {
        fi: 'Sähköpostiosoitteet eivät täsmää'
      }
    });
    
    // Dependencies
    
    $('[data-dependencies]').change(function() {
      var name = $(this).attr('name');
      var srcVisible = $(this).is(':visible') || name == 'field-line';
      var value = [];
      if ($(this).is(':checkbox') || $(this).is(':radio')) {
        $('input[name="' + name + '"]:checked').each(function() {
          value.push($(this).val());
        });
      }
      else {
        value.push($(this).val());
      }
      $('[data-dependent-field="' + name + '"]').each(function() {
        var show = false;
        if (srcVisible) {
          var values = $(this).attr('data-dependent-values').split(',');
          for (var i = 0; i < values.length; i++) {
            show = $.inArray(values[i], value) > -1;
            if (show) {
              break;
            }
          }
        }
        $(this).toggle(show);
        // #1425: If the field is supposed to be visible, its ancestors should be visible as well
        // #1436: ...but only up to section level
        if (show) {
          $(this).parentsUntil('section').toggle(show);
        }
        // #1359: Disable hidden form fields to prevent their serialization when submitting
        $(this).find("input,select,textarea").prop('disabled', !show);
        $(this).find('[data-dependencies]').trigger('change');
      });
    });
    
    // Course fees

    $('.internetix-course-fees-link, .course-fees__close, .course-fees-overlay').on('click', function() {
      $('.course-fees-overlay').toggle();
      $('.course-fees').toggle();
    });
    
    // Study promises

    $('.nettilukio-promise-link, .nettilukio-promise__close, .nettilukio-promise-overlay').on('click', function() {
      $('.nettilukio-promise-overlay').toggle();
      $('.nettilukio-promise').toggle();
    });

    $('.nettilukio-contactpromise-link, .nettilukio-contactpromise__close, .nettilukio-contactpromise-overlay').on('click', function() {
      $('.nettilukio-contactpromise-overlay').toggle();
      $('.nettilukio-contactpromise').toggle();
    });

    $('.aineopiskelu-promise-link, .aineopiskelu-promise__close, .aineopiskelu-promise-overlay').on('click', function() {
      $('.aineopiskelu-promise-overlay').toggle();
      $('.aineopiskelu-promise').toggle();
    });

    $('.nettipk-promise-link, .nettipk-promise__close, .nettipk-promise-overlay').on('click', function() {
      $('.nettipk-promise-overlay').toggle();
      $('.nettipk-promise').toggle();
    });
    
    // Page information
    
    $('.application-header__show-content-information').on('click', function() {
      $('.application-content__information').fadeToggle();
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
      var valid = $('.application-form').parsley().validate({group: 'block-' + currentIndex()});
      if (valid) {
        var newIndex = currentIndex() + 1;  
        while ($(applicationSections[newIndex]).attr('data-skip') == 'true') {
          newIndex++;
        }
        navigateTo($(applicationSections[newIndex]));
      }
    });
    
    $('input[name="field-source"]').click(function() {
      var val = $('input[name="field-source"]:checked').val();
      $('#field-source-mandatory').toggle(!val);
    });

    $('.button-save-application').click(function() {
      // TODO Disable UI, show saving message 
      var valid = false;
      var existingApplication = $('#field-application-id').attr('data-preload') == 'true';
      if (existingApplication) {
        valid = $('.application-form').parsley().validate();
      }
      else {
        valid = $('.application-form').parsley().validate({group: 'block-' + currentIndex()});
      }
      if (valid) {
        var data = JSON.stringify($('.application-form').serializeObject());
        $.ajax({
          url: $('#application-form').attr('data-save-url'),
          type: "POST",
          data: data, 
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          success: function(response) {
            if ($('#application-form').attr('data-done-page') == 'true') {
              if (response.autoRegistered == 'true') {
                navigateTo('.section-done.registered');
              }
              else if ($('#field-line').val() == 'aineopiskelu') {
                navigateTo('.section-done.internetix-submitted');
              }
              else {
                $('#edit-info-last-name').text($('#field-last-name').val());
                $('#edit-info-reference-code').text(response.referenceCode);
                $('#edit-info-email').text($('#field-email').val());
                navigateTo('.section-done.submitted');
              }
            }
            else if (response.redirectURL) {
              window.location.href = response.redirectURL;
            }
            else {
              $('.notification-queue').notificationQueue('notification', 'info', 'Hakemus tallennettu');
            }
          },
          error: function(err) {
            if (err.status == 409) {
              $('.notification-queue').notificationQueue('notification', 'error',
                  'Annetulla sähköpostiosoitteella on jo jätetty hakemus');
            }
            else {
              $('.notification-queue').notificationQueue('notification', 'error', 'Virhe tallennettaessa hakemusta: ' + err.statusText);
            }
          }
        });
      }
    });
    
    $('.button-edit-application').click(function() {
      if ($('.application-form').parsley().validate()) {
        var data = JSON.stringify($('.application-form').serializeObject());
        $.ajax({
          url: "/1/applications/getapplicationid",
          type: "POST",
          data: data, 
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          success: function(response) {
            window.location.replace(window.location.href + '?applicationId=' + response.applicationId);
          },
          error: function(err) {
            window.location.replace(window.location.href + '?applicationId=404');
          }
        });
      }
    });

    $(applicationSections).each(function(index, section) {
      $(section).find(':input').each(function(idx, input) {
        $(input).attr('data-parsley-group', 'block-' + index);
        // #545: Validate fields as soon as they lose focus
        $(input).on('focusout', function() {
          $(input).parsley().validate();
        });
      });
    });
    // #764: Preselected line
    if ($('#field-line').attr('data-preselect')) {
      $('#field-line').val($('#field-line').attr('data-preselect'));
      $('.section-line').attr('data-skip', true);
    }
    $('#field-line').trigger('change');
    navigateTo($(applicationSections).get(firstIndex()));

    // Previously stored data
    
    var existingApplication = $('#field-application-id').attr('data-preload') == 'true';
    if (existingApplication) {
      $('#application-page-indicator').hide();
      $('#button-previous-section').hide();
      $('#button-next-section').hide();
      $('#button-save-application').show();
      $('.form-navigation').show();
      preloadApplication($('#field-application-id').val());
    }
    else {
      setupNicknameSelector();
    }
  });

  function setLine(line) {
    var option =  $('#field-line').find('option:selected');
    var hasAttachmentSupport = $(option).attr('data-attachment-support') == 'true' || $('body').attr('data-mode') == 'edit';
    $('.section-attachments').attr('data-skip', !hasAttachmentSupport);
    $('.section-internetix-school').attr('data-skip', option.val() != 'aineopiskelu');
    // section toggle for existing applications
    var existingApplication = $('#field-application-id').attr('data-preload') == 'true';
    if (existingApplication) {
      $('.section-attachments').toggle(hasAttachmentSupport);
      $('.section-internetix-school').toggle(line == 'aineopiskelu');
    }
    // age check when line changes 
    $('#field-birthday').trigger('change');
    // semi-required ssn postfix
    if (line == 'mk') {
      $('label[for="field-ssn-end"]').removeClass('required');
    }
    else {
      $('label[for="field-ssn-end"]').addClass('required');
    }
    // update page count
    updateProgress();
    // #774: selected study program
    if ($('#field-application-id').attr('data-preload') != 'true') {
      $('.form-section__header').removeClass().addClass('form-section__header form-section__header--' + option.text());
    }
  };
  
  function navigateTo(section) {
    $('.form-section').hide();
    $('.form-section.current').removeClass('current');
    $(section).addClass('current').show();
    $('.form-navigation').toggle(!$(section).hasClass('section-done'));
    $('.application-content__information-page-specific').toggle(!$(section).hasClass('section-done'));
    $('.application-content__information-page-specific-non-summary').toggle(!$(section).hasClass('section-summary'));
    $('.application-content__information-page-specific-summary').toggle($(section).hasClass('section-summary'));
    // toggle previous section button
    var canNavigate = false;
    for (var i = currentIndex() - 1; i >= 0; i--) {
      if (!$(applicationSections[i]).attr('data-skip')) {
        canNavigate = true;
        break;
      }
    }
    $('.button-previous-section').toggle(canNavigate);
    // toggle next section button
    canNavigate = false;
    for (var i = currentIndex() + 1; i < applicationSections.length; i++) {
      if (!$(applicationSections[i]).attr('data-skip')) {
        canNavigate = true;
        break;
      }
    }
    $('.button-next-section').toggle(canNavigate);
    $('.button-save-application').toggle($(section).hasClass('section-summary'));
    if ($(section).hasClass('section-summary')) {
      $('#summary-name').text($('#field-first-names').val() + ' ' + $('#field-last-name').val());
      $('#summary-phone').text($('#field-phone').val());
      $('#summary-email').text($('#field-email').val());
    }
    updateProgress();
  }
  
  function isValidSsnEnd(value, allowEmpty) {
    if (!value || value == '') {
      return allowEmpty;
    }
    else if (value.toUpperCase() == 'XXXX') {
      return true;
    }
    var valid = value != '' && value.length == 4 && /^[0-9]{3}[a-zA-Z0-9]{1}/.test(value);
    if (valid) {
      valid = false;
      var num = $('#field-birthday').val();
      if (num) {
        num = moment(num, "D.M.YYYY").format('DDMMYY') + value.substring(0, 3);
        if (!isNaN(num)) {
          var checksumChars = '0123456789ABCDEFHJKLMNPRSTUVWXY';
          valid = checksumChars[num % 31] == value.substring(3, 4).toUpperCase();
        }
      }
    }
    return valid;
  }
  
  function updateProgress() {
    $('.application-page-indicator').text(currentPage() + ' / ' + totalPages());    
  }
  
  function currentPage() {
    var page = 0;
    for (var i = 0; i < $(applicationSections).size(); i++) {
      var section = $(applicationSections).get(i);
      if ($(section).attr('data-skip') == 'true') {
        continue;
      }
      page++;
      if ($(section).hasClass('current')) {
        break;
      }
    }
    return page;
  }
  
  function totalPages() {
    var pages = 0;
    for (var i = 0; i < $(applicationSections).size(); i++) {
      var section = $(applicationSections).get(i);
      if ($(section).attr('data-skip') == 'true') {
        continue;
      }
      pages++;
    }
    return pages;
  }

  function currentIndex() {
    return $(applicationSections).index($(applicationSections).filter('.current')); 
  }
  
  function firstIndex() {
    for (var i = 0; i < applicationSections.length; i++) {
      if (!$(applicationSections[i]).attr('data-skip')) {
        return i;
      }
    }
    return 0;
  }

  function preloadApplication() {
    var applicationId = $('#field-application-id').val();
    $.ajax({
      url: "/1/applications/getapplicationdata/" + applicationId,
      type: "GET",
      contentType: "application/json; charset=utf-8",
      success: function(result) {
        $('.form-section').each(function() {
          $(this).toggle($(this).attr('data-skip') != 'true' && !$(this).hasClass('section-summary'));
        });
        for (var key in result) {
          if (key.startsWith('field-attachments-')) {
            continue;
          }
          var formElement = $('[name="' + key + '"]');
          if (formElement.length) {
            if ($(formElement).is('select')) {
              $(formElement).val(result[key]);
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
        setupNicknameSelector();
      }
    });
  }
  
  function preloadApplicationAttachments(result) {
    var applicationId = $('#field-application-id').val();
    var fileContainer = $('#field-attachments-files');
    $.ajax({
      url: '/1/applications/listattachments/' + applicationId,
      type: 'GET',
      contentType: "application/json; charset=utf-8",
      success: function(files) {
        for (var i = 0; i < files.length; i++) {
          createAttachmentFormElement(files[i].name, files[i].description, files[i].size);
        }
      },
      error: function(err) {
        $('.notification-queue').notificationQueue('notification', 'error', 'Virhe ladattaessa liitteitä: ' + err.statusText);
      }
    });
  }
  
  function createAttachmentFormElement(name, description, size) {
    var applicationId = $('#field-application-id').val();
    var fileContainer = $('#field-attachments-files');
    var fileElement = $('.application-file.template').clone();
    fileElement.attr('data-file-size', size);
    fileElement.removeClass('template');
    fileContainer.append(fileElement);
    var nameElement = $('<input>').attr('type', 'hidden');
    nameElement.attr('name', 'attachment-name');
    nameElement.attr('value', name);
    var descriptionElement = $('<input>').attr('type', 'text');
    descriptionElement.addClass('attachment-description');
    descriptionElement.attr('name', 'attachment-description');
    descriptionElement.attr('placeholder', 'Kuvaus');
    descriptionElement.attr('value', description);
    fileElement.find('.application-file__description').append(nameElement);
    fileElement.find('.application-file__description').append(descriptionElement);
    fileElement.find('.application-file__link').attr('href', '/1/applications/getattachment/' + applicationId + '?attachment=' + name);
    fileElement.find('.application-file__size').text((size / 1024 + 1).toFixed(0) + ' KB');
    fileElement.find('.application-file__link').text(name);
    fileElement.find('.application-file__delete').on('click', function() {
      $.ajax({
        url: '/1/applications/removeattachment/' + applicationId + '?attachment=' + name,
        type: 'DELETE',
        success: function(data) {
          fileElement.remove();
        },
        error: function(err) {
          fileElement.remove();
        }
      });
    });
    fileElement.show();
  }
  
  function setupNicknameSelector() {
    if ($('#field-first-names').length > 0) {
      var currentVal = $('#field-nickname').val();
      var firstNames = $('#field-first-names').val().trim();
      var names = firstNames == '' ? [] : firstNames.split(/\ +/);
      if (names.length == 0) {
        $('div.form-section__field-container.field-nickname').hide();
      }
      else {
        $('div.form-section__field-container.field-nickname').show();
      }
      var nicknamesContainer = $('div.nicknames-container');
      $(nicknamesContainer).empty();
      var nameFound = false;
      for (var i = 0; i < names.length; i++) {
        var nicknameElement = $('<span>').addClass('nickname').text(names[i]);
        if (currentVal == names[i]) {
          nameFound = true;
          nicknameElement.addClass('selected');
        }
        $(nicknamesContainer).append(nicknameElement);
        nicknameElement.on('click', function() {
          $('#field-nickname').val($(this).text());
          $('span.nickname').removeClass('selected');
          $(this).addClass('selected');
          $('#field-nickname').parsley().validate();
        });
      }
      if (!nameFound) {
        if (names.length == 0) {
          $('#field-nickname').val('');
        }
        else {
          $('#field-nickname').val(names[0]);
          $('div.nicknames-container span:first').addClass('selected');
        }
      }
    }
  }
  
  function uploadAttachment(file) {
    var applicationId = $('#field-application-id').val();
    var fileContainer = $('.field-attachments__files'); 
    var fileName = decodeURIComponent(file.name);
    var formData = new FormData();
    formData.append('file', file);
    formData.append('name', fileName);
    formData.append('applicationId', applicationId);
    
    var progressElement = $('.application-file-upload-progress.template').clone();
    progressElement.removeClass('template');
    progressElement.find('.application-file-upload-progress-text').text('Lähetetään tiedostoa ' + fileName);
    var progressBarElement = progressElement.find('.application-file-upload-progress-bar');
    progressBarElement.progressbar({
      value: 0
    });
    $('#field-attachments-files').append(progressElement);
    progressElement.show();

    $.ajax({
      url: '/1/applications/createattachment',
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
              progressBarElement.progressbar('value', percentComplete);
            }
          }, false);
        }
        return myXhr;
      },
      success: function(data) {
        progressElement.remove();
        createAttachmentFormElement(fileName, '', file.size);
      },
      error: function(err) {
        if (err.status == 409) {
          $('.notification-queue').notificationQueue('notification', 'warn', 'Hakemuksessa on jo samanniminen liite');
        }
        else {
          $('.notification-queue').notificationQueue('notification', 'error', 'Virhe tallennettaessa liitettä: ' + err.statusText);
        }
        progressElement.remove();
      }
    });
  }
  
}).call(this);