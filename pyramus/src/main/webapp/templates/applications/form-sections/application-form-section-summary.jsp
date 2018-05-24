<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <section class="form-section section-summary">

    <div class="application-line"></div>

    <h3 class="form-section__header">Tarkista vielä yhteystietosi</h3>

    <div class="summary-container">
      <label for="summary-name">Nimi</label>
      <div id="summary-name"></div>
    </div>
    <div class="summary-container">
      <label for="summary-phone">Puhelinnumero</label>
      <div id="summary-phone"></div>
    </div>
    <div class="summary-container">
      <label for="summary-email">Sähköpostiosoite</label>
      <div id="summary-email"></div>
    </div>

    <div class="summary-container field-privacy">
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-privacy" name="field-privacy" value="kylla" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>
        <div class="field-row-label">
          <label for="field-privacy" class="required">Olen lukenut <a href="http://otavanopisto.fi/resources/public/tietosuojaselosteet/opiskelijarekisteri_tietosuojaseloste.pdf" class="summary-privacy-link">tietosuojaselosteen</a> ja hyväksyn, että tietoni tallennetaan Otavan Opiston oppilashallintojärjestelmään sekä verkko-oppimisympäristöön. </label>
        </div>
      </div>
    </div>

    <div class="summary-container field-changes">
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="checkbox" id="field-changes" name="field-changes" value="kylla" data-parsley-required-if-shown="true" data-parsley-validate-if-empty="true">
        </div>
        <div class="field-row-label">
          <label for="field-changes" class="required">Vakuutan, että antamani tiedot ovat oikein. Lupaan ilmoittaa, jos antamissani tiedoissa tapahtuu muutoksia.</label>
        </div>
      </div>
    </div>
  </section>
