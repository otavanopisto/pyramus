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
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application-credentials.js"></script>
  </head>
  <body>
    <main class="application">
      <header class="application-logo-header"></header>
      <h3>Muikku-tunnusten luonti</h3>
      <c:choose>
        <c:when test="${credentialsCreated eq true}">
          <p>Tunnuksesi on luotu onnistuneesti.</p>
          <p>Voit nyt kirjautua niillä osoitteessa <a href="https://otavanopisto.muikkuverkko.fi">https://otavanopisto.muikkuverkko.fi</a></p>
        </c:when>
        <c:when test="${credentialsAlreadyExist eq true}">
          <p>Käyttäjätiliisi on jo liitetty Muikku-tunnukset.</p>
          <p>Voit kirjautua niillä osoitteessa <a href="https://otavanopisto.muikkuverkko.fi">https://otavanopisto.muikkuverkko.fi</a></p>
          <p>Mikäli olet unohtanut tunnuksesi, voit palauttaa ne sähköpostiisi Muikun etusivulta löytyvän <i>Unohtuiko salasana?</i> -linkin kautta.</p>
        </c:when>
        <c:otherwise>
          <div class="error-container" style="display:none;"></div>
          <div>Käyttäjätunnus: <input id="u" type="text" size="20"/></div>
          <div>Salasana: <input id="p1" type="password" size="20"/></div>
          <div>Salasana uudelleen: <input id="p2" type="password" size="20"/></div>
          <input id="a" type="hidden" name="a" value="${applicationId}"/>
          <input id="t" type="hidden" name="t" value="${credentialToken}"/>
          <div><span id="save">Luo tunnukset</span></div>
        </c:otherwise>
      </c:choose>
    </main>
  </body>
</html>