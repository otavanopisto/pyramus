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
      alert('sign with ' + authService);
    }
  });
  
}).call(this);