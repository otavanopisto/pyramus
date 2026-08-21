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

    <script type="text/javascript">
      function onLoad(event) {
        tabControl = new IxProtoTabs($('tabs'));
        
        var settingsTable = new IxTable($('clientApplicationsTableContainer'), {
          id : "clientApplicationsTable",
          columns : [{
            dataType: 'hidden',
            paramName: "id"
          }, {
            header : '<fmt:message key="system.clientapplications.nameHeader"/>',
            left : 8,
            width: 300,
            dataType: 'text',
            editable: false,
            paramName: 'appName'
          }, {
            right: 8,
            width: 22,
            dataType: 'button',
            paramName: 'editBtn',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: getLocale().getText("generic.action.edit"),
            onclick: function (event) {
              var table = event.tableComponent;
              var clientApplicationId = table.getCellValue(event.row, table.getNamedColumnIndex('id'));
              redirectTo(GLOBAL_contextPath + '/system/clientapplication.page?clientApplicationId=' + clientApplicationId);
            }
          }]
        });

        var rows = new Array();
        <c:forEach var="clientApplication" items="${clientApplications}">
          rows.push([
            '${clientApplication.id}',
            '${clientApplication.clientName}',
            null
          ]);
        </c:forEach>
        settingsTable.addRows(rows);
      }
    </script>
  </head>
  
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="system.clientapplications.pageTitle"/></h1>
    
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#clientApplications">
          <fmt:message key="system.clientapplications.tabLabel"/>
        </a>
      </div>
      
      <div id="clientApplications" class="tabContent">
        <div class="genericTableAddRowContainer">
          <a class="genericTableAddRowLinkContainer" href="${pageContext.request.contextPath}/system/clientapplication.page?clientApplicationId=NEW"><fmt:message key="system.clientapplications.addBtn"/></a>
        </div>
        <div id="clientApplicationsTableContainer"></div>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>