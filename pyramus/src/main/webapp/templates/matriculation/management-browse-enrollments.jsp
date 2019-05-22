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
      function doSearch(page) {
        var searchForm = $("searchForm");
        var below20courses = searchForm.below20courses.checked === true;

        JSONRequest.request("matriculation/searchenrollments.json", {
          parameters: {
            page: page,
            state: searchForm.enrollmentState.value,
            below20courses: below20courses
          },
          onSuccess: function(jsonResponse) {
            var resultsTable = getIxTableById('searchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              resultsTable.addRow([
                results[i].id,
                results[i].name,
                results[i].email,
                results[i].state,
                results[i].numMandatoryCourses,
                '']);
            }
            resultsTable.reattachToDom();
            getSearchNavigationById('searchResultsNavigation').setTotalPages(jsonResponse.pages);
            getSearchNavigationById('searchResultsNavigation').setCurrentPage(jsonResponse.page);
            $('searchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
            $('searchResultsWrapper').setStyle({
              display: ''
            });
          } 
        });
      }

      function onSearchEnrollments(event) {
        Event.stop(event);
        doSearch(0);
      }

      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        new IxSearchNavigation($('searchResultsPagesContainer'), {
          id: 'searchResultsNavigation',
          maxNavigationPages: 19,
          onclick: function(event) {
            doSearch(event.page);
          }
        });
        var searchResultsTable = new IxTable($('searchResultsTableContainer'), {
          id: 'searchResultsTable',
          columns : [{
            dataType: 'hidden',
            paramName: 'enrollmentId'
          }, {
            header : 'Nimi',
            left: 8,
            width: 300,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            header : 'Sähköposti',
            width: 300,
            left: 8 + 300,
            dataType : 'text',
            editable: false,
            paramName: 'email'
          }, {
            header : 'Tila',
            width: 200,
            left: 8 + 300 + 8 + 300,
            dataType : 'select',
            editable: false,
            paramName: 'state',
            options: [
              { text: "Jätetty", value: "PENDING" },
              { text: "Hyväksytty", value: "APPROVED" },
              { text: "Hylätty", value: "REJECTED" }
            ]
          }, {
            header : 'Pakollisia kursseja',
            width: 150,
            left: 8 + 300 + 8 + 300 + 8 + 200,
            dataType : 'text',
            editable: false,
            paramName: 'numMandatoryCourses'
          }, {
            width : 22,
            right : 8,
            dataType : 'button',
            imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip : 'Muokkaa',
            onclick : function(event) {
              var table = event.tableComponent;
              var enrollmentId = table.getCellValue(event.row, table.getNamedColumnIndex('enrollmentId'));
              redirectTo(GLOBAL_contextPath + '/matriculation/edit.page?enrollment=' + enrollmentId);
            }
          }]
        });
      };
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">YO-ilmoittautumiset</h1>
    
    <div id="applicationFilters" class="genericFormContainer">

      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#filters">Suodattimet</a>
      </div>

      <form id="searchForm" method="post" onSubmit="onSearchEnrollments(event);">
  
        <div id="filters" class="tabContent">

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Ilmoittautumisen tila"/>
            </jsp:include>
            <select id="enrollmentState" name="enrollmentState">
              <option value=""></option>
              <option value="PENDING">Jätetty</option>
              <option value="APPROVED">Hyväksytty</option>
              <option value="REJECTED">Hylätty</option>
            </select>
          </div>
  
          <div class="genericFormSection">
            <input type="checkbox" name="below20courses" id="below20courses" value="1"> Vain alle 20 kurssia suorittaneet
          </div>
          
          <div class="genericFormSubmitSection">
            <input type="submit" value="Käytä">
          </div>
        </div>

      </form>
    </div>
    
    <div id="searchResultsWrapper" style="display:none;">
      <div class="searchResultsTitle">Ilmoittautumiset</div>
      <div id="searchResultsContainer" class="searchResultsContainer">
        <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
        <div id="searchResultsTableContainer"></div>
        <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>