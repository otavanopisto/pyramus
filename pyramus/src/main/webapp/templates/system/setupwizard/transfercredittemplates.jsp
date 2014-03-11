<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:setupwizard-template>
<jsp:attribute name="script">
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/system/setupwizard/transfercredittemplates.js"></script>
</jsp:attribute>
<jsp:body>
<form method="post" action="">
            <div class="tabLabelsContainer" id="tabs">
              <a class="tabLabel" href="#transferCreditTemplate">
                <fmt:message key="settings.createTransferCreditTemplate.tabLabelTransferCreditTemplate"/>
              </a>
            </div>
          
          <div id="transferCreditTemplate" class="tabContentixTableFormattedData">
          
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="settings.createTransferCreditTemplate.nameTitle"/>
                  <jsp:param name="helpLocale" value="settings.createTransferCreditTemplate.nameHelp"/>
                </jsp:include>
                        
                <input type="text" name="name" class="required" size="40">
              </div>
          
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addCoursesTableRow();"><fmt:message key="settings.createTransferCreditTemplate.addCourseLink"/></span>
            </div>
              
            <div id="noCoursesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="settings.createTransferCreditTemplate.noCoursesAddedPreFix"/> <span onclick="addCoursesTableRow();" class="genericTableAddRowLink"><fmt:message key="settings.createTransferCreditTemplate.noCoursesAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="coursesTable"></div>
          </div>

    <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" name="next" value="<fmt:message key="system.setupwizard.next"/>">
    </div>
</form>
</jsp:body>
</t:setupwizard-template>
