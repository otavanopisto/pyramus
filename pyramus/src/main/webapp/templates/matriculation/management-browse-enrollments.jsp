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
            name: searchForm.name.value,
            examId: searchForm.examId.value,
            state: searchForm.enrollmentState.value,
            below20courses: below20courses,
            sort: searchForm.sorting.value
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
                results[i].guiders,
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
            width: 250,
            left: 8 + 300,
            dataType : 'text',
            editable: false,
            paramName: 'email'
          }, {
            header : 'Tila',
            width: 150,
            left: 8 + 300 + 8 + 250,
            dataType : 'select',
            editable: false,
            paramName: 'state',
            options: [
              { text: getLocale().getText("generic.matriculation.enrollmentStates.PENDING"), value: "PENDING" },
              { text: getLocale().getText("generic.matriculation.enrollmentStates.SUPPLEMENTATION_REQUEST"), value: "SUPPLEMENTATION_REQUEST" },
              { text: getLocale().getText("generic.matriculation.enrollmentStates.SUPPLEMENTED"), value: "SUPPLEMENTED" },
              { text: getLocale().getText("generic.matriculation.enrollmentStates.APPROVED"), value: "APPROVED" },
              { text: getLocale().getText("generic.matriculation.enrollmentStates.REJECTED"), value: "REJECTED" },
              { text: getLocale().getText("generic.matriculation.enrollmentStates.CONFIRMED"), value: "CONFIRMED" },
              { text: getLocale().getText("generic.matriculation.enrollmentStates.FILLED_ON_BEHALF"), value: "FILLED_ON_BEHALF" }
            ]
          }, {
            header : 'Pakollisia opintoja',
            width: 130,
            left: 8 + 300 + 8 + 250 + 8 + 150,
            dataType : 'text',
            editable: false,
            paramName: 'numMandatoryCourses'
          }, {
            header : 'Ryhmäohjaaja(t)',
            width: 200,
            left: 8 + 300 + 8 + 250 + 8 + 150 + 8 + 130,
            dataType : 'text',
            editable: false,
            paramName: 'guider'
          }, {
            header : 'Käsittelijä',
            width: 200,
            left: 8 + 300 + 8 + 250 + 8 + 150 + 8 + 130 + 8 + 200,
            dataType : 'text',
            editable: false,
            paramName: 'handler'
          }, {
            width : 22,
            right : 8,
            dataType : 'button',
            imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip : 'Muokkaa',
            onclick : function(event) {
              var table = event.tableComponent;
              var enrollmentId = table.getCellValue(event.row, table.getNamedColumnIndex('enrollmentId'));
              window.open(GLOBAL_contextPath + '/matriculation/edit.page?enrollment=' + enrollmentId, "_blank");
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
              <jsp:param name="titleText" value="Nimi"/>
            </jsp:include>
            <input type="text" name="name" />
          </div>
  
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Ilmoittautumiskierros"/>
            </jsp:include>
            <select id="examId" name="examId">
              <option value=""></option>
              <c:forEach var="exam" items="${exams}">
                <c:choose>
                  <c:when test="${exam.examTerm == 'SPRING'}">
                    <c:set var="examTermText"><fmt:message key="terms.seasons.spring" /></c:set>
                  </c:when>
                  <c:when test="${exam.examTerm == 'AUTUMN'}">
                    <c:set var="examTermText"><fmt:message key="terms.seasons.autumn" /></c:set>
                  </c:when>
                  <c:otherwise>
                    <c:set var="examTermText"></c:set>
                  </c:otherwise>
                </c:choose>
                <option value="${exam.id}">${exam.examYear} ${examTermText}</option>
              </c:forEach>
            </select>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Ilmoittautumisen tila"/>
            </jsp:include>
            <select id="enrollmentState" name="enrollmentState">
              <option value=""></option>
              <option value="PENDING"><fmt:message key="generic.matriculation.enrollmentStates.PENDING"/></option>
              <option value="SUPPLEMENTATION_REQUEST"><fmt:message key="generic.matriculation.enrollmentStates.SUPPLEMENTATION_REQUEST"/></option>
              <option value="SUPPLEMENTED"><fmt:message key="generic.matriculation.enrollmentStates.SUPPLEMENTED"/></option>
              <option value="APPROVED"><fmt:message key="generic.matriculation.enrollmentStates.APPROVED"/></option>
              <option value="REJECTED"><fmt:message key="generic.matriculation.enrollmentStates.REJECTED"/></option>
              <option value="CONFIRMED"><fmt:message key="generic.matriculation.enrollmentStates.CONFIRMED"/></option>
              <option value="FILLED_ON_BEHALF"><fmt:message key="generic.matriculation.enrollmentStates.FILLED_ON_BEHALF"/></option>
            </select>
          </div>
  
          <div class="genericFormSection">
            <input type="checkbox" name="below20courses" id="below20courses" value="1"> Vain alle 20 kurssia suorittaneet
          </div>
          
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Järjestys"/>
            </jsp:include>
            <select id="sorting" name="sorting">
              <option value="NONE"></option>
              <option value="DATE">Ilmoittautumispäivämäärä (uusin ensin)</option>
              <option value="STATE">Ilmoittautumisen tila</option>
            </select>
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