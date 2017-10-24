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
          for (var i = 0; i < files.length; i++) {
            attachmentsContainer.append($('<div>').append(
              $('<a>')
                .attr('href', '/1/applications/getattachment/' + applicationId + '?attachment=' + files[i].name)
                .attr('target', '_blank')
                .text(files[i].name)));
          }
        },
        error: function(err) {
          $('.notification-queue').notificationQueue('notification', 'error', 'Virhe ladattaessa liitteitä: ' + err.statusText);
        }
      });
    }
    
    // Header buttons
    
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
    
    $('#action-application-log').on('click', function() {
      $('section.application-logs').toggle();
      if ($('section.application-logs').is(':visible')) {
        $('section.application-mail').hide();
      }
    });
    $('#action-application-mail').on('click', function() {
      $('section.application-mail').toggle();
      if ($('section.application-mail').is(':visible')) {
        $('section.application-logs').hide();
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
    
    // Existing students
    
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
      logElement.find('.log-entry-author').text(entry.user);
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
      url: '/applications/listmailrecipients.json',
      type: "GET",
      data: {
        applicationEntityId: $('body').attr('data-application-entity-id')
      },
      dataType: "json",
      contentType: "application/json; charset=utf-8",
      success: function(response) {
        for (var i = 0; i < response.recipients.length; i++) {
          var row = $('<div>').addClass('field-row-flex');
          var rowInputElement = $('<div>').addClass('field-row-element');
          var rowInput = $('<input>').attr({
            'id': 'mail-form-recipient-' + i,
            'type': 'checkbox',
            'name': 'mail-form-recipient-' + response.recipients[i].type,
            'value': response.recipients[i].mail});
          if (response.recipients[i].type == 'to') {
            $(rowInput).attr('checked', 'checked');
          }
          var rowLabelElement = $('<div>').addClass('field-row-label');
          var rowLabel = $('<label>')
            .attr('for', 'mail-form-recipient-' + i)
            .text(response.recipients[i].name + ' <' + response.recipients[i].mail + '>');
          $(row).append(rowInputElement).append(rowLabelElement);
          $(rowInputElement).append(rowInput);
          $(rowLabelElement).append(rowLabel);
          $('div.mail-form-recipients').append(row);
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
    
    // Actions
    
    $('.application-handling-option').on('click', function(event) {
      $('.application-handling-options-container').hide();
      var id = $('body').attr('data-application-entity-id');
      var state = $(event.target).attr('data-state');
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
          $('#info-application-handler-value').text(response.handler||'-');
          $('#info-application-handler-value').attr('data-handler-id', response.handlerId);
          $('#info-application-last-modified-value').text(moment(response.lastModified).format('D.M.YYYY h:mm'));
          $('#info-application-state-value').text(response.stateUi);
          $('#info-application-state-value').attr('data-state', response.state);
          $('#action-application-toggle-lock').removeClass('icon-locked icon-unlocked');
          $('#action-application-toggle-lock').addClass(response.applicantEditable ? 'icon-unlocked' : 'icon-locked');
          loadLogEntries();
          refreshActions();
          $('.notification-queue').notificationQueue('notification', 'info', 'Hakemuksen tila vaihdettu');
        }
      });
    });
    $('.application-handling-options-container').on('click', function(event) {
      event.stopPropagation();
    });
    $('.application-action.icon-handling').on('click', function(event) {
      event.stopPropagation();
      $('.application-handling-options-container').toggle();
    });
    $(document).on("click", function () {
      $('.application-handling-options-container').hide();
    });
    function refreshActions() {
      var currentState = $('#info-application-state-value').attr('data-state');
      $('div.application-handling-option').each(function() {
        var optionState = $(this).attr('data-state');
        var applicableStates = [];
        if ($(this).attr('data-show')) {
          applicableStates = $(this).attr('data-show').split(',');
        }
        $(this).toggle(optionState != currentState && (applicableStates.length == 0 || $.inArray(currentState, applicableStates) >= 0));
      });
    }
  });
  
}).call(this);