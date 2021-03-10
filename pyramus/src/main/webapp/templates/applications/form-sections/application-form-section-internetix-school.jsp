<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-internetix-school" data-skip="true" style="display:none;">

    <div class="application-line"></div>

    <h2 class="form-section__header">Oppilaitostiedot</h2>
    <p>Kurssimateriaalien käyttäminen itseopiskelussa on ilmaista. Voit siis ilmoittautua Muikun käyttäjäksi ja ilmoittautua kursseille, vaikka et haluaisikaan niistä arviointia tai kurssisuoritusta. Jos haluat, että opettaja arvioi kurssisuorituksesi, se on joissakin tapauksissa maksullista. <a href="#" class="internetix-course-fees-link">Lue lisää</a>.</p>
    <p><b>Huom!</b> Oppilaitostieto tarkistetaan vielä jälkikäteen ja lähetämme laskun kurssin suorittamisesta, mikäli olet opiskelijana jossain toisessa oppilaitoksessa.</p>

    <div class="form-section__field-container field-internetix-school">
      <label for="field-internetix-school" class="required">Opiskelu muussa oppilaitoksessa</label>
      <select id="field-internetix-school" name="field-internetix-school" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="en">En opiskele missään oppilaitoksessa</option>
        <option value="kylla">Opiskelen toisessa oppilaitoksessa</option>
      </select>
    </div>

    <div class="form-section__field-container field-internetix-school-info dependent" data-dependent-field="field-internetix-school" data-dependent-values="kylla" style="display:none;">
      <p>Alla olevassa alaspudotusvalikossa ovat ne oppilaitokset, joiden kanssa olemme tehneet sopimuksen. Jos et löydä omaa oppilaitostasi listasta, valitse kohta <i>Muu oppilaitos</i>. Valitse myös, mitä tutkintoa suoritat tällä hetkellä. Jos et suorita mitään tutkintoa tai tutkintoasi ei ole listassa, valitse kohta <i>Muu tutkinto</i>.</p>
    </div>

    <div class="form-section__field-container field-internetix-contract-school dependent" data-dependent-field="field-internetix-school" data-dependent-values="kylla" style="display:none;">
      <label for="field-internetix-contract-school" class="required">Oppilaitokseni</label>
      <select id="field-internetix-contract-school" name="field-internetix-contract-school" data-source="/1/applications/contractschools" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true" data-dependencies="true">
        <option value="">-- Valitse --</option>
        <option value="muu">Muu oppilaitos</option>
      </select>
    </div>

    <div class="form-section__field-container field-internetix-contract-school-name dependent" data-dependent-field="field-internetix-contract-school" data-dependent-values="muu" style="display:none;">
      <label for="field-internetix-contract-school-name" class="required">Oppilaitos</label>
      <input type="text" id="field-internetix-contract-school-name" name="field-internetix-contract-school-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-internetix-contract-school-municipality dependent" data-dependent-field="field-internetix-contract-school" data-dependent-values="muu" style="display:none;">
      <label for="field-internetix-contract-school-municipality" class="required">Opiskelupaikkakunta</label>
      <input id="field-internetix-contract-school-municipality" type="text" name="field-internetix-contract-school-municipality" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>

    <div class="form-section__field-container field-internetix-contract-school-contact dependent" data-dependent-field="field-internetix-contract-school" data-dependent-values="muu" style="display:none;">
      <label for="field-internetix-contract-school-contact" class="required">Oppilaitoksen yhteyshenkilö ja yhteystiedot</label>
      <textarea id="field-internetix-contract-school-contact" name="field-internetix-contract-school-contact" rows="5" cols="40" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"></textarea>
    </div>

    <div class="form-section__field-container field-internetix-contract-school-degree dependent" data-dependent-field="field-internetix-school" data-dependent-values="kylla" style="display:none;">
      <label for="field-internetix-contract-school-degree" class="required">Suoritan</label>
      <select id="field-internetix-contract-school-degree" name="field-internetix-contract-school-degree" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="muu">Muu tutkinto</option>
        <option value="yo-tutkinto">YO-tutkinto / lukion oppimäärä</option>
        <option value="ammatillinen-perus">Ammatillinen perustutkinto</option>
        <option value="kaksoistutkinto">Kaksoistutkinto</option>
        <option value="oppisopimus">Oppisopimuskoulutus</option>
        <option value="peruskoulu">Peruskoulun oppimäärä</option>
        <option value="korkeakoulu">Korkeakoulututkinto</option>
        <option value="ylempi-kk">Ylempi korkeakoulututkinto</option>
      </select>
    </div>
    
    <div class="course-fees-overlay" style="display:none;">
    </div>
    <div class="course-fees" style="display:none;">
      <div class="course-fees__close"></div>
      <h3>Aineopiskelulinjan kurssimaksuista</h3>
      <p>Aineopiskelulinjalla voit opiskella samoja kursseja kuin Otavan Opiston nettilukiossa ja nettiperuskoulussa. Lisäksi tarjolla on jonkin verran myös muita kursseja.</p>
      <p><b>Milloin opiskelu maksaa?</b></p>
      <p>Oppimateriaalien itsenäinen opiskeleminen (ilman arviointia) aineopiskelulinjalla on maksutonta.</p>
      <p>Kurssiarvioinnin maksullisuuteen vaikuttaa kaksi asiaa: suoritettu kurssi sekä se, opiskeletko suoritushetkellä toisessa oppilaitoksessa.</p>
      <p><b>Jos et opiskele missään oppilaitoksessa:</b></p>
      <ul>
        <li>voit tehdä ilmaiseksi lukion pakollisia tai valtakunnallisia syventäviä kursseja</li>
        <li>voit tehdä ilmaiseksi perusopetuksen pakollisia tai valinnaisia kursseja</li>
        <li>koulukohtaiset syventävät kurssit ovat aina maksullisia (140€/kurssi)</li>
        <li>Huom! perusopetuksen kurssit ovat aina maksullisia oppivelvollisuusikäisille eli alle 17-vuotiaille (140€/kurssi)</li>
      </ul>
      <p><b>Jos opiskelet sopimusoppilaitoksessa</b> (oppilaitoksen nimi löytyy ilmoittautumislomakkeen listalta):</p>
      <ul>
        <li>varmista aina etukäteen oman oppilaitoksesi opolta tai rehtorilta, maksaako oppilaitos kurssisi vai joudutko maksamaan sen itse (140€/kurssi)</li>
      </ul>
      <p><b>Jos opiskelet jossain muussa oppilaitoksessa:</b></p>
      <ul>
        <li>kaikki lukion ja perusopetuksen kurssit ovat maksullisia (140€/kurssi)</li>
      </ul>
    </div>

  </section>