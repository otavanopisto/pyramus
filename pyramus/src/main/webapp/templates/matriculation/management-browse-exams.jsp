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
        var searchResultsTable = new IxTable($('searchResultsTableContainer'), {
          id: 'searchResultsTable',
          columns : [{
            header : 'Vuosi',
            left: 8,
            width: 100,
            dataType : 'text',
            editable: false,
            paramName: 'examYear'
          }, {
            header : 'Vuodenaika',
            width: 100,
            left: 8 + 100,
            dataType : 'select',
            editable: false,
            paramName: 'examTerm',
            options: [
              { text: "Kevät", value: "SPRING" },
              { text: "Syksy", value: "AUTUMN" }
            ]
          }, {
            header : 'Alkaa',
            width: 100,
            left: 8 + 100 + 8 + 100,
            dataType : 'date',
            editable: false,
            paramName: 'starts'
          }, {
            header : 'Päättyy',
            width: 100,
            left: 8 + 100 + 8 + 100 + 8 + 100,
            dataType : 'date',
            editable: false,
            paramName: 'ends'
          }, {
            dataType: 'hidden',
            paramName: 'examId'
          }, {
            width : 22,
            right : 8,
            dataType : 'button',
            imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip : 'Muokkaa',
            onclick : function(event) {
              var table = event.tableComponent;
              var examId = table.getCellValue(event.row, table.getNamedColumnIndex('examId'));
              redirectTo(GLOBAL_contextPath + '/matriculation/settings.page?examId=' + examId);
            }
          }]
        });

        var examsJSON = JSDATA["matriculationExams"].evalJSON();
        var examsArr = [];
        if (examsJSON) {
          for (var i = 0; i < examsJSON.length; i++) {
            examsArr.push([
              examsJSON[i].examYear,
              examsJSON[i].examTerm,
              examsJSON[i].starts,
              examsJSON[i].ends,
              examsJSON[i].id,
              ''
            ]);
          }
        }
        searchResultsTable.addRows(examsArr);
      };
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">Hallinnoi ylioppilaskirjoitusten kokeita</h1>
    
    <div id="applicationFilters" class="genericFormContainer">

      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#filters">Hallinnoi ylioppilaskirjoitusten kokeita</a>
      </div>

      <div id="filters" class="tabContent">
        <div class="genericTableAddRowContainer">
          <a class="genericTableAddRowLinkContainer" href="${pageContext.request.contextPath}/matriculation/settings.page?examId=new">Lisää uusi ylioppilaskirjoitusten koe</a>
        </div>
      
        <div id="searchResultsTableContainer"></div>
      </div>
    </div>
       
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>