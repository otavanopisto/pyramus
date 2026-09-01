<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="form-section section-line" style="display:none;">

  <h2 class="application-form-section-header">Hakukohde</h2>

  <select id="field-line" name="field-line" data-parsley-required="true" data-dependencies="true" data-preselect="${preselectLine}">
    <option value="">-- Valitse --</option>
    <option value="aineopiskelu" data-underage-support="true" data-attachment-support="true">Aineopiskelu/lukio</option>
    <option value="aineopiskelupk" data-underage-support="true" data-attachment-support="false">Aineopiskelu/perusopetus</option>
    <option value="nettilukio" data-underage-support="true" data-attachment-support="true">Nettilukio</option>
    <option value="nettilukioov" data-underage-support="true" data-attachment-support="true">Nettilukio (oppivelvolliset)</option>
    <option value="nettipk" data-underage-support="true" data-attachment-support="true">Nettiperuskoulu</option>
    <option value="aikuislukio" data-underage-support="true" data-attachment-support="true">Aikuislukio</option>
    <option value="mk" data-underage-support="true" data-attachment-support="true">Aikuisten perusopetus</option>
  </select>
    
  <c:choose>
    <c:when test="${param.includeHandlerLines eq 'true'}">
      
      <div data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
        <div class="form-section__field-container">
          <label for="field-studyprogramme">Koulutusohjelma</label>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-nettilukio_alternativelines_nettilukio" name="field-nettilukio_alternativelines" value="NETTILUKIO" checked="checked" />
            </div>
            <div class="field-row-label">
              <label for="field-nettilukio_alternativelines_nettilukio">Nettilukio</label>
            </div>
          </div>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-nettilukio_alternativelines_private" name="field-nettilukio_alternativelines" value="PRIVATE" />
            </div>
            <div class="field-row-label">
              <label for="field-nettilukio_alternativelines_private">Nettilukio/yksityisopiskelu (aineopiskelu)</label>
            </div>
          </div>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-nettilukio_alternativelines_yo" name="field-nettilukio_alternativelines" value="YO" />
            </div>
            <div class="field-row-label">
              <label for="field-nettilukio_alternativelines_yo">Aineopiskelu/yo-tutkinto</label>
            </div>
          </div>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-nettilukio_alternativelines_eu-eta" name="field-nettilukio_alternativelines" value="EU_ETA" />
            </div>
            <div class="field-row-label">
              <label for="field-nettilukio_alternativelines_eu-eta">Nettilukio/yksityisopiskelu (EU- ja ETA-maiden ulkopuoliset opiskelijat)</label>
            </div>
          </div>
        </div>
      </div>

      <div data-dependent-field="field-line" data-dependent-values="aineopiskelu" style="display:none;">
        <div class="form-section__field-container">
          <label for="field-studyprogramme">Koulutusohjelma</label>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-aineopiskelu-studyprogramme-aineopiskelu" name="field-aineopiskelu-studyprogramme" value="AINEOPISKELU" checked="checked" />
            </div>
            <div class="field-row-label">
              <label for="field-aineopiskelu-studyprogramme-aineopiskelu">Aineopiskelu/lukio</label>
            </div>
          </div>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-aineopiskelu-studyprogramme-aineopiskelu-oppivelvolliset" name="field-aineopiskelu-studyprogramme" value="AINEOPISKELU_OPPIVELVOLLISET" />
            </div>
            <div class="field-row-label">
              <label for="field-aineopiskelu-studyprogramme-oppivelvolliset">Aineopiskelu/lukio (oppivelvolliset)</label>
            </div>
          </div>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-aineopiskelu-studyprogramme-kahden-tutkinnon-opinnot" name="field-aineopiskelu-studyprogramme" value="KAHDEN_TUTKINNON_OPINNOT" />
            </div>
            <div class="field-row-label">
              <label for="field-aineopiskelu-studyprogramme-kahden-tutkinnon-opinnot">Kahden tutkinnon opinnot</label>
            </div>
          </div>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-aineopiskelu-studyprogramme-eu-eta" name="field-aineopiskelu-studyprogramme" value="EU_ETA" />
            </div>
            <div class="field-row-label">
              <label for="field-aineopiskelu-studyprogramme-eu-eta">Aineopiskelu/lukio (EU- ja ETA-maiden ulkopuoliset opiskelijat)</label>
            </div>
          </div>
        </div>
      </div>

      <div data-dependent-field="field-line" data-dependent-values="aikuislukio" style="display:none;">
        <div class="form-section__field-container">
          <label for="field-studyprogramme">Koulutusohjelma</label>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-aikuislukio-studyprogramme-aikuislukio" name="field-aikuislukio-studyprogramme" value="AIKUISLUKIO" checked="checked" />
            </div>
            <div class="field-row-label">
              <label for="field-aikuislukio-studyprogramme-aikuislukio">Aikuislukio</label>
            </div>
          </div>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-aikuislukio-studyprogramme-eu-eta" name="field-aikuislukio-studyprogramme" value="EU_ETA" />
            </div>
            <div class="field-row-label">
              <label for="field-aikuislukio-studyprogramme-eu-eta">Otavan Opiston aikuislukio (EU- ja ETA-maiden ulkopuoliset opiskelijat)</label>
            </div>
          </div>
        </div>
      </div>

      <div data-dependent-field="field-line" data-dependent-values="aineopiskelupk" style="display:none;">
        <div class="form-section__field-container">
          <label for="field-studyprogramme">Koulutusohjelma</label>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-internetix_alternativelines_perusopetus" name="field-internetix_alternativelines" value="PERUSOPETUS" checked="checked" />
            </div>
            <div class="field-row-label">
              <label for="field-internetix_alternativelines_perusopetus">Aineopiskelu/perusopetus</label>
            </div>
          </div>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-internetix_alternativelines_oppilaitos" name="field-internetix_alternativelines" value="OPPILAITOS" />
            </div>
            <div class="field-row-label">
              <label for="field-internetix_alternativelines_oppilaitos">Aineopiskelu/perusopetus (oppilaitos ilmoittaa)</label>
            </div>
          </div>
          <div class="field-row-flex">
            <div class="field-row-element">
              <input type="radio" id="field-internetix_alternativelines_oppivelvollinen" name="field-internetix_alternativelines" value="OPPIVELVOLLINEN" />
            </div>
            <div class="field-row-label">
              <label for="field-internetix_alternativelines_oppivelvollinen">Aineopiskelu/perusopetus (oppivelvolliset)</label>
            </div>
          </div>
        </div>
      </div>

      <div data-dependent-field="field-line" data-dependent-values="nettilukio,nettilukioov,nettipk" style="display:none;">
        <div class="form-section__field-container">
          <label for="field-nettilukio_compulsory">Maksuton oppivelvollisuus</label>
          <select id="field-nettilukio_compulsory" name="field-nettilukio_compulsory" data-dependencies="true">
            <option value="">Ei koske opiskelijaa</option>
            <option value="compulsory">Opiskelija on maksuttoman oppivelvollisuuden piirissä</option>
            <option value="non_compulsory">Opiskelija ei kuulu maksuttoman oppivelvollisuuden piiriin</option>
          </select>
        </div>
  
        <div class="form-section__field-container" data-dependent-field="field-nettilukio_compulsory" data-dependent-values="compulsory" style="display:none;">
          <label for="field-nettilukio_compulsory_enddate">Maksuton oppivelvollisuus päättyy</label>
          <input type="text" id="field-nettilukio_compulsory_enddate" name="field-nettilukio_compulsory_enddate" data-parsley-date-format="" />
        </div>
      </div>
    </c:when>
    <c:otherwise>
      <input type="hidden" name="field-nettilukio_alternativelines"/>
      <input type="hidden" name="field-nettilukio_compulsory"/>
      <input type="hidden" name="field-nettilukio_compulsory_enddate"/>
      <input type="hidden" name="field-internetix_alternativelines"/>
      <input type="hidden" name="field-aineopiskelu-studyprogramme"/>
      <input type="hidden" name="field-aikuislukio-studyprogramme"/>
    </c:otherwise>
  </c:choose>

</section>