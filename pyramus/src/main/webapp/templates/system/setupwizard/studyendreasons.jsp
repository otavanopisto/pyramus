<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<t:setupwizard-template>
  <jsp:attribute name="script">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/system/setupwizard/studyendreasons.js"></script>
  </jsp:attribute>
  <jsp:body>
        <form action="" method="post" >
          
          <div id="manageStudyEndReasons" class="tabContentixTableFormattedData">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addStudyEndReasonsTableRow();"><fmt:message key="settings.studyEndReasons.addStudyEndReasonLink"/></span>
            </div>
              
            <div id="noStudyEndReasonsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span>
               <fmt:message key="settings.studyEndReasons.noStudyEndReasonsAddedPrefix"/> <span onclick="addStudyEndReasonsTableRow();" class="genericTableAddRowLink"> <fmt:message key="settings.studyEndReasons.noStudyEndReasonsAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="studyEndReasonsTableContainer"></div>
          </div>
    
      <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" name="next" value="<fmt:message key="system.setupwizard.next"/>">
      </div>
    </form>
    
  </jsp:body>
</t:setupwizard-template>