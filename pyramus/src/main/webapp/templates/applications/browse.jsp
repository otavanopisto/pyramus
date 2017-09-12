<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Hakemukset</title>

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
        JSONRequest.request("applications/searchapplications.json", {
          parameters: {
            line: searchForm.applicationLine.value,
            state: searchForm.applicationState.value,
            page: page
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
                $('applicationLine').select('option[value="' + results[i].line +'"]').first().innerHTML,
                results[i].date,
                $('applicationState').select('option[value="' + results[i].state +'"]').first().innerHTML,
                results[i].handler,
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

      function onSearchApplications(event) {
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
            paramName: 'applicationId'
          }, {
            header : 'Nimi',
            left: 8,
            dataType : 'text',
            editable: false,
            paramName: 'applicantName'
          }, {
            header : 'Sähköposti',
            right: 8 + 22 + 8 + 200 + 8 + 200 + 8 + 150 + 8 + 200 + 8,
            width: 300,
            dataType : 'text',
            editable: false,
            paramName: 'applicantEmail'
          }, {
            header : 'Linja',
            right: 8 + 22 + 8 + 200 + 8 + 200 + 8 + 150 + 8,
            width: 200,
            dataType : 'text',
            editable: false,
            paramName: 'applicantLine'
          }, {
            header : 'Viimeisin muutos',
            right: 8 + 22 + 8 + 200 + 8 + 200 + 8,
            width: 150,
            dataType : 'date',
            editable: false,
            paramName: 'applicantDate'
          }, {
            header : 'Tila',
            right: 8 + 22 + 8 + 200 + 8,
            width: 200,
            dataType : 'text',
            editable: false,
            paramName: 'applicantState'
          }, {
            header : 'Käsittelijä',
            right: 8 + 22 + 8, 
            width: 200,
            dataType : 'text',
            editable: false,
            paramName: 'applicantHandler'
          }, {
            width : 22,
            right : 8,
            dataType : 'button',
            imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip : 'Muokkaa',
            onclick : function(event) {
              var table = event.tableComponent;
              var applicationId = table.getCellValue(event.row, table.getNamedColumnIndex('applicationId'));
              console.log('Edit application ' + applicationId);
              //redirectTo(GLOBAL_contextPath + '/applications/editapplication.page?application=' + applicationId);
            }
          }]
        });
      };
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">Hakemukset</h1>
    
    <div id="applicationFilters" class="genericFormContainer">

      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#filters">Suodattimet</a>
      </div>

      <form id="searchForm" method="post" onSubmit="onSearchApplications(event);">
  
        <div id="filters" class="tabContent">

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Hakemuksen tila"/>
            </jsp:include>
            <select id="applicationState" name="applicationState">
              <option value=""></option>
              <option value="PENDING">Jätetty</option>
              <option value="PROCESSING">Käsittelyssä</option>
              <option value="WAITING_STAFF_SIGNATURE">Odottaa virallista hyväksyntää</option>
              <option value="STAFF_SIGNED">Hyväksyntä allekirjoitettu</option>
              <option value="APPROVED_BY_SCHOOL">Hyväksytty</option>
              <option value="APPROVED_BY_APPLICANT">Opiskelupaikka vastaanotettu</option>
              <option value="TRANSFERRED_AS_STUDENT">Siirretty opiskelijaksi</option>
              <option value="REGISTERED_AS_STUDENT">Rekisteröitynyt opiskelijaksi</option>
              <option value="REJECTED">Hylätty</option>
            </select>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Linja"/>
            </jsp:include>
            <select id="applicationLine" name="applicationLine">
              <option value=""></option>
              <option value="aineopiskelu">Aineopiskelu</option>
              <option value="nettilukio">Nettilukio</option>
              <option value="nettipk">Nettiperuskoulu</option>
              <option value="aikuislukio">Aikuislukio</option>
              <option value="bandilinja">Bändilinja</option>
              <option value="mk">Maahanmuuttajakoulutukset</option>
            </select>
          </div>
  
          <div class="genericFormSubmitSection">
            <input type="submit" value="Käytä">
          </div>
        </div>

      </form>
    </div>
    
    <div id="searchResultsWrapper" style="display:none;">
      <div class="searchResultsTitle">Hakemukset</div>
      <div id="searchResultsContainer" class="searchResultsContainer">
        <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
        <div id="searchResultsTableContainer"></div>
        <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>