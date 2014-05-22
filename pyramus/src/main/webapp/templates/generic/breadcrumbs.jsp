<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${breadcrumbHandler.size>0}">
  <div>
    <a href="${pageContext.request.contextPath}/index.page?resetbreadcrumb=1"><fmt:message key="generic.navigation.index" /></a>
    <c:forEach var="breadcrumb" items="${breadcrumbHandler.breadcrumbs}">
      &nbsp;&gt;&nbsp;
      <a href="${breadcrumb.url}">${breadcrumb.name}</a>
    </c:forEach>
  </div>
</c:if>