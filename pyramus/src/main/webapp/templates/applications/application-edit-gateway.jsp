<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
  <head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/scripts/parsley/parsley.css"/>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css"/>

    <script defer="defer" type="text/javascript" src="//code.jquery.com/jquery-1.12.4.min.js"></script>
    <script defer="defer" type="text/javascript" src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    <script defer="defer" type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.5.7/jquery.fileupload.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/parsley.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/fi.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/moment/moment.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application.js"></script>
    
    <style>
    </style>

  </head>
  <body>
    <main class="application">
      
      <header class="application-logo-header"></header>
      
      <c:choose>
        <c:when test="${notFound eq true}">
          <p class="application-info-paragraph notify">Emme löytäneet hakemusta antamillasi tiedoilla.</p>
          <p class="application-contact-info-description">Ongelmatilanteissa, tai jos sinulla on kysyttävää, voit olla yhteydessä seuraaviin tahoihin:</p>
          <p><b>Monikulttuuriset koulutukset</b><br/>Anna-Maria Suora<br/>anna-maria.suora@otavanopisto.fi<br/>044 794 3515</p>
          <p><b>Nettilukio</b><br/>Eeva Lehikoinen<br/>eeva.lehikoinen@otavanopisto.fi<br/>044 794 5107</p>
          <p><b>Nettiperuskoulu</b><br/>Elise Hokkanen<br/>elise.hokkanen@otavanopisto.fi<br/>044 794 3273</p>
          <p><b>Aineopiskelu</b><br/>Sari Jaaranen<br/>sari.jaaranen@otavanopisto.fi<br/>044 794 5271</p>
          <p><b>Lähilukio</b><br/>Otavan Opiston toimisto<br/>info@otavanopisto.fi<br/>015 194 3552</p>
          <p><b>Bändilinja</b><br/>Jukka Tikkanen<br/>jukka.tikkanen@otavanopisto.fi<br/>044 794 5103</p>
          <p><b>Kasvatustieteen linja</b><br/>Jukka Tikkanen<br/>jukka.tikkanen@otavanopisto.fi<br/>044 794 5103</p>
          <p><b>Lääkislinja</b><br/>Minna Vähämäki<br/>minna.vahamaki@otavanopisto.fi<br/>040 189 7053</p>
          <p></p>
          <p><a href="/applications/edit.page">Takaisin edelliselle sivulle</a></p>
        </c:when>
        <c:when test="${locked eq true}">
          <p class="application-info-paragraph in-progress">Hakemuksesi on jo otettu käsittelyyn ja sen muokkaaminen on tällä hetkellä estetty.</p>
          <c:choose>
            <c:when test="${!empty handlerName && !empty handlerEmail}">
              <p></p>
              <p class="application-contact-info-description">Ongelmatilanteissa, tai jos sinulla on kysyttävää, voit olla yhteydessä hakemuksesi käsittelijään:</p>
              <p>${handlerName}<br/>${handlerEmail}</p>
              <p></p>
              <p><a href="/applications/edit.page">Takaisin edelliselle sivulle</a></p>
            </c:when>
            <c:otherwise>
              <p class="application-contact-info-description">Ongelmatilanteissa, tai jos sinulla on kysyttävää, voit olla yhteydessä seuraaviin tahoihin:</p>
              <p><b>Monikulttuuriset koulutukset</b><br/>Anna-Maria Suora<br/>anna-maria.suora@otavanopisto.fi<br/>044 794 3515</p>
              <p><b>Nettilukio</b><br/>Eeva Lehikoinen<br/>eeva.lehikoinen@otavanopisto.fi<br/>044 794 5107</p>
              <p><b>Nettiperuskoulu</b><br/>Elise Hokkanen<br/>elise.hokkanen@otavanopisto.fi<br/>044 794 3273</p>
              <p><b>Aineopiskelu</b><br/>Sari Jaaranen<br/>sari.jaaranen@otavanopisto.fi<br/>044 794 5271</p>
              <p><b>Lähilukio</b><br/>Otavan Opiston toimisto<br/>info@otavanopisto.fi<br/>015 194 3552</p>
              <p><b>Bändilinja</b><br/>Jukka Tikkanen<br/>jukka.tikkanen@otavanopisto.fi<br/>044 794 5103</p>
              <p><b>Kasvatustieteen linja</b><br/>Jukka Tikkanen<br/>jukka.tikkanen@otavanopisto.fi<br/>044 794 5103</p>
              <p><b>Lääkislinja</b><br/>Minna Vähämäki<br/>minna.vahamaki@otavanopisto.fi<br/>040 189 7053</p>
              <p></p>
              <p><a href="/applications/edit.page">Takaisin edelliselle sivule</a></p>
            </c:otherwise>
          </c:choose>
          <p>
        </c:when>
        <c:otherwise>
          <form class="application-form">
    
            <section class="form-section section-edit-info">
            
              <h3>Tervetuloa hakemuksen muokkaamiseen</h3>
            
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
        </c:otherwise>
      </c:choose>
	
    </main>

  </body>
</html>

