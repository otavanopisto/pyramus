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
      <section class="form-section section-create-credentials current">
      <h3 class="application-form-section-header">Muikku-tunnusten luonti</h3>
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
            
            <div class="field-container">
              <label class="required" for="u">Käyttäjätunnus</label> 
              <input id="u" type="text"/>
            </div>
            
            <div class="field-container">
              <label class="required" for="p1">Salasana</label>
              <input id="p1" type="password"/>
            </div>
            
            <div class="field-container">
              <label class="required" for="p2">Salasana uudelleen</label>
              <input id="p2" type="password"/>
            </div>
            
            <input id="a" type="hidden" name="a" value="${applicationId}"/>
            <input id="t" type="hidden" name="t" value="${credentialToken}"/>
            
            <nav class="form-navigation">
              <button id="button-create-credentials" type="button" class="button-create-credentials">Luo tunnukset</button>
            </nav>
  
          </c:otherwise>
        </c:choose>
      </section>
    </main>
  </body>
</html>