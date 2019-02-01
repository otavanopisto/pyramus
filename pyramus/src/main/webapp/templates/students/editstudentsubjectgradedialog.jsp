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
  </head>
  
  <body>
    <div class="genericFormContainer">
      <form id="editStudentSubjectGradeForm">
        <input type="hidden" name="studentId" value="${student.id}"/>
        <input type="hidden" name="subjectId" value="${subject.id}"/>

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="students.editStudentSubjectGradeDialog.studentNameTitle" />
            <jsp:param name="helpLocale" value="students.editStudentSubjectGradeDialog.studentNameHelp" />
          </jsp:include>
          <div>${student.firstName} ${student.lastName}</div>
        </div>
        
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="students.editStudentSubjectGradeDialog.subjectNameTitle" />
            <jsp:param name="helpLocale" value="students.editStudentSubjectGradeDialog.subjectNameHelp" />
          </jsp:include>
          <div>${subject.name}</div>
        </div>
        
        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="students.editStudentSubjectGradeDialog.gradeTitle"/>
            <jsp:param name="helpLocale" value="students.editStudentSubjectGradeDialog.gradeTitle"/>
          </jsp:include>
          
          <select name="gradeId">
            <option value=""><fmt:message key="students.editStudentSubjectGradeDialog.defaultGradeTitle"/></option>
          
            <c:forEach var="gradingScale" items="${gradingScales}">
              <optgroup label="${fn:escapeXml(gradingScale.name)}">
                <c:forEach var="grade" items="${gradingScale.grades}">
                  <c:choose>
                    <c:when test="${studentSubjectGrade.grade.id eq grade.id}">
                      <option value="${grade.id}" selected="selected">${fn:escapeXml(grade.name)}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${grade.id}">${fn:escapeXml(grade.name)}</option>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </optgroup>
            </c:forEach>
          </select>
        </div>
        
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="students.editStudentSubjectGradeDialog.explanationTitle" />
            <jsp:param name="helpLocale" value="students.editStudentSubjectGradeDialog.explanationHelp" />
          </jsp:include>
          <textarea name="explanation" cols="60" rows="5">${studentSubjectGrade.explanation}</textarea>
        </div>
        
      </form>
    </div>

  </body>
</html>