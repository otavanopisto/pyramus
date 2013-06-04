<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
  <c:when test="${glassPaneSupportIncluded != true}">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixglasspane/ixglasspane.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ixglasspane.css"/>
    <c:set scope="request" var="glassPaneSupportIncluded" value="true"/>
  </c:when>
</c:choose>