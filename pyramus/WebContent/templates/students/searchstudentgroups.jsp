<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title><fmt:message key="students.searchStudentGroups.pageTitle"/></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
 
      function doSearch(page) {
        var searchForm = $("searchForm");
        JSONRequest.request("students/searchstudentgroups.json", {
          parameters: {
            text: searchForm.text.value,
            name: searchForm.name.value,
            tags: searchForm.tags.value,
            user: searchForm.user.value,
            description: searchForm.description.value,
            timeframeStart: searchForm.timeframeStart.value,
            timeframeEnd: searchForm.timeframeEnd.value,
            activeTab: searchForm.activeTab.value,
            page: page
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('searchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              var name = results[i].name;
              var nameExt = results[i].nameExtension;
              if (nameExt && (nameExt.length > 0))
                name += ' (' + nameExt + ')';
              resultsTable.addRow([name.escapeHTML(), results[i].beginDate, '', '', '', results[i].id]);
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
     function onSearch(event) {
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

        setupTags();
        
        new IxSearchNavigation($('searchResultsPagesContainer'), {
          id: 'searchResultsNavigation',
          maxNavigationPages: 19,
          onclick: function(event) {
            doSearch(event.page);
          }
        });

        var searchResultsTable = new IxTable($('searchResultsTableContainer'), {
          id: 'searchResultsTable',
          columns : [ {
            header : '<fmt:message key="students.searchStudentGroups.resultsTableNameHeader"/>',
            left: 8,
            right: 506,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            header : '<fmt:message key="students.searchStudentGroups.resultsTableBeginDateHeader"/>',
            right: 218,
            width : 180,
            dataType : 'date',
            editable: false
          }, {
            width: 30,
            right : 60,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="students.searchStudentGroups.resultsTableViewRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var studentGroupId = table.getCellValue(event.row, table.getNamedColumnIndex('studentGroupId'));
              redirectTo(GLOBAL_contextPath + '/students/viewstudentgroup.page?studentgroup=' + studentGroupId);
            } 
          }, {
            width: 30,
            right : 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.searchStudentGroups.resultsTableEditRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var studentGroupId = table.getCellValue(event.row, table.getNamedColumnIndex('studentGroupId'));
              redirectTo(GLOBAL_contextPath + '/students/editstudentgroup.page?studentgroup=' + studentGroupId);
            } 
          }, {
            width: 30,
            right : 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="students.searchStudentGroups.resultsTableArchiveRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var studentGroupId = table.getCellValue(event.row, table.getNamedColumnIndex('studentGroupId'));
              var studentGroupName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=students.searchStudentGroups.archiveConfirmDialogContent&localeParams=" + encodeURIComponent(studentGroupName);
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="students.searchStudentGroups.archiveConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="students.searchStudentGroups.archiveConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="students.searchStudentGroups.archiveConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener( function(event) {
                var dlg = event.dialog;
            
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("students/archivestudentgroup.json", {
                      parameters: {
                        studentGroupId: studentGroupId
                      },
                      onSuccess: function (jsonResponse) {
                        var currentPage = getSearchNavigationById('searchResultsNavigation').getCurrentPage();
                        doSearch(currentPage);
                      }
                    });   
                  break;
                }
              });
            
              dialog.open();
            }
          }, {
            dataType: 'hidden',
            paramName: 'studentGroupId'
          }]
        });
      };
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="students.searchStudentGroups.pageTitle" /></h1>
    
    <div id="searchStudentGroupsFormContainer"> 
      <div class="genericFormContainer"> 
        <form id="searchForm" method="post" onsubmit="onSearch(event);">

          <input type="hidden" name="activeTab" id="activeTab" value="basic"/>
  
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
             <fmt:message key="students.searchStudentGroups.tabLabelBasicSearch"/>
            </a>
            <a class="tabLabel" href="#advanced">
             <fmt:message key="students.searchStudentGroups.tabLabelAdvancedSearch"/>
            </a>
          </div>
  
          <div id="basic" class="tabContent">

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.searchStudentGroups.textTitle"/>
                <jsp:param name="helpLocale" value="students.searchStudentGroups.textHelp"/>
              </jsp:include>
              <input type="text" name="text" size="40">
            </div>

            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="students.searchStudentGroups.basicSearchButton"/>">
            </div>

          </div>

          <div id="advanced" class="tabContent">
            <div id="searchStudentGroupsAdvancedSearchCriterias">

              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.searchStudentGroups.nameTitle"/>
                  <jsp:param name="helpLocale" value="students.searchStudentGroups.nameHelp"/>
                </jsp:include>
                <input type="text" name="name" size="40">
              </div>

              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.searchStudentGroups.descriptionTitle"/>
                  <jsp:param name="helpLocale" value="students.searchStudentGroups.descriptionHelp"/>
                </jsp:include>
                <input type="text" name="description" size="40">
              </div>

              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.searchStudentGroups.tagsTitle"/>
                  <jsp:param name="helpLocale" value="students.searchStudentGroups.tagsHelp"/>
                </jsp:include>
                <input type="text" id="tags" name="tags" size="40"/>
                <div id="tags_choices" class="autocomplete_choices"></div>
              </div>

              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.searchStudentGroups.userTitle"/>
                  <jsp:param name="helpLocale" value="students.searchStudentGroups.userHelp"/>
                </jsp:include>
                <select name="user">
                  <option value="-1"></option>
                  <c:forEach var="user" items="${users}">
                    <option value="${user.id}">${user.lastName}, ${user.firstName}</option>
                  </c:forEach>
                </select>
              </div>

              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.searchStudentGroups.timeframeTitle"/>
                  <jsp:param name="helpLocale" value="students.searchStudentGroups.timeframeHelp"/>
                </jsp:include>
  
                <div class="searchStudentGroupsTimeFrameContainer">
                  <div class="searchStudentGroupsTimeFrameStartContainer"> <input type="text" name="timeframeStart" class="ixDateField"/> </div> 
                  <div class="searchStudentGroupsTimeFrameHyphenContainer">-</div> 
                  <div class="searchStudentGroupsTimeFrameEndContainer"> <input type="text" name="timeframeEnd" class="ixDateField"/> </div>
                </div>
              </div>
            </div>

            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="students.searchStudentGroups.advancedSearchButton"/>">
            </div>
          </div>
        </form>
      </div>
    </div>
    
    <div id="searchResultsWrapper" style="display:none;">
      <div class="searchResultsTitle"><fmt:message key="students.searchStudentGroups.resultsTitle"/></div>
      <div id="searchResultsContainer" class="searchResultsContainer">
        <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
        <div id="searchResultsTableContainer"></div>
        <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>