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
  </head>
  <body>
    <header class="application-header">
      <div class="application-header__content">
        <div class="application-header__logo">
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
               Opiskelupaikka vastaanotettu
              </div>
            </div>
          </section>
          
          <main class="application-content application-content--credentials">
            <p>Tervetuloa opiskelemaan! Lähetämme Sinulle piakkoin lisätietoja opintojen aloittamisesta.</p>
          </main>
        </c:otherwise>
      </c:choose>
  
    </main>
    <footer class="application-footer">
      <div class="application-footer__contact">
        <div class="application-footer__contact-title">Ota yhteyttä</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Osoite:</span> Otavantie 2 B, 50670 Otava</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Puhelin:</span> 044 794 3552</div>
        <div class="application-footer__contact-row"><span class="application-footer__contact-row-label">Sähköposti:</span> info@otavia.fi</div>
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