<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="application-section application-logs">

  <h3 class="application-log-header">Käsittelyhistoria</h3>
  <nav class="application-logs-tabs">
    <div class="application-logs-tabs__tab tab-comment active" id="applications-tab-comment">Lisää kommentti</div>
    <div class="application-logs-tabs__tab tab-mail" id="applications-tab-mail">Lähetä sähköpostia</div>
  </nav>
  
  <section class="application-subsection application-comment">
    <div class="log-form-container">
      <form id="log-form" name="log-form">
        <input type="hidden" id="log-form-application-id" name="log-form-application-id" value="${applicationId}"/>
        <div class="field-container">
          <h4 class="application-log-title">Sisältö</h4>
          <textarea id="log-form-text" name="log-form-text" rows="5"></textarea>
        </div>
        <div class="field-button-set">
          <button id="log-form-save" class="button-save-logentry" name="log-form-save" type="button">Tallenna</button>
        </div>
      </form>
    </div>
  </section> 
  
  <section class="application-subsection application-mail">
    <div class="mail-form-container">
      <form id="mail-form" name="mail-form">
        <input type="hidden" name="applicationEntityId" value="${applicationEntityId}"/>
        <div class="field-container mail-form-recipients">
          <h4 class="application-log-title">Vastaanottajat</h4>
        </div>
        <div class="field-container">
          <h4 class="application-log-title">Viestipohjat</h4>
          <div class="application-templates-filters">
            <span class="application-templates-filter"><input id="mail-templates-filter-line" type="checkbox"> <label for="mail-templates-filter-line"> Vain linjakohtaiset</label></span> 
            <span class="application-templates-filter"><input id="mail-templates-filter-owner" type="checkbox"> <label for="mail-templates-filter-owner">Vain omat</label></span>
          </div>
          <div class="application-templates-container">
            <span id="application-mail-templates" class="application-mail-templates"></span>
            <button id="application-mail-template-apply" class="button-apply-template" type="button">Lisää</button>
          </div>
        </div>
        <div class="field-container">
          <h4 class="application-log-title">Otsikko</h4>
          <input id="mail-form-subject" class="mail-subject" name="mail-form-subject"/>
        </div>
        <div class="field-container">
          <h4 class="application-log-title">Sisältö</h4>
          <textarea id="mail-form-content" class="mail-content" name="mail-form-content" rows="10"></textarea>
        </div>
       
        <div class="field-button-set">
          <button id="mail-form-send" class="button-send-mail" type="button">Lähetä</button>
        </div>
      </form>
    </div>
  </section>
  
  <section class="application-subsection application-log-entries">
    <div class="log-entries-container">
    </div>
  </section>
  
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