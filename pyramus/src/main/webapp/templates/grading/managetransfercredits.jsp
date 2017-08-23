<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="grading.manageTransferCredits.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>

    <script type="text/javascript">

      var archivedRowIndex;

      function addTransferCreditsTableRow() {
        var table = getIxTableById('transferCreditsTable');
        
        rowIndex = table.addRow(['', '', -1, 0, 0, -1, '', -1, -1, '', '', ${loggedUserId}, new Date().getTime(), '', '', -1], true);
        
        var subjectColumnIndex = table.getNamedColumnIndex('subject');
        var schoolColumnIndex = table.getNamedColumnIndex('school');
        var userColumnIndex = table.getNamedColumnIndex('user');
        var modifyColumnIndex = table.getNamedColumnIndex('modifyButton');

        IxTableControllers.getController('autoCompleteSelect').setDisplayValue(table.getCellEditor(rowIndex, subjectColumnIndex), '');
        IxTableControllers.getController('autoCompleteSelect').setDisplayValue(table.getCellEditor(rowIndex, schoolColumnIndex), '');
        IxTableControllers.getController('autoCompleteSelect').setDisplayValue(table.getCellEditor(rowIndex, userColumnIndex), '${fn:escapeXml(loggedUserName)}');

        // Hide modify button for new entries
        table.hideCell(rowIndex, modifyColumnIndex);
        
        $('noTransferCreditsAddedMessageContainer').setStyle({
          display: 'none'
        });
        
        updateTransferCreditsCount();
      }
      
      function onAddTemplateButtonClick(event) {
        Event.stop(event);
        
        var transferCreditTemplateId = $('transferCreditTemplateSelect').value;
        loadTransferCreditTemplates(transferCreditTemplateId);
      }
      
      function updateTransferCreditsCount() {
        var table = getIxTableById('transferCreditsTable');
        $('transferCreditsCount').update(table.getRowCount());
      }
      
      function loadTransferCreditTemplates(transferCreditTemplateId) {
        var glassPane = new IxGlassPane(document.body, { });
        glassPane.show();

        JSONRequest.request('grading/loadtransfercredittemplates.json', {
          parameters: {
            transferCreditTemplateId: transferCreditTemplateId
          },
          onSuccess: function (jsonResponse) {
            var table = getIxTableById('transferCreditsTable');
            var rowCountBefore = table.getRowCount();
            var results = jsonResponse.results;
            var rowDatas = new Array();
            table.detachFromDom();
            
            for (var i = 0, l = results.length; i < l; i++) {
              var template = results[i];
              
              rowDatas.push([
                '',
                template.courseName.escapeHTML(),
                template.courseOptionality,                                  
                template.courseNumber,
                -1,           
                template.subjectId,         
                template.courseUnits, 
                template.courseUnit,          
                -1,
                template.curriculumId,
                '',
                ${loggedUserId},           
                new Date().getTime(), 
                '',
                '',
                -1
              ]);
            }
            
            table.addRows(rowDatas, true);
            
            var userColumnIndex = table.getNamedColumnIndex('user');
            var subjectColumnIndex = table.getNamedColumnIndex('subject');
            
            for (var i = 0, l = results.length; i < l; i++) {
              var template = results[i];
              
              var userCellEditor = table.getCellEditor(i + rowCountBefore, userColumnIndex);
              var subjectCellEditor = table.getCellEditor(i + rowCountBefore, subjectColumnIndex);
              
              IxTableControllers.getController('autoCompleteSelect').setDisplayValue(userCellEditor, '${fn:escapeXml(loggedUserName)}');
              IxTableControllers.getController('autoCompleteSelect').setDisplayValue(subjectCellEditor, template.subjectName);
            }
            
            if (table.getRowCount() > 0) {
              $('noTransferCreditsAddedMessageContainer').setStyle({
                display: 'none'
              });
            }
            
            updateTransferCreditsCount();
            
            table.reattachToDom();
            glassPane.hide();
            delete glassPane;
          }
        });
      }
      
      function onLoad(event) {
        tabControl = new IxProtoTabs($('tabs'));
        var curriculums = JSDATA["curriculums"].evalJSON();

        var curriculumOptions = [{text: '', value: ''}];
        for (var i = 0, len = curriculums.length; i < len; i++) {
          curriculumOptions.push({
            text: curriculums[i].name,
            value: curriculums[i].id
          });
        }
        
        var transferCreditsTable = new IxTable($('transferCreditsTable'), {
          id : "transferCreditsTable",
          columns : [{
            left: 4,
            width: 22,
            dataType: 'button',
            paramName: 'modifyButton',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="grading.manageTransferCredits.transferCreditsTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              for (var i = 0; i < table.getColumnCount(); i++) {
                table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
              }
            }
          }, {
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableCourseNameHeader"/>',
            left: 4 + 22 + 4,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3 + 35 + 3 + 80 + 3 + 120 + 3 + 100 + 4 + 80 + 3 + 120 + 3 + 72 + 4 + 57 + 3 + 110 + 4,
            dataType: 'autoComplete',
            required: true,
            editable: false,
            paramName: 'courseName',
            autoCompleteUrl: GLOBAL_contextPath + '/settings/transfercreditcoursenameautocomplete.binary',
            autoCompleteProgressUrl: '${pageContext.request.contextPath}/gfx/progress_small.gif',
            onAutoCompleteValueSelect: function (event) {
              var row = event.row;
              var li = event.liElement;
              var subjectId = li.down('input[name="subjectId"]').value;
              var subjectDisplayValue = li.down('input[name="subjectName"]').value; 
              var courseLength = li.down('input[name="courseLength"]').value;
              var courseLengthUnitId = li.down('input[name="courseLengthUnitId"]').value;
              var courseNumber = li.down('input[name="courseNumber"]').value;

              var subjectColumnIndex = transferCreditsTable.getNamedColumnIndex('subject'); 
              var courseLengthColumnIndex = transferCreditsTable.getNamedColumnIndex('courseLength'); 
              var courseLengthUnitColumnIndex = transferCreditsTable.getNamedColumnIndex('courseLengthUnit'); 
              var courseNumberColumnIndex = transferCreditsTable.getNamedColumnIndex('courseNumber'); 

              transferCreditsTable.setCellValue(row, subjectColumnIndex, subjectId);
              IxTableControllers.getController('autoCompleteSelect').setDisplayValue(transferCreditsTable.getCellEditor(row, subjectColumnIndex), subjectDisplayValue);

              transferCreditsTable.setCellValue(row, courseLengthColumnIndex, courseLength);
              transferCreditsTable.setCellValue(row, courseLengthUnitColumnIndex, courseLengthUnitId);
              transferCreditsTable.setCellValue(row, courseNumberColumnIndex, courseNumber);
            },
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
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableCourseOptionalityHeader"/>',
            width: 110,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3 + 35 + 3 + 80 + 3 + 120 + 3 + 100 + 4 + 80 + 3 + 120 + 3 + 72 + 4 + 57 + 3,
            dataType: 'select',
            required: true,
            editable: false,
            paramName: 'courseOptionality',
            options: [
              {text: '<fmt:message key="grading.manageTransferCredits.transferCreditsTableCourseOptionalityOptional"/>', value: 'OPTIONAL'},
              {text: '<fmt:message key="grading.manageTransferCredits.transferCreditsTableCourseOptionalityMandatory"/>', value: 'MANDATORY'}
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
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableCourseNumberHeader"/>',
            width : 57,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3 + 35 + 3 + 80 + 3 + 120 + 3 + 100 + 4 + 80 + 3 + 120 + 3 + 72 + 4,
            dataType: 'number',
            editorClassNames: "number",
            editable: false,
            paramName: 'courseNumber'
          }, {
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableGradeHeader"/>',
            width : 72,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3 + 35 + 3 + 80 + 3 + 120 + 3 + 100 + 4 + 80 + 3 + 120 + 3, 
            dataType: 'select',
            editable: false,
            required: true,
            paramName: 'grade',
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
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableSubjectHeader"/>',
            width : 120,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3 + 35 + 3 + 80 + 3 + 120 + 3 + 100 + 4 + 80 + 3,
            dataType: 'autoCompleteSelect',
            autoCompleteParamName: 'subjectId',
            editable: false,
            required: true,
            paramName: 'subject',
            autoCompleteUrl: GLOBAL_contextPath + '/settings/subjectsautocomplete.binary',
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
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableLengthHeader"/>',
            width : 80,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3 + 35 + 3 + 80 + 3 + 120 + 3 + 100 + 4,
            dataType: 'number',
            required: true,
            editable: false,
            paramName: 'courseLength',
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
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableLengthUnitHeader"/>',
            width : 100,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3 + 35 + 3 + 80 + 3 + 120 + 3,
            dataType: 'select',
            required: true,
            editable: false,
            paramName: 'courseLengthUnit',
            options: [
              <c:forEach var="timeUnit" items="${timeUnits}" varStatus="vs">
                {text: "${fn:escapeXml(timeUnit.name)}", value: ${timeUnit.id}}
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
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableSchoolHeader"/>',
            width : 120,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3 + 35 + 3 + 80 + 3,
            dataType: 'autoCompleteSelect',
            autoCompleteParamName: 'schoolId',
            required: true,
            editable: false,
            paramName: 'school',
            autoCompleteUrl: GLOBAL_contextPath + '/settings/schoolsautocomplete.binary',
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
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableCourseCurriculumHeader"/>',
            width: 80,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3 + 35 + 3,
            dataType: 'select',
            required: false,
            editable: false,
            paramName: 'curriculum',
            options: curriculumOptions,
            contextMenu: [
              {
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ]            
          }, {
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableCourseOffCurriculumHeader"/>',
            headerTooltip: '<fmt:message key="grading.manageTransferCredits.transferCreditsTableCourseOffCurriculumTooltip"/>',
            width: 35,
            right: 4 + 22 + 4 + 141 + 4 + 110 + 3,
            dataType: 'checkbox',
            required: false,
            editable: false,
            paramName: 'offCurriculum',
            contextMenu: [
              {
                text: '<fmt:message key="generic.action.copyValues"/>',
                onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
              }
            ]            
          }, {
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableUserHeader"/>',
            width : 110,
            right: 4 + 22 + 4 + 141 + 4,
            dataType: 'autoCompleteSelect',
            autoCompleteParamName: 'userId',
            required: true,
            editable: false,
            paramName: 'user',
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
            header : '<fmt:message key="grading.manageTransferCredits.transferCreditsTableDateHeader"/>',
            width : 141,
            right: 4 + 22 + 4,
            dataType: 'date',
            required: true,
            editable: false,
            paramName: 'date',
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
            right: 4,
            width: 22,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="grading.manageTransferCredits.transferCreditsTableArchiveTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var creditId = table.getCellValue(event.row, table.getNamedColumnIndex('creditId'));
              var courseName = table.getCellValue(event.row, table.getNamedColumnIndex('courseName'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=grading.manageTransferCredits.transferCreditArchiveConfirmDialogContent&localeParams=" + encodeURIComponent(courseName);

              archivedRowIndex = event.row; 
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="grading.manageTransferCredits.transferCreditArchiveConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="grading.manageTransferCredits.transferCreditArchiveConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="grading.manageTransferCredits.transferCreditArchiveConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener(function(event) {
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("grading/archivetransfercredit.json", {
                      parameters: {
                        transferCreditId: creditId
                      },
                      onSuccess: function (jsonResponse) {
                        getIxTableById('transferCreditsTable').deleteRow(archivedRowIndex);
                        saveFormDraft();
                      }
                    });
                  break;
                }
              });
            
              dialog.open();
            },
            paramName: 'archiveButton',
            hidden: true
          }, {
            right: 4,
            width: 22,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="grading.manageTransferCredits.transferCreditsTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
              if (event.tableComponent.getRowCount() == 0) {
                $('noTransferCreditsAddedMessageContainer').setStyle({
                  display: ''
                });
              }
              
              updateTransferCreditsCount();
            },
            paramName: 'removeButton',
            hidden: false
          }, {
            dataType: 'hidden',
            paramName: 'creditId'
          }]
        });

        var subjectColumnIndex = transferCreditsTable.getNamedColumnIndex('subject');
        var schoolColumnIndex = transferCreditsTable.getNamedColumnIndex('school');
        var userColumnIndex = transferCreditsTable.getNamedColumnIndex('user');
        var removeButtonColumnIndex = transferCreditsTable.getNamedColumnIndex('removeButton');
        var archiveButtonColumnIndex = transferCreditsTable.getNamedColumnIndex('archiveButton');
        
        var rowIndex;
        transferCreditsTable.detachFromDom();
        <c:forEach var="transferCredit" items="${transferCredits}">
          <c:choose>
            <c:when test="${transferCredit.offCurriculum}">
              <c:set var="offCurriculum">1</c:set>
            </c:when>
            <c:otherwise>
              <c:set var="offCurriculum"></c:set>
            </c:otherwise>
          </c:choose>
        
          rowIndex = transferCreditsTable.addRow([
            '',
            '${fn:escapeXml(transferCredit.courseName)}',
            '${transferCredit.optionality}',
            ${transferCredit.courseNumber},
            ${transferCredit.grade.id},           
            ${transferCredit.subject.id},         
            ${transferCredit.courseLength.units}, 
            ${transferCredit.courseLength.unit.id},          
            ${transferCredit.school.id},                     
            '${transferCredit.curriculum.id}',
            '${offCurriculum}',
            ${transferCredit.assessor.id},
            ${transferCredit.date.time},                     
            '',
            '',
            ${transferCredit.id}
          ]);
          
          transferCreditsTable.hideCell(rowIndex, removeButtonColumnIndex);
          transferCreditsTable.showCell(rowIndex, archiveButtonColumnIndex);
          
          <c:choose>
            <c:when test="${transferCredit.subject.educationType ne null and not empty transferCredit.subject.code}">
              <c:set var="subjectName">
                <fmt:message key="generic.subjectFormatterWithEducationType">
                  <fmt:param value="${transferCredit.subject.code}"/>
                  <fmt:param value="${transferCredit.subject.name}"/>
                  <fmt:param value="${transferCredit.subject.educationType.name}"/>
                </fmt:message>
              </c:set>
            </c:when>
            <c:when test="${transferCredit.subject.educationType ne null and empty transferCredit.subject.code}">
              <c:set var="subjectName">
                <fmt:message key="generic.subjectFormatterNoSubjectCode">
                  <fmt:param value="${transferCredit.subject.name}"/>
                  <fmt:param value="${transferCredit.subject.educationType.name}"/>
                </fmt:message>
              </c:set>
            </c:when>
            <c:when test="${transferCredit.subject.educationType eq null and not empty transferCredit.subject.code}">
              <c:set var="subjectName">
                <fmt:message key="generic.subjectFormatterNoEducationType">
                  <fmt:param value="${transferCredit.subject.code}"/>
                  <fmt:param value="${transferCredit.subject.name}"/>
                </fmt:message>
              </c:set>
            </c:when>
            <c:otherwise>
              <c:set var="subjectName">${transferCredit.subject.name}</c:set>
            </c:otherwise>
          </c:choose>
          
          IxTableControllers.getController('autoCompleteSelect').setDisplayValue(transferCreditsTable.getCellEditor(rowIndex, subjectColumnIndex), '${fn:escapeXml(subjectName)}');
          IxTableControllers.getController('autoCompleteSelect').setDisplayValue(transferCreditsTable.getCellEditor(rowIndex, schoolColumnIndex), '${fn:escapeXml(transferCredit.school.name)}');
          IxTableControllers.getController('autoCompleteSelect').setDisplayValue(transferCreditsTable.getCellEditor(rowIndex, userColumnIndex), '${fn:escapeXml(transferCredit.assessor.fullName)}');
        </c:forEach>
        transferCreditsTable.reattachToDom();

        if (transferCreditsTable.getRowCount() > 0) {
          $('noTransferCreditsAddedMessageContainer').setStyle({
            display: 'none'
          });
        }
        
        updateTransferCreditsCount();
      }
        
    </script>
  </head>
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="grading.manageTransferCredits.pageTitle"/></h1>
    
    <div id="manageTransferCreditsFormContainer"> 
      <div class="genericFormContainer"> 
        <form action="savetransfercredits.json" method="post" ix:jsonform="true" ix:useglasspane="true" name="saveTransferCreditsForm" id="saveTransferCreditsForm">
          <input type="hidden" value="${student.id}" name="studentId"/>
    
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#manageTransferCredits">
              <fmt:message key="grading.manageTransferCredits.tabLabelTransferCredits"/>
            </a>
          </div>
          
          <div id="manageTransferCredits" class="tabContentixTableFormattedData">
          
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="grading.manageTransferCredits.studentTitle"/>
                <jsp:param name="helpLocale" value="grading.manageTransferCredits.studentHelp"/>
              </jsp:include>
              <div> ${student.fullName} </div>
            </div>

            <c:choose>
              <c:when test="${!empty student.curriculum}">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="grading.manageTransferCredits.studentCurriculumTitle"/>
                    <jsp:param name="helpLocale" value="grading.manageTransferCredits.studentCurriculumHelp"/>
                  </jsp:include>
                  <div>${student.curriculum.name}</div>
                </div>
              </c:when>
            </c:choose>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="grading.manageTransferCredits.templateTitle"/>
                <jsp:param name="helpLocale" value="grading.manageTransferCredits.templateHelp"/>
              </jsp:include>
              <select id="transferCreditTemplateSelect">
                <c:forEach var="transferCreditTemplate" items="${transferCreditTemplates}">
                   <option value="${transferCreditTemplate.id}">${transferCreditTemplate.name}</option>
                </c:forEach>
              </select>     
              
              <button onclick="onAddTemplateButtonClick(event);">
                <fmt:message key="grading.manageTransferCredits.addTemplateButton"/>
              </button>   
            </div>

            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addTransferCreditsTableRow();"><fmt:message key="grading.manageTransferCredits.addTransferCreditLink"/></span>
            </div>
              
            <div id="noTransferCreditsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="grading.manageTransferCredits.noTransferCreditsAddedPreFix"/> <span onclick="addTransferCreditsTableRow();" class="genericTableAddRowLink"><fmt:message key="grading.manageTransferCredits.noTransferCreditsAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="transferCreditsTable"></div>
            
            <div id="transferCreditsCountContainer">
              <div><fmt:message key="grading.manageTransferCredits.transferCreditCountLabel"/></div> <div id="transferCreditsCount"> 0 </div>
            </div>
          </div>
    
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="grading.manageTransferCredits.saveButton"/>">
          </div>

        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>