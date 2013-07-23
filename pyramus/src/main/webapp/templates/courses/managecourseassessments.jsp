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

      function setRowEditable(table, rowIndex) {
        var modifiedCol = table.getNamedColumnIndex('modified');
        
        if (table.getCellValue(rowIndex, modifiedCol) == 0) {
          var gradeCol = table.getNamedColumnIndex('gradeId');
          var participationCol = table.getNamedColumnIndex('participationType');
          var assessingUserCol = table.getNamedColumnIndex('assessingUserId');
          var assessmentDateCol = table.getNamedColumnIndex('assessmentDate');
          var editVerbalAssessmentButtonCol = table.getNamedColumnIndex('editVerbalAssessmentButton');

          table.setCellEditable(rowIndex, gradeCol, table.isCellEditable(rowIndex, gradeCol) == false);
          table.setCellEditable(rowIndex, participationCol, table.isCellEditable(rowIndex, participationCol) == false);
          table.setCellEditable(rowIndex, assessingUserCol, table.isCellEditable(rowIndex, assessingUserCol) == false);
          table.setCellEditable(rowIndex, assessmentDateCol, table.isCellEditable(rowIndex, assessmentDateCol) == false);

          table.showCell(rowIndex, editVerbalAssessmentButtonCol);
          
          var value = table.getCellValue(rowIndex, assessmentDateCol);
          if (!(value && value !== ''))
            table.setCellValue(rowIndex, assessmentDateCol, new Date().getTime());

          value = table.getCellValue(rowIndex, assessingUserCol);
          if (!(value && value !== '')) {
            table.setCellValue(rowIndex, assessingUserCol, '${loggedUserId}');
            IxTableControllers.getController('autoCompleteSelect').setDisplayValue(table.getCellEditor(rowIndex, assessingUserCol), '${fn:escapeXml(loggedUserName)}');
          }

          table.setCellValue(rowIndex, modifiedCol, 1);
        }
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

      function openEditVerbalAssessmentDialog(row) {
        var table = getIxTableById("studentsTable"); 
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
              
              var table = getIxTableById("studentsTable"); 
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
      
      function setupStudentsTable() {
        var studentsTable = new IxTable($('studentsTable'), {
          id : "studentsTable",
          columns : [{
            width: 22,
            left: 8,
            dataType: 'button',
            paramName: 'studentInfoButton',
            imgsrc: GLOBAL_contextPath + '/gfx/info.png',
            tooltip: '<fmt:message key="courses.manageCourseAssessments.studentsTableStudentInfoTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var abstractStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('abstractStudentId'));
              var button = table.getCellEditor(event.row, table.getNamedColumnIndex('studentInfoButton'));
              openStudentInfoPopupOnElement(button, abstractStudentId);
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
                      if (table.isRowVisible(i))
                        setRowEditable(table, i);
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

              setRowEditable(table, event.row);
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
              openEditVerbalAssessmentDialog(event.row);
            }
          }, {
            dataType: 'hidden', 
            paramName: 'courseStudentId'
          }, {
            dataType: 'hidden', 
            paramName: 'assessmentId'
          }, {
            dataType: 'hidden', 
            paramName: 'abstractStudentId'
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
        <c:forEach var="courseStudent" items="${courseStudents}" varStatus="status">
          <c:choose>
            <c:when test="${courseStudent.student.studyProgramme ne null}">
              <c:set var="studyProgrammeName">${fn:escapeXml(courseStudent.student.studyProgramme.name)}</c:set>
            </c:when>
            <c:otherwise>
              <c:set var="studyProgrammeName"><fmt:message key="courses.manageCourseAssessments.studentsTableNoStudyProgrammeLabel"/></c:set>
            </c:otherwise>
          </c:choose>
          
          <c:choose>
            <c:when test="${fn:length(verbalAssessments[assessments[courseStudent.id].id]) gt 14}">
              <c:set var="verbalAssessment">${fn:substring(verbalAssessments[assessments[courseStudent.id].id], 0, 14)}...</c:set>
            </c:when>
            <c:otherwise>
              <c:set var="verbalAssessment">${verbalAssessments[assessments[courseStudent.id].id]}</c:set>
            </c:otherwise>
          </c:choose>
          
          rowIndex = studentsTable.addRow([
            '',
            '',
            "${fn:escapeXml(courseStudent.student.lastName)}, ${fn:escapeXml(courseStudent.student.firstName)}", 
            "${studyProgrammeName}", 
            '${assessments[courseStudent.id].grade.id}',
            '${courseStudent.participationType.id}',
            '${assessments[courseStudent.id].assessingUser.id}',
            '${assessments[courseStudent.id].date.time}',
            '${verbalAssessment}',
            '',
            '${courseStudent.id}',
            '${assessments[courseStudent.id].id}',
            '${courseStudent.student.abstractStudent.id}',
            0,
            '',
            0]);
          IxTableControllers.getController('autoCompleteSelect').setDisplayValue(studentsTable.getCellEditor(rowIndex, userColumnIndex), '${fn:escapeXml(assessments[courseStudent.id].assessingUser.fullName)}');
        </c:forEach>
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
        setupStudentsTable();
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
          <input type="hidden" name="course" value="${course.id}"/>
          <input type="hidden" name="version" value="${course.version}"/>

          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#students"><fmt:message key="courses.manageCourseAssessments.studentsTabTitle" /></a>
          </div>
  
          <div id="students" class="tabContent">
            <div id="relatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>

            <div id="manageCourseAssessmentsStudentsTableContainer">
              <c:if test="${fn:length(courseStudents) eq 0}">
                <div id="noStudentsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
                  <span><fmt:message key="courses.manageCourseAssessments.noStudentsAddedMessage"/></span>
                </div>
              </c:if>
            
              <div id="studentsTable"> </div>

              <c:if test="${fn:length(courseStudents) gt 0}">
                <div id="manageCourseAssessmentsStudentsTotalContainer">
                  <fmt:message key="courses.manageCourseAssessments.studentsTotal"/> <span id="manageCourseAssessmentsStudentsTotalValue"></span>
                </div>
              </c:if>

            </div>
          </div>
      
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="courses.manageCourseAssessments.saveButton"/>">
          </div>
        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>
