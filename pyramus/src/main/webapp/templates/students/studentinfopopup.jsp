<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<div class="studentInfoPopupContainer">
  <div class="studentInfoPopupImageContainer">
    <c:choose>
      <c:when test="${latestStudentHasImage}">
        <img class="studentInfoPopupImage" src="${pageContext.request.contextPath}/students/viewstudentimage.binary?studentId=${person.latestStudent.id}"/> 
      </c:when>
      <c:otherwise>
        <img class="studentInfoPopupImage" src="${studentImage}"/> 
      </c:otherwise>
    </c:choose>
  </div>
  <div class="studentInfoPopupDetailsContainer">
    <div class="studentInfoPopupName">
      <c:choose>
        <c:when test="${fn:length(person.latestStudent.fullName) ge 30}">
          <c:out value="${fn:substring(person.latestStudent.fullName, 0, 27)}..."/>
        </c:when>
        <c:otherwise>
          <c:out value="${person.latestStudent.fullName}"/>
        </c:otherwise>
      </c:choose>
    </div>
    <div class="studentInfoPopupMail"><c:choose><c:when test="${fn:length(person.latestStudent.primaryEmail.address) ge 40}"><c:out value="${fn:substring(person.latestStudent.primaryEmail.address, 0, 37)}..."/></c:when><c:otherwise><c:out value="${person.latestStudent.primaryEmail.address}"/></c:otherwise></c:choose></div>
    <div class="studentInfoPopupSSNContainer">
      <div class="studentInfoPopupSSNTitle"><fmt:message key="students.studentInfoPopup.socialSecurityNumberLabel"/></div>
      <div class="studentInfoPopupSSNValue">
        <c:choose>
          <c:when test="${fn:length(person.socialSecurityNumber) ge 30}">
            <c:out value="${fn:substring(person.socialSecurityNumber, 0, 27)}..."/>
          </c:when>
          <c:otherwise>
            <c:out value="${person.socialSecurityNumber}"/>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
    <div class="studentInfoPopupPhoneNumberContainer">
      <div class="studentInfoPopupPhoneNumberTitle"><fmt:message key="students.studentInfoPopup.phoneNumberLabel"/></div>
      <div class="studentInfoPopupPhoneNumberValue">
        <c:choose>
          <c:when test="${person.latestStudent.defaultPhone.number eq ''}">
            -
          </c:when>
          <c:otherwise>
            <c:choose>
              <c:when test="${fn:length(person.latestStudent.defaultPhone.number) ge 30}">
                <c:out value="${fn:substring(person.latestStudent.defaultPhone.number, 0, 27)}..."/>
              </c:when>
              <c:otherwise>
                <c:out value="${person.latestStudent.defaultPhone.number}"/>
              </c:otherwise>
            </c:choose>
          </c:otherwise>
        </c:choose>
      </div>
    </div> 

    <c:set var="activeStudyProgrammes"/>
    <c:set var="archivedStudyProgrammes"/>
    
    <c:forEach var="student" items="${students}" varStatus="sl">
      <c:if test="${student.studyProgramme != null}">
        <c:choose>
          <c:when test="${!student.hasFinishedStudies}">
            <c:if test="${fn:length(activeStudyProgrammes) gt 0}">
              <c:set var="activeStudyProgrammes">${activeStudyProgrammes},&nbsp;</c:set>
            </c:if>
            <c:set var="activeStudyProgrammes">${activeStudyProgrammes}${student.studyProgramme.name}</c:set>
          </c:when>
          <c:otherwise>
            <c:if test="${fn:length(archivedStudyProgrammes) gt 0}">
              <c:set var="archivedStudyProgrammes">${archivedStudyProgrammes},&nbsp;</c:set>
            </c:if>
            <c:set var="archivedStudyProgrammes">${archivedStudyProgrammes}${student.studyProgramme.name}</c:set>
          </c:otherwise>
        </c:choose>
      </c:if>
    </c:forEach>

    <div class="studentInfoPopupStudyProgrammeContainer">
      <div class="studentInfoPopupStudyProgrammeTitle"><fmt:message key="students.studentInfoPopup.studyProgrammeLabel"/></div>
      <div class="studentInfoPopupStudyProgrammeValue">
<!-- TODO: max pituus -->
        ${activeStudyProgrammes}
<!--
 
        <c:choose>
          <c:when test="${fn:length(activeStudyProgrammes) ge 30}">
            ${fn:substring(activeStudyProgrammes, 0, 27)}...
          </c:when>
          <c:otherwise>
            ${activeStudyProgrammes}
          </c:otherwise>
        </c:choose>
-->
      </div>
    </div> 
    <div class="studentInfoPopupArchivedStudyProgrammeContainer">
      <div class="studentInfoPopupArchivedStudyProgrammeTitle"><fmt:message key="students.studentInfoPopup.archivedStudyProgrammeLabel"/></div>
      <div class="studentInfoPopupArchivedStudyProgrammeValue">
<!-- TODO: max pituus -->
        ${archivedStudyProgrammes}
      </div>
    </div> 
    <div class="studentInfoPopupNationalityContainer">
      <div class="studentInfoPopupNationalityTitle"><fmt:message key="students.studentInfoPopup.nationalityLabel"/></div>
      <div class="studentInfoPopupNationalityValue">
        <c:choose>
          <c:when test="${person.latestStudent.nationality == null}">
            -
          </c:when>
          <c:otherwise>
            <c:choose>
              <c:when test="${fn:length(person.latestStudent.nationality.name) ge 30}">
                <c:out value="${fn:substring(person.latestStudent.nationality.name, 0, 27)}..."/>
              </c:when>
              <c:otherwise>
                <c:out value="${person.latestStudent.nationality.name}"/>
              </c:otherwise>
            </c:choose>
          </c:otherwise>
        </c:choose>
      </div>
    </div> 
    <div class="studentInfoPopupLanguageContainer">
      <div class="studentInfoPopupLanguageTitle"><fmt:message key="students.studentInfoPopup.languageLabel"/></div>
      <div class="studentInfoPopupLanguageValue">
        <c:choose>
          <c:when test="${person.latestStudent.language == null}">
            -
          </c:when>
          <c:otherwise>
            <c:choose>
              <c:when test="${fn:length(person.latestStudent.language.name) ge 30}">
                <c:out value="${fn:substring(person.latestStudent.language.name, 0, 27)}..."/>
              </c:when>
              <c:otherwise>
                <c:out value="${person.latestStudent.language.name}"/>
              </c:otherwise>
            </c:choose>
          </c:otherwise>
        </c:choose>
      </div>
    </div> 
  </div>
</div>