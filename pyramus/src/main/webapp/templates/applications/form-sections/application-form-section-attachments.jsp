<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-attachments" data-skip="true" style="display:none;">

    <div class="application-line"></div>

    <h2 class="form-section__header">Hakemuksen liitteet</h2>

    <div class="form-section__field-container field-nettilukioov-liiteohje dependent" data-dependent-field="field-line" data-dependent-values="nettilukioov" style="display:none;">
      <p>
        Alaikäisen hakijan on toimitettava <b>perusopetuksen päättötodistus</b>. Lisäksi hakemukseen tarvitaan seuraavat asiakirjat:
        <ul>
          <li>selvitys hakuperusteesta, jos hakija vetoaa terveydellisiin tai muihin henkilökohtaisiin syihin</li>
          <li>nykyisen tai aiemman oppilaitoksen lausunto (puolto) riittävistä opiskeluvalmiuksista</li>
          <li>uusin opintorekisteriote, jos hakijalla on aiempia toisen asteen opintoja</li>
          <li>tiedonsiirtolomake ja tarvittaessa pedagogisen tuen asiakirjat.</li>
        </ul>
      </p>
    </div>
    
    <div class="form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,aikuislukio" style="display:none;">
      <p>Liitä tähän todistukset ja mahdolliset erotodistukset aiemmista opinnoistasi.</p>  
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu" style="display:none;">
      <p>Jos haluat suorittaa lukion oppiaineen oppimäärän, liitä tähän kopio aiemmasta lukiotodistuksesta (ei päättötodistuksesta).</p>  
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu,nettilukio,aikuislukio" style="display:none;">
      <!-- 151 = suomi -->
      <div class="form-section__field-container dependent" data-dependent-field="field-language" data-dependent-values="!151" style="display:none;">
        <p>Koska äidinkielesi ei ole suomi, liitä tähän kopio suomen kielen taitotasosi osoittavasta todistuksesta.</p>  
      </div>
      <div class="form-section__field-container dependent" data-dependent-field="field-has-ssn" data-dependent-values="ei" style="display:none;">
        <p>Koska sinulla ei ole suomalaista henkilötunnusta, liitä tähän valokuva passistasi.</p>  
      </div>
    </div>

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
