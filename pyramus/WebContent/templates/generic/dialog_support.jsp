<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${dialogSupportIncluded != true}">
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixdialog/ixdialog.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dialog.css"/>
    <c:set scope="request" var="dialogSupportIncluded" value="true"/>
  </c:when>
</c:choose>