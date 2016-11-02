<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="students.editStudentGroup.pageTitle">
        <fmt:param value="${studentGroup.name}"/>
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
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>

    <script type="text/javascript">
      function setupRelatedCommands() {
        var basicRelatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="students.editStudentGroup.basicTabRelatedActionsLabel"/>'
        });
    
        basicRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/eye.png',
          text: '<fmt:message key="students.editStudentGroup.viewStudentGroupRelatedActionLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/students/viewstudentgroup.page?studentgroup=${studentGroup.id}');
          }
        }));          
      }
      
      function addNewStudent(studentsTable, personId, studentId, studentName) {
        JSONRequest.request("students/getstudentstudyprogrammes.json", {
          parameters: {
            personId: personId
          },
          onSuccess: function (jsonResponse) {
            var rowIndex = studentsTable.addRow(['', studentName, studentId, personId, '', '']);
            var cellEditor = studentsTable.getCellEditor(rowIndex, studentsTable.getNamedColumnIndex('studentId'));
            for (var j = 0, l = jsonResponse.studentStudyProgrammes.length; j < l; j++) {
              IxTableControllers.getController('select').addOption(cellEditor , jsonResponse.studentStudyProgrammes[j].studentId, jsonResponse.studentStudyProgrammes[j].studyProgrammeName);
            }
            $('noStudentsAddedMessageContainer').setStyle({
              display: 'none'
            });
            $('editStudentGroupStudentsTotalContainer').setStyle({
              display: ''
            });
            $('editStudentGroupStudentsTotalValue').innerHTML = studentsTable.getRowCount(); 
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
            header : '<fmt:message key="students.editStudentGroup.usersTablePersonHeader"/>',
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
            tooltip: '<fmt:message key="students.editStudentGroup.usersTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            } 
          }, {
            dataType: 'hidden',
            paramName: 'studentGroupUserId'
          }]        
        });

        <c:forEach var="user" items="${studentGroup.users}">
          usersTable.addRow([
            ${user.staffMember.id},
            '${fn:escapeXml(user.staffMember.fullName)}',
            '',
            ${user.id}
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
              var usersTable = getIxTableById('usersTable');
              usersTable.detachFromDom();
              for (var i = 0, len = event.results.users.length; i < len; i++) {
                var userId = event.results.users[i].id;
                var userName = event.results.users[i].name;
                var index = getUserRowIndex(userId);
                if (index == -1) {
                  usersTable.addRow([userId, userName, '', '']);
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
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableUserId = table.getCellValue(i, table.getNamedColumnIndex('userId'));
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
            tooltip: '<fmt:message key="students.editStudentGroup.studentsTableStudentInfoTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var personId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              var button = table.getCellEditor(event.row, table.getNamedColumnIndex('studentInfoButton'));
              openStudentInfoPopupOnElement(button, personId);
            } 
          }, {
            header : '<fmt:message key="students.editStudentGroup.studentsTableNameHeader"/>',
            left : 38,
            dataType : 'text',
            paramName: 'studentName',
            editable: false
          }, {
            header : '<fmt:message key="students.editStudentGroup.studentsTableStudyProgrammeHeader"/>',
            width: 150,
            left: 230,
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
            paramName: 'removeButton',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.editStudentGroup.studentsTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
              if (event.tableComponent.getRowCount() == 0) {
                $('noStudentsAddedMessageContainer').setStyle({
                  display: ''
                });
                $('editStudentGroupStudentsTotalContainer').setStyle({
                  display: 'none'
                });
              }
              else {
                $('noStudentsAddedMessageContainer').setStyle({
                  display: 'none'
                });
                $('editStudentGroupStudentsTotalContainer').setStyle({
                  display: ''
                });
                $('editStudentGroupStudentsTotalValue').innerHTML = event.tableComponent.getRowCount(); 
              }
            } 
          }, {
            dataType: 'hidden', 
            paramName: 'studentGroupStudentId'
          }]        
        });

        var studentGroupStudents = JSDATA["studentGroupStudents"].evalJSON();

        studentsTable.detachFromDom();
        for (var i = 0, len = studentGroupStudents.length; i < len; i++) {
          var studentGroupStudent = studentGroupStudents[i];
          var rowIndex = studentsTable.addRow(['',
                     studentGroupStudent.lastName + ', ' + studentGroupStudent.firstName, 
                     studentGroupStudent.studentId,
                     studentGroupStudent.personId, 
                     '',
                     studentGroupStudent.id]);
          
          var cellEditor = studentsTable.getCellEditor(rowIndex, studentsTable.getNamedColumnIndex('studentId'));
          
          for (var j = 0, l = studentGroupStudent.studyProgrammes.length; j < l; j++) {
            var studyProgramme = studentGroupStudent.studyProgrammes[j];
            IxTableControllers.getController('select').addOption(
                cellEditor, 
                studyProgramme.studentId, 
                studyProgramme.studyProgrammeName, 
                studyProgramme.studentId == studentGroupStudent.studentId);
          }
        }
        studentsTable.reattachToDom();

        if (studentGroupStudents.length > 0) {
          $('noStudentsAddedMessageContainer').setStyle({
            display: 'none'
          });
          $('editStudentGroupStudentsTotalContainer').setStyle({
            display: ''
          });
          $('editStudentGroupStudentsTotalValue').innerHTML = studentsTable.getRowCount(); 
        }
        
        studentsTable.addListener("rowAdd", function (event) {
          var studentsTable = event.tableComponent;
          studentsTable.showCell(event.row, studentsTable.getNamedColumnIndex("removeButton"));
        });
        
        <c:if test="${fn:length(studentGroup.students) gt 0}">
          $('noStudentsAddedMessageContainer').setStyle({
            display: 'none'
          });
        </c:if>
      }

      function initializeDraftListener() {
        Event.observe(document, "ix:draftRestore", function (event) {
          var studentsTable = getIxTableById('studentsTable'); 
          if (studentsTable.getRowCount() > 0) {
            $('noStudentsAddedMessageContainer').setStyle({
              display: 'none'
            });
            $('editStudentGroupStudentsTotalContainer').setStyle({
              display: ''
            });
            $('editStudentGroupStudentsTotalValue').innerHTML = studentsTable.getRowCount(); 
          }
          else {
            $('noStudentsAddedMessageContainer').setStyle({
              display: ''
            });
            $('editStudentGroupStudentsTotalContainer').setStyle({
              display: 'none'
            });
          }
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
        setupRelatedCommands();
        initializeDraftListener();
        setupTags();
        setupUsersTable();
        setupStudentsTable();
      }
    </script>
  </head>
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">
      <fmt:message key="students.editStudentGroup.pageTitle">
        <fmt:param value="${studentGroup.name}"/>
      </fmt:message>
    </h1>
    
    <div id="createStudentGroupCreateFormContainer">
      <div class="genericFormContainer">
        <form action="editstudentgroup.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <input type="hidden" name="version" value="${studentGroup.version}"/>
      
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic"><fmt:message key="students.editStudentGroup.basicTabTitle" /></a>
            <a class="tabLabel" href="#students"><fmt:message key="students.editStudentGroup.studentsTabTitle" /></a>
            <ix:extensionHook name="students.editStudentGroup.tabLabels"/>
          </div>
  
          <div id="basic" class="tabContent">
            <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudentGroup.nameTitle"/>
                <jsp:param name="helpLocale" value="students.editStudentGroup.nameHelp"/>
              </jsp:include>
              
              <input type="text" class="required" name="name" value="${fn:escapeXml(studentGroup.name)}" size="40">
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudentGroup.tagsTitle"/>
                <jsp:param name="helpLocale" value="students.editStudentGroup.tagsHelp"/>
              </jsp:include>
              
              <input type="text" id="tags" name="tags" size="40" value="${fn:escapeXml(tags)}"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudentGroup.beginsTitle"/>
                <jsp:param name="helpLocale" value="students.editStudentGroup.beginsHelp"/>
              </jsp:include>

              <input type="text" name="beginDate" class="ixDateField" value="${studentGroup.beginDate.time}"/>
            </div>
  
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudentGroup.descriptionTitle"/>
                <jsp:param name="helpLocale" value="students.editStudentGroup.descriptionHelp"/>
              </jsp:include>

              <textarea ix:cktoolbar="studentGroupDescription" name="description" ix:ckeditor="true">${studentGroup.description}</textarea>
            </div>

            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="openSearchUsersDialog();"><fmt:message key="students.editStudentGroup.addPersonLink"/></span>
            </div>
            <div id="usersTable"> </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudentGroup.guidanceGroupTitle"/>
                <jsp:param name="helpLocale" value="students.editStudentGroup.guidanceGroupHelp"/>
              </jsp:include>

              <input type="checkbox" id="guidanceGroup" name="guidanceGroup" />
            </div>

            <ix:extensionHook name="students.editStudentGroup.tabs.basic"/>
          </div>
      
          <div id="students" class="tabContentixTableFormattedData hiddenTab">
            <div class="studentGroupStudentsTableContainer">
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" onclick="openSearchStudentsDialog();"><fmt:message key="students.editStudentGroup.addStudentLink"/></span>
              </div>
                
              <div id="noStudentsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
                <span><fmt:message key="students.editStudentGroup.noStudentsAddedPreFix"/> <span onclick="openSearchStudentsDialog();" class="genericTableAddRowLink"><fmt:message key="students.editStudentGroup.noStudentsAddedClickHereLink"/></span>.</span>
              </div>
            
              <div id="studentsTable"> </div>

              <div id="editStudentGroupStudentsTotalContainer" style="display:none;">
                <fmt:message key="students.editStudentGroup.studentsTotal"/> <span id="editStudentGroupStudentsTotalValue"></span>
              </div>

            </div>
            <ix:extensionHook name="students.editStudentGroup.tabs.students"/>
          </div>

          <ix:extensionHook name="students.editStudentGroup.tabs"/>
      
          <input type="hidden" name="studentGroupId" value="${studentGroup.id}"/>
          <div class="genericFormSubmitSectionOffTab"><input type="submit" class="formvalid" value="<fmt:message key="students.editStudentGroup.saveButton"/>"></div>
  
        </form>
      </div>
    </div>
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>