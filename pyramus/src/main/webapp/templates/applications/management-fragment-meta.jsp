<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="application-section application-meta">
  <div class="user-exists-container">
    <div class="helpful-piggy"></div>
    <div class="user-exists-description">
      <div class="user-exists-description-title">Hakija löytyy Pyramuksesta</div> 
    </div>
  </div>
  <div class="additional-info-wrapper">
    <div class="additional-info-container">
      <div class="data-container">
        <span class="data-name">Hakemuksen tila</span>
        <span id="info-application-state-value" class="data-value">${infoState}</span>
      </div>
      <div class="data-container">
        <span class="data-name">Käsittelijä</span>
        <span id="info-application-handler-value" class="data-value">${infoHandler}</span>
      </div>
      <div class="data-container">
        <span class="data-name">Muokattu viimeksi</span>
        <span id="info-application-last-modified-value" class="data-value">
          <fmt:formatDate pattern="d.M.yyyy H:mm" value="${infoLastModified}"/>
        </span>
      </div>
    </div>
  </div>
</section>
