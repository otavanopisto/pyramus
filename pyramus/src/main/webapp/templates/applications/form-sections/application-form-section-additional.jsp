<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-additional" style="display:none;">

    <div class="application-line"></div>

    <h2 class="form-section__header">Hakemiseen tarvittavat lisätiedot</h2>

    <div class="form-section__field-container field-foreign-line dependent" data-dependent-field="field-line" data-dependent-values="mk" style="display:none;">
      <label for="field-foreign-line" class="required">Opintojen tyyppi</label>
      <select id="field-foreign-line" name="field-foreign-line" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="luku">Aikuisten perusopetuksen lukutaitovaihe</option>
        <option value="apa">Aikuisten perusopetuksen alkuvaihe</option>
        <option value="pk">Aikuisten perusopetuksen päättövaihe</option>
      </select>
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="luku" style="display:none;">
      <p>Koulutus on oppivelvollisuusiän ylittäneille luku- ja kirjoitustaidottomille maahanmuuttajille, jotka eivät osaa lukea tai kirjoittaa suomen kieltä ja jotka haluavat saada valmiudet perusopetukseen tai työelämään siirtymistä varten.</p>
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="apa" style="display:none;">
      <p>Koulutuksessa opiskelet peruskouluopiskelussa tarvittavia perustietoja ja -taitoja. Lisäksi harjoitellaan opiskelu- ja tiedonhankintataitoja.</p>
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="pk" style="display:none;">
      <p>Koulutuksen tavoitteena on peruskoulun päättötodistus.</p>
    </div>

    <div class="form-section__field-container field-previous-studies-aineopiskelu dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu,aineopiskelupk" style="display:none;">
      <label for="field-previous-studies-aineopiskelu" class="required">Yleissivistävä koulutustausta</label>
      <select id="field-previous-studies-aineopiskelu" name="field-previous-studies-aineopiskelu" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="perus">Peruskoulu (tai vastaava)</option>
        <option value="lukio">Lukion oppimäärä tai 4 vuotta lukio-opintoja</option>
        <option value="ei">En ole suorittanut mitään näistä</option>
      </select>
    </div>

    <div class="form-section__field-container field-previous-studies dependent" data-dependent-field="field-line" data-dependent-values="nettipk,aikuislukio" style="display:none;">
      <label for="field-previous-studies" class="required">Aiemmat opinnot (listaa myös keskeytyneet)</label>
      <textarea id="field-previous-studies" name="field-previous-studies" rows="5" cols="40" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"></textarea>
    </div>

    <div class="form-section__field-container field-previous-studies-nettilukio dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <label for="field-previous-studies-nettilukio" class="required">Aiemmat opinnot</label>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-previous-studies-nettilukio-perus" name="field-previous-studies-nettilukio" value="perus" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-parsley-errors-container="#field-previous-studies-nettilukio-errors"/>
        </div>
        <div class="field-row-label">
          <label for="field-previous-studies-nettilukio-perus">Peruskoulu</label>
        </div>
      </div>
      
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-previous-studies-nettilukio-kansa" name="field-previous-studies-nettilukio" value="kansa" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
        </div>
        <div class="field-row-label">
          <label for="field-previous-studies-nettilukio-kansa">Kansakoulu</label>
        </div>
      </div>

      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-previous-studies-nettilukio-lukio" name="field-previous-studies-nettilukio" value="lukio" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true"/>
        </div>
        <div class="field-row-label">
          <label for="field-previous-studies-nettilukio-lukio">Lukio (keskeytynyt)</label>
        </div>
      </div>
      
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-previous-studies-nettilukio-ammatillinen-kesken" name="field-previous-studies-nettilukio" value="ammatillinen-kesken" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
        </div>
        <div class="field-row-label">
          <label for="field-previous-studies-nettilukio-ammatillinen-kesken">Ammatilliset 2. asteen opinnot (keskeytynyt)</label>
        </div>
      </div>
      
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-previous-studies-nettilukio-ammatillinen-valmis" name="field-previous-studies-nettilukio" value="ammatillinen-valmis" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
        </div>
        <div class="field-row-label">
          <label for="field-previous-studies-nettilukio-ammatillinen-valmis">Ammatilliset 2. asteen opinnot (valmis tutkinto)</label>
        </div>
      </div>
      
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-previous-studies-nettilukio-korkea" name="field-previous-studies-nettilukio" value="korkea" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
        </div>
        <div class="field-row-label">
          <label for="field-previous-studies-nettilukio-korkea">Korkeakoulu</label>
        </div>
      </div>
      
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-previous-studies-nettilukio-muu" name="field-previous-studies-nettilukio" value="muu" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"  data-dependencies="true"/>
        </div>
        <div class="field-row-label">
          <label for="field-previous-studies-nettilukio-muu">Muu</label>
        </div>
      </div>
      <div id="field-previous-studies-nettilukio-errors"></div>
    </div>

    <div class="form-section__field-container field-previous-studies-nettilukio-other dependent" data-dependent-field="field-previous-studies-nettilukio" data-dependent-values="muu" style="display:none;">
      <label for="field-previous-studies-nettilukio-other" class="required">Kerro tarkemmin</label>
      <input id="field-previous-studies-nettilukio-other" type="text" name="field-previous-studies-nettilukio-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>
    
    <div class="form-section__field-container field-elementary-done dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <label for="field-elementary-done" class="required">Minä vuonna olet valmistunut peruskoulusta?</label>
      <input id="field-elementary-done" type="text" name="field-elementary-done" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-elementary-school dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <label for="field-elementary-school" class="required">Missä oppilaitoksissa olet ollut kirjoilla peruskoulun jälkeen?</label>
      <input id="field-elementary-school" type="text" name="field-elementary-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-latest-school dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <label for="field-latest-school" class="required">Missä oppilaitoksessa olet viimeksi opiskellut ja milloin?</label>
      <input id="field-latest-school" type="text" name="field-latest-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-previous-matriculation-exams-nettilukio dependent" data-dependent-field="field-previous-studies-nettilukio" data-dependent-values="lukio" style="display:none;">
      <label for="field-previous-matriculation-exams-nettilukio" class="required">Oletko suorittanut aiemmin yo-kokeita?</label>
      <select id="field-previous-matriculation-exams-nettilukio" name="field-previous-matriculation-exams-nettilukio" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="en">En</option>
      </select>
      <span class="field-help">Huomioi, että yo-kokeita koskevat tiedot eivät siirry automaattisesti koululta toiselle. Jos olet ilmoittautunut ylioppilaskokeisiin tai suorittanut jo joitain kokeita, ilmoita tästä jo hakuvaiheessa.</span>
    </div>

    <div class="form-section__field-container field-previous-matriculation-exams-nettilukio-when dependent" data-dependent-field="field-previous-matriculation-exams-nettilukio" data-dependent-values="kylla" style="display:none;">
      <label for="field-previous-matriculation-exams-nettilukio-when" class="required">Milloin olet viimeksi osallistunut yo-kokeisiin?</label>
      <input id="field-previous-matriculation-exams-nettilukio-when" type="text" name="field-previous-matriculation-exams-nettilukio-when" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-high-school-length dependent" data-dependent-field="field-previous-studies-nettilukio" data-dependent-values="lukio" style="display:none;">
      <label for="field-high-school-length">Aiempien lukio-opintojen kesto</label>
      <input id="field-high-school-length" type="text" name="field-high-school-length">
    </div>

    <div class="form-section__field-container field-other-school dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,nettipk,aikuislukio" style="display:none;">
      <label for="field-other-school" class="required">Opiskelen tällä hetkellä toisessa oppilaitoksessa</label>
      <select id="field-other-school" name="field-other-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="en">En</option>
      </select>
    </div>

    <div class="form-section__field-container field-other-school-name dependent" data-dependent-field="field-other-school" data-dependent-values="kylla" style="display:none;">
      <label for="field-other-school-name" class="required">Missä oppilaitoksessa opiskelet?</label>
      <input id="field-other-school-name" type="text" name="field-other-school-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-goals dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,aikuislukio" style="display:none;">
      <label for="field-goals" class="required">Opiskelutavoitteet</label>
      <select id="field-goals" name="field-goals" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="lukio">Lukion päättötodistus</option>
        <option value="molemmat">Lukion päättötodistus ja YO-tutkinto</option>
        <option value="yo_ammatillinen">YO-tutkinto ammatillisen tutkinnon pohjalta (maksullinen opiskelu)</option>
      </select>
    </div>

    <div class="form-section__field-container field-foreign-student dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu" style="display:none;">
      <label for="field-foreign-student" class="required">Oletko ulkomainen vaihto-opiskelija?</label>
      <select id="field-foreign-student" name="field-foreign-student" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="en">En</option>
      </select>
    </div>

    <div class="form-section__field-container field-previous-foreign-studies dependent" data-dependent-field="field-line" data-dependent-values="mk" style="display:none;">
      <label for="field-previous-foreign-studies" class="required">Aikaisemmat opinnot</label>
      <textarea id="field-previous-foreign-studies" name="field-previous-foreign-studies" rows="5" cols="40" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"></textarea>
    </div>

    <div class="form-section__field-container field-job">
      <label for="field-job" class="required">Olen tällä hetkellä</label>
      <select id="field-job" name="field-job" data-parsley-required="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="tyollinen">Työllinen</option>
        <option value="tyoton">Työtön</option>
        <option value="opiskelija">Opiskelija</option>
        <option value="elakelainen">Eläkeläinen</option>
        <option value="muu">Muu</option>
      </select>
    </div>

    <div class="form-section__field-container field-job-other dependent" data-dependent-field="field-job" data-dependent-values="muu" style="display:none;">
      <label for="field-job-other" class="required">Kerro tarkemmin</label>
      <input id="field-job-other" type="text" name="field-job-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-info dependent" data-dependent-field="field-line" data-dependent-values="nettipk,aineopiskelu,aineopiskelupk,aikuislukio,mk" style="display:none;">
      <label for="field-info">Haluan kertoa itsestäni ja opiskelutavoitteistani seuraavaa</label>
      <textarea id="field-info" name="field-info" rows="5" cols="40"></textarea>
      <span class="field-help">Huom! Älä kirjoita tähän terveystietoja.</span>
    </div>

    <div class="form-section__field-container field-info-nettilukio dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <label for="field-info-nettilukio">Haluan kertoa Nettilukion omalle ohjaajalle nämä taustatiedot</label>
      <textarea id="field-info-nettilukio" name="field-info-nettilukio" rows="5" cols="40"></textarea>
      <span class="field-help">Voit kertoa esimerkiksi opiskeluvalmiuksistasi, ajankäytöstäsi, motivaatiostasi ja tavoitteistasi. Nämä tiedot välitetään omalle ohjaajallesi, kun aloitat Nettilukion opinnot.</span>
    </div>

    <div class="form-section__field-container field-lodging dependent" data-dependent-field="field-line" data-dependent-values="aikuislukio,mk" style="display:none;">
      <label for="field-lodging" class="required">Tarvitsen asunnon opiston kampukselta</label>
      <select id="field-lodging" name="field-lodging" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="ei">Ei</option>
      </select>
    </div>

    <div class="form-section__field-container field-student-card dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,nettipk,aikuislukio,mk" style="display:none;">
      <label for="field-student-card" class="required">Haluan digitaalisen opiskelijakortin</label>
      <select id="field-student-card" name="field-student-card" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="ei">Ei</option>
      </select>
      <span class="field-help">Käytössä on <a href="https://slice.fi/fi" target="_blank">Slice.fi</a>, joka on digitaalinen opiskelijakortti ja opiskelijaetupalvelu. Vastaamalla kyllä annat meille luvan toimittaa palvelulle seuraavat tiedot: nimi, sähköpostiosoite, syntymäaika, opinto-oikeuden tai opintojen päättymispäivä.</span>
    </div>

  </section>
