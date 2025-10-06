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
    <script defer="defer" type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.28.0/js/jquery.fileupload.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/parsley.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/fi.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/moment/moment.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application-verify-email.js"></script>
    
    <style>
    </style>

  </head>
  <body>
    <header class="application-header">
      <div class="application-header__content">
        <div class="application-header__logo">
        </div>
      </div>
    </header>

    <c:choose>
      <c:when test="${notFound eq true}">
        <section class="application-description application-description--edit">
          <div class="application-description__edit">
            <div class="application-description__edit-header">
              Hakemusta ei löytynyt
            </div>
            <div class="application-description__edit-content">Ongelmatilanteissa, tai jos sinulla on kysyttävää, voit olla yhteydessä seuraaviin tahoihin:</div>
          </div>
        </section>
        
        <main class="application-content application-content--edit">
          <p class="application-editing-information-row"><b>Monikulttuuriset koulutukset</b><br/>Anna-Maria Suora<br/>anna-maria.suora@otavanopisto.fi<br/>044 794 3515</p>
          <p class="application-editing-information-row"><b>Nettilukio</b><br/>Eeva Lehikoinen<br/>eeva.lehikoinen@otavia.fi<br/>044 794 5107</p>
          <p class="application-editing-information-row"><b>Nettiperuskoulu</b><br/>Elise Hokkanen<br/>elise.hokkanen@otavia.fi<br/>044 794 3273</p>
          <p class="application-editing-information-row"><b>Aineopiskelu</b><br/>aineopiskelu@otavanopisto.fi</p>
          <p class="application-editing-information-row"><b>Lähilukio</b><br/>Otavan Opiston toimisto<br/>info@otavia.fi<br/>015 194 3552</p>
        </main>
      </c:when>
      <c:otherwise>
        <section class="application-description application-description--edit">
          <div class="application-description__edit">
            <div class="application-description__edit-header">
              Sähköpostin varmistus
            </div>
            <div class="application-description__edit-content">Sähköpostisi on nyt varmistettu. Hakemustietojen suojaamiseksi pyydämme vielä täyttämään alla olevaan kenttään hakijan syntymäajan. Tämän myötä voimme olla teihin yhteydessä hakemukseen käsittelyn aikana.</div>
          </div>
        </section>
        
        <main class="application-content application-content--edit">
          <section class="application-content__form">
            <form class="application-form">
              <input id="a" type="hidden" name="a" value="${applicationId}"/>
              <input id="v" type="hidden" name="t" value="${verificationToken}"/>
              <section class="form-section section-edit-info">
                <div class="form-section__field-container field-birthday">
                  <label for="field-birthday" class="required">Hakijan syntymäaika</label>
                  <input type="text" id="field-birthday" name="field-birthday" data-parsley-required="true">
                  <span class="field-help">Esitysmuoto p.k.vvvv (esim. 15.3.1995)</span>
                </div>
              </section> 
              <nav class="form-navigation">
                <button type="button" class="button-edit-application">Lähetä</button>
              </nav>
            </form>
          </section>
        </main>
      </c:otherwise>
    </c:choose>
	
    <footer class="application-footer">
      <div class="application-footer__contact">
        <div class="application-footer__contact-title">Ota yhteyttä</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Osoite:</span> Otavantie 2 B, 50670 Otava</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Puhelin:</span> 044 794 3552</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Sähköposti:</span> info@otavia.fi</div>
      </div>
      <div class="application-footer__links">
        <a href="https://www.otavanopisto.fi" target="top" class="application-footer__external-link">www.otavanopisto.fi</a>
        <a href="https://www.nettilukio.fi" target="top" class="application-footer__external-link">www.nettilukio.fi</a>
        <a href="https://www.nettiperuskoulu.fi" target="top" class="application-footer__external-link">www.nettiperuskoulu.fi</a>
        <a href="https://otavanopisto.muikkuverkko.fi" target="top" class="application-footer__external-link">otavanopisto.muikkuverkko.fi</a>
        <a href="#" target="top" class="application-footer__external-link">Tietosuojaseloste</a>
      </div>
    </footer>
  </body>
</html>

