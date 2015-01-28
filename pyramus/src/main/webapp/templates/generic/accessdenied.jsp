<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
  <title>Access Denied!</title>
  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
  <link href="${pageContext.request.contextPath}/css/index.css" rel="stylesheet">
</head>

<body>
  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
  <!-- 
    <a href="#" onclick="setLocale('fi_FI');">FI</a><a href="#" onclick="setLocale('en_US');">EN</a>
  -->

  <div class="errorPageTitleContainer">
    <div class="errorPageTitleIconContainer">
      <img src="${pageContext.request.contextPath}/gfx/icons/32x32/status/dialog-error.png"/>
    </div>
    <div class="errorPageTitle">
      <fmt:message key="generic.errorPage.errorPageTitle">
        <fmt:param>Access denied!</fmt:param>
      </fmt:message>
    </div>
  </div>

  <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
</body>
</html>