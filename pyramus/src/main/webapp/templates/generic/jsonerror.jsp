<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title></title>
    <jsp:include page="/templates/generic/dialoghead_generic.jsp"></jsp:include>
  </head>
  <body>
    <div id="jsonErrorContainer">
      <div class="jsonErrorIcon">
        <c:choose>
          <c:when test="${errorLevel == 1}">
            <img src="${pageContext.request.contextPath}/gfx/icons/32x32/status/dialog-information.png"/>
          </c:when>
          <c:when test="${errorLevel == 2}">
            <img src="${pageContext.request.contextPath}/gfx/icons/32x32/status/dialog-warning.png"/>
          </c:when>
          <c:when test="${errorLevel == 3}">
            <img src="${pageContext.request.contextPath}/gfx/icons/32x32/status/dialog-error.png"/>
          </c:when>
          <c:when test="${errorLevel == 4}">
            <img src="${pageContext.request.contextPath}/gfx/icons/32x32/status/dialog-error.png"/>
          </c:when>
        </c:choose>
      </div>
      
      <div class="jsonErrorMessage">${errorMessage}</div>
    </div>
  </body>
</html>