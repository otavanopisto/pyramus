<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="students.searchStudents.pageTitle"/></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/studentinfopopup_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">

      /**
       * Performs the search and displays the results of the given page.
       *
       * @param page The results page to be shown after the search
       */
      function doSearch(page) {
        var searchForm = $("searchForm");
        JSONRequest.request("students/searchstudents.json", {
          parameters: {
            firstname: searchForm.firstname.value,
            lastname: searchForm.lastname.value,
            nickname: searchForm.nickname.value,
            tags: searchForm.tags.value,
            education: searchForm.education.value,
            email: searchForm.email.value,
            phone: searchForm.phone.value,
            addressCity: searchForm.addressCity.value,
            addressCountry: searchForm.addressCountry.value,
            addressPostalCode: searchForm.addressPostalCode.value,
            addressStreetAddress: searchForm.addressStreetAddress.value,
            sex: searchForm.sex.value,
            nationality: searchForm.nationality.value,
            municipality: searchForm.municipality.value,
            language: searchForm.language.value,
            lodging: searchForm.lodging.value,
            studyProgramme: searchForm.studyProgramme.value,
            ssn: searchForm.ssn.value,
            studentFilter: searchForm.studentFilter.value,
            activeTab: searchForm.activeTab.value,
            query: searchForm.simpleQuery.value,
            activesQuery: searchForm.simpleActiveQuery.value,
            page: page
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('searchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              var rowIndex = resultsTable.addRow(['', String(results[i].lastName + ", " + results[i].firstName).escapeHTML(), String(results[i].activeStudyProgrammes).escapeHTML(), String(results[i].inactiveStudyProgrammes).escapeHTML(), '', '', results[i].personId]);
              var rowElement = resultsTable.getRowElement(rowIndex);

              if (results[i].active == "false")
                rowElement.addClassName("searchStudentsFinishedStudentRow");
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
  
      /**
       * Invoked when the user submits the search form. We cancel the submit event
       * and delegate the work to the doSearch method.
       *
       * @param event The search form submit event
       */
      function onSearchStudents(event) {
        Event.stop(event);
        doSearch(0);
      }

      function setupTags() {
        JSONRequest.request("tags/getalltags.json", {
          onSuccess: function (jsonResponse) {
            new Autocompleter.Local("tags", "tags_choices", jsonResponse.tags, {
              tokens: [',', '\n', ' ']
            });
          }
        });   
      }

      function onLoad(event) {
        // Tabs
        
        var tabControl = new IxProtoTabs($('tabs'));
        
        $('searchForm').activeTab.value = tabControl.getActiveTab();
        tabControl.addListener(function (event) {
          if ((event.action == 'tabActivated')||(event.action == 'tabInitialized')) {
            $('searchForm').activeTab.value = event.name;
            $('activeTab').value = event.name;
          } 
        });
        <c:choose>
          <c:when test="${!empty param.activeTab}">
            tabControl.setActiveTab("${param.activeTab}");  
          </c:when>
        </c:choose>
        
        // Tags
        
        setupTags();

        // Search navigation
        
        new IxSearchNavigation($('searchResultsPagesContainer'), {
          id: 'searchResultsNavigation',
          maxNavigationPages: 19,
          onclick: function(event) {
            doSearch(event.page);
          }
        }); 

        // Search results table

        var searchResultsTable = new IxTable($('searchResultsTableContainer'), {
          id: 'searchResultsTable',
          columns : [{
            width: 30,
            left: 8,
            dataType: 'button',
            paramName: 'studentInfoButton',
            imgsrc: GLOBAL_contextPath + '/gfx/info.png',
            tooltip: '<fmt:message key="courses.createCourse.studentsTableStudentInfoTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var personId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              var button = table.getCellEditor(event.row, table.getNamedColumnIndex('studentInfoButton'));
              openStudentInfoPopupOnElement(button, personId);
            } 
          }, {
            header : '<fmt:message key="students.searchStudents.resultsTableNameHeader"/>',
            left: 38,
            width: 300,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.searchStudents.resultsTableActiveStudyProgrammeHeader"/>',
            left: 38 + 300 + 8,
            width: 300,
            dataType: 'text',
            editable: false
          }, {
            header : '<fmt:message key="students.searchStudents.resultsTableInactiveStudyProgrammeHeader"/>',
            left: 38 + 300 + 8 + 300 + 8,
            right: 60,
            dataType: 'text',
            editable: false
          }, {
            width: 30,
            right: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="students.searchStudents.studentTableViewTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var studentId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              redirectTo(GLOBAL_contextPath + '/students/viewstudent.page?person=' + studentId);
            }
          }, {
            width: 30,
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.searchStudents.studentTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var studentId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              redirectTo(GLOBAL_contextPath + '/students/editstudent.page?person=' + studentId);
            }
          }, {
            dataType: 'hidden',
            paramName: 'personId'
          }]
        });
      }

    </script>
  </head>
  <body onload="onLoad(event);">
  
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="students.searchStudents.pageTitle" /></h1>
  
    <div id="searchStudentsSearchContainer"> 
      <div class="genericFormContainer"> 
        <form method="post" id="searchForm" onsubmit="onSearchStudents(event);">
          <input type="hidden" name="activeTab" id="activeTab" value="basic"/>
  
          <div class="tabLabelsContainer" id="tabs">
	          <a class="tabLabel" href="#basic">
              <fmt:message key="students.searchStudents.tabLabelBasicSearch"/>
	          </a>
	
            <a class="tabLabel" href="#active">
              <fmt:message key="students.searchStudents.tabLabelActiveSearch"/>
            </a>

	          <a class="tabLabel" href="#advanced">
              <fmt:message key="students.searchStudents.tabLabelAdvancedSearch"/>
	          </a>
	        </div>
          
          <div id="basic" class="tabContent">
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.searchStudents.basicQueryTitle"/>
                <jsp:param name="helpLocale" value="students.searchStudents.basicQueryHelp"/>
              </jsp:include>                
              <input type="text" name="simpleQuery" class="basicSearchQueryField" size="40">
            </div>
      
            <div class="genericFormSubmitSection">
              <input type="submit" name="query" value="<fmt:message key="students.searchStudents.basicSearchButton"/>">
            </div>
          </div>

          <div id="active" class="tabContent">
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.searchStudents.basicActiveQueryTitle"/>
                <jsp:param name="helpLocale" value="students.searchStudents.basicActiveQueryHelp"/>
              </jsp:include>                
              <input type="text" name="simpleActiveQuery" class="basicSearchQueryField" size="40">
            </div>
      
            <div class="genericFormSubmitSection">
              <input type="submit" name="query" value="<fmt:message key="students.searchStudents.basicActiveSearchButton"/>">
            </div>
          </div>
         
          <div id="advanced" class="tabContent">
            <div id="searchStudentsAdvancedSearchCriterias">
              <div id="searchStudentsAdvancedSearchLeft">

                <div class="genericFormSection">  
                  <div class="searchStudentsAdvancedSearchPairFieldContainer">  
                    <div class="searchStudentsAdvancedSearchPairField1">
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchFirstnameTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchFirstnameHelp"/>
                      </jsp:include>                                    
                      <input type="text" name="firstname" size="30">
                    </div>
                    <div class="searchStudentsAdvancedSearchPairField2">
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchLastnameTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchLastnameHelp"/>
                      </jsp:include>                                    
                      <input type="text" name="lastname" size="30">
                    </div>
                  </div>
                </div>
                
                <div class="genericFormSection">  
                  <div class="searchStudentsAdvancedSearchPairFieldContainer">  
                    <div class="searchStudentsAdvancedSearchPairField1">
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchNicknameTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchNicknameHelp"/>
                      </jsp:include>                                    
                      <input type="text" name="nickname" size="30">
                    </div>
                    <div class="searchStudentsAdvancedSearchPairField2">
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchTagsTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchTagsHelp"/>
                      </jsp:include>
                      <input type="text" id="tags" name="tags" size="30"/>
                      <div id="tags_choices" class="autocomplete_choices"></div>
                    </div>
                  </div>
                </div>

                <div class="genericFormSection">  
                  <div class="searchStudentsAdvancedSearchPairFieldContainer">  
                    <div class="searchStudentsAdvancedSearchPairField1">
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchEmailTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchEmailHelp"/>
                      </jsp:include>                                    
                      <input type="text" name="email" size="30">
                    </div>
                    <div class="searchStudentsAdvancedSearchPairField2">
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchPhoneTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchPhoneHelp"/>
                      </jsp:include>                                    
                      <input type="text" name="phone" size="30">
                    </div>
                  </div>
                </div>
                
                <div class="genericFormSection"> 
                  <div class="searchStudentsAdvancedSearchPairFieldContainer">  
                    <div class="searchStudentsAdvancedSearchPairField1"> 
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchAddressStreetAddressTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchAddressStreetAddressHelp"/>
                      </jsp:include>                                     
                      <input type="text" name="addressStreetAddress" size="30">
                    </div>  
                    <div class="searchStudentsAdvancedSearchPairField2"> 
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchAddressPostalCodeTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchAddressPostalCodeHelp"/>
                      </jsp:include>                                    
                      <input type="text" name="addressPostalCode" size="30">
                    </div>  
                  </div>  
                  
                  <div class="searchStudentsAdvancedSearchPairFieldContainer">  
                    <div class="searchStudentsAdvancedSearchPairField1">  
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchAddressCityTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchAddressCityHelp"/>
                      </jsp:include>                                    
                      <input type="text" name="addressCity" size="30">
                    </div>  
                    <div class="searchStudentsAdvancedSearchPairField2">  
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchAddressCountryTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchAddressCountryHelp"/>
                      </jsp:include>                                    
                      <input type="text" name="addressCountry" size="30">
                    </div>  
                  </div>  
                </div>   
                
                <div class="genericFormSection"> 
                  <div class="searchStudentsAdvancedSearchPairFieldContainer">  
                    <div class="searchStudentsAdvancedSearchPairField1">  
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchSocialSecurityNumberTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchSocialSecurityNumberHelp"/>
                      </jsp:include>                                     
                      <input type="text" name="ssn" size="30">
                    </div>  
                    <div class="searchStudentsAdvancedSearchPairField2">  
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchEducationTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchEducationHelp"/>
                      </jsp:include>                                    
                      <input type="text" name="education" size="30">
                    </div>  
                  </div>    
                </div>
                
                <div class="genericFormSection"> 
                  <div class="searchStudentsAdvancedSearchPairFieldContainer">  
                    <div class="searchStudentsAdvancedSearchPairField1">  
                      <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                        <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchSexTitle"/>
                        <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchSexHelp"/>
                      </jsp:include>                                     
                      <select name="sex">
                        <option></option>
                        <option value="MALE"><fmt:message key="students.searchStudents.advancedSearchSexMaleOption"/></option>
                        <option value="FEMALE"><fmt:message key="students.searchStudents.advancedSearchSexFemaleOption"/></option>
                      </select>    
                    </div>
                    <div class="searchStudentsAdvancedSearchPairField2">  
                    </div>  
                  </div>    
                </div>
              </div>
              
              <div id="searchStudentsAdvancedSearchRight">

                <div class="genericFormSection">   
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchLodgingTitle"/>
                    <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchLodgingHelp"/>
                  </jsp:include>                                     
                  <select name="lodging">
                    <option></option>
                    <option value="0"><fmt:message key="students.searchStudents.advancedSearchLodgingNo"/></option>
                    <option value="1"><fmt:message key="students.searchStudents.advancedSearchLodgingYes"/></option>
                  </select>
                </div>

                <div class="genericFormSection">   
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchStudyProgrammeTitle"/>
                    <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchStudyProgrammeHelp"/>
                  </jsp:include>                                     
                  <select name="studyProgramme">
                    <option></option>
                    <c:forEach var="studyProgramme" items="${studyProgrammes}">
                      <option value="${studyProgramme.id}">${studyProgramme.name}</option>
                    </c:forEach>
                  </select>
                </div>
              
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchLanguageTitle"/>
                    <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchLanguageHelp"/>
                  </jsp:include>                                     
                  <select name="language">
                    <option></option>
                    <c:forEach var="language" items="${languages}">
                      <option value="${language.id}">${language.code} - ${language.name}</option>
                    </c:forEach>
                  </select>
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchMunicipalityTitle"/>
                    <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchMunicipalityHelp"/>
                  </jsp:include>                                     
                  <select name="municipality">
                    <option></option>
                    <c:forEach var="municipality" items="${municipalities}">
                      <option value="${municipality.id}"><fmt:formatNumber type="number" minIntegerDigits="3" value="${municipality.code}"/> - ${municipality.name}</option>
                    </c:forEach>
                  </select>
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchNationalityTitle"/>
                    <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchNationalityHelp"/>
                  </jsp:include>                                     
                  <select name="nationality">
                    <option></option>
                    <c:forEach var="nationality" items="${nationalities}">
                      <option value="${nationality.id}">
                        <fmt:formatNumber type="number" minIntegerDigits="3" value="${nationality.code}"/> - ${nationality.name}
                      </option>
                    </c:forEach>
                  </select>
                </div>

                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.searchStudents.advancedSearchArchiveTitle"/>
                    <jsp:param name="helpLocale" value="students.searchStudents.advancedSearchArchiveHelp"/>
                  </jsp:include>                                     
                  <select name="studentFilter">
                    <option value="SKIP_INACTIVE"><fmt:message key="students.searchStudents.advancedSearchSkipInactiveOption"/></option>
                    <option value="INCLUDE_INACTIVE"><fmt:message key="students.searchStudents.advancedSearchIncludeInactiveOption"/></option>
                    <option value="ONLY_INACTIVE"><fmt:message key="students.searchStudents.advancedSearchOnlyInactiveOption"/></option>
                  </select>
                </div>
                
              </div>
            </div>
            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="students.searchStudents.advancedSearchButton"/>">
            </div>

          </div>
        </form>
      </div>
    </div>
  
    <div id="searchResultsWrapper" style="display:none;">
      <div class="searchResultsTitle"><fmt:message key="modules.searchModules.resultsTitle"/></div>
      <div id="searchResultsContainer" class="searchResultsContainer">
        <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
        <div id="searchResultsTableContainer"></div>
        <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
      </div>
    </div>

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>