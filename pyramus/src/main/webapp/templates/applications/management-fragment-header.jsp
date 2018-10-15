<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<nav class="application-actions-header">
  <div class="application-actions-container">
     <div class="application-action icon-back" id="action-application-back-pyramus">
       <span class="application-action__label">Takaisin Pyramukseen</span>
     </div>
    <c:if test="${infoStudentUrl eq null}">
      <c:choose>
        <c:when test="${mode eq 'view'}">
          <div class="application-action application-action--edit icon-edit" id="action-application-edit">
            <span class="application-action__label">Muokkaa hakemusta</span>
          </div>
        </c:when>
        <c:otherwise>
          <div class="application-action application-action--view icon-view" id="action-application-view">
            <span class="application-action__label">Näytä hakemus</span>
          </div>
        </c:otherwise>
      </c:choose>
      <c:choose>
        <c:when test="${infoApplicantEditable eq true}">
          <div class="application-action application-action--log icon-unlocked" id="action-application-toggle-lock">
            <span class="application-action__label">Hakemus auki</span>
          </div>
        </c:when>
        <c:otherwise>
          <div class="application-action application-action--lock icon-locked" id="action-application-toggle-lock">
            <span class="application-action__label">Hakemus lukittu</span>
          </div>
        </c:otherwise>
      </c:choose>
    </c:if>
    <div class="application-action application-action--logs icon-logs" id="action-application-logs"></div>
  </div>
</nav>