<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>

<html>
  <head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css"/>
  </head>
  <body>
    <main class="application">
      <div><a href="/applications/create.page?line=aineopiskelu">Aineopiskelu</a></div>
      <div><a href="/applications/create.page?line=nettilukio">Nettilukio</a></div>
      <div><a href="/applications/create.page?line=nettipk">Nettiperuskoulu</a></div>
      <div><a href="/applications/create.page?line=aikuislukio">Aikuislukio</a></div>
      <div><a href="/applications/create.page?line=mk">Maahanmuuttajakoulutukset</a></div>
    </main>
  </body>
</html>