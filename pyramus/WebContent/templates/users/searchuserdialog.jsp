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
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      var result = null;
    
      /**
       * Convenience method to return the row index of the given user in the given user table.
       *
       * @param tableId The table identifier
       * @param userId The user identifier
       *
       * @return The row index of the given user in the given user table. Returns -1 if not found.
       */
      function getUserRowIndex(tableId, userId) {
        var table = getIxTableById(tableId);
        if (table) {
          for (var i = 0; i < table.getRowCount(); i++) {
            var tableUserId = table.getCellValue(i, table.getNamedColumnIndex('userId'));
            if (tableUserId == userId) {
              return i;
            }
          }
        }
        return -1;
      }

      /**
       * Performs the search and displays the results of the given page.
       *
       * @param page The results page to be shown after the search
       */
      function doSearch(page) {
        var searchUsersForm = $("searchUserForm");
        JSONRequest.request("users/searchusers.json", {
          parameters: {
            text: searchUsersForm.name.value,
            page: page
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('searchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              var name = results[i].lastName + ", " + results[i].firstName;
              resultsTable.addRow([name.escapeHTML(), results[i].id]);
            }
            resultsTable.reattachToDom();
            getSearchNavigationById('searchResultsNavigation').setTotalPages(jsonResponse.pages);
            getSearchNavigationById('searchResultsNavigation').setCurrentPage(jsonResponse.page);
            $('modalSearchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
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

      /**
       * Returns the identifiers of the users selected in this dialog.
       *
       * @return The users selected in this dialog
       */
      function getResults() {
        return {
          user: result
        };
      }

      function setResult(userName, userId) {
        result = {name: userName, id: userId};
        getDialog().enableOkButton();
      }      
      
      /**
       * Called when this dialog loads. Initializes the search navigation and user tables.
       *
       * @param event The page load event
       */
      function onLoad(event) {
        new IxSearchNavigation($('modalSearchResultsPagesContainer'), {
          id: 'searchResultsNavigation',
          maxNavigationPages: 9,
          onclick: function(event) {
            doSearch(event.page);
          }
        });

        var searchResultsTable = new IxTable($('searchResultsTableContainer'), {
          id: 'searchResultsTable',
          columns : [{
            left: 8,
            right: 8,
            dataType: 'text',
            editable: false,
            selectable: false,
            paramName: 'name',
            onclick: function (event) {
              var table = event.tableComponent;
              var userId = table.getCellValue(event.row, table.getNamedColumnIndex('userId'));
              var userName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
              $$('.searchUserDialogTableSelectedRow').each(function (item) {
                item.removeClassName('searchUserDialogTableSelectedRow');
              });
              table.getRowElement(event.row).addClassName('searchUserDialogTableSelectedRow');
              setResult(userName, userId);
            }
          }, {
            dataType: 'hidden',
            paramName: 'userId'
          }]
        });
        searchResultsTable.domNode.addClassName("modalDialogSearchResultsIxTable");
        
        $('searchUserForm').name.focus();
      }
    </script>

  </head>
  <body onload="onLoad(event);">

    <div id="searchUserDialogSearchContainer" class="modalSearchContainer">
      <div class="modalSearchTabLabel"><fmt:message key="users.searchUserDialog.searchTitle"/></div> 
      <div class="modalSearchTabContent">
	      <div class="genericFormContainer"> 
	        
	        <form id="searchUserForm" method="post" onsubmit="onSearchUsers(event);">

	          <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.searchUserDialog.nameTitle"/>
                <jsp:param name="helpLocale" value="users.searchUserDialog.nameHelp"/>
              </jsp:include>                   
	            <input type="text" name="name" size="40"/>
	          </div>
  
	          <div class="genericFormSubmitSection">
	            <input type="submit" value="<fmt:message key="users.searchUserDialog.searchButton"/>"/>
	          </div>
	    
	        </form>
	      </div>
      </div>
      
      <div id="searchResultsContainer" class="modalSearchResultsContainer">
        <div class="modalSearchResultsTabLabel"><fmt:message key="users.searchUserDialog.searchResultsTitle"/></div>
        <div id="modalSearchResultsStatusMessageContainer" class="modalSearchResultsMessageContainer"></div>    
        <div id="searchResultsTableContainer" class="modalSearchResultsTabContent"></div>
        <div id="modalSearchResultsPagesContainer" class="modalSearchResultsPagesContainer"></div>
      </div>
    </div>
    
  </body>
</html>