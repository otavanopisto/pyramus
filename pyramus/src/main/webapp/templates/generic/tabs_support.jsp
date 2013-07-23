<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="datefield_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${tabsSupportIncluded != true}">    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixprototabs/ixprototabs.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tabs.css"/>
    <c:set scope="request" var="tabsSupportIncluded" value="true"/>
  </c:when>
</c:choose>