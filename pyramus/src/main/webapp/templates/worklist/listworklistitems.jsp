<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title><fmt:message key="worklist.listWorklistItems.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/worklist/listworklistitems.js">
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="worklist.listWorklistItems.pageTitle" /></h1>
    
    <div id="listWorklistItemsFilterContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#listWorklistItems">
            <fmt:message key="worklist.listWorklistItems.tabListWorklistItems"/>
          </a>
        </div>
        
        <div id="listWorklistItems" class="tabContent">
          <form id="filterForm" method="post" onsubmit="onListWorklistItems(event);">
      
            <div class="genericFormSection">   
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="worklist.listWorklistItems.staffMember"/>
              </jsp:include>                                     
              <select name="staffMember" id="staffMember">
                <c:forEach var="staffMember" items="${staffMembers}">
                  <option value="${staffMember.id}">${staffMember.lastName}, ${staffMember.firstName}</option>
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">   
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="worklist.listWorklistItems.beginDate"/>
              </jsp:include>
              <input type="text" ix:datefieldid="beginDate" name="beginDate" class="ixDateField"/> 
            </div>

            <div class="genericFormSection">   
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="worklist.listWorklistItems.endDate"/>
              </jsp:include>
              <input type="text" ix:datefieldid="endDate" name="endDate" class="ixDateField"/> 
            </div>
            
            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="terms.search"/>">
            </div>
          </form>
        </div>
      </div>
    </div>
    
    <div id="worklistItemsWrapper" style="display:none;">
      <div class="searchResultsTitle"><fmt:message key="worklist.listWorklistItems.worklistTitle"/></div>
      <div id="worklistItemsContainer" class="searchResultsContainer">
        <div class="genericFormSection" style="padding:8px;">   
          <select name="worklistTemplate" id="worklistTemplate">
            <c:forEach var="worklistTemplate" items="${worklistTemplates}">
              <option value="${worklistTemplate.id}">${worklistTemplate.description}</option>
            </c:forEach>
          </select>
          <input type="button" id="createNew" name="createNew" value="<fmt:message key="worklist.listWorklistItems.createNew"/>" onClick="createNew();">
          <span style="float:right;">
            <select name="stateChangeDropdown" id="stateChangeDropdown">
              <option value="ENTERED"><fmt:message key="worklist.listWorklistItems.state.entered"/></option>
              <option value="PROPOSED"><fmt:message key="worklist.listWorklistItems.state.proposed"/></option>
              <option value="APPROVED"><fmt:message key="worklist.listWorklistItems.state.approved"/></option>
              <option value="PAID"><fmt:message key="worklist.listWorklistItems.state.paid"/></option>
            </select>
            <input type="button" id="changeStateButton" name="changeStateButton" value="<fmt:message key="worklist.listWorklistItems.changeState"/>" onClick="changeState();">
          </span>
        </div>
        <div id="worklistItemsTableContainer"></div>
      </div>
    </div>
  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>