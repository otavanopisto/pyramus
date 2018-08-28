<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-additional">

    <div class="application-line"></div>

    <h3 class="form-section__header">Hakemiseen tarvittavat lisätiedot</h3>

    <div class="form-section__field-container field-foreign-line dependent" data-dependent-field="field-line" data-dependent-values="mk">
      <label for="field-foreign-line" class="required">Opintojen tyyppi</label>
      <select name="field-foreign-line" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="luku">Aikuisten perusopetuksen lukutaitovaihe</option>
        <option value="apa">Aikuisten perusopetuksen alkuvaihe</option>
        <option value="pk">Aikuisten perusopetuksen päättövaihe (monikulttuurinen peruskoululinja)</option>
        <option value="luva">Lukioon valmistava koulutus maahanmuuttajille (LUVA)</option>
        <option value="lisaopetus">Lisäopetus</option>
      </select>
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="luku">
      <p>Koulutus on oppivelvollisuusiän ylittäneille luku- ja kirjoitustaidottomille maahanmuuttajille, jotka eivät osaa lukea tai kirjoittaa suomen kieltä ja jotka haluavat saada valmiudet perusopetukseen tai työelämään siirtymistä varten.</p>
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="apa">
      <p>Koulutuksessa opiskelet peruskouluopiskelussa tarvittavia perustietoja ja -taitoja. Lisäksi harjoitellaan opiskelu- ja tiedonhankintataitoja.</p>
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="pk">
      <p>Koulutuksen tavoitteena on peruskoulun päättötodistus.</p>
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="luva">
      <p>Koulutus on tarkoitettu maahanmuuttajille ja vieraskielisille, joiden tavoitteena ovat lukio-opinnot ja joilla on jo peruskoulun päättötodistus tai vastaavat tiedot.</p>
    </div>

    <div class="form-section__field-container dependent" data-dependent-field="field-foreign-line" data-dependent-values="lisaopetus">
      <p>Koulutuksessa voit korottaa peruskoulun päättötodistuksen arvosanoja.</p>
    </div>

    <div class="form-section__field-container field-previous-studies dependent" data-dependent-field="field-line" data-dependent-values="nettipk,aikuislukio,bandilinja,laakislinja,kasvatustieteet">
      <label for="field-previous-studies" class="required">Aiemmat opinnot (listaa myös keskeytyneet)</label>
      <textarea name="field-previous-studies" rows="5" cols="40" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"></textarea>
    </div>

    <div class="form-section__field-container field-previous-studies-nettilukio dependent" data-dependent-field="field-line" data-dependent-values="nettilukio">
      <label for="field-previous-studies-nettilukio" class="required">Aiemmat opinnot</label>
      <select name="field-previous-studies-nettilukio" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="perus">Peruskoulu</option>
        <option value="kansa">Kansakoulu</option>
        <option value="lukio">Lukio (keskeytynyt)</option>
        <option value="ammatillinen">Ammatillinen 2. aste</option>
        <option value="korkea">Korkeakoulu</option>
        <option value="muu">Muu</option>
      </select>
    </div>

    <div class="form-section__field-container field-previous-studies-nettilukio-school dependent" data-dependent-field="field-previous-studies-nettilukio" data-dependent-values="lukio">
      <label for="field-previous-studies-nettilukio-school" class="required">Oppilaitos</label>
      <input type="text" name="field-previous-studies-nettilukio-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-previous-studies-nettilukio-duration dependent" data-dependent-field="field-previous-studies-nettilukio" data-dependent-values="lukio">
      <label for="field-previous-studies-nettilukio-duration" class="required">Opintojen kesto</label>
      <input type="text" name="field-previous-studies-nettilukio-duration" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-previous-studies-nettilukio-other dependent" data-dependent-field="field-previous-studies-nettilukio" data-dependent-values="muu">
      <label for="field-previous-studies-nettilukio-other" class="required">Kerro tarkemmin</label>
      <input type="text" name="field-previous-studies-nettilukio-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-other-school dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,nettipk,aikuislukio">
      <label for="field-other-school" class="required">Opiskelen tällä hetkellä toisessa oppilaitoksessa</label>
      <select name="field-other-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="en">En</option>
      </select>
    </div>

    <div class="form-section__field-container field-other-school-name dependent" data-dependent-field="field-other-school" data-dependent-values="kylla">
      <label for="field-other-school-name" class="required">Missä oppilaitoksessa opiskelet?</label>
      <input type="text" name="field-other-school-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-goals dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,aikuislukio">
      <label for="field-goals" class="required">Opiskelutavoitteet</label>
      <select name="field-goals" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="lukio">Lukion päättötodistus</option>
        <option value="yo">YO-tutkinto</option>
        <option value="molemmat">Molemmat</option>
      </select>
    </div>

    <div class="form-section__field-container field-foreign-student dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu">
      <label for="field-foreign-student" class="required">Oletko ulkomainen vaihto-opiskelija?</label>
      <select name="field-foreign-student" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="en">En</option>
      </select>
    </div>

    <div class="form-section__field-container field-previous-foreign-studies dependent" data-dependent-field="field-line" data-dependent-values="mk">
      <label for="field-previous-foreign-studies" class="required">Aikaisemmat opinnot kotimaassasi ja Suomessa</label>
      <textarea name="field-previous-foreign-studies" rows="5" cols="40" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"></textarea>
    </div>

    <div class="form-section__field-container field-job">
      <label for="field-job" class="required">Olen tällä hetkellä</label>
      <select name="field-job" data-parsley-required="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="tyollinen">Työllinen</option>
        <option value="tyoton">Työtön</option>
        <option value="opiskelija">Opiskelija</option>
        <option value="elakelainen">Eläkeläinen</option>
        <option value="muu">Muu</option>
      </select>
    </div>

    <div class="form-section__field-container field-job-other dependent" data-dependent-field="field-job" data-dependent-values="muu">
      <label for="field-job-other" class="required">Kerro tarkemmin</label>
      <input type="text" name="field-job-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-residence-permit dependent" data-dependent-field="field-line" data-dependent-values="mk">
      <label for="field-residence-permit" class="required">Onko sinulla oleskelulupa Suomeen?</label>
      <select name="field-residence-permit" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="ei">Ei</option>
      </select>
    </div>

    <div class="form-section__field-container field-info">
      <label for="field-info">Haluan kertoa itsestäni ja opiskelutavoitteistani seuraavaa</label>
      <textarea name="field-info" rows="5" cols="40"></textarea>
    </div>

    <div class="form-section__field-container field-lodging dependent" data-dependent-field="field-line" data-dependent-values="aikuislukio,bandilinja,mk">
      <label for="field-lodging" class="required">Tarvitsen asunnon opiston kampukselta</label>
      <select name="field-lodging" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="ei">Ei</option>
      </select>
    </div>

    <div class="form-section__field-container field-lodging-partial dependent" data-dependent-field="field-line" data-dependent-values="laakislinja,kasvatustieteet">
      <label for="field-lodging-partial" class="required">Tarvitsen asunnon opiston kampukselta lähijaksojen ajaksi</label>
      <select name="field-lodging-partial" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="ei">Ei</option>
      </select>
    </div>

  </section>
