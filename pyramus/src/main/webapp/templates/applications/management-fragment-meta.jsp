<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="application-section application-meta">
  <div class="additional-info-container">
    <div class="meta-container">
      <span class="meta-name">Hakemuksen tila</span>
      <span id="info-application-state-value" data-state="${infoState}" class="meta-value">${infoStateUi}</span>
    </div>
    <div class="meta-container">
      <span class="meta-name">Käsittelijä</span>
      <span id="info-application-handler-value" data-handler-id="${infoHandlerId}" class="meta-value">
        <c:choose>
          <c:when test="${empty infoHandler}">-</c:when>
          <c:otherwise>${infoHandler}</c:otherwise>
        </c:choose>
      </span>
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
    <div class="meta-container">
      <span class="meta-name">Asiakirjat</span>
      <span id="info-application-documents-value" class="meta-value"></span>
    </div>
    <c:if test="${infoStudentUrl ne null}">
      <div class="meta-container">
        <span class="meta-name">Pyramus-profiili</span>
        <span class="meta-value"><a href="${infoStudentUrl}" target="_blank">Näytä opiskelija</a></span>
      </div>
    </c:if>
  </div>
  
</section>