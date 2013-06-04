<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="event_support.jsp"></jsp:include>
<jsp:include page="locale_support.jsp"></jsp:include>
<jsp:include page="scriptaculous_support.jsp"></jsp:include>
<jsp:include page="datefield_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${tableSupportIncluded != true}">    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixtable/ixtable.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ixtable.css"/>
    <c:set scope="request" var="tableSupportIncluded" value="true"/>
  </c:when>
</c:choose>