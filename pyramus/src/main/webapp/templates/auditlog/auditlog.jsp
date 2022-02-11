<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Audishoooooooooon!</title>
</head>
<body>

  <c:forEach var="item" items="${items}">
    <p>${item.date} | ${item.user} | ${item.target} | ${item.type} | ${item.id} | ${item.data}</p>
  </c:forEach>

</body>
</html>