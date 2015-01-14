<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="system.plugins.pageTitle" /></title>
    
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function setupPlugins() {
        var pluginsTable = new IxTable($('pluginsTableContainer'), {
          id : "pluginsTable",
          columns : [{
            dataType: 'hidden',
            paramName: "id"
          }, {
            dataType: 'hidden',
            paramName: "remove"
          }, {
            header : '<fmt:message key="system.plugins.pluginsTableEnabledHeader"/>',
            left : 8,
            width: 100,
            dataType: 'checkbox',
            editable: true,
            paramName: 'enabled'
          }, {
            header : '<fmt:message key="system.plugins.pluginsTableStatusHeader"/>',
            left : 8 + 100 + 8,
            width: 150,
            dataType: 'text',
            editable: false,
            paramName: 'status'
          }, {
            header : '<fmt:message key="system.plugins.pluginsTableGroupIdHeader"/>',
            left : 8 + 100 + 8 + 150 + 8,
            width: 200,
            dataType: 'text',
            editable: true,
            paramName: 'groupId',
            required: true
          }, {
            header : '<fmt:message key="system.plugins.pluginsTableArtifactIdHeader"/>',
            left : 8 + 100 + 8 + 150 + 8 + 200 + 8,
            right: 8 + 100 + 8 + 22 + 8,
            dataType: 'text',
            editable: true,
            paramName: 'artifactId',
            required: true
          }, {
            header : '<fmt:message key="system.plugins.pluginsTableVersionHeader"/>',
            width : 100,
            right: 8 + 22 + 8,
            dataType: 'text',
            editable: true,
            paramName: 'version',
            required: true
          }, {
            right: 8,
            width: 22,
            dataType: 'button',
            paramName: 'deleteButton',
            imgsrc: GLOBAL_contextPath + '/gfx/icons/16x16/actions/mail-mark-junk.png',
            tooltip: '<fmt:message key="system.plugins.pluginsTableRemoveButtonTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              table.setCellValue(event.row, table.getNamedColumnIndex('remove'), 1);
              table.disableRow(event.row);
              table.hideCell(event.row, table.getNamedColumnIndex('deleteButton'));
              table.showCell(event.row, table.getNamedColumnIndex('undeleteButton'));
              table.enableCellEditor(event.row, table.getNamedColumnIndex('undeleteButton'));
            }
          }, {
            right: 8,
            width: 22,
            dataType: 'button',
            paramName: 'undeleteButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/icons/16x16/actions/mail-mark-not-junk.png',
            tooltip: '<fmt:message key="system.plugins.pluginsTableRestoreButtonTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              table.setCellValue(event.row, table.getNamedColumnIndex('remove'), 0);
              table.enableRow(event.row);
              table.showCell(event.row, table.getNamedColumnIndex('deleteButton'));
              table.enableCellEditor(event.row, table.getNamedColumnIndex('deleteButton'));
              table.hideCell(event.row, table.getNamedColumnIndex('undeleteButton'));
            }
          }]
        });
        
        <c:forEach var="plugin" items="${plugins}">
          var row = pluginsTable.addRow([ 
            ${plugin.id},
            0,
            ${plugin.enabled},
            '${plugin.status}',
            '${plugin.groupId}',
            '${plugin.artifactId}',
            '${plugin.version}',
            null, 
            null
          ]);
          
          if (${plugin.deletable ne true}) {
            pluginsTable.disableCellEditor(row, pluginsTable.getNamedColumnIndex('deleteButton'));
          }
        </c:forEach>
      }
      
      function addPlugin() {
        var table = getIxTableById('pluginsTable');
        rowIndex = table.addRow(['', 0, false, '<fmt:message key="system.plugins.pluginsTableStatusNotLoaded"/>', '', '', '', null, null]);
      }
      
      function setupRepositories() {
        var repositoriesTable = new IxTable($('repositoriesTableContainer'), {
          id : "repositoriesTable",
          columns : [{
            dataType: 'hidden',
            paramName: "id"
          }, {
            dataType: 'hidden',
            paramName: "remove"
          }, {
            header : '<fmt:message key="system.plugins.repositoriesTableRepositoryIdHeader"/>',
            left : 8,
            width: 150,
            dataType: 'text',
            editable: true,
            paramName: 'repositoryId',
            required: true
          }, {
            header : '<fmt:message key="system.plugins.repositoriesTableUrlHeader"/>',
            left : 8 + 150 + 8,
            right: 8 + 22 + 8,
            dataType: 'text',
            editable: true,
            paramName: 'url',
            required: true
          }, {
            right: 8,
            width: 22,
            dataType: 'button',
            paramName: 'deleteButton',
            imgsrc: GLOBAL_contextPath + '/gfx/icons/16x16/actions/mail-mark-junk.png',
            tooltip: '<fmt:message key="system.plugins.repositoriesTableRemoveButtonTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              table.setCellValue(event.row, table.getNamedColumnIndex('remove'), 1);
              table.disableRow(event.row);
              table.hideCell(event.row, table.getNamedColumnIndex('deleteButton'));
              table.showCell(event.row, table.getNamedColumnIndex('undeleteButton'));
              table.enableCellEditor(event.row, table.getNamedColumnIndex('undeleteButton'));
            }
          }, {
            right: 8,
            width: 22,
            dataType: 'button',
            paramName: 'undeleteButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/icons/16x16/actions/mail-mark-not-junk.png',
            tooltip: '<fmt:message key="system.plugins.repositoriesTableRestoreButtonTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              table.setCellValue(event.row, table.getNamedColumnIndex('remove'), 0);
              table.enableRow(event.row);
              table.showCell(event.row, table.getNamedColumnIndex('deleteButton'));
              table.enableCellEditor(event.row, table.getNamedColumnIndex('deleteButton'));
              table.hideCell(event.row, table.getNamedColumnIndex('undeleteButton'));
            }
          }]
        });
        
        var rows = new Array();
        <c:forEach var="repository" items="${repositories}">
          rows.push([
            ${repository.id},
            0,
            '${fn:escapeXml(repository.repositoryId)}',
            '${fn:escapeXml(repository.url)}',
            null,
            null
          ]);
        </c:forEach>
        
        repositoriesTable.addRows(rows);
      }
      
      function addRepository() {
        var table = getIxTableById('repositoriesTable');
        rowIndex = table.addRow(['', false, '', '', null, null], true);
      }
    
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        
        setupPlugins();
        setupRepositories();
      }
    </script>
  </head>
  <body onload="onLoad(event)">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader">
      <fmt:message key="system.plugins.pageTitle" />
    </h1>
  
    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#plugins"> <fmt:message key="system.plugins.pluginsTabLabel" /></a>
        <a class="tabLabel" href="#repositories"> <fmt:message key="system.plugins.repositoriesTabLabel" /></a>
      </div>

      <form method="post" action="plugins.page">
        <div id="plugins" class="tabContent">
          <div class="genericTableAddRowContainer">
            <span class="genericTableAddRowLinkContainer" onclick="addPlugin();"><fmt:message key="system.plugins.addPluginLink"/></span>
          </div>
          <div id="pluginsTableContainer"></div>
        </div>
        
        <div id="repositories" class="tabContent">
          <div class="genericTableAddRowContainer">
            <span class="genericTableAddRowLinkContainer" onclick="addRepository();"><fmt:message key="system.plugins.addRepositoryLink"/></span>
          </div>
          <div id="repositoriesTableContainer"></div>
        </div>
        
        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="<fmt:message key="system.plugins.save"/>">
        </div>
      </form>
    </div>
  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>