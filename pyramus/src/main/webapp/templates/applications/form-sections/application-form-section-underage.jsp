<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-underage" data-skip="true" style="display:none;">

    <div class="application-line"></div>

    <h2 class="form-section__header">Alaikäisen hakijan lisätiedot</h2>

    <div class="form-section__field-container field-underage-grounds dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,aikuislukio,bandilinja,laakislinja" style="display:none;">  
      <label for="field-underage-grounds">Alaikäisen hakemusperusteet</label> 
      <textarea id="field-underage-grounds" name="field-underage-grounds" rows="5" cols="40"></textarea>  
    </div>

    <h2 class="form-section__header">Huoltajan tiedot</h2>

    <p>Merkitse alaikäisen hakijan kaikkien virallisten huoltajien tiedot. Merkitse ensisijainen yhteyshenkilö ensimmäiseksi.</p>

    <h2 class="form-section__subheader">Huoltaja 1</h2>
    
    <div class="form-section__field-container field-underage-last-name">
      <label for="field-underage-last-name" class="required">Sukunimi</label>
      <input id="field-underage-last-name" type="text" name="field-underage-last-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
    </div> 

    <div class="form-section__field-container field-underage-first-name">
      <label for="field-underage-first-name" class="required">Etunimi</label>
      <input id="field-underage-first-name" type="text" name="field-underage-first-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
    </div> 
    
    <div class="form-section__field-container field-underage-phone">
      <label for="field-underage-phone" class="required">Puhelinnumero</label>
      <input id="field-underage-phone" type="text" name="field-underage-phone" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
    </div> 

    <div class="form-section__field-container field-underage-email">
      <label for="field-underage-email" class="required">Sähköpostiosoite</label>
      <input id="field-underage-email" type="text" name="field-underage-email" data-parsley-required-email-if-shown="true" data-parsley-validate-if-empty="true"/>
    </div> 
    
    <div class="form-section__field-container field-underage-street-address">
      <label for="field-underage-street-address" class="required">Lähiosoite</label>
      <input id="field-underage-street-address" type="text" name="field-underage-street-address" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
    </div> 

    <div class="form-section__field-container field-zip-code">
      <label for="field-underage-zip-code" class="required">Postinumero</label>
      <input id="field-underage-zip-code" type="text" name="field-underage-zip-code" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
    </div> 

    <div class="form-section__field-container field-underage-city">
      <label for="field-underage-city" class="required">Postitoimipaikka</label>
      <input id="field-underage-city" type="text" name="field-underage-city" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
    </div> 

    <div class="form-section__field-container field-underage-country">
      <label for="field-underage-country" class="required">Maa</label>
      <input id="field-underage-country" type="text" name="field-underage-country" value="Suomi" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true"/>
    </div>
    
    <h2 class="form-section__subheader">Huoltaja 2</h2>
    
    <div class="form-section__field-container field-underage-last-name-2">
      <label for="field-underage-last-name-2">Sukunimi</label>
      <input id="field-underage-last-name-2" type="text" name="field-underage-last-name-2"/>
    </div> 

    <div class="form-section__field-container field-underage-first-name-2">
      <label for="field-underage-first-name-2">Etunimi</label>
      <input id="field-underage-first-name-2" type="text" name="field-underage-first-name-2"/>
    </div> 
    
    <div class="form-section__field-container field-underage-phone-2">
      <label for="field-underage-phone-2">Puhelinnumero</label>
      <input id="field-underage-phone-2" type="text" name="field-underage-phone-2"/>
    </div> 

    <div class="form-section__field-container field-underage-email-2">
      <label for="field-underage-email-2">Sähköpostiosoite</label>
      <input id="field-underage-email-2" type="text" name="field-underage-email-2"/>
    </div> 
    
    <div class="form-section__field-container field-underage-street-address-2">
      <label for="field-underage-street-address-2">Lähiosoite</label>
      <input id="field-underage-street-address-2" type="text" name="field-underage-street-address-2"/>
    </div> 

    <div class="form-section__field-container field-zip-code-2">
      <label for="field-underage-zip-code-2">Postinumero</label>
      <input id="field-underage-zip-code-2" type="text" name="field-underage-zip-code-2"/>
    </div> 

    <div class="form-section__field-container field-underage-city-2">
      <label for="field-underage-city-2">Postitoimipaikka</label>
      <input id="field-underage-city-2" type="text" name="field-underage-city-2"/>
    </div> 

    <div class="form-section__field-container field-underage-country-2">
      <label for="field-underage-country-2">Maa</label>
      <input id="field-underage-country-2" type="text" name="field-underage-country-2" value="Suomi"/>
    </div>
    
    <h2 class="form-section__subheader">Huoltaja 3</h2>
    
    <div class="form-section__field-container field-underage-last-name-3">
      <label for="field-underage-last-name-3">Sukunimi</label>
      <input id="field-underage-last-name-3" type="text" name="field-underage-last-name-3"/>
    </div> 

    <div class="form-section__field-container field-underage-first-name-3">
      <label for="field-underage-first-name-3">Etunimi</label>
      <input id="field-underage-first-name-3" type="text" name="field-underage-first-name-3"/>
    </div> 
    
    <div class="form-section__field-container field-underage-phone-3">
      <label for="field-underage-phone-3">Puhelinnumero</label>
      <input id="field-underage-phone-3" type="text" name="field-underage-phone-3"/>
    </div> 

    <div class="form-section__field-container field-underage-email-3">
      <label for="field-underage-email-3">Sähköpostiosoite</label>
      <input id="field-underage-email-3" type="text" name="field-underage-email-3"/>
    </div> 
    
    <div class="form-section__field-container field-underage-street-address-3">
      <label for="field-underage-street-address-3">Lähiosoite</label>
      <input id="field-underage-street-address-3" type="text" name="field-underage-street-address-3"/>
    </div> 

    <div class="form-section__field-container field-zip-code-3">
      <label for="field-underage-zip-code-3">Postinumero</label>
      <input id="field-underage-zip-code-3" type="text" name="field-underage-zip-code-3"/>
    </div> 

    <div class="form-section__field-container field-underage-city-3">
      <label for="field-underage-city-3">Postitoimipaikka</label>
      <input id="field-underage-city-3" type="text" name="field-underage-city-3"/>
    </div> 

    <div class="form-section__field-container field-underage-country-3">
      <label for="field-underage-country-3">Maa</label>
      <input id="field-underage-country-3" type="text" name="field-underage-country-3" value="Suomi"/>
    </div>
    
  </section>