<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<t:setupwizard-template>
  <jsp:attribute name="script">
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/system/setupwizard/academicterms.js"></script>
  </jsp:attribute>
  <jsp:body>
    

        <form action="" method="post" >
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#manageAcademicTermsListTerms">
              <fmt:message key="settings.academicTerms.tabLabelAcademicTerms"/>
            </a>
          </div>
          
          <div id="manageAcademicTermsListTerms" class="tabContentixTableFormattedData">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addTermsTableRow();"><fmt:message key="settings.academicTerms.addTermLink"/></span>
            </div>
              
            <div id="noTermsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="settings.academicTerms.noTermsAddedPreFix"/> <span onclick="addTermsTableRow();" class="genericTableAddRowLink"><fmt:message key="settings.academicTerms.noTermsAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="termsTable"></div>
          </div>
    
      <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" name="next" value="<fmt:message key="system.setupwizard.next"/>">
        <input type="submit" name="previous" value="<fmt:message key="system.setupwizard.previous"/>">
      </div>
    </form>
    
  </jsp:body>
</t:setupwizard-template>