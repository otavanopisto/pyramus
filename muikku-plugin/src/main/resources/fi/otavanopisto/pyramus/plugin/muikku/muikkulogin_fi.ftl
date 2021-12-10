<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <#include "muikkuloginhead.ftl">
  </head>
  <body class="muikku-login-body">
    <div class="muikku-login-card-wrapper">
      <section class="muikku-login-card">
		<header class="muikku-logo-container">
		  <img class="muikku-logo" src="//cdn.muikkuverkko.fi/assets/muikku/oo-branded-site-logo.png" role="presentation"/>
		  <span class="muikku-logo-text">Muikku</span>
		</header>
		<main class="muikku-login-container">
		  <h1 class="muikku-login-title">Muikku kirjautuminen</h1>
		  <form action="login.json" method="post" ix:jsonform="true">
		    <div class="muikku-login-container-row">
		      <label class="muikku-login-label" for="username">Käyttäjätunnus</label>
		      <input class="muikku-login-input" type="text" required="required" name="username" id="username">
		    </div>
		    
		    <div class="muikku-login-container-row">
		      <label class="muikku-login-label" for="password">Salasana</label>
		      <input class="muikku-login-input" type="password" required="required" name="password" id="password">
		    </div>
	      </form>
	    </main>
	    <footer class="muikku-login-footer">
	      <input type="submit" class="muikku-login-button" value="Kirjaudu">
	      <#list externalProviders as externalProvider>
	        <a class="${externalProvider.name}-login-button external-login-button" href="?external=${externalProvider.name}"></a>
	      </#list>
        </footer>
	  </section>
    </div>
  </body>
</html>