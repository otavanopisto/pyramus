<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:choose>
  <c:when test="${ckeditorSupportIncluded != true}">
    <script src="https://cdn.ckeditor.com/4.6.2/full/ckeditor.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/config/ckeditor.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixckeditorsupport.js"></script>
    <c:set scope="request" var="ckeditorSupportIncluded" value="true"/>
  </c:when>
</c:choose>