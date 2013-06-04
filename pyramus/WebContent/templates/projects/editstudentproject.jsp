<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="projects.editStudentProject.pageTitle">
        <fmt:param value="${studentProject.student.fullName}"/>
        <fmt:param value="${studentProject.name}"/>
      </fmt:message>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>

    <script type="text/javascript">
      function redoFilters() {
        var table1 = getIxTableById('modulesTable');
        var table2 = getIxTableById('coursesTable');
        
        table1.applyFilters();
        table2.applyFilters();
      }
    
      function checkModulesMessage() {
        var table = getIxTableById('modulesTable');
        var allMessageVisible = false;
        var noMessageVisible = false;
      
        if (table.getRowCount() > 0) {
          if (table.getVisibleRowCount() == 0) {
            allMessageVisible = true;
          }
        } else {
          noMessageVisible = true;
        }
        
        $('noModulesAddedMessageContainer').setStyle({
          display: noMessageVisible ? '' : 'none'
        });
      
        $('allModulesAddedMessageContainer').setStyle({
          display: allMessageVisible ? '' : 'none'
        });
        
        if (table.getVisibleRowCount() > 0) {
          $('editStudentProjectModulesTotalContainer').setStyle({
            display: ''
          });
          $('editStudentProjectModulesTotalValue').innerHTML = table.getVisibleRowCount(); 
        }
        else {
          $('editStudentProjectModulesTotalContainer').setStyle({
            display: 'none'
          });
        }
      }

      function checkCoursesMessage() {
        var coursesTable = getIxTableById('coursesTable');

        $('noCoursesAddedMessageContainer').setStyle({
          display: (coursesTable.getVisibleRowCount() > 0) ? 'none' : ''
        });
        
        if (coursesTable.getVisibleRowCount() > 0) {
          $('editStudentProjectCoursesTotalContainer').setStyle({
            display: ''
          });
          $('editStudentProjectCoursesTotalValue').innerHTML = coursesTable.getVisibleRowCount(); 
          $('coursesContainer').setStyle({
            display: ''
          });
        }
        else {
          $('editStudentProjectCoursesTotalContainer').setStyle({
            display: 'none'
          });
          $('coursesContainer').setStyle({
            display: 'none'
          });
        }
      }
      
      function filterModules(showPassingGrade) {
        var modulesTable = getIxTableById('modulesTable');
        modulesTable.addFilter({
          execute: function (event) {
            var moduleIdColumnIndex = modulesTable.getNamedColumnIndex('studentProjectModuleId');
            var passingGradeColumnIndex = modulesTable.getNamedColumnIndex('hasPassingGrade');

            var hideArray = new Array();
            
            for (var i = 0; i < modulesTable.getRowCount(); i++) {
              var studentProjectModuleId = modulesTable.getCellValue(i, moduleIdColumnIndex);
              var passingGrade = modulesTable.getCellValue(i, passingGradeColumnIndex);
              
              if ((showPassingGrade ? (passingGrade != "1") : (passingGrade == "1")) && (studentProjectModuleId != -1)) { 
                hideArray.push(i);
              }
            }
      
            if (hideArray.size() > 0)
              modulesTable.hideRows(hideArray.toArray());
          },
          getColumn: function() {
            return -1;
          }
        });
   
//         checkModulesMessage();
//         checkCoursesMessage();
      }
      
      function openSearchModulesDialog() {

        var selectedModules = new Array();
        var modulesTable = getIxTableById('modulesTable');
        for (var i = 0; i < modulesTable.getRowCount(); i++) {
          var moduleName = modulesTable.getCellValue(i, modulesTable.getNamedColumnIndex('name'));
          var moduleId = modulesTable.getCellValue(i, modulesTable.getNamedColumnIndex('moduleId'));
          selectedModules.push({
            name: moduleName,
            id: moduleId
          });
        }

        var dialog = new IxDialog({
          id : 'searchModulesDialog',
          contentURL : GLOBAL_contextPath + '/projects/searchmodulesdialog.page',
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="projects.searchModulesDialog.searchModulesDialog.dialogTitle"/>',
          okLabel : '<fmt:message key="projects.searchModulesDialog.okLabel"/>', 
          cancelLabel : '<fmt:message key="projects.searchModulesDialog.cancelLabel"/>' 
        });
      
        dialog.setSize("800px", "660px");
        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'okClick':
              var modulesTable = getIxTableById('modulesTable');
              modulesTable.detachFromDom();              
              for (var i = 0, len = event.results.modules.length; i < len; i++) {
                var moduleId = event.results.modules[i].id;
                var moduleName = event.results.modules[i].name;
                var index = getModuleTableModuleRowIndex(modulesTable, moduleId);
                var unsavedText = '<fmt:message key="projects.editStudentProject.unsavedModule"/>';
                if (index == -1) {
                  var rowNumber = modulesTable.addRow([
                      moduleName, 
                      unsavedText, 
                      unsavedText, 
                      -1, 
                      0, 
                      '', 
                      '', 
                      '', 
                      moduleId, 
                      -1,
                      0]);
                }
              }
              modulesTable.reattachToDom();              
              
              checkModulesMessage();
              checkCoursesMessage();
            break;
          }
        });
        dialog.open();
      }
      
      function openSearchCoursesDialog() {
        var dialog = new IxDialog({
          id : 'searchCoursesDialog',
          contentURL : GLOBAL_contextPath + '/projects/searchstudentprojectcoursesdialog.page',
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="projects.searchStudentProjectCoursesDialog.dialogTitle"/>',
          okLabel : '<fmt:message key="projects.searchStudentProjectCoursesDialog.okLabel"/>', 
          cancelLabel : '<fmt:message key="projects.searchStudentProjectCoursesDialog.cancelLabel"/>' 
        });
        
        dialog.setSize("800px", "600px");
        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'okClick':
              var coursesTable = getIxTableById('coursesTable');
              for (var i = 0, len = event.results.courses.length; i < len; i++) {
                var courseId = event.results.courses[i].id;
                var moduleId = event.results.courses[i].moduleId;
                var courseName = event.results.courses[i].name;
                var participationType = '<fmt:message key="projects.editStudentProject.unsavedStudentParticipationType"/>' ;
                var beginDate = event.results.courses[i].beginDate;
                var endDate = event.results.courses[i].endDate;
                
                var index = getCourseTableCourseRowIndex(coursesTable, courseId);
                if (index == -1) {
                  var c2row = coursesTable.addRow([
                      courseName,
                      participationType,
                      '',
                      beginDate||'',
                      endDate||'',
                      'OPTIONAL',
                      '',
                      '',
                      moduleId,
                      courseId,
                      -1]);
                }
              }
              
              redoFilters();
              checkModulesMessage();
              checkCoursesMessage();
            break;
          }
        });
        dialog.open();
      }
  
      function getCourseTableCourseRowIndex(table, courseId) {
        var colIndex = table.getNamedColumnIndex('courseId');
        for (var i = 0; i < table.getRowCount(); i++) {
          var tableCourseId = table.getCellValue(i, colIndex);
          if (tableCourseId == courseId) {
            return i;
          }
        }
        return -1;
      }
  
      function getModuleTableModuleRowIndex(table, moduleId) {
        var colIndex = table.getNamedColumnIndex('moduleId');
        for (var i = 0; i < table.getRowCount(); i++) {
          var tableModuleId = table.getCellValue(i, colIndex);
          if (tableModuleId == moduleId) {
            return i;
          }
        }
        return -1;
      }

      function getCourseTableModuleRowIndex(table, moduleId) {
        var colIndex = table.getNamedColumnIndex('moduleId');
        for (var i = 0; i < table.getRowCount(); i++) {
          var tableModuleId = table.getCellValue(i, colIndex);
          if (tableModuleId == moduleId) {
            return i;
          }
        }
        return -1;
      }

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
        setupTags();
        var modulesTable = new IxTable($('modulesTableContainer'), {
          id : "modulesTable",
          columns : [{
            header : '<fmt:message key="projects.editStudentProject.moduleTableNameHeader"/>',
            left : 8,
            right : 8 + 22 + 8 + 22 + 8 + 22 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 150 + 8,
            dataType: 'text',
            editable: false,
            paramName: 'name',
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
            header : '<fmt:message key="projects.editStudentProject.moduleTableCourseStatesHeader"/>',
            right : 8 + 22 + 8 + 22 + 8 + 22 + 8 + 100 + 8 + 100 + 8 + 100 + 8,
            width: 165,
            dataType: 'text',
            editable: false,
//             paramName: 'courseStates',
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
            header : '<fmt:message key="projects.editStudentProject.moduleTableCreditsHeader"/>',
            right : 8 + 22 + 8 + 22 + 8 + 22 + 8 + 100 + 8 + 100 + 8,
            width: 100,
            dataType: 'text',
            editable: false,
//             paramName: 'credits',
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
            header : '<fmt:message key="projects.editStudentProject.moduleTableStudyTermHeader"/>',
            right : 8 + 22 + 8 + 22 + 8 + 22 + 8 + 100 + 8,
            width: 100,
            dataType : 'select',
            paramName: 'academicTerm',
            editable: true,
            options: [
              {text: ""}
              <c:if test="${not empty academicTerms}">,</c:if>
              <c:forEach var="academicTerm" items="${academicTerms}" varStatus="vs">
                {text: "${academicTerm.name}", value: ${academicTerm.id}}
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
            header : '<fmt:message key="projects.editStudentProject.moduleTableOptionalityHeader"/>',
            right : 8 + 22 + 8 + 22 + 8 + 22 + 8,
            width: 100,
            dataType : 'select',
            paramName: 'optionality',
            editable: true,
            options: [
              {text: '<fmt:message key="projects.editStudentProject.optionalityMandatory"/>', value: 'MANDATORY'},
              {text: '<fmt:message key="projects.editStudentProject.optionalityOptional"/>', value: 'OPTIONAL'}
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
            width: 22,
            right: 8 + 22 + 8 + 22 + 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/icons/16x16/actions/link-to-editor.png',
            tooltip: '<fmt:message key="projects.editStudentProject.moduleTableEditModuleRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              redirectTo(GLOBAL_contextPath + '/modules/editmodule.page?module=' + moduleId);
            } 
          }, {
            width: 22,
            right: 8 + 22 + 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/icons/16x16/actions/search.png',
            tooltip: '<fmt:message key="projects.editStudentProject.moduleTableFindCourseRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var row = event.row;
              
              var academicTermId = table.getCellValue(row, table.getNamedColumnIndex('academicTerm'));
              var moduleId = table.getCellValue(row, table.getNamedColumnIndex('moduleId'));
              var studentId = ${studentProject.student.id}; 
              
              var dialog = new IxDialog({
                id : 'searchStudentProjectModuleCoursesDialog',
                contentURL : GLOBAL_contextPath + '/projects/searchstudentprojectmodulecoursesdialog.page?moduleId=' + moduleId + '&academicTermId=' + academicTermId + "&studentId=" + studentId,
                centered : true,
                showOk : false,
                showCancel : true,
                title : '<fmt:message key="projects.searchStudentProjectModuleCoursesDialog.dialogTitle"/>',
                cancelLabel : '<fmt:message key="projects.searchStudentProjectModuleCoursesDialog.cancelLabel"/>' 
              });
              
              dialog.setSize("800px", "600px");
              dialog.addDialogListener(function(event) {
                var dlg = event.dialog;
                switch (event.name) {
                  case 'okClick':
                    var moduleId = table.getCellValue(row, table.getNamedColumnIndex('moduleId'));
                    var courseId = event.results.courseId;
                    var name = event.results.name;
                    var beginDate = event.results.beginDate;
                    var endDate = event.results.endDate;
                    var optionality = table.getCellValue(row, table.getNamedColumnIndex('optionality'));
                    var participationType = '<fmt:message key="projects.editStudentProject.unsavedStudentParticipationType"/>' ;
                    
                    var coursesTable = getIxTableById('coursesTable'); 
                    coursesTable.addRow([name, participationType, '', beginDate, endDate, optionality, '', '', moduleId, courseId, -1]);
                  
                    redoFilters();
                    checkModulesMessage();
                    checkCoursesMessage();
                  break;
                }
              });
              dialog.open();
            } 
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="projects.editStudentProject.moduleTableDeleteRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
              checkModulesMessage();
            } 
          }, {
            dataType: 'hidden',
            paramName: 'moduleId'
          }, {
            dataType: 'hidden',
            paramName: 'studentProjectModuleId'
          }, {
            dataType: 'hidden',
            paramName: 'hasPassingGrade'
          }]
        });
        
        var coursesTable = new IxTable($('coursesTableContainer'), {
          id: 'coursesTable',
          columns : [ {
            header : '<fmt:message key="projects.editStudentProject.coursesTableNameHeader"/>',
            left: 8,
            right: 8 + 22 + 8 + 22 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 150 + 8,
//             right: 8 + 22 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 150 + 8,
            dataType : 'text',
            editable: false,
            paramName: 'name',
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
            header : '<fmt:message key="projects.editStudentProject.coursesTableStudentsParticipationTypeHeader"/>',
            right: 8 + 22 + 8 + 22 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 100 + 8,
//             right: 8 + 22 + 8 + 100 + 8 + 100 + 8 + 100 + 8,
            width : 150,
            dataType : 'text',
            editable: false,
          }, {
            header : '<fmt:message key="projects.editStudentProject.coursesTableGradeHeader"/>',
            right: 8 + 22 + 8 + 22 + 8 + 100 + 8 + 100 + 8 + 100 + 8,
            width : 100,
            dataType : 'text',
            editable: false,
          }, {
            header : '<fmt:message key="projects.editStudentProject.coursesTableBeginDateHeader"/>',
            right: 8 + 22 + 8 + 22 + 8 + 100 + 8 + 100 + 8,
//             right: 8 + 22 + 8 + 100 + 8 + 100 + 8,
            width : 100,
            dataType : 'date',
            editable: false
          }, {
            header : '<fmt:message key="projects.editStudentProject.coursesTableEndDateHeader"/>',
            width: 100,
            right : 8 + 22 + 8 + 22 + 8 + 100 + 8,
//             right : 8 + 22 + 8 + 100 + 8,
            dataType : 'date',
            editable: false
          }, {
            header : '<fmt:message key="projects.editStudentProject.coursesTableOptionalityHeader"/>',
            right : 8 + 22 + 8 + 22 + 8,
//             right : 8 + 22 + 8,
            width: 100,
            dataType : 'select',
            paramName: 'optionality',
            editable: true,
            options: [
              {text: '', value: ''},
              {text: '<fmt:message key="projects.editStudentProject.optionalityMandatory"/>', value: 'MANDATORY'},
              {text: '<fmt:message key="projects.editStudentProject.optionalityOptional"/>', value: 'OPTIONAL'}
            ]
          }, {
            width: 22,
            right: 8 + 22 + 8,
//             right: 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/icons/16x16/actions/link-to-editor.png',
            tooltip: '<fmt:message key="projects.editStudentProject.coursesTableEditCourseRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
              redirectTo(GLOBAL_contextPath + '/courses/editcourse.page?course=' + courseId);
            } 
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            paramName: 'removeButton',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="projects.editStudentProject.coursesTableDeleteRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
              checkCoursesMessage();
            } 
          }, {
            dataType: 'hidden',
            paramName: 'moduleId'
          }, {
            dataType: 'hidden',
            paramName: 'courseId'
          }, {
            dataType: 'hidden',
            paramName: 'id'
          }]
        });
        
        var rowId;
        modulesTable.detachFromDom();
        
        var studentProjectModules = JSDATA['studentProjectModules'].evalJSON();

        for (var i = 0, l = studentProjectModules.length; i < l; i++) {
          
          var courseStates = "";
          var credits = "";
          
          for (var j = 0, l2 = studentProjectModules[i].moduleCourseStudents.length; j < l2; j++) {
            var pType = studentProjectModules[i].moduleCourseStudents[j].courseStudentParticipationType;
            
            if (courseStates != "")
              courseStates += ", ";
            courseStates += pType;
          }

          for (var j = 0, l2 = studentProjectModules[i].moduleCredits.length; j < l2; j++) {
            var gradeName = studentProjectModules[i].moduleCredits[j].gradeName;
            
            if (credits != "")
              credits += ", ";
            credits += gradeName;
          }
          
          rowId = modulesTable.addRow([
            studentProjectModules[i].moduleName,
            courseStates,
            credits,
            studentProjectModules[i].projectModuleAcademicTermId,
            studentProjectModules[i].projectModuleOptionality,
            '',
            '',
            '',
            studentProjectModules[i].moduleId,
            studentProjectModules[i].projectModuleId,
            studentProjectModules[i].projectModuleHasPassingGrade]);
        }
        
        modulesTable.reattachToDom();
        
        coursesTable.detachFromDom();

        var courses = JSDATA['courseStudents'].evalJSON();
        
        for (var i = 0, l = courses.length; i < l; i++) {
          rowId = coursesTable.addRow([
              courses[i].courseName,
              courses[i].participationType,
              courses[i].gradeName,
              courses[i].courseBeginDate,
              courses[i].courseEndDate,
              courses[i].optionality,
              '',
              '',
              courses[i].moduleId,
              courses[i].courseId,
              courses[i].courseStudentId]);

          coursesTable.disableCellEditor(rowId, coursesTable.getNamedColumnIndex("removeButton"));
        }
        
        coursesTable.reattachToDom();

        checkModulesMessage();
        checkCoursesMessage();

        var assessmentsTable = new IxTable($('projectAssessmentsTableContainer'), {
          id: 'assessmentsTable',
            columns : [{
              left: 8,
              width: 22,
              paramName: 'editRowButton',
              dataType: 'button',
              imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
              tooltip: '<fmt:message key="projects.editStudentProject.assessmentsTableEditTooltip"/>',
              onclick: function (event) {
                var table = event.tableComponent;

                table.setCellEditable(event.row, table.getNamedColumnIndex('date'), true);
                table.setCellEditable(event.row, table.getNamedColumnIndex('grade'), true);
                table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
                table.showCell(event.row, table.getNamedColumnIndex('editVerbalAssessmentButton'));
              }
            }, {
              header : '<fmt:message key="projects.editStudentProject.assessmentsTableDateHeader"/>',
              left: 8 + 22 + 8,
              width : 150,
              dataType : 'date',
              paramName: 'date',
              editable: false
            }, {
              header : '<fmt:message key="projects.editStudentProject.assessmentsTableGradeHeader"/>',
              left: 8 + 22 + 8 + 150 + 8,
              width: 150,
              dataType : 'select',
              paramName: 'grade',
              editable: false,
              options: [
                {text: "-"}
                <c:if test="${fn:length(gradingScales) gt 0}">,</c:if>
                <c:forEach var="gradingScale" items="${gradingScales}" varStatus="vs">
                  {text: "${fn:escapeXml(gradingScale.name)}", optionGroup: true, 
                    options: [
                      <c:forEach var="grade" items="${gradingScale.grades}" varStatus="vs2">
                        {text: "${fn:escapeXml(grade.name)}", value: ${grade.id}}
                        <c:if test="${not vs2.last}">,</c:if>
                      </c:forEach>
                    ]
                  } 
                  <c:if test="${not vs.last}">,</c:if>
                </c:forEach>
              ]
            }, {
              header : '<fmt:message key="projects.editStudentProject.studentsTableVerbalAssessmentHeader"/>',
              left: 8 + 22 + 8 + 150 + 8 + 150 + 8,
              width : 150,
              dataType : 'text',
              paramName: 'verbalAssessmentShort',
              editable: false
            }, {
              left: 8 + 22 + 8 + 150 + 8 + 150 + 8 + 150 + 8,
              width: 22,
              hidden: true,
              dataType: 'button',
              paramName: 'editVerbalAssessmentButton',
              imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
              tooltip: '<fmt:message key="projects.editStudentProject.studentsTableEditVerbalAssessmentTooltip"/>',
              onclick: function (event) {
                openEditVerbalAssessmentDialog(event.row);
              }
            }, {
              left: 8 + 22 + 8 + 150 + 8 + 150 + 8 + 150 + 8 + 22 + 8,
              dataType: 'button',
              paramName: 'removeButton',
              imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
              tooltip: '<fmt:message key="projects.editStudentProject.assessmentsTableDeleteRowTooltip"/>',
              onclick: function (event) {
                var table = event.tableComponent;
                var rowIndex = event.row;
                var assessmentId = table.getCellValue(event.row, table.getNamedColumnIndex('assessmentId'));
                if (assessmentId == -1)
                  table.deleteRow(rowIndex);
                else {
                  var url = GLOBAL_contextPath + "/simpledialog.page?localeId=projects.editStudentProject.deleteAssessmentConfirmDialogContent";
                  
                  var dialog = new IxDialog({
                    id : 'confirmRemoval',
                    contentURL : url,
                    centered : true,
                    showOk : true,  
                    showCancel : true,
                    autoEvaluateSize: true,
                    title : '<fmt:message key="projects.editStudentProject.deleteAssessmentConfirmDialogTitle"/>',
                    okLabel : '<fmt:message key="projects.editStudentProject.deleteAssessmentConfirmDialogOkLabel"/>',
                    cancelLabel : '<fmt:message key="projects.editStudentProject.deleteAssessmentConfirmDialogCancelLabel"/>'
                  });
                
                  dialog.addDialogListener( function(event) {
                    var dlg = event.dialog;
                
                    switch (event.name) {
                      case 'okClick':
                        table.setCellValue(rowIndex, table.getNamedColumnIndex('modified'), 1);
                        table.setCellValue(rowIndex, table.getNamedColumnIndex('deleted'), 1);
                        table.hideRow(rowIndex);
                      break;
                    }
                  });
                
                  dialog.open(); 
                }
              } 
            }, {
              dataType: 'hidden',
              paramName: 'assessmentId'
            }, {
              dataType: 'hidden',
              paramName: 'modified'
            }, {
              dataType: 'hidden',
              paramName: 'deleted'
            }, {
              dataType: 'hidden', 
              paramName: 'verbalAssessment'
            }, {
              dataType: 'hidden', 
              paramName: 'verbalModified'
            }]
        });
        
        <c:forEach var="assessment" items="${projectAssessments}">
        <c:choose>
          <c:when test="${fn:length(verbalAssessments[assessment.id]) gt 20}">
            <c:set var="verbalAssessment">${fn:substring(verbalAssessments[assessment.id], 0, 20)}...</c:set>
          </c:when>
          <c:otherwise>
            <c:set var="verbalAssessment">${verbalAssessments[assessment.id]}</c:set>
          </c:otherwise>
        </c:choose>
        
        assessmentsTable.addRow([
            '', 
            '${assessment.date.time}',
            '${assessment.grade.id}',
            '${verbalAssessment}',
            '',
            '',
            ${assessment.id},
            0,
            0,
            '',
            0
        ]);
        </c:forEach>
        
        var basicTabRelatedActionsHoverMenu = new IxHoverMenu($('basicTabRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="projects.editStudentProject.basicTabRelatedActionsLabel"/>'
        });

        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/eye.png',
          text: '<fmt:message key="projects.editStudentProject.basicTabRelatedActionsViewStudentLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/students/viewstudent.page?abstractStudent=${studentProject.student.abstractStudent.id}');
          }
        }));

        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/icons/16x16/actions/link-to-editor.png',
          text: '<fmt:message key="projects.editStudentProject.basicTabRelatedActionsEditStudentLabel"/>',
          link: GLOBAL_contextPath + '/students/editstudent.page?abstractStudent=${studentProject.student.abstractStudent.id}'  
        }));
        
        var extensionHoverMenuLinks = $$('#extensionHoverMenuLinks a');
        for (var i=0, l=extensionHoverMenuLinks.length; i<l; i++) {
          var extensionHoverMenuLink = extensionHoverMenuLinks[i].remove();
          if (extensionHoverMenuLink.href.indexOf('?') == -1) {
              basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
                  iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
                  text: extensionHoverMenuLink.innerHTML,
                  link: GLOBAL_contextPath + extensionHoverMenuLink.href + "?creditId=${studentProject.id}"
              }));
          } else {
              basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
                  iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
                  text: extensionHoverMenuLink.innerHTML,
                  link: GLOBAL_contextPath + extensionHoverMenuLink.href + "&creditId=${studentProject.id}"
              }));
          }
        }

        
        
        var moduleFilter = $('moduleFilter');
        
        Event.observe(moduleFilter, "change", onModuleFilterChange);
      }

      function onModuleFilterChange(event) {
        var modulesTable = getIxTableById('modulesTable');
        var moduleFilter = $('moduleFilter');
        
        modulesTable.clearFilters();
        
        switch(moduleFilter.value) {
          case "1":
            filterModules(true);
          break;
          
          case "2":
            filterModules(false);
          break;
        }
      }
      
      function addAssessmentRow() {
        var table = getIxTableById('assessmentsTable');
        var rowIndex = table.addRow(['', new Date().getTime(), '', '', '', '', -1, 1, 0, '', 0]);
        table.setCellEditable(rowIndex, table.getNamedColumnIndex('date'), true);
        table.setCellEditable(rowIndex, table.getNamedColumnIndex('grade'), true);
        table.showCell(rowIndex, table.getNamedColumnIndex('editVerbalAssessmentButton'));
      }
      
      function openEditVerbalAssessmentDialog(row) {
        var table = getIxTableById("assessmentsTable"); 
        var assessmentId = table.getCellValue(row, table.getNamedColumnIndex('assessmentId'));
        
        var dialog = new IxDialog({
          id : 'editVerbalAssessmentDialog',
          contentURL : GLOBAL_contextPath + '/courses/editverbalassessmentdialog.page?creditId=' + assessmentId,
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="courses.editVerbalAssessmentDialog.dialogTitle"/>',
          okLabel : '<fmt:message key="courses.editVerbalAssessmentDialog.okLabel"/>', 
          cancelLabel : '<fmt:message key="courses.editVerbalAssessmentDialog.cancelLabel"/>' 
        });
        
        dialog.setSize("600px", "350px");
        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'okClick':
              var verbalAssessment = event.results.verbalAssessment.escapeHTML();
              var verbalShort = event.results.verbalAssessment.stripTags().trim().truncate(20);
              
              var table = getIxTableById("assessmentsTable"); 
              var verbalModCol = table.getNamedColumnIndex('verbalModified');
              var verbalAssessmentCol = table.getNamedColumnIndex('verbalAssessment');
              var verbalShortCol = table.getNamedColumnIndex('verbalAssessmentShort');
              table.setCellValue(row, verbalModCol, 1);
              table.setCellValue(row, verbalAssessmentCol, verbalAssessment);
              table.setCellValue(row, verbalShortCol, verbalShort);
            break;
          }
        });
        dialog._rowIndex = row;
        var verbalModified = table.getCellValue(row, table.getNamedColumnIndex('verbalModified'));
        if (verbalModified == 1)
          dialog._modifiedVerbalAssessment = table.getCellValue(row, table.getNamedColumnIndex('verbalAssessment'));
        dialog.open();
      }
      
    </script>
    <ix:extensionHook name="projects.editStudentProject.head" />
  </head>
  <body onLoad="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">
      <fmt:message key="projects.editStudentProject.pageTitle">
        <fmt:param value="${studentProject.student.fullName}"/>
        <fmt:param value="${studentProject.name}"/>
      </fmt:message>
    </h1>
    
    <div id="extensionHoverMenuLinks" style="display: none;">
      <ix:extensionHook name="projects.editStudentProject.hoverMenuLinks" />
    </div>
    
    
    <form id="studentProjectForm" action="editstudentproject.json" method="post" ix:jsonform="true" ix:useglasspane="true">
      <input type="hidden" name="version" value="${studentProject.version}"/>
      
      <div id="editStudentProjectEditFormContainer"> 
        <div class="genericFormContainer"> 
          
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="projects.editStudentProject.tabLabelBasic"/>
            </a>
            <a class="tabLabel" href="#coursesmodules">
              <fmt:message key="projects.editStudentProject.tabLabelCoursesAndModules"/>
            </a>
          </div>
          
          <!--  Basic tab -->
        
          <div id="basic" class="tabContent">
            <div id="basicTabRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
            <input type="hidden" name="studentProject" value="${studentProject.id}"/>
            
            <!--  TODO italic tags to css -->
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.createdTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.createdHelp"/>
              </jsp:include>
              <span><i>${studentProject.creator.fullName} <fmt:formatDate type="both" value="${studentProject.created}"/></i></span>    
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.modifiedTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.modifiedHelp"/>
              </jsp:include>
              <span><i>${studentProject.lastModifier.fullName} <fmt:formatDate type="both" value="${studentProject.lastModified}"/></i></span>    
            </div>
  
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.studentTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.studentHelp"/>
              </jsp:include>
              <div>${studentProject.student.lastName}, ${studentProject.student.firstName}</div>
            </div>
  
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.studyProgrammeTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.studyProgrammeHelp"/>
              </jsp:include>     
              
              <div>${studentProject.student.studyProgramme.name}<c:if test="${student.hasFinishedStudies}">*</c:if></div>
              
              <input type="hidden" name="student" value="${studentProject.student.id}"/>
              
<!--               <select name="student"> -->
<%--                 <c:forEach var="student" items="${students}"> --%>
<%--                   <c:choose> --%>
<%--                     <c:when test="${student.studyProgramme.id == studentProject.student.studyProgramme.id}"> --%>
<%--                       <option value="${student.id}" selected="selected">${student.studyProgramme.name}<c:if test="${student.hasFinishedStudies}">*</c:if></option>  --%>
<%--                     </c:when> --%>
<%--                     <c:otherwise> --%>
<%--                       <option value="${student.id}">${student.studyProgramme.name}<c:if test="${student.hasFinishedStudies}">*</c:if></option>  --%>
<%--                     </c:otherwise> --%>
<%--                   </c:choose> --%>
<%--                 </c:forEach> --%>
<%--                 <c:if test="${studentProject.student.studyProgramme.archived == true}"> --%>
<%--                   <option value="${studentProject.student.id}" selected="selected">${studentProject.student.studyProgramme.name}***</option> --%>
<%--                 </c:if> --%>
<!--               </select> -->
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.nameTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.nameHelp"/>
              </jsp:include>
              <input type="text" class="required" name="name" value="${fn:escapeXml(studentProject.name)}" size="40"/>
            </div>
                 
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.tagsTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40" value="${fn:escapeXml(tags)}"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
    
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.optionalityTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.optionalityHelp"/>
              </jsp:include>
              <select name="projectOptionality">
                <option></option>
                <c:choose>
                  <c:when test="${studentProject.optionality eq 'MANDATORY'}">
                    <option value="MANDATORY" selected="selected"><fmt:message key="projects.editStudentProject.optionalityMandatory"/></option>
                    <option value="OPTIONAL"><fmt:message key="projects.editStudentProject.optionalityOptional"/></option>
                  </c:when>
                  <c:when test="${studentProject.optionality eq 'OPTIONAL'}">
                    <option value="MANDATORY"><fmt:message key="projects.editStudentProject.optionalityMandatory"/></option>
                    <option value="OPTIONAL" selected="selected"><fmt:message key="projects.editStudentProject.optionalityOptional"/></option>
                  </c:when>
                  <c:otherwise>
                    <option value="MANDATORY"><fmt:message key="projects.editStudentProject.optionalityMandatory"/></option>
                    <option value="OPTIONAL"><fmt:message key="projects.editStudentProject.optionalityOptional"/></option>
                  </c:otherwise>
                </c:choose>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.projectAssessmentsTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.projectAssessmentsHelp"/>
              </jsp:include>
              <div id="addProjectAssessmentLinkContainer">
                <span class="genericTableAddRowLinkContainer" onclick="addAssessmentRow();"><fmt:message key="projects.editStudentProject.addAssessmentLink"/></span>
              </div>
              <div id="projectAssessmentsTableContainer"></div>
            </div>
    
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.descriptionTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.descriptionHelp"/>
              </jsp:include>
              <textarea ix:cktoolbar="studentProjectDescription" name="description" ix:ckeditor="true">${studentProject.description}</textarea>
            </div>
  
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.optionalStudiesTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.optionalStudiesHelp"/>
              </jsp:include>
              <input type="text" name="optionalStudiesLength" class="required" value="${studentProject.optionalStudiesLength.units}" size="15"/>
              <select name="optionalStudiesLengthTimeUnit">           
                <c:forEach var="optionalStudiesLengthTimeUnit" items="${optionalStudiesLengthTimeUnits}">
                  <option value="${optionalStudiesLengthTimeUnit.id}" <c:if test="${studentProject.optionalStudiesLength.unit.id == optionalStudiesLengthTimeUnit.id}">selected="selected"</c:if>>${optionalStudiesLengthTimeUnit.name}</option> 
                </c:forEach>
              </select>            
            </div>
          </div>
          
          <!--  Courses and Modules tab -->
  
          <div id="coursesmodules" class="tabContentixTableFormattedData">
            <div class="genericFormSection editStudentProjectModuleListTitle">
             <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.moduleListTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.moduleListHelp"/>
              </jsp:include>
              
              <div class="editStudentProjectModuleListFilter">
                <select id="moduleFilter">
                  <option value="0"><fmt:message key="projects.editStudentProject.moduleTableFilterNoFilter"/></option>
                  <option value="1"><fmt:message key="projects.editStudentProject.moduleTableFilterShowPassed"/></option>
                  <option value="2"><fmt:message key="projects.editStudentProject.moduleTableFilterShowNonPassed"/></option>
                </select>
              </div>
            </div>
              
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="openSearchModulesDialog();"><fmt:message key="projects.editStudentProject.addModuleLink"/></span>
            </div>
          
            <div id="noModulesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="projects.editStudentProject.noModulesAddedPreFix"/> <span onclick="openSearchModulesDialog();" class="genericTableAddRowLink"><fmt:message key="projects.editStudentProject.noModulesAddedClickHereLink"/></span>.</span>
            </div>
  
            <div id="allModulesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="projects.editStudentProject.allModulesAddedPreFix"/></span>
            </div>
            
            <div id="modulesContainer">
              <div id="modulesTableContainer"></div>
            </div>
  
            <div id="editStudentProjectModulesTotalContainer">
              <fmt:message key="projects.editStudentProject.modulesTotal"/> <span id="editStudentProjectModulesTotalValue"></span>
            </div>
  
            <div class="genericFormSection editStudentProjectCourseListTitle">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.editStudentProject.courseListTitle"/>
                <jsp:param name="helpLocale" value="projects.editStudentProject.courseListHelp"/>
              </jsp:include>
            </div> 
            
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="openSearchCoursesDialog();"><fmt:message key="projects.editStudentProject.addCourseLink"/></span>
            </div>
  
            <div id="noCoursesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="projects.editStudentProject.noCoursesAddedPreFix"/> <span onclick="openSearchCoursesDialog();" class="genericTableAddRowLink"><fmt:message key="projects.editStudentProject.noCoursesAddedClickHereLink"/></span>.</span>
            </div>
  
            <div id="coursesContainer">
              <div id="coursesTableContainer"></div>
            </div>

            <div id="editStudentProjectCoursesTotalContainer">
              <fmt:message key="projects.editStudentProject.coursesTotal"/> <span id="editStudentProjectCoursesTotalValue"></span>
            </div>
          </div>
        </div>
      </div>
    
      <div class="genericFormSubmitSection">
        <input type="submit" class="formvalid" value="<fmt:message key="projects.editStudentProject.saveButton"/>">
      </div>
    
    </form>

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>