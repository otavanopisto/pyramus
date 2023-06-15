<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="application-section application-actions">  
  <div class="application-handling-container" style="display:none;">
    <div class="application-handling-option backward-action" data-state="PENDING" data-show="PROCESSING"><span class="application-handling-text">Peruuta k‰sittely</span></div>
    <div class="application-handling-option backward-action" data-state="PROCESSING" data-show="WAITING_STAFF_SIGNATURE,REJECTED"><span class="application-handling-text">Palauta k‰sittelyyn</span></div>
    <div class="application-handling-option forward-action" data-state="PROCESSING" data-show="PENDING"><span class="application-handling-text">Ota k‰sittelyyn</span></div>
    <div class="application-handling-option forward-action" data-line="nettilukio,nettipk,aikuislukio,mk" data-state="WAITING_STAFF_SIGNATURE" data-show="PROCESSING"><span class="application-handling-text">Siirr‰ hyv‰ksytt‰v‰ksi</span></div>
    <div class="application-handling-option accept-action sign-button" data-document-id="${infoSignatures.staffDocumentId}" data-document-state="${infoSignatures.staffDocumentState}" data-ssn="${infoSsn}" style="display:none;">
      <span class="application-handling-text">Allekirjoita hyv‰ksynt‰</span>
    </div>
    <div class="application-handling-option accept-action" data-state="APPROVED_BY_SCHOOL" data-show="STAFF_SIGNED"><span class="application-handling-text">Ilmoita hyv‰ksymisest‰</span></div>
    <div class="application-handling-option accept-action" data-state="TRANSFERRED_AS_STUDENT" data-show="PROCESSING,STAFF_SIGNED,APPROVED_BY_SCHOOL,APPROVED_BY_APPLICANT"><span class="application-handling-text">Siirr‰ opiskelijaksi</span></div>
    <div class="application-handling-option accept-action" data-line="aineopiskelu.aineopiskelupk" data-state="REGISTRATION_CHECKED" data-show="REGISTERED_AS_STUDENT"><span class="application-handling-text">Kuittaa tarkistetuksi</span></div>    
    <div class="application-handling-option decline-action" data-state="REJECTED" data-show="PROCESSING,APPROVED_BY_SCHOOL,APPROVED_BY_APPLICANT,REGISTERED_AS_STUDENT"><span class="application-handling-text decline-application">Hylk‰‰ hakemus</span></div>
    <div class="application-handling-option delete-action" data-state="ARCHIVE" data-show="PROCESSING"><span class="application-handling-text archive-application">Poista hakemus</span></div>
  </div>
  
  <div id="signatures-dialog" class="signatures-auth-sources" title="Valitse tunnistusl‰hde" style="display:none;">
  </div>
  
  <div id="delete-application-dialog" title="Hakemuksen poistaminen" style="display:none;">
    <p>Haluatko varmasti poistaa t‰m‰n hakemuksen?</p>
  </div>
  <div id="transfer-as-student-dialog" title="Siirret‰‰nkˆ hakija opiskelijaksi?" style="display:none;">
    <p>Huomaa, ett‰ t‰m‰n j‰lkeen hakemuksen k‰sittely ei en‰‰ ole mahdollista<br/>ja ett‰ opiskelija saa ohjeet Muikku-tunnusten luomiseksi.</p>
  </div>
</section>  