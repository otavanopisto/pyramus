<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
  <c:when test="${ajaxSupportIncluded != true}">
    <script src="${pageContext.request.contextPath}/scripts/es6-promise/4.2.8/es6-promise.auto.min.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/axios/0.19.0/axios.min.js"></script>
    
    <script type="text/javascript">
      axios.defaults.baseURL = GLOBAL_contextPath + '/1/';
    </script>
    
    <c:set scope="request" var="ajaxSupportIncluded" value="true"/>
  </c:when>
</c:choose>