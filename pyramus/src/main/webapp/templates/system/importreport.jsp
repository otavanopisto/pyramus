<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="system.importReport.pageTitle"/></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <script type="text/javascript">
      function onChangeSelection() {
        var reports = JSDATA["reports"].evalJSON();
        var contextTypes = JSDATA["contextTypes"].evalJSON();
        var selectedValue = $('reportSelect').value;
        
        for (var i = 0, l = contextTypes.length; i < l; i++) {
          document.importForm["context." + contextTypes[i]].checked = false;
        }

        if (selectedValue == "")
          document.importForm["context.Common"].checked = true;
        
        for (var i = 0, l = reports.length; i < l; i++) {
          if (reports[i].id == selectedValue) {
            for (var j = 0, l2 = reports[i].contexts.length; j < l2; j++) {
              document.importForm["context." + reports[i].contexts[j]].checked = true;
            }
            break;
          }
        }
        
        document.importForm.name.value = document.importForm.report.options[document.importForm.report.selectedIndex].text;
      }
    
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      }
    </script>
  </head>
  <body onload="onLoad(event)">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    <h1 class="genericPageHeader"><fmt:message key="system.importReport.pageTitle" /></h1>
  
    <div id="importDataImportFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#importData">
            <fmt:message key="system.importReport.tabLabelImportReport"/>
          </a>
        </div>
      
        <div id="importData" class="tabContent">
          <form name="importForm" action="importreport.page" enctype="multipart/form-data" method="post" accept-charset="UTF-8">

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="system.importReport.existingReportTitle"/>
                <jsp:param name="helpLocale" value="system.importReport.existingReportHelp"/>
              </jsp:include>          
              <select name="report" id="reportSelect" onchange="onChangeSelection();">
                <option></option>
                <c:forEach var="report" items="${reports}">
                  <option value="${report.id}">${report.name}</option>
                </c:forEach>
              </select>
            </div> 

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="system.importReport.nameTitle"/>
                <jsp:param name="helpLocale" value="system.importReport.nameHelp"/>
              </jsp:include>          
              <input type="text" name="name"/>
            </div> 

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="system.importReport.contextsTitle"/>
                <jsp:param name="helpLocale" value="system.importReport.contextsHelp"/>
              </jsp:include>
              
              <c:forEach var="contextType" items="${contextTypes}">
                <c:choose>
                  <c:when test="${contextType eq 'Common'}">
                    <input type="checkbox" name="context.${contextType}" value="true" checked="checked"/>
                  </c:when>
                  <c:otherwise>
                    <input type="checkbox" name="context.${contextType}" value="true"/>
                  </c:otherwise>
                </c:choose>
                <fmt:message key="generic.reportContextTypes.${contextType}"/><br/>                
              </c:forEach>
            </div> 
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="system.importReport.fileTitle"/>
                <jsp:param name="helpLocale" value="system.importReport.fileHelp"/>
              </jsp:include>          
              <input type="file" name="file"/>
            </div>
    
            <div class="genericFormSubmitSection">
              <input type="submit" class="formvalid" value="<fmt:message key="system.importReport.importButton"/>">
            </div>
          </form>
        </div>
      </div>
    </div>
   
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>