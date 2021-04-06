<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title></title>
    <jsp:include page="/templates/generic/dialoghead_generic.jsp"></jsp:include>
  </head>
  <body>
    <div class="simpleDialogContainer">
      <b><fmt:message key="worklist.listWorklistItems.student"/>:</b> ${student}<br/>
      <b><fmt:message key="worklist.listWorklistItems.course"/>:</b> ${course}<br/>
      <b><fmt:message key="worklist.listWorklistItems.grade"/>:</b> ${grade}<br/>
      <b><fmt:message key="worklist.listWorklistItems.raise"/>:</b> ${raise}<br/>
    </div>
  </body>
</html>