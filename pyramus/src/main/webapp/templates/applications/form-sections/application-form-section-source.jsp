<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-source">

    <div class="application-line"></div>

    <h3 class="form-section__header">Mistä sait tiedon koulutuksesta?</h3>
  
    <div class="form-section__field-container field-source">
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="known" type="checkbox" name="field-source" value="tuttu">
        </div>
        <div class="field-row-label">
          <label for="known">Ennestään tuttu</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="google" type="checkbox" name="field-source" value="google">
        </div>
        <div class="field-row-label">
          <label for="google">Google</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="facebook" type="checkbox" name="field-source" value="facebook">
        </div>
        <div class="field-row-label">
          <label for="facebook">Facebook</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="instagram" type="checkbox" name="field-source" value="instagram">
        </div>
        <div class="field-row-label">
          <label for="instagram">Instagram</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="sanomalehti" type="checkbox" name="field-source" value="sanomalehti">
        </div>
        <div class="field-row-label">
          <label for="sanomalehti">Sanomalehti</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="tienvarsimainos" type="checkbox" name="field-source" value="tienvarsimainos">
        </div>
        <div class="field-row-label">
          <label for="tienvarsimainos">Tienvarsimainos</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="valotaulumainos" type="checkbox" name="field-source" value="valotaulumainos">
        </div>
        <div class="field-row-label">
          <label for="valotaulumainos">Valotaulumainos</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="elokuva" type="checkbox" name="field-source" value="elokuva">
        </div>
        <div class="field-row-label">
          <label for="elokuva">Elokuva- tai TV-mainos</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="radio" type="checkbox" name="field-source" value="radio">
        </div>
        <div class="field-row-label">
          <label for="radio">Radio</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="tuttava" type="checkbox" name="field-source" value="tuttava">
        </div>
        <div class="field-row-label">
          <label for="tuttava">Kuulin kaverilta, tuttavalta, tms.</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="te-toimisto" type="checkbox" name="field-source" value="te-toimisto">
        </div>
        <div class="field-row-label">
          <label for="te-toimisto">TE-toimisto</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="messut" type="checkbox" name="field-source" value="messut">
        </div>
        <div class="field-row-label">
          <label for="messut">Messut</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="nuorisotyo" type="checkbox" name="field-source" value="nuorisotyo">
        </div>
        <div class="field-row-label">
          <label for="nuorisotyo">Nuorisotyö</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="opot" type="checkbox" name="field-source" value="opot">
        </div>
        <div class="field-row-label">
          <label for="opot">Opot</label>
        </div>
      </div>
      <div class="field-row-flex">
        <div class="field-row-element">
          <input id="muu" type="checkbox" name="field-source" value="muu" data-dependencies="true">
        </div>
        <div class="field-row-label">
          <label for="muu">Muu</label>
        </div>
      </div>
      <p id="field-source-mandatory" style="display:none;">Valitse vähintään yksi</p>
    </div>

    <div class="form-section__field-container field-source-other">
      <label for="field-source-other">Kerro tarkemmin mistä</label>
      <input type="text" name="field-source-other">
    </div>
  
  </section>