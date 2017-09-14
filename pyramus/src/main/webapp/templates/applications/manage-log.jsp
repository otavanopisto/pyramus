<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div>
  <h3>Käsittelyhistoria</h3>
  
  <div class="log-form-container">
    <form id="log-form" name="log-form">
      <input type="hidden" id="log-form-application-id" name="log-form-application-id" value="${applicationId}"/>
      <textarea id="log-form-text" name="log-form-text" rows="10" cols="80"></textarea>
      <input id="log-form-save" name="log-form-save" type="button" value="Tallenna"/> 
    </form>
  </div>
  
  <div class="log-entries-container">
  </div>

  <div class="log-entry template" style="display:none;">
    <div class="log-entry-text"></div>
    <div class="log-entry-author"></div>
    <div class="log-entry-date"></div>
    <div class="log-entry-archive">X</div>
  </div>  

</div>