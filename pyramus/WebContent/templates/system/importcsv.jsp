<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
	<head>
	  <title><fmt:message key="system.importcsv.pageTitle"/></title>
	  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      }
    </script>
  </head>
  <body onload="onLoad(event)">
	  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
	  <h1 class="genericPageHeader"><fmt:message key="system.importcsv.pageTitle" /></h1>
	  
	  <div id="importDataImportFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#importData">
            <fmt:message key="system.importcsv.tabLabelImportData"/>
          </a>
        </div>
        
        <div id="importCSV" class="tabContent">
	    	  <form action="importcsv.page" enctype="multipart/form-data" method="post">
	    	    <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="system.importcsv.fileTitle"/>
                <jsp:param name="helpLocale" value="system.importcsv.fileHelp"/>
              </jsp:include>          
              <input type="file" name="file">
            </div>
	    	      
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="system.importcsv.entityTitle"/>
                <jsp:param name="helpLocale" value="system.importcsv.entityHelp"/>
              </jsp:include>
              <select name="importStrategy">
                <option value=""><fmt:message key="system.importcsv.selectOneOptionCaption"/></option>
                <option value="COURSE"><fmt:message key="system.importcsv.optionCourse" /></option>
                <option value="STUDENT"><fmt:message key="system.importcsv.optionStudent" /></option>
                <option value="COURSESTUDENT"><fmt:message key="system.importcsv.optionCourseStudent" /></option>
              </select>          
            </div>

	    	    <div class="genericFormSubmitSection">
	    	      <input type="submit" value="<fmt:message key="system.importcsv.importButton"/>">
	    	    </div>
	    	  </form>
	    	</div>
	    	
	    	<div>
          <c:choose>
	    	    <c:when test="${importStrategy eq 'STUDENT'}">
	    	      <table>
	    	        <tr align="left">
	    	          <th><fmt:message key="system.importcsv.results.studentId" /></th>
                  <th><fmt:message key="system.importcsv.results.studentSSN" /></th>
                  <th><fmt:message key="system.importcsv.results.studentName" /></th>
	    	        </tr>
			    	    <c:forEach var="student" items="${entityClassEntities}">
			    	      <tr>
			    	        <td>${student.id}</td>
		                <td>${student.abstractStudent.socialSecurityNumber}</td>
		                <td>${student.fullName}</td>
			    	      </tr>
			    	    </c:forEach>
		    	    </table>
		    	  </c:when>
            <c:when test="${importStrategy eq 'COURSE'}">
              <table>
                <tr align="left">
                  <th><fmt:message key="system.importcsv.results.courseId" /></th>
                  <th><fmt:message key="system.importcsv.results.courseName" /></th>
                </tr>
		            <c:forEach var="course" items="${entityClassEntities}">
		              <tr>
		                <td>${course.id}</td>
		                <td>${course.name}</td>
		              </tr>
		            </c:forEach>
              </table>
            </c:when>
            <c:when test="${importStrategy eq 'COURSESTUDENT'}">
              <table>
                <tr align="left">
                  <th><fmt:message key="system.importcsv.results.courseStudentStudentName" /></th>
                  <th><fmt:message key="system.importcsv.results.courseStudentCourseName" /></th>
                </tr>
                <c:forEach var="coursestudent" items="${entityClassEntities}">
                  <tr>
                    <td>${coursestudent.student.fullName}</td>
                    <td>${coursestudent.course.name}</td>
                  </tr>
                </c:forEach>
              </table>
            </c:when>
		    	</c:choose>
	    	</div>
      </div>
	  </div>
	  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>