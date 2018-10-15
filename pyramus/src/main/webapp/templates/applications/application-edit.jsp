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
    <link type="text/css" rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"/>
    
    <script defer="defer" type="text/javascript" src="//code.jquery.com/jquery-1.12.4.min.js"></script>
    <script defer="defer" type="text/javascript" src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    <script defer="defer" type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.5.7/jquery.fileupload.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/parsley.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/fi.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/moment/moment.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/notificationqueue/notificationqueue.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application.js"></script>

  </head>
  <body>
    <div class="notification-queue">
      <div class="notification-queue-items">
      </div>
    </div>
    <header class="application-header">
      <div class="application-header__content">
        <div class="application-header__logo">
          <div class="application-header__logo-text">Otavan<br/>Opist<span class="application-header__logo-branding">o...</span></div>
        </div>
        <nav class="application-header__show-content-information">?</nav>
      </div>
    </header>
    
    <section class="application-description">
      <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="" style="display:none;">
        <div class="application-description__line-header">
          Hae opiskelijaksi <span class="application-description__line-label">Otavan Opistoon</span>
        </div>
        <div class="application-description__line-content">Valitse haluamasi koulutusohjelma ja siirry täyttämään opiskelijahakemus.</div>
      </div>

      <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
        <div class="application-description__line-header">
          Olet hakemassa <span class="application-description__line-label--nettilukio">nettilukioon</span>
        </div>
        <div class="application-description__line-content">Nettilukiossa opiskelet lukio-opintoja tutkintotavoitteisesti tavoitteenasi lukion päättötodistus ja/tai ylioppilastutkinto. Nettilukio on aikuislukio ja tarkoitettu yli 18-vuotiaille opiskelijoille. Alaikäisiä voidaan hyväksyä vain, jos haun taustalla on riittävät perusteet. Toisessa oppilaitoksessa opiskelevat ja yksittäisiä lukiokursseja suorittavat voivat ilmoittautua aineopiskelijaksi.</div>
      </div>

      <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="nettipk" style="display:none;">
        <div class="application-description__line-header">
          Olet hakemassa <span class="application-description__line-label--nettiperuskoulu">nettiperuskouluun</span>
        </div>
        <div class="application-description__line-content">Nettiperuskoulussa voit opiskella kesken jääneen peruskoulun loppuun tai tehdä koko aikuisten perusopetuksen oppimäärän alusta asti. Nettiperuskoulu on tarkoitettu yli 18-vuotiaille opiskelijoille, joilta puuttuu perusopetuksen päättötodistus.</div>
      </div>

      <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu" style="display:none;">
        <div class="application-description__line-header">
          Olet ilmoittautumassa <span class="application-description__line-label--aineopiskelu">aineopiskelijaksi</span>
        </div>
        <div class="application-description__line-content">Aineopiskelijana voit opiskella yksittäisiä lukion ja perusopetuksen kursseja. Aineopiskelijaksi voit ilmoittautua, vaikka opiskelisit samaan aikaan toisessa oppilaitoksessa.</div>
      </div>

      <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="aikuislukio" style="display:none;">
        <div class="application-description__line-header">
          Olet hakemassa <span class="application-description__line-label--aikuislukio">aikuislukioon</span>
        </div>
        <div class="application-description__line-content">Aikuislukiossa opiskelet koko lukion oppimäärän tavoitteenasi lukion päättötodistus ja/tai ylioppilastutkinto. Aikuislukiossa voit myös tehdä loppuun aiemmin kesken jääneet toisessa päivä- tai aikuislukiossa aloittamasi lukio-opinnot.</div>
      </div>

      <div class="application-description__line form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="mk" style="display:none;">
        <div class="application-description__line-header">
          Olet hakemassa <span class="application-description__line-label--maahanmuuttajakoulutus">maahanmuuttajakoulutuksiin</span>
        </div>
        <div class="application-description__line-content">Maahanmuuttajakoulutukset ovat sellaisia opiskelijoita varten, joiden äidinkieli ei ole suomi ja jotka tarvitsevat peruskoulun päättötodistuksen jatko-opintoja varten.</div>
      </div>
    </section>

    <main class="application-content">

      <section class="application-content__information">
        <div class="application-content__information-page-specific" style="display:none;">
          <p class="application-content__information-page-specific-non-summary">Klikkaa Seuraava-painiketta lomakkeen alaosassa kun olet täyttänyt tarvittavat tiedot.</p>
          <p class="application-content__information-page-specific-summary">Klikkaa Lähetä-painiketta lomakkeen alaosassa kun olet täyttänyt tarvittavat tiedot.</p>
          <p><b>Huom!</b> Punaisella tähdellä merkityt kentät ovat pakollisia.</p>
        </div>
        <div class="application-content__information-line-specific dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
          <p>Mikäli sinulla on kysyttävää, ota yhteyttä <a href="mailto:nettilukio@otavanopisto.fi">nettilukio@otavanopisto.fi</a></p>
        </div>
        <div class="application-content__information-line-specific dependent" data-dependent-field="field-line" data-dependent-values="nettipk" style="display:none;">
          <p>Mikäli sinulla on kysyttävää, ota yhteyttä <a href="mailto:nettiperuskoulu@otavanopisto.fi">nettiperuskoulu@otavanopisto.fi</a></p>
        </div>
        <div class="application-content__information-line-specific dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu" style="display:none;">
          <p>Mikäli sinulla on kysyttävää, ota yhteyttä <a href="mailto:aineopiskelu@otavanopisto.fi">aineopiskelu@otavanopisto.fi</a></p>
        </div>
        <div class="application-content__information-line-specific dependent" data-dependent-field="field-line" data-dependent-values="mk" style="display:none;">
          <p>Mikäli sinulla on kysyttävää, ota yhteyttä <a href="mailto:maahanmuuttajakoulutukset@otavanopisto.fi">maahanmuuttajakoulutukset@otavanopisto.fi</a></p>
        </div>
        <div class="application-content__information-line-specific dependent" data-dependent-field="field-line" data-dependent-values="aikuislukio" style="display:none;">
          <p>Mikäli sinulla on kysyttävää, ota yhteyttä <a href="mailto:aikuislukio@otavanopisto.fi">aikuislukio@otavanopisto.fi</a></p>
        </div>
      </section>
      <section class="application-content__form">
        <jsp:include page="/templates/applications/application-form.jsp"></jsp:include>
      </section>
    </main>
    <footer class="application-footer">
      <div class="application-footer__contact">
        <div class="application-footer__contact-title">Ota yhteyttä</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Osoite:</span> Otavantie 2 B, 50670 Otava</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Puhelin:</span> 044 794 3552</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Sähköposti:</span> info@otavanopisto.fi</div>
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
