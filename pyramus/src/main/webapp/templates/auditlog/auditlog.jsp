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

  <table border="1">
    <tr>
      <td><b>Date</b></td>
      <td><b>Author</b></td>
      <td><b>Person</b></td>
      <td><b>User</b></td>
      <td><b>Entity</b></td>
      <td><b>Id</b></td>
      <td><b>Type</b></td>
      <td><b>Field</b></td>
      <td><b>Value</b></td>
    </tr>
    <c:forEach var="e" items="${entries}">
      <tr>
        <td>${e.date}</td>
        <td>${e.authorId}</td>
        <td>${e.personId}</td>
        <td>${e.userId}</td>
        <td>${e.className}</td>
        <td>${e.entityId}</td>
        <td>${e.type}</td>
        <td>${e.field}</td>
        <td>${e.data}</td>
      </tr>
    </c:forEach>
  </table>

</body>
</html>