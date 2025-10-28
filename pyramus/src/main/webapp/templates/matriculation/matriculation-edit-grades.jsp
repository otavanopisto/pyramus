<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>YO-ilmoittautumiset</title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));

        var subjectOptions = JSON.parse(JSDATA["subjectOptions"]);

        var gradeOptions = [
          {value: "", text: ""},
          {value: "IMPROBATUR", text: "Improbatur"},
          {value: "APPROBATUR", text: "Approbatur"},
          {value: "LUBENTER_APPROBATUR", text: "Lubenter approbatur"},
          {value: "CUM_LAUDE_APPROBATUR", text: "Cum laude approbatur"},
          {value: "MAGNA_CUM_LAUDE_APPROBATUR", text: "Magna cum laude approbatur"},
          {value: "EXIMIA_CUM_LAUDE_APPROBATUR", text: "Eximia cum laude approbatur"},
          {value: "LAUDATUR", text: "Laudatur"},
          {value: "UNKNOWN", text: "Ei tiedossa"},
          {value: "NO_RIGHT_TO_PARTICIPATE", text: "Ei osallistumisoikeutta"},
          {value: "INVALIDATED", text: "Mitätöity"},
          {value: "K", text: "Keskeytynyt"}
        ];

        var gradesTable = new IxTable($('enrolledAttendanceGradesTableContainer'), {
          id: 'gradesTable',
          columns : [{
            header : 'Aine',
            left: 8,
            width: 200,
            dataType : 'select',
            options: subjectOptions,
            editable: false,
            paramName: 'subject'
          }, {
            header : 'Arvosana',
            width: 200,
            left: 8 + 200 + 8,
            dataType : 'select',
            options: gradeOptions,
            editable: true,
            paramName: 'grade'
          }, {
            header : 'Yhteispisteet',
            width: 120,
            left: 8 + 200 + 8 + 200 + 8,
            dataType : 'number',
            editable: true,
            paramName: 'gradeTotalPoints'
          }, {
            header : 'Arviointipäivämäärä',
            width: 140,
            left: 8 + 200 + 8 + 200 + 8 + 120 + 8,
            dataType : 'date',
            editable: true,
            paramName: 'gradeDate'
          }, {
            dataType: 'hidden',
            paramName: 'gradeId'
          }]
        });

        var gradesJSON = JSDATA["grades"].evalJSON();
        var gradesArr = [];
        if (gradesJSON) {
          for (var i = 0; i < gradesJSON.length; i++) {
            gradesArr.push([
              gradesJSON[i].subject,
              gradesJSON[i].grade,
              gradesJSON[i].gradeTotalPoints ? gradesJSON[i].gradeTotalPoints : '',
              gradesJSON[i].gradeDate,
              gradesJSON[i].gradeId
            ]);
          }
        }
        gradesTable.addRows(gradesArr);
        
        document.getElementById("addGradeTableRow").addEventListener('click', function() {
          var rowId = gradesTable.addRow(['', '', '', '', '-1']);
          gradesTable.setCellEditable(rowId, gradesTable.getNamedColumnIndex('subject'), true);
        });
      };
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="matriculation.editGrades.pageTitle"/></h1>
    
    <div id="applicationFilters" class="genericFormContainer">

      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#grades"><fmt:message key="matriculation.editGrades.pageTitle"/></a>
      </div>

      <div id="grades" class="tabContent">
        <form method="post">
          <input name="person" type="hidden" value="${person.id}"/>
        
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.editGrades.studentTitle"/>
              <jsp:param name="helpLocale" value="matriculation.editGrades.studentTitle.help"/>
            </jsp:include>
            
            ${fn:escapeXml(person.latestStudent.fullName)}
          </div>
    
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.editGrades.termTitle"/>
              <jsp:param name="helpLocale" value="matriculation.editGrades.termTitle.help"/>
            </jsp:include>
            
            <div>
              <c:choose>
                <c:when test="${term == 'AUTUMN'}">
                  <fmt:message key="terms.seasons.autumn"/>
                </c:when>
                <c:when test="${term == 'SPRING'}">
                  <fmt:message key="terms.seasons.spring"/>
                </c:when>
                <c:otherwise>
                  ???
                </c:otherwise>
              </c:choose>
              ${year}
            </div>
          </div>
  
          <div class="genericTableAddRowContainer">
            <span class="genericTableAddRowLinkContainer" id="addGradeTableRow"><fmt:message key="matriculation.editGrades.grades.addRow"/></span>
          </div>
  
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.editGrades.gradesTitle"/>
              <jsp:param name="helpLocale" value="matriculation.editGrades.gradesTitle.help"/>
            </jsp:include>
            
            <div id="enrolledAttendanceGradesTableContainer"></div>
          </div>
          
          <button>Tallenna</button>
        </form>
      </div>
    </div>
       
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>