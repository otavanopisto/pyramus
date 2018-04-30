<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<form id="application-form" class="application-form" data-save-url="${saveUrl}" data-done-page="${donePage}">

  <input type="hidden" id="field-application-id" name="field-application-id" value="${applicationId}" data-preload="${preload}" data-parsley-excluded="true"/>
  
  <jsp:include page="/templates/applications/form-sections/application-form-section-selection.jsp"></jsp:include>
  
  <jsp:include page="/templates/applications/form-sections/application-form-section-underage.jsp"></jsp:include>
  
  <jsp:include page="/templates/applications/form-sections/application-form-section-internetix-school.jsp"></jsp:include>  
  
  <jsp:include page="/templates/applications/form-sections/application-form-section-additional.jsp"></jsp:include>
  
  <jsp:include page="/templates/applications/form-sections/application-form-section-attachments.jsp"></jsp:include>
  
  <jsp:include page="/templates/applications/form-sections/application-form-section-summary.jsp"></jsp:include>
  
  <jsp:include page="/templates/applications/form-sections/application-form-section-done.jsp"></jsp:include>

  <nav class="form-navigation" style="display:none;">
    <button id="button-previous-section" type="button" class="button-previous-section previous btn btn-info pull-left">Edellinen</button>
    <div id="application-page-indicator" class="application-page-indicator"></div>
    <button id="button-next-section" type="button" class="button-next-section next btn btn-info pull-right">Seuraava</button>
    <button id="button-save-application" type="button" class="button-save-application">Lähetä</button>
  </nav>    

</form>

<jsp:include page="/templates/applications/form-templates/application-form-template-file.jsp"></jsp:include>

<jsp:include page="/templates/applications/form-templates/application-form-template-file-upload.jsp"></jsp:include>

