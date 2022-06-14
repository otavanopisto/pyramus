<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-personal-info" style="display:none;">

    <h2 class="form-section__header">Henkilötiedot</h2>
    
    <div class="form-section__field-container field-last-name">
      <label for="field-last-name" class="required">Sukunimi</label>
      <input type="text" id="field-last-name" name="field-last-name" data-parsley-required="true">
    </div>
    
    <div class="form-section__field-container field-first-names">
      <label for="field-first-names" class="required">Etunimet</label>
      <input type="text" id="field-first-names" name="field-first-names" data-parsley-required="true">
    </div> 

    <div class="form-section__field-container field-nickname">
      <label for="field-nickname" class="required">Kutsumanimi</label>
      <input type="text" style="display:none;" id="field-nickname" name="field-nickname" data-parsley-validate-if-empty="true"  data-parsley-nickname="">
      <div class="nicknames-container">
      </div>
    </div> 
    
    <div class="form-section__field-container field-birthday">
      <label for="field-birthday" class="required">Syntymäaika</label>
      <input type="text" id="field-birthday" name="field-birthday" data-parsley-required="true" data-parsley-date-format="">
      <span class="field-help">Esitysmuoto p.k.vvvv (esim. 15.3.1995)</span>
    </div>

    <div class="form-section__field-container field-ssn-end">
      <label for="field-ssn-end">Henkilötunnuksen loppuosa</label>
      <input type="text" id="field-ssn-end" name="field-ssn-end" maxlength="4" style="text-transform:uppercase;" data-parsley-validate-if-empty="true" data-parsley-ssn-end-format="">
      <span class="field-help">Esitysmuoto XXXX (ilman edeltävää välimerkkiä A tai -)</span>
    </div>
    
    <div class="-form-section__field-container-field-compulsory-education dependent" data-dependent-field="field-line" data-dependent-values="aineopiskelu" style="display:none;">
      <label for="field-compulsory-education" class="required">Olen oppivelvollinen</label>
      <select id="field-compulsory-education" name="field-compulsory-education" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        <option value="">-- Valitse --</option>
        <option value="kylla">Kyllä</option>
        <option value="en">En ole</option>
      </select>
      <span class="field-help">Vastaa kyllä, jos olet alle 18-vuotias etkä ole suorittanut toisen asteen tutkintoa (lukio tai ammattitutkinto) ja olet saanut perusopetuksen päättötodistuksen 1.1.2021 jälkeen. Muussa tapauksessa vastaa en ole.</span>
    </div>

    <div class="form-section__field-container field-sex">
      <label for="field-sex" class="required">Sukupuoli</label>
      <select id="field-sex" name="field-sex" data-parsley-required="true">
        <option value="">-- Valitse --</option>
        <option value="mies">Mies</option>
        <option value="nainen">Nainen</option>
        <option value="muu">Muu</option>
      </select>
    </div>

    <div class="form-section__field-container field-street-address">
      <label for="field-street-address" class="required">Lähiosoite</label>
      <input id="field-street-address" type="text" name="field-street-address" data-parsley-required="true">
    </div> 

    <div class="form-section__field-container field-zip-code">
      <label for="field-zip-code" class="required">Postinumero</label>
      <input id="field-zip-code" type="text" name="field-zip-code" data-parsley-required="true">
    </div> 

    <div class="form-section__field-container field-city">
      <label for="field-city" class="required">Postitoimipaikka</label>
      <input id="field-city" type="text" name="field-city" data-parsley-required="true">
    </div> 

    <div class="form-section__field-container field-country">
      <label for="field-country" class="required">Maa</label>
      <input id="field-country" type="text" name="field-country" value="Suomi" data-parsley-required="true">
    </div> 

    <div class="form-section__field-container field-municipality">
      <label for="field-municipality" class="required">Kotikunta</label>
      <select id="field-municipality" name="field-municipality" data-parsley-required="true" data-source="/1/applications/municipalities">
        <option value="">-- Valitse --</option>
        <option value="none">Ei kotikuntaa Suomessa</option>
      </select>
    </div>

    <div class="form-section__field-container field-nationality">
      <label for="field-nationality" class="required">Kansalaisuus</label>
      <select id="field-nationality" name="field-nationality" data-parsley-required="true" data-source="/1/applications/nationalities" data-preselect="Suomi">
        <option value="">-- Valitse --</option>
      </select>
    </div>

    <div class="form-section__field-container field-language">
      <label for="field-language" class="required">Äidinkieli</label>
      <select id="field-language" name="field-language" data-parsley-required="true" data-source="/1/applications/languages" data-preselect="suomi">
        <option value="">-- Valitse --</option>
      </select>
    </div>

    <div class="form-section__field-container field-phone">
      <label for="field-phone" class="required">Puhelinnumero</label>
      <input type="text" id="field-phone" name="field-phone" data-parsley-required="true">
      <span class="field-help">Laita mukaan myös maakoodi, jos olet ulkomailla</span>
    </div>

    <div class="form-section__field-container field-email">
      <label for="field-email" class="required">Sähköpostiosoite</label>
      <input type="text" id="field-email" name="field-email" data-parsley-required="true" data-parsley-required-email-if-shown="true">
    </div>

    <div class="form-section__field-container field-email-secondary">
      <label for="field-email-secondary" class="required">Sähköpostiosoite uudelleen</label>
      <input type="text" id="field-email-secondary" name="field-email-secondary" data-parsley-required="true" data-parsley-required-email-if-shown="true" data-parsley-email-match="">
    </div>
  
  </section>