<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="students.createStudent.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/students/createstudent.js">
    </script>
    
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="students.createStudent.pageTitle" /></h1>
    <div id="createStudentCreateFormContainer"> 
      <div class="genericFormContainer">    
        <form action="createstudent.json" method="post" ix:jsonform="true" ix:useglasspane="true">  

          <input type="hidden" name="personId" value="${person.id}" />

          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="students.createStudent.studentBasicInfoTabLabel"/>
            </a>
            <a class="tabLabel" href="#createStudent">
              <fmt:message key="students.createStudent.tabLabelCreateStudent"/>
            </a>
            <ix:extensionHook name="students.createStudent.tabLabels"/>
          </div>
          
          <div id="basic" class="tabContent">
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.birthdayTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.birthdayHelp"/>
              </jsp:include>   
              <input type="text" name="birthday" class="ixDateField" value="${person.birthday.time}"/>
            </div>
      
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.ssecIdTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.ssecIdHelp"/>
              </jsp:include>   
              <input type="text" name="ssecId" size="15" class="mask" data-validatemask="^([0-9]{6})[-A]([0-9A-Z]{4})$" value="${person.socialSecurityNumber}">
            </div>
      
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.genderTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.genderHelp"/>
              </jsp:include>                             
              <select name="gender">
                <option value="MALE" ${person.sex == 'MALE' ? 'selected="selected"' : ''}><fmt:message key="generic.genders.male"/></option>
                <option value="FEMALE" ${person.sex == 'FEMALE' ? 'selected="selected"' : ''}><fmt:message key="generic.genders.female"/></option>
                <option value="OTHER" ${person.sex == 'OTHER' ? 'selected="selected"' : ''}><fmt:message key="generic.genders.other"/></option>
              </select>
            </div>

            <div class="genericFormSection">       
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.secureInfoTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.secureInfoHelp"/>
              </jsp:include>
              <c:choose>
                <c:when test="${person.secureInfo}"><input type="checkbox" name="secureInfo" value="true" checked="checked"/></c:when>
                <c:otherwise><input type="checkbox" name="secureInfo" value="true"/></c:otherwise>              
              </c:choose>
              <fmt:message key="students.createStudent.secureInfoCheckboxLabel"/>
            </div>

            <div class="genericFormSection">         
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.personBasicInfoTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.personBasicInfoHelp"/>
              </jsp:include>            
              <textarea name="basicInfo" ix:cktoolbar="studentAdditionalInformation" ix:ckeditor="true">${person.basicInfo}</textarea>
            </div>
            <ix:extensionHook name="students.createStudent.tabs.basic"/>
          </div>

          <div id="createStudent" class="tabContent">

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.studyProgrammeTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.studyProgrammeHelp"/>
              </jsp:include>                                           
              <select class="required" name="studyProgramme">
                <option></option>           
                <c:forEach var="studyProgramme" items="${studyProgrammes}">
                  <option value="${studyProgramme.id}">${studyProgramme.name}</option> 
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.curriculumTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.curriculumHelp"/>
              </jsp:include>                                           
              <select name="curriculum">
                <option></option>           
                <c:forEach var="curriculum" items="${curriculums}">
                  <option value="${curriculum.id}">${curriculum.name}</option> 
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.firstNameTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.firstNameHelp"/>
              </jsp:include>                 
              <input type="text" name="firstName" size="20" class="required" value="${staffMember.firstName}">
            </div>
            
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.lastNameTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.lastNameHelp"/>
              </jsp:include>                 
              <input type="text" name="lastName" size="30" class="required" value="${staffMember.lastName}">
            </div>
              
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.nicknameTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.nicknameHelp"/>
              </jsp:include>                                           
              <input type="text" name="nickname" size="30">
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.tagsTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
      
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.addressesTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.addressesHelp"/>
              </jsp:include>                                         
              <div id="addressTable"></div>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.emailTableEmailsTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.emailTableEmailsHelp"/>
              </jsp:include>                                         
              <div id="emailTable"></div>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.phoneNumbersTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.phoneNumbersHelp"/>
              </jsp:include>                                         
              <div id="phoneTable"></div>
            </div>

            <div class="genericFormSection">                                  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.otherContactInfoTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.otherContactInfoInfoHelp"/>
              </jsp:include>
              <textarea name="otherContactInfo" rows="5" cols="50"></textarea>
            </div>
      
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.municipalityTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.municipalityHelp"/>
              </jsp:include>                                           
              <select name="municipality">           
                <option></option>
                <c:forEach var="municipality" items="${municipalities}">
                  <option value="${municipality.id}">${municipality.name}</option> 
                </c:forEach>
              </select>
            </div>
      
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.languageTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.languageHelp"/>
              </jsp:include>                                           
              <select name="language">           
                <option></option>
                <c:forEach var="language" items="${languages}">
                  <option value="${language.id}">${language.name}</option> 
                </c:forEach>
              </select>
            </div>
      
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.nationalityTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.nationalityHelp"/>
              </jsp:include>                                           
              <select name="nationality">           
                <option></option>
                <c:forEach var="nationality" items="${nationalities}">
                  <option value="${nationality.id}">${nationality.name}</option> 
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.activityTypeTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.activityTypeHelp"/>
              </jsp:include>                                           
              <select name="activityType">
                <option></option>           
                <c:forEach var="activityType" items="${activityTypes}">
                  <option value="${activityType.id}">${activityType.name}</option> 
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.examinationTypeTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.examinationTypeHelp"/>
              </jsp:include>                                           
              <select name="examinationType">
                <option></option>           
                <c:forEach var="examinationType" items="${examinationTypes}">
                  <option value="${examinationType.id}">${examinationType.name}</option> 
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.educationalLevelTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.educationalLevelHelp"/>
              </jsp:include>                                           
              <select name="educationalLevel">
                <option></option>           
                <c:forEach var="educationalLevel" items="${educationalLevels}">
                  <option value="${educationalLevel.id}">${educationalLevel.name}</option> 
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.schoolTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.schoolHelp"/>
              </jsp:include>                                           
              <select name="school">
                <option></option>           
                <c:forEach var="school" items="${schools}">
                  <option value="${school.id}">${school.name}</option> 
                </c:forEach>
              </select>
            </div>
            
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.educationTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.educationHelp"/>
              </jsp:include>                                           
              <input type="text" name="education" size="50"/>
            </div>
              
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.previousStudiesTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.previousStudiesHelp"/>
              </jsp:include>                                           
              <input type="text" name="previousStudies" size="5" class="float"/>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.studyTimeEndTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.studyTimeEndHelp"/>
              </jsp:include>                                           
              <input type="text" name="studyTimeEnd" class="ixDateField"/>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.studyStartDateTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.studyStartDateHelp"/>
              </jsp:include>                                           
              <input type="text" name="studyStartDate" class="ixDateField"/>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.studyEndDateTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.studyEndDateHelp"/>
              </jsp:include>                                           
              <input type="text" name="studyEndDate" class="ixDateField"/>
            </div>
            
            <div class="genericFormSection">      
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.studyEndReasonTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.studyEndReasonHelp"/>
              </jsp:include>
              <select name="studyEndReason">
                <option></option>  
                <c:forEach var="reason" items="${studyEndReasons}">
                  <c:choose>
                    <c:when test="${reason.id eq student.studyEndReason.id}">
                      <option value="${reason.id}" selected="selected">${reason.name}</option> 
                    </c:when>
                    <c:otherwise>
                      <option value="${reason.id}">${reason.name}</option> 
                    </c:otherwise>
                  </c:choose>
  
                  <c:if test="${fn:length(reason.childEndReasons) gt 0}">
                    <optgroup>
                      <c:forEach var="childReason" items="${reason.childEndReasons}">
                        <c:choose>
                          <c:when test="${childReason.id eq student.studyEndReason.id}">
                            <option value="${childReason.id}" selected="selected">${childReason.name}</option> 
                          </c:when>
                          <c:otherwise>
                            <option value="${childReason.id}">${childReason.name}</option> 
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>
                  </optgroup>
                  </c:if>
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">    
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.studyEndTextTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.studyEndTextHelp"/>
              </jsp:include>
              <input type="text" name="studyEndText" size="50">
            </div>
            
            <div class="genericFormSection">    
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.lodgingTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.lodgingHelp"/>
              </jsp:include>
              
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" onclick="addLodgingPeriodTableRow(getIxTableById('lodgingPeriodsTable'));"><fmt:message key="students.createStudent.addLodgingPeriodLink"/></span>
              </div>

              <div id="lodgingPeriodsTableContainer"></div>
            </div>

            <c:choose>
              <c:when test="${fn:length(variableKeys) gt 0}">
                <div class="genericFormSection">      
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.createStudent.variablesTitle"/>
                    <jsp:param name="helpLocale" value="students.createStudent.variablesHelp"/>
                  </jsp:include>
                  <div id="variablesTableContainer"></div>
                </div>
              </c:when>
            </c:choose>

            <div class="genericFormSection">        
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudent.additionalInformationTitle"/>
                <jsp:param name="helpLocale" value="students.createStudent.additionalInformationHelp"/>
              </jsp:include>
              <textarea name="additionalInfo" ix:cktoolbar="studentAdditionalInformation" ix:ckeditor="true"></textarea>
            </div>

            <ix:extensionHook name="students.createStudent.tabs.studyProgramme"/>
          </div>
          
          <ix:extensionHook name="students.createStudent.tabs"/>

          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" name="login" class="formvalid" value="<fmt:message key="students.createStudent.createButton"/>">
          </div>
        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>