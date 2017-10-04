(function() {

  $(document).ready(function() {
    
    // Contact log entries
    
    $.ajax({
      url: '/applications/listlogentries.json',
      type: "GET",
      data: {
        applicationId: $('#log-form-application-id').val() 
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
            width: 400,
            modal: true,
            buttons: {
              "Poista": function() {
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
              },
              "Peruuta": function() {
                $(dialog).dialog("close");
              }
            }
          });
        }, logElement));
      }
      else {
        logElement.find('.log-entry-actions').hide();
      }
      return logElement;
    }
    
    // Actions
    
    $('.application-action.icon-handling').on('click', function() {
      $('.application-handling-options-container').toggle();
    });
    $('.application-handling-option').on('click', function() {
      var state = this.attr('data-state');
      console.log('change state to ' + state);
    });
    
  });
  
}).call(this);