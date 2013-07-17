<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
  <c:when test="${hoverMenuSupportIncluded != true}">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixhovermenu/ixhovermenu.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/hovermenu.css"/>
    <c:set scope="request" var="hoverMenuSupportIncluded" value="true"/>
  </c:when>
</c:choose>