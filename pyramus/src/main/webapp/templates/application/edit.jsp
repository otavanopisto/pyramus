<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
  <head>
    <meta charset="UTF-8"/>

    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/scripts/parsley/parsley.css"/>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css"/>

    <script defer="defer" type="text/javascript" src="//code.jquery.com/jquery-1.12.4.min.js"></script>
    <script defer="defer" type="text/javascript" src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    <script defer="defer" type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.5.7/jquery.fileupload.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/parsley.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/fi.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/moment/moment.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/application/application.js"></script>
    
    <style>
    </style>

  </head>
  <body>
    <main>
	    <form class="application-form">

        <section class="form-section section-edit-info">
        
          <div>Tervetuloa hakemuksen muokkaamiseen ja niin pois päin</div>
        
          <div class="field-container field-last-name">
            <label for="field-last-name">Sukunimi</label>
            <input type="text" id="field-last-name" name="field-last-name" data-parsley-required="true">
          </div> 
  
          <div class="field-container field-reference-code">
            <label for="field-reference-code">Hakemustunnus</label>
            <input type="text" id="field-reference-code" name="field-reference-code" maxlength="6" style="text-transform:uppercase;" data-parsley-required="true">
          </div>
        
        </section> 

	      <div>
	        <button type="button" class="button-edit-application">Lähetä</button>
	      </div>
	 
	    </form>
	
    </main>

  </body>
</html>

