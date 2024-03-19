function toggleLogin() {
  $('#createGuardianCredentialsContainer').hide();
  $('#loginGuardianCredentialsContainer').show();
  $('#parentRegisterCredentialType').val('LOGIN');
}

(function() {
  $(document).ready(function() {
    $('#button-create-credentials').on('click', function() {
      $('.error-container').hide();

      var hash = $('#hash').val();
      var ssn = $('#ssn').val();
      var type = $('#parentRegisterCredentialType').val();

      var usr = $('#u').val();
      var pwd1 = $('#p1').val();
      var pwd2 = $('#p2').val();
      
      var oldusr = $('#lu').val();
      var oldpwd = $('#lp1').val();
/*      
      if (!usr || !pwd1 || !pwd2) {
        $('.error-container').text('Täytä kaikki kentät').show();
        return;
      }
      if (pwd1 != pwd2) {
        $('.error-container').text('Salasanat eivät täsmää').show();
        return;
      }
*/
      $.ajax({
        url: '/parentregister.json',
        type: 'POST',
        data: {
          hash: hash,
          type: type,
          "ssn-confirm": ssn,
          "new-username": usr,
          "new-password1": pwd1,
          "new-password2": pwd2,
          username: oldusr,
          password: oldpwd
        },
        dataType: 'json',
        success: function(response) {
          if (response.status == 'OK') {
            window.location.search = '?status=ok'; 
          }
          else {
            $('.error-container').text(response.reason).show();
          }
        },
        error: function(err) {
          $('.error-container').text(err.statusText).show();
        }
      });
    });
  });
}).call(this);