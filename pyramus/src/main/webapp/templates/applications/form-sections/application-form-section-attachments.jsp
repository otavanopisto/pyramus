<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-attachments" data-skip="true">

    <div class="application-line"></div>
    
    <h3 class="form-section__header">Hakemuksen liitteet</h3>

    <div class="form-section__field-container field-attachments">
      <div class="field-attachments__uploader">
        <div class="field-attachments__selector-container">
          <input type="file" id="field-attachments" name="field-attachments" multiple="true" class="field-attachments__selector">
          <div class="field-attachments__description">Lisää liitteitä klikkaamalla tästä tai raahaamalla niitä tähän laatikkoon. Liitteiden suurin sallittu yhteiskoko on 10 MB</div>
        </div>
        <div id="field-attachments-files" class="field-attachments__files"></div>
      </div>
    </div>

    <div class="form-section__field-container field-nettilukio-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="nettilukio">
      <p>Huom! Onnistuneesti lähetetystä hakemuksesta saat sähköpostiviestin. Viestissä olevan linkin ja tunnuksen avulla voit muokata hakemusta siihen asti kunnes se otetaan käsittelyyn. Voit myös lisätä liitteitä.</p>
      <p>Voit toimittaa todistusjäljennökset myös postitse (Otavan Opisto / nettilukio, Otavantie 2 B, 50670 Otava)</p>
    </div>

    <div class="form-section__field-container field-nettipk-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="nettipk">
      <p>Huom! Onnistuneesti lähetetystä hakemuksesta saat sähköpostiviestin. Viestissä olevan linkin ja tunnuksen avulla voit muokata hakemusta siihen asti kunnes se otetaan käsittelyyn. Voit myös lisätä liitteitä.</p>
      <p>Voit toimittaa todistusjäljennökset myös postitse (Otavan Opisto / nettiperuskoulu, Otavantie 2 B, 50670 Otava)</p>
    </div>

    <div class="form-section__field-container field-nettipk-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="aikuislukio">
      <p>Huom! Onnistuneesti lähetetystä hakemuksesta saat sähköpostiviestin. Viestissä olevan linkin ja tunnuksen avulla voit muokata hakemusta siihen asti kunnes se otetaan käsittelyyn. Voit myös lisätä liitteitä.</p>
      <p>Voit toimittaa todistusjäljennökset myös postitse (Otavan Opisto / aikuislukio, Otavantie 2 B, 50670 Otava)</p>
    </div>

    <!--
    <div class="form-section__field-container field-bandilinja-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="bandilinja">
      <div>Voit liittää tähän musiikkinäytteitäsi sähköisesti. Voit toimittaa musiikkinäytteet myös sähköpostin liitetiedostona osoitteeseen jukka.tikkanen@otavanopisto.fi.</div>
    </div>
    -->

    <div class="form-section__field-container field-nettipk-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="mk">
      <p>Huom! Onnistuneesti lähetetystä hakemuksesta saat sähköpostiviestin. Viestissä olevan linkin ja tunnuksen avulla voit muokata hakemusta siihen asti kunnes se otetaan käsittelyyn. Voit myös lisätä liitteitä.</p>
      <p>Voit toimittaa todistusjäljennökset myös postitse (Otavan Opisto / maahanmuuttajakoulutukset, Otavantie 2 B, 50670 Otava)</p>
    </div>

  </section>
