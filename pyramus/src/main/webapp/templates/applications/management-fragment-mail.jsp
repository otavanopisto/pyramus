<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="application-section application-mail">
  <h3 class="application-mail-header">Sähköposti</h3>
  
  <div class="mail-form-container">
    <form id="mail-form" name="mail-form">
      <div class="field-container mail-form-recipients">
        <div>Vastaanottajat</div>
      </div>
      <div class="field-container">
        <div>Otsikko</div>
        <input id="mail-form-subject" name="mail-form-subject"/>
      </div>
      <div class="field-container">
        <div>Sisältö</div>
        <textarea id="mail-form-content" name="mail-form-content" rows="10"></textarea>
      </div>
      <div class="field-button-set">
        <button id="mail-form-send" class="mail-form-button-send" type="button">Lähetä</button>
      </div>
    </form>
  </div>

</section>