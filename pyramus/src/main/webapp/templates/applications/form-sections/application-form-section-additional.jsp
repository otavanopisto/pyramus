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
        <option value="pk">Aikuisten perusopetuksen päättövaihe (monikulttuurinen peruskoululinja)</option>
        <option value="luva">Lukioon valmistava koulutus maahanmuuttajille (LUVA)</option>
        <option value="lisaopetus">Lisäopetus</option>
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

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="luva" style="display:none;">
      <p>Koulutus on tarkoitettu maahanmuuttajille ja vieraskielisille, joiden tavoitteena ovat lukio-opinnot ja joilla on jo peruskoulun päättötodistus tai vastaavat tiedot.</p>
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="lisaopetus" style="display:none;">
      <p>Koulutuksessa voit korottaa peruskoulun päättötodistuksen arvosanoja.</p>
    </div>

    <div class="form-section__field-container field-previous-studies-aineopiskelu dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu" style="display:none;">
      <label for="field-previous-studies-aineopiskelu" class="required">Yleissivistävä koulutustausta</label>
      <select id="field-previous-studies-aineopiskelu" name="field-previous-studies-aineopiskelu" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="perus">Peruskoulu (tai vastaava)</option>
        <option value="lukio">Lukion oppimäärä tai 4 vuotta lukio-opintoja</option>
        <option value="ei">En ole suorittanut mitään näistä</option>
      </select>
    </div>

    <div class="form-section__field-container field-previous-studies dependent" data-dependent-field="field-line" data-dependent-values="nettipk,aikuislukio,bandilinja,laakislinja,kasvatustieteet" style="display:none;">
      <label for="field-previous-studies" class="required">Aiemmat opinnot (listaa myös keskeytyneet)</label>
      <textarea id="field-previous-studies" name="field-previous-studies" rows="5" cols="40" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"></textarea>
    </div>

    <div class="form-section__field-container field-previous-studies-nettilukio dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <label for="field-previous-studies-nettilukio" class="required">Aiemmat opinnot</label>
      <select id="field-previous-studies-nettilukio" name="field-previous-studies-nettilukio" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="perus">Peruskoulu</option>
        <option value="kansa">Kansakoulu</option>
        <option value="lukio">Lukio (keskeytynyt)</option>
        <option value="ammatillinen">Ammatillinen 2. aste</option>
        <option value="korkea">Korkeakoulu</option>
        <option value="muu">Muu</option>
      </select>
    </div>
    <div class="form-section__field-container field-previous-studies-nettilukio-school dependent" data-dependent-field="field-previous-studies-nettilukio" data-dependent-values="lukio" style="display:none;">
      <label for="field-previous-studies-nettilukio-school" class="required">Oppilaitos</label>
      <input id="field-previous-studies-nettilukio-school" type="text" name="field-previous-studies-nettilukio-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-previous-studies-nettilukio-duration dependent" data-dependent-field="field-previous-studies-nettilukio" data-dependent-values="lukio" style="display:none;">
      <label for="field-previous-studies-nettilukio-duration" class="required">Opintojen kesto</label>
      <input id="field-previous-studies-nettilukio-duration" type="text" name="field-previous-studies-nettilukio-duration" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-previous-studies-nettilukio-other dependent" data-dependent-field="field-previous-studies-nettilukio" data-dependent-values="muu" style="display:none;">
      <label for="field-previous-studies-nettilukio-other" class="required">Kerro tarkemmin</label>
      <input id="field-previous-studies-nettilukio-other" type="text" name="field-previous-studies-nettilukio-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-previous-matriculation-exams-nettilukio dependent" data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <label for="field-previous-matriculation-exams-nettilukio" class="required">Oletko suorittanut aiemmin yo-kokeita?</label>
      <select id="field-previous-matriculation-exams-nettilukio" name="field-previous-matriculation-exams-nettilukio" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="en">En</option>
      </select>
    </div>
    <div class="form-section__field-container field-previous-matriculation-exams-nettilukio-when dependent" data-dependent-field="field-previous-matriculation-exams-nettilukio" data-dependent-values="kylla" style="display:none;">
      <label for="field-previous-matriculation-exams-nettilukio-when" class="required">Milloin olet viimeksi osallistunut yo-kokeisiin?</label>
      <input id="field-previous-matriculation-exams-nettilukio-when" type="text" name="field-previous-matriculation-exams-nettilukio-when" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
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

    <div class="form-section__field-container field-foreign-student dependent" data-dependent-field="field-internetix-line" data-dependent-values="lukio" style="display:none;">
      <label for="field-foreign-student" class="required">Oletko ulkomainen vaihto-opiskelija?</label>
      <select id="field-foreign-student" name="field-foreign-student" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="en">En</option>
      </select>
    </div>

    <div class="form-section__field-container field-previous-foreign-studies dependent" data-dependent-field="field-line" data-dependent-values="mk" style="display:none;">
      <label for="field-previous-foreign-studies" class="required">Aikaisemmat opinnot kotimaassasi ja Suomessa</label>
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

    <div class="form-section__field-container field-residence-permit dependent" data-dependent-field="field-line" data-dependent-values="mk" style="display:none;">
      <label for="field-residence-permit" class="required">Onko sinulla oleskelulupa Suomeen?</label>
      <select id="field-residence-permit" name="field-residence-permit" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="ei">Ei</option>
      </select>
    </div>

    <div class="form-section__field-container field-info">
      <label for="field-info">Haluan kertoa itsestäni ja opiskelutavoitteistani seuraavaa</label>
      <textarea id="field-info" name="field-info" rows="5" cols="40"></textarea>
    </div>

    <div class="form-section__field-container field-lodging dependent" data-dependent-field="field-line" data-dependent-values="aikuislukio,bandilinja,mk" style="display:none;">
      <label for="field-lodging" class="required">Tarvitsen asunnon opiston kampukselta</label>
      <select id="field-lodging" name="field-lodging" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="ei">Ei</option>
      </select>
    </div>

    <div class="form-section__field-container field-lodging-partial dependent" data-dependent-field="field-line" data-dependent-values="laakislinja,kasvatustieteet" style="display:none;">
      <label for="field-lodging-partial" class="required">Tarvitsen asunnon opiston kampukselta lähijaksojen ajaksi</label>
      <select id="field-lodging-partial" name="field-lodging-partial" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="ei">Ei</option>
      </select>
    </div>

  </section>
