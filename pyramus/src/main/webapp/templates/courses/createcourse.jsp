<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="courses.createCourse.pageTitle" /></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/datefield_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/studentinfopopup_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/coursecomponenteditor.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/coursecomponentseditor.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/coursecomponenteditordrafttask.js"></script>
    
    <script type="text/javascript">
      var componentsEditor;
  
      function getCourseComponentsEditor() {
        return componentsEditor;
      }

      // Generic resource related functions

      function openSearchResourcesDialog(targetTableId) {

        var selectedResources = new Array();
        var resourcesTable = getIxTableById(targetTableId);
        for (var i = 0; i < resourcesTable.getRowCount() - 1; i++) {
          var resourceId = resourcesTable.getCellValue(i, resourcesTable.getNamedColumnIndex('resourceId'));
          selectedResources.push({
            id: resourceId});
        }
        // TODO selectedResources -> dialog

        var dialog = new IxDialog({
          id : 'searchResourcesDialog',
          contentURL : GLOBAL_contextPath + '/resources/searchresourcesdialog.page',
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="resources.searchResourcesDialog.searchResourcesDialogTitle"/>',
          okLabel : '<fmt:message key="resources.searchResourcesDialog.okLabel"/>', 
          cancelLabel : '<fmt:message key="resources.searchResourcesDialog.cancelLabel"/>' 
        });
        
        dialog.setSize("800px", "600px");
        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'okClick':
              var resourcesTable = getIxTableById(targetTableId);
              resourcesTable.detachFromDom();
              for (var i = 0, len = event.results.resources.length; i < len; i++) {
                var resourceId = event.results.resources[i].id;
                var resourceName = event.results.resources[i].name;
                var index = getResourceRowIndex(targetTableId, resourceId);
                if (index == -1) {
                  resourcesTable.addRow([resourceId, resourceName, 0, 0, 0, 0, 0, '']);
                }
              }
              resourcesTable.reattachToDom();
            break;
          }
        });
        dialog.open();
      }

      function addNewCourseStudent(studentsTable, personId, studentId, studentName) {
        JSONRequest.request("students/getstudentstudyprogrammes.json", {
          parameters: {
            personId: personId
          },
          onSuccess: function (jsonResponse) {
            var rowIndex = studentsTable.addRow(['', studentName.escapeHTML(), studentId, 10, new Date().getTime(), 0, '', 'false', personId, '']);
            var cellEditor = studentsTable.getCellEditor(rowIndex, studentsTable.getNamedColumnIndex('studentId'));
            for (var j = 0, l = jsonResponse.studentStudyProgrammes.length; j < l; j++) {
              IxTableControllers.getController('select').addOption(cellEditor , jsonResponse.studentStudyProgrammes[j].studentId, jsonResponse.studentStudyProgrammes[j].studyProgrammeName);
            }
            $('noStudentsAddedMessageContainer').setStyle({
              display: 'none'
            });
            $('createCourseStudentsTotalContainer').setStyle({
              display: ''
            });
            $('createCourseStudentsTotalValue').innerHTML = studentsTable.getRowCount(); 
          }
        });   
      };

      function openSearchStudentsDialog() {
        var selectedStudents = new Array();
        var studentsTable = getIxTableById('studentsTable');
        for (var i = 0; i < studentsTable.getRowCount() - 1; i++) {
          var studentId = studentsTable.getCellValue(i, studentsTable.getNamedColumnIndex('studentId'));
          selectedStudents.push({
            id: studentId});
        }
        // TODO selectedStudents -> dialog

        var dialog = new IxDialog({
          id : 'searchStudentsDialog',
          contentURL : GLOBAL_contextPath + '/students/searchstudentsdialog.page',
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="students.searchStudentsDialog.searchStudentsDialogTitle"/>',
          okLabel : '<fmt:message key="students.searchStudentsDialog.okLabel"/>', 
          cancelLabel : '<fmt:message key="students.searchStudentsDialog.cancelLabel"/>' 
        });
        
        dialog.setSize("800px", "600px");
        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'okClick':
              var studentsTable = getIxTableById('studentsTable');
              studentsTable.detachFromDom();
              for (var i = 0, len = event.results.students.length; i < len; i++) {
                var personId = event.results.students[i].personId;
                var studentId = event.results.students[i].id;
                var studentName = event.results.students[i].name;
                var index = getStudentRowIndex(personId);
                if (index == -1) {
                  addNewCourseStudent(studentsTable, personId, studentId, studentName);
                } 
              }
              studentsTable.reattachToDom();
            break;
          }
        });
        dialog.open();
      }

      function getResourceRowIndex(tableId, resourceId) {
        var table = getIxTableById(tableId);
        if (table) {
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableResourceId = table.getCellValue(i, table.getNamedColumnIndex('resourceId'));
            if (tableResourceId == resourceId) {
              return i;
            }
          }
        }
        return -1;
      }

      function getStudentRowIndex(personId) {
        var table = getIxTableById('studentsTable');
        if (table) {
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableStudentId = table.getCellValue(i, table.getNamedColumnIndex('personId'));
            if (tableStudentId == personId) {
              return i;
            }
          }
        }
        return -1;
      }

      function updateCosts(tableId, rowId, tableTotalContainerId) {

        // Get the table in question

        var table = getIxTableById(tableId);

        // Update the row total, if a row was given
        
        if (rowId != -1) {
          var hours = table.getCellValue(rowId, table.getNamedColumnIndex('hours'));
          var hourlyCost = table.getCellValue(rowId, table.getNamedColumnIndex('hourlyCost'));
          var units = table.getCellValue(rowId, table.getNamedColumnIndex('units'));
          var unitCost = table.getCellValue(rowId, table.getNamedColumnIndex('unitCost'));
          table.setCellValue(rowId, table.getNamedColumnIndex('total'), (hours * hourlyCost) + (units * unitCost));
        }

        // Update the table total
        
        var sum = 0;
        for (var row = 0; row < table.getRowCount(); row++) {
          sum += parseInt(table.getCellValue(row, table.getNamedColumnIndex('total')));
        }
        $(tableTotalContainerId).innerHTML = sum;

        // Update the overall total

        updateTotalCosts();
      }

      function updateTotalCosts() {
        var totalCosts = 0;
      
        var basicResourcesTable = getIxTableById('basicResourcesTable');
        var studentResourcesTable = getIxTableById('studentResourcesTable');
        var gradeResourcesTable = getIxTableById('gradeResourcesTable');
        var otherCostsTable = getIxTableById('otherCostsTable');
      
        for (var row = 0; row < basicResourcesTable.getRowCount(); row++)
          totalCosts += parseInt(basicResourcesTable.getCellValue(row, basicResourcesTable.getNamedColumnIndex('total')));
      
        for (var row = 0; row < studentResourcesTable.getRowCount(); row++)
          totalCosts += parseInt(studentResourcesTable.getCellValue(row, studentResourcesTable.getNamedColumnIndex('total')));
      
        for (var row = 0; row < gradeResourcesTable.getRowCount(); row++)
          totalCosts += parseInt(gradeResourcesTable.getCellValue(row, gradeResourcesTable.getNamedColumnIndex('total')));
      
        for (var row = 0; row < otherCostsTable.getRowCount(); row++)
          totalCosts += parseInt(otherCostsTable.getCellValue(row, otherCostsTable.getNamedColumnIndex('cost')));
      
        $('courseCostsTotalValue').innerHTML = totalCosts;
      }

      // Personnel
      
      function setupPersonnelTable() {
        var personnelTable = new IxTable($('personnelTable'), {
          id : "personnelTable",
          columns : [{
            dataType: 'hidden',
            paramName: 'userId'
          }, {
            header : '<fmt:message key="courses.createCourse.personnelTablePersonHeader"/>',
            left : 8,
            width: 250,
            dataType : 'text',
            editable: false,
            paramName: 'userName'
          }, {
            header : '<fmt:message key="courses.createCourse.personnelTableRoleHeader"/>',
            width: 200,
            left : 266,
            dataType: 'select',
            editable: true,
            paramName: 'roleId',
            options: [
              <c:forEach var="role" items="${roles}" varStatus="vs">
                {text: "${role.name}", value: ${role.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            left: 474,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.createCourse.personnelTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            } 
          }]        
        });
      }
      
      function openSearchUsersDialog() {
        var dialog = new IxDialog({
          id : 'searchUsersDialog',
          contentURL : GLOBAL_contextPath + '/users/searchusersdialog.page',
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="users.searchUsersDialog.searchUsersDialogTitle"/>',
          okLabel : '<fmt:message key="users.searchUsersDialog.okLabel"/>', 
          cancelLabel : '<fmt:message key="users.searchUsersDialog.cancelLabel"/>' 
        });
        
        dialog.setSize("800px", "600px");
        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'okClick':
              var personnelTable = getIxTableById('personnelTable');
              personnelTable.detachFromDom();
              for (var i = 0, len = event.results.users.length; i < len; i++) {
                var userId = event.results.users[i].id;
                var userName = event.results.users[i].name;
                var index = getUserRowIndex(userId);
                if (index == -1) {
                  personnelTable.addRow([userId, userName, '', '']);
                } 
              }
              personnelTable.reattachToDom();
            break;
          }
        });
        dialog.open();
      }

      function getUserRowIndex(userId) {
        var table = getIxTableById('personnelTable');
        if (table) {
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableUserId = table.getCellValue(i, table.getNamedColumnIndex('userId'));
            if (tableUserId == userId) {
              return i;
            }
          }
        }
        return -1;
      }

      // Course components
      
      function setupComponents() {
        componentsEditor = new CourseComponentsEditor($('createCourseComponentsList'), {
          paramName: 'components',
          componentHoursSumElement: $('componentHoursTotalValueContainer'),
          resourceSearchUrl: '${pageContext.request.contextPath}/resources/searchresourcesautocomplete.binary',
          resourceSearchParamName: 'query',
          resourceSearchProgressImageUrl: '${pageContext.request.contextPath}/gfx/progress_small.gif',
          nameHeader: '<fmt:message key="courses.createCourse.componentsNameHeader"/>',
          lengthHeader: '<fmt:message key="courses.createCourse.componentsLengthHeader"/>',
          componentUnit: '<fmt:message key="courses.createCourse.componentUnit"/>',
          materialResourceUnit: '<fmt:message key="courses.createCourse.componentsResourceMaterialResourceUnit"/>',
          workResourceUnit: '<fmt:message key="courses.createCourse.componentsResourceWorkResourceUnit"/>',
          descriptionHeader: '<fmt:message key="courses.createCourse.componentsDescriptionHeader"/>',
          editButtonTooltip: '<fmt:message key="courses.createCourse.componentsEditButtonTooltip"/>',
          removeButtonTooltip: '<fmt:message key="courses.createCourse.componentsRemoveButtonTooltip"/>',
          archiveButtonTooltip: '<fmt:message key="courses.createCourse.componentsArchiveButtonTooltip"/>',
          resourceNameTitle: '<fmt:message key="courses.createCourse.componentsResourceNameTitle"/>',
          resourceUsageTitle: '<fmt:message key="courses.createCourse.componentsResourceUsageTitle"/>',
          resourceRemoveButtonTooltip: '<fmt:message key="courses.createCourse.componentsResourceRemoveButtonTooltip"/>',
          resourceArchiveButtonTooltip: '<fmt:message key="courses.createCourse.componentsResourceArchiveButtonTooltip"/>',
          noResourcesMessage: "<fmt:message key="courses.createCourse.componentsNoResourcesMessage" />"
        });   
        
        <c:if test="${fn:length(moduleComponents) gt 0}">
	        
	        $('noComponentsAddedMessageContainer').setStyle({
	          display: 'none'
	        });
	        $('componentHoursTotalContainer').setStyle({
	          display: ''
	        });
	        
	        <c:forEach var="component" items="${moduleComponents}" varStatus="componentsVs">
	          componentsEditor.addCourseComponent(
	            -1, 
	            '${fn:escapeXml(component.name)}', 
	            ${component.length.units}, 
	            '${fn:escapeXml(component.description)}'
	          );
	        </c:forEach>
	      </c:if>
      }
      
      function addNewComponent() {
        var componentEditor = getCourseComponentsEditor().addCourseComponent(-1, '', 0, '');
        
        componentEditor.toggleEditable();
        if (!componentsEditor.isComponentInView(componentEditor))
          componentsEditor.scrollToComponent(componentEditor);
        
        $('noComponentsAddedMessageContainer').setStyle({
          display: 'none'
        });
        $('componentHoursTotalContainer').setStyle({
          display: ''
        });
      }

      // Basic course resources 
      
      function setupBasicResourcesTable() {
        var basicResourcesTable = new IxTable($('basicResourcesTable'), {
          id : "basicResourcesTable",
          columns : [{
            dataType : 'hidden',
            paramName : 'resourceId'
          }, {
            header : '<fmt:message key="courses.createCourse.basicResourcesTableNameHeader"/>',
            dataType : 'text',
            editable  : false,
            left : 0,
            right : 350,
            paramName : 'resourceName'
          }, {
            header : '<fmt:message key="courses.createCourse.basicResourcesTableHoursHeader"/>',
            dataType : 'number',
            editable: true,
            right : 310,
            width : 30,
            paramName : 'hours'
          }, {
            header : '<fmt:message key="courses.createCourse.basicResourcesTableHourlyCostHeader"/>',
            dataType : 'number',
            editable: true,
            right : 225,
            width : 70,
            paramName : 'hourlyCost'
          }, {
            header : '<fmt:message key="courses.createCourse.basicResourcesTableUnitsHeader"/>',
            dataType : 'number',
            editable: true,
            right : 180,
            width : 30,
            paramName : 'units'
          }, {
            header : '<fmt:message key="courses.createCourse.basicResourcesTableCostPerUnitHeader"/>',
            dataType : 'number',
            editable: true,
            right : 85,
            width : 80,
            paramName : 'unitCost'
          }, {
            header : '<fmt:message key="courses.createCourse.basicResourcesTableTotalHeader"/>',
            dataType : 'number',
            editable: false,
            right : 30,
            width : 40,
            paramName : 'total'
          }, {
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.createCourse.basicResourcesTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            } 
          } ]
        });

        basicResourcesTable.addListener("cellValueChange", function (event) {
          if (event.tableComponent.getNamedColumnIndex('total') != event.column)
            updateCosts('basicResourcesTable', event.row, 'basicResourcesTableTotal');
        });
        basicResourcesTable.addListener("rowAdd", function (event) {
          updateCosts('basicResourcesTable', event.row, 'basicResourcesTableTotal');
        });
        basicResourcesTable.addListener("rowDelete", function(event) {
          updateCosts('basicResourcesTable', -1, 'basicResourcesTableTotal');
        });
      }

      // Student resources
      
      function setupStudentResourcesTable() {
        var studentResourcesTable = new IxTable($('studentResourcesTable'), {
          id : "studentResourcesTable",
          columns : [{
            dataType : 'hidden',
            paramName : 'resourceId'
          }, {
            header : '<fmt:message key="courses.createCourse.studentResourcesTableNameHeader"/>',
            dataType : 'text',
            editable  : false,
            left : 0,
            right : 350,
            paramName : 'resourceName'
          }, {
            header : '<fmt:message key="courses.createCourse.studentResourcesTableHoursHeader"/>',
            dataType : 'number',
            editable: true,
            right : 310,
            width : 30,
            paramName : 'hours'
          }, {
            header : '<fmt:message key="courses.createCourse.studentResourcesTableHourlyCostHeader"/>',
            dataType : 'number',
            editable: true,
            right : 225,
            width : 70,
            paramName : 'hourlyCost'
          }, {
            header : '<fmt:message key="courses.createCourse.studentResourcesTableUnitsHeader"/>',
            dataType : 'number',
            editable: true,
            right : 180,
            width : 30,
            paramName : 'units'
          }, {
            header : '<fmt:message key="courses.createCourse.studentResourcesTableCostPerUnitHeader"/>',
            dataType : 'number',
            editable: true,
            right : 85,
            width : 80,
            paramName : 'unitCost'
          }, {
            header : '<fmt:message key="courses.createCourse.studentResourcesTableTotalHeader"/>',
            dataType : 'number',
            editable: false,
            right : 30,
            width : 40,
            paramName : 'total'
          }, {
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.createCourse.studentResourcesTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            } 
          } ]
        });
        
        studentResourcesTable.addListener("cellValueChange", function (event) {
          if (event.tableComponent.getNamedColumnIndex('total') != event.column)
            updateCosts('studentResourcesTable', event.row, 'studentResourcesTableTotal');
        });
        studentResourcesTable.addListener("rowAdd", function (event) {
          updateCosts('studentResourcesTable', event.row, 'studentResourcesTableTotal');
        });
        studentResourcesTable.addListener("rowDelete", function(event) {
          updateCosts('studentResourcesTable', -1, 'studentResourcesTableTotal');
        });
      }

      // Grade resources
      
      function setupGradeResourcesTable() {
        var gradeResourcesTable = new IxTable($('gradeResourcesTable'), {
          id : "gradeResourcesTable",
          columns : [{
            dataType : 'hidden',
            paramName : 'resourceId'
          }, {
            header : '<fmt:message key="courses.createCourse.gradeResourcesTableNameHeader"/>',
            dataType : 'text',
            editable  : false,
            left : 0,
            right : 350,
            paramName : 'resourceName'
          }, {
            header : '<fmt:message key="courses.createCourse.gradeResourcesTableHoursHeader"/>',
            dataType : 'number',
            editable: true,
            right : 310,
            width : 30,
            paramName : 'hours'
          }, {
            header : '<fmt:message key="courses.createCourse.gradeResourcesTableHourlyCostHeader"/>',
            dataType : 'number',
            editable: true,
            right : 225,
            width : 70,
            paramName : 'hourlyCost'
          }, {
            header : '<fmt:message key="courses.createCourse.gradeResourcesTableUnitsHeader"/>',
            dataType : 'number',
            editable: true,
            right : 180,
            width : 30,
            paramName : 'units'
          }, {
            header : '<fmt:message key="courses.createCourse.gradeResourcesTableCostPerUnitHeader"/>',
            dataType : 'number',
            editable: true,
            right : 85,
            width : 80,
            paramName : 'unitCost'
          }, {
            header : '<fmt:message key="courses.createCourse.gradeResourcesTableTotalHeader"/>',
            dataType : 'number',
            editable: false,
            right : 30,
            width : 40,
            paramName : 'total'
          }, {
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.createCourse.gradeResourcesTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            } 
          } ]
        });
        
        gradeResourcesTable.addListener("cellValueChange", function (event) {
          if (event.tableComponent.getNamedColumnIndex('total') != event.column)
            updateCosts('gradeResourcesTable', event.row, 'gradeResourcesTableTotal');
        });
        gradeResourcesTable.addListener("rowAdd", function (event) {
          updateCosts('gradeResourcesTable', event.row, 'gradeResourcesTableTotal');
        });
        gradeResourcesTable.addListener("rowDelete", function(event) {
          updateCosts('gradeResourcesTable', -1, 'gradeResourcesTableTotal');
        });
      }

      // Other costs
      
      function setupOtherCostsTable() {
        var otherCostsTable = new IxTable($('otherCostsTable'), {
          id : "otherCostsTable",
          columns : [{
            header : '<fmt:message key="courses.createCourse.otherCostsTableNameHeader"/>',
            dataType : 'text',
            editable: true,
            left : 0,
            right : 120,
            paramName : 'name',
            required: true
          }, {
            header : '<fmt:message key="courses.createCourse.otherCostsTableCostHeader"/>',
            dataType : 'number',
            editable: true,
            right : 37,
            width : 78,
            paramName : 'cost'
          }, {
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.createCourse.otherCostsTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            } 
          } ]
        });

        var updateOtherCostsFunction = function(event) {
          var sum = 0;
          for (var row = 0; row < event.tableComponent.getRowCount(); row++) {
            sum += parseInt(event.tableComponent.getCellValue(row, otherCostsTable.getNamedColumnIndex('cost')));
          }
          $('otherCostsTableTotal').innerHTML = sum;
          updateTotalCosts();
        }
        otherCostsTable.addListener("cellValueChange", updateOtherCostsFunction);
        otherCostsTable.addListener("rowAdd", updateOtherCostsFunction);
        otherCostsTable.addListener("rowDelete", updateOtherCostsFunction);
      }
      
      function addOtherCostRow() {
        getIxTableById('otherCostsTable').addRow(['', 0, '']);
      }

      function setupStudentsTable() {
        var studentsTable = new IxTable($('courseStudentsTable'), {
          id : "studentsTable",
          columns : [{
            width: 22,
            left: 8,
            dataType: 'button',
            paramName: 'studentInfoButton',
            imgsrc: GLOBAL_contextPath + '/gfx/info.png',
            tooltip: '<fmt:message key="courses.createCourse.studentsTableStudentInfoTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var personId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              var button = table.getCellEditor(event.row, table.getNamedColumnIndex('studentInfoButton'));
              openStudentInfoPopupOnElement(button, personId);
            } 
          }, {
            header : '<fmt:message key="courses.createCourse.studentsTableNameHeader"/>',
            left : 8 + 22 + 8,
            right : 8 + 22 + 8 + 100 + 8 + 140 + 8 + 140 + 8 + 145 + 8 + 200 + 8 + 140 + 8,
            dataType : 'text',
            paramName: 'studentName',
            editable: false,
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSTRINGSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSTRINGSORT
              }
            }
          }, {
            header : '<fmt:message key="courses.createCourse.studentsTableStudyProgrammeHeader"/>',
            width: 140,
            right : 8 + 22 + 8 + 100 + 8 + 140 + 8 + 140 + 8 + 145 + 8 + 200 + 8,
            dataType : 'select',
            editable: true,
            dynamicOptions: true,
            paramName: 'studentId',
            options: [
            ],
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSELECTSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSELECTSORT
              }
            }
          }, {
            header : '<fmt:message key="courses.createCourse.studentsTableParticipationTypeHeader"/>',
            width: 200,
            right : 8 + 22 + 8 + 100 + 8 + 140 + 8 + 140 + 8 + 145 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'participationType',
            options: [
              <c:forEach var="courseParticipationType" items="${courseParticipationTypes}" varStatus="vs">
                {text: "${courseParticipationType.name}", value: ${courseParticipationType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ],
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSELECTSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSELECTSORT
              }
            },
            contextMenu: [
              {
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ]            
          }, {
            header : '<fmt:message key="courses.createCourse.studentsTableEnrolmentDateHeader"/>',
            width: 145,
            right : 8 + 22 + 8 + 100 + 8 + 140 + 8 + 140 + 8,
            dataType: 'date',
            editable: true,
            paramName: 'enrolmentDate',
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWNUMBERSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWNUMBERSORT
              }
            },
            contextMenu: [
              {
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ]            
          }, {
            header : '<fmt:message key="courses.createCourse.studentsTableEnrolmentTypeHeader"/>',
            width: 140,
            right : 8 + 22 + 8 + 100 + 8 + 140 + 8,
            dataType: 'select', 
            editable: true,
            paramName: 'enrolmentType',
            options: [
            <c:forEach var="courseEnrolmentType" items="${courseEnrolmentTypes}" varStatus="vs">
              {text: "${courseEnrolmentType.name}", value: ${courseEnrolmentType.id}}
              <c:if test="${not vs.last}">,</c:if>
            </c:forEach>
            ],
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSELECTSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSELECTSORT
              }
            }
          }, {
            header : '<fmt:message key="courses.createCourse.studentsTableOptionalityHeader"/>',
            right :  8 + 22 + 8 + 100 + 8,
            width: 140,
            dataType : 'select',
            paramName: 'optionality',
            editable: true,
            options: [
              {text: '', value: ''},
              {text: '<fmt:message key="courses.createCourse.studentsTableOptionalityMandatory"/>', value: 'MANDATORY'},
              {text: '<fmt:message key="courses.createCourse.studentsTableOptionalityOptional"/>', value: 'OPTIONAL'}
            ],
            contextMenu: [
              {
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ],            
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSELECTSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSELECTSORT
              }
            }
          }, {
            header : '<fmt:message key="courses.createCourse.studentsTableLodgingHeader"/>',
            width: 100,
            right : 8 + 22 + 8,
            dataType: 'select', 
            editable: true,
            paramName: 'lodging',
            options: [
              {text: '<fmt:message key="courses.createCourse.studentsTableLodgingYes"/>', value: 'true'},
              {text: '<fmt:message key="courses.createCourse.studentsTableLodgingNo"/>', value: 'false'}
            ],
            contextMenu: [
              {
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ],
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSELECTSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSELECTSORT
              }
            }
          }, {
            dataType: 'hidden', 
            paramName: 'personId'
          }, {
            right: 8,
            width: 22,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.createCourse.studentsTableRemoveRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              table.deleteRow(event.row);
              if (studentsTable.getRowCount() == 0) {
                $('noStudentsAddedMessageContainer').setStyle({
                  display: ''
                });
                $('createCourseStudentsTotalContainer').setStyle({
                  display: 'none'
                });
              }
              else {
                $('createCourseStudentsTotalContainer').setStyle({
                  display: ''
                });
                $('createCourseStudentsTotalValue').innerHTML = table.getRowCount(); 
              }
            } 
          }]        
        });
      }

      // onLoad
      
      function setupTags() {
        JSONRequest.request("tags/getalltags.json", {
          onSuccess: function (jsonResponse) {
            new Autocompleter.Local("tags", "tags_choices", jsonResponse.tags, {
              tokens: [',', '\n', ' ']
            });
          }
        });   
      }

      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        var descTabControl = new IxProtoTabs($('descriptionTabs'), {
          <c:if test="${fn:length(courseDescriptionCategories) gt 0}">
          tabAddContextMenu: [
            <c:forEach var="category" varStatus="vs" items="${courseDescriptionCategories}">
            <c:if test="${not vs.first}">,</c:if>
            {
              text: '${category.name}',
              onclick: function (event) {
                var descName = 'courseDescription.' + '${category.id}';
                var tabContent = descTabControl.addTab(descName, '${category.name}');
                tabContent.update('<input type="hidden" name="' + descName + '.catId" id="' + descName + '.catId" value="${category.id}"/><textarea name="' + descName + '.text" ix:ckeditor="true"></textarea>');
                CKEDITOR.replace(descName + '.text', { toolbar: "courseDescription", language: document.getCookie('pyramusLocale') });
                descTabControl.setActiveTab(descName);
              },
              isEnabled: function () {
                var catIdElement = $('courseDescription.${category.id}.catId');
                return catIdElement ? catIdElement.value != ${category.id} : true;
              }
            }
            </c:forEach>
          ]
          </c:if>
        });
        
        setupTags();
        setupPersonnelTable();
        setupComponents();
        setupBasicResourcesTable();
        setupStudentResourcesTable();
        setupGradeResourcesTable();
        setupOtherCostsTable();
        setupStudentsTable();
      }

    </script>
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="courses.createCourse.pageTitle" /></h1>
    
    <div id="createCourseCreateFormContainer">
      <div class="genericFormContainer">
        <form action="createcourse.json" method="post" ix:jsonform="true" ix:useglasspane="true">
      
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic"><fmt:message key="courses.createCourse.basicTabTitle" /></a>
            <a class="tabLabel" href="#components"><fmt:message key="courses.createCourse.componentsTabTitle" /></a>
            <a class="tabLabel" href="#costplan"><fmt:message key="courses.createCourse.costPlanTabTitle" /></a>
            <a class="tabLabel" href="#students"><fmt:message key="courses.createCourse.studentsTabTitle" /></a>
            <ix:extensionHook name="courses.createCourse.tabLabels"/>
          </div>
  
          <div id="basic" class="tabContent">
        
            <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="courses.createCourse.moduleTitle"/>
                  <jsp:param name="helpLocale" value="courses.createCourse.moduleHelp"/>
                </jsp:include>

              <input type="hidden" name="module" value="${module.id}" /> <i> ${module.name} </i>
            </div>
  
            <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="courses.createCourse.nameTitle"/>
                  <jsp:param name="helpLocale" value="courses.createCourse.nameHelp"/>
                </jsp:include>

              <input type="text" class="required" name="name" value="${fn:escapeXml(module.name)}" size="40">
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.tagsTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40" value="${fn:escapeXml(tags)}"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
            
            <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="courses.createCourse.nameExtensionTitle"/>
                  <jsp:param name="helpLocale" value="courses.createCourse.nameExtensionHelp"/>
                </jsp:include>
              <input type="text" name="nameExtension" size="40">
            </div>
      
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.stateTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.stateHelp"/>
              </jsp:include>
              <select name="state">           
                <c:forEach var="state" items="${states}">
                  <option value="${state.id}">${state.name}</option> 
                </c:forEach>
              </select>
            </div>
      
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.typeTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.typeHelp"/>
              </jsp:include>
              <select name="type">           
                <option></option> 
                <c:forEach var="type" items="${types}">
                  <option value="${type.id}">${type.name}</option> 
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.educationTypesTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.educationTypesHelp"/>
              </jsp:include>
              <div class="createCourseFormSectionEducationType">
                <c:forEach var="educationType" items="${educationTypes}">
                  <div class="createCourseFormSectionEducationTypeCell">
                    <div class="createCourseFormSectionEducationTypeTitle">
                      <div class="createCourseFormSectionEducationTypeTitleText">${educationType.name}</div>
                    </div>
                    <c:forEach var="educationSubtype" items="${educationSubtypes[educationType.id]}">
                      <c:set var="key" value="${educationType.id}.${educationSubtype.id}"/>
                      <c:choose>
                        <c:when test="${enabledEducationTypes[key]}">
                          <input type="checkbox" name="educationType.${key}" checked="checked"/>                      
                        </c:when>
                        <c:otherwise>
                          <input type="checkbox" name="educationType.${key}"/>                      
                        </c:otherwise>
                      </c:choose>
                      ${educationSubtype.name}<br/>
                    </c:forEach>
                  </div>
                </c:forEach>
              </div>
            </div>
  
            <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="courses.createCourse.subjectTitle"/>
                  <jsp:param name="helpLocale" value="courses.createCourse.subjectHelp"/>
                </jsp:include>
              
              <select name="subject">           
                <c:forEach var="educationType" items="${educationTypes}">
                  <c:if test="${subjectsByEducationType[educationType.id] ne null}">
                    <optgroup label="${educationType.name}">
                      <c:forEach var="subject" items="${subjectsByEducationType[educationType.id]}">
                        <c:choose>
                          <c:when test="${empty subject.code}">
                            <c:set var="subjectName">${subject.name}</c:set>
                          </c:when>
                          <c:otherwise>
                            <c:set var="subjectName">
                              <fmt:message key="generic.subjectFormatterNoEducationType">
                                <fmt:param value="${subject.code}"/>
                                <fmt:param value="${subject.name}"/>
                              </fmt:message>
                            </c:set>
                          </c:otherwise>
                        </c:choose>

                        <c:choose>
                          <c:when test="${subject.id eq module.subject.id}">
                            <option value="${subject.id}" selected="selected">${subjectName}</option>
                          </c:when>
                          <c:otherwise>
                            <option value="${subject.id}">${subjectName}</option> 
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>

                      <c:if test="${course.subject.archived and module.subject.educationType.id eq educationType.id}">
                        <option value="${course.subject.id}" selected="selected">${subjectName}*</option>
                      </c:if>
                    </optgroup>
                  </c:if>
                </c:forEach>

                <c:forEach var="subject" items="${subjectsByNoEducationType}">
                  <c:choose>
                    <c:when test="${empty subject.code}">
                      <c:set var="subjectName">${subject.name}</c:set>
                    </c:when>
                    <c:otherwise>
                      <c:set var="subjectName">
                        <fmt:message key="generic.subjectFormatterNoEducationType">
                          <fmt:param value="${subject.code}"/>
                          <fmt:param value="${subject.name}"/>
                        </fmt:message>
                      </c:set>
                    </c:otherwise>
                  </c:choose>

                  <c:choose>
                    <c:when test="${subject.id eq module.subject.id}">
                      <option value="${subject.id}" selected="selected">${subjectName}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${subject.id}">${subjectName}</option> 
                    </c:otherwise>
                  </c:choose>
                </c:forEach>

                <c:if test="${module.subject.archived and module.subject.educationType.id eq null}">
                  <option value="${module.subject.id}" selected="selected">${module.subject.name} (${module.subject.code})*</option>
                </c:if>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.courseNumberTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.courseNumberHelp"/>
              </jsp:include>

              <input type="text" name="courseNumber" value="${module.courseNumber}" size="2">
            </div>

            <!-- 
              TODO: Remove table
            -->
            <table>
              <tr>
                <td>
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="courses.createCourse.beginsTitle"/>
                      <jsp:param name="helpLocale" value="courses.createCourse.beginsHelp"/>
                    </jsp:include>    
                  </td>
                <td>
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="courses.createCourse.endsTitle"/>
                      <jsp:param name="helpLocale" value="courses.createCourse.endsHelp"/>
                    </jsp:include>    
                  </td>
                <td>
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="courses.createCourse.lengthTitle"/>
                      <jsp:param name="helpLocale" value="courses.createCourse.lengthHelp"/>
                    </jsp:include>    
                  </td>
              </tr>
              <tr>
                <td>
                  <input type="text" name="beginDate" class="ixDateField"/>
                </td>
                <td>
                  <input type="text" name="endDate" class="ixDateField"/>
                </td>
                <td>
                  <input type="text" class="float required" name="courseLength" value="${fn:escapeXml(module.courseLength.units)}" class="required" size="15"/>
                  <select name="courseLengthTimeUnit">           
                    <c:forEach var="courseLengthTimeUnit" items="${courseLengthTimeUnits}">
                      <option value="${courseLengthTimeUnit.id}" <c:if test="${module.courseLength.unit.id eq courseLengthTimeUnit.id}">selected="selected"</c:if>>${courseLengthTimeUnit.name}</option> 
                    </c:forEach>
                  </select>   
                </td>
              </tr>
            </table>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.localTeachingDaysTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.localTeachingDaysHelp"/>
              </jsp:include>

              <input type="text" class="float" name="localTeachingDays" size="5">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.distanceTeachingDaysTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.distanceTeachingDaysHelp"/>
              </jsp:include>

              <input type="text" class="float" name="distanceTeachingDays" size="5">
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.planningHoursTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.planningHoursHelp"/>
              </jsp:include>    
            
              <input type="text" class="float" name="planningHours" size="5">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.teachingHoursTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.teachingHoursHelp"/>
              </jsp:include>

              <input type="text" class="float" name="teachingHours" size="5">
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.distanceTeachingHoursTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.distanceTeachingHoursHelp"/>
              </jsp:include>

              <input type="text" class="float" name="distanceTeachingHours" size="5">
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.assessingHoursTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.assessingHoursHelp"/>
              </jsp:include>    
            
              <input type="text" class="float" name="assessingHours" size="5">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.maxParticipantsTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.maxParticipantsHelp"/>
              </jsp:include>    
            
              <input type="text" name="maxParticipantCount" size="3" value="${module.maxParticipantCount}">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.enrolmentTimeEndTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.enrolmentTimeEndHelp"/>
              </jsp:include>    
            
              <input type="text" name="enrolmentTimeEnd" class="ixDateField">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.courseFeeTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.courseFeeHelp"/>
              </jsp:include>    
            
              <input type="number" min="1" step="any" name="courseFee" value="">
              <select name="courseFeeCurrency">
                <c:forEach var="currency" items="${currencies}" varStatus="vs">
                  <c:choose>
                    <c:when test="${vs.first}">
                      <option value="${currency.currencyCode}" selected="selected">${currency.currencyCode}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${currency.currencyCode}">${currency.currencyCode}</option>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.createCourse.descriptionTitle"/>
                <jsp:param name="helpLocale" value="courses.createCourse.descriptionHelp"/>
              </jsp:include>    

              <div class="tabLabelsContainer" id="descriptionTabs">
                <a class="tabLabel" href="#descGeneric"><fmt:message key="courses.editCourse.genericDescriptionTabTitle" /></a>

                <c:forEach var="cDesc" items="${courseDescriptions}">              
                  <a class="tabLabel" href="#courseDescription.${cDesc.category.id}">${cDesc.category.name}</a>
                </c:forEach>
              </div>
      
              <div id="descGeneric" class="tabContent">
                <textarea ix:cktoolbar="courseDescription" name="description" ix:ckeditor="true">${module.description}</textarea>
              </div>

              <c:forEach var="cDesc" items="${courseDescriptions}">              
                <div id="courseDescription.${cDesc.category.id}" class="tabContent">
                  <input type="hidden" name="courseDescription.${cDesc.category.id}.catId" id="courseDescription.${cDesc.category.id}.catId" value="${cDesc.category.id}"/>
                  <textarea ix:cktoolbar="courseDescription" name="courseDescription.${cDesc.category.id}.text" ix:ckeditor="true">${cDesc.description}</textarea>
                </div>
              </c:forEach>
            </div>

            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="openSearchUsersDialog();"><fmt:message key="courses.createCourse.addPersonLink"/></span>
            </div>
            <div id="personnelTable"> </div>

            <ix:extensionHook name="courses.createCourse.tabs.basic"/>
          </div>
      
          <div id="components" class="tabContentixTableFormattedData hiddenTab">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addNewComponent();"><fmt:message key="courses.createCourse.addComponentLink"/></span>
            </div>
              
            <div id="noComponentsAddedMessageContainer" class="genericTableAddRowContainer">
              <span><fmt:message key="courses.createCourse.noComponentsAddedPreFix"/> <span onclick="addNewComponent();" class="genericTableAddRowLink"><fmt:message key="courses.createCourse.noComponentsAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="createCourseComponentsList">
              
            </div>

            <!-- TODO Lankinen taitaa komponenttien yhteistunnit kauniiksi -->
            <div id="componentHoursTotalContainer" style="display:none;">
              <span><fmt:message key="courses.createCourse.totalComponentHoursLabel"/></span>
              <span id="componentHoursTotalValueContainer">0</span>
            </div>
            <ix:extensionHook name="courses.createCourse.tabs.components"/>
          </div>
      
          <div id="costplan" class="tabContent hiddenTab">
      
            <div id="courseIncomeContainer">
              <div id="courseIncomesTitle" class="genericFormTitle">
                <div class="genericFormTitleText">
                  <fmt:message key="courses.createCourse.courseIncomesTitle"/>
                </div>
              </div>
            
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText">
                    <fmt:message key="courses.createCourse.incomePerCourseIncomesTitle" />
                  </div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addCourseIncomeRow();"></div>
              
                <div id="courseIncomesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.createCourse.courseIncomesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="courseIncomesTableTotal"> 0 </div> 
                </div>
              </div>
          
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText">
                    <fmt:message key="courses.createCourse.incomePerStudentIncomesTitle" />
                  </div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addStudentIncomeRow();"></div>
              
                <div id="studentIncomesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.createCourse.studentIncomesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="studentIncomesTableTotal"> 0 </div> 
                </div>
              </div>
              
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText">
                    <fmt:message key="courses.createCourse.incomePerGradeIncomesTitle" />
                  </div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addGradeIncomeRow();"></div>
              
                <div id="gradeIncomesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.createCourse.gradeIncomesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="gradeIncomesTableTotal"> 0 </div> 
                </div>
              </div>
        
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.createCourse.otherIncomesTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addOtherIncomeRow();"></div>
              
                <div id="otherIncomesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.createCourse.otherIncomesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="otherIncomesTableTotal"> 0 </div> 
                </div>
              </div>
              
              <div id="courseIncomeTotalContainer">
                <div id="courseIncomeTotalTitle">
                  <fmt:message key="courses.createCourse.courseIncomeTotalTitle"/>
                </div>
                <div id="courseIncomeTotalValue"> 0 </div>
              </div>
              
            </div>
            
            <div id="courseCostContainer">
            
              <div id="courseCostsTitle" class="genericFormTitle">
                <div class="genericFormTitleText"><fmt:message key="courses.createCourse.courseCostsTitle"/></div>
              </div>
           
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.createCourse.resourcesPerCourseCostTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="openSearchResourcesDialog('basicResourcesTable');"></div>
                <div id="basicResourcesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle"> 
                    <fmt:message key="courses.createCourse.basicResourcesTableTotalTitle"/>
                   </div> 
                  <div class="courseCostsPlanTableTotalValue" id="basicResourcesTableTotal"> 0 </div> 
                </div>
              </div>
           
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.createCourse.resourcesPerStudentCostTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="openSearchResourcesDialog('studentResourcesTable');"></div>
              
                <div id="studentResourcesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle"> 
                     <fmt:message key="courses.createCourse.studentResourcesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="studentResourcesTableTotal"> 0 </div> 
                </div>
              </div>
          
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.createCourse.resourcesPerGradeCostTitle" /> </div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="openSearchResourcesDialog('gradeResourcesTable');"></div>
                <div id="gradeResourcesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.createCourse.gradeResourcesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="gradeResourcesTableTotal"> 0 </div> 
                </div>
              </div>
          
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.createCourse.otherCostsTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addOtherCostRow();"></div>
              
                <div id="otherCostsTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.createCourse.otherCostsTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="otherCostsTableTotal"> 0 </div> 
                </div>
              </div>
          
              <div id="courseCostsTotalContainer">
                <div id="courseCostsTotalTitle">
                  <fmt:message key="courses.createCourse.courseCostsTotalTitle"/>
                </div>
                <div id="courseCostsTotalValue"> 0 </div>
              </div>
              
            </div>
            <div style="clear:both; height:1px;"></div>
            
            <ix:extensionHook name="courses.createCourse.tabs.costPlan"/>
          </div>
      
          <div id="students" class="tabContentixTableFormattedData hiddenTab">
            <div class="courseStudentsTableContainer">
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" onclick="openSearchStudentsDialog();"><fmt:message key="courses.createCourse.addStudentLink"/></span>
              </div>
                
              <div id="noStudentsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
                <span><fmt:message key="courses.createCourse.noStudentsAddedPreFix"/> <span onclick="openSearchStudentsDialog();" class="genericTableAddRowLink"><fmt:message key="courses.createCourse.noStudentsAddedClickHereLink"/></span>.</span>
              </div>
            
              <div id="courseStudentsTable"> </div>

              <div id="createCourseStudentsTotalContainer" style="display:none;">
                <fmt:message key="courses.createCourse.studentsTotal"/> <span id="createCourseStudentsTotalValue"></span>
              </div>

            </div>
            <ix:extensionHook name="courses.createCourse.tabs.students"/>
          </div>

          <ix:extensionHook name="courses.createCourse.tabs"/>
      
          <div class="genericFormSubmitSectionOffTab"><input type="submit" class="formvalid" value="<fmt:message key="courses.createCourse.saveButton"/>"></div>
  
        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>