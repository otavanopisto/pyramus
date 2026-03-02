<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-attachments" data-skip="true" style="display:none;">

    <div class="application-line"></div>

    <div class="form-section__field-container field-nettilukioov-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="nettilukioov" style="display:none;">
      <p>
        Alaikäisen hakijan on hakuperusteen osoittamiseksi esitettävä <b>perusopetuksen päättötodistuksen</b> lisäksi seuraavat asiakirjat:
        <ul>
          <li>painavan perusteen selvittävä asiakirja, jos hakuperusteena terveydelliset tai muut henkilökohtaiset syyt</li>
          <li>nykyisen / edellisen oppilaitoksen edustajan puolto riittävistä opiskeluvalmiuksista</li>
          <li>tuorein opintorekisteriote mahdollisista toisen asteen opinnoista</li>
          <li><a href="https://nettilukio.fi/wp-content/uploads/2026/01/Tiedonsiirtolomake-Nettilukio.pdf" target="_blank">tiedonsiirtolomake</a> ja tarvittaessa pedagogisen tuen asiakirja</li>
        </ul>
      </p>
    </div>
    
    <h2 class="form-section__header">Hakemuksen liitteet</h2>

    <div class="form-section__field-container field-attachments">
      <div class="field-attachments__uploader">
        <div class="field-attachments__selector-container">
          <label class="visually-hidden" for="field-attachments">Liitteet</label>
          <input type="file" id="field-attachments" name="field-attachments" multiple="true" class="field-attachments__selector">
          <div class="field-attachments__description">Lisää liitteitä klikkaamalla tästä tai raahaamalla niitä tähän laatikkoon. Liitteiden suurin sallittu yhteiskoko on 20 MB</div>
        </div>
        <div id="field-attachments-files" class="field-attachments__files"></div>
      </div>
    </div>

    <div class="form-section__field-container field-nettilukio-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <p>Huom! Onnistuneesti lähetetystä hakemuksesta saat sähköpostiviestin. Viestissä olevan linkin ja tunnuksen avulla voit muokata hakemusta ja lisätä liitteitä.</p>
      <p>Voit toimittaa todistusjäljennökset myös postitse (Mikkelin kaupungin liikelaitos Otavia / Nettilukio, Otavantie 2 B, 50670 Otava)</p>
    </div>

    <div class="form-section__field-container field-nettipk-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="nettipk" style="display:none;">
      <p>Huom! Onnistuneesti lähetetystä hakemuksesta saat sähköpostiviestin. Viestissä olevan linkin ja tunnuksen avulla voit muokata hakemusta. Voit myös lisätä liitteitä.</p>
      <p>Voit toimittaa todistusjäljennökset myös salatulla sähköpostilla (saat tiedot hakemuksen käsittelijältä).</p>
    </div>

    <div class="form-section__field-container field-nettipk-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="aikuislukio" style="display:none;">
      <p>Huom! Onnistuneesti lähetetystä hakemuksesta saat sähköpostiviestin. Viestissä olevan linkin ja tunnuksen avulla voit muokata hakemusta siihen asti kunnes se otetaan käsittelyyn. Voit myös lisätä liitteitä.</p>
      <p>Voit toimittaa todistusjäljennökset myös postitse (Otavan Opisto / aikuislukio, Otavantie 2 B, 50670 Otava)</p>
    </div>

    <div class="form-section__field-container field-nettipk-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="mk" style="display:none;">
      <p>Huom! Onnistuneesti lähetetystä hakemuksesta saat sähköpostiviestin. Viestissä olevan linkin ja tunnuksen avulla voit muokata hakemusta siihen asti kunnes se otetaan käsittelyyn. Voit myös lisätä liitteitä.</p>
      <p>Voit toimittaa todistusjäljennökset myös postitse (Otavan Opisto / aikuisten perusopetus, Otavantie 2 B, 50670 Otava)</p>
    </div>

  </section>
