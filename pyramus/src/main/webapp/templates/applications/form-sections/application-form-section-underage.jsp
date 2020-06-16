<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-underage" data-skip="true" style="display:none;">

    <div class="application-line"></div>

    <h3 class="form-section__header">Huoltajan tiedot</h3>

    <div class="form-section__field-container field-underage-grounds dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,aikuislukio,bandilinja,laakislinja" style="display:none;">  
      <label for="field-underage-grounds">Alaikäisen hakemusperusteet</label> 
      <textarea name="field-underage-grounds" rows="5" cols="40"></textarea>  
    </div>
    
    <div class="form-section__field-container field-underage-last-name">
      <label for="field-underage-last-name" class="required">Sukunimi</label>
      <input type="text" name="field-underage-last-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div> 

    <div class="form-section__field-container field-underage-first-name">
      <label for="field-underage-first-name" class="required">Etunimi</label>
      <input type="text" name="field-underage-first-name" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div> 
    
    <div class="form-section__field-container field-underage-phone">
      <label for="field-underage-phone" class="required">Puhelinnumero</label>
      <input type="text" name="field-underage-phone" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div> 

    <div class="form-section__field-container field-underage-email">
      <label for="field-underage-email" class="required">Sähköpostiosoite</label>
      <input type="text" name="field-underage-email" data-parsley-required-email-if-shown="true" data-parsley-validate-if-empty="true">
    </div> 
    
    <div class="form-section__field-container field-underage-street-address">
      <label for="field-underage-street-address" class="required">Lähiosoite</label>
      <input type="text" name="field-underage-street-address" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div> 

    <div class="form-section__field-container field-zip-code">
      <label for="field-underage-zip-code" class="required">Postinumero</label>
      <input type="text" name="field-underage-zip-code" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div> 

    <div class="form-section__field-container field-underage-city">
      <label for="field-underage-city" class="required">Postitoimipaikka</label>
      <input type="text" name="field-underage-city" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div> 

    <div class="form-section__field-container field-underage-country">
      <label for="field-underage-country" class="required">Maa</label>
      <input type="text" name="field-underage-country" value="Suomi" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div> 
    
  </section>