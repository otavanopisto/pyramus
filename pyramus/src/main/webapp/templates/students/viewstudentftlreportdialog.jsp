<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <!-- Adds some generic styling, otherwise not really even needed.. -->
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
  </head>
  
  <body>
    <div class="genericFormContainer">
      <h3>
        ${student.fullName} - ${report.name}
      </h3>

      <iframe src="${pageContext.request.contextPath}/1/students/students/${student.id}/reports/${report.id}?format=pdf" width="100%" height="580px">
      </iframe>
    </div>

  </body>
</html>