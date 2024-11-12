<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="grading.manageSpokenLanguageExams.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>

    <script type="text/javascript">

      function onLoad(event) {
        tabControl = new IxProtoTabs($('tabs'));

        var credits = JSDATA["credits"].evalJSON();
        var skillLevels = JSDATA["skillLevels"].evalJSON();

        var table = new IxTable($('spokenLanguageExamsTable'), {
          id : "spokenLanguageExamsTable",
          columns : [{
            left: 8,
            width: 22,
            dataType: 'button',
            paramName: 'modifyButton',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="grading.manageSpokenLanguageExams.tableEditRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              
              table.setCellValue(event.row, table.getNamedColumnIndex('edited'), true);

              var assessorIdColumn = table.getNamedColumnIndex('assessorId');
              var timestampColumn = table.getNamedColumnIndex('timestamp');

              table.setCellEditable(event.row, assessorIdColumn, true);
              table.setCellEditable(event.row, timestampColumn, true);
              table.setCellEditable(event.row, table.getNamedColumnIndex('gradeId'), true);
              table.setCellEditable(event.row, table.getNamedColumnIndex('skillLevel'), true);

              if (!table.getCellValue(event.row, timestampColumn)) {
                table.setCellValue(event.row, timestampColumn, new Date().getTime());
              }
              if (!table.getCellValue(event.row, assessorIdColumn)) {
                table.setCellValue(event.row, assessorIdColumn, '${loggedUserId}');
                IxTableControllers.getController('autoCompleteSelect').setDisplayValue(table.getCellEditor(event.row, assessorIdColumn), '${fn:escapeXml(loggedUserName)}');
              }
            }
          }, {
            header : '<fmt:message key="grading.manageSpokenLanguageExams.tableCourseNameHeader"/>',
            left: 8 + 22 + 8,
            right: 8 + 141 + 8 + 110 + 8 + 72 + 8 + 72 + 8,
            dataType: 'autoComplete',
            editable: false,
            paramName: 'courseName',
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
            header : '<fmt:message key="grading.manageSpokenLanguageExams.tableGradeHeader"/>',
            width : 72,
            right: 8 + 141 + 8 + 110 + 8 + 72 + 8, 
            dataType: 'select',
            editable: false,
            required: true,
            paramName: 'gradeId',
            options: [
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
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ]
          }, {
            header : '<fmt:message key="grading.manageSpokenLanguageExams.tableSkillLevelHeader"/>',
            width : 72,
            right: 8 + 141 + 8 + 110 + 8,
            dataType: 'select',
            editable: false,
            required: true,
            paramName: 'skillLevel',
            options: skillLevels, 
            contextMenu: [
              {
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ]            
          }, {
            header : '<fmt:message key="grading.manageSpokenLanguageExams.tableUserHeader"/>',
            width : 110,
            right: 8 + 141 + 8,
            dataType: 'autoCompleteSelect',
            autoCompleteParamName: 'userId',
            required: true,
            editable: false,
            paramName: 'assessorId',
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
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ]            
          }, {
            header : '<fmt:message key="grading.manageSpokenLanguageExams.tableDateHeader"/>',
            width : 141,
            right: 8,
            dataType: 'date',
            required: true,
            editable: false,
            paramName: 'timestamp',
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
            dataType: 'hidden',
            paramName: 'creditId'
          }, {
            dataType: 'hidden',
            paramName: 'edited'
          }]
        });

        var userColumnIndex = table.getNamedColumnIndex('assessorId');

        table.detachFromDom();

        for (var i = 0; i < credits.length; i++) {
          var credit = credits[i];
          var newRowIndex = table.addRow([
            '',
            credit.courseName,
            credit.examGradeId,
            credit.examSkillLevel,
            credit.examAssessorId,
            credit.examTimestamp,
            credit.creditId,
            false
          ]);

          IxTableControllers.getController('autoCompleteSelect').setDisplayValue(table.getCellEditor(newRowIndex, userColumnIndex), credit.examAssessorName);
        }
        
        table.reattachToDom();
      }
        
    </script>
  </head>
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="grading.manageSpokenLanguageExams.pageTitle"/></h1>
    
    <div id="manageSpokenLanguageExamsFormContainer"> 
      <div class="genericFormContainer"> 
        <form action="managespokenexams.page" method="post">
          <input type="hidden" value="${student.id}" name="studentId"/>
    
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#exams">
              <fmt:message key="grading.manageSpokenLanguageExams.tabLabel"/>
            </a>
          </div>
          
          <div id="exams" class="tabContentixTableFormattedData">
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="grading.manageSpokenLanguageExams.studentTitle"/>
                <jsp:param name="helpLocale" value="grading.manageSpokenLanguageExams.studentHelp"/>
              </jsp:include>
              <div> ${student.fullName} </div>
            </div>

            <c:choose>
              <c:when test="${!empty student.curriculum}">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="grading.manageSpokenLanguageExams.studentCurriculumTitle"/>
                    <jsp:param name="helpLocale" value="grading.manageSpokenLanguageExams.studentCurriculumHelp"/>
                  </jsp:include>
                  <div>${student.curriculum.name}</div>
                </div>
              </c:when>
            </c:choose>

            <div id="spokenLanguageExamsTable"></div>
          </div>
    
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="generic.form.saveButton"/>">
          </div>

        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>