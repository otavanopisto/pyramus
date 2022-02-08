<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-underage" data-skip="true" style="display:none;">

    <div class="application-line"></div>

    <h2 class="form-section__header">Huoltajan tiedot</h2>

    <div class="form-section__field-container field-underage-grounds dependent" data-dependent-field="field-line" data-dependent-values="nettilukio,aikuislukio,bandilinja,laakislinja" style="display:none;">  
      <label for="field-underage-grounds">Alaikäisen hakemusperusteet</label> 
      <textarea id="field-underage-grounds" name="field-underage-grounds" rows="5" cols="40"></textarea>  
    </div>
    
    <div class="form-section__field-container field-underage-last-name">
      <label for="field-underage-last-name" class="required">Sukunimi</label>
      <input id="field-underage-last-name" type="text" name="field-underage-last-name">
    </div> 

    <div class="form-section__field-container field-underage-first-name">
      <label for="field-underage-first-name" class="required">Etunimi</label>
      <input id="field-underage-first-name" type="text" name="field-underage-first-name">
    </div> 
    
    <div class="form-section__field-container field-underage-phone">
      <label for="field-underage-phone" class="required">Puhelinnumero</label>
      <input id="field-underage-phone" type="text" name="field-underage-phone">
    </div> 

    <div class="form-section__field-container field-underage-email">
      <label for="field-underage-email" class="required">Sähköpostiosoite</label>
      <input id="field-underage-email" type="text" name="field-underage-email">
    </div> 
    
    <div class="form-section__field-container field-underage-street-address">
      <label for="field-underage-street-address" class="required">Lähiosoite</label>
      <input id="field-underage-street-address" type="text" name="field-underage-street-address">
    </div> 

    <div class="form-section__field-container field-zip-code">
      <label for="field-underage-zip-code" class="required">Postinumero</label>
      <input id="field-underage-zip-code" type="text" name="field-underage-zip-code">
    </div> 

    <div class="form-section__field-container field-underage-city">
      <label for="field-underage-city" class="required">Postitoimipaikka</label>
      <input id="field-underage-city" type="text" name="field-underage-city">
    </div> 

    <div class="form-section__field-container field-underage-country">
      <label for="field-underage-country" class="required">Maa</label>
      <input id="field-underage-country" type="text" name="field-underage-country" value="Suomi">
    </div> 
    
  </section>