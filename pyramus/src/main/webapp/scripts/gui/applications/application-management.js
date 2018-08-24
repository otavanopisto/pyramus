(function() {

  $(document).ready(function() {
    
    // Actions
    
    refreshActions();
    
    // Contact log entries
    
    loadLogEntries();
    
    // Attachments
    
    var attachmentsContainer = $('#attachments-readonly-container');
    if (attachmentsContainer.length) {
      var applicationId = $('body').attr('data-application-id');
      $.ajax({
        url: '/1/applications/listattachments/' + applicationId,
        type: 'GET',
        contentType: "application/json; charset=utf-8",
        success: function(files) {
          $('#attachments-title').toggle(files.length > 0);
          for (var i = 0; i < files.length; i++) {
            attachmentsContainer.append($('<div>').addClass('application-attachment')
              .append(
                $('<span>').addClass('icon-attachment'))
              .append(
                $('<a>')
                  .attr('href', '/1/applications/getattachment/' + applicationId + '?attachment=' + files[i].name)
                  .attr('target', '_blank')
                  .addClass('attachment-link')
                  .text(files[i].description||files[i].name)));
          }
        },
        error: function(err) {
          $('.notification-queue').notificationQueue('notification', 'error', 'Virhe ladattaessa liitteitä: ' + err.statusText);
        }
      });
    }
    
    // Document urls
    
    updateDocumentUrls();
    
    // Header buttons
    
    $('#action-application-back-pyramus').on('click', function() {
      window.location.href = '/applications/browse.page';
    });
    
    $('#action-application-view').on('click', function() {
      window.location.href = '/applications/view.page?application=' + $('body').attr('data-application-entity-id');
    });

    $('#action-application-edit').on('click', function() {
      window.location.href = '/applications/manage.page?application=' + $('body').attr('data-application-entity-id');
    });

    $('#action-application-toggle-lock').on('click', function() {
      var _this = this;
      var applicantEditable = $(this).hasClass('icon-locked');
      var applicationEntityId = $('body').attr('data-application-entity-id');
      $.ajax({
        url: '/applications/toggleapplicanteditable.json',
        type: 'POST',
        data: {
          id: applicationEntityId,
          applicantEditable: applicantEditable
        },
        dataType: "json",
        success: function(response) {
          if (applicantEditable) {
            $(_this).removeClass('icon-locked').addClass('icon-unlocked');
          }
          else {
            $(_this).removeClass('icon-unlocked').addClass('icon-locked');
          }
        }
      });
    });
    
    $('#action-application-logs').on('click', function() {
      $('section.application-logs').slideToggle();
    });
    
    $('#applications-tab-mail').on('click', function() {
      $('section.application-mail').fadeIn( 100 );
      $('#applications-tab-mail').addClass('active');
      $('#applications-tab-comment').removeClass('active');
      
      if ($('section.application-mail').is(':visible')) {
        $('section.application-comment').hide();
      }
      
    });
    
    $('#applications-tab-comment').on('click', function() {
      $('section.application-comment').fadeIn( 100 );
      $('#applications-tab-comment').addClass('active');
      $('#applications-tab-mail').removeClass('active');
      
      if ($('section.application-comment').is(':visible')) {
        $('section.application-mail').hide();
      }
      
    });
    
    // Save log entry
    
    $('#log-form-save').on('click', function() {
      var data = JSON.stringify($('#log-form').serializeObject());
      $.ajax({
        url: '/applications/savelogentry.json',
        type: "POST",
        data: data, 
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(response) {
          $('#log-form-text').val('');
          var logElement = createLogElement(response);
          $('.log-entries-container').prepend(logElement);
          logElement.show();
        },
        error: function(err) {
          $('.notification-queue').notificationQueue('notification', 'error', 'Virhe tallennettaessa merkintää: ' + err.statusText);
        }
      });
    });
    
    // Existing students (unless student already created)
    
    var currentState = $('#info-application-state-value').attr('data-state');
    if (currentState != 'TRANSFERRED_AS_STUDENT' && currentState != 'REGISTERED_AS_STUDENT') {
      $.ajax({
        url: '/applications/listexistingpersons.json',
        type: "GET",
        data: {
          applicationEntityId: $('body').attr('data-application-entity-id')
        },
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(response) {
          if (response.persons.length == 0) {
            $('div.user-exists-container').hide();
          }
          else {
            for (var i = 0; i < response.persons.length; i++) {
              var personElement = $('<span>').addClass('user-exists-user-link-container').appendTo($('div.user-exists-description-actions'));
              var hrefElement = $('<a>').appendTo(personElement);
              hrefElement.attr('href', '/students/viewstudent.page?person=' + response.persons[i].id);
              hrefElement.attr('target', '_blank');
              hrefElement.text(response.persons[i].name);
            }
            $('div.user-exists-container').show();
          }
        }
      });
    }
    
    // Helper functions
    
    function loadLogEntries() {
      $('.log-entries-container').empty();
      $.ajax({
        url: '/applications/listlogentries.json',
        type: "GET",
        data: {
          applicationId: $('body').attr('data-application-id') 
        },
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(response) {
          var logEntries = response.logEntries;
          if (logEntries) {
            for (var i = 0; i < logEntries.length; i++) {
              var logElement = createLogElement(logEntries[i]);
              $('.log-entries-container').append(logElement);
              logElement.show();
            }
          }
        },
        error: function(err) {
          $('.notification-queue').notificationQueue('notification', 'error', 'Virhe ladattaessa merkintöjä: ' + err.statusText);
        }
      });
    }
    
    function createLogElement(entry) {
      var logElement = $('.log-entry.template').clone();
      logElement.removeClass('template');
      logElement.attr('data-applicationlog-id', entry.id);
      logElement.attr('data-owner', entry.owner);
      logElement.find('.log-entry-text').html(entry.type == 'HTML' ? entry.text : entry.text.replace(/\n/g,"<br/>"));
      logElement.find('.log-entry-author').text(entry.user||'Automaattinen tapahtuma');
      logElement.find('.log-entry-date').text(moment(entry.date).format('D.M.YYYY h:mm'));
      if (entry.owner === true) {
        // Edit log entry
        logElement.find('.log-entry-edit').on('click', function() {
          var id = logElement.attr('data-applicationlog-id');
          $.ajax({
            url: '/applications/getlogentry.json',
            type: "GET",
            data: {
              id: id
            },
            dataType: 'json',
            success: function(entry) {
              var editor = $('<textarea>').attr('rows', 5);
              var saveButton = $('<button>').addClass('button-save-logentry').attr('type', 'button').text('Tallenna');
              var cancelButton = $('<button>').addClass('button-cancel-logentry').attr('type', 'button').text('Peruuta'); 
              var textContainer = logElement.find('.log-entry-text');
              editor.text(entry.text);
              textContainer.empty().append($('<div>').addClass('field-container').append(editor));
              textContainer.append($('<div>').addClass('field-button-set').append(saveButton).append(cancelButton));
              saveButton.on('click', function() {
                $.ajax({
                  url: '/applications/updatelogentry.json',
                  type: "POST",
                  data: {
                    id: id,
                    text: editor.val()
                  },
                  dataType: 'json',
                  success: function(entry) {
                    textContainer.empty().html(entry.type == 'HTML' ? entry.text : entry.text.replace(/\n/g,"<br/>"));
                    logElement.find('.log-entry-date').text(moment(entry.date).format('D.M.YYYY h:mm'));
                  }
                });
              });
              cancelButton.on('click', function() {
                textContainer.empty().html(entry.type == 'HTML' ? entry.text : entry.text.replace(/\n/g,"<br/>"));
              });
            }
          });
        });
        // Remove log entry
        logElement.find('.log-entry-archive').on('click', $.proxy(function() {
          var logElement = this;
          var id = this.attr('data-applicationlog-id');
          var dialog = $('#delete-log-entry-dialog');
          $(dialog).dialog({
            resizable: false,
            height: "auto",
            width: 'auto',
            modal: true,
            position: {
              my: 'center',
              at: 'center',
              of: window
            },
            buttons: [{
              text: "Poista",
              class: 'remove-button',
              click: function() {
	              $.ajax({
	                url: '/applications/archivelogentry.json',
	                type: "POST",
	                data: {
	                  id: id 
	                },
                    dataType: 'json',
	                success: $.proxy(function(response) {
	                  logElement.remove();
	                }, this),
	                error: function(err) {
	                  $('.notification-queue').notificationQueue('notification', 'error', 'Virhe poistaessa merkintää: ' + err.statusText);
	                }
	              });
	              $(dialog).dialog("close");
	            }
            }, {
              text: "Peruuta",
              class: 'cancel-button',
              click: function() {
  	            $(dialog).dialog("close");
  	          }
            }]
          });
        }, logElement));
      }
      else {
        logElement.find('.log-entry-actions').hide();
      }
      return logElement;
    }
    
    // Mail
    
    CKEDITOR.replace('mail-form-content', {
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
    $.ajax({
      url: '/applications/listmailtemplates.json',
      type: 'GET',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      success: function(response) {
        var selectField = $('<select>');
        $('#application-mail-templates').append(selectField);
        for (var i = 0; i < response.mailTemplates.length; i++) {
          var optionField = $('<option>');
          $(optionField).attr('value', response.mailTemplates[i].id);
          $(optionField).attr('data-line', response.mailTemplates[i].lineInternal);
          $(optionField).attr('data-owner', response.mailTemplates[i].owner);
          $(optionField).text(response.mailTemplates[i].name);
          $(selectField).append(optionField);
        }
      }
    });
    $('#mail-templates-filter-line,#mail-templates-filter-owner').on('click', function() {
      var selectField = $('#application-mail-templates').find('select'); 
      $(selectField).val('');
      $(selectField).find('option[data-line]').each(function() {
        $(this).removeAttr('hidden');
        if ($('#mail-templates-filter-line').prop('checked') && $('#field-line').val() != $(this).attr('data-line')) {
          $(this).attr('hidden', 'hidden');
        }
        if ($('#mail-templates-filter-owner').prop('checked') && !$(this).attr('data-owner')) {
          $(this).attr('hidden', 'hidden');
        }
        if (!$(this).attr('hidden') && !$(selectField).val()) {
          $(selectField).val($(this).attr('value'));
        }
      });
    });
    $('#application-mail-template-apply').on('click', function() {
      var selectField = $('#application-mail-templates').find('select'); 
      var templateId = $(selectField).val();
      if (templateId) {
        $.ajax({
          url: '/applications/getmailtemplate.json',
          type: 'GET',
          data: {
            id: templateId
          },
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          success: function(response) {
            if (response.subject) {
              $('#mail-form-subject').val(response.subject);
            }
            CKEDITOR.instances['mail-form-content'].insertHtml(response.content);
          }
        });
      }
    });
    $.ajax({
      url: '/applications/listmailrecipients.json',
      type: 'GET',
      data: {
        applicationEntityId: $('body').attr('data-application-entity-id')
      },
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      success: function(response) {
        for (var i = 0; i < response.recipients.length; i++) {
          var row = $('<div>').addClass('application-mail-recipient');
          var rowInput = $('<input>').attr({
            'id': 'mail-form-recipient-' + i,
            'type': 'checkbox',
            'name': 'mail-form-recipient-' + response.recipients[i].type,
            'value': response.recipients[i].mail});
          if (response.recipients[i].type == 'to') {
            $(rowInput).attr('checked', 'checked');
          }
          var rowLabel = $('<label>')
            .attr('for', 'mail-form-recipient-' + i)
            .text(response.recipients[i].name + ' <' + response.recipients[i].mail + '>');
          $(row).append(rowInput).append(rowLabel);
          $('div.application-mail-recipients').append(row);
        }
      }
    });
    $('#mail-form-send').on('click', function() {
      var sendButton = this;
      if (!$(sendButton).hasClass('loading')) {
        $(sendButton).addClass('loading');
        CKEDITOR.instances['mail-form-content'].updateElement();
        var data = JSON.stringify($('#mail-form').serializeObject());
        $.ajax({
          url: '/applications/sendmail.json',
          type: "POST",
          data: data, 
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          success: function(response) {
            $(sendButton).removeClass('loading');
            loadLogEntries();
            $('.notification-queue').notificationQueue('notification', 'info', 'Viesti lähetetty');
          },
          error: function(err) {
            $('.notification-queue').notificationQueue('notification', 'error', 'Virhe lähetettäessä postia: ' + err.statusText);
            $(sendButton).removeClass('loading');
          }
        });
      }
    });
    
    // Signatures
    
    var docId = $('.sign-button').attr('data-document-id');
    var docState = $('.sign-button').attr('data-document-state');
    $('.sign-button').on('click', function(event) {
      event.stopPropagation();
      processingOn();
      $.ajax({
        url: '/applications/generateacceptancedocument.json',
        type: 'GET',
        data: {
          id: $('body').attr('data-application-entity-id')
        },
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(response) {
          if (response.status == 'OK') {
            updateDocumentUrls();
            showSignatures();
          }
          else {
            $('.notification-queue').notificationQueue('notification', 'error', response.reason);
          }
          processingOff();
        },
        error: function(err) {
          $('.notification-queue').notificationQueue('notification', 'error', err.statusText);
          processingOff();
        }
      });
    });
    
    // Application handling options
    
    $('.application-handling-option').on('click', function(event) {
      var state = $(this).attr('data-state');
      if (!state) {
        return; // handlling option won't change state, ignore
      }
      if (state == 'ARCHIVE') {
        archiveApplication();
        return;
      }
      var id = $('body').attr('data-application-entity-id');
      processingOn();
      $.ajax({
        url: '/applications/updateapplicationstate.json',
        type: "POST",
        data: {
          id: id,
          state: state,
          lockApplication: state == 'PROCESSING',
          setHandler: !$('#info-application-handler-value').attr('data-handler-id'),
          removeHandler: state == 'PENDING'
        },
        dataType: 'json',
        success: function(response) {
          if (response.status == 'OK') {
            window.location.reload();
          }
          else {
            $('.notification-queue').notificationQueue('notification', 'error', response.reason);
          }
          processingOff();
        },
        error: function(err) {
          $('.notification-queue').notificationQueue('notification', 'error', err.statusText);
          processingOff();
        }
      });
    });
    $('.application-handling-container').show();
    
    function refreshActions() {
      var applicationLine = $('#field-line').val();
      var currentState = $('#info-application-state-value').attr('data-state');
      $('div.application-handling-option').each(function() {
        // see if option is valid for the application line 
        var line = $(this).attr('data-line');
        var available = !line || applicationLine == line || (line.startsWith('!') && applicationLine != line.substring(1));
        // see if option is valid for the application state
        if (available) {
          var optionState = $(this).attr('data-state');
          if (optionState == currentState) {
            available = false;
          }
          else {
            var applicableStates = [];
            if ($(this).attr('data-show')) {
              applicableStates = $(this).attr('data-show').split(',');
              available = $.inArray(currentState, applicableStates) >= 0;
            }
          }
        }
        $(this).toggle(available);
      });
      $('.sign-button').toggle(currentState == 'WAITING_STAFF_SIGNATURE');
    }
    
    function archiveApplication() {
      var dialog = $('#delete-application-dialog');
      $(dialog).dialog({
        resizable: false,
        height: 'auto',
        width: 'auto',
        modal: true,
        position: {
          my: 'center',
          at: 'center',
          of: window
        },
        buttons: [{
          text: 'Poista',
          class: 'remove-button',
          click: function() {
            $.ajax({
              url: '/applications/archiveapplication.json',
              type: "POST",
              data: {
                id: $('body').attr('data-application-entity-id') 
              },
              dataType: 'json',
              success: function(response) {
                window.location.href = '/applications/browse.page';
              }
            });
            $(dialog).dialog("close");
          }
        }, {
          text: "Peruuta",
          class: 'cancel-button',
          click: function() {
            $(dialog).dialog("close");
          }
        }]
      });
    }
    
    function updateDocumentUrls() {
      $.getJSON('/applications/getdocumenturls.json', {
        id: $('body').attr('data-application-entity-id')
      }, function(response) {
        var html = '-';
        if (response.staffDocumentUrl) {
          html = '<a href="' + response.staffDocumentUrl + '" target="_blank">Oppilaitos</a>';
        }
        if (response.applicantDocumentUrl) {
          html += '<br/><a href="' + response.applicantDocumentUrl + '" target="_blank">Hakija</a>';
        }
        $('#info-application-documents-value').html(html);
      });
    }

    function processingOn() {
      $('body')
        .append($('<div>')
          .addClass('processing-overlay'));
    }

    function processingOff() {
      $('.processing-overlay').remove();
    }

    function showSignatures() {
      if ($('#signatures-dialog').find('.auth-source').length == 0) {
        $.getJSON('/applications/listsignaturesources.json', function(data) {
          if (data.sources) {
            $.each(data.sources.methods, function(index, method) {
              $('#signatures-dialog').append(
                  $('<img>')
                  .addClass('auth-source')
                  .attr('src', method.image)
                  .attr('data-identifier', method.identifier)
                  .attr('title', method.name)
                  .on('click', function(event) {
                    event.stopPropagation();
                    sign($(this).attr('data-identifier'));
                  })
              );
            });
            openSignaturesDialog();
          }
          else {
            $('.notification-queue').notificationQueue('notification', 'error', 'Tunnistuslähteiden lataaminen epäonnistui');
          }
        });
      }
      else {
        openSignaturesDialog();
      }
    }
    
    function openSignaturesDialog() {
      var dialog = $('#signatures-dialog');
      $(dialog).dialog({
        resizable: false,
        height: 'auto',
        minHeight: 350,
        width: 'auto',
        modal: true,
        position: {
          my: 'center',
          at: 'center',
          of: window
        },
        buttons: [{
          text: "Peruuta",
          class: 'cancel-button',
          click: function() {
            $(dialog).dialog("close");
          }
        }]
      });
    }
    
    function sign(authService) {
      $.ajax({
        url: '/applications/signacceptancedocument.json',
        type: 'GET',
        data: {
          id: $('body').attr('data-application-entity-id'),
          mode: $('body').attr('data-mode'),
          authService: authService
        },
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(response) {
          if (response.status == 'OK') {
            window.open(response.completionUrl, "_self");
          }
          else {
            $('.notification-queue').notificationQueue('notification', 'error', response.reason);
          }
        },
        error: function(err) {
          $('.notification-queue').notificationQueue('notification', 'error', err.statusText);
        }
      });
    }
  });
  
}).call(this);