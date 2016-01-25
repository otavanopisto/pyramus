<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <#include "muikkuloginhead.ftl">
  </head>
  <body class="index">
    <div class="muikku-login-overlay"></div>
    <div class="muikku-login-wrapper">
    <div class="muikku-logo"></div>
    <div class="muikku-login-container">
    <h1>Muikku kirjautuminen</h1>
    <form action="login.json" method="post" ix:jsonform="true">
      <div class="formElementRow">
        <label class="formLabel" for="username">Käyttäjätunnus</label>
        <input type="text" required="required" name="username" id="username">
      </div>
      
      <div class="formElementRow">
        <label class="formLabel" for="password">Salasana</label>
        <input type="password" required="required" name="password" id="password">
      </div>
      
      <div class="formElementRow">
        <#list externalProviders as externalProvider>
          <a class="${externalProvider.name}-button external-login-button" href="?external=${externalProvider.name}"></a>
        </#list>
        <input type="submit" class="login-button" value="Kirjaudu">
      </div>
    </form>
      <div class="clear"></div>
    </div>
  </div>
</body>
</html>