function selectCredentialsPhase(phaseName) {
  $('.error-container').hide();
  $(".application-content__credentials-phase-wrapper").hide();
  document.querySelector(`.application-content__credentials-phase-wrapper[data-phase-name="${phaseName}"]`).style.display = 'flex';
  document.getElementById("parentRegisterCredentialType").value = phaseName;
}

function createCredentials(event) {
  $('.error-container').hide();

  event.preventDefault();
  
  const formElement = event.target.closest("form");
  const formData = new FormData(formElement);
  
  var hash = formData.get("hash");
  var ssn = formData.get("ssn-confirm");
  var type = $('#parentRegisterCredentialType').val();

  var payload = {
    hash: hash,
    type: type,
    "ssn-confirm": ssn
  };

  switch (type) {
    case "LOGGEDIN":
    break;
    
    case "CREATE":
      var usr = $('#u').val();
      var pwd1 = $('#p1').val();
      var pwd2 = $('#p2').val();
      
      payload = Object.assign(payload, {
        "new-username": usr,
        "new-password1": pwd1,
        "new-password2": pwd2
      });
    break;
    
    case "LOGIN":
      var oldusr = $('#lu').val();
      var oldpwd = $('#lp1').val();
      
      payload = Object.assign(payload, {
        username: oldusr,
        password: oldpwd
      });
    break;
  }
  
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
    data: payload,
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
}
