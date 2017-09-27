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
      
      <header class="application-logo-header"></header>
      
      <c:choose>
        <c:when test="${notFound eq true}">
          <p>Emme löytäneet hakemusta antamillasi tiedoilla.</p>
          <p></p>
          <p>Ongelmatilanteissa, tai jos sinulla on kysyttävää, voit olla yhteydessä seuraaviin tahoihin:</p>
          <p><b>Vapaan sivistystyön kurssit</b><br/>koulutussihteeri Heli Auvinen<br/>044 794 3565<br/>kurssit@otavanopisto.fi</p>
          <p><b>Monikulttuuriset koulutukset</b><br/>Anna-Maria Suora<br/>044 794 3515<br/>anna-maria.suora@otavanopisto.fi</p>
          <p><b>Nettilukio</b><br/>Miia Sivén<br/>040 129 4671<br/>miia.siven@otavanopisto.fi</p>
          <p><b>Nettiperuskoulu</b><br/>Annukka Manninen<br/>044 794 5108<br/>annukka.manninen@otavanopisto.fi</p>
          <p><b>Aineopiskelu</b><br/>Sari Jaaranen<br/>044 794 5271<br/>sari.jaaranen@otavanopisto.fi</p>
          <p><b>Lähilukio</b><br/>Otavan Opiston toimisto<br/>p. 015 194 3552<br/>info@otavanopisto.fi</p>
          <p><b>Bändilinja</b><br/>Jukka Tikkanen<br/>044 794 5103<br/>bandilinja@otavanopisto.fi</p>
          <p></p>
          <p><a href="/applications/edit.page">Takaisin edelliselle sivule</a></p>
        </c:when>
        <c:when test="${locked eq true}">
          <p>Hakemuksesi on jo otettu käsittelyyn ja sen muokkaaminen on tällä hetkellä estetty.</p>
          <c:choose>
            <c:when test="${!empty handlerName && !empty handlerEmail}">
              <p></p>
              <p>Ongelmatilanteissa, tai jos sinulla on kysyttävää, voit olla yhteydessä hakemuksesi käsittelijään:</p>
              <p>${handlerName}<br/>${handlerEmail}</p>
              <p></p>
              <p><a href="/applications/edit.page">Takaisin edelliselle sivule</a></p>
            </c:when>
            <c:otherwise>
              <p></p>
              <p>Ongelmatilanteissa, tai jos sinulla on kysyttävää, voit olla yhteydessä seuraaviin tahoihin:</p>
              <p><b>Vapaan sivistystyön kurssit</b><br/>koulutussihteeri Heli Auvinen<br/>044 794 3565<br/>kurssit@otavanopisto.fi</p>
              <p><b>Monikulttuuriset koulutukset</b><br/>Anna-Maria Suora<br/>044 794 3515<br/>anna-maria.suora@otavanopisto.fi</p>
              <p><b>Nettilukio</b><br/>Miia Sivén<br/>040 129 4671<br/>miia.siven@otavanopisto.fi</p>
              <p><b>Nettiperuskoulu</b><br/>Annukka Manninen<br/>044 794 5108<br/>annukka.manninen@otavanopisto.fi</p>
              <p><b>Aineopiskelu</b><br/>Sari Jaaranen<br/>044 794 5271<br/>sari.jaaranen@otavanopisto.fi</p>
              <p><b>Lähilukio</b><br/>Otavan Opiston toimisto<br/>p. 015 194 3552<br/>info@otavanopisto.fi</p>
              <p><b>Bändilinja</b><br/>Jukka Tikkanen<br/>044 794 5103<br/>bandilinja@otavanopisto.fi</p>
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

