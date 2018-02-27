(function() {
  $(document).ready(function() {
    $('#button-create-credentials').on('click', function() {
      $('.error-container').hide();
      var usr = $('#u').val();
      var pwd1 = $('#p1').val();
      var pwd2 = $('#p2').val();
      if (!usr || !pwd1 || !pwd2) {
        $('.error-container').text('Täytä kaikki kentät').show();
        return;
      }
      if (pwd1 != pwd2) {
        $('.error-container').text('Salasanat eivät täsmää').show();
        return;
      }
      $.ajax({
        url: '/applications/createcredentials.json',
        type: 'POST',
        data: {
          applicationId: $('#a').val(),
          token: $('#t').val(),
          username: usr,
          password: pwd1
        },
        dataType: 'json',
        success: function(response) {
          if (response.status == 'OK') {
            window.location.search = '?status=ok'; 
          }
          else {
            $('.error-container').text(response.reason);
            $('.error-container').show();
          }
        },
        error: function(err) {
          $('.error-container').text(err.statusText).show();
        }
      });
    });
  });
}).call(this);