(function() {
  $(document).ready(function() {
    $('#button-verify-email').on('click', function() {
      $('.error-container').hide();
      var token = $('#v').val();
      var birthday = $('#field-birthday').val();
      if (!birthday) {
        $('.error-container').text('Täytä kaikki kentät').show();
        return;
      }
      $.ajax({
        url: '/applications/verifymail.json',
        type: 'POST',
        data: {
          token: token,
          birthday: birthday 
        },
        dataType: 'json',
        success: function(response) {
          window.location.search = '?v=' + token + '&status=ok'; 
        },
        error: function(err) {
          $('.error-container').text(err.responseText).show();
        }
      });
    });
  });
}).call(this);