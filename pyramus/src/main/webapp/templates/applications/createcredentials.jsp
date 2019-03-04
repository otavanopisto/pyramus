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
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css"/>
    <script defer="defer" type="text/javascript" src="//code.jquery.com/jquery-1.12.4.min.js"></script>
    <script defer="defer" type="text/javascript" src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application-credentials.js"></script>
  </head>
  <body>
    <header class="application-header">
      <div class="application-header__content">
        <div class="application-header__logo">
          <div class="application-header__logo-text">Otavan<br/>Opist<span class="application-header__logo-branding">o...</span></div>
        </div>
      </div>
    </header>

    <c:choose>
      <c:when test="${credentialsCreated eq true}">
        <section class="application-description application-description--credentials">
          <div class="application-description__credentials">
            <div class="application-description__credentials-header">
              Tunnuksesi on luotu onnistuneesti
            </div>
          </div>
        </section>
        
        <main class="application-content application-content--credentials">
          <p>Voit nyt kirjautua niillä osoitteessa <a href="https://otavanopisto.muikkuverkko.fi">https://otavanopisto.muikkuverkko.fi</a></p>
        </main>
        
      </c:when>
      <c:when test="${credentialsAlreadyExist eq true}">
      
        <section class="application-description application-description--credentials">
          <div class="application-description__credentials">
            <div class="application-description__credentials-header">
              Käyttäjätiliisi on jo liitetty Muikku-tunnukset
            </div>
          </div>
        </section>
        
        <main class="application-content application-content--credentials">
          <p>Voit kirjautua niillä osoitteessa <a href="https://otavanopisto.muikkuverkko.fi">https://otavanopisto.muikkuverkko.fi</a></p>
          <p>Mikäli olet unohtanut tunnuksesi, voit palauttaa ne sähköpostiisi Muikun etusivulta löytyvän <i>Unohtuiko salasana?</i> -linkin kautta.</p>
        </main>
        
      </c:when>
      <c:otherwise>
        <section class="application-description application-description--credentials">
          <div class="application-description__credentials">
            <div class="application-description__credentials-header">
              Muikku-tunnusten luonti
            </div>
          </div>
        </section>
        
        <main class="application-content application-content--credentials">
          <section class="application-content__form application-content__form--credentials">
            <form class="application-form">
              <section class="form-section section-create-credentials current">
                <div class="error-container" style="display:none;"></div>
                <div class="form-section__field-container">
                  <label class="required" for="u">Käyttäjätunnus</label> 
                  <input id="u" type="text"/>
                </div>
                
                <div class="form-section__field-container">
                  <label class="required" for="p1">Salasana</label>
                  <input id="p1" type="password"/>
                </div>
                
                <div class="form-section__field-container">
                  <label class="required" for="p2">Salasana uudelleen</label>
                  <input id="p2" type="password"/>
                </div>
                
                <input id="a" type="hidden" name="a" value="${applicationId}"/>
                <input id="t" type="hidden" name="t" value="${credentialToken}"/>
                
                <nav class="form-navigation">
                  <button id="button-create-credentials" type="button" class="button-create-credentials">Luo tunnukset</button>
                </nav>
                
              </section>
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