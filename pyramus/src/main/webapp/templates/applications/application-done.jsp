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
    <main class="application">
      <header class="application-logo-header"></header>
      <c:choose>
        <c:when test="${invalidState eq true}">
          <p class="application-info-paragraph notify"><c:out value="${invalidStateReason}"/></p> 
        </c:when>
        <c:otherwise>
          <h3>Opiskelupaikka vastaanotettu</h3>
          <p></p>
          <p>Tervetuloa opiskelemaan Otavan Opistoon! Lähetämme Sinulle piakkoin lisätietoja opintojen aloittamisesta.</p>
        </c:otherwise>
      </c:choose>
    </main>
  </body>
</html>