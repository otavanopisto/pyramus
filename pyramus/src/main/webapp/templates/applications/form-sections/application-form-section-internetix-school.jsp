<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-internetix-school" data-skip="true" style="display:none;">

    <div class="application-line"></div>

    <h2 class="form-section__header">Oppilaitostiedot</h2>
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
        <option value="peruskoulu">Perusopetuksen oppimäärä</option>
        <option value="tuva">TUVA-koulutus</option>
        <option value="yo-tutkinto">YO-tutkinto / lukion oppimäärä</option>
        <option value="ammatillinen-perus">Ammatillinen perustutkinto</option>
        <option value="kaksoistutkinto">Kaksoistutkinto</option>
        <option value="oppisopimus">Oppisopimuskoulutus</option>
        <option value="korkeakoulu">Korkeakoulututkinto</option>
        <option value="muu">Muu tutkinto</option>
      </select>
    </div>
    
  </section>