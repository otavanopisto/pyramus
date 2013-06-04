<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
  <c:when test="${jsonRequestSupportIncluded != true}">      
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixjsonrequest.js"></script>
    <c:set scope="request" var="jsonRequestSupportIncluded" value="true"/>
  </c:when>
</c:choose>