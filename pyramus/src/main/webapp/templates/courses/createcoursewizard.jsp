<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="courses.createCourseWizard.pageTitle" /></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">

      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        new IxSearchNavigation($('moduleSearchResultsPagesContainer'), {
          id: 'moduleSearchResultsNavigation',
          maxNavigationPages: 9,
          onclick: function(event) {
            doModuleSearch(event.page);
          }
        });
        new IxTable($('moduleSearchResultsTableContainer'), {
          id: 'moduleSearchResultsTable',
          columns : [ {
            left: 10,
            right: 10,
            header : '<fmt:message key="courses.createCourseWizard.moduleTableNameHeader"/>',
            dataType: 'text',
            editable: false,
            onclick: function (event) {
              var table = event.tableComponent;
              table.setActiveRows([event.row]);
              $('selectedModuleId').value = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              $('createCourseButton').disabled = false;
            },
            paramName: 'name' 
          }, {
            dataType: 'hidden',
            paramName: 'moduleId'
          }]
        });

        var basicTabRelatedActionsHoverMenu = new IxHoverMenu($('basicTabRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="courses.createCourseWizard.basicTabRelatedActionsLabel"/>'
        });
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/list-add.png',
          text: '<fmt:message key="courses.createCourseWizard.basicTabRelatedActionsCreateModuleLabel"/>',
          link: GLOBAL_contextPath + '/modules/createmodule.page'  
        }));
      };
  
      /**
       * Performs the search of modules and displays the results of the given page.
       *
       * @param page The results page to be shown after the search
       */
      function doModuleSearch(page) {
        var searchForm = $("searchModulesForm");
        JSONRequest.request("modules/searchmodules.json", {
          parameters: {
            text: searchForm.text.value,
            page: page
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('moduleSearchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              resultsTable.addRow([results[i].name.escapeHTML(), results[i].id]);
            }
            resultsTable.reattachToDom();
            getSearchNavigationById('moduleSearchResultsNavigation').setTotalPages(jsonResponse.pages);
            getSearchNavigationById('moduleSearchResultsNavigation').setCurrentPage(jsonResponse.page);
            $('moduleSearchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
            $('moduleSearchResultsWrapper').setStyle({
              display: ''
            });
            $('createCourseButton').disabled = true;
          } 
        });
      }

      /**
       * Invoked when the user submits the module search form. We cancel the submit event
       * and delegate the work to the doProjectSearch method.
       *
       * @param event The search form submit event
       */
      function onSearchModules(event) {
        Event.stop(event);
        doModuleSearch(0);
      }

    </script>
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>

    <h1 class="genericPageHeader"><fmt:message key="courses.createCourseWizard.pageTitle" /></h1>

    <div id="createCourseCreateFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#createCourseWizard">
            <fmt:message key="courses.createCourseWizard.tabLabelCreateCourse"/>
          </a>
        </div>
        
        <div id="createCourseWizard" class="tabContent">
          <div id="basicTabRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
        
          <div class="genericInfoMessageContainer">
            <span><fmt:message key="courses.createCourseWizard.instructionMessage"/></span>
          </div>

          <div id="createCourseWizardModuleContainer">
            <div id="createCourseWizardSearchModulesTab"><fmt:message key="courses.createCourseWizard.searchModulesTabLabel"/></div>
            <div class="genericFormContainer" id="createCourseWizardModuleSearchFormContainer">
              <form id="searchModulesForm" method="post" onsubmit="onSearchModules(event);">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.createCourseWizard.textTitle"/>
                    <jsp:param name="helpLocale" value="courses.createCourseWizard.textHelp"/>
                  </jsp:include>
                  <input type="text" name="text" size="40"/>
                </div>
                <div class="genericFormSubmitSection">
                  <input type="submit" value="<fmt:message key="courses.createCourseWizard.searchModulesButton"/>">
                </div>
              </form>
              
              <div id="moduleSearchResultsWrapper" style="display:none;">
	              <div class="createCourseWizardSearchResultsTitle"><fmt:message key="courses.createCourseWizard.searchModulesResultsTitle"/></div>
	              <div id="moduleSearchResultsContainer" class="createCourseWizardSearchResultsContainer">
	                <div id="moduleSearchResultsStatusMessageContainer" class="createCourseWizardSearchResultsMessageContainer"></div>
	                <div id="moduleSearchResultsTableContainer"></div>
	                <div id="moduleSearchResultsPagesContainer" class="createCourseWizardSearchResultsPagesContainer"></div>
	              </div>
	            </div>
              
            </div>
          </div>
        </div>
    
        <!--  Create course project section -->          

        <div class="genericFormSubmitSectionOffTab">
          <form id="createCourseForm" method="get" action="createcourse.page">
            <input id="selectedModuleId" type="hidden" name="module" value="-1"/>
            <input id="createCourseButton" type="submit" disabled="disabled" value="<fmt:message key="courses.createCourseWizard.createCourseButton"/>"/>
          </form>
        </div>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>