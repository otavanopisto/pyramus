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
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/studentinfopopup_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/glasspane_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/students/editstudentsubjectgradesdialog.js"></script>

    <style type="text/css">
      .settingbox {
        align-items: center;
        background-color: #E0E9F8; 
        border-top: 1px solid #D1DFF8; 
        border-radius: 5px; 
        display: flex;
        justify-content: space-between; 
        margin: 4px 1px 8px 0px;      
        padding: 4px;
      }

      .settingbox_column {
      }
    </style>
  </head>
  
  <body onload="editstudentsubjectgradesdialog_onLoad(event);">
    <div class="genericFormContainer">
      <form id="editStudentSubjectGradeForm" action="editstudentsubjectgrades.json" method="POST" ix:jsonform="true">
        <input type="hidden" name="studentId" value="${student.id}"/>
        <input type="hidden" name="subjectId" value="${subject.id}"/>
      
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="students.editStudentSubjectGradeDialog.studentNameTitle" />
            <jsp:param name="helpLocale" value="students.editStudentSubjectGradeDialog.studentNameHelp" />
          </jsp:include>
          <div>${student.firstName} ${student.lastName}</div>
        </div>

        <div class="settingbox">
          <span class="settingbox_column">
            <input type="checkbox" name="autoFillOnEdit" id="autoFillOnEdit" checked="checked"/>
            <label for="autoFillOnEdit"><fmt:message key="students.editStudentSubjectGradesDialog.autofillCheckboxLabel"/></label>
          </span>

          <span class="settingbox_column">
            <span class="genericFormSection">      
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudentSubjectGradesDialog.gradeApproverTitle"/>
                <jsp:param name="helpLocale" value="students.editStudentSubjectGradesDialog.gradeApproverHelp"/>
              </jsp:include>
    
              <select id="defaultGradeApproverId" name="defaultGradeApproverId" class="required">
                <c:forEach var="studyApprover" items="${studyApprovers}">
                  <c:choose>
                    <c:when test="${studyApprover.title eq null}">
                      <c:set var="approverName">${studyApprover.lastName}, ${studyApprover.firstName}</c:set>
                    </c:when>
                    <c:otherwise>
                      <c:set var="approverName">${studyApprover.lastName}, ${studyApprover.firstName} (${studyApprover.title})</c:set>
                    </c:otherwise>
                  </c:choose>
    
                  <c:choose>
                    <c:when test="${studyApprover.id eq studentSubjectGrade.gradeApprover.id}">
                      <option value="${studyApprover.id}" selected="selected">${approverName}</option> 
                    </c:when>
                    <c:otherwise>
                      <option value="${studyApprover.id}">${approverName}</option> 
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </span>
          </span>
          
          <span class="settingbox_column">
            <button type="button" onclick="editstudentsubjectgradesdialog_activateAllCompletedSubjects(event);">
              <fmt:message key="students.editStudentSubjectGradesDialog.editCompletedSubjectsButton"/>
            </button>
          </span>
        </div>      

        <div id="studentSubjectGradesTableContainer"></div>
        
        <button type="submit"><fmt:message key="generic.form.saveButton"/></button>
      </form>
    </div>

  </body>
</html>