<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<div class="studentCourseAssessmentRequestsPopupContainer">

  <div class="genericFormSection">
    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
      <jsp:param name="titleLocale" value="students.studentCourseAssessmentRequestsPopup.studentNameTitle" />
      <jsp:param name="helpLocale" value="students.studentCourseAssessmentRequestsPopup.studentNameHelp" />
    </jsp:include>
    <div>${courseStudent.student.fullName}</div>
  </div>

  <div class="genericFormSection">
    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
      <jsp:param name="titleLocale" value="students.studentCourseAssessmentRequestsPopup.courseNameTitle" />
      <jsp:param name="helpLocale" value="students.studentCourseAssessmentRequestsPopup.courseNameHelp" />
    </jsp:include>
    <div>${courseStudent.course.name}</div>
  </div>

  <div class="genericFormSection">
    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
      <jsp:param name="titleLocale" value="students.studentCourseAssessmentRequestsPopup.requestListTitle" />
      <jsp:param name="helpLocale" value="students.studentCourseAssessmentRequestsPopup.requestListHelp" />
    </jsp:include>
    <div class="studentCourseAssessmentRequestContainer">
      <c:forEach var="assessmentRequest" items="${courseAssessmentRequests}">
        <div class="studentCourseAssessmentRequest">
          <div><fmt:formatDate value="${assessmentRequest.created}" type="both"/></div>
          <div>${assessmentRequest.requestText}</div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>