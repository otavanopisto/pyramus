<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/studentinfopopup_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/glasspane_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabs = new IxProtoTabs($('tabContainer'));
      }
    </script>

  </head>
  <body onload="onLoad(event);">

    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="tabContainer">
        <a class="tabLabel" href="#ids"><fmt:message key="terms.studyProgramme" /></a>
      </div>

      <div id="ids" class="tabContent">
        <form id="changeStudentStudyProgrammeForm">
          <input type="hidden" name="studentId" value="${student.id}"/>
          
          <div class="genericFormSection">           
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="terms.studyProgramme"/>
            </jsp:include>            
            <select class="required" name="studyProgrammeId">
              <c:forEach var="studyProgramme" items="${studyProgrammes}">
                <c:choose>
                  <c:when test="${studyProgramme.id eq student.studyProgramme.id}">
                    <option value="${studyProgramme.id}" selected="selected">${studyProgramme.name}</option> 
                  </c:when>
                  <c:otherwise>
                    <option value="${studyProgramme.id}">${studyProgramme.name}</option> 
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              <c:if test="${student.studyProgramme.archived}">
                <option value="${student.studyProgramme.id}" selected="selected">${student.studyProgramme.name}*</option>
              </c:if>
            </select>
          </div>
	      </form>
      </div>
      
    </div>

  </body>
</html>