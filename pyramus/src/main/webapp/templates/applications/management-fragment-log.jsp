<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="application-section application-logs">
  <h3 class="application-log-header">Käsittelyhistoria</h3>
  
  <div class="log-form-container">
    <form id="log-form" name="log-form">
      <input type="hidden" id="log-form-application-id" name="log-form-application-id" value="${applicationId}"/>
      <div class="field-container">
        <textarea id="log-form-text" name="log-form-text" rows="5"></textarea>
      </div>
      <div class="field-button-set">
        <button id="log-form-save" class="button-save-logentry" name="log-form-save" type="button">Tallenna</button>
      </div>
    </form>
  </div>
  
  <div class="log-entries-container">
  </div>

  <div class="log-entry template" style="display:none;">
    <div class="log-entry-header">
      <div class="log-entry-author"></div>
      <div class="log-entry-date"></div>
    </div>
    <div class="log-entry-text"></div>
    <div class="log-entry-actions">
      <div class="log-entry-edit log-entry-action"></div>
      <div class="log-entry-archive log-entry-action"></div>
    </div>
  </div>
  
  <div id="delete-log-entry-dialog" title="Merkinnän poistaminen" style="display:none;">
    <p>Haluatko varmasti poistaa tämän merkinnän?</p>
  </div>  

</section>