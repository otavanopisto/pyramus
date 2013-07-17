<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="settings.manageTransferCreditTemplates.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/settings/managetransfercredittemplates.js">
    </script>
    
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="settings.manageTransferCreditTemplates.pageTitle"/></h1>
  
    <div id="listTransferCreditTemplatesFormContainer">
      <div class="genericFormContainer"> 
		    <div class="tabLabelsContainer" id="tabs">
		      <a class="tabLabel" href="#transferCreditTemplates">
		        <span class="tabLabelLeftTopCorner">
		          <span class="tabLabelRightTopCorner">
		            <fmt:message key="settings.manageTransferCreditTemplates.tabLabelTransferCreditTemplates"/>
		          </span>
		        </span>
		      </a>
		    </div>
              
		    <div id="transferCreditTemplates" class="tabContentixTableFormattedData">
          <div class="genericTableAddRowContainer">
            <a class="genericTableAddRowLinkContainer" href="${pageContext.request.contextPath}/settings/createtransfercredittemplate.page"><fmt:message key="settings.manageTransferCreditTemplates.addTransferCreditTemplate"/></a>
          </div>
		      
	        <div id="noTransferCreditTemapltesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
	          <span><fmt:message key="settings.manageTransferCreditTemplates.noTransferCreditTemplatesAddedPreFix"/> <a href="${pageContext.request.contextPath}/settings/createtransfercredittemplate.page" class="genericTableAddRowLink"><fmt:message key="settings.manageTransferCreditTemplates.noTransferCreditTemplatesAddedClickHereLink"/></a>.</span>
	        </div>

          <div id="transferCreditTemplatesTableContainer"></div>
		    </div>
		  </div>
		</div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>