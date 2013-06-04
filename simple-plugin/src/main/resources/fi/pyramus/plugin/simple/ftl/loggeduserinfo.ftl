<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>	  
    <@include_page path="/templates/generic/head_generic.jsp"/> 
	  <title>Logged in user info</title>
  </head>
  <body onload="onLoad(event)">
    <@include_page path="/templates/generic/header.jsp"/> 
	  <h1 class="genericPageHeader">Logged user</h1>
	
	  <div style="font-weight: bold">First name</div>  
    <div>${loggedUser.firstName?html}</div>
    <br/>
    <div style="font-weight: bold">Last name</div>
    <div>${loggedUser.lastName?html}</div>
    
    <@include_page path="/templates/generic/footer.jsp"/> 
  </body>
</html>