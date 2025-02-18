<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="fi.otavanopisto.pyramus.domainmodel.users.Role" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="studentparents.createStudentParentRegistration.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/glasspane_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      }
      
      function getResults() {
        return {
          studentId: $('studentId').value,
          invitationId: $('invitationId').value,
          firstName: $('firstName').value,
          lastName: $('lastName').value,
          email: $('email').value
        };
      }
    </script>
  </head>

  <body onload="onLoad(event);">
    <div> 
      <div class="genericFormContainer"> 

        <form action="editstudentparentinvitation.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="studentparents.createStudentParentRegistration.pageTitle"/>
            </a>
          </div>
    
          <div id="basic" class="tabContent">    
            <input type="hidden" id="studentId" name="studentId" value="${student.id}"/>
            <input type="hidden" id="invitationId" name="invitationId" value="${invitation.id}"/>
            
            <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="studentparents.createStudentParentRegistration.studentTitle"/>
                <jsp:param name="helpLocale" value="studentparents.createStudentParentRegistration.studentHelp"/>
              </jsp:include>
              <div>${student.fullName}</div>
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="studentparents.createStudentParentRegistration.firstNameTitle"/>
                <jsp:param name="helpLocale" value="studentparents.createStudentParentRegistration.firstNameHelp"/>
              </jsp:include>
              <input type="text" id="firstName" name="firstName" size="20" class="required" value="${invitation.firstName}">
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="studentparents.createStudentParentRegistration.lastNameTitle"/>
                <jsp:param name="helpLocale" value="studentparents.createStudentParentRegistration.lastNameHelp"/>
              </jsp:include>
              <input type="text" id="lastName" name="lastName" size="30" class="required" value="${invitation.lastName}">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="studentparents.createStudentParentRegistration.emailTitle"/>
                <jsp:param name="helpLocale" value="studentparents.createStudentParentRegistration.emailNameHelp"/>
              </jsp:include>
              <input type="text" id="email" name="email" size="30" class="required email" value="${invitation.email}">
            </div>

          </div>
        </form>
      </div>
    </div>  

  </body>
</html>