<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-source" style="display:none;">

    <div class="application-line"></div>

    <h2 class="form-section__header">Mistä sait tiedon koulutuksesta?</h2>
  
    <div class="form-section__field-container field-source">
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="known" type="radio" name="field-source" value="tuttu" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="known">Ennestään tuttu</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="google" type="radio" name="field-source" value="google" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="google">Googlaamalla</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="facebook" type="radio" name="field-source" value="facebook" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="facebook">Facebook</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="instagram" type="radio" name="field-source" value="instagram" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="instagram">Instagram</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="sanomalehti" type="radio" name="field-source" value="sanomalehti" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="sanomalehti">Sanomalehti</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="tienvarsimainos" type="radio" name="field-source" value="tienvarsimainos" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="tienvarsimainos">Tienvarsimainos</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="valotaulumainos" type="radio" name="field-source" value="valotaulumainos" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="valotaulumainos">Valotaulumainos</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="elokuva" type="radio" name="field-source" value="elokuva" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="elokuva">Elokuva- tai TV-mainos</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="tuttava" type="radio" name="field-source" value="tuttava" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="tuttava">Kuulin kaverilta, tuttavalta, tms.</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="opot" type="radio" name="field-source" value="opot" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="opot">Opo</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="messut" type="radio" name="field-source" value="messut" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="messut">Messut</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="te-toimisto" type="radio" name="field-source" value="te-toimisto" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="te-toimisto">TE-toimisto</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="nuorisotyo" type="radio" name="field-source" value="nuorisotyo" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="nuorisotyo">Nuorisotyö</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="muu" type="radio" name="field-source" value="muu" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="muu">Muu</label>
        </div>
      </div>
      <p id="field-source-mandatory" style="display:none;">Valitse vähintään yksi</p>
    </div>

    <div class="form-section__field-container field-source-other dependent" data-dependent-field="field-source" data-dependent-values="sanomalehti,valotaulumainos,elokuva,messut,muu" style="display:none;">
      <label for="field-source-other" class="required">Kerro tarkemmin mistä</label>
      <input id="field-source-other" type="text" name="field-source-other" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
    </div>
  
  </section>