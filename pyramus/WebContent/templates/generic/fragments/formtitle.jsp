<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="genericFormTitle">
  <c:choose>
    <c:when test="${param.titleLocale != null}">
      <div class="genericFormTitleText">
        <fmt:message key="${param.titleLocale}"/>
      </div>  
    </c:when>
    <c:otherwise>
      <div class="genericFormTitleText">
        ${param.titleText}
      </div>
    </c:otherwise>
  </c:choose>
  
  <c:set var="helpText"><fmt:message key="${param.helpLocale}"></fmt:message></c:set> 
  <c:set var="emptyHelpText">???${param.helpLocale}???</c:set> 
  
  <c:choose>
    <c:when test="${helpText ne emptyHelpText}">
      <div class="genericFormSectionHelp" style="background-image: url(${pageContext.request.contextPath}/gfx/icons/16x16/apps/help-browser.png);">
        <div class="genericFormSectionHelpText">${helpText}</div>
      </div>
    </c:when>
  </c:choose>
</div>