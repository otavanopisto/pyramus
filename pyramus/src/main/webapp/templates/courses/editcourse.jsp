<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="courses.editCourse.pageTitle">
        <fmt:param value="${course.name}"/>
      </fmt:message>
    </title>
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
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ajax_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/editcourse.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/coursecomponenteditor.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/coursecomponentseditor.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/coursecomponenteditordrafttask.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/coursemoduleutils.js"></script>

    <script type="text/javascript">

      var archivedStudentRowIndex;
      var archivedComponentRowIndex;
      var componentsEditor;

      function getCourseComponentsEditor() {
        return componentsEditor;
      }

      ActiveStudentsFilter = Class.create(_IxTable_FILTER, {
        initialize : function($super, column) {
          $super(column);
        },
        execute: function ($super, event) {
          var studentsTable = event.tableComponent;
          var hideArray = new Array();
          
          for (var i = studentsTable.getRowCount() - 1; i >= 0; i--) {
            var rowValue = studentsTable.getCellValue(i, this.getColumn());
            if (rowValue != 10) {
              hideArray.push(i);
            }
          }

          if (hideArray.size() > 0)
            studentsTable.hideRows(hideArray.toArray());
        }
      });
      
      function filterOnlyActiveStudents() {
        var studentsTable = getIxTableById('studentsTable');
        var stateColumn = studentsTable.getNamedColumnIndex('participationType');

        studentsTable.clearFilters();
        
        studentsTable.addFilter(new ActiveStudentsFilter(stateColumn));
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
                var resourceUnitCost = event.results.resources[i].unitCost;
                var resourceHourlyCost = event.results.resources[i].hourlyCost;
                var index = getResourceRowIndex(targetTableId, resourceId);
                if (index == -1) {
                  resourcesTable.addRow([-1, resourceId, resourceName, 0, resourceHourlyCost, 0, resourceUnitCost, 0, '']);
                }
              }
              resourcesTable.reattachToDom();
            break;
          }
        });
        dialog.open();
      }

      function addNewCourseStudent(personId, studentId, studentName) {
        var table = getIxTableById('studentsTable');
        var rowIndex = table.addRow(['', '', studentName, studentId, 10, new Date().getTime(), 0, '', 'false', personId, -1, 1, '', '', '', '']);
        table.hideCell(rowIndex, table.getNamedColumnIndex('detailsButton'));
        table.hideCell(rowIndex, table.getNamedColumnIndex('evaluateButton'));
        for (var i = 3; i < 9; i++) {
          table.setCellEditable(rowIndex, i, true);
        }
        loadStudentStudyProgrammes(rowIndex, studentId);
        $('noStudentsAddedMessageContainer').setStyle({
          display: 'none'
        });
        $('editCourseStudentsTotalContainer').setStyle({
          display: ''
        });
        $('editCourseStudentsTotalValue').innerHTML = table.getRowCount(); 
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
        
        dialog.setSize("800px", "700px");
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
                var index = getStudentRowIndex(studentId);
                if (index == -1) {
                  addNewCourseStudent(personId, studentId, studentName);
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
          var columnIndex = table.getNamedColumnIndex('resourceId');
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableResourceId = table.getCellValue(i, columnIndex);
            if (tableResourceId == resourceId) {
              return i;
            }
          }
        }
        return -1;
      }

      function getStudentRowIndex(studentId) {
        var table = getIxTableById('studentsTable');
        if (table) {
          var columnIndex = table.getNamedColumnIndex('studentId');
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableStudentId = table.getCellValue(i, columnIndex);
            if (tableStudentId == studentId) {
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

      function setupResources() {

        // Base course resources

        var basicResourcesTable = getIxTableById('basicResourcesTable');
        <c:forEach var="courseResource" items="${course.basicCourseResources}">
          basicResourcesTable.addRow([
            ${courseResource.id},
            ${courseResource.resource.id},
            '${fn:escapeXml(courseResource.resource.name)}',
            ${courseResource.hours},
            ${courseResource.hourlyCost.amount},
            ${courseResource.units},
            ${courseResource.unitCost.amount},
            '',
            '']);
        </c:forEach>

        // Student course resources

        var studentResourcesTable = getIxTableById('studentResourcesTable');
        <c:forEach var="courseResource" items="${course.studentCourseResources}">
          studentResourcesTable.addRow([
            ${courseResource.id},
            ${courseResource.resource.id},
            '${fn:escapeXml(courseResource.resource.name)}',
            ${courseResource.hours},
            ${courseResource.hourlyCost.amount},
            ${courseResource.units},
            ${courseResource.unitCost.amount},
            '',
            '']);
        </c:forEach>

        // Grade course resources

        var gradeResourcesTable = getIxTableById('gradeResourcesTable');
        <c:forEach var="courseResource" items="${course.gradeCourseResources}">
          gradeResourcesTable.addRow([
            ${courseResource.id},
            ${courseResource.resource.id},
            '${fn:escapeXml(courseResource.resource.name)}',
            ${courseResource.hours},
            ${courseResource.hourlyCost.amount},
            ${courseResource.units},
            ${courseResource.unitCost.amount},
            '',
            '']);
        </c:forEach>

        // Other costs

        var otherCostsTable = getIxTableById('otherCostsTable');
        <c:forEach var="otherCost" items="${course.otherCosts}">
          otherCostsTable.addRow([
            ${otherCost.id},
            '${fn:escapeXml(otherCost.name)}',
            ${otherCost.cost.amount},
            '']);
        </c:forEach>
      }

      // Personnel
      
      function setupPersonnelTable() {
        var personnelTable = new IxTable($('personnelTable'), {
          id : "personnelTable",
          columns : [{
            dataType: 'hidden',
            paramName: 'courseUserId'
          }, {
            dataType: 'hidden',
            paramName: 'userId'
          }, {
            header : '<fmt:message key="courses.editCourse.personnelTablePersonHeader"/>',
            left : 8,
            width: 250,
            dataType : 'text',
            editable: false,
            paramName: 'userName'
          }, {
            header : '<fmt:message key="courses.editCourse.personnelTableRoleHeader"/>',
            width: 200,
            left : 266,
            dataType: 'select',
            editable: true,
            paramName: 'role',
            required: true,
            options: [
              {text: getLocale().getText("courseroles.TEACHER"), value: "TEACHER"},
              {text: getLocale().getText("courseroles.TUTOR"), value: "TUTOR"},
              {text: getLocale().getText("courseroles.ORGANIZER"), value: "ORGANIZER"}
            ]
          }, {
            left: 474,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.editCourse.personnelTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            } 
          }]        
        });
        <c:forEach var="courseUser" items="${courseUsers}">
          personnelTable.addRow([
            ${courseUser.id},
            ${courseUser.staffMember.id},
            '${fn:escapeXml(courseUser.staffMember.lastName)}, ${fn:escapeXml(courseUser.staffMember.firstName)}',
            '${courseUser.role}',
            ''
          ]);
        </c:forEach>
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
                  personnelTable.addRow([-1, userId, userName, '', '']);
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
          var columnIndex = table.getNamedColumnIndex('userId');
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableUserId = table.getCellValue(i, columnIndex);
            if (tableUserId == userId) {
              return i;
            }
          }
        }
        return -1;
      }
      
      // Course components
      
      function setupComponents() {
        componentsEditor = new CourseComponentsEditor($('editCourseComponentsList'), {
          paramName: 'components',
          componentHoursSumElement: $('componentHoursTotalValueContainer'),
          resourceSearchUrl: '${pageContext.request.contextPath}/resources/searchresourcesautocomplete.binary',
          resourceSearchParamName: 'query',
          resourceSearchProgressImageUrl: '${pageContext.request.contextPath}/gfx/progress_small.gif',
          nameHeader: '<fmt:message key="courses.editCourse.componentsNameHeader"/>',
          lengthHeader: '<fmt:message key="courses.editCourse.componentsLengthHeader"/>',
          componentUnit: '<fmt:message key="courses.editCourse.componentUnit"/>',
          materialResourceUnit: '<fmt:message key="courses.editCourse.componentsResourceMaterialResourceUnit"/>',
          workResourceUnit: '<fmt:message key="courses.editCourse.componentsResourceWorkResourceUnit"/>',
          descriptionHeader: '<fmt:message key="courses.editCourse.componentsDescriptionHeader"/>',
          editButtonTooltip: '<fmt:message key="courses.editCourse.componentsEditButtonTooltip"/>',
          removeButtonTooltip: '<fmt:message key="courses.editCourse.componentsRemoveButtonTooltip"/>',
          archiveButtonTooltip: '<fmt:message key="courses.editCourse.componentsArchiveButtonTooltip"/>',
          archiveConfirmTitle: '<fmt:message key="courses.editCourse.archiveComponentConfirmDialogTitle"/>',
          archiveConfirmContentLocale: 'courses.editCourse.archiveComponentConfirmDialogContent',
          archiveConfirmOkLabel: '<fmt:message key="courses.editCourse.archiveComponentConfirmDialogOkLabel"/>',
          archiveConfirmCancelLabel: '<fmt:message key="courses.editCourse.archiveComponentConfirmDialogCancelLabel"/>',
          resourceNameTitle: '<fmt:message key="courses.editCourse.componentsResourceNameTitle"/>',
          resourceUsageTitle: '<fmt:message key="courses.editCourse.componentsResourceUsageTitle"/>',
          resourceRemoveButtonTooltip: '<fmt:message key="courses.editCourse.componentsResourceRemoveButtonTooltip"/>',
          resourceArchiveButtonTooltip: '<fmt:message key="courses.editCourse.componentsResourceArchiveButtonTooltip"/>',
          resourceDeleteConfirmTitle: '<fmt:message key="courses.editCourse.componentsResourceDeleteConfirmDialogTitle"/>',
          resourceDeleteConfirmContentLocale: 'courses.editCourse.componentsResourceDeleteConfirmDialogContent',
          resourceDeleteConfirmOkLabel: '<fmt:message key="courses.editCourse.componentsResourceDeleteConfirmDialogOkLabel"/>',
          resourceDeleteConfirmCancelLabel: '<fmt:message key="courses.editCourse.componentsResourceDeleteConfirmDialogCancelLabel"/>',
          noResourcesMessage: "<fmt:message key="courses.editCourse.componentsNoResourcesMessage" />"
        });   
        
        <c:if test="${fn:length(courseComponents) gt 0}">
        
          $('noComponentsAddedMessageContainer').setStyle({
            display: 'none'
          });
          $('componentHoursTotalContainer').setStyle({
            display: ''
          });
          
          var componentEditor;
          var resourceCategory;
          
          <c:forEach var="component" items="${courseComponents}" varStatus="componentsVs">
            componentEditor = componentsEditor.addCourseComponent(
              ${component.id}, 
              '${fn:escapeXml(component.name)}', 
              ${component.length.units}, 
              '${fn:escapeXml(component.description)}');
            
            <c:forEach var="componentResource" items="${component.resources}">
              if (!componentEditor.hasResourceCategory(${componentResource.resource.category.id})) {
                resourceCategory = componentEditor.addResourceCategory(
                    ${componentResource.resource.category.id}, 
                    '${fn:escapeXml(componentResource.resource.category.name)}');
              }
                
              componentEditor.addResource(${componentResource.resource.category.id}, 
                  ${componentResource.id}, 
                  ${componentResource.resource.id},
                  '${componentResource.resource.resourceType}',
                  '${fn:escapeXml(componentResource.resource.name)}', 
                  ${componentResource.usagePercent},
                  ${componentResource.usagePercent / 100});
            </c:forEach>
            
          </c:forEach>
        </c:if>
      }
      
      function addNewComponent() {
        var componentEditor = componentsEditor.addCourseComponent(-1, '', 0, '');
        
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
          columns : [ {
            dataType : 'hidden',
            paramName : 'basicResourceId'
          }, {
            dataType : 'hidden',
            paramName : 'resourceId'
          }, {
            header : '<fmt:message key="courses.editCourse.basicResourcesTableNameHeader"/>',
            dataType : 'text',
            editable  : false,
            left : 0,
            right : 350,
            paramName : 'resourceName'
          }, {
            header : '<fmt:message key="courses.editCourse.basicResourcesTableHoursHeader"/>',
            dataType : 'number',
            editable: true,
            right : 310,
            width : 30,
            paramName : 'hours'
          }, {
            header : '<fmt:message key="courses.editCourse.basicResourcesTableHourlyCostHeader"/>',
            dataType : 'number',
            editable: true,
            right : 225,
            width : 70,
            paramName : 'hourlyCost'
          }, {
            header : '<fmt:message key="courses.editCourse.basicResourcesTableUnitsHeader"/>',
            dataType : 'number',
            editable: true,
            right : 180,
            width : 30,
            paramName : 'units'
          }, {
            header : '<fmt:message key="courses.editCourse.basicResourcesTableCostPerUnitHeader"/>',
            dataType : 'number',
            editable: true,
            right : 85,
            width : 80,
            paramName : 'unitCost'
          }, {
            header : '<fmt:message key="courses.editCourse.basicResourcesTableTotalHeader"/>',
            dataType : 'number',
            editable: false,
            right : 30,
            width : 40,
            paramName : 'total'
          }, {
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.editCourse.basicResourcesTableRemoveRowTooltip"/>',
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
          columns : [ {
            dataType : 'hidden',
            paramName : 'studentResourceId'
          }, {
            dataType : 'hidden',
            paramName : 'resourceId'
          }, {
            header : '<fmt:message key="courses.editCourse.studentResourcesTableNameHeader"/>',
            dataType : 'text',
            editable  : false,
            left : 0,
            right : 350,
            paramName : 'resourceName'
          }, {
            header : '<fmt:message key="courses.editCourse.studentResourcesTableHoursHeader"/>',
            dataType : 'number',
            editable: true,
            right : 310,
            width : 30,
            paramName : 'hours'
          }, {
            header : '<fmt:message key="courses.editCourse.studentResourcesTableHourlyCostHeader"/>',
            dataType : 'number',
            editable: true,
            right : 225,
            width : 70,
            paramName : 'hourlyCost'
          }, {
            header : '<fmt:message key="courses.editCourse.studentResourcesTableUnitsHeader"/>',
            dataType : 'number',
            editable: true,
            right : 180,
            width : 30,
            paramName : 'units'
          }, {
            header : '<fmt:message key="courses.editCourse.studentResourcesTableCostPerUnitHeader"/>',
            dataType : 'number',
            editable: true,
            right : 85,
            width : 80,
            paramName : 'unitCost'
          }, {
            header : '<fmt:message key="courses.editCourse.studentResourcesTableTotalHeader"/>',
            dataType : 'number',
            editable: false,
            right : 30,
            width : 40,
            paramName : 'total'
          }, {
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.editCourse.studentResourcesTableRemoveRowTooltip"/>',
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
          columns : [ {
            dataType : 'hidden',
            paramName : 'gradeResourceId'
          }, {
            dataType : 'hidden',
            paramName : 'resourceId'
          }, {
            header : '<fmt:message key="courses.editCourse.gradeResourcesTableNameHeader"/>',
            dataType : 'text',
            editable  : false,
            left : 0,
            right : 350,
            paramName : 'resourceName'
          }, {
            header : '<fmt:message key="courses.editCourse.gradeResourcesTableHoursHeader"/>',
            dataType : 'number',
            editable: true,
            right : 310,
            width : 30,
            paramName : 'hours'
          }, {
            header : '<fmt:message key="courses.editCourse.gradeResourcesTableHourlyCostHeader"/>',
            dataType : 'number',
            editable: true,
            right : 225,
            width : 70,
            paramName : 'hourlyCost'
          }, {
            header : '<fmt:message key="courses.editCourse.gradeResourcesTableUnitsHeader"/>',
            dataType : 'number',
            editable: true,
            right : 180,
            width : 30,
            paramName : 'units'
          }, {
            header : '<fmt:message key="courses.editCourse.gradeResourcesTableCostPerUnitHeader"/>',
            dataType : 'number',
            editable: true,
            right : 85,
            width : 80,
            paramName : 'unitCost'
          }, {
            header : '<fmt:message key="courses.editCourse.gradeResourcesTableTotalHeader"/>',
            dataType : 'number',
            editable: false,
            right : 30,
            width : 40,
            paramName : 'total'
          }, {
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.editCourse.gradeResourcesTableRemoveRowTooltip"/>',
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
          columns : [ {
            dataType : 'hidden',
            paramName : 'otherCostId'
          }, {
            header : '<fmt:message key="courses.editCourse.otherCostsTableNameHeader"/>',
            dataType : 'text',
            editable: true,
            left : 0,
            right : 120,
            paramName : 'name',
            required: true
          }, {
            header : '<fmt:message key="courses.editCourse.otherCostsTableCostHeader"/>',
            dataType : 'number',
            editable: true,
            right : 37,
            width : 78,
            paramName : 'cost'
          }, {
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.editCourse.otherCostsTableRemoveRowTooltip"/>',
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
        getIxTableById('otherCostsTable').addRow( [ -1, '', 0, '' ]);
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
            tooltip: '<fmt:message key="courses.editCourse.studentsTableStudentInfoTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var personId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              var button = table.getCellEditor(event.row, table.getNamedColumnIndex('studentInfoButton'));
              openStudentInfoPopupOnElement(button, personId);
            } 
          }, {
            left: 8 + 22 + 8,
            width: 22,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="courses.editCourse.studentsTableEditStudentTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var modified = table.getCellValue(event.row, table.getNamedColumnIndex('modified'));
              if (modified != 1) {
                var studentId = table.getCellValue(event.row, table.getNamedColumnIndex('studentId'));
                table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
                for (var i = 3; i < 9; i++) {
                  table.setCellEditable(event.row, i, true);
                }
              }
            }
          }, {
            header : '<fmt:message key="courses.editCourse.studentsTableNameHeader"/>',
            left : 8 + 22 + 8 + 8 + 22 + 8,
            right : 8 + 22 + 8 + 8 + 22 + 8 + 100 + 8 + 140 + 8 + 140 + 8 + 145 + 8 + 160 + 8 + 160,
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
            header : '<fmt:message key="courses.editCourse.studentsTableStudyProgrammeHeader"/>',
            width: 160,
            right : 8 + 22 + 8 + 8 + 22 + 8 + 100 + 8 + 140 + 8 + 140 + 8 + 145 + 8 + 160,
            dataType : 'select',
            editable: false,
            dynamicOptions: true,
            paramName: 'studentId',
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
            header : '<fmt:message key="courses.editCourse.studentsTableParticipationTypeHeader"/>',
            width: 160,
            right : 8 + 22 + 8 + 8 + 22 + 8 + 100 + 8 + 140 + 8 + 140 + 8 + 145,
            dataType : 'select',
            editable: false,
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
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(function (table, row) {
                  var col = table.getNamedColumnIndex('modified');
                  var modified = table.getCellValue(row, col);
                  return (!(modified == 1));
                })
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              },
              {
                text: '-'
              },
              {
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ]            
          }, {
            header : '<fmt:message key="courses.editCourse.studentsTableEnrolmentDateHeader"/>',
            width: 145,
            right : 8 + 22 + 8 + 8 + 22 + 8 + 100 + 8 + 140 + 8 + 140,
            dataType: 'date',
            editable: false,
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
                text: '<fmt:message key="generic.filter.earlier"/>',
                onclick: new IxTable_ROWDATEFILTER(true)
              },
              {
                text: '<fmt:message key="generic.filter.later"/>',
                onclick: new IxTable_ROWDATEFILTER(false)
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              },
              {
                text: '-'
              },
              {
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ]            
          }, {
            header : '<fmt:message key="courses.editCourse.studentsTableEnrolmentTypeHeader"/>',
            width: 140,
            right : 8 + 22 + 8 + 8 + 22 + 8 + 100 + 8 + 140,
            dataType: 'select', 
            editable: false,
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
            header : '<fmt:message key="courses.editCourse.studentsTableOptionalityHeader"/>',
            right :  8 + 22 + 8 + 8 + 22 + 8 + 100,
            width: 140,
            dataType : 'select',
            paramName: 'optionality',
            editable: false,
            options: [
              {text: '', value: ''},
              {text: '<fmt:message key="courses.editCourse.studentsTableOptionalityMandatory"/>', value: 'MANDATORY'},
              {text: '<fmt:message key="courses.editCourse.studentsTableOptionalityOptional"/>', value: 'OPTIONAL'}
            ],
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(function (table, row) {
                  var col = table.getNamedColumnIndex('modified');
                  var modified = table.getCellValue(row, col);
                  return (!(modified == 1));
                })
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              },
              {
                text: '-'
              },
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
            header : '<fmt:message key="courses.editCourse.studentsTableLodgingHeader"/>',
            width: 100,
            right : 8 + 22 + 8 + 8 + 22 + 30,
            dataType: 'select', 
            editable: false,
            paramName: 'lodging',
            options: [
              {text: '<fmt:message key="courses.editCourse.studentsTableLodgingYes"/>', value: 'true'},
              {text: '<fmt:message key="courses.editCourse.studentsTableLodgingNo"/>', value: 'false'}
            ],
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(function (table, row) {
                  var col = table.getNamedColumnIndex('modified');
                  var modified = table.getCellValue(row, col);
                  return (!(modified == 1));
                })
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              },
              {
                text: '-'
              },
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
            dataType: 'hidden', 
            paramName: 'courseStudentId'
          }, {
            dataType: 'hidden', 
            paramName: 'modified'
          }, {
            width: 22,
            right: 8 + 22 + 8 + 30,
            dataType: 'button',
            paramName: 'detailsButton',
            imgsrc: GLOBAL_contextPath + '/gfx/info.png',
            tooltip: '<fmt:message key="courses.editCourse.studentsTableStudentDetailsTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('courseStudentId'));
              openStudentDetailsDialog(courseStudentId);
            } 
          }, {
            width: 22,
            right: 8 + 22 + 8,
            dataType: 'button',
            paramName: 'evaluateButton',
            imgsrc: GLOBAL_contextPath + '/gfx/kdb_form.png',
            tooltip: '<fmt:message key="courses.editCourse.studentsTableEvaluateStudentTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('courseStudentId'));
              redirectTo(GLOBAL_contextPath + '/grading/courseassessment.page?courseStudentId=' + courseStudentId);
            } 
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="courses.editCourse.studentsTableRemoveRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              table.deleteRow(event.row);
              if (table.getRowCount() > 0) {
                $('noStudentsAddedMessageContainer').setStyle({
                  display: 'none'
                });
                $('editCourseStudentsTotalContainer').setStyle({
                  display: ''
                });
                $('editCourseStudentsTotalValue').innerHTML = table.getRowCount(); 
              }
              else {
                $('editCourseStudentsTotalContainer').setStyle({
                  display: 'none'
                });
                $('noStudentsAddedMessageContainer').setStyle({
                  display: ''
                });
              }
            } 
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            paramName: 'archiveButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="courses.editCourse.studentsTableArchiveRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('courseStudentId'));
              var studentName = table.getCellValue(event.row, table.getNamedColumnIndex('studentName'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=courses.editCourse.studentArchiveConfirmDialogContent&localeParams=" + encodeURIComponent(studentName);

              archivedStudentRowIndex = event.row; 
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="courses.editCourse.studentArchiveConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="courses.editCourse.studentArchiveConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="courses.editCourse.studentArchiveConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener(function(event) {
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("courses/archivecoursestudent.json", {
                      parameters: {
                        courseStudentId: courseStudentId
                      },
                      onSuccess: function (jsonResponse) {
                        var table = getIxTableById('studentsTable');
                        table.deleteRow(archivedStudentRowIndex);
                        if (table.getRowCount() > 0) {
                          $('noStudentsAddedMessageContainer').setStyle({
                            display: 'none'
                          });
                          $('editCourseStudentsTotalContainer').setStyle({
                            display: ''
                          });
                          $('editCourseStudentsTotalValue').innerHTML = table.getRowCount(); 
                        }
                        else {
                          $('editCourseStudentsTotalContainer').setStyle({
                            display: 'none'
                          });
                          $('noStudentsAddedMessageContainer').setStyle({
                            display: ''
                          });
                        }
                      }
                    });   
                  break;
                }
              });
            
              dialog.open();
            } 
          }]        
        });

        var rowIndex;
        var cellEditor; 
        var selectController = IxTableControllers.getController('select');
        var studentIdColumnIndex = studentsTable.getNamedColumnIndex('studentId');
        studentsTable.detachFromDom();
        
        <c:forEach var="courseStudent" items="${courseStudents}" varStatus="status">
          rowIndex = studentsTable.addRow([
            '',
            '',
            "${fn:escapeXml(courseStudent.student.lastName)}, ${fn:escapeXml(courseStudent.student.firstName)}",
            ${courseStudent.student.id},
            ${courseStudent.participationType.id}, 
            ${courseStudent.enrolmentTime.time}, 
            ${courseStudent.courseEnrolmentType.id},
            '${courseStudent.optionality}',
            '${courseStudent.lodging}',
            ${courseStudent.student.person.id},
            ${courseStudent.id},
            0,
            '',
            '',
            '',
            '']);

          cellEditor = studentsTable.getCellEditor(rowIndex, studentIdColumnIndex);
          
          <c:forEach var="courseStudentStudent" items="${courseStudentsStudents[courseStudent.id]}">
          <c:choose>
            <c:when test="${courseStudentStudent.studyProgramme ne null}">
              <c:set var="studyProgrammeName">${fn:escapeXml(courseStudentStudent.studyProgramme.name)}</c:set>
            </c:when>
            <c:otherwise>
              <c:set var="studyProgrammeName"><fmt:message key="courses.editCourse.studentsTableNoStudyProgrammeLabel"/></c:set>
            </c:otherwise>
          </c:choose>
  
          <c:if test="${courseStudentStudent.hasFinishedStudies}">
            <c:set var="studyProgrammeName">${studyProgrammeName} *</c:set>
          </c:if>
            
            selectController.addOption(cellEditor, 
                ${courseStudentStudent.id},
                '${fn:escapeXml(studyProgrammeName)}',
                ${courseStudent.student.id eq courseStudentStudent.id}
             );
          </c:forEach>
          
          studentsTable.showCell(rowIndex, studentsTable.getNamedColumnIndex("archiveButton"));
        </c:forEach>

        studentsTable.reattachToDom();

        studentsTable.addListener("rowAdd", function (event) {
          var studentsTable = event.tableComponent;
          studentsTable.showCell(event.row, studentsTable.getNamedColumnIndex("removeButton"));
        });

        var noStudentsAddedMessageContainer = $('noStudentsAddedMessageContainer');
        var editCourseStudentsTotalContainer = $('editCourseStudentsTotalContainer');
        
        <c:choose>
          <c:when test="${fn:length(courseStudents) gt 0}">
            if (noStudentsAddedMessageContainer) {
              noStudentsAddedMessageContainer.setStyle({
                display: 'none'
              });
            }
            if (editCourseStudentsTotalContainer) {
              editCourseStudentsTotalContainer.setStyle({
                display: ''
              });
            }
            $('editCourseStudentsTotalValue').innerHTML = studentsTable.getRowCount(); 
          </c:when>
          <c:otherwise>
            if (editCourseStudentsTotalContainer) {
              editCourseStudentsTotalContainer.setStyle({
                display: 'none'
              });
            }
            if (noStudentsAddedMessageContainer) {
              noStudentsAddedMessageContainer.setStyle({
                display: ''
              });
            }
          </c:otherwise>
        </c:choose>
      }

      function loadStudentStudyProgrammes(rowIndex, studentId) {
        var studentsTable = getIxTableById('studentsTable');
        var personId = studentsTable.getCellValue(rowIndex, studentsTable.getNamedColumnIndex('personId'));
        JSONRequest.request("students/getstudentstudyprogrammes.json", {
          asynchronous: false,
          parameters: {
            personId: personId
          },
          onSuccess: function (jsonResponse) {
            var cellEditor = studentsTable.getCellEditor(rowIndex, studentsTable.getNamedColumnIndex('studentId'));
            for (var j = 0, l = jsonResponse.studentStudyProgrammes.length; j < l; j++) {
              IxTableControllers.getController('select').addOption(cellEditor, 
                  jsonResponse.studentStudyProgrammes[j].studentId, 
                  jsonResponse.studentStudyProgrammes[j].studyProgrammeName, 
                  jsonResponse.studentStudyProgrammes[j].studentId == studentId);
            }
          }
        });   
      }
      
      function initializeDraftListener() {
        Event.observe(document, "ix:draftRestore", function (event) {
          var table = getIxTableById('studentsTable');
          if (table.getRowCount() > 0) {
            $('editCourseStudentsTotalContainer').setStyle({
              display: ''
            });
            $('noStudentsAddedMessageContainer').setStyle({
              display: 'none'
            });
            $('editCourseStudentsTotalValue').innerHTML = table.getRowCount(); 
          }
          else {
            $('editCourseStudentsTotalContainer').setStyle({
              display: 'none'
            });
            $('noStudentsAddedMessageContainer').setStyle({
              display: ''
            });
          }
        });
      }

      function setupRelatedCommands() {
        var basicRelatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="courses.editCourse.basicTabRelatedActionsLabel"/>'
        });
    
        basicRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/eye.png',
          text: '<fmt:message key="courses.editCourse.viewCourseRelatedActionLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/courses/viewcourse.page?course=${course.id}');
          }
        }));          

        basicRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="courses.editCourse.manageCourseAssessmentsRelatedActionLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/courses/managecourseassessments.page?course=${course.id}');
          }
        }));          

        basicRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="courses.editCourse.changeCourseModuleRelatedActionLabel"/>',
          onclick: function (event) {
            var courseId = ${course.id};

            var dialog = new IxDialog({
              id : 'changeModuleDialog',
              contentURL : GLOBAL_contextPath + '/courses/changemoduledialog.page?course=${course.id}',
              centered : true,
              showOk : true,  
              showCancel : true,
              title : '<fmt:message key="courses.editCourse.changeCourseModuleDialogTitle"/>',
              okLabel : '<fmt:message key="courses.editCourse.changeCourseModuleDialogOkLabel"/>',
              cancelLabel : '<fmt:message key="courses.editCourse.changeCourseModuleDialogCancelLabel"/>'
            });
          
            dialog.setSize("400px", "620px");
            dialog.addDialogListener(function(event) {
              var dlg = event.dialog;
          
              switch (event.name) {
                case 'onLoad':
                  dlg.disableOkButton();
                break;
                case 'okClick':
                  if (event.results.selectedModule) {
                    var captionElement = $("moduleName");
                    var inputElement = $("moduleId");
                    captionElement.update(event.results.selectedModule.name);
                    inputElement.value = event.results.selectedModule.id;
                  }
                break;
              }
            });
          
            dialog.open();
          }
        }));          
        
        basicRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/edit-delete.png',
          text: '<fmt:message key="courses.editCourse.archiveCourseRelatedActionLabel"/>',
          onclick: function (event) {
            var courseId = ${course.id};
            var courseName = '${fn:escapeXml(course.name)}';
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=courses.editCourse.courseArchiveConfirmDialogContent&localeParams=" + encodeURIComponent(courseName);
               
            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,  
              showCancel : true,
              autoEvaluateSize: true,
              title : '<fmt:message key="courses.editCourse.courseArchiveConfirmDialogTitle"/>',
              okLabel : '<fmt:message key="courses.editCourse.courseArchiveConfirmDialogOkLabel"/>',
              cancelLabel : '<fmt:message key="courses.editCourse.courseArchiveConfirmDialogCancelLabel"/>'
            });
          
            dialog.addDialogListener( function(event) {
              var dlg = event.dialog;
          
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("courses/archivecourse.json", {
                    parameters: {
                      courseId: courseId
                    },
                    onSuccess: function (jsonResponse) {
                      window.location = GLOBAL_contextPath + '/index.page?resetbreadcrumb=1';
                    }
                  });   
                break;
              }
            });
          
            dialog.open();
          }
        }));          

        /* Extension hook links */
        
        var extensionHoverMenuLinks = $$('#extensionHoverMenuLinks a');
        for (var i=0, l=extensionHoverMenuLinks.length; i<l; i++) {
          var extensionHoverMenuLink = extensionHoverMenuLinks[i];
          basicRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
            iconURL: GLOBAL_contextPath + '/gfx/eye.png',
            text: extensionHoverMenuLink.innerHTML,
            link: GLOBAL_contextPath + extensionHoverMenuLink.href
          }));
        }
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
        var courseId = ${course.id};
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
        
        initializeDraftListener();
        setupTags();
        setupRelatedCommands();
        setupPersonnelTable();
        setupComponents();
        setupBasicResourcesTable();
        setupStudentResourcesTable();
        setupGradeResourcesTable();
        setupOtherCostsTable();
        setupResources();
        setupStudentsTable();

        var courseModulesData = JSDATA["courseModules"].evalJSON();
        var courseModulesTable = setupCourseModulesTable();
        var courseModulesRows = [];
        for (var i = 0, l = courseModulesData.length; i < l; i++) {
          var courseModule = courseModulesData[i];
          courseModulesRows.push([
            courseModule.subject ? courseModule.subject.id : '',
            courseModule.courseNumber,
            courseModule.courseLength ? courseModule.courseLength.units : '',
            (courseModule.courseLength && courseModule.courseLength.unit) ? courseModule.courseLength.unit.id : '',
            '',
            courseModule.id
          ]);
        }
        courseModulesTable.addRows(courseModulesRows);

        initializeSignupStudyProgrammesTable($('signupStudyProgrammesTable'), courseId);
        initializeSignupStudyProgrammeSearchField(courseId, $('searchStudyProgrammesInput'), $('searchStudyProgrammesInputAutoComplete'), $('searchSignupStudyProgrammes_indicator'));
        
        initializeSignupStudentGroupsTable($('signupStudentGroupsTable'), courseId);
        initializeSignupStudentGroupSearchField(courseId, $('searchStudentGroupsInput'), $('searchStudentGroupsInputAutoComplete'), $('searchSignupStudentGroups_indicator'));
        
        //filterOnlyActiveStudents();
      }

    </script>
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">
      <fmt:message key="courses.editCourse.pageTitle">
        <fmt:param value="${course.name}"/>
      </fmt:message>
    </h1>
    
    <div id="editCourseEditFormContainer">
      <div class="genericFormContainer">
        <form action="editcourse.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <input type="hidden" name="course" value="${course.id}"/>
          <input type="hidden" name="version" value="${course.version}"/>

          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic"><fmt:message key="courses.editCourse.basicTabTitle" /></a>
            <a class="tabLabel" href="#components"><fmt:message key="courses.editCourse.componentsTabTitle" /></a>
            <a class="tabLabel" href="#costplan"><fmt:message key="courses.editCourse.costPlanTabTitle" /></a>
            <a class="tabLabel" href="#students"><fmt:message key="courses.editCourse.StudentsTabTitle" /></a>
            <a class="tabLabel" href="#signup"><fmt:message key="courses.editCourse.signupsTabTitle" /></a>
            <ix:extensionHook name="courses.editCourse.tabLabels"/>
          </div>

          <div id="basic" class="tabContent">
            <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
            <div id="extensionHoverMenuLinks" style="display: none;">
              <ix:extensionHook name="courses.editCourse.basic.hoverMenuLinks" />
            </div>
          
            <!--  TODO italic tags to css -->
          
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.createdTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.createdHelp"/>
              </jsp:include>
              <span><i>${course.creator.fullName} <fmt:formatDate type="both" value="${course.created}"/></i></span>    
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.modifiedTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.modifiedHelp"/>
              </jsp:include>
              <span><i>${course.lastModifier.fullName} <fmt:formatDate type="both" value="${course.lastModified}"/></i></span>    
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.moduleTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.moduleHelp"/>
              </jsp:include>    
              <span><i id="moduleName">${course.module.name}</i></span>
              <input type="hidden" id="moduleId" name="moduleId" value="${course.module.id}" />
            </div>
      
            <c:if test="${loggedUserRole == 'ADMINISTRATOR'}">
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="courses.editCourse.isCourseTemplateTitle"/>
                  <jsp:param name="helpLocale" value="courses.editCourse.isCourseTemplateHelp"/>
                </jsp:include>                  
                <select name="isCourseTemplate" class="required">
                  <option ${not course.courseTemplate ? 'selected="selected"' : ''} value="false"><fmt:message key="terms.no" /></option>
                  <option ${course.courseTemplate ? 'selected="selected"' : ''} value="true"><fmt:message key="terms.yes" /></option>
                </select>
              </div>
            </c:if>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="terms.organization"/>
              </jsp:include>                  
              <select name="organizationId" class="required">
                <option value=""></option>
                <c:forEach items="${organizations}" var="organization">
                  <c:choose>
                    <c:when test="${course.organization.id == organization.id}">
                      <option value="${organization.id}" selected="selected">${organization.name}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${organization.id}">${organization.name}</option>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.nameTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.nameHelp"/>
              </jsp:include>    
              <input type="text" class="required" name="name" value="${fn:escapeXml(course.name)}" size="40">
            </div>
             
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.nameExtensionTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.nameExtensionHelp"/>
              </jsp:include>    
              <input type="text" name="nameExtension" value="${fn:escapeXml(course.nameExtension)}" size="40">
            </div>
      
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.tagsTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40" value="${fn:escapeXml(tags)}"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.stateTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.stateHelp"/>
              </jsp:include>    
              <select name="state">           
                <c:forEach var="state" items="${states}">
                  <c:choose>
                    <c:when test="${state.id eq course.state.id}">
                      <option value="${state.id}" selected="selected">${state.name}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${state.id}">${state.name}</option> 
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.typeTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.typeHelp"/>
              </jsp:include>    
              <select name="type">           
                <option></option>
                <c:forEach var="type" items="${types}">
                  <c:choose>
                    <c:when test="${type.id eq course.type.id}">
                      <option value="${type.id}" selected="selected">${type.name}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${type.id}">${type.name}</option> 
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.educationTypesTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.educationTypesHelp"/>
              </jsp:include>
              <div class="editCourseFormSectionEducationType">
                <c:forEach var="educationType" items="${educationTypes}">
                  <div class="editCourseFormSectionEducationTypeCell">
                    <div class="editCourseFormSectionEducationTypeTitle">
                      <div class="editCourseFormSectionEducationTypeTitleText">${educationType.name}</div>
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
                <jsp:param name="titleLocale" value="courses.generic.courseModules.label"/>
                <jsp:param name="helpLocale" value="courses.generic.courseModules.labelHelp"/>
              </jsp:include>
              
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" onclick="addCourseModuleTableRow();"><fmt:message key="modules.editModule.addCourseModuleLink"/></span>
              </div>
              
              <div id="courseModulesTableContainer"></div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.curriculumTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.curriculumHelp"/>
              </jsp:include>
                
              <c:forEach var="curriculum" items="${curriculums}">
                <div>
                  <c:choose>
                    <c:when test="${fn:contains(course.curriculums, curriculum)}">
                      <input type="checkbox" name="curriculum.${curriculum.id}" value="1" checked="checked" />${curriculum.name}
                    </c:when>
                    <c:otherwise>
                      <input type="checkbox" name="curriculum.${curriculum.id}" value="1" />${curriculum.name}
                    </c:otherwise>
                  </c:choose>
                </div>
              </c:forEach>
            </div>
            
            <table>
              <tr>
                <td>
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.editCourse.beginsTitle"/>
                    <jsp:param name="helpLocale" value="courses.editCourse.beginsHelp"/>
                  </jsp:include>    
                </td>
                <td>
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.editCourse.endsTitle"/>
                    <jsp:param name="helpLocale" value="courses.editCourse.endsHelp"/>
                  </jsp:include>    
                </td>
              </tr>
              <tr>
                <td>
                  <input type="text" name="beginDate" class="ixDateField" value="${course.beginDate.time}"/>
                </td>
                <td>
                  <input type="text" name="endDate" class="ixDateField" value="${course.endDate.time}"/>
                </td>
              </tr>
              
              <tr>
                <td>
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.editCourse.signupStartTitle"/>
                  </jsp:include>    
                </td>
                <td>
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.editCourse.signupEndTitle"/>
                  </jsp:include>    
                </td>
              </tr>
              
              <tr>
                <td>
                  <input type="text" name="signupStart" class="ixDateField" value="${course.signupStart.time}"/>
                </td>
                <td>
                  <input type="text" name="signupEnd" class="ixDateField" value="${course.signupEnd.time}"/>
                </td>
              </tr>
            </table>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.localTeachingDaysTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.localTeachingDaysHelp"/>
              </jsp:include>    
              <input type="text" class="float" name="localTeachingDays" value="${fn:escapeXml(course.localTeachingDays)}" size="5">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.distanceTeachingDaysTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.distanceTeachingDaysHelp"/>
              </jsp:include>    
              <input type="text" class="float" name="distanceTeachingDays" value="${fn:escapeXml(course.distanceTeachingDays)}" size="5">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.planningHoursTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.planningHoursHelp"/>
              </jsp:include>    
            
              <input type="text" class="float" name="planningHours" value="${fn:escapeXml(course.planningHours)}" size="5">
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.teachingHoursTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.teachingHoursHelp"/>
              </jsp:include>    
              <input type="text" class="float" name="teachingHours" value="${fn:escapeXml(course.teachingHours)}" size="5">
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.distanceTeachingHoursTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.distanceTeachingHoursHelp"/>
              </jsp:include>    
              <input type="text" class="float" name="distanceTeachingHours" value="${fn:escapeXml(course.distanceTeachingHours)}" size="5">
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.assessingHoursTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.assessingHoursHelp"/>
              </jsp:include>    
              <input type="text" class="float" name="assessingHours" value="${fn:escapeXml(course.assessingHours)}" size="5">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.maxParticipantsTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.maxParticipantsHelp"/>
              </jsp:include>    
            
              <input type="text" name="maxParticipantCount" size="3" value="${course.maxParticipantCount}">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.enrolmentTimeEndTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.enrolmentTimeEndHelp"/>
              </jsp:include>    
            
              <input type="text" name="enrolmentTimeEnd" class="ixDateField" value="${course.enrolmentTimeEnd.time}">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.courseFeeTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.courseFeeHelp"/>
              </jsp:include>    
            
              <input type="number" min="1" step="any" name="courseFee" value="${course.courseFee}">
              <select name="courseFeeCurrency">
                <c:forEach var="currency" items="${currencies}" varStatus="vs">
                  <c:choose>
                    <c:when test="${(currency eq course.courseFeeCurrency) or (course.courseFeeCurrency eq null && vs.first)}">
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
                <jsp:param name="titleLocale" value="courses.editCourse.descriptionTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.descriptionHelp"/>
              </jsp:include>    

              <div class="tabLabelsContainer" id="descriptionTabs">
                <a class="tabLabel" href="#descGeneric"><fmt:message key="courses.editCourse.genericDescriptionTabTitle" /></a>

                <c:forEach var="cDesc" items="${courseDescriptions}">              
                  <a class="tabLabel" href="#courseDescription.${cDesc.category.id}">${cDesc.category.name}</a>
                </c:forEach>
              </div>
      
              <div id="descGeneric" class="tabContent">
                <textarea ix:cktoolbar="courseDescription" name="description" ix:ckeditor="true">${course.description}</textarea>
              </div>

              <c:forEach var="cDesc" items="${courseDescriptions}">              
                <div id="courseDescription.${cDesc.category.id}" class="tabContent">
                  <input type="hidden" name="courseDescription.${cDesc.category.id}.catId" id="courseDescription.${cDesc.category.id}.catId" value="${cDesc.category.id}"/>
                  <textarea ix:cktoolbar="courseDescription" name="courseDescription.${cDesc.category.id}.text" ix:ckeditor="true">${cDesc.description}</textarea>
                </div>
              </c:forEach>
            </div>

            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="openSearchUsersDialog();"><fmt:message key="courses.editCourse.addPersonLink"/></span>
            </div>
            <div id="personnelTable"> </div>

            <ix:extensionHook name="courses.editCourse.tabs.basic"/>
          </div>
      
          <div id="components" class="tabContentixTableFormattedData hiddenTab">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addNewComponent();"><fmt:message key="courses.editCourse.addComponentLink"/></span>
            </div>
            
            <div id="noComponentsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="courses.editCourse.noComponentsAddedPreFix"/> <span onclick="addNewComponent();" class="genericTableAddRowLink"><fmt:message key="courses.editCourse.noComponentsAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="editCourseComponentsList">
              
            </div>
            
            <div id="componentHoursTotalContainer" style="display:none;">
              <span><fmt:message key="courses.editCourse.totalComponentHoursLabel"/></span>
              <span id="componentHoursTotalValueContainer">0</span>
            </div>
            
            <ix:extensionHook name="courses.editCourse.tabs.components"/>
          </div>
          
          <div id="costplan" class="tabContent hiddenTab">
            <div id="courseIncomeContainer">
              <div id="courseIncomesTitle" class="genericFormTitle">
                <div class="genericFormTitleText"><fmt:message key="courses.editCourse.courseIncomesTitle"/></div>
              </div>
            
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.editCourse.incomePerCourseIncomesTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addCourseIncomeRow();"></div>
              
                <div id="courseIncomesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.editCourse.courseIncomesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="courseIncomesTableTotal"> 0 </div> 
                </div>
              </div>
          
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.editCourse.incomePerStudentIncomesTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addStudentIncomeRow();"></div>
              
                <div id="studentIncomesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.editCourse.studentIncomesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="studentIncomesTableTotal"> 0 </div> 
                </div>
              </div>
              
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.editCourse.incomePerGradeIncomesTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addGradeIncomeRow();"></div>
              
                <div id="gradeIncomesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.editCourse.gradeIncomesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="gradeIncomesTableTotal"> 0 </div> 
                </div>
              </div>
        
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.editCourse.otherIncomesTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addOtherIncomeRow();"></div>
              
                <div id="otherIncomesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.editCourse.otherIncomesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="otherIncomesTableTotal"> 0 </div> 
                </div>
              </div>
              
              <div id="courseIncomeTotalContainer">
                <div id="courseIncomeTotalTitle">
                  <fmt:message key="courses.editCourse.courseIncomeTotalTitle"/>
                </div>
                <div id="courseIncomeTotalValue"> 0 </div>
              </div>
              
            </div>
            
            <div id="courseCostContainer">
            
              <div id="courseCostsTitle" class="genericFormTitle">
                <div class="genericFormTitleText"><fmt:message key="courses.editCourse.courseCostsTitle"/></div>
              </div>
           
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.editCourse.resourcesPerCourseCostTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="openSearchResourcesDialog('basicResourcesTable');"></div>
                <div id="basicResourcesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle"> 
                    <fmt:message key="courses.editCourse.basicResourcesTableTotalTitle"/>
                   </div> 
                  <div class="courseCostsPlanTableTotalValue" id="basicResourcesTableTotal"> 0 </div> 
                </div>
              </div>
           
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.editCourse.resourcesPerStudentCostTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="openSearchResourcesDialog('studentResourcesTable');"></div>
              
                <div id="studentResourcesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle"> 
                    <fmt:message key="courses.editCourse.studentResourcesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="studentResourcesTableTotal"> 0 </div> 
                </div>
              </div>
          
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.editCourse.resourcesPerGradeCostTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="openSearchResourcesDialog('gradeResourcesTable');"></div>
                <div id="gradeResourcesTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.editCourse.gradeResourcesTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="gradeResourcesTableTotal"> 0 </div> 
                </div>
              </div>
          
              <div class="courseCostsPlanTableContainer">
                <div class="genericFormTitle courseCostsPlanTableTitle">
                  <div class="genericFormTitleText"><fmt:message key="courses.editCourse.otherCostsTitle" /></div>
                </div>
                <div class="courseCostsPlanTableAddRowContainer" onclick="addOtherCostRow();"></div>
              
                <div id="otherCostsTable"> </div>
                <div class="courseCostsPlanTableTotal"> 
                  <div class="courseCostsPlanTableTotalTitle">
                    <fmt:message key="courses.editCourse.otherCostsTableTotalTitle"/>
                  </div> 
                  <div class="courseCostsPlanTableTotalValue" id="otherCostsTableTotal"> 0 </div> 
                </div>
              </div>
          
              <div id="courseCostsTotalContainer">
                <div id="courseCostsTotalTitle">
                  <fmt:message key="courses.editCourse.courseCostsTotalTitle"/>
                </div>
                <div id="courseCostsTotalValue"> 0 </div>
              </div>
              
            </div>
            <div style="clear:both; height:1px;"></div>
            <ix:extensionHook name="courses.editCourse.tabs.costPlan"/>
          </div>
      
          <div id="students" class="tabContentixTableFormattedData hiddenTab">
            <div class="courseStudentsTableContainer">
            
              <c:choose>
                <c:when test="${course.courseTemplate}">
                  <div><fmt:message key="courses.editCourse.courseTemplateCannotHaveStudentsMessage"/></div>
                </c:when>
                <c:otherwise>
                  <div class="genericTableAddRowContainer">
                    <span class="genericTableAddRowLinkContainer" onclick="openSearchStudentsDialog();"><fmt:message key="courses.editCourse.addStudentLink"/></span>
                  </div>
                    
                  <div id="noStudentsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
                    <span><fmt:message key="courses.editCourse.noStudentsAddedPreFix"/> <span onclick="openSearchStudentsDialog();" class="genericTableAddRowLink"><fmt:message key="courses.editCourse.noStudentsAddedClickHereLink"/></span>.</span>
                  </div>
                </c:otherwise>
              </c:choose>
            
              <div id="courseStudentsTable"> </div>

              <div id="editCourseStudentsTotalContainer">
                <fmt:message key="courses.editCourse.studentsTotal"/> <span id="editCourseStudentsTotalValue"></span>
              </div>

            </div>
            <ix:extensionHook name="courses.editCourse.tabs.students"/>
          </div>
          
          <div id="signup" class="tabContent hiddenTab">
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.signupStudyProgrammesTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.signupStudyProgrammesHelp"/>
              </jsp:include>    
              <input type="text" id="searchStudyProgrammesInput" size="40" />
              <span id="searchSignupStudyProgrammes_indicator" class="autocomplete_progress_indicator" style="display: none"><img src="${pageContext.request.contextPath}/gfx/progress_small.gif"/></span>
              <div id="searchStudyProgrammesInputAutoComplete" class="autocomplete_choices"></div>
            </div>

            <div class="genericFormSection">
              <div id="signupStudyProgrammesTable"></div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.editCourse.signupStudentGroupsTitle"/>
                <jsp:param name="helpLocale" value="courses.editCourse.signupStudentGroupsHelp"/>
              </jsp:include>    
              <input type="text" id="searchStudentGroupsInput" size="40" />
              <span id="searchSignupStudentGroups_indicator" class="autocomplete_progress_indicator" style="display: none"><img src="${pageContext.request.contextPath}/gfx/progress_small.gif"/></span>
              <div id="searchStudentGroupsInputAutoComplete" class="autocomplete_choices"></div>
            </div>

            <div class="genericFormSection">
              <div id="signupStudentGroupsTable"></div>
            </div>
          </div>
      
          <ix:extensionHook name="courses.editCourse.tabs"/>

          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="courses.editCourse.saveButton"/>">
          </div>
  
        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>
