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
  <body data-application-entity-id="${applicationEntityId}" data-application-id="${applicationId}" data-mode="view">
    <div class="notification-queue">
      <div class="notification-queue-items">
      </div>
    </div>
    <main class="application-content--management">
      <jsp:include page="/templates/applications/management-fragment-header.jsp"></jsp:include>
      
      <section class="application-wrapper">
        <jsp:include page="/templates/applications/management-fragment-actions.jsp"></jsp:include>
        <jsp:include page="/templates/applications/management-fragment-meta.jsp"></jsp:include>
        <input type="hidden" id="field-line" name="field-line" value="${applicationLine}"/>
        <section class="application-section application-data">
        
          <c:if test="${!empty conflicts}">
            <div class="application-conflicts-container">
              <div class="application-conflicts-description">
                <div class="application-conflicts-description-piggy"></div>
                <div class="application-conflicts-actions">
                  <c:forEach items="${conflicts}" var="conflict">
                    <p>${conflict}</p>
                  </c:forEach>
                </div>
              </div>
            </div>
          </c:if>
        
          <c:forEach var="section" items="${sections}">
            <h3 class="application-data-title">${section.key}</h3>
            <div class="application-sub-section">
              <c:forEach var="field" items="${section.value}">
                <div class="data-container">
                  <span class="data-name">${field.key}</span>
                  <span class="data-value">${fn:replace(field.value, newLineChar, "<br/>")}</span>
                </div>
              </c:forEach>
            </div>
          </c:forEach>
          <h3 id="attachments-title" class="application-data-title">Liitteet</h3>
          <div id="attachments-readonly-container" class="attachments-container">
          </div>
        </section>

        <jsp:include page="/templates/applications/management-fragment-logs.jsp"></jsp:include>
        
      </section>
    </main>
  </body>
</html>