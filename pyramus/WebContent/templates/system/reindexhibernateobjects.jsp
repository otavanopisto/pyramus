<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
	<meta http-equiv="refresh" content="0">
  </head>
  <body onload="onLoad(event)">
    <div>Pending tasks:</div>
    <c:forEach var="pendingIndexingTask" items="${pendingIndexingTasks}">
      <div>${pendingIndexingTask.name}</div>
    </c:forEach>
  </body>
</html>