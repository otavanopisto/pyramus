<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
  <title><fmt:message key="generic.applicationTitle" /></title>
  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
  <link href="${pageContext.request.contextPath}/css/index.css" rel="stylesheet">
</head>

<body>
  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
  <!-- 
    <a href="#" onclick="setLocale('fi_FI');">FI</a><a href="#" onclick="setLocale('en_US');">EN</a>
  -->

  Access denied!

  <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
</body>
</html>