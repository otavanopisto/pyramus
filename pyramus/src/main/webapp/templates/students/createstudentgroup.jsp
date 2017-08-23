<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="students.createStudentGroup.pageTitle" /></title>
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
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript">

      function addNewStudent(studentsTable, personId, studentId, studentName) {
        JSONRequest.request("students/getstudentstudyprogrammes.json", {
          parameters: {
            personId: personId
          },
          onSuccess: function (jsonResponse) {
            var rowIndex = studentsTable.addRow(['', studentName, studentId, personId, '']);
            var cellEditor = studentsTable.getCellEditor(rowIndex, studentsTable.getNamedColumnIndex('studentId'));
            for (var j = 0, l = jsonResponse.studentStudyProgrammes.length; j < l; j++) {
              IxTableControllers.getController('select').addOption(cellEditor , jsonResponse.studentStudyProgrammes[j].studentId, jsonResponse.studentStudyProgrammes[j].studyProgrammeName);
            }
            $('noStudentsAddedMessageContainer').setStyle({
              display: 'none'
            });
            $('createStudentGroupStudentsTotalContainer').setStyle({
              display: ''
            });
            $('createStudentGroupStudentsTotalValue').innerHTML = studentsTable.getRowCount(); 
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
                var index = getStudentRowIndex(personId);
                if (index == -1) {
                  addNewStudent(studentsTable, personId, studentId, studentName);
                } 
              }
              studentsTable.reattachToDom();
            break;
          }
        });
        dialog.open();
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

      // Users
      
      function setupUsersTable() {
        var usersTable = new IxTable($('usersTable'), {
          id : "usersTable",
          columns : [{
            dataType: 'hidden',
            paramName: 'userId'
          }, {
            header : '<fmt:message key="students.createStudentGroup.usersTablePersonHeader"/>',
            left : 8,
            width: 250,
            dataType : 'text',
            editable: false,
            paramName: 'userName'
          }, {
            left: 270,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.createStudentGroup.usersTableRemoveRowTooltip"/>',
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
              var usersTable = getIxTableById('usersTable');
              usersTable.detachFromDom();
              for (var i = 0, len = event.results.users.length; i < len; i++) {
                var userId = event.results.users[i].id;
                var userName = event.results.users[i].name;
                var index = getUserRowIndex(userId);
                if (index == -1) {
                  usersTable.addRow([userId, userName, '']);
                } 
              }
              usersTable.reattachToDom();
            break;
          }
        });
        dialog.open();
      }

      function getUserRowIndex(userId) {
        var table = getIxTableById('usersTable');
        if (table) {
          var userIdColumn = table.getNamedColumnIndex('userId');
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableUserId = table.getCellValue(i, userIdColumn);
            if (tableUserId == userId) {
              return i;
            }
          }
        }
        return -1;
      }

      function setupStudentsTable() {
        var studentsTable = new IxTable($('studentsTable'), {
          id : "studentsTable",
          columns : [{
            width: 30,
            left: 8,
            dataType: 'button',
            paramName: 'studentInfoButton',
            imgsrc: GLOBAL_contextPath + '/gfx/info.png',
            tooltip: '<fmt:message key="students.createStudentGroup.studentsTableStudentInfoTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var personId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              var button = table.getCellEditor(event.row, table.getNamedColumnIndex('studentInfoButton'));
              openStudentInfoPopupOnElement(button, personId);
            } 
          }, {
            header : '<fmt:message key="students.createStudentGroup.studentsTableNameHeader"/>',
            left : 38,
            right : 860,
            dataType : 'text',
            paramName: 'studentName',
            editable: false
          }, {
            header : '<fmt:message key="students.createStudentGroup.studentsTableStudyProgrammeHeader"/>',
            width: 100,
            left : 230,
            dataType : 'select',
            editable: true,
            dynamicOptions: true,
            paramName: 'studentId',
            options: [
            ]
          }, {
            dataType: 'hidden', 
            paramName: 'personId'
          }, {
            right: 0,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.createStudentGroup.studentsTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
              if (event.tableComponent.getRowCount() == 0) {
                $('noStudentsAddedMessageContainer').setStyle({
                  display: ''
                });
                $('createStudentGroupStudentsTotalContainer').setStyle({
                  display: 'none'
                });
              }
              else {
                $('noStudentsAddedMessageContainer').setStyle({
                  display: 'none'
                });
                $('createStudentGroupStudentsTotalContainer').setStyle({
                  display: ''
                });
                $('createStudentGroupStudentsTotalValue').innerHTML = event.tableComponent.getRowCount(); 
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
        setupTags();
        setupUsersTable();
        setupStudentsTable();
      }
    </script>  
  </head>
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="students.createStudentGroup.pageTitle" /></h1>
    
    <div id="createStudentGroupFormContainer">
      <div class="genericFormContainer">
        <form action="createstudentgroup.json" method="post" ix:jsonform="true" ix:useglasspane="true">
      
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic"><fmt:message key="students.createStudentGroup.basicTabTitle" /></a>
            <a class="tabLabel" href="#students"><fmt:message key="students.createStudentGroup.studentsTabTitle" /></a>
            <ix:extensionHook name="students.createStudentGroup.tabLabels"/>
          </div>
  
          <div id="basic" class="tabContent">
            <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.createStudentGroup.nameTitle"/>
                  <jsp:param name="helpLocale" value="students.createStudentGroup.nameHelp"/>
                </jsp:include>

              <input type="text" class="required" name="name" value="${fn:escapeXml(module.name)}" size="40">
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudentGroup.tagsTitle"/>
                <jsp:param name="helpLocale" value="students.createStudentGroup.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudentGroup.beginsTitle"/>
                <jsp:param name="helpLocale" value="students.createStudentGroup.beginsHelp"/>
              </jsp:include>

              <input type="text" name="beginDate" class="ixDateField"/>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudentGroup.descriptionTitle"/>
                <jsp:param name="helpLocale" value="students.createStudentGroup.descriptionHelp"/>
              </jsp:include>

              <textarea ix:cktoolbar="studentGroupDescription" name="description" ix:ckeditor="true">${module.description}</textarea>
            </div>

            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="openSearchUsersDialog();"><fmt:message key="students.createStudentGroup.addPersonLink"/></span>
            </div>
            <div id="usersTable"> </div>
           
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.createStudentGroup.guidanceGroupTitle"/>
                <jsp:param name="helpLocale" value="students.createStudentGroup.guidanceGroupHelp"/>
              </jsp:include>

              <input type="checkbox" id="guidanceGroup" name="guidanceGroup" value="true" />
              <fmt:message key="students.createStudentGroup.guidanceGroupLabel" />
            </div>

            <ix:extensionHook name="students.createStudentGroup.tabs.basic"/>
          </div>
      
          <div id="students" class="tabContentixTableFormattedData hiddenTab">
            <div class="studentsTableContainer">
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" onclick="openSearchStudentsDialog();"><fmt:message key="students.createStudentGroup.addStudentLink"/></span>
              </div>
                
              <div id="noStudentsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
                <span><fmt:message key="students.createStudentGroup.noStudentsAddedPreFix"/> <span onclick="openSearchStudentsDialog();" class="genericTableAddRowLink"><fmt:message key="students.createStudentGroup.noStudentsAddedClickHereLink"/></span>.</span>
              </div>
            
              <div id="studentsTable"> </div>

              <div id="createStudentGroupStudentsTotalContainer" style="display:none;">
                <fmt:message key="students.createStudentGroup.studentsTotal"/> <span id="createStudentGroupStudentsTotalValue"></span>
              </div>

            </div>
            <ix:extensionHook name="students.createStudentGroup.tabs.students"/>
          </div>

          <ix:extensionHook name="students.createStudentGroup.tabs"/>
      
          <div class="genericFormSubmitSectionOffTab"><input type="submit" class="formvalid" value="<fmt:message key="students.createStudentGroup.saveButton"/>"></div>
  
        </form>
      </div>
    </div>
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>