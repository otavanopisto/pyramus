<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
  <head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css"/>
    <script defer="defer" type="text/javascript" src="//code.jquery.com/jquery-1.12.4.min.js"></script>
    <script defer="defer" type="text/javascript" src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application-signatures.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/notificationqueue/notificationqueue.js"></script>

  </head>
  <body data-application-id="${applicationId}" data-applicant-ssn="${applicantSsn}">
    <header class="application-header">
      <div class="application-header__content">
        <div class="application-header__logo">
          <div class="application-header__logo-text">Otavan<br/>Opist<span class="application-header__logo-branding">o...</span></div>
        </div>
      </div>
    </header>
      
      <c:choose>
        <c:when test="${invalidState eq true}">
          <section class="application-description application-description--credentials">
            <div class="application-description__credentials">
              <div class="application-description__credentials-header application-description__credentials-header--error">
               Voi ei! Jokin meni pieleen
              </div>
            </div>
          </section>
          
          <main class="application-content application-content--credentials">
            <p><c:out value="${invalidStateReason}"/></p>
          </main>
        </c:when>
        <c:otherwise>
          <section class="application-description application-description--credentials">
            <div class="application-description__credentials">
              <div class="application-description__credentials-header">
                Opiskelupaikan vastaanottaminen
              </div>
            </div>
          </section>
          
          <main class="application-content application-content--credentials">
            <p class="application-credentials-information-row"><b>Nimi:</b> <c:out value="${applicantName}"/></p>
            <p class="application-credentials-information-row"><b>Henkilötunnus:</b> <c:out value="${applicantSsn}"/></p>
            <p class="application-credentials-information-row"><b>Linja:</b> <c:out value="${applicantLine}"/></p>
            <br/>
            <p>Vastaanota opiskelupaikka sähköisen tunnistautumisen tai mobiilivarmenteen avulla:</p>
            <div class="signatures-auth-sources"></div>
            <br/>
            <p class="application-credentials-information-row">Mikäli Sinulla ei ole pankkitunnuksia tai mobiilivarmennetta, voit ottaa opiskelupaikan vastaan lähettämällä <a href="${applicantDocumentUrl}">tulostamasi ilmoittautumislomakkeen</a> meille.</p>
          </main>
 
        </c:otherwise>
      </c:choose>
	
    </main>
    <footer class="application-footer">
      <div class="application-footer__contact">
        <div class="application-footer__contact-title">Ota yhteyttä</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Osoite:</span> Otavantie 2 B, 50670 Otava</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Puhelin:</span> 044 794 3552</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Sähköposti:</span> info@otavanopisto.fi</div>
      </div>
      <div class="application-footer__links">
        <a href="https://www.otavanopisto.fi" target="top" class="application-footer__external-link">www.otavanopisto.fi</a>
        <a href="https://www.nettilukio.fi" target="top" class="application-footer__external-link">www.nettilukio.fi</a>
        <a href="https://www.nettiperuskoulu.fi" target="top" class="application-footer__external-link">www.nettiperuskoulu.fi</a>
        <a href="https://otavanopisto.muikkuverkko.fi" target="top" class="application-footer__external-link">otavanopisto.muikkuverkko.fi</a>
        <a href="#" target="top" class="application-footer__external-link">Tietosuojaseloste</a>
      </div>
    </footer>

  </body>
</html>