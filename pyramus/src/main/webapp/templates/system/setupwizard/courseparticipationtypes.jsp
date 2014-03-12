<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<t:setupwizard-template>
  <jsp:attribute name="script">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/system/setupwizard/courseparticipationtypes.js"></script>
  </jsp:attribute>
  <jsp:body>
  
    <form method="post" action="">
      <div id="courseParticipationTypes" class="tabContent">
        <div class="genericTableAddRowContainer">
          <span class="genericTableAddRowLinkContainer" onclick="addCourseParticipationTypesTableRow();"><fmt:message key="system.setupwizard.courseparticipationtypes.addNew" /></span>
        </div>
        <div id="courseParticipationTypesTable">
          <div id="noCourseParticipationTypesAddedMessageContainer">
            <fmt:message key="system.setupwizard.courseparticipationtypes.addNew" />
          </div>
        </div>
      </div>

      <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" name="next" value="<fmt:message key="system.setupwizard.next"/>">
        <input type="submit" name="previous" value="<fmt:message key="system.setupwizard.previous"/>">
      </div>
    </form>
    
  </jsp:body>
</t:setupwizard-template>