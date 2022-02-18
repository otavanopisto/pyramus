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

  <p><b>Date</b> | <b>Author</b> | <b>Person</b> | <b>User</b> | <b>Type</b> | <b>Entity</b> | <b>Entity id</b> | <b>Field</b> | <b>Value</b></p>
  <c:forEach var="e" items="${entries}">
    <p>${e.date} | ${e.authorId} | ${e.personId} | ${e.userId} | ${e.type} | ${e.className} | ${e.entityId} | ${e.field} | ${e.data}</p>
  </c:forEach>

</body>
</html>