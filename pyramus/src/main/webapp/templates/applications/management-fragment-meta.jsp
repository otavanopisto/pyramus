<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="application-section application-actions">  
  <div class="application-handling-container" style="display:none;">
    <div class="application-handling-option backward-action" data-line="!aineopiskelu" data-state="PENDING" data-show="PROCESSING"><span class="application-handling-text">Peruuta k‰sittely</span></div>
    <div class="application-handling-option backward-action" data-line="!aineopiskelu" data-state="PROCESSING" data-show="WAITING_STAFF_SIGNATURE,REJECTED"><span class="application-handling-text">Palauta k‰sittelyyn</span></div>
    <div class="application-handling-option forward-action" data-line="!aineopiskelu" data-state="PROCESSING" data-show="PENDING"><span class="application-handling-text">Ota k‰sittelyyn</span></div>
    <div class="application-handling-option forward-action" data-line="!aineopiskelu" data-state="WAITING_STAFF_SIGNATURE" data-show="PROCESSING"><span class="application-handling-text">Siirr‰ hyv‰ksytt‰v‰ksi</span></div>
    <div class="application-handling-option accept-action signatures-container" data-document-id="${infoSignatures.staffDocumentId}" data-document-state="${infoSignatures.staffDocumentState}" data-ssn="${infoSsn}" style="display:none;">
      <span class="application-handling-text">Allekirjoita hyv‰ksynt‰</span>
    </div>
    <div class="application-handling-option accept-action" data-line="!aineopiskelu" data-state="APPROVED_BY_SCHOOL" data-show="STAFF_SIGNED"><span class="application-handling-text">Ilmoita hyv‰ksymisest‰</span></div>
    <div class="application-handling-option accept-action" data-line="!aineopiskelu" data-state="TRANSFERRED_AS_STUDENT" data-show="APPROVED_BY_APPLICANT"><span class="application-handling-text">Siirr‰ opiskelijaksi</span></div>
    <div class="application-handling-option accept-action" data-line="aineopiskelu" data-state="TRANSFERRED_AS_STUDENT" data-show="PENDING,PROCESSING"><span class="application-handling-text">Siirr‰ opiskelijaksi</span></div>
    <div class="application-handling-option decline-action" data-state="REJECTED" data-show="PENDING,PROCESSING"><span class="application-handling-text decline-application">Hylk‰‰ hakemus</span></div>
    <div class="application-handling-option delete-action" data-state="ARCHIVE" data-show="PENDING,PROCESSING"><span class="application-handling-text archive-application">Poista hakemus</span></div>
  </div>
  
  <!-- TODO: PLZ be a dialog in the future -->
  <div class="signatures-auth-sources"></div>
</section>

<section class="application-section application-meta">      
  
  <div class="additional-info-container">
    <div class="meta-container">
      <span class="meta-name">Hakemuksen tila</span>
      <span id="info-application-state-value" data-state="${infoState}" class="meta-value">${infoStateUi}</span>
    </div>
    <div class="meta-container">
      <span class="meta-name">K‰sittelij‰</span>
      <span id="info-application-handler-value" data-handler-id="${infoHandlerId}" class="meta-value">
        <c:choose>
          <c:when test="${empty infoHandler}">-</c:when>
          <c:otherwise>${infoHandler}</c:otherwise>
        </c:choose>
      </span>
    </div>
    <div class="meta-container">
      <span class="meta-name">J‰tetty</span>
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
        <span class="meta-value"><a href="${infoStudentUrl}" target="_blank">N‰yt‰ opiskelija</a></span>
      </div>
    </c:if>
  </div>
  
  <div id="delete-application-dialog" title="Hakemuksen poistaminen" style="display:none;">
    <p>Haluatko varmasti poistaa t‰m‰n hakemuksen?</p>
  </div>  
  
</section>