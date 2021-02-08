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
  </head>
  <body>
    <header class="application-header">
      <div class="application-header__content">
        <div class="application-header__logo"></div>
      </div>
    </header>

    <section class="application-description">
      <div class="application-description__line application-description__line--selection">
        <div class="application-description__line-header">
          Hae opiskelijaksi
        </div>
        <div class="application-description__line-content">Valitse haluamasi koulutusohjelma ja siirry täyttämään opiskelijahakemus.</div>
      </div>
    </section>

    <main class="application-selection">

      <div class="application-selection__section-wrapper">
        <div class="application-selection__section-header">Verkkokoulutukset</div>
        <div class="application-selection__section-container">
          <div class="selection-box selection-box--nettilukio">
            <div class="selection-box__header-image">
              <div class="selection-box__header-text">
                Nettiluki<span class="selection-box__header-text-indicator">o...</span>
              </div>
            </div>
            <div class="selection-box__description">
              Nettilukiossa opiskelet lukio-opintoja tutkintotavoitteisesti tavoitteenasi lukion päättötodistus ja/tai ylioppilastutkinto. Nettilukio on aikuislukio ja tarkoitettu yli 18-vuotiaille opiskelijoille. Alaikäisiä voidaan hyväksyä vain, jos haun taustalla on riittävät perusteet. Toisessa oppilaitoksessa opiskelevat ja yksittäisiä lukiokursseja suorittavat voivat ilmoittautua aineopiskelijaksi.
            </div>
            <div class="selection-box__link-container">
              <a class="selection-box__link" href="/applications/create.page?line=nettilukio">Hae Nettilukioon</a>
            </div>
          </div>
  
          <div class="selection-box selection-box--nettiperuskoulu">
            <div class="selection-box__header-image">
              <div class="selection-box__header-text">
                Nettiperuskoul<span class="selection-box__header-text-indicator">u...</span>
              </div>
            </div>
            <div class="selection-box__description">
              Nettiperuskoulussa voit opiskella kesken jääneen peruskoulun loppuun tai tehdä koko aikuisten perusopetuksen oppimäärän alusta asti. Nettiperuskoulu on tarkoitettu yli 18-vuotiaille opiskelijoille, joilta puuttuu perusopetuksen päättötodistus.
            </div>
            <div class="selection-box__link-container">
              <a class="selection-box__link" href="/applications/create.page?line=nettipk">Hae Nettiperuskouluun</a>
            </div>
          </div>
  
          <div class="selection-box selection-box--aineopiskelu">
            <div class="selection-box__header-image">
              <div class="selection-box__header-text">
                Aineopiskel<span class="selection-box__header-text-indicator">u...</span>
              </div>
            </div>
            <div class="selection-box__description">
              Aineopiskelijana voit opiskella yksittäisiä lukion ja perusopetuksen kursseja. Aineopiskelijaksi voit ilmoittautua, vaikka opiskelisit samaan aikaan toisessa oppilaitoksessa.<br/><br/>
              Jos haluat käyttää vain oppimateriaaleja, sinun ei tarvitse ilmoittautua. Löydät avoimet oppimateriaalit osoitteesta: <a href="https://otavanopisto.muikkuverkko.fi">https://otavanopisto.muikkuverkko.fi</a> → Avoimet materiaalit
            </div>
            <div class="selection-box__link-container">
              <a class="selection-box__link" href="/applications/create.page?line=aineopiskelu">Ilmoittaudu aineopiskelijaksi</a>
            </div>
          </div>
        </div>
      </div>

      <div class="application-selection__section-wrapper">
        <div class="application-selection__section-header">Lähikoulutukset</div>
        <div class="application-selection__section-container">
          <div class="selection-box selection-box--aikuislukio">
            <div class="selection-box__header-image">
              <div class="selection-box__header-text">
                Aikuisluki<span class="selection-box__header-text-indicator">o...</span>
              </div>
            </div>
            <div class="selection-box__description">
              Aikuislukiossa opiskelet koko lukion oppimäärän tavoitteenasi lukion päättötodistus ja/tai ylioppilastutkinto. Aikuislukiossa voit myös tehdä loppuun aiemmin kesken jääneet toisessa päivä- tai aikuislukiossa aloittamasi lukio-opinnot.<br/><br/>
              Aikuislukio on valintasi, jos haluat opiskella lukio-opintoja lähiopintoina Otavan Opiston kampuksella Mikkelissä. Myös asuminen asuntolassa on mahdollista.
            </div>
            <div class="selection-box__link-container">
              <a class="selection-box__link" href="/applications/create.page?line=aikuislukio">Hae Aikuislukioon</a>
            </div>
          </div>

          <div class="selection-box selection-box--maahanmuuttajakoulutus">
            <div class="selection-box__header-image">
              <div class="selection-box__header-text">
                Maahanmuuttajakoulutukse<span class="selection-box__header-text-indicator">t...</span>
              </div>
            </div>
            <div class="selection-box__description">
              Maahanmuuttajakoulutukset ovat opiskelijoita varten, joiden äidinkieli ei ole suomi ja jotka tarvitsevat peruskoulun päättötodistuksen jatko-opintoja varten.
            </div>
            <div class="selection-box__link-container">
              <a class="selection-box__link" href="/applications/create.page?line=mk">Hae Maahanmuuttajakoulutuksiin</a>
            </div>
          </div>
        </div>
      </div>
    </main>
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
        <a href="https://otavia.fi/opiskelija-ja-opintotietorekisterin-tietosuojaseloste/" target="_blank" class="application-footer__external-link">Tietosuojaseloste</a>
        <a href="https://docs.google.com/document/d/1EXS3TroGTNAq9N8bV1pK6W2byKntZpHudBHH3NBGgXY/edit?usp=sharing" target="_blank" class="application-footer__external-link">Saavutettavuusseloste</a>
      </div>
    </footer>
  </body>
</html>