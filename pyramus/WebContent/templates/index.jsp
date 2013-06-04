<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
  <title><fmt:message key="generic.applicationTitle" /></title>
  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
  <link href="${pageContext.request.contextPath}/css/index.css" rel="stylesheet">
</head>

<body>
  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
  <!-- 
    <a href="#" onclick="setLocale('fi_FI');">FI</a><a href="#" onclick="setLocale('en_US');">EN</a>
  -->

  <div class="index_row">
    <div class="index_cell">
      <img src="${pageContext.request.contextPath}/gfx/index/users.png" class="index_cell_image"/>
      <div class="index_cell_title"><fmt:message key="generic.index.studentsTitle"/></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/students/searchstudents.page?resetbreadcrumb=1"><fmt:message key="generic.index.searchStudents"/></a></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/students/createstudent.page?resetbreadcrumb=1"><fmt:message key="generic.index.createStudent"/></a></div>
    </div>
    <div class="index_cell">
      <img src="${pageContext.request.contextPath}/gfx/index/courses.png" class="index_cell_image"/>
      <div class="index_cell_title"><fmt:message key="generic.index.coursesTitle"/></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/courses/searchcourses.page?resetbreadcrumb=1"><fmt:message key="generic.index.searchCourses"/></a></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/courses/createcoursewizard.page?resetbreadcrumb=1"><fmt:message key="generic.index.createCourse"/></a></div>
    </div>
  </div>

  <div class="index_row">
    <div class="index_cell">
      <img src="${pageContext.request.contextPath}/gfx/index/modules.png" class="index_cell_image"/>
      <div class="index_cell_title"><fmt:message key="generic.index.modulesTitle"/></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/modules/searchmodules.page?resetbreadcrumb=1"><fmt:message key="generic.index.searchModules"/></a></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/modules/createmodule.page?resetbreadcrumb=1"><fmt:message key="generic.index.createModule"/></a></div>
    </div>
    <div class="index_cell">
      <img src="${pageContext.request.contextPath}/gfx/index/projects.png" class="index_cell_image"/>
      <div class="index_cell_title"><fmt:message key="generic.index.projectsTitle"/></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/projects/searchprojects.page?resetbreadcrumb=1"><fmt:message key="generic.index.searchProjects"/></a></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/projects/createproject.page?resetbreadcrumb=1"><fmt:message key="generic.index.createProject"/></a></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/projects/searchstudentprojects.page?resetbreadcrumb=1"><fmt:message key="generic.index.searchStudentProjects"/></a></div>
      <div><a class="index_cell_link" href="${pageContext.request.contextPath}/projects/createstudentproject.page?resetbreadcrumb=1"><fmt:message key="generic.index.createStudentProject"/></a></div>
    </div>
  </div>

  <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
</body>
</html>