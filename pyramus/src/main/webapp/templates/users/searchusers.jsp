<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title><fmt:message key="users.searchUsers.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        new IxSearchNavigation($('searchResultsPagesContainer'), {
          id: 'searchResultsNavigation',
          maxNavigationPages: 19,
          onclick: function(event) {
            doSearch(event.page);
          }
        });
        new IxTable($('searchResultsTableContainer'), {
          id: 'searchResultsTable',
          columns : [ {
            header : '<fmt:message key="users.searchUsers.userTableNameHeader"/>',
            left: 8,
            dataType: 'text',
            editable: false
          }, {
            width: 30,
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="users.searchUsers.userTableEditUserTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var userId = table.getCellValue(event.row, table.getNamedColumnIndex('userId'));
              redirectTo(GLOBAL_contextPath + '/users/edituser.page?userId=' + userId);
            }
          }, {
            dataType: 'hidden',
            paramName: 'userId'
          }]
        });
      };

      /**
       * Performs the search and displays the results of the given page.
       *
       * @param page The results page to be shown after the search
       */
      function doSearch(page) {
        var searchForm = $("searchForm");
        JSONRequest.request("users/searchusers.json", {
          parameters: {
            text: searchForm.text.value,
            role: searchForm.role.value,
            page: page
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('searchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              var name = results[i].lastName + ", " + results[i].firstName;
              resultsTable.addRow([name.escapeHTML(), '', results[i].id]);
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
      function onSearchUsers(event) {
        Event.stop(event);
        doSearch(0);
      }

    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="users.searchUsers.pageTitle" /></h1>
    
    <div id="searchUsersSearchFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#searchUsers">
            <fmt:message key="users.searchUsers.tabLabelSearchUsers"/>
          </a>
        </div>
        
        <div id="searchUsers" class="tabContent">
          <form id="searchForm" method="post" onsubmit="onSearchUsers(event);">
      
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.searchUsers.textTitle"/>
                <jsp:param name="helpLocale" value="users.searchUsers.textHelp"/>
              </jsp:include>      
              <input type="text" name="text" size="40"/>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.searchUsers.roleTitle"/>
                <jsp:param name="helpLocale" value="users.searchUsers.roleHelp"/>
              </jsp:include>      
              <select name="role">
                <option></option>
                <option value="CLOSED"><fmt:message key="users.searchUsers.roleClosedTitle"/></option>
                <option value="GUEST"><fmt:message key="users.searchUsers.roleGuestTitle"/></option>
                <option value="USER"><fmt:message key="users.searchUsers.roleUserTitle"/></option>
                <option value="MANAGER"><fmt:message key="users.searchUsers.roleManagerTitle"/></option>
                <option value="ADMINISTRATOR"><fmt:message key="users.searchUsers.roleAdministratorTitle"/></option>
                <option value="TEACHER"><fmt:message key="users.searchUsers.roleTeacherTitle"/></option>
                <option value="STUDY_GUIDER"><fmt:message key="users.searchUsers.roleStudyGuiderTitle"/></option>
                <option value="STUDY_PROGRAMME_LEADER"><fmt:message key="users.searchUsers.roleStudyProgrammeLeaderTitle"/></option>
              </select>
            </div>
            
            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="users.searchUsers.searchButton"/>">
            </div>
      
          </form>
        </div>
      </div>
    </div>
    
    <div id="searchResultsWrapper" style="display:none;">
      <div class="searchResultsTitle"><fmt:message key="users.searchUsers.resultsTitle"/></div>
      <div id="searchResultsContainer" class="searchResultsContainer">
        <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
        <div id="searchResultsTableContainer"></div>
        <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
      </div>
    </div>
  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>