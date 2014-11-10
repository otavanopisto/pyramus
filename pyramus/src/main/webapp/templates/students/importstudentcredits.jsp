<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head> 
    <title>
      <fmt:message key="students.importStudentCredits.pageTitle">
        <fmt:param value="${baseStudent.fullName}"/>
      </fmt:message>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/datefield_support.jsp"></jsp:include>
    
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>

    <!-- Used to render memo values with line breaks; for some reason this is the only approach that works -->
    <% pageContext.setAttribute("newLineChar", "\n"); %>

    <script type="text/javascript">
      function setupRelatedActions() {
        var relatedActionsHoverMenu = new IxHoverMenu($('relatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="students.importStudentCredits.relatedActionsLabel"/>'
        });
    
        relatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/eye.png',
          text: '<fmt:message key="students.importStudentCredits.relatedActionsViewStudentLabel"/>',
          link: GLOBAL_contextPath + '/students/viewstudent.page?person=${baseStudent.person.id}'  
        }));
        
        relatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.importStudentCredits.relatedActionsEditStudentLabel"/>',
          link: GLOBAL_contextPath + '/students/editstudent.page?person=${baseStudent.person.id}'  
        }));
      }

      function setupTransferCreditsTable(studentId, containerElement, tableId, selectableRows, isCreditLinkTable) {
        var columns = [{
          left: 8, 
          width: 16,
          hidden: !selectableRows,
          dataType: 'checkbox',
          paramName: 'selected',
          editable: true,
          headerimg: {
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.importStudentCredits.transferCreditsTableSelectAllTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var colIndex = table.getNamedColumnIndex('selected');

              for (var i = 0, len = table.getRowCount(); i < len; i++) {
                table.setCellValue(i, colIndex, table.getCellValue(i, colIndex) == false);
              }
            }
          }
        }, {
          header : '<fmt:message key="students.importStudentCredits.transferCreditsTableNameHeader"/>',
          left: selectableRows ? 32 : 8,
          right: 8 + 100 + 8 + 200 + 8, 
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
          header : '<fmt:message key="students.importStudentCredits.transferCreditsTableSubjectHeader"/>',
          right: 8 + 100 + 8, 
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
          header : '<fmt:message key="students.importStudentCredits.transferCreditsTableGradeHeader"/>',
          right: 8, 
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
          dataType: 'hidden',
          paramName: 'transferCreditId'
        }];
        
        if (isCreditLinkTable) {
          columns.push({
            dataType: 'hidden',
            paramName: 'creditLinkId'
          });
        }
        
        var transferCreditsTable = new IxTable(containerElement, {
          id: tableId,
          rowHoverEffect: true,
          columns: columns
        });

        return transferCreditsTable;
      }
      
      function setupCourseAssessmentsTable(studentId, containerElement, tableId, selectableRows, isCreditLinkTable) {
        var columns = [{
          left: 8, 
          width: 16,
          hidden: !selectableRows,
          dataType: 'checkbox',
          paramName: 'selected',
          editable: true,
          headerimg: {
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.importStudentCredits.courseAssessmentsTableSelectAllTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var colIndex = table.getNamedColumnIndex('selected');

              for (var i = 0, len = table.getRowCount(); i < len; i++) {
                table.setCellValue(i, colIndex, table.getCellValue(i, colIndex) == false);
              }
            }
          }
        }, {
          header : '<fmt:message key="students.importStudentCredits.courseAssessmentsTableNameHeader"/>',
          left: selectableRows ? 32 : 8,
          right: 8 + 100 + 8 + 200 + 8, 
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
          header : '<fmt:message key="students.importStudentCredits.courseAssessmentsTableSubjectHeader"/>',
          right: 8 + 100 + 8, 
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
          header : '<fmt:message key="students.importStudentCredits.courseAssessmentsTableGradeHeader"/>',
          right: 8, 
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
          dataType: 'hidden',
          paramName: 'courseAssessmentId'
        }];
        
        if (isCreditLinkTable) {
          columns.push({
            dataType: 'hidden',
            paramName: 'creditLinkId'
          });
        }
        
        var courseAssessmentsTable = new IxTable(containerElement, {
          id: tableId,
          rowHoverEffect: true,
          columns: columns
        });
  
        return courseAssessmentsTable;
      }

      function setupStudentTables(studentId, baseStudentId) {
        var courseAssessmentsContainer = JSDATA["courseAssessments"].evalJSON();
        var transferCreditsContainer = JSDATA["transferCredits"].evalJSON();
        var linkedCourseAssessmentsContainer = JSDATA["linkedCourseAssessments"].evalJSON();
        var linkedTransferCreditsContainer = JSDATA["linkedTransferCredits"].evalJSON();

        var rows = new Array();

        // Course Assessments
        
        var courseAssessmentsTable = setupCourseAssessmentsTable(
            studentId, 
            $('courseAssessmentsTableContainer.' + studentId), 
            'courseAssessmentsTable.' + studentId,
            studentId != baseStudentId,
            false);

        var courseAssessments = courseAssessmentsContainer[studentId];

        if (courseAssessments) {
          rows.clear();
          for (var i = 0, l = courseAssessments.length; i < l; i++) {
            var cAs = courseAssessments[i];
            var isLinked = cAs.isLinked == true;

            rows.push([
                isLinked,
                cAs.courseName,
                cAs.subjectName,
                cAs.gradeName,
                cAs.courseAssessmentId
            ]);
          }
          courseAssessmentsTable.addRows(rows);
        }

        if (courseAssessmentsTable.getRowCount() > 0) {
          $('importStudentCreditsCourseAssessmentsTotalValue.' + studentId).innerHTML = courseAssessmentsTable.getRowCount(); 
        }
        else {
          $('importStudentCreditsCourseAssessmentsTotalContainer.' + studentId).setStyle({
            display: 'none'
          });
        }

        courseAssessmentsTable.addListener("afterFiltering", function (event) {
          var visibleRows = event.tableComponent.getVisibleRowCount();
          var totalRows = event.tableComponent.getRowCount();
          if (visibleRows == totalRows)
            $('importStudentCreditsCourseAssessmentsTotalValue.' + studentId).innerHTML = totalRows;
          else
            $('importStudentCreditsCourseAssessmentsTotalValue.' + studentId).innerHTML = visibleRows + " (" + totalRows + ")";
        });

        // TransferCredits
        
        var transferCreditsTable = setupTransferCreditsTable(
            studentId, 
            $('transferCreditsTableContainer.' + studentId),
            'transferCreditsTable.' + studentId,
            studentId != baseStudentId,
            false);

        var transferCredits = transferCreditsContainer[studentId];

        if (transferCredits) {
          rows.clear();
          for (var i = 0, l = transferCredits.length; i < l; i++) {
            var tc = transferCredits[i];
            var isLinked = tc.isLinked == true;
            
            rows.push([
                isLinked,
                tc.courseName,
                tc.subjectName,
                tc.gradeName,
                tc.transferCreditId
            ]);
          }
          transferCreditsTable.addRows(rows);
        }
        
        if (transferCreditsTable.getRowCount() > 0) {
          $('importStudentCreditsTransferCreditsTotalValue.' + studentId).innerHTML = transferCreditsTable.getRowCount(); 
        }
        else {
          $('importStudentCreditsTransferCreditsTotalContainer.' + studentId).setStyle({
            display: 'none'
          });
        }
        
        transferCreditsTable.addListener("afterFiltering", function (event) {
          var visibleRows = event.tableComponent.getVisibleRowCount();
          var totalRows = event.tableComponent.getRowCount();
          if (visibleRows == totalRows)
            $('importStudentCreditsTransferCreditsTotalValue.' + studentId).innerHTML = totalRows;
          else
            $('importStudentCreditsTransferCreditsTotalValue.' + studentId).innerHTML = visibleRows + " (" + totalRows + ")";
        });
        
        // Linked course assessments
        
        var linkedCourseAssessmentsTable = setupCourseAssessmentsTable(
            studentId, 
            $('linkedCourseAssessmentsTableContainer.' + studentId), 
            'linkedCourseAssessmentsTable.' + studentId,
            true,
            true);

        var linkedCourseAssessments = linkedCourseAssessmentsContainer[studentId];

        if (linkedCourseAssessments) {
          rows.clear();
          for (var i = 0, l = linkedCourseAssessments.length; i < l; i++) {
            var cAs = linkedCourseAssessments[i];
            var isLinked = (cAs.isLinked == true) && (studentId != baseStudentId);

            rows.push([
                isLinked,
                cAs.courseName,
                cAs.subjectName,
                cAs.gradeName,
                cAs.courseAssessmentId,
                cAs.creditLinkId
            ]);
          }
          linkedCourseAssessmentsTable.addRows(rows);
        }

        if (linkedCourseAssessmentsTable.getRowCount() > 0) {
          $('importStudentCreditsLinkedCourseAssessmentsTotalValue.' + studentId).innerHTML = linkedCourseAssessmentsTable.getRowCount(); 
        }
        else {
          $('importStudentCreditsLinkedCourseAssessmentsTotalContainer.' + studentId).setStyle({
            display: 'none'
          });
        }

        linkedCourseAssessmentsTable.addListener("afterFiltering", function (event) {
          var visibleRows = event.tableComponent.getVisibleRowCount();
          var totalRows = event.tableComponent.getRowCount();
          if (visibleRows == totalRows)
            $('importStudentCreditsLinkedCourseAssessmentsTotalValue.' + studentId).innerHTML = totalRows;
          else
            $('importStudentCreditsLinkedCourseAssessmentsTotalValue.' + studentId).innerHTML = visibleRows + " (" + totalRows + ")";
        });
        
        // Linked transfer credits
        
        var linkedTransferCreditsTable = setupTransferCreditsTable(
            studentId, 
            $('linkedTransferCreditsTableContainer.' + studentId),
            'linkedTransferCreditsTable.' + studentId,
            true,
            true);

        var linkedTransferCredits = linkedTransferCreditsContainer[studentId];

        if (linkedTransferCredits) {
          rows.clear();
          for (var i = 0, l = linkedTransferCredits.length; i < l; i++) {
            var tc = linkedTransferCredits[i];
            var isLinked = (tc.isLinked == true) && (studentId != baseStudentId);

            rows.push([
                isLinked,
                tc.courseName,
                tc.subjectName,
                tc.gradeName,
                tc.transferCreditId,
                tc.creditLinkId
            ]);
          }
          linkedTransferCreditsTable.addRows(rows);
        }
        
        if (linkedTransferCreditsTable.getRowCount() > 0) {
          $('importStudentCreditsLinkedTransferCreditsTotalValue.' + studentId).innerHTML = linkedTransferCreditsTable.getRowCount(); 
        }
        else {
          $('importStudentCreditsLinkedTransferCreditsTotalContainer.' + studentId).setStyle({
            display: 'none'
          });
        }
        
        linkedTransferCreditsTable.addListener("afterFiltering", function (event) {
          var visibleRows = event.tableComponent.getVisibleRowCount();
          var totalRows = event.tableComponent.getRowCount();
          if (visibleRows == totalRows)
            $('importStudentCreditsLinkedTransferCreditsTotalValue.' + studentId).innerHTML = totalRows;
          else
            $('importStudentCreditsLinkedTransferCreditsTotalValue.' + studentId).innerHTML = visibleRows + " (" + totalRows + ")";
        });
      }
      
      function onLoad(event) {
        var baseStudentId = ${baseStudent.id};
        
        <c:forEach var="student" items="${students}">
          var studentId = ${student.id};
          
          setupStudentTables(studentId, baseStudentId);
        </c:forEach>
        
        
        var tabControl = new IxProtoTabs($('studentTabs'));
        var tabControl = new IxProtoTabs($('baseStudentTabs'));

        <c:if test="${!empty param.activeTab}">
          tabControl.setActiveTab("${param.activeTab}");  
        </c:if>
        
        setupRelatedActions();
      }
    </script>
  </head>

  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <div>
      <div id="relatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
    <div>
    
    <h1 class="genericPageHeader">
      <fmt:message key="students.importStudentCredits.pageTitle">
        <fmt:param value="${baseStudent.fullName}"/>
      </fmt:message>
    </h1>
  
    <div id="importStudentCreditsViewContainer">
      <div class="genericFormContainer">
        <form action="importstudentcredits.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <input type="hidden" name="studentId" value="${baseStudent.id}"/>
        
          <div>
            <div class="importStudentCreditsLeftPane">
              <div class="tabLabelsContainer containsNestedTabs" id="baseStudentTabs">
                <a class="tabLabel" href="#baseStudent">
                  <c:choose>
                    <c:when test="${baseStudent.studyProgramme == null}">
                      <fmt:message key="students.importStudentCredits.noStudyProgrammeTabLabel"/>
                    </c:when>
                    <c:otherwise>
                      ${baseStudent.studyProgramme.name}
                    </c:otherwise>
                  </c:choose>
                  <c:if test="${baseStudent.hasFinishedStudies}">*</c:if>
                </a>
              </div>
  
              <div id="baseStudent" class="tabContent tabContentNestedTabs">    
                <div id="importStudentCreditsViewContainer"> 
                  <div class="genericFormContainer"> 
                    <div class="genericFormSection">
                      <fmt:message key="students.importStudentCredits.baseStudentPanelDescriptionText"/>
                    </div>
                    
                    <div class="genericFormSection">
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.importStudentCredits.courseAssessmentsTitle"/>
                        <jsp:param name="helpLocale" value="students.importStudentCredits.courseAssessmentsHelp"/>
                      </jsp:include> 
                      <div id="importStudentCreditsCourseAssessmentsTableContainer"><div id="courseAssessmentsTableContainer.${baseStudent.id}"></div></div>
                      <div id="importStudentCreditsCourseAssessmentsTotalContainer.${baseStudent.id}" class="importStudentCreditsCourseAssessmentsTotalContainer">
                        <fmt:message key="students.importStudentCredits.courseAssessmentsTotal"/> <span id="importStudentCreditsCourseAssessmentsTotalValue.${baseStudent.id}"></span>
                      </div>
                    </div>
                    
                    <div class="genericFormSection">                                  
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.importStudentCredits.transferCreditsTitle"/>
                        <jsp:param name="helpLocale" value="students.importStudentCredits.transferCreditsHelp"/>
                      </jsp:include> 
                      <div id="importStudentCreditsTransferCreditsTableContainer"><div id="transferCreditsTableContainer.${baseStudent.id}"></div></div>
                      <div id="importStudentCreditsTransferCreditsTotalContainer.${baseStudent.id}" class="importStudentCreditsTransferCreditsTotalContainer">
                        <fmt:message key="students.importStudentCredits.transferCreditsTotal"/> <span id="importStudentCreditsTransferCreditsTotalValue.${baseStudent.id}"></span>
                      </div>
                    </div>
    
                    <div class="genericFormSection">                                
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.importStudentCredits.linkedCourseAssessmentsTitle"/>
                        <jsp:param name="helpLocale" value="students.importStudentCredits.linkedCourseAssessmentsHelp"/>
                      </jsp:include> 
                      <div id="importStudentCreditsLinkedCourseAssessmentsTableContainer"><div id="linkedCourseAssessmentsTableContainer.${baseStudent.id}"></div></div>
                      <div id="importStudentCreditsLinkedCourseAssessmentsTotalContainer.${baseStudent.id}" class="importStudentCreditsLinkedCourseAssessmentsTotalContainer">
                        <fmt:message key="students.importStudentCredits.linkedCourseAssessmentsTotal"/> <span id="importStudentCreditsLinkedCourseAssessmentsTotalValue.${baseStudent.id}"></span>
                      </div>
                    </div>
    
                    <div class="genericFormSection">                                  
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.importStudentCredits.linkedTransferCreditsTitle"/>
                        <jsp:param name="helpLocale" value="students.importStudentCredits.linkedTransferCreditsHelp"/>
                      </jsp:include> 
                      <div id="importStudentCreditsLinkedTransferCreditsTableContainer"><div id="linkedTransferCreditsTableContainer.${baseStudent.id}"></div></div>
                      <div id="importStudentCreditsLinkedTransferCreditsTotalContainer.${baseStudent.id}" class="importStudentCreditsLinkedTransferCreditsTotalContainer">
                        <fmt:message key="students.importStudentCredits.linkedTransferCreditsTotal"/> <span id="importStudentCreditsLinkedTransferCreditsTotalValue.${baseStudent.id}"></span>
                      </div>
                    </div>
                  </div>
                </div>  
              </div>
            </div>
          
            <div class="importStudentCreditsRightPane">
              <div class="tabLabelsContainer containsNestedTabs" id="studentTabs">
                <c:forEach var="student" items="${students}">
                  <c:if test="${student.id ne baseStudent.id}">
                    <a class="tabLabel" href="#student.${student.id}">
                      <c:choose>
                        <c:when test="${student.studyProgramme == null}">
                          <fmt:message key="students.importStudentCredits.noStudyProgrammeTabLabel"/>
                        </c:when>
                        <c:otherwise>
                          ${student.studyProgramme.name}
                        </c:otherwise>
                      </c:choose>
                      <c:if test="${student.hasFinishedStudies}">*</c:if>
                    </a>
                  </c:if>
                </c:forEach>
              </div>
  
              <c:forEach var="student" items="${students}">
                <c:if test="${student.id ne baseStudent.id}">
                  <div id="student.${student.id}" class="tabContent tabContentNestedTabs">    
          
                    <div id="importStudentCreditsViewContainer"> 
                      <div class="genericFormContainer"> 
                        <div class="genericFormSection">
                          <fmt:message key="students.importStudentCredits.importStudentPanelDescriptionText"/>
                        </div>

                        <div class="genericFormSection">                                
                          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                            <jsp:param name="titleLocale" value="students.importStudentCredits.courseAssessmentsTitle"/>
                            <jsp:param name="helpLocale" value="students.importStudentCredits.courseAssessmentsHelp"/>
                          </jsp:include> 
                          <div id="importStudentCreditsCourseAssessmentsTableContainer"><div id="courseAssessmentsTableContainer.${student.id}"></div></div>
                          <div id="importStudentCreditsCourseAssessmentsTotalContainer.${student.id}" class="importStudentCreditsCourseAssessmentsTotalContainer">
                            <fmt:message key="students.importStudentCredits.courseAssessmentsTotal"/> <span id="importStudentCreditsCourseAssessmentsTotalValue.${student.id}"></span>
                          </div>
                        </div>
                        
                        <div class="genericFormSection">                                  
                          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                            <jsp:param name="titleLocale" value="students.importStudentCredits.transferCreditsTitle"/>
                            <jsp:param name="helpLocale" value="students.importStudentCredits.transferCreditsHelp"/>
                          </jsp:include> 
                          <div id="importStudentCreditsTransferCreditsTableContainer"><div id="transferCreditsTableContainer.${student.id}"></div></div>
                          <div id="importStudentCreditsTransferCreditsTotalContainer.${student.id}" class="importStudentCreditsTransferCreditsTotalContainer">
                            <fmt:message key="students.importStudentCredits.transferCreditsTotal"/> <span id="importStudentCreditsTransferCreditsTotalValue.${student.id}"></span>
                          </div>
                        </div>
        
                        <div class="genericFormSection">                                
                          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                            <jsp:param name="titleLocale" value="students.importStudentCredits.linkedCourseAssessmentsTitle"/>
                            <jsp:param name="helpLocale" value="students.importStudentCredits.linkedCourseAssessmentsHelp"/>
                          </jsp:include> 
                          <div id="importStudentCreditsLinkedCourseAssessmentsTableContainer"><div id="linkedCourseAssessmentsTableContainer.${student.id}"></div></div>
                          <div id="importStudentCreditsLinkedCourseAssessmentsTotalContainer.${student.id}" class="importStudentCreditsLinkedCourseAssessmentsTotalContainer">
                            <fmt:message key="students.importStudentCredits.linkedCourseAssessmentsTotal"/> <span id="importStudentCreditsLinkedCourseAssessmentsTotalValue.${student.id}"></span>
                          </div>
                        </div>
        
                        <div class="genericFormSection">                                  
                          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                            <jsp:param name="titleLocale" value="students.importStudentCredits.linkedTransferCreditsTitle"/>
                            <jsp:param name="helpLocale" value="students.importStudentCredits.linkedTransferCreditsHelp"/>
                          </jsp:include> 
                          <div id="importStudentCreditsLinkedTransferCreditsTableContainer"><div id="linkedTransferCreditsTableContainer.${student.id}"></div></div>
                          <div id="importStudentCreditsLinkedTransferCreditsTotalContainer.${student.id}" class="importStudentCreditsLinkedTransferCreditsTotalContainer">
                            <fmt:message key="students.importStudentCredits.linkedTransferCreditsTotal"/> <span id="importStudentCreditsLinkedTransferCreditsTotalValue.${student.id}"></span>
                          </div>
                        </div>
                      </div>
                    </div>  
                  </div>
                </c:if>
              </c:forEach>
            </div>
          </div>
      
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="students.importStudentCredits.saveButton"/>">
          </div>
        </form>      
      </div>
    </div>  

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>