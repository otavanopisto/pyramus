<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <title><fmt:message key="students.viewStudent.pageTitle">
    <fmt:param value="${person.latestStudent.fullName}" />
  </fmt:message></title>
<jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
<jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/datefield_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/hoverpanel_support.jsp"></jsp:include>

<link href="${pageContext.request.contextPath}/css/viewstudent-subjectcredits.css" rel="stylesheet">

<script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/moment/moment.min.js"></script>
<script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/students/koski.js"></script>

<!-- Used to render memo values with line breaks; for some reason this is the only approach that works -->
<%
  pageContext.setAttribute("newLineChar", "\n");
%>

<script type="text/javascript">
      function setupBasicTab(personId, studentId, studentFullName) {
        var basicTabRelatedActionsHoverMenu = new IxHoverMenu($('basicTabRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsLabel"/>'
        });
    
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsEditStudentLabel"/>',
          link: GLOBAL_contextPath + '/students/editstudent.page?person=' + personId  
        }));

        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsManageContactEntriesLabel"/>',
          link: GLOBAL_contextPath + '/students/managestudentcontactentries.page?person=' + personId  
        }));
        
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsManageTransferCreditsLabel"/>',
          link: GLOBAL_contextPath + '/grading/managetransfercredits.page?studentId=' + studentId  
        }));

        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsEditStudentKoskiLabel"/>',
          onclick: function (event) {
            openEditStudentKoskiDialog(personId);
          }
        }));
        
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsChangeStudyProgrammeLabel"/>',
          onclick: function (event) {
            openChangeStudyProgrammeDialog(studentId);
          }
        }));
        
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsEditStudentImageLabel"/>',
          onclick: function (event) {
            openEditStudentImageDialog(studentId);
          }
        }));
        
        <c:if test="${empty staffMember}">
          basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
            iconURL: GLOBAL_contextPath + '/gfx/list-add.png',
            text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsAddStaffMemberLabel"/>',
            link: GLOBAL_contextPath + '/users/createuser.page?studentId=' + studentId 
          }));
        </c:if>

        var defaultUserId = "${person.defaultUser.id}";
        
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/star.png',
          iconOpacity: (studentId == defaultUserId) ? 1.0 : 0.4, 
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsSetAsDefaultUserLabel"/>',
          onclick: function (event) {
            JSONRequest.request("users/setdefaultuser.json", {
              parameters: {
                personId: personId,
                userId: studentId
              },
              onSuccess: function (jsonResponse) {
                window.location.reload(true);
              }
            }); 
          }  
        }));     

        <c:if test="${loggedUserRole == 'ADMINISTRATOR'}">
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/icons/16x16/apps/attention.png',
          text: '<fmt:message key="students.viewStudent.basicTabRelatedActionsPoseAsLabel"/>',
          onclick: function (event) {
            JSONRequest.request("users/pose.json", {
              parameters: {
                userId: studentId
              },
              onSuccess: function (jsonResponse) {
                window.location = GLOBAL_contextPath + "/";
              }
            }); 
          }  
        }));
        </c:if>      
        
        var extensionHoverMenuLinks = $$('#extensionHoverMenuLinks a');
        for (var i=0, l=extensionHoverMenuLinks.length; i<l; i++) {
          var extensionHoverMenuLink = extensionHoverMenuLinks[i];
          if (extensionHoverMenuLink.href.indexOf('?') == -1) {
	          basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
	              iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
	              text: extensionHoverMenuLink.innerHTML,
	              link: GLOBAL_contextPath + extensionHoverMenuLink.href + "?studentId=" + studentId
	          }));
          } else {
	          basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
	              iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
	              text: extensionHoverMenuLink.innerHTML,
	              link: GLOBAL_contextPath + extensionHoverMenuLink.href + "&studentId=" + studentId
	          }));
          }
        }

        var studentReports = JSDATA["studentReports"].evalJSON();
        
        if (studentReports) {
          if (studentReports.length > 0) {
            basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuSpacer());

            for (var i = 0, l = studentReports.length; i < l; i++) {
              var reportId = studentReports[i].id;
              var reportName = studentReports[i].name;
              
              basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
                iconURL: GLOBAL_contextPath + '/gfx/icons/16x16/apps/report.png',
                text: reportName,
                link: GLOBAL_contextPath + '/reports/viewreport.page?reportId=' + reportId + "&studentId=" + studentId
              }));
            }            
          }
        }
        
        var gradesTabRelatedActionsHoverMenu = new IxHoverMenu($('gradesTabRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.viewStudent.gradesTabRelatedActionsLabel"/>'
        });
        
        gradesTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.gradesTabRelatedActionsManageTransferCreditsLabel"/>',
          link: GLOBAL_contextPath + '/grading/managetransfercredits.page?studentId=' + studentId  
        }));
        gradesTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.gradesTabRelatedActionsImportStudentCreditsLabel"/>',
          link: GLOBAL_contextPath + '/students/importstudentcredits.page?studentId=' + studentId  
        }));

        var contactLogTabRelatedActionsHoverMenu = new IxHoverMenu($('contactLogTabRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.viewStudent.contactLogTabRelatedActionsLabel"/>'
        });
    
        contactLogTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.contactLogTabRelatedActionsManageContactEntriesLabel"/>',
          link: GLOBAL_contextPath + '/students/managestudentcontactentries.page?person=' + personId  
        }));

        var projectsTabRelatedActionsHoverMenu = new IxHoverMenu($('projectsTabRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.viewStudent.contactLogTabRelatedActionsLabel"/>'
        });

        projectsTabRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudent.projectsTabRelatedActionsAddProjectLabel"/>',
          onclick: function (event) {
            openAddNewProjectDialog(studentId);         
          }
        }));
      }

      function initStudentVariableTable(studentId) {
        var variablesTable = new IxTable($('variablesTableContainer.' + studentId), {
          id : "variablesTable." + studentId,
          columns : [{
            left : 8,
            width: 160,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            left : 180,
            width : 500,
            dataType: 'text',
            editable: false,
            paramName: 'value'
          }]
        });

        return variablesTable;
      }

      function initPersonVariablesTable(studentId, personVariables) {
        var variablesTable = new IxTable($('personVariablesTableContainer.' + studentId), {
          id : "personVariablesTable." + studentId,
          columns : [{
            left : 8,
            width: 160,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            left : 180,
            width : 500,
            dataType: 'text',
            editable: false,
            paramName: 'value'
          }]
        });

        if (personVariables && personVariables.length > 0) {
          for (var i = 0, l = personVariables.length; i < l; i++) {
            var rowNumber = variablesTable.addRow([
              personVariables[i].name,
              personVariables[i].value
            ]);
  
            switch (personVariables[i].type) {
              case 'NUMBER':
                variablesTable.setCellDataType(rowNumber, 1, 'text');
              break;
              case 'DATE':
                variablesTable.setCellDataType(rowNumber, 1, 'date');
              break;
              case 'BOOLEAN':
                variablesTable.setCellDataType(rowNumber, 1, 'checkbox');
              break;
              default:
                variablesTable.setCellDataType(rowNumber, 1, 'text');
              break;
            }
          }
        }
        
        return variablesTable;
      }

      function openStudentCourseAssessmentRequestsPopupOnElement(element, courseStudentId) {
        var hoverPanel = new IxHoverPanel({
          contentURL: GLOBAL_contextPath + '/students/studentcourseassessmentrequestspopup.page?courseStudent=' + courseStudentId
        });

        hoverPanel.showOverElement(element);
      }
      
      function openAddNewProjectDialog(studentId) {
        var dialog = new IxDialog({
          id : 'selectProjectDialog',
          contentURL : GLOBAL_contextPath + '/projects/selectprojectdialog.page',
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="projects.selectProjectDialog.dialogTitle"/>',
          okLabel : '<fmt:message key="projects.selectProjectDialog.okLabel"/>', 
          cancelLabel : '<fmt:message key="projects.selectProjectDialog.cancelLabel"/>' 
        });
        
        dialog.setSize("350px", "200px");
        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'okClick':
              var selectedProjectId = event.results.selectedProjectId;
              var optionality = event.results.optionality;

              JSONRequest.request("students/createstudentproject.json", {
                parameters: {
                  projectId: selectedProjectId,
                  studentId: studentId,
                  optionality: optionality
                },
                onSuccess: function (jsonResponse) {
                  redirectTo(GLOBAL_contextPath + '/students/viewstudent.page?person=${person.id}'); //'#at-studentProject.' + studentId);
                }
              });
            break;
          }
        });
        
        dialog.open();
      }
      
      function setupCoursesTab(studentId) {
        var relatedContainer = $('tabRelatedActionsContainer.' + studentId);
    
        var coursesTable = new IxTable($('coursesTableContainer.' + studentId), {
          id: 'coursesTable.' + studentId,
          rowHoverEffect: true,
          columns : [{
            header : '<fmt:message key="students.viewStudent.coursesTableNameHeader"/>',
            left: 8,
            right: 8 + 22 + 8 + 22 + 8 + 150 + 8 + 200 + 8,
            dataType: 'text',
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
            header : '<fmt:message key="students.viewStudent.coursesTableCourseStateHeader"/>',
            width: 200,
            right: 8 + 22 + 8 + 22 + 150 + 8 + 8 + 150 + 8,
            dataType: 'text',
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
            },
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]            
          }, {
            header : '<fmt:message key="students.viewStudent.coursesTableCourseEnrolmentTimeHeader"/>',
            width: 150,
            right: 8 + 22 + 8 + 22 + 8 + 150 + 8,
            dataType: 'date',
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
              }
            ]            
          }, {
            header : '<fmt:message key="students.viewStudent.coursesTableAssessmentRequestsHeader"/>',
            width: 150,
            right: 8 + 22 + 8 + 22 + 8,
            dataType: 'date',
            editable: false,
            paramName: 'courseAssessmentRequest',
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSTRINGSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSTRINGSORT
              }
            },
            contextMenu: [
              {
                text: '<fmt:message key="students.viewStudent.viewStudentCourseAssessmentRequestsTitle"/>',
                onclick: { 
                  execute: function (event) {
                    var table = event.tableComponent;
                    var row = event.row;
                    var cell = table.getCellEditor(row, table.getNamedColumnIndex('courseAssessmentRequest'));
                    var courseStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('courseStudentId'));
                    
                    openStudentCourseAssessmentRequestsPopupOnElement(cell, courseStudentId);
                  }
                }
              },
              {
                text: '<fmt:message key="generic.filter.empty"/>',
                onclick: new IxTable_ROWEMPTYFILTER(false)
              },
              {
                text: '<fmt:message key="generic.filter.nonEmpty"/>',
                onclick: new IxTable_ROWEMPTYFILTER(true)
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]            
          }, {
            dataType: 'hidden',
            paramName: 'courseStudentId'
          }, {
            dataType: 'hidden',
            paramName: 'courseId'
          }, {
            width: 22,
            right: 8 + 22 + 8,
            dataType: 'button',
            paramName: 'evaluateButton',
            imgsrc: GLOBAL_contextPath + '/gfx/kdb_form.png',
            tooltip: '<fmt:message key="students.viewStudent.coursesTableEvaluateStudentTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('courseStudentId'));
              redirectTo(GLOBAL_contextPath + '/grading/courseassessment.page?courseStudentId=' + courseStudentId);
            } 
          }, {
            width: 30,
            right: 00,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="students.viewStudent.courseTableViewTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
              redirectTo(GLOBAL_contextPath + '/courses/viewcourse.page?course=' + courseId);
            }
          }]
        });

        return coursesTable;
      }

      function setupFilesTable(studentId) {
        var relatedActions = new IxHoverMenu($('filesTabRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.viewStudent.filesTabRelatedActionsLabel"/>'
        });
        
        relatedActions.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/list-add.png',
          text: '<fmt:message key="students.viewStudent.filesTabRelatedActionsAddFileLinkLabel"/>',
          onclick: function (event) {
            openAddNewFileDialog(studentId);
          }
        }));

        var filesTable = new IxTable($('filesTableContainer.' + studentId), {
          id: 'filesTable.' + studentId,
          rowHoverEffect: true,
          columns : [{
            header : '<fmt:message key="students.viewStudent.filesTableNameHeader"/>',
            left: 8,
            right: 8 + 22 + 8 + 22 + 8 + 22 + 8 + 150 + 8 + 200 + 8,
            dataType: 'text',
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
            header : '<fmt:message key="students.viewStudent.filesTableFileTypeHeader"/>',
            width: 200,
            right: 8 + 22 + 8 + 22 + 8 + 22 + 8 + 150 + 8,
            dataType: 'text',
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
            },
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]            
          }, {
            header : '<fmt:message key="students.viewStudent.filesTableFileDateHeader"/>',
            width: 150,
            right: 8 + 22 + 8 + 22 + 8 + 22 + 8,
            dataType: 'date',
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
              }
            ]            
          }, {
            dataType: 'hidden',
            paramName: 'fileId'
          }, {
            width: 22,
            right: 8 + 22 + 8 + 22 + 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="students.viewStudent.filesTableDownloadFileTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var fileId = table.getCellValue(event.row, table.getNamedColumnIndex('fileId'));
              redirectTo(GLOBAL_contextPath + '/studentfiles/downloadfile.binary?fileId=' + fileId);
            }
          }, {
            width: 22,
            right: 8 + 22 + 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.viewStudent.filesTableEditFileTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var fileId = table.getCellValue(event.row, table.getNamedColumnIndex('fileId'));
              openEditFileDialog(fileId, studentId);
            } 
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="students.viewStudent.filesTableDeleteFileTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var fileId = table.getCellValue(event.row, table.getNamedColumnIndex('fileId'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=students.viewStudent.filesTableDeleteFileArchiveConfirmDialogContent"

              archivedRowIndex = event.row; 
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="students.viewStudent.filesTableDeleteFileArchiveConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="students.viewStudent.filesTableDeleteFileArchiveConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="students.viewStudent.filesTableDeleteFileArchiveConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener(function(event) {
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("studentfiles/archivestudentfile.json", {
                      parameters: {
                        fileId: fileId
                      },
                      onSuccess: function (jsonResponse) {
                        table.deleteRow(archivedRowIndex);
                      }
                    });   
                  break;
                }
              });
            
              dialog.open();

            } 
          }]
        });

        return filesTable;
      }

      function setupSubjectCreditsTable(studentId) {
        var subjectCreditsData = JSDATA["subjectCredits"].evalJSON();
        
        if (subjectCreditsData) {
          var studentSubjectCreditsData = subjectCreditsData["" + studentId];

          if (studentSubjectCreditsData) {
            var container = $('subjectCreditsTable.' + studentId);

            for (var i = 0, l = studentSubjectCreditsData.problems.length; i < l; i++) {
              var problem = studentSubjectCreditsData.problems[i];

              var problemElement = container.appendChild(new Element("div", {className: "studentSubjectCreditsProblem"}));
              var exclMark = problemElement.appendChild(new Element("div", {className: "studentSubjectCreditsProblemIcon"}));
              var problemText = problemElement.appendChild(new Element("div", {className: "studentSubjectCreditsProblemText"}));

              var reason = getLocale().getText("student.tor.problems." + problem.type);
              var details = problem.additionalInfo ? " (" + problem.additionalInfo + ")" : "";
              problemText.update(reason + details);
            }
            
            for (var i = 0, l = studentSubjectCreditsData.subjects.length; i < l; i++) {
              var subject = studentSubjectCreditsData.subjects[i];
              var subjectElement = container.appendChild(new Element("div", {className: "studentSubjectCreditsSubject"}));
              var subjectNameElement = subjectElement.appendChild(new Element("div", {className: "studentSubjectCreditsSubjectName"}));
              subjectNameElement.update(subject.name);
              var subjectCreditsContainer = subjectElement.appendChild(new Element("div", {className: "studentSubjectCreditsCreditsContainer"}));
  
              if (subject.courses) {
                for (var j = 0, m = subject.courses.length; j < m; j++) {
                  var course = subject.courses[j];
                  var courseContainer = subjectCreditsContainer.appendChild(new Element("div", {className: "studentSubjectCreditsCourseContainer"}));
                  var courseIdentifierElement = courseContainer.appendChild(new Element("div", {className: "studentSubjectCreditsCourseIdentifier"}));

                  var courseNumber = (course.courseNumber) ? course.courseNumber : "";
                  var courseIdentifier = course.subject.code + courseNumber;
                  courseIdentifierElement.update(courseIdentifier);

                  var gradesContainer = courseContainer.appendChild(new Element("div", {className: "studentSubjectCreditsGradesContainer"}));

                  // There are bunch of credits for a single course - figure out a way to show all of them (in order)
                  if (course.credits && course.credits.length > 0) {
                    var credit = course.credits[0];

                    if (credit.passingGrade == false) {
                      // Credit is not a passing grade, try to see if there is a passing grade in the list to override this
                      // This is not necessarily the "best grade" but at least it's passing
                      for (var crindx = 1; crindx < course.credits.length; crindx++) {
                        if (course.credits[crindx].passingGrade == true) {
                          credit = course.credits[crindx];
                          break;
                        }
                      }
                    }

                    var gradeContainer = gradesContainer.appendChild(new Element("span", {className: "studentSubjectCreditsGradeContainer"}));
                    var gradeElement = gradeContainer.appendChild(new Element("span", {className: "studentSubjectCreditsGrade"}));
                    
                    var courseGrade = credit.gradeName;
                    gradeElement.update(courseGrade);
                  }
                }
              }

              var pccElement = subjectElement.appendChild(new Element("div", {className: "studentSubjectCreditsPassedCoursesCount"}));
              pccElement.update("" + subject.passedCoursesCount);
              
              var subMeanContainer = subjectElement.appendChild(new Element("div", {className: "studentSubjectCreditsMeanGradeContainer"}));
              var subMeanGrade = subMeanContainer.appendChild(new Element("div", {
                id: "studentSubjectCreditsMeanGrade." + studentId + "." + subject.id,
                className: "studentSubjectCreditsMeanGrade"
              }));
              var subMeanComputedGrade = subMeanContainer.appendChild(new Element("div", {className: "studentSubjectCreditsComputedMeanGrade"}));

              if (subject.meanGrade) {
                subMeanGrade.update("" + subject.meanGrade.name);
              }

              if (subject.computedMeanGrade) {
                subMeanComputedGrade.update("" + subject.computedMeanGrade);
              }

              var subMeanEditButton = subMeanContainer.appendChild(new Element("img", {
                src: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
                "data-studentId": studentId,
                "data-subjectId": subject.id
              }));
              
              Event.observe(subMeanEditButton, "click", clickEditSubjectTotalCredit);
            }
          }
        }
      }

      function clickEditSubjectTotalCredit(event) {
        var button = Event.element(event);
        var studentId = button.getAttribute("data-studentId");
        var subjectId = button.getAttribute("data-subjectId")
        
        var dialog = new IxDialog({
          id : 'uploadReportDialog',
          contentURL : GLOBAL_contextPath + '/students/editstudentsubjectgradedialog.page?studentId=' + studentId + '&subjectId=' + subjectId,
          centered : true,
          showOk : true,
          showCancel : true,
          title: '<fmt:message key="students.viewStudent.editStudentSubjectGradeDialog.title"/>',
          okLabel: '<fmt:message key="generic.dialog.save"/>',
          cancelLabel: '<fmt:message key="generic.dialog.cancel"/>'
        });

        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'okClick':
              event.preventDefault(true);

              var contentDoc = dlg.getContentDocument();
              var uploadForm = contentDoc.getElementById("editStudentSubjectGradeForm");
              var studentId = uploadForm.elements["studentId"].value;
              var subjectId = uploadForm.elements["subjectId"].value;
              var gradeId = uploadForm.elements["gradeId"].value;
              var explanation = uploadForm.elements["explanation"].value;
              var gradeDate = uploadForm.elements["gradeDate"].value;
              var gradeApproverId = uploadForm.elements["gradeApproverId"].value;
                
              dlg.disableOkButton();

              JSONRequest.request("students/editstudentsubjectgrade.json", {
                parameters: {
                  studentId: studentId,
                  subjectId: subjectId,
                  gradeId: gradeId,
                  explanation: explanation,
                  gradeDate: gradeDate,
                  gradeApproverId: gradeApproverId
                },
                onSuccess: function (jsonResponse) {
                  var results = jsonResponse.results;

                  var subMeanGradeElement = $("studentSubjectCreditsMeanGrade." + studentId + "." + subjectId);
                  if (results.computed) {
                    subMeanGradeElement.update("");
                  } else {
                    var gradeName = results.gradeName;
                    subMeanGradeElement.update(gradeName);
                  }
                },
                onFailure: function(errorMessage, errorCode, isHttpError, jsonResponse) {
                  alert(errorMessage);
                }
              });   
      
              dlg.close();
            break;
          }
        });
        
        dialog.setSize("400px", "400px");
        dialog.open();
      }
      
      function setupTransferCreditsTable(studentId, containerElement, tableId) {
        /* TODO: Oppilaitos */
        
        var transferCreditsTable = new IxTable(containerElement, {
          id: tableId,
          rowHoverEffect: true,
          columns : [{
            header : '<fmt:message key="students.viewStudent.transferCreditsTableNameHeader"/>',
            left: 8,
            right: 8 + 180 + 8 + 120 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 120 + 8 + 100 + 8 + 200 + 8, 
            dataType: 'text',
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
            header : '<fmt:message key="students.viewStudent.transferCreditsTableSubjectHeader"/>',
            right: 8 + 180 + 8 + 120 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 120 + 8 + 100 + 8, 
            width: 200,
            dataType: 'text',
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
            header : '<fmt:message key="students.viewStudent.transferCreditsTableCurriculumHeader"/>',
            right: 8 + 180 + 8 + 120 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 120 + 8, 
            width: 100,
            dataType: 'text',
            editable: false,
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.byNotValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(undefined, false)
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ],
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
            header : '<fmt:message key="students.viewStudent.transferCreditsTableGradingDateHeader"/>',
            right: 8 + 180 + 8 + 120 + 8 + 100 + 8 + 100 + 8 + 100 + 8, 
            width: 120,
            dataType: 'date',
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
            header : '<fmt:message key="students.viewStudent.transferCreditsTableCourseLengthHeader"/>',
            right: 8 + 180 + 8 + 120 + 8 + 100 + 8 + 100 + 8, 
            width: 100,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableCourseLengthUnitHeader"/>',
            right: 8 + 180 + 8 + 120 + 8 + 100 + 8, 
            width: 100,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableGradeHeader"/>',
            right: 8 + 180 + 8 + 120 + 8, 
            width: 100,
            dataType: 'text',
            editable: false,
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.byNotValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(undefined, false)
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]            
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableGradingScaleHeader"/>',
            right: 8 + 180 + 8, 
            width: 120,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.transferCreditsTableAssessingUserHeader"/>',
            right: 8,
            width: 180,
            dataType: 'text',
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
          }]
        });

        return transferCreditsTable;
      }
      
      function setupCourseAssessmentsTable(studentId, containerElement, tableId, editable) {
        var courseAssessmentsTable = new IxTable(containerElement, {
          id: tableId,
          rowHoverEffect: true,
          columns : [{
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableNameHeader"/>',
            left: 8,
            right: 8 + 22 + 8 + 150 + 8 + 120 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 120 + 8 + 100 + 8 + 200 + 8, 
            dataType: 'text',
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
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableSubjectHeader"/>',
            right: 8 + 22 + 8 + 150 + 8 + 120 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 120 + 8 + 100 + 8, 
            width: 200,
            dataType: 'text',
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
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableCurriculumHeader"/>',
            right: 8 + 22 + 8 + 150 + 8 + 120 + 8 + 100 + 8 + 100 + 8 + 100 + 8 + 120 + 8, 
            width: 100,
            dataType: 'text',
            editable: false,
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.byNotValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(undefined, false)
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ],
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
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableGradingDateHeader"/>',
            right: 8 + 22 + 8 + 150 + 8 + 120 + 8 + 100 + 8 + 100 + 8 + 100 + 8, 
            width: 120,
            dataType: 'date',
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
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableCourseLengthHeader"/>',
            right: 8 + 22 + 8 + 150 + 8 + 120 + 8 + 100 + 8 + 100 + 8, 
            width: 100,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableCourseLengthUnitHeader"/>',
            right: 8 + 22 + 8 + 150 + 8 + 120 + 8 + 100 + 8, 
            width: 100,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableGradeHeader"/>',
            right: 8 + 22 + 8 + 150 + 8 + 120 + 8, 
            width: 100,
            dataType: 'text',
            editable: false,
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.byNotValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(undefined, false)
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]            
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableGradingScaleHeader"/>',
            right: 8 + 22 + 8 + 150 + 8, 
            width: 120,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.courseAssessmentsTableAssessingUserHeader"/>',
            right: 8 + 22 + 8,
            width: 150,
            dataType: 'text',
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
            dataType: 'hidden',
            paramName: 'courseStudentId'
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            hidden: !editable,
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.viewStudent.courseAssessmentsTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('courseStudentId'));
              redirectTo(GLOBAL_contextPath + '/grading/courseassessment.page?courseStudentId=' + courseStudentId);
            }
          }]
        });
  
        return courseAssessmentsTable;
      }

      function setupStudentProjectModuleTable(studentId, projectId) {
        var studentProjectModulesTable = new IxTable($('studentProjectModulesTable.' + studentId + '.' + projectId), {
          id: 'studentProjectModulesTable.' + studentId + '.' + projectId,
          rowHoverEffect: true,
          columns : [{
            header : '<fmt:message key="students.viewStudent.projectTableNameHeader"/>',
            left: 8,
            right: 8 + 120 + 8 + 120 + 8 + 140 + 8, 
            dataType: 'text',
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
            header : '<fmt:message key="students.viewStudent.projectTableOptionalityHeader"/>',
            right :  8 + 120 + 8 + 120 + 8,
            width: 140,
            dataType : 'select',
            editable: false,
            options: [
              {text: '', value: ''},
              {text: '<fmt:message key="students.viewStudent.projectTableOptionalityMandatory"/>', value: 'MANDATORY'},
              {text: '<fmt:message key="students.viewStudent.projectTableOptionalityOptional"/>', value: 'OPTIONAL'}
            ]
          }, {
            header : '<fmt:message key="students.viewStudent.projectTableCourseGradeHeader"/>',
            right: 8 + 120 + 8, 
            width: 120,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.projectTableTransferCreditGradeHeader"/>',
            right: 8, 
            width: 120,
            dataType: 'text',
            editable: false
          }]
        });
  
        return studentProjectModulesTable;
      }

      function setupStudentProjectSubjectCourseTable(studentId, projectId) {
        var studentProjectModulesTable = new IxTable($('studentProjectSubjectCourseTable.' + studentId + '.' + projectId), {
          id: 'studentProjectSubjectCourseTable.' + studentId + '.' + projectId,
          rowHoverEffect: true,
          columns : [{
            header : '<fmt:message key="students.viewStudent.projectSubjectCourseTable.subject"/>',
            left: 8,
            width: 200,
            dataType: 'text',
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
            header : '<fmt:message key="students.viewStudent.projectSubjectCourseTable.courseNumber"/>',
            left :  8 + 200 + 8,
            right: 8 + 120 + 8 + 120 + 8 + 140 + 8, 
            dataType : 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.projectTableOptionalityHeader"/>',
            right :  8 + 120 + 8 + 120 + 8,
            width: 140,
            dataType : 'select',
            editable: false,
            options: [
              {text: '', value: ''},
              {text: '<fmt:message key="students.viewStudent.projectTableOptionalityMandatory"/>', value: 'MANDATORY'},
              {text: '<fmt:message key="students.viewStudent.projectTableOptionalityOptional"/>', value: 'OPTIONAL'}
            ]
          }, {
            header : '<fmt:message key="students.viewStudent.projectTableCourseGradeHeader"/>',
            right: 8 + 120 + 8, 
            width: 120,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudent.projectTableTransferCreditGradeHeader"/>',
            right: 8, 
            width: 120,
            dataType: 'text',
            editable: false
          }]
        });
  
        return studentProjectModulesTable;
      }

      function onLoad(event) {
        var coursesTable;
        var filesTable;
        var transferCreditsTable;
        var courseAssessmentsTable;

        var studentFilesContainer = JSDATA["studentFiles"].evalJSON();
        var linkedCourseAssessmentsContainer = JSDATA["linkedCourseAssessments"].evalJSON();
        var linkedTransferCreditsContainer = JSDATA["linkedTransferCredits"].evalJSON();
        var curriculumContainer = JSDATA["curriculums"].evalJSON();
        var studentVariablesContainer = JSDATA["studentVariables"].evalJSON();
        var studentAssessmentsContainer = JSDATA["studentAssessments"].evalJSON();
        var personVariables = JSDATA["personVariables"].evalJSON();

        Event.observe($('koski-status'), 'click', toggleKoskiLogDetailsVisibility);
        loadLogEntries(${person.id});
        
        <c:forEach var="student" items="${students}">
          // Setup basics
          setupBasicTab(${person.id}, ${student.id}, '${fn:escapeXml(student.fullName)}');

          // Setup course tabs
          coursesTable = setupCoursesTab(${student.id});

          var rows = new Array();
          <c:forEach var="studentCourse" items="${courses[student.id]}">
            <c:set var="courseName">${studentCourse.course.name}</c:set>

            <c:if test="${not empty studentCourse.course.nameExtension}">
              <c:set var="courseName">${studentCourse.course.name} (${studentCourse.course.nameExtension})</c:set>
            </c:if>
          
            var assessmentRequest = "";
            <c:if test="${courseAssessmentRequests[studentCourse.id] ne null}">
            assessmentRequest = "${courseAssessmentRequests[studentCourse.id].created.time}";
            </c:if>
            
            rows.push([
                       '${courseName}', 
                       '${studentCourse.participationType.name}', 
                       '${studentCourse.enrolmentTime.time}',
                       assessmentRequest,
                       ${studentCourse.id}, 
                       ${studentCourse.course.id}, 
                       '', 
                       '']);
          </c:forEach>
          coursesTable.addRows(rows);
          if (coursesTable.getRowCount() > 0) {
            $('viewStudentCoursesTotalValue.${student.id}').innerHTML = coursesTable.getRowCount();
          }
          else {
            $('viewStudentCoursesTotalContainer.${student.id}').setStyle({
              display: 'none'
            });
          }

          coursesTable.addListener("afterFiltering", function (event) {
            var visibleRows = event.tableComponent.getVisibleRowCount();
            var totalRows = event.tableComponent.getRowCount();
            if (visibleRows == totalRows)
              $('viewStudentCoursesTotalValue.${student.id}').innerHTML = totalRows;
            else
              $('viewStudentCoursesTotalValue.${student.id}').innerHTML = visibleRows + " (" + totalRows + ")";
          });

          setupSubjectCreditsTable(${student.id});
          
          // Setup grade tabs
          transferCreditsTable = setupTransferCreditsTable(
              ${student.id}, 
              $('transferCreditsTableContainer.' + '${student.id}'),
              'transferCreditsTable.' + '${student.id}');

          rows.clear();
          <c:forEach var="studentTransferCredit" items="${transferCredits[student.id]}">
            <c:set var="subjectName">
              <c:choose>
                <c:when test="${studentTransferCredit.subject.educationType ne null and not empty studentTransferCredit.subject.code}">
                  <fmt:message key="generic.subjectFormatterWithEducationType">
                    <fmt:param value="${studentTransferCredit.subject.code}"/>
                    <fmt:param value="${studentTransferCredit.subject.name}"/>
                    <fmt:param value="${studentTransferCredit.subject.educationType.name}"/>
                  </fmt:message>
                </c:when>
                <c:when test="${studentTransferCredit.subject.educationType ne null and empty studentTransferCredit.subject.code}">
                  <fmt:message key="generic.subjectFormatterNoSubjectCode">
                    <fmt:param value="${studentTransferCredit.subject.name}"/>
                    <fmt:param value="${studentTransferCredit.subject.educationType.name}"/>
                  </fmt:message>
                </c:when>
                <c:when test="${studentTransferCredit.subject.educationType eq null and not empty studentTransferCredit.subject.code}">
                  <fmt:message key="generic.subjectFormatterNoEducationType">
                    <fmt:param value="${studentTransferCredit.subject.code}"/>
                    <fmt:param value="${studentTransferCredit.subject.name}"/>
                  </fmt:message>
                </c:when>
                <c:otherwise>
                  ${studentTransferCredit.subject.name}
                </c:otherwise>
              </c:choose>
            </c:set>
          
            rows.push([
              '${fn:escapeXml(studentTransferCredit.courseName)}',
              '${fn:escapeXml(subjectName)}',
              '${fn:escapeXml(studentTransferCredit.curriculum.name)}',
              '${studentTransferCredit.date.time}',
              '${studentTransferCredit.courseLength.units}',
              '${fn:escapeXml(studentTransferCredit.courseLength.unit.name)}',
              '${fn:escapeXml(studentTransferCredit.grade.name)}',
              '${fn:escapeXml(studentTransferCredit.grade.gradingScale.name)}',
              '${fn:escapeXml(studentTransferCredit.assessor.fullName)}']);
          </c:forEach>
          transferCreditsTable.addRows(rows);
          if (transferCreditsTable.getRowCount() > 0) {
            $('viewStudentTransferCreditsTotalValue.${student.id}').innerHTML = transferCreditsTable.getRowCount(); 
          }
          else {
            $('viewStudentTransferCreditsTotalContainer.${student.id}').setStyle({
              display: 'none'
            });
          }
          
          transferCreditsTable.addListener("afterFiltering", function (event) {
            var visibleRows = event.tableComponent.getVisibleRowCount();
            var totalRows = event.tableComponent.getRowCount();
            if (visibleRows == totalRows)
              $('viewStudentTransferCreditsTotalValue.${student.id}').innerHTML = totalRows;
            else
              $('viewStudentTransferCreditsTotalValue.${student.id}').innerHTML = visibleRows + " (" + totalRows + ")";
          });
          
          courseAssessmentsTable = setupCourseAssessmentsTable(
              ${student.id}, 
              $('courseAssessmentsTableContainer.' + '${student.id}'), 
              'courseAssessmentsTable.' + '${student.id}',
              true);

          rows.clear();

          var studentAssessments = studentAssessmentsContainer['${student.id}'];
          if (studentAssessments) {
            for (var i = 0, l = studentAssessments.length; i < l; i++) {
              var studentAssessment = studentAssessments[i];
              
              var curriculumsStr = "";
              if (studentAssessment.curriculums && studentAssessment.curriculums.length > 0) {
                curriculumsStr = studentAssessment.curriculums[0].name;
                for (var ci = 1; ci < studentAssessment.curriculums.length; ci++) {
                  curriculumsStr = curriculumsStr + ", " + studentAssessment.curriculums[ci].name;
                }
              }

              var topAssessment = studentAssessment.assessments[0];
              var gradeTooltip = undefined;
              var gradeExtraClass = undefined;

              if (studentAssessment.assessments.length > 1) {
                gradeTooltip = "";
                gradeExtraClass = "viewStudentRaisedGrade";
                
                for (var ai = 0; ai < studentAssessment.assessments.length; ai++) {
                  gradeTooltip = 
                    gradeTooltip + 
                    getLocale().getDate(studentAssessment.assessments[ai].timestamp, false) + 
                    " - " + 
                    studentAssessment.assessments[ai].gradeName + 
                    "\n";

                  if (studentAssessment.assessments[ai].timestamp > topAssessment.timestamp) {
                    topAssessment = studentAssessment.assessments[ai];
                  }
                }
              }
              
              var grade = {
                value: topAssessment.gradeName,
                tooltip: gradeTooltip,
                extraClass: gradeExtraClass
              }

              rows.push([
                studentAssessment.courseName,
                studentAssessment.subjectName,
                curriculumsStr,
                topAssessment.timestamp,
                studentAssessment.courseLength,
                studentAssessment.courseLengthUnitName,
                grade,
                topAssessment.gradingScaleName,
                topAssessment.assessorName,
                studentAssessment.courseStudentId,
                '']);
            }
          }
          
          courseAssessmentsTable.addRows(rows);
          if (courseAssessmentsTable.getRowCount() > 0) {
            $('viewStudentCourseAssessmentsTotalValue.${student.id}').innerHTML = courseAssessmentsTable.getRowCount(); 
          }
          else {
            $('viewStudentCourseAssessmentsTotalContainer.${student.id}').setStyle({
              display: 'none'
            });
          }

          courseAssessmentsTable.addListener("afterFiltering", function (event) {
            var visibleRows = event.tableComponent.getVisibleRowCount();
            var totalRows = event.tableComponent.getRowCount();
            if (visibleRows == totalRows)
              $('viewStudentCourseAssessmentsTotalValue.${student.id}').innerHTML = totalRows;
            else
              $('viewStudentCourseAssessmentsTotalValue.${student.id}').innerHTML = visibleRows + " (" + totalRows + ")";
          });

          // Linked course assessments
          
          var linkedCourseAssessmentsTable = setupCourseAssessmentsTable(
              ${student.id}, 
              $('linkedCourseAssessmentsTableContainer.' + '${student.id}'), 
              'linkedCourseAssessmentsTable.' + '${student.id}',
              false);

          var linkedCourseAssessments = linkedCourseAssessmentsContainer['${student.id}'];

          if (linkedCourseAssessments) {
            rows.clear();
            for (var i = 0, l = linkedCourseAssessments.length; i < l; i++) {
              var cAs = linkedCourseAssessments[i];

              var curriculums = "";
              if (cAs.curriculums && (cAs.curriculums.length > 0)) {
                curriculums = cAs.curriculums[0].curriculumName;
                for (var currInd = 1, currLen = cAs.curriculums.length; currInd < currLen; currInd++) {
                  curriculums = curriculums + ", " + cAs.curriculums[currInd].curriculumName;
                }
              }
              
              rows.push([
                  cAs.courseName,
                  cAs.subjectName,
                  curriculums,
                  cAs.creditDate,
                  cAs.courseLength,
                  cAs.courseLengthUnitName,
                  cAs.gradeName,
                  cAs.gradingScaleName,
                  cAs.assessingUserName,
                  cAs.courseStudentId,
                  '']);
            }
            linkedCourseAssessmentsTable.addRows(rows);
          }

          if (linkedCourseAssessmentsTable.getRowCount() > 0) {
            $('viewStudentLinkedCourseAssessmentsTotalValue.${student.id}').innerHTML = linkedCourseAssessmentsTable.getRowCount(); 
          }
          else {
            $('viewStudentLinkedCourseAssessmentsTotalContainer.${student.id}').setStyle({
              display: 'none'
            });
          }

          linkedCourseAssessmentsTable.addListener("afterFiltering", function (event) {
            var visibleRows = event.tableComponent.getVisibleRowCount();
            var totalRows = event.tableComponent.getRowCount();
            if (visibleRows == totalRows)
              $('viewStudentLinkedCourseAssessmentsTotalValue.${student.id}').innerHTML = totalRows;
            else
              $('viewStudentLinkedCourseAssessmentsTotalValue.${student.id}').innerHTML = visibleRows + " (" + totalRows + ")";
          });
          
          // Linked transfer credits
          
          var linkedTransferCreditsTable = setupTransferCreditsTable(
              ${student.id}, 
              $('linkedTransferCreditsTableContainer.' + '${student.id}'),
              'linkedTransferCreditsTable.' + '${student.id}');

          var linkedTransferCredits = linkedTransferCreditsContainer['${student.id}'];

          if (linkedTransferCredits) {
            rows.clear();
            for (var i = 0, l = linkedTransferCredits.length; i < l; i++) {
              var tc = linkedTransferCredits[i];
              rows.push([
                  tc.courseName,
                  tc.subjectName,
                  tc.curriculumName,
                  tc.creditDate,
                  tc.courseLength,
                  tc.courseLengthUnitName,
                  tc.gradeName,
                  tc.gradingScaleName,
                  tc.assessingUserName
              ]);
            }
            linkedTransferCreditsTable.addRows(rows);
          }
          
          if (linkedTransferCreditsTable.getRowCount() > 0) {
            $('viewStudentLinkedTransferCreditsTotalValue.${student.id}').innerHTML = linkedTransferCreditsTable.getRowCount(); 
          }
          else {
            $('viewStudentLinkedTransferCreditsTotalContainer.${student.id}').setStyle({
              display: 'none'
            });
          }
          
          linkedTransferCreditsTable.addListener("afterFiltering", function (event) {
            var visibleRows = event.tableComponent.getVisibleRowCount();
            var totalRows = event.tableComponent.getRowCount();
            if (visibleRows == totalRows)
              $('viewStudentLinkedTransferCreditsTotalValue.${student.id}').innerHTML = totalRows;
            else
              $('viewStudentLinkedTransferCreditsTotalValue.${student.id}').innerHTML = visibleRows + " (" + totalRows + ")";
          });

          // Projects 
          
          var projectTable;
          <c:forEach var="sp" items="${studentProjects[student.id]}">
            rows.clear();
            projectTable = setupStudentProjectModuleTable(${student.id}, ${sp.studentProject.id});
          
            <c:forEach var="spm" items="${studentProjectModules[sp.studentProject.id]}">
              <c:set var="moduleName" value="${spm.studentProjectModule.module.name}"/>
              <c:set var="moduleOptionality" value="${spm.studentProjectModule.optionality}"/>
              <c:set var="moduleCourseGrades" value=""/>
              <c:set var="moduleTransferCreditGrades" value=""/>
  
              <c:forEach var="cs" items="${spm.courseStudents}">
                <c:set var="grade" value="${courseAssessmentsByCourseStudent[cs.id].grade.name}"/>
                
                <c:if test="${not empty grade}">
                  <c:if test="${not empty moduleCourseGrades}">
                    <c:set var="moduleCourseGrades" value="${moduleCourseGrades}, "/>
                  </c:if>
                  <c:set var="moduleCourseGrades" value="${moduleCourseGrades}${grade}"/>
                </c:if>
              </c:forEach>
                  
              <c:forEach var="tc" items="${spm.transferCredits}">
                <c:set var="grade" value="${tc.grade.name}"/>
                
                <c:if test="${not empty grade}">
                  <c:if test="${not empty moduleTransferCreditGrades}">
                    <c:set var="moduleTransferCreditGrades" value="${moduleTransferCreditGrades}, "/>
                  </c:if>
                  <c:set var="moduleTransferCreditGrades" value="${moduleTransferCreditGrades}${grade}"/>
                </c:if>
              </c:forEach>
              
              rows.push([
                  '${moduleName}', 
                  '${moduleOptionality}', 
                  '${moduleCourseGrades}', 
                  '${moduleTransferCreditGrades}']
              );
            </c:forEach>
            projectTable.addRows(rows);
            
            rows.clear();
            projectTable = setupStudentProjectSubjectCourseTable(${student.id}, ${sp.studentProject.id});
          
            <c:forEach var="spm" items="${studentProjectSubjectCourses[sp.studentProject.id]}">
              <c:set var="subject" value="${spm.studentProjectModule.subject}"/>
              <c:set var="courseNumber" value="${spm.studentProjectModule.courseNumber}"/>
              <c:set var="subjectText">
                <c:choose>
                  <c:when test="${subject.educationType ne null and not empty subject.code}">
                    <fmt:message key="generic.subjectFormatterWithEducationType">
                      <fmt:param value="${subject.code}"/>
                      <fmt:param value="${subject.name}"/>
                      <fmt:param value="${subject.educationType.name}"/>
                    </fmt:message>
                  </c:when>
                  <c:when test="${subject.educationType ne null and empty subject.code}">
                    <fmt:message key="generic.subjectFormatterNoSubjectCode">
                      <fmt:param value="${subject.name}"/>
                      <fmt:param value="${subject.educationType.name}"/>
                    </fmt:message>
                  </c:when>
                  <c:when test="${subject.educationType eq null and not empty subject.code}">
                    <fmt:message key="generic.subjectFormatterNoEducationType">
                      <fmt:param value="${subject.code}"/>
                      <fmt:param value="${subject.name}"/>
                    </fmt:message>
                  </c:when>
                  <c:otherwise>
                    ${studentTransferCredit.subject.name}
                  </c:otherwise>
                </c:choose>
              </c:set>
              <c:set var="moduleOptionality" value="${spm.studentProjectModule.optionality}"/>
              <c:set var="moduleCourseGrades" value=""/>
              <c:set var="moduleTransferCreditGrades" value=""/>
  
              <c:forEach var="cs" items="${spm.courseStudents}">
                <c:set var="grade" value="${courseAssessmentsByCourseStudent[cs.id].grade.name}"/>
                
                <c:if test="${not empty grade}">
                  <c:if test="${not empty moduleCourseGrades}">
                    <c:set var="moduleCourseGrades" value="${moduleCourseGrades}, "/>
                  </c:if>
                  <c:set var="moduleCourseGrades" value="${moduleCourseGrades}${grade}"/>
                </c:if>
              </c:forEach>
                  
              <c:forEach var="tc" items="${spm.transferCredits}">
                <c:set var="grade" value="${tc.grade.name}"/>
                
                <c:if test="${not empty grade}">
                  <c:if test="${not empty moduleTransferCreditGrades}">
                    <c:set var="moduleTransferCreditGrades" value="${moduleTransferCreditGrades}, "/>
                  </c:if>
                  <c:set var="moduleTransferCreditGrades" value="${moduleTransferCreditGrades}${grade}"/>
                </c:if>
              </c:forEach>
              
              rows.push([
                  '${subjectText}', 
                  '${courseNumber}', 
                  '${moduleOptionality}', 
                  '${moduleCourseGrades}', 
                  '${moduleTransferCreditGrades}']
              );
            </c:forEach>
            projectTable.addRows(rows);
          </c:forEach>

          // Setup course tabs
          filesTable = setupFilesTable(${student.id});
          
          var files = studentFilesContainer['${student.id}'];
          if (files) {
            rows.clear();
            for (var i = 0, l = files.length; i < l; i++) {
              var file = files[i];
              rows.push([
                  file.name,
                  file.fileTypeName,
                  file.lastModified,
                  file.id,
                  '',
                  '',
                  ''
              ]);
            }
            filesTable.addRows(rows);
          }

          var variables = studentVariablesContainer['${student.id}'];
          
          if (variables && variables.length > 0) {
            // Student variables
            variablesTable = initStudentVariableTable(${student.id});

            for (var i = 0, l = variables.length; i < l; i++) {
              var rowNumber = variablesTable.addRow([
                variables[i].name,
                variables[i].value
              ]);

              switch (variables[i].type) {
                case 'NUMBER':
                  variablesTable.setCellDataType(rowNumber, 1, 'text');
                break;
                case 'DATE':
                  variablesTable.setCellDataType(rowNumber, 1, 'date');
                break;
                case 'BOOLEAN':
                  variablesTable.setCellDataType(rowNumber, 1, 'checkbox');
                break;
                default:
                  variablesTable.setCellDataType(rowNumber, 1, 'text');
                break;
              }
            }
          }
          
          if (personVariables && personVariables.length > 0) {
            initPersonVariablesTable(${student.id}, personVariables);
          }
        </c:forEach>
        
        
        var tabControl2 = new IxProtoTabs($('studentTabs'));

        <c:forEach var="student" items="${students}">
          var tabControl = new IxProtoTabs($('tabs.${student.id}'));
        </c:forEach>

        <c:if test="${!empty param.activeTab}">
          tabControl.setActiveTab("${param.activeTab}");  
        </c:if>
        
        $$('.viewStudentProjectHeader').each(function (node) {
          Event.observe(node, 'click', onStudentProjectHeaderClick);
        });
        
        $$('.viewStudentProjectHeaderEditButton').each(function (node) {
          Event.observe(node, 'click', onStudentProjectEditClick);
        });

        <c:if test="${!empty staffMember}">
          var staffMemberTabRelatedActionsHoverMenu = new IxHoverMenu($('staffMemberTabRelatedActionsHoverMenuContainer'), {
            text: '<fmt:message key="students.viewStudent.staffMemberTabRelatedActionsLabel"/>'
          });
      
          staffMemberTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
            iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            text: '<fmt:message key="students.viewStudent.staffMemberTabRelatedActionsEditUserLabel"/>',
            link: GLOBAL_contextPath + '/users/edituser.page?userId=${staffMember.id}' 
          }));

          var defaultUserId = "${person.defaultUser.id}";

          staffMemberTabRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
            iconURL: GLOBAL_contextPath + '/gfx/star.png',
            iconOpacity: (${staffMember.id} == defaultUserId) ? 1.0 : 0.4, 
            text: '<fmt:message key="students.viewStudent.staffMemberTabRelatedActionsSetAsDefaultUserLabel"/>',
            onclick: function (event) {
              JSONRequest.request("users/setdefaultuser.json", {
                parameters: {
                  personId: ${person.id},
                  userId: ${staffMember.id}
                },
                onSuccess: function (jsonResponse) {
                  window.location.reload(true);
                }
              }); 
            }  
          }));      
          
          <c:if test="${empty students}">
            staffMemberTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
              iconURL: GLOBAL_contextPath + '/gfx/list-add.png',
              text: '<fmt:message key="students.viewStudent.staffMemberTabRelatedActionsAddStudentLabel"/>',
              link: GLOBAL_contextPath + '/students/createstudent.page?personId=${staffMember.person.id}' 
            }));
          </c:if>
        </c:if>
      }

      function onStudentProjectHeaderClick(event) {
        Event.stop(event);
        var element = Event.element(event);
        
        var projectElement = element.up(".viewStudentProject");
        var projectTableElement = projectElement.down(".viewStudentStudentProjectTableContainer");
        
        if (projectTableElement.visible())
          projectTableElement.hide();
        else
          projectTableElement.show();
      }
      
      function onStudentProjectEditClick(event) {
        Event.stop(event);
        var element = Event.element(event);
        
        if (!element.hasClassName("viewStudentProjectHeaderEditButton"))
          element = element.up(".viewStudentProjectHeaderEditButton");
        
        var studentProjectId = element.down(".viewStudentProjectHeaderEditButtonProjectId").value;
        
        redirectTo(GLOBAL_contextPath + "/projects/editstudentproject.page?studentproject=" + studentProjectId);        
      }
      
      function openEditStudentKoskiDialog(personId) {
        var dialog = new IxDialog({
          id : 'editStudentKoskiDialog',
          contentURL : GLOBAL_contextPath + '/students/editstudentkoskidialog.page?personId=' + personId,
          centered : true,
          showCancel : true,
          title : '<fmt:message key="students.editStudentKoskiDialog.dialogTitle"/>',
          cancelLabel : '<fmt:message key="students.editStudentKoskiDialog.closeLabel"/>' 
        });
        
        dialog.setSize("640px", "450px");
        dialog.addDialogListener(function(event) {
          switch (event.name) {
            case 'cancelClick':
            break;
          }
        });
        
        dialog.open();
      }
      
      function openChangeStudyProgrammeDialog(studentId) {
        var dialog = new IxDialog({
          id : 'changeStudyProgrammeDialog',
          contentURL : GLOBAL_contextPath + '/students/changestudentstudyprogrammedialog.page?studentId=' + studentId,
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="students.changeStudentStudyProgrammeDialog.dialogTitle"/>',
          okLabel : '<fmt:message key="generic.dialog.save"/>', 
          cancelLabel : '<fmt:message key="generic.dialog.cancel"/>' 
        });
        
        dialog.setSize("640px", "200px");
        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'okClick':
              event.preventDefault(true);
              dlg.disableOkButton();

              var contentDoc = dlg.getContentDocument();
              var uploadForm = contentDoc.getElementById("changeStudentStudyProgrammeForm");
              var studyProgrammeId = uploadForm.elements["studyProgrammeId"].value;
              
              JSONRequest.request("students/changestudentstudyprogramme.json", {
                parameters: {
                  studentId: studentId,
                  studyProgrammeId: studyProgrammeId
                },
                onSuccess: function (jsonResponse) {
                  window.location.reload(true);
                }
              }); 
            break;
            case 'cancelClick':
            break;
          }
        });
        
        dialog.open();
      }
      
      function openEditStudentImageDialog(studentId) {
        var dialog = new IxDialog({
          id : 'editStudentImageDialog',
          contentURL : GLOBAL_contextPath + '/students/editstudentimagedialog.page?studentId=' + studentId,
          centered : true,
          showCancel : true,
          title : '<fmt:message key="students.editStudentImageDialog.dialogTitle"/>',
          cancelLabel : '<fmt:message key="students.editStudentImageDialog.closeLabel"/>' 
        });
        
        dialog.setSize("450px", "400px");
        dialog.addDialogListener(function(event) {
          switch (event.name) {
            case 'cancelClick':
              reloadStudentImage(studentId);
            break;
          }
        });
        
        dialog.open();
      }

      function reloadStudentImage(studentId) {
        var imageContainer = $('studentImage.' + studentId);
        
        var imageUrl = 'viewstudentimage.binary?studentId=' + studentId + '&time=' + new Date().getTime();
        
        imageContainer.setStyle({
          backgroundImage: 'url("' + imageUrl + '")'
        });
      }
      
      function openAddNewFileDialog(studentId) {
        var dialog = new IxDialog({
          id : 'newFileDialog',
          contentURL : GLOBAL_contextPath + '/studentfiles/uploadfile.page?studentId=' + studentId,
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="studentFiles.uploadFileDialog.dialogTitle"/>',
          okLabel: '<fmt:message key="studentFiles.uploadFileDialog.uploadButton"/>',
          cancelLabel: '<fmt:message key="studentFiles.uploadFileDialog.closeLabel"/>' 
        });
        
        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'onLoad':
              var contentDoc = dlg.getContentDocument();
              var uploadForm = contentDoc.getElementById("uploadStudentFileForm");
              
              var listener = function (event) {
                var contentDoc = dlg.getContentDocument();
                var uploadForm = contentDoc.getElementById("uploadStudentFileForm");

                if ((uploadForm.fileName.hasClassName("valid")) && (uploadForm.file.hasClassName("valid")))
                  dlg.enableOkButton();
                else
                  dlg.disableOkButton();
              };
              Event.observe(uploadForm.fileName, "change", listener);
              Event.observe(uploadForm.fileName, "keyup", listener);
              Event.observe(uploadForm.file, "change", listener);
            break;
            case 'okClick':
              event.preventDefault(true);
              
              var contentDoc = dlg.getContentDocument();
              var uploadForm = contentDoc.getElementById("uploadStudentFileForm");
              var uploadFrame = contentDoc.getElementById("_uploadFrame");

              Event.observe(uploadFrame, "load", function (event) {
                dlg.close();
                updateFilesTable(studentId);
              });
              
              dlg.disableOkButton();
              dlg.disableCancelButton();
              uploadForm.submit();
            break;
          }
        });
        
        dialog.disableOkButton();
        dialog.setSize("350px", "300px");
        dialog.open();
      }

      function openEditFileDialog(fileId, studentId) {
        var dialog = new IxDialog({
          id : 'editFileDialog',
          contentURL : GLOBAL_contextPath + '/studentfiles/editfile.page?fileId=' + fileId,
          centered : true,
          showOk : true,
          showCancel : true,
          title : '<fmt:message key="studentFiles.uploadFileDialog.dialogTitle"/>',
          okLabel: '<fmt:message key="studentFiles.uploadFileDialog.updateButton"/>',
          cancelLabel : '<fmt:message key="studentFiles.uploadFileDialog.closeLabel"/>' 
        });

        dialog.addDialogListener(function(event) {
          var dlg = event.dialog;
          switch (event.name) {
            case 'onLoad':
              var contentDoc = dlg.getContentDocument();
              var uploadForm = contentDoc.getElementById("uploadStudentFileForm");
              
              var listener = function (event) {
                var field = Event.element(event);
                if (field.hasClassName("invalid"))
                  dlg.disableOkButton();
                else
                  dlg.enableOkButton();
              };
              Event.observe(uploadForm.fileName, "change", listener);
              Event.observe(uploadForm.fileName, "keyup", listener);
            break;
            case 'okClick':
              event.preventDefault(true);
              
              var contentDoc = dlg.getContentDocument();
              var uploadForm = contentDoc.getElementById("uploadStudentFileForm");
              var uploadFrame = contentDoc.getElementById("_uploadFrame");

              Event.observe(uploadFrame, "load", function (event) {
                dlg.close();
                updateFilesTable(studentId);
              });
              
              dlg.disableOkButton();
              dlg.disableCancelButton();
              uploadForm.submit();
            break;
          }
        });
        
        dialog.setSize("350px", "300px");
        dialog.open();
      }

      function updateFilesTable(studentId) {
        
        JSONRequest.request("studentfiles/liststudentfiles.json", {
          parameters: {
            studentId: studentId
          },
          onSuccess: function (jsonResponse) {
            var filesTable = getIxTableById('filesTable.' + studentId);
            filesTable.detachFromDom();
            filesTable.deleteAllRows();
            var files = jsonResponse.files;

            var rows = new Array();
            
            for (var i = 0, l = files.length; i < l; i++) {
              var file = files[i];
              rows.push([
                  file.name,
                  file.fileTypeName,
                  file.lastModified,
                  file.id,
                  '',
                  '',
                  ''
              ]);
            }
            filesTable.addRows(rows);
            filesTable.sort();
            
            filesTable.reattachToDom();
          }
        });
      }
      
    </script>
    
    <ix:extensionHook name="students.viewStudent.head" />
    
</head>

<body onload="onLoad(event);">
  <jsp:include page="/templates/generic/header.jsp"></jsp:include>

  <h1 class="genericPageHeader">
    <fmt:message key="students.viewStudent.pageTitle">
      <fmt:param value="${person.latestStudent.fullName}" />
    </fmt:message>
    
    <span id="koski-status" class="koski-status">KOSKI</span>
  </h1>
  
  <div id="koski-status-details" style="display: none;">
  </div>

  <div id="extensionHoverMenuLinks" style="display: none;">
    <ix:extensionHook name="students.viewStudent.hoverMenuLinks" />
  </div>

  <div id="viewStudentViewContainer">
    <div class="genericFormContainer">
      <div class="tabLabelsContainer containsNestedTabs"
        id="studentTabs">
        <c:forEach var="student" items="${students}">
          <a class="tabLabel" href="#student.${student.id}"> <c:choose>
              <c:when test="${student.studyProgramme == null}">
                <fmt:message
                  key="students.viewStudent.noStudyProgrammeTabLabel" />
              </c:when>
              <c:otherwise>
                  ${student.studyProgramme.name}
                </c:otherwise>
            </c:choose> <c:if test="${student.hasFinishedStudies}">*</c:if>
          </a>
        </c:forEach>
        
        <c:if test="${!empty staffMember}">
          <a class="tabLabel tabLabelUserId" href="#staffMember"><fmt:message key="students.viewStudent.staffMemberTab" /></a>
        </c:if>
      </div>

      <c:choose>
        <c:when test="${person.secureInfo}">
          <c:set var="secureInfoTitle">
            <fmt:message key="students.viewStudent.secureInfoTooltip" />
          </c:set>
          <c:set var="secureInfoClass" value="studentSecureInfo" />
        </c:when>
        <c:otherwise>
          <c:set var="secureInfoTitle" value="" />
          <c:set var="secureInfoClass" value="" />
        </c:otherwise>
      </c:choose>

      <c:forEach var="student" items="${students}">
        <div id="student.${student.id}"
          class="tabContent tabContentNestedTabs">

          <div id="viewStudentViewContainer">
            <div class="genericFormContainer">
              <div class="tabLabelsContainer" id="tabs.${student.id}">
                <a class="tabLabel" href="#basic.${student.id}"> <fmt:message
                    key="students.viewStudent.basicTabLabel" />
                </a> <a class="tabLabel" href="#courses.${student.id}">
                  <fmt:message
                    key="students.viewStudent.coursesTabLabel" />
                </a> <a class="tabLabel" href="#grades.${student.id}"> <fmt:message
                    key="students.viewStudent.gradesTabLabel" />
                </a> <a class="tabLabel" href="#subgrades.${student.id}"> <fmt:message
                    key="students.viewStudent.subjectGradesTabLabel" />
                </a> <a class="tabLabel" href="#contactlog.${student.id}">
                  <fmt:message
                    key="students.viewStudent.contactLogTabLabel" />
                </a> <a class="tabLabel"
                  href="#studentProject.${student.id}"> <fmt:message
                    key="students.viewStudent.studentProjectTabLabel" />
                </a> <a class="tabLabel" href="#studentFiles.${student.id}">
                  <fmt:message
                    key="students.viewStudent.studentFilesTabLabel" />
                </a>
                <ix:extensionHook name="students.viewStudent.tabLabels" studentId="${student.id}"/>
              </div>

              <div id="basic.${student.id}" class="tabContent">
                <div
                  id="basicTabRelatedActionsHoverMenuContainer.${student.id}"
                  class="tabRelatedActionsContainer"></div>

                <!--  Student Basic Info Starts -->
                <div class="genericViewInfoWapper ${secureInfoClass}"
                  id="studentViewBasicInfoWrapper">

                  <div class="genericFormSection"
                    title="${secureInfoTitle}">
                    <jsp:include
                      page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale"
                        value="students.viewStudent.firstNameTitle" />
                      <jsp:param name="helpLocale"
                        value="students.viewStudent.firstNameHelp" />
                    </jsp:include>
                    <div class="genericViewFormDataText">${student.firstName}</div>
                  </div>

                  <c:choose>
                    <c:when test="${studentHasImage[student.id]}">
                      <span id="studentImage.${student.id}"
                        style="background-image: url('viewstudentimage.binary?studentId=${student.id}');"
                        class="viewStudent_studentImage"> </span>
                    </c:when>
                    <c:otherwise>
                      <span id="studentImage.${student.id}"
                        class="viewStudent_studentImage"> </span>
                    </c:otherwise>
                  </c:choose>

                  <div class="genericFormSection"
                    title="${secureInfoTitle}">
                    <jsp:include
                      page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale"
                        value="students.viewStudent.lastNameTitle" />
                      <jsp:param name="helpLocale"
                        value="students.viewStudent.lastNameHelp" />
                    </jsp:include>
                    <div class="genericViewFormDataText">${student.lastName}</div>
                  </div>

                  <c:choose>
                    <c:when test="${!empty person.birthday}">
                      <div class="genericFormSection"
                        title="${secureInfoTitle}">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.birthdayTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.birthdayHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">
                          <fmt:formatDate
                            value="${person.birthday}" />
                        </div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when
                      test="${!empty person.socialSecurityNumber}">
                      <div class="genericFormSection"
                        title="${secureInfoTitle}">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.ssecIdTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.ssecIdHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${person.socialSecurityNumber}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <div class="genericFormSection"
                    title="${secureInfoTitle}">
                    <jsp:include
                      page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale"
                        value="students.viewStudent.genderTitle" />
                      <jsp:param name="helpLocale"
                        value="students.viewStudent.genderHelp" />
                    </jsp:include>
                    <div class="genericViewFormDataText">
                      <c:choose>
                        <c:when test="${person.sex == 'MALE'}">
                          <fmt:message key="generic.genders.male" />
                        </c:when>
                        <c:when test="${person.sex == 'FEMALE'}">
                          <fmt:message key="generic.genders.female" />
                        </c:when>
                        <c:when test="${person.sex == 'OTHER'}">
                          <fmt:message key="generic.genders.other" />
                        </c:when>
                      </c:choose>
                    </div>
                  </div>

                  <c:choose>
                    <c:when test="${!empty student.nickname}">
                      <div class="genericFormSection"
                        title="${secureInfoTitle}">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.nicknameTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.nicknameHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.nickname}</div>
                      </div>
                    </c:when>
                  </c:choose>

                </div>
                <!--  Student Basic Info Ends -->

                <!--  Student Contact Info Starts -->
                <div class="genericViewInfoWapper"
                  id="studentViewContactInfoWrapper">

                  <c:if test="${!empty student.contactInfo.addresses}">
                    <div class="genericFormSection">
                      <c:forEach var="address"
                        items="${student.contactInfo.addresses}">
                        <div class="genericFormTitle">
                          <div class="genericFormTitleText">
                            <div>${address.contactType.name}</div>
                          </div>
                        </div>
                        <div class="genericViewFormDataText">
                          <div>${address.name}</div>
                          <div>${address.streetAddress}</div>
                          <div>${address.postalCode}
                            ${address.city}</div>
                          <div>${address.country}</div>
                        </div>
                      </c:forEach>
                    </div>
                  </c:if>

                  <div class="genericFormSection">
                    <jsp:include
                      page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale"
                        value="students.viewStudent.emailTitle" />
                      <jsp:param name="helpLocale"
                        value="students.viewStudent.emailHelp" />
                    </jsp:include>
                    <div class="genericViewFormDataText">
                      <c:forEach var="email"
                        items="${student.contactInfo.emails}">
                        <c:choose>
                          <c:when test="${not empty email.contactType}">
                            <div>
                              <a href="mailto:${email.address}">${email.address}</a>
                              (${fn:toLowerCase(email.contactType.name)})
                            </div>
                          </c:when>
                          <c:otherwise>
                            <div>
                              <a href="mailto:${email.address}">${email.address}</a>
                            </div>
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>
                    </div>
                  </div>

                  <c:choose>
                    <c:when
                      test="${!empty student.contactInfo.phoneNumbers}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.phoneNumberTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.phoneNumberHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">
                          <c:forEach var="phone"
                            items="${student.contactInfo.phoneNumbers}">
                            <c:choose>
                              <c:when
                                test="${not empty phone.contactType}">
                                <div>${phone.number}
                                  (${fn:toLowerCase(phone.contactType.name)})</div>
                              </c:when>
                              <c:otherwise>
                                <div>${phone.number}</div>
                              </c:otherwise>
                            </c:choose>
                          </c:forEach>
                        </div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.municipality}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.municipalityTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.municipalityHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.municipality.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                </div>
                <!--  Student Contact Info Ends -->

                <div class="columnClear"></div>

                <!--  Student Education Info Starts -->
                <div class="genericViewInfoWapper"
                  id="studentViewEducationInfoWrapper">

                  <c:choose>
                    <c:when test="${!empty student.examinationType}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.examinationTypeTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.examinationTypeHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.examinationType.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.educationalLevel}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.educationalLevelTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.educationalLevelHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.educationalLevel.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test=">${!empty student.education}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.educationTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.educationHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.education}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.studyProgramme}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.studyProgrammeTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.studyProgrammeHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.studyProgramme.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.curriculum}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.curriculumTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.curriculumHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.curriculum.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.funding}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.studentFunding.ui.title" />
                          <jsp:param name="helpLocale"
                            value="students.studentFunding.ui.help" />
                        </jsp:include>
                        <c:choose>
                          <c:when test="${student.funding eq 'GOVERNMENT_FUNDING'}">
                            <fmt:message key="students.studentFunding.governmentFunding" />
                          </c:when>
                          <c:when test="${student.funding eq 'OTHER_FUNDING'}">
                            <fmt:message key="students.studentFunding.otherFunding" />
                          </c:when>
                          <c:otherwise>
                            <fmt:message key="students.studentFunding.defaultFunding" />
                          </c:otherwise>
                        </c:choose>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when
                      test="${fn:length(studentGroups[student.id]) gt 0}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.studentGroupTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.studentGroupHelp" />
                        </jsp:include>

                        <div class="genericViewFormDataText">
                          <c:forEach var="studentGroup"
                            items="${studentGroups[student.id]}"
                            varStatus="sgStat">
                                ${studentGroup.name}<c:if
                              test="${!sgStat.last}">, </c:if>
                          </c:forEach>
                        </div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.school}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.schoolTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.schoolHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.school.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.previousStudies}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.previousStudiesTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.previousStudiesHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.previousStudies}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.studyTimeEnd}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.studyTimeEndTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.studyTimeEndHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">
                          <fmt:formatDate
                            value="${student.studyTimeEnd}" />
                        </div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.studyStartDate}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.studyStartDateTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.studyStartDateHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">
                          <fmt:formatDate
                            value="${student.studyStartDate}" />
                        </div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty studentStudyPeriods[student.id]}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.studyPeriodsTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.studyPeriodsHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">
                          <c:forEach var="period" items="${studentStudyPeriods[student.id]}">
                            <div>
                              <fmt:message key="generic.studentStudyPeriods.${period.periodType}"/>
                              <fmt:formatDate value="${period.begin}"/> - <fmt:formatDate value="${period.end}"/>
                            </div>
                          </c:forEach>
                        </div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.studyEndDate}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.studyEndDateTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.studyEndDateHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">
                          <fmt:formatDate
                            value="${student.studyEndDate}" />
                        </div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.studyEndReason}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.studyEndReasonTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.studyEndReasonHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.studyEndReason.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.studyEndText}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.studyEndTextTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.studyEndTextHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.studyEndText}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty studentMatriculationEnrollments[student.id]}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.matriculationExamEnrollmentsTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.matriculationExamEnrollmentsHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">
                          <c:forEach var="enrollment" items="${studentMatriculationEnrollments[student.id]}">
                            <c:choose>
                              <c:when test="${enrollment.exam.examTerm == 'SPRING'}">
                                <c:set var="matriculationExamEnrollmentTerm">
                                  <fmt:message key="terms.seasons.spring"/>
                                </c:set>
                              </c:when>
                              <c:when test="${enrollment.exam.examTerm == 'AUTUMN'}">
                                <c:set var="matriculationExamEnrollmentTerm">
                                  <fmt:message key="terms.seasons.autumn"/>
                                </c:set>
                              </c:when>
                              <c:otherwise>
                                <c:set var="matriculationExamEnrollmentTerm">???</c:set>
                              </c:otherwise>
                            </c:choose>
                          
                            <c:choose>
                              <c:when test="${enrollment.state == 'PENDING'}">
                                <c:set var="matriculationExamEnrollmentState">
                                  <fmt:message key="terms.pending"/>
                                </c:set>
                              </c:when>
                              <c:when test="${enrollment.state == 'APPROVED'}">
                                <c:set var="matriculationExamEnrollmentState">
                                  <fmt:message key="terms.approved"/>
                                </c:set>
                              </c:when>
                              <c:when test="${enrollment.state == 'REJECTED'}">
                                <c:set var="matriculationExamEnrollmentState">
                                  <fmt:message key="terms.rejected"/>
                                </c:set>
                              </c:when>
                              <c:otherwise>
                                <c:set var="matriculationExamEnrollmentState"></c:set>
                              </c:otherwise>
                            </c:choose>
                          
                            <div>
                              <a href="${pageContext.request.contextPath}/matriculation/edit.page?enrollment=${enrollment.id}">${matriculationExamEnrollmentTerm} ${enrollment.exam.examYear} (<fmt:formatDate value="${enrollment.enrollmentDate}"/>)</a> [${matriculationExamEnrollmentState}]
                            </div>
                          </c:forEach>
                        </div>
                      </div>
                    </c:when>
                  </c:choose>
                </div>

                <!--  Student Education Info Ends -->

                <!--  Student Additional Info Starts -->
                <div class="genericViewInfoWapper"
                  id="studentViewAdditionalInfoWrapper">

                  <c:choose>
                    <c:when test="${not empty student.tags}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.tagsTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.tagsHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">
                          <c:forEach var="tag" items="${student.tags}"
                            varStatus="vs">
                            <c:out value="${tag.text}" />
                            <c:if test="${not vs.last}">
                              <c:out value=" " />
                            </c:if>
                          </c:forEach>
                        </div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.language}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.languageTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.languageHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.language.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.nationality}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.nationalityTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.nationalityHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.nationality.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${!empty student.activityType}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.activityTypeTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.activityTypeHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.activityType.name}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when test="${hasPersonVariables}">
                      <div class="genericFormSection">
                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale" value="students.viewStudent.personVariablesTitle" />
                          <jsp:param name="helpLocale" value="students.viewStudent.personVariablesHelp" />
                        </jsp:include>
                        <div id="personVariablesTableContainer.${student.id}"></div>
                      </div>
                    </c:when>
                  </c:choose>
                  
                  <div class="genericFormSection">
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="students.viewStudent.studentVariablesTitle" />
                      <jsp:param name="helpLocale" value="students.viewStudent.studentVariablesHelp" />
                    </jsp:include>
                    <div id="variablesTableContainer.${student.id}"></div>
                  </div>

                  <c:choose>
                    <c:when test="${!empty student.additionalInfo}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.additionalInformationTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.additionalInformationHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${student.additionalInfo}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <c:choose>
                    <c:when
                      test="${!empty student.contactInfo.additionalInfo}">
                      <div class="genericFormSection">
                        <jsp:include
                          page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale"
                            value="students.viewStudent.additionalContactInfoTitle" />
                          <jsp:param name="helpLocale"
                            value="students.viewStudent.additionalContactInfoHelp" />
                        </jsp:include>
                        <div class="genericViewFormDataText">${fn:replace(student.contactInfo.additionalInfo,
                          newLineChar, "<br/>")}</div>
                      </div>
                    </c:when>
                  </c:choose>

                  <div class="genericFormSection">
                    <jsp:include
                      page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale"
                        value="students.viewStudent.lodgingTitle" />
                      <jsp:param name="helpLocale"
                        value="students.viewStudent.lodgingHelp" />
                    </jsp:include>
                    <div class="genericViewFormDataText">
	                    <c:forEach var="period" items="${studentLodgingPeriods[student.id]}">
	                      <div><fmt:formatDate value="${period.begin}"/> - <fmt:formatDate value="${period.end}"/></div>
	                    </c:forEach>
                    </div>
                  </div>

                </div>
                <!--  Student Additional Info Ends -->

                <div class="columnClear"></div>

              </div>

              <div id="courses.${student.id}" class="tabContent">
                <div
                  id="coursesTabRelatedActionsHoverMenuContainer.${student.id}"
                  class="tabRelatedActionsContainer"></div>
                <div class="genericFormSection">
                  <jsp:include
                    page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale"
                      value="students.viewStudent.coursesTitle" />
                    <jsp:param name="helpLocale"
                      value="students.viewStudent.coursesHelp" />
                  </jsp:include>
                  <div id="viewStudentStudentCoursesTableContainer">
                    <div id="coursesTableContainer.${student.id}"></div>
                  </div>
                  <div
                    id="viewStudentCoursesTotalContainer.${student.id}"
                    class="viewStudentCoursesTotalContainer">
                    <fmt:message key="students.viewStudent.coursesTotal" />
                    <span
                      id="viewStudentCoursesTotalValue.${student.id}"></span>
                  </div>
                </div>
              </div>

              <div id="grades.${student.id}" class="tabContent">
                <div
                  id="gradesTabRelatedActionsHoverMenuContainer.${student.id}"
                  class="tabRelatedActionsContainer"></div>

                <div class="genericFormSection">
                  <jsp:include
                    page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale"
                      value="students.viewStudent.courseAssessmentsTitle" />
                    <jsp:param name="helpLocale"
                      value="students.viewStudent.courseAssessmentsHelp" />
                  </jsp:include>
                  <div id="viewStudentCourseAssessmentsTableContainer">
                    <div
                      id="courseAssessmentsTableContainer.${student.id}"></div>
                  </div>
                  <div
                    id="viewStudentCourseAssessmentsTotalContainer.${student.id}"
                    class="viewStudentCourseAssessmentsTotalContainer">
                    <fmt:message
                      key="students.viewStudent.courseAssessmentsTotal" />
                    <span
                      id="viewStudentCourseAssessmentsTotalValue.${student.id}"></span>
                  </div>
                </div>

                <div class="genericFormSection">
                  <jsp:include
                    page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale"
                      value="students.viewStudent.transferCreditsTitle" />
                    <jsp:param name="helpLocale"
                      value="students.viewStudent.transferCreditsHelp" />
                  </jsp:include>
                  <div id="viewStudentTransferCreditsTableContainer">
                    <div
                      id="transferCreditsTableContainer.${student.id}"></div>
                  </div>
                  <div
                    id="viewStudentTransferCreditsTotalContainer.${student.id}"
                    class="viewStudentTransferCreditsTotalContainer">
                    <fmt:message
                      key="students.viewStudent.transferCreditsTotal" />
                    <span
                      id="viewStudentTransferCreditsTotalValue.${student.id}"></span>
                  </div>
                </div>

                <div class="genericFormSection">
                  <jsp:include
                    page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale"
                      value="students.viewStudent.linkedCourseAssessmentsTitle" />
                    <jsp:param name="helpLocale"
                      value="students.viewStudent.linkedCourseAssessmentsHelp" />
                  </jsp:include>
                  <div
                    id="viewStudentLinkedCourseAssessmentsTableContainer">
                    <div
                      id="linkedCourseAssessmentsTableContainer.${student.id}"></div>
                  </div>
                  <div
                    id="viewStudentLinkedCourseAssessmentsTotalContainer.${student.id}"
                    class="viewStudentLinkedCourseAssessmentsTotalContainer">
                    <fmt:message
                      key="students.viewStudent.linkedCourseAssessmentsTotal" />
                    <span
                      id="viewStudentLinkedCourseAssessmentsTotalValue.${student.id}"></span>
                  </div>
                </div>

                <div class="genericFormSection">
                  <jsp:include
                    page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale"
                      value="students.viewStudent.linkedTransferCreditsTitle" />
                    <jsp:param name="helpLocale"
                      value="students.viewStudent.linkedTransferCreditsHelp" />
                  </jsp:include>
                  <div
                    id="viewStudentLinkedTransferCreditsTableContainer">
                    <div
                      id="linkedTransferCreditsTableContainer.${student.id}"></div>
                  </div>
                  <div
                    id="viewStudentLinkedTransferCreditsTotalContainer.${student.id}"
                    class="viewStudentLinkedTransferCreditsTotalContainer">
                    <fmt:message
                      key="students.viewStudent.linkedTransferCreditsTotal" />
                    <span
                      id="viewStudentLinkedTransferCreditsTotalValue.${student.id}"></span>
                  </div>
                </div>
              </div>

              <div id="subgrades.${student.id}" class="tabContent">
                <div class="genericFormSection">
                  <div id="subjectCreditsTable.${student.id}"></div>
                </div>
              </div>
              
              <div id="contactlog.${student.id}" class="tabContent">
                <div
                  id="contactLogTabRelatedActionsHoverMenuContainer.${student.id}"
                  class="tabRelatedActionsContainer"></div>

                <div id="studentContactEntryList.${student.id}"
                  class="studentContactEntryWrapper">
                  <c:forEach var="contactEntry"
                    items="${contactEntries[student.id]}">
                    <div id="studentContactEntryItem"
                      class="studentContactEntryItem">
                      <div>
                        <span class="studentContactEntryDate"><fmt:formatDate
                            value="${contactEntry.entryDate}" /></span> <span
                          class="studentContactEntryType"> <c:choose>
                            <c:when
                              test="${contactEntry.type eq 'OTHER'}">
                              <fmt:message
                                key="students.viewStudent.contactEntry.types.other" />
                            </c:when>
                            <c:when
                              test="${contactEntry.type eq 'LETTER'}">
                              <fmt:message
                                key="students.viewStudent.contactEntry.types.letter" />
                            </c:when>
                            <c:when
                              test="${contactEntry.type eq 'EMAIL'}">
                              <fmt:message
                                key="students.viewStudent.contactEntry.types.email" />
                            </c:when>
                            <c:when
                              test="${contactEntry.type eq 'PHONE'}">
                              <fmt:message
                                key="students.viewStudent.contactEntry.types.phone" />
                            </c:when>
                            <c:when
                              test="${contactEntry.type eq 'CHATLOG'}">
                              <fmt:message
                                key="students.viewStudent.contactEntry.types.chatlog" />
                            </c:when>
                            <c:when
                              test="${contactEntry.type eq 'SKYPE'}">
                              <fmt:message
                                key="students.viewStudent.contactEntry.types.skype" />
                            </c:when>
                            <c:when
                              test="${contactEntry.type eq 'FACE2FACE'}">
                              <fmt:message
                                key="students.viewStudent.contactEntry.types.face2face" />
                            </c:when>
                            <c:when
                              test="${contactEntry.type eq 'ABSENCE'}">
                              <fmt:message
                                key="students.viewStudent.contactEntry.types.absence" />
                            </c:when>
                            <c:when
                              test="${contactEntry.type eq 'MUIKKU'}">
                              <fmt:message
                                key="students.viewStudent.contactEntry.types.muikku" />
                            </c:when>
                          </c:choose>
                        </span> <span class="studentContactEntryCreator">${contactEntry.creatorName}</span>
                      </div>
                      <div>${contactEntry.text}</div>

                      <div class="contactEntryCommentsWrapper">
                        <c:forEach var="comment"
                          items="${contactEntryComments[contactEntry.id]}">
                          <div class="studentContactCommentEntryItem">
                            <div
                              class="studentContactCommentEntryCaption">
                              <span
                                class="studentContactCommentEntryDate"><fmt:formatDate
                                  value="${comment.commentDate}" /></span> <span
                                class="studentContactCommentEntryCreator">${comment.creatorName}</span>
                            </div>
                            <div>${comment.text}</div>
                          </div>
                        </c:forEach>
                      </div>
                    </div>
                  </c:forEach>
                </div>
              </div>

              <div id="studentProject.${student.id}" class="tabContent">
                <div
                  id="projectsTabRelatedActionsHoverMenuContainer.${student.id}"
                  class="tabRelatedActionsContainer"></div>

                <div class="genericFormSection">
                  <jsp:include
                    page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale"
                      value="students.viewStudent.projectsTitle" />
                    <jsp:param name="helpLocale"
                      value="students.viewStudent.coursesHelp" />
                  </jsp:include>
                </div>

                <c:forEach var="sp"
                  items="${studentProjects[student.id]}">
                  <div class="viewStudentProject">
                    <div class="viewStudentProjectHeader">
                      <div>
                        <span class="viewStudentProjectHeaderName">${sp.studentProject.name}</span>
                        <span
                          class="viewStudentProjectHeaderOptionality">
                          <c:choose>
                            <c:when
                              test="${sp.studentProject.optionality eq 'MANDATORY'}">
                              <fmt:message
                                key="students.viewStudent.projectOptionalityMandatory" />
                            </c:when>
                            <c:when
                              test="${sp.studentProject.optionality eq 'OPTIONAL'}">
                              <fmt:message
                                key="students.viewStudent.projectOptionalityOptional" />
                            </c:when>
                          </c:choose>
                        </span>

                        <c:forEach var="assessment"
                          items="${sp.assessments}">
                          <span
                            class="viewStudentProjectHeaderAssessment">
                            <span
                            class="viewStudentProjectHeaderAssessmentDate"><fmt:formatDate
                                value="${assessment.date}" /></span> <span
                            class="viewStudentProjectHeaderAssessmentGrade">${assessment.grade.name}</span>
                          </span>
                        </c:forEach>

                        <span class="viewStudentProjectHeaderEditButton">
                          <%--                             <img src="../gfx/accessories-text-editor.png" class="iconButton" onclick="openEditStudentProject(${sp.studentProject.id});"/> --%>
                          <img src="../gfx/accessories-text-editor.png"
                          class="iconButton" /> <input type="hidden"
                          name="viewStudentProjectHeaderEditButtonProjectId"
                          class="viewStudentProjectHeaderEditButtonProjectId"
                          value="${sp.studentProject.id}" />
                        </span>
                      </div>
                      <div>
                        <span class="viewStudentProjectHeaderMandatory"><fmt:message
                            key="students.viewStudent.projectHeaderMandatoryCourseCount" />
                          ${sp.passedMandatoryModuleCount}/${sp.mandatoryModuleCount}</span>
                        <span class="viewStudentProjectHeaderOptional"><fmt:message
                            key="students.viewStudent.projectHeaderOptionalCourseCount" />
                          ${sp.passedOptionalModuleCount}/${sp.optionalModuleCount}</span>
                      </div>
                    </div>

                    <div class="viewStudentStudentProjectTableContainer"
                      style="display: none;">
                      <div
                        id="studentProjectModulesTable.${student.id}.${sp.studentProject.id}"></div>
                      <div
                        id="studentProjectSubjectCourseTable.${student.id}.${sp.studentProject.id}"></div>
                    </div>
                  </div>
                </c:forEach>
              </div>

              <div id="studentFiles.${student.id}" class="tabContent">
                <div
                  id="filesTabRelatedActionsHoverMenuContainer.${student.id}"
                  class="tabRelatedActionsContainer"></div>

                <div class="genericFormSection">
                  <jsp:include
                    page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale"
                      value="students.viewStudent.filesTitle" />
                    <jsp:param name="helpLocale"
                      value="students.viewStudent.filesHelp" />
                  </jsp:include>
                  <div id="viewStudentStudentFilesTableContainer">
                    <div id="filesTableContainer.${student.id}"></div>
                  </div>
                </div>
              </div>

              <ix:extensionHook name="students.viewStudent.tabs" studentId="${student.id}"/>
            </div>
          </div>
        </div>
      </c:forEach>
      
      <c:if test="${!empty staffMember}">
        <div id="staffMember" class="tabContent tabContentNestedTabs">
          <div
            id="staffMemberTabRelatedActionsHoverMenuContainer"
            class="tabRelatedActionsContainer"></div>
  
          <div class="genericViewInfoWapper" id="studentViewStaffMemberBasicInfoWrapper">
          
            <div class="genericFormSection">
              <jsp:include
                page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale"
                  value="students.viewStudent.firstNameTitle" />
                <jsp:param name="helpLocale"
                  value="students.viewStudent.firstNameHelp" />
              </jsp:include>
              <div class="genericViewFormDataText">${staffMember.firstName}</div>
            </div>
  
            <div class="genericFormSection">
              <jsp:include
                page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale"
                  value="students.viewStudent.lastNameTitle" />
                <jsp:param name="helpLocale"
                  value="students.viewStudent.lastNameHelp" />
              </jsp:include>
              <div class="genericViewFormDataText">${staffMember.lastName}</div>
            </div>
  
            <c:if test="${!empty staffMember.title}">
              <div class="genericFormSection">
                <jsp:include
                  page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale"
                    value="students.viewStudent.titleTitle" />
                  <jsp:param name="helpLocale"
                    value="students.viewStudent.titleHelp" />
                </jsp:include>
                <div class="genericViewFormDataText">${staffMember.title}</div>
              </div>
            </c:if>
  
            <div class="genericFormSection">
              <jsp:include
                page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale"
                  value="students.viewStudent.roleTitle" />
                <jsp:param name="helpLocale"
                  value="students.viewStudent.roleHelp" />
              </jsp:include>
              <div class="genericViewFormDataText">
                <c:choose>
                  <c:when test="${staffMember.role == 'CLOSED'}">
                    <fmt:message key="students.viewStudent.roleClosedTitle"/>
                  </c:when>
                  <c:when test="${staffMember.role == 'GUEST'}">
                    <fmt:message key="students.viewStudent.roleGuestTitle"/>
                  </c:when>
                  <c:when test="${staffMember.role == 'USER'}">
                    <fmt:message key="students.viewStudent.roleUserTitle"/>
                  </c:when>
                  <c:when test="${staffMember.role == 'MANAGER'}">
                    <fmt:message key="students.viewStudent.roleManagerTitle"/>
                  </c:when>
                  <c:when test="${staffMember.role == 'ADMINISTRATOR'}">
                    <fmt:message key="students.viewStudent.roleAdministratorTitle"/>
                  </c:when>
                  <c:when test="${staffMember.role == 'STUDY_PROGRAMME_LEADER'}">
                    <fmt:message key="students.viewStudent.roleStudyProgrammeLeaderTitle"/>
                  </c:when>
                </c:choose>
              </div>
            </div>
  
            <c:choose>
              <c:when test="${not empty staffMember.tags}">
                <div class="genericFormSection">
                  <jsp:include
                    page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale"
                      value="students.viewStudent.tagsTitle" />
                    <jsp:param name="helpLocale"
                      value="students.viewStudent.tagsHelp" />
                  </jsp:include>
                  <div class="genericViewFormDataText">
                    <c:forEach var="tag" items="${staffMember.tags}"
                      varStatus="vs">
                      <c:out value="${tag.text}" />
                      <c:if test="${not vs.last}">
                        <c:out value=" " />
                      </c:if>
                    </c:forEach>
                  </div>
                </div>
              </c:when>
            </c:choose>
          </div>
  
          <!--  Student Contact Info Starts -->
          <div class="genericViewInfoWapper"
            id="studentViewStaffMemberContactInfoWrapper">
  
            <c:if test="${!empty staffMember.contactInfo.addresses}">
              <div class="genericFormSection">
                <c:forEach var="address"
                  items="${staffMember.contactInfo.addresses}">
                  <div class="genericFormTitle">
                    <div class="genericFormTitleText">
                      <div>${address.contactType.name}</div>
                    </div>
                  </div>
                  <div class="genericViewFormDataText">
                    <div>${address.name}</div>
                    <div>${address.streetAddress}</div>
                    <div>${address.postalCode}
                    ${address.city}</div>
                  <div>${address.country}</div>
                </div>
              </c:forEach>
            </div>
          </c:if>

          <div class="genericFormSection">
            <jsp:include
              page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale"
                value="students.viewStudent.emailTitle" />
              <jsp:param name="helpLocale"
                value="students.viewStudent.emailHelp" />
            </jsp:include>
            <div class="genericViewFormDataText">
              <c:forEach var="email"
                items="${staffMember.contactInfo.emails}">
                <c:choose>
                  <c:when test="${not empty email.contactType}">
                    <div>
                      <a href="mailto:${email.address}">${email.address}</a>
                      (${fn:toLowerCase(email.contactType.name)})
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div>
                      <a href="mailto:${email.address}">${email.address}</a>
                    </div>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </div>
          </div>

          <c:choose>
            <c:when
              test="${!empty staffMember.contactInfo.phoneNumbers}">
              <div class="genericFormSection">
                <jsp:include
                  page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale"
                    value="students.viewStudent.phoneNumberTitle" />
                  <jsp:param name="helpLocale"
                    value="students.viewStudent.phoneNumberHelp" />
                </jsp:include>
                <div class="genericViewFormDataText">
                  <c:forEach var="phone"
                    items="${staffMember.contactInfo.phoneNumbers}">
                    <c:choose>
                      <c:when
                        test="${not empty phone.contactType}">
                        <div>${phone.number}
                          (${fn:toLowerCase(phone.contactType.name)})</div>
                      </c:when>
                      <c:otherwise>
                        <div>${phone.number}</div>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </div>
              </div>
            </c:when>
          </c:choose>
        </div>
      </div>
      </c:if>
    </div>
  </div>

  <ix:extensionHook name="students.viewStudent.footer"/>

  <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
</body>
</html>