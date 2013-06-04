<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="students.viewStudentGroup.pageTitle">
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
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function setupStudentsTable() {
        var studentsTable = new IxTable($('studentsTable'), {
          id : "studentsTable",
          rowHoverEffect: true,
          columns : [{
            width: 30,
            left: 8,
            dataType: 'button',
            paramName: 'studentInfoButton',
            imgsrc: GLOBAL_contextPath + '/gfx/info.png',
            tooltip: '<fmt:message key="students.viewStudentGroup.studentsTableStudentInfoTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var abstractStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('abstractStudentId'));
              var button = table.getCellEditor(event.row, table.getNamedColumnIndex('studentInfoButton'));
              openStudentInfoPopupOnElement(button, abstractStudentId);
            } 
          }, {
            header : '<fmt:message key="students.viewStudentGroup.studentsTableNameHeader"/>',
            left : 38,
            width : 300,
            dataType : 'text',
            paramName: 'studentName',
            editable: false
          }, {
            header : '<fmt:message key="students.viewStudentGroup.studentsTableStudyProgrammeHeader"/>',
            right: 8 + 22 + 8 + 22 + 8,
            left : 308,
            dataType : 'text', 
            paramName: 'studyProgramme',
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
            dataType: 'hidden', 
            paramName: 'abstractStudentId'
          }, {
            width: 22,
            right: 8 + 22 + 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="students.viewStudentGroup.studentsTableViewStudentTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var abstractStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('abstractStudentId'));
              redirectTo(GLOBAL_contextPath + '/students/viewstudent.page?abstractStudent=' + abstractStudentId);
            }
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.viewStudentGroup.studentsTableEditStudentTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var abstractStudentId = table.getCellValue(event.row, table.getNamedColumnIndex('abstractStudentId'));
              redirectTo(GLOBAL_contextPath + '/students/editstudent.page?abstractStudent=' + abstractStudentId);
            }
          }]        
        });

        var rows = new Array();
        <c:forEach var="student" items="${studentGroupStudents}">
          <c:choose>
            <c:when test="${student.student.studyProgramme ne null}">
              <c:set var="studyProgrammeName">${fn:escapeXml(student.student.studyProgramme.name)}</c:set>
            </c:when>
            <c:otherwise>
              <c:set var="studyProgrammeName"><fmt:message key="students.viewStudentGroup.studentsTableNoStudyProgramme"/></c:set>
            </c:otherwise>
          </c:choose>
          
          <c:if test="${student.student.hasFinishedStudies}">
            <c:set var="studyProgrammeName">${studyProgrammeName} *</c:set>
          </c:if>
          
          <c:set var="studentName">${fn:escapeXml(student.student.lastName)}, ${fn:escapeXml(student.student.firstName)}</c:set>
          
          rows.push([
            "", 
            "${studentName}", 
            "${studyProgrammeName}",
            ${student.student.abstractStudent.id},
            '',
            ''
          ]);
        </c:forEach>
        studentsTable.addRows(rows);
        if (studentsTable.getRowCount() > 0) {
          $('viewStudentGroupStudentsTotalValue').innerHTML = studentsTable.getRowCount();
        }
        
        studentsTable.addListener("afterFiltering", function (event) {
          var visibleRows = event.tableComponent.getVisibleRowCount();
          var totalRows = event.tableComponent.getRowCount();
          if (visibleRows == totalRows)
            $('viewStudentGroupStudentsTotalValue').innerHTML = totalRows;
          else
            $('viewStudentGroupStudentsTotalValue').innerHTML = visibleRows + " (" + totalRows + ")";
        });
      }

      function setupRelatedCommands() {
        var basicRelatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="students.viewStudentGroup.basicTabRelatedActionsLabel"/>'
        });
    
        basicRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.viewStudentGroup.editStudentGroupRelatedActionLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/students/editstudentgroup.page?studentgroup=${studentGroup.id}');
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
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    <h1 class="genericPageHeader">
      <fmt:message key="students.viewStudentGroup.pageTitle">
        <fmt:param value="${studentGroup.name}"/>
      </fmt:message>
    </h1>
  
    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic"><fmt:message key="students.viewStudentGroup.basicTabTitle" /></a>
        <a class="tabLabel" href="#students"><fmt:message key="students.viewStudentGroup.studentsTabTitle" /></a>
      </div>
        
      <div id="basic" class="tabContent">
        <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
        
        <c:choose>
          <c:when test="${not empty studentGroup.tags}">
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.viewStudentGroup.tagsTitle" />
                <jsp:param name="helpLocale" value="students.viewStudentGroup.tagsHelp" />
              </jsp:include>
              <c:forEach var="tag" items="${studentGroup.tags}" varStatus="vs">
                <c:out value="${tag.text}"/>
                <c:if test="${not vs.last}"><c:out value=" "/></c:if>
              </c:forEach>
            </div>
          </c:when>
        </c:choose> 

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="students.viewStudentGroup.beginsTitle" />
            <jsp:param name="helpLocale" value="students.viewStudentGroup.beginsHelp" />
          </jsp:include>
          <div><fmt:formatDate value="${studentGroup.beginDate}" /></div>
        </div>

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="students.viewStudentGroup.descriptionTitle" />
            <jsp:param name="helpLocale" value="students.viewStudentGroup.descriptionHelp" />
          </jsp:include>
          <div>${fn:replace(studentGroup.description, newLineChar, "<br/>")}</div>
        </div>

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="students.viewStudentGroup.usersTitle" />
            <jsp:param name="helpLocale" value="students.viewStudentGroup.usersHelp" />
          </jsp:include>
          <div>
            <c:forEach var="user" items="${studentGroup.users}">
              <div>${user.user.fullName}</div>
            </c:forEach>
          </div>
        </div>          
      </div>
  
      <div id="students" class="tabContent hiddenTab">
        <div id="studentsTable"></div>
        <c:if test="${fn:length(studentGroup.students) le 0}">
          <div class="genericTableNotAddedMessageContainer">
            <fmt:message key="students.viewStudentGroup.noStudents" />
          </div>
        </c:if>
        <c:if test="${fn:length(studentGroup.students) gt 0}">
          <div id="viewStudentGroupStudentsTotalContainer">
            <fmt:message key="students.viewStudentGroup.studentsTotal"/> <span id="viewStudentGroupStudentsTotalValue"></span>
          </div>
        </c:if>
      </div>
    </div>
  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>
