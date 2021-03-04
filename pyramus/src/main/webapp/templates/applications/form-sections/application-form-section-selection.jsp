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
    <!--<option value="bandilinja" data-underage-support="true" data-attachment-support="true">Bändilinja</option>-->
    <!--<option value="kasvatustieteet" data-underage-support="false" data-attachment-support="false">Kasvatustieteen linja</option>-->
    <!--<option value="laakislinja" data-underage-support="false" data-attachment-support="false">Lääkislinja</option>-->
    <option value="mk" data-underage-support="true" data-attachment-support="true">Maahanmuuttajakoulutukset</option>
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
  </c:if>

</section>