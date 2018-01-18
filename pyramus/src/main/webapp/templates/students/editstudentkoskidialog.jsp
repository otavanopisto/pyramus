<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/studentinfopopup_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      /**
       * Called when this dialog loads. Initializes the search navigation and student tables.
       *
       * @param event The page load event
       */
      function onLoad(event) {
        var tabControl2 = new IxProtoTabs($('studentKoskiDialogTabs'));
        var personId = '${person.id}';
        
        initIDsTable(personId);
        initInvalidationTable(personId);

        loadIDsTable(personId);
        loadInvalidationTable(personId);
      }

      function initIDsTable(personId) {
        var table = new IxTable($('studentKoskiIDsTableContainer'), {
          id : "studentKoskiIDsTable",
          columns : [{
            header : '<fmt:message key="students.viewStudent.projectTableOptionalityHeader"/>',
            left : 8,
            width: 100,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            header : '<fmt:message key="students.viewStudent.projectTableOptionalityHeader"/>',
            left : 100 + 8,
            width : 200,
            dataType: 'text',
            editable: true,
            paramName: 'oid'
          }, {
            header : '<fmt:message key="students.viewStudent.projectTableOptionalityHeader"/>',
            left : 100 + 8 + 200 + 8,
            width : 200,
            dataType: 'text',
            editable: true,
            paramName: 'linkedOid'
          }, {
            dataType: 'hidden',
            editable: false,
            paramName: 'studentId'
          }]
        });

        return table;
      }

      function loadIDsTable(personId) {
        var table = getIxTableById('studentKoskiIDsTable');
        
        JSONRequest.request("students/koskipersonvariables.json", {
          parameters: {
            personId: personId
          },
          onSuccess: function (jsonResponse) {
            table.detachFromDom();
            table.deleteAllRows();
            var rows = new Array();
                       
            var studentVariables = jsonResponse.studentVariables;
            if (studentVariables) {
              for (var i = 0, l = studentVariables.length; i < l; i++) {
                var student = studentVariables[i];

                rows.push([
                  student.studyProgrammeName,
                  student.oid,
                  student.linkedOid,
                  student.studentId
                ]);
              }
            }
            
            table.addRows(rows);
            table.reattachToDom();
          }
        });
      }
      
      function initInvalidationTable() {
        var table = new IxTable($('studentKoskiInvalidateTableContainer'), {
          id : "studentKoskiInvalidateTable",
          columns : [{
            header : '<fmt:message key="students.viewStudent.projectTableOptionalityHeader"/>',
            left : 8,
            width: 200,
            dataType : 'text',
            editable: false,
            paramName: 'oid'
          }, {
            header : '<fmt:message key="students.viewStudent.projectTableOptionalityHeader"/>',
            left : 200 + 8,
            width : 100,
            dataType: 'text',
            editable: false,
            paramName: 'studyProgrammeName'
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            paramName: 'archiveButton',
//             hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="courses.editCourse.studentsTableArchiveRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var oid = table.getCellValue(event.row, table.getNamedColumnIndex('oid'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=courses.editCourse.studentArchiveConfirmDialogContent&localeParams=" + encodeURIComponent(oid);

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
                    alert('boom');
//                     JSONRequest.request("courses/archivecoursestudent.json", {
//                       parameters: {
//                         courseStudentId: courseStudentId
//                       },
//                       onSuccess: function (jsonResponse) {
//                         var table = getIxTableById('studentsTable');
//                         table.deleteRow(archivedStudentRowIndex);
//                         if (table.getRowCount() > 0) {
//                           $('noStudentsAddedMessageContainer').setStyle({
//                             display: 'none'
//                           });
//                           $('editCourseStudentsTotalContainer').setStyle({
//                             display: ''
//                           });
//                           $('editCourseStudentsTotalValue').innerHTML = table.getRowCount(); 
//                         }
//                         else {
//                           $('editCourseStudentsTotalContainer').setStyle({
//                             display: 'none'
//                           });
//                           $('noStudentsAddedMessageContainer').setStyle({
//                             display: ''
//                           });
//                         }
//                       }
//                     });   
                  break;
                }
              });
            
              dialog.open();
            } 
          }]
        });

        return table;
      }
      
      function loadInvalidationTable(personId) {
        var table = getIxTableById('studentKoskiInvalidateTable');
        
        JSONRequest.request("students/koskipersonstudies.json", {
          parameters: {
            personId: personId
          },
          onSuccess: function (jsonResponse) {
            table.detachFromDom();
            table.deleteAllRows();
            var rows = new Array();
                       
            var studyPermitIDs = jsonResponse.studyPermitIDs;
            if (studyPermitIDs) {
              for (var i = 0, l = studyPermitIDs.length; i < l; i++) {
                var student = studyPermitIDs[i];

                rows.push([
                  student.oid,
                  student.linkedStudyProgrammeName,
                  ''
                ]);
              }
            }
            
            table.addRows(rows);
            table.reattachToDom();
          }
        });
      }

    </script>

  </head>
  <body onload="onLoad(event);">

    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="studentKoskiDialogTabs">
        <a class="tabLabel" href="#ids"><fmt:message key="students.viewStudent.staffMemberTab" /></a>
        <a class="tabLabel" href="#invalidate"><fmt:message key="students.viewStudent.staffMemberTab" /></a>
      </div>

      <div id="ids" class="tabContent">
        <form action="editkoskipersonvariables.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <input type="hidden" name="personId" value="${person.id}"/>
	        <div class="genericFormSection">
	          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
	            <jsp:param name="titleLocale" value="students.viewStudent.filesTitle" />
	            <jsp:param name="helpLocale" value="students.viewStudent.filesHelp" />
	          </jsp:include>
	          <input type="text" name="personOid" size="32" value="${personOID}">
	        </div>
	        
	        <div class="genericFormSection">
	          <div id="editStudentKoskiIDsTableContainer">
	            <div id="studentKoskiIDsTableContainer"></div>
	          </div>
	        </div>
	        
          <input type="submit" value="<fmt:message key="courses.editCourse.saveButton"/>">
	      </form>
      </div>
      
      <div id="invalidate" class="tabContent">
        <div class="genericFormSection">
          <div id="editStudentKoskiInvalidateTableContainer">
            <div id="studentKoskiInvalidateTableContainer"></div>
          </div>
        </div>
      </div>
    </div>

  </body>
</html>