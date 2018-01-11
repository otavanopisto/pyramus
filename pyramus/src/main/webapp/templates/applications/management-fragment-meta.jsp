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
      <span id="info-application-documents-value" class="meta-value">
        <span id="staff-acceptance-document">
          <c:choose>
            <c:when test="${not empty infoSignatures.staffInvitationId}">
              <a href="https://www.onnistuu.fi/api/v1/invitation/${infoSignatures.staffInvitationId}/${infoSignatures.staffInvitationToken}/files/0" target="_blank">Oppilaitos</a>
            </c:when>
            <c:otherwise>-</c:otherwise>
          </c:choose>
        </span>
      </span>
    </div>
  </div>
  
  <div class="application-handling-container">
    <div class="application-handling-option" data-state="PENDING" data-show="PROCESSING"><span class="application-handling-text cancel-handling">Peruuta käsittely</span></div>
    <div class="application-handling-option" data-state="PROCESSING" data-show="WAITING_STAFF_SIGNATURE,REJECTED"><span class="application-handling-text">Palauta käsittelyyn</span></div>
    <div class="application-handling-option" data-state="PROCESSING" data-show="PENDING"><span class="application-handling-text">Ota käsittelyyn</span></div>
    <div class="application-handling-option" data-state="WAITING_STAFF_SIGNATURE" data-show="PROCESSING"><span class="application-handling-text">Siirrä hyväksyttäväksi</span></div>
    <div class="signatures-container" data-document-id="${infoSignatures.staffDocumentId}" data-document-state="${infoSignatures.staffDocumentState}" data-ssn="${infoSsn}" style="display:none;">
      <span class="application-handling-text start-processing">Allekirjoita hyväksyntä</span>
      <div class="signatures-auth-sources"></div>
    </div>
    <div class="application-handling-option" data-state="REJECTED"><span class="application-handling-text decline-application">Hylkää hakemus</span></div>
  </div>
  
</section>