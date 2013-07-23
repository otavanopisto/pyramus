<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="system.systemSettings.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>

    <script type="text/javascript">

      function onLoad(event) {
        tabControl = new IxProtoTabs($('tabs'));
        
        var settingsTable = new IxTable($('settingsTableContainer'), {
          id : "settingsTable",
          columns : [{
            header : '<fmt:message key="system.systemSettings.settingsTableKeyHeader"/>',
            left : 8,
            width: 300,
            dataType: 'text',
            editable: false,
            paramName: 'key'
          }, {
            header : '<fmt:message key="system.systemSettings.settingsTableValueHeader"/>',
            left : 316,
            right: 8,
            dataType: 'text',
            editable: true,
            paramName: 'value'
          }]
        });

        var rows = new Array();
        <c:forEach var="settingKey" items="${settingKeys}">
          rows.push([
            '${settingKey.name}',
            '${fn:escapeXml(settings[settingKey.name])}',
          ]);
        </c:forEach>
        settingsTable.addRows(rows);
      }
        
    </script>
  </head>
  
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="system.systemSettings.pageTitle"/></h1>
    
    <div class="genericFormContainer"> 
      <form action="systemsettings.page" method="post">
  
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#settings">
            <fmt:message key="system.systemSettings.tabLabelSettings"/>
          </a>
        </div>
        
        <div id="settings" class="tabContent">
          <div id="settingsTableContainer"></div>
        </div>
  
        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="<fmt:message key="system.systemSettings.saveButton"/>">
        </div>

      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>