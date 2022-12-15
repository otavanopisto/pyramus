<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="form-section section-line" style="display:none;">

  <h2 class="application-form-section-header">Valitse hakukohteesi</h2>

  <select id="field-line" name="field-line" data-parsley-required="true" data-dependencies="true" data-preselect="${preselectLine}">
    <option value="">-- Valitse --</option>
    <option value="aineopiskelu" data-underage-support="true" data-attachment-support="false">Aineopiskelu</option>
    <option value="nettilukio" data-underage-support="true" data-attachment-support="true">Nettilukio</option>
    <option value="nettipk" data-underage-support="true" data-attachment-support="true">Nettiperuskoulu</option>
    <option value="aikuislukio" data-underage-support="true" data-attachment-support="true">Aikuislukio</option>
    <option value="mk" data-underage-support="true" data-attachment-support="true">Aikuisten perusopetus</option>
  </select>
    
  <c:if test="${param.includeHandlerLines eq 'true'}">
    <div data-dependent-field="field-line" data-dependent-values="nettilukio" style="display:none;">
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="radio" id="field-nettilukio_alternativelines_private" name="field-nettilukio_alternativelines" value="PRIVATE">
        </div>
        <div class="field-row-label">
          <label for="field-nettilukio_alternativelines_private">Yksityisopiskelija</label>
        </div>
      </div>
      
      <div class="field-row-flex">
        <div class="field-row-element">
          <input type="radio" id="field-nettilukio_alternativelines_yo" name="field-nettilukio_alternativelines" value="YO">
        </div>
        <div class="field-row-label">
          <label for="field-nettilukio_alternativelines_yo">Aineopiskelu/yo-tutkinto</label>
        </div>
      </div>
    </div>

    <div data-dependent-field="field-line" data-dependent-values="nettilukio,nettipk" style="display:none;">
      <div class="form-section__field-container">
        <label for="field-nettilukio_compulsory">Maksuton oppivelvollisuus</label>
        <select id="field-nettilukio_compulsory" name="field-nettilukio_compulsory" data-dependencies="true">
          <option value="">Ei koske opiskelijaa</option>
          <option value="compulsory">Opiskelija on maksuttoman oppivelvollisuuden piirissä</option>
          <option value="non_compulsory">Opiskelija ei kuulu maksuttoman oppivelvollisuuden piiriin</option>
        </select>
      </div>
  
      <div class="form-section__field-container" data-dependent-field="field-nettilukio_compulsory" data-dependent-values="compulsory" style="display:none;">
        <label for="field-nettilukio_compulsory_enddate">Maksuton oppivelvollisuus päättynyt alkaen</label>
        <input type="text" id="field-nettilukio_compulsory_enddate" name="field-nettilukio_compulsory_enddate" data-parsley-date-format="" />
      </div>
    </div>
  </c:if>

</section>