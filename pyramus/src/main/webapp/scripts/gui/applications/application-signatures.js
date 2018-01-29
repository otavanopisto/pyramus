(function() {
  $(document).ready(function() {
    $.getJSON('/applications/listsignaturesources.json', function(data) {
      if (data.sources) {
        $.each(data.sources.methods, function(index, method) {
          $('.signatures-auth-sources').append(
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
      }
    });
    function sign(authService) {
      $.ajax({
        url: '/applications/signstudentdocument.json',
        type: 'GET',
        data: {
          id: $('body').attr('data-application-id'),
          ssn: $('body').attr('data-applicant-ssn'),
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