<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      var selectedCourseId = -1;

      function getCourseRowIndex(table, courseId) {
         for (var i = 0; i < table.getRowCount(); i++) {
           var tableCourseId = table.getCellValue(i, table.getNamedColumnIndex('courseId'));
           if (tableCourseId == courseId) {
             return i;
           }
         }
        return -1;
      }
      
      function getResults() {
        var table = getIxTableById('searchResultsTable');
        var rowId = getCourseRowIndex(table, selectedCourseId);
        
        var name = table.getCellValue(rowId, table.getNamedColumnIndex('name'));
        var beginDate = table.getCellValue(rowId, table.getNamedColumnIndex('beginDate'));
        var endDate = table.getCellValue(rowId, table.getNamedColumnIndex('endDate'));
        
        return {
          courseId: selectedCourseId,
          name: name,
          beginDate: beginDate,
          endDate: endDate
        };
      }

      function onLoad(event) {
        var onCellClick = function (event) {
          var table = event.tableComponent;
          selectedCourseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
          getDialog().clickOk();
        };
        
        var searchResultsTable = new IxTable($('searchResultsTableContainer'), {
          id: 'searchResultsTable',
          columns : [ {
            header : '<fmt:message key="projects.searchStudentProjectModuleCoursesDialog.courseTableNameHeader"/>',
            left: 8,
            right: 8 + 130 + 8 + 130 + 8 + 70 + 8,
            dataType : 'text',
            editable: false,
            paramName: 'name',
            onclick: onCellClick
          }, {
            header : '<fmt:message key="projects.searchStudentProjectModuleCoursesDialog.courseTableStudentCountHeader"/>',
            right: 8 + 130 + 8 + 130 + 8,
            width : 90,
            dataType : 'text',
            editable: false,
            paramName: 'studentCount',
            onclick: onCellClick
          }, {
            header : '<fmt:message key="projects.searchStudentProjectModuleCoursesDialog.courseTableBeginDateHeader"/>',
            right: 8 + 130 + 8,
            width : 130,
            dataType : 'date',
            editable: false,
            paramName: 'beginDate',
            onclick: onCellClick
          }, {
            header : '<fmt:message key="projects.searchStudentProjectModuleCoursesDialog.courseTableEndDateHeader"/>',
            width: 130,
            right : 8,
            dataType : 'date',
            editable: false,
            paramName: 'endDate',
            onclick: onCellClick
          }, {
            dataType: 'hidden',
            paramName: 'courseId'
          }]
        });
        
        var name;
        var rowId;
        searchResultsTable.detachFromDom();
        <c:forEach var="studentProjectModuleCourse" items="${studentProjectModuleCourses}">
          <c:choose>
            <c:when test="${studentProjectModuleCourse.maxCourseStudentCount ne null}">
              <c:set var="studentCount">${studentProjectModuleCourse.courseStudentCount}/${studentProjectModuleCourse.maxCourseStudentCount}</c:set>
            </c:when>
            <c:otherwise>
              <c:set var="studentCount">${studentProjectModuleCourse.courseStudentCount}</c:set>
            </c:otherwise>
          </c:choose>
        
          rowId = searchResultsTable.addRow([
            '${fn:escapeXml(studentProjectModuleCourse.courseName)}', 
            '${studentCount}',
            ${studentProjectModuleCourse.courseBeginDate.time}, 
            ${studentProjectModuleCourse.courseEndDate.time},  
            ${studentProjectModuleCourse.courseId}]);
          
          <c:if test="${studentProjectModuleCourse.withinTimeFrame}">
            searchResultsTable.getRowElement(rowId).addClassName("studentProjectModuleCourseWithinTimeFrame");
          </c:if>
        </c:forEach>
        searchResultsTable.reattachToDom();
      }
    </script>

  </head>
  <body onload="onLoad(event);">

    <div id="searchStudentProjectModuleCoursesDialogContainer" class="modalSearchContainer">

      <div id="searchResultsContainer" class="modalSearchResultsContainer">
        <div class="modalSearchResultsTabLabel"><fmt:message key="projects.searchStudentProjectModuleCoursesDialog.searchResultsTitle"/></div>
        <div id="modalSearchResultsStatusMessageContainer" class="modalSearchResultsMessageContainer">${message}</div>    
        <div id="searchResultsTableContainer" class="modalSearchResultsTabContent"></div>
      </div>
      
    </div>

  </body>
</html>