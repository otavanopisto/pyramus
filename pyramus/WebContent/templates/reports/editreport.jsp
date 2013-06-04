<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="reports.editReport.pageTitle"/></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      }
    </script>
  </head>
  <body onload="onLoad(event)">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    <h1 class="genericPageHeader"><fmt:message key="reports.editReport.pageTitle" /></h1>
  
    <div> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#editReport">
            <fmt:message key="reports.editReport.tabLabelEditReport"/>
          </a>
        </div>
      
        <div id="editReport" class="tabContent">
	  	  <form action="editreport.json" method="post" ix:jsonform="true" ix:useglasspane="true">
            <input type="hidden" name="reportId" value="${report.id}"></input>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="reports.editReport.nameTitle"/>
                <jsp:param name="helpLocale" value="reports.editReport.nameHelp"/>
              </jsp:include>          
              <input type="text" name="name" class="required" size="60" value="${fn:escapeXml(report.name)}"/>
            </div> 
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="reports.editReport.categoryTitle"/>
                <jsp:param name="helpLocale" value="reports.editReport.categoryHelp"/>
              </jsp:include>          
              <select name="category">
                <option></option>
                <c:forEach var="category" items="${reportCategories}">
                  <c:choose>
                    <c:when test="${category.id eq report.category.id}">
                      <option value="${category.id}" selected="selected">${category.name}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${category.id}">${category.name}</option> 
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </div> 

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="reports.editReport.contextsTitle"/>
                <jsp:param name="helpLocale" value="reports.editReport.contextsHelp"/>
              </jsp:include>
              
              <c:forEach var="contextType" items="${contextTypes}">
                <c:choose>
                  <c:when test="${reportContexts[contextType]}">
                    <input type="checkbox" name="context.${contextType}" value="true" checked="checked"/>
                  </c:when>
                  <c:otherwise>
                    <input type="checkbox" name="context.${contextType}" value="true"/>
                  </c:otherwise>
                </c:choose>
                <fmt:message key="generic.reportContextTypes.${contextType}"/><br/>                
              </c:forEach>
            </div> 
            
            <div class="genericFormSubmitSection">
              <input type="submit" class="formvalid" value="<fmt:message key="reports.editReport.saveButton"/>">
            </div>
          </form>
        </div>
      </div>
    </div>
   
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>