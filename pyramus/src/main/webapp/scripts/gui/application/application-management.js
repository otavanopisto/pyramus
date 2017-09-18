(function() {

  $(document).ready(function() {
    
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
    
    $('#button-edit-application').on('click', function() {
      window.location.href = '/applications/manage.page?application=' + $('body').attr('data-application-entity-id');
    });
    
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
    
    function createLogElement(entry) {
      var logElement = $('.log-entry.template').clone();
      logElement.removeClass('template');
      logElement.attr('data-applicationlog-id', entry.id);
      logElement.find('.log-entry-text').text(entry.text);
      logElement.find('.log-entry-author').text(entry.user);
      logElement.find('.log-entry-date').text(moment(entry.date).format('D.M.YYYY h:mm'));
      logElement.find('.log-entry-archive').on('click', $.proxy(function() {
        var id = this.attr('data-applicationlog-id');
        $.ajax({
          url: '/applications/archivelogentry.json',
          type: "GET",
          data: {
            id: id 
          },
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          success: $.proxy(function(response) {
            this.remove();
          }, this),
          error: function(err) {
            $('.notification-queue').notificationQueue('notification', 'error', 'Virhe poistaessa merkintää: ' + err.statusText);
          }
        });
      }, logElement));
      return logElement;
    }
    
  });
  
}).call(this);