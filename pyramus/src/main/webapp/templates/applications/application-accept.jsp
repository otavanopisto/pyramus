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
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application.js"></script>
    
    <style>
    </style>

  </head>
  <body>
    <main class="application">
      
      <header class="application-logo-header"></header>
      
      <c:choose>
        <c:when test="${invalidState eq true}">
          <p class="application-info-paragraph notify"><c:out value="${invalidStateReason}"/></p> 
        </c:when>
        <c:otherwise>
          <h3>Opiskelupaikan vastaanottaminen</h3>
          <p></p>
          <div>Nimi: <c:out value="${applicantName}"/></div>
          <div>Henkilötunnus: <c:out value="${applicantSsn}"/></div>
          <div>Linja: <c:out value="${applicantLine}"/></div>
          <p></p>
          <p>Vastaanota opiskelupaikka sähköisen tunnistautumisen tai mobiilivarmenteen avulla:</p>
          <div class="application-signature-sources">
          </div>
          <p></p>
          <p>Mikäli Sinulla ei ole pankkitunnuksia tai mobiilivarmennetta, voit ottaa opiskelupaikan vastaan lähettämällä <a href="${applicantDocumentUrl}">tulostamasi ilmoittautumislomakkeen</a> meille.</p> 
        </c:otherwise>
      </c:choose>
	
    </main>

  </body>
</html>