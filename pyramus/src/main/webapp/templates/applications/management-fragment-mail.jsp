<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="application-section application-mail">
  <h3 class="application-mail-header">Sähköposti</h3>
  
  <div class="mail-form-container">
    <form id="mail-form" name="mail-form">
      <input type="hidden" name="applicationEntityId" value="${applicationEntityId}"/>
      <div class="field-container mail-form-recipients">
        <h4 class="application-mail-title">Vastaanottajat</h4>
      </div>
      <div class="field-container">
        <h4 class="application-mail-title">Otsikko</h4>
        <input id="mail-form-subject" class="mail-subject" name="mail-form-subject"/>
      </div>
      <div class="field-container">
        <h4 class="application-mail-title">Sisältö</h4>
        <textarea id="mail-form-content" class="mail-content" name="mail-form-content" rows="10"></textarea>
      </div>
      <div class="field-button-set">
        <button id="mail-form-send" class="button-send-mail" type="button">Lähetä</button>
      </div>
    </form>
  </div>

</section>