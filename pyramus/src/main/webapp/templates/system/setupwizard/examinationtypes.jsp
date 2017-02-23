<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<t:setupwizard-template>
  <jsp:attribute name="script">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/system/setupwizard/examinationtypes.js"></script>
  </jsp:attribute>
  <jsp:body>
  
    <form method="post" action="" ix:pageform="true">
      <div id="educationTypes" class="tabContent">
        <div class="genericTableAddRowContainer">
          <span class="genericTableAddRowLinkContainer" onclick="addExaminationTypesTableRow();"><fmt:message key="system.setupwizard.examinationtypes.addNew" /></span>
        </div>
        <div id="examinationTypesTable">
          <div id="noExaminationTypesAddedMessageContainer">
            <fmt:message key="system.setupwizard.examinationtypes.addNew" />
          </div>
        </div>
      </div>

      <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" name="next" value="<fmt:message key="system.setupwizard.next"/>">
      </div>
    </form>
    
  </jsp:body>
</t:setupwizard-template>