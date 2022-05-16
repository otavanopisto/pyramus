<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="courses.manageCourseAssessments.pageTitle">
        <fmt:param value="${course.name}"/>
      </fmt:message>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
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

    <script type="text/javascript">
      var MAX_EDITALLROWS = 30;

      function toggleRowEditable(table, rowIndex) {
        var modifiedCol = table.getNamedColumnIndex('modified');
        var modified = table.getCellValue(rowIndex, modifiedCol) == 1;
        setRowEditable(table, rowIndex, !modified);
      }
      
      function setRowEditable(table, rowIndex, editable) {
        var modifiedCol = table.getNamedColumnIndex('modified');
        var gradeCol = table.getNamedColumnIndex('gradeId');
        var participationCol = table.getNamedColumnIndex('participationType');
        var assessingUserCol = table.getNamedColumnIndex('assessingUserId');
        var assessmentDateCol = table.getNamedColumnIndex('assessmentDate');
        var editVerbalAssessmentButtonCol = table.getNamedColumnIndex('editVerbalAssessmentButton');

        table.setCellEditable(rowIndex, gradeCol, editable);
        table.setCellEditable(rowIndex, participationCol, editable);
        table.setCellEditable(rowIndex, assessingUserCol, editable);
        table.setCellEditable(rowIndex, assessmentDateCol, editable);

        if (editable) {
          table.showCell(rowIndex, editVerbalAssessmentButtonCol);

          var value = table.getCellValue(rowIndex, assessmentDateCol);
          if (!(value && value !== '')) {
            table.setCellValue(rowIndex, assessmentDateCol, new Date().getTime());
          }

          value = table.getCellValue(rowIndex, assessingUserCol);
          if (!(value && value !== '')) {
            table.setCellValue(rowIndex, assessingUserCol, '${loggedUserId}');
            IxTableControllers.getController('autoCompleteSelect').setDisplayValue(table.getCellEditor(rowIndex, assessingUserCol), '${fn:escapeXml(loggedUserName)}');
          }
        } else {
          table.hideCell(rowIndex, editVerbalAssessmentButtonCol);
        }

        table.setCellValue(rowIndex, modifiedCol, editable ? 1 : 0);
      } 

      function checkEditAllBtnStatus(table) {
        var headerCell = table.getHeaderCell(table.getNamedColumnIndex('editRowButton'));
        var buttonElement = headerCell.down('.ixTableHeaderCellImageButton');
        
        if (buttonElement) {
          if (table.getVisibleRowCount() > MAX_EDITALLROWS) {
            buttonElement.addClassName('ixTableHeaderCellImageButtonDisabled');            
          } else {
            buttonElement.removeClassName('ixTableHeaderCellImageButtonDisabled');            
          }
        }
      }

      function openEditVerbalAssessmentDialog(moduleId, row) {
        var table = getIxTableById("courseModule." + moduleId + ".studentsTable"); 
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
              var verbalShort = event.results.verbalAssessment.stripTags().trim().truncate(17);
              
              var table = getIxTableById("courseModule." + moduleId + ".studentsTable"); 
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
      
      function setupStudentsTable(moduleId) {
        var studentsTable = new IxTable($('studentsTableContainer.' + moduleId), {
          id : "courseModule." + moduleId + ".studentsTable",
          columns : [{
            width: 22,
            left: 8,
            dataType: 'button',
            paramName: 'studentInfoButton',
            imgsrc: GLOBAL_contextPath + '/gfx/info.png',
            tooltip: '<fmt:message key="courses.manageCourseAssessments.studentsTableStudentInfoTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var personId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              var button = table.getCellEditor(event.row, table.getNamedColumnIndex('studentInfoButton'));
              openStudentInfoPopupOnElement(button, personId);
            } 
          }, {
            headerimg: {
              imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
              tooltip: '<fmt:message key="courses.manageCourseAssessments.studentsTableEditAllTooltip"/>',
              onclick: function (event) {
                var table = event.tableComponent;
                if (table.getVisibleRowCount() <= MAX_EDITALLROWS) {
                  var glassPane = new IxGlassPane(document.body, { });
                  table.detachFromDom();
                  glassPane.show();
                  
                  setTimeout(function () {
                    for (var i = 0, len = table.getRowCount(); i < len; i++) {
                      if (table.isRowVisible(i)) {
                        toggleRowEditable(table, i);
                      }
                    }
                    table.reattachToDom();

                    glassPane.hide();
                    delete glassPane;
                  }, 0);
                }
              }
            },
            left: 38,
            width: 22,
            paramName: 'editRowButton',
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="courses.manageCourseAssessments.studentsTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;

              toggleRowEditable(table, event.row);
              forceRevalidateAll(true);
            }
          }, {
            header : '<fmt:message key="courses.manageCourseAssessments.studentsTableNameHeader"/>',
            left : 8 + 22 + 8 + 22 + 8,
            right : 8 + 22 + 8 + 100 + 8 + 145 + 8 + 150 + 8 + 130 + 8 + 80 + 8 + 100 + 8,
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
            header : '<fmt:message key="courses.manageCourseAssessments.studentsTableStudyProgrammeHeader"/>',
            width: 160,
            right : 8 + 22 + 8 + 100 + 8 + 145 + 8 + 150 + 8 + 130 + 8 + 80 + 8,
            dataType : 'text',
            editable: false,
            paramName: 'studyProgrammeName',
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
            header : '<fmt:message key="courses.manageCourseAssessments.studentsTableGradeHeader"/>',
            width: 80,
            right : 8 + 22 + 8 + 100 + 8 + 145 + 8 + 150 + 8 + 130 + 8,
            dataType : 'select',
            required: true,
            editable: false,
            paramName: 'gradeId',
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
            ]            
          }, {
            header : '<fmt:message key="courses.manageCourseAssessments.studentsTableParticipationTypeHeader"/>',
            width: 130,
            right : 8 + 22 + 8 + 100 + 8 + 145 + 8 + 150 + 8,
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
            header : '<fmt:message key="courses.manageCourseAssessments.studentsTableAssessingUserHeader"/>',
            width : 150,
            right: 8 + 22 + 8 + 100 + 8 + 145 + 8,
            dataType: 'autoCompleteSelect',
            autoCompleteParamName: 'userId',
            required: true,
            editable: false,
            paramName: 'assessingUserId',
            autoCompleteUrl: GLOBAL_contextPath + '/users/usersautocomplete.binary',
            autoCompleteProgressUrl: '${pageContext.request.contextPath}/gfx/progress_small.gif',
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
            header : '<fmt:message key="courses.manageCourseAssessments.studentsTableAssessmentDateHeader"/>',
            width: 145,
            right : 8 + 22 + 8 + 100 + 8,
            dataType: 'date',
            editable: false,
            paramName: 'assessmentDate',
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
            header : '<fmt:message key="courses.manageCourseAssessments.studentsTableVerbalAssessmentHeader"/>',
            width : 100,
            right : 8 + 22 + 8,
            dataType : 'text',
            paramName: 'verbalAssessmentShort',
            editable: false
          }, {
            width: 22,
            right: 8,
            hidden: true,
            dataType: 'button',
            paramName: 'editVerbalAssessmentButton',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="courses.manageCourseAssessments.studentsTableEditVerbalAssessmentTooltip"/>',
            onclick: function (event) {
              openEditVerbalAssessmentDialog(moduleId, event.row);
            }
          }, {
            dataType: 'hidden', 
            paramName: 'courseStudentId'
          }, {
            dataType: 'hidden', 
            paramName: 'assessmentId'
          }, {
            dataType: 'hidden', 
            paramName: 'personId'
          }, {
            dataType: 'hidden', 
            paramName: 'modified'
          }, {
            dataType: 'hidden', 
            paramName: 'verbalAssessment'
          }, {
            dataType: 'hidden', 
            paramName: 'verbalModified'
          }]
        });

        studentsTable.addListener("afterRowVisibilityChange", function (event) {
          var table = event.tableComponent;
          checkEditAllBtnStatus(table);
        });
        
        var rowIndex;
        var userColumnIndex = studentsTable.getNamedColumnIndex('assessingUserId');
        
        studentsTable.detachFromDom();

        var rows = new Array();

        var allCourseModuleCourseStudents = JSDATA["courseModuleCourseStudents"].evalJSON();
        var courseModuleCourseStudents = allCourseModuleCourseStudents[moduleId];

        for (var i = 0, l = courseModuleCourseStudents.length; i < l; i++) {
          var courseModuleCourseStudent = courseModuleCourseStudents[i];
          var verbalAssessment = courseModuleCourseStudent.verbalAssessment;
          if (verbalAssessment && verbalAssessment.length > 14) {
            verbalAssessment = verbalAssessment.substring(0, 14) + "...";
          }
          
          var rowIndex = studentsTable.addRow([
            '',
            '',
            courseModuleCourseStudent.fullName,
            courseModuleCourseStudent.studyProgrammeName,
            courseModuleCourseStudent.gradeId,
            courseModuleCourseStudent.participationTypeId,
            courseModuleCourseStudent.assessorId,
            courseModuleCourseStudent.assessmentDate,
            verbalAssessment,
            '',
            courseModuleCourseStudent.courseStudentId,
            courseModuleCourseStudent.assessmentId,
            courseModuleCourseStudent.personId,
            0,
            '',
            0
          ]);

          IxTableControllers.getController('autoCompleteSelect').setDisplayValue(studentsTable.getCellEditor(rowIndex, userColumnIndex), courseModuleCourseStudent.assessorName);
        }

        studentsTable.reattachToDom();
        
        if (studentsTable.getRowCount() > 0) {
          $('manageCourseAssessmentsStudentsTotalValue').innerHTML = studentsTable.getRowCount();
        }

        checkEditAllBtnStatus(studentsTable);
      }
      
      function setupRelatedCommands() {
        var basicRelatedActionsHoverMenu = new IxHoverMenu($('relatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="courses.manageCourseAssessments.basicTabRelatedActionsLabel"/>'
        });
    
        basicRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/eye.png',
          text: '<fmt:message key="courses.manageCourseAssessments.viewCourseRelatedActionLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/courses/viewcourse.page?course=${course.id}');
          }
        }));
        
        basicRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="courses.manageCourseAssessments.editCourseRelatedActionLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/courses/editcourse.page?course=${course.id}');
          }
        }));
      }
            
      // onLoad

      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        setupRelatedCommands();

        <c:forEach var="courseModule" items="${course.courseModules}">
          setupStudentsTable(${courseModule.id});
        </c:forEach>
      }

    </script>
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">
      <fmt:message key="courses.manageCourseAssessments.pageTitle">
        <fmt:param value="${course.name}"/>
      </fmt:message>
    </h1>
    
    <div id="manageCourseAssessmentsEditFormContainer">
      <div class="genericFormContainer">
        <form action="savecourseassessments.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <input type="hidden" name="courseId" value="${course.id}"/>
          <input type="hidden" name="version" value="${course.version}"/>

          <div class="tabLabelsContainer" id="tabs">
            <c:forEach var="courseModule" items="${course.courseModules}">
              <a class="tabLabel" href="#cm-${courseModule.id}">${courseModule.subject.name} ${courseModule.courseNumber}</a>
            </c:forEach>
          </div>
  
          <c:forEach var="courseModule" items="${course.courseModules}">
            <div id="cm-${courseModule.id}" class="tabContent">
              <div id="relatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
  
              <div id="manageCourseAssessmentsStudentsTableContainer">
                <c:if test="${fn:length(courseStudents) eq 0}">
                  <div id="noStudentsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
                    <span><fmt:message key="courses.manageCourseAssessments.noStudentsAddedMessage"/></span>
                  </div>
                </c:if>
              
                <div id="studentsTableContainer.${courseModule.id}"> </div>
  
                <c:if test="${fn:length(courseStudents) gt 0}">
                  <div id="manageCourseAssessmentsStudentsTotalContainer">
                    <fmt:message key="courses.manageCourseAssessments.studentsTotal"/> <span id="manageCourseAssessmentsStudentsTotalValue"></span>
                  </div>
                </c:if>
  
              </div>
            </div>
          </c:forEach>
      
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="courses.manageCourseAssessments.saveButton"/>">
          </div>
        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>
