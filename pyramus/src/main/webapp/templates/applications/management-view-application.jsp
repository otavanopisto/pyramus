<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<!DOCTYPE html>

<html>
  <head>
    <jsp:include page="/templates/applications/management-fragment-head.jsp"></jsp:include>
  </head>
  <body data-application-entity-id="${applicationEntityId}" data-application-id="${applicationId}">
    <div class="notification-queue">
      <div class="notification-queue-items">
      </div>
    </div>
    <main class="application-management">
      <jsp:include page="/templates/applications/management-fragment-header.jsp"></jsp:include>
      <section class="application-wrapper">
      
        <section class="application-section application-data">
          <h3 class="application-data-header">Hakemuksen tiedot</h3>
          <c:forEach var="section" items="${sections}">
            <h4 class="application-data-title">${section.key}</h4>
            <div class="application-sub-section">
              <c:forEach var="field" items="${section.value}">
                <div class="data-container">
                  <span class="data-name">${field.key}</span>
                  <span class="data-value">${fn:replace(field.value, newLineChar, "<br/>")}</span>
                </div>
              </c:forEach>
            </div>
          </c:forEach>
          <h4 class="application-data-title">Liitteet</h4>
          <div id="attachments-readonly-container" class="attachments-container">
          </div>
        </section>

        <jsp:include page="/templates/applications/management-fragment-meta.jsp"></jsp:include>
        <jsp:include page="/templates/applications/management-fragment-log.jsp"></jsp:include>
        <jsp:include page="/templates/applications/management-fragment-mail.jsp"></jsp:include>
        
      </section>
    </main>
  </body>
</html>