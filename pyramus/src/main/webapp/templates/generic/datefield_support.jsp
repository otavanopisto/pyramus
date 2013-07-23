<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="event_support.jsp"></jsp:include>
<jsp:include page="locale_support.jsp"></jsp:include>
<jsp:include page="validation_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${datefieldSupportIncluded != true}">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixdatefield/ixdatefield.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype-datepicker-widget/datepicker.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ixdatefield.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/datepicker.css"/>
    <script type="text/javascript">
      document.observe("dom:loaded", function(event) {
        replaceDateFields();
      });
    </script>
    <c:set scope="request" var="datefieldSupportIncluded" value="true"/>
  </c:when>
</c:choose>