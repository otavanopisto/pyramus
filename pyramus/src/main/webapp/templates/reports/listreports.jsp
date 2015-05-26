<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="reports.listReports.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      };
    </script>
    
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="reports.listReports.pageTitle"/></h1>
  
    <div id="listReportsEditFormContainer">
      <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#listReports">
          <span class="tabLabelLeftTopCorner">
            <span class="tabLabelRightTopCorner">
              <fmt:message key="reports.listReports.tabLabelReports"/>
            </span>
          </span>
        </a>
      </div>
      
      <div id="listReports" class="tabContent">
        <c:set var="category" value=""/>
        <c:forEach var="report" items="${reports}">
          <c:if test="${report.category.name != category}">
            <c:if test="${not empty category}">
              <div class="listReportsSpacer"></div>
            </c:if>
            <div class="listReportsCategoryContainer">
              <c:choose>
                <c:when test="${empty report.category}"><fmt:message key="reports.listReports.uncategorizedLabel"/></c:when>
                <c:otherwise><c:out value="${report.category.name}"/></c:otherwise>
              </c:choose>
            </div>
            <c:set var="category" value="${report.category.name}"/>
          </c:if>
          <div class="listReportsReportContainer">
            <div class="listReportsNameContainer" title="<fmt:message key="reports.listReports.viewReportTooltip"/>" onclick="redirectTo(GLOBAL_contextPath + '/reports/viewreport.page?reportId=<c:out value="${report.id}"/>');"><c:out value="${report.name}"/></div>
<%--             <div class="listReportsEditReportContainer" title="<fmt:message key="reports.listReports.editReportTooltip"/>" onclick="redirectTo(GLOBAL_contextPath + '/reports/editreport.page?reportId=<c:out value="${report.id}"/>');"></div> --%>
          </div>
        </c:forEach>
        <div class="listReportsSpacer"></div>
      </div>
      
    </div>
  </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>