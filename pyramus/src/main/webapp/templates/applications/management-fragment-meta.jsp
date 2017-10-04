<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="application-section application-meta">      
  <div class="user-exists-container" style="display:none;">
    <div class="user-exists-description-title">Hakija löytyy jo Pyramuksesta.</div> 
    <div class="user-exists-description">
      <div class="user-exists-description-piggy"></div>
      <div class="user-exists-description-actions">
        <span>Hakijan Pyramus-profiili:</span> 
      </div>
    </div>
  </div>
  
  <div class="additional-info-wrapper">
    <div class="additional-info-container">
      <div class="meta-container">
        <span class="meta-name">Hakemuksen tila</span>
        <span id="info-application-state-value" class="meta-value">${infoState}</span>
      </div>
      <div class="meta-container">
        <span class="meta-name">Käsittelijä</span>
        <span id="info-application-handler-value" class="meta-value">${infoHandler}</span>
      </div>
      <div class="meta-container">
        <span class="meta-name">Jätetty</span>
        <span id="info-application-created-value" class="meta-value">
          <fmt:formatDate pattern="d.M.yyyy H:mm" value="${infoCreated}"/>
        </span>
      </div>
      <div class="meta-container">
        <span class="meta-name">Muokattu viimeksi</span>
        <span id="info-application-last-modified-value" class="meta-value">
          <fmt:formatDate pattern="d.M.yyyy H:mm" value="${infoLastModified}"/>
        </span>
      </div>
    </div>
  </div>
</section>
