<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title>
    <fmt:message key="generic.errorPage.errorPageTitle">
      <fmt:param>${errorMessage}</fmt:param>
    </fmt:message>
  </title>
  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
</head> 
<body>
  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
  <div class="errorPageTitleContainer">
    <div class="errorPageTitleIconContainer">
      <img src="${pageContext.request.contextPath}/gfx/icons/32x32/status/dialog-error.png"/>
    </div>
    <div class="errorPageTitle">
      <fmt:message key="generic.errorPage.errorPageTitle">
        <fmt:param>${errorMessage}</fmt:param>
      </fmt:message>
    </div>
  </div>
  <div class="errorPageDetailsContainer">
    <h3>Error message sent!</h3>
  </div>
  <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
</body>
</html>