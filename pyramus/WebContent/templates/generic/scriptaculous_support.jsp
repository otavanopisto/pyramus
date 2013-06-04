<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
  <c:when test="${scriptaculousSupportIncluded != true}">    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/scriptaculous/src/scriptaculous.js"></script>
    <c:set scope="request" var="scriptaculousSupportIncluded" value="true"/>
  </c:when>
</c:choose>