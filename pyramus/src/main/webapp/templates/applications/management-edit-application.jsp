<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
  <head>
    <jsp:include page="/templates/applications/management-fragment-head.jsp"></jsp:include>
  </head>
  <body data-application-entity-id="${applicationEntityId}" data-application-id="${applicationId}" data-mode="edit">
    <input id="saveUrl" name="saveUrl" type="hidden"/>
    <div class="notification-queue">
      <div class="notification-queue-items">
      </div>
    </div>
    <main class="application-content--management">
      <jsp:include page="/templates/applications/management-fragment-header.jsp"></jsp:include>
      <section class="application-wrapper">
        <jsp:include page="/templates/applications/management-fragment-actions.jsp"></jsp:include>
        <jsp:include page="/templates/applications/management-fragment-meta.jsp"></jsp:include>
        <section class="application-section application-data">
          <jsp:include page="/templates/applications/application-form.jsp"></jsp:include>
        </section>
        <jsp:include page="/templates/applications/management-fragment-logs.jsp"></jsp:include>
      </section>
    </main>
  </body>
</html>

