<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="system.clientapplications.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>

    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript">
      function onLoad(event) {
        tabControl = new IxProtoTabs($('tabs'));
        
        var settingsTable = new IxTable($('redirectURIsTableContainer'), {
          id : "redirectURIsTable",
          columns : [{
            header : '<fmt:message key="system.clientapplications.nameHeader"/>',
            left : 8,
            width: 300,
            required: true,
            dataType: 'text',
            editable: true,
            paramName: 'redirectURI'
          }, {
            left: 8 + 300 + 8,
            width: 22,
            dataType: 'button',
            paramName: 'deleteButton',
            imgsrc: GLOBAL_contextPath + '/gfx/icons/16x16/actions/mail-mark-junk.png',
            tooltip: '<fmt:message key="system.clientapplications.removeTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              table.deleteRow(event.row);
            }
          }]
        });

        var rows = new Array();
        <c:forEach var="redirectURI" items="${clientApplication.redirectURIs}">
          rows.push([
            '${fn:escapeXml(redirectURI)}',
            ''
          ]);
        </c:forEach>
        settingsTable.addRows(rows);
      }
      
      function addRedirectURI() {
        var table = getIxTableById('redirectURIsTable');
        table.addRow(['', '']);
      }
      
    </script>
  </head>
  
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="system.clientapplications.pageTitle"/></h1>
    
    <div class="genericFormContainer"> 
      <form action="clientapplication.page" method="post">
        <input type="hidden" name="clientApplicationId" value="${clientApplication.id}" />

        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#clientApplications">
            <fmt:message key="system.clientapplications.tabLabel"/>
          </a>
        </div>
        
        <div id="clientApplications" class="tabContent">
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="system.clientapplications.nameHeader"/>
              <jsp:param name="helpLocale" value="system.clientapplications.nameHeaderHelp"/>
            </jsp:include>
            <input type="text" class="required" name="clientName" value="${fn:escapeXml(clientApplication.clientName)}" />
          </div>
        
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="grading.manageSpokenLanguageExams.studentTitle"/>
              <jsp:param name="helpLocale" value="grading.manageSpokenLanguageExams.studentHelp"/>
            </jsp:include>
            <div>${fn:escapeXml(clientApplication.clientId)}</div>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="grading.manageSpokenLanguageExams.studentTitle"/>
              <jsp:param name="helpLocale" value="grading.manageSpokenLanguageExams.studentHelp"/>
            </jsp:include>
            <div>${fn:escapeXml(clientApplication.clientSecret)}</div>
          </div>

          <div class="genericFormSection">
            <div>
              <input type="checkbox" id="active" name="active" value="1" ${clientApplication.active ? 'checked="checked"' : '' } />
              <label for="active"><fmt:message key="terms.active"/></label>
            </div>
            <div>
              <input type="checkbox" id="skipPrompt" name="skipPrompt" value="1" ${clientApplication.skipPrompt eq true ? 'checked="checked"' : '' } />
              <label for="active"><fmt:message key="system.clientapplications.skipHeader"/></label>
            </div>
            <div>
              <input type="checkbox" id="allowAllRedirectURIs" name="allowAllRedirectURIs" value="1" ${clientApplication.allowAllRedirectURIs ? 'checked="checked"' : '' } />
              <label for="active"><fmt:message key="system.clientapplications.allowAllRedirectURIs"/></label>
            </div>
          </div>
        
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="grading.manageSpokenLanguageExams.studentTitle"/>
              <jsp:param name="helpLocale" value="grading.manageSpokenLanguageExams.studentHelp"/>
            </jsp:include>
            <c:set var="scopes">
              <c:forEach items="${clientApplication.scopes}" var="scope" varStatus="status">${fn:escapeXml(scope)}<c:if test="${!status.last}">,</c:if></c:forEach>
            </c:set>
            <input type="text" name="scopes" value="${scopes}" />
          </div>
        
          <div class="genericTableAddRowContainer">
            <span class="genericTableAddRowLinkContainer" onclick="addRedirectURI();"><fmt:message key="system.clientapplications.addBtn"/></span>
          </div>
          <div id="redirectURIsTableContainer"></div>
          
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="grading.manageSpokenLanguageExams.studentTitle"/>
              <jsp:param name="helpLocale" value="grading.manageSpokenLanguageExams.studentHelp"/>
            </jsp:include>
            <input type="checkbox" name="regenerateSecret" value="1" />
          </div>
        </div>
  
        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="<fmt:message key="system.clientapplications.saveBtn"/>">
        </div>

      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>