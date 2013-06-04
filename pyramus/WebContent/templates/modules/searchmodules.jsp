<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title><fmt:message key="modules.searchModules.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">

      /**
       * Performs the search and displays the results of the given page.
       *
       * @param page The results page to be shown after the search
       */
      function doSearch(page) {
        var searchForm = $("searchForm");
        
        var educationTypeParam = searchForm.educationType.value;
        var educationType = ""; 
        var educationSubtype = "";
        
        if (educationTypeParam.startsWith("et:")) {
          educationType = educationTypeParam.substring(3); 
        } else if (educationTypeParam.startsWith("st:")) {
          educationSubtype = educationTypeParam.substring(3);
        }

        JSONRequest.request("modules/searchmodules.json", {
          parameters: {
            text: searchForm.text.value,
            name: searchForm.name.value,
            tags: searchForm.tags.value,
            subject: searchForm.subject.value,
            description: searchForm.description.value,
            educationType: educationType, 
            educationSubtype: educationSubtype, 
            activeTab: searchForm.activeTab.value,
            page: page
          },
          onSuccess: function (jsonResponse) {
            var resultsTable = getIxTableById('searchResultsTable');
            resultsTable.detachFromDom();
            resultsTable.deleteAllRows();
            var results = jsonResponse.results;
            for (var i = 0; i < results.length; i++) {
              resultsTable.addRow([results[i].name.escapeHTML(), '', '', '', '', results[i].id]);
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
      function onSearchModules(event) {
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

        setupTags();
        
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
            paramName: 'name',
            left: 8,
            dataType: 'text',
            editable: false
          }, {
            width: 30,
            right: 90,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="modules.searchModules.modulesTableViewModuleTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              redirectTo(GLOBAL_contextPath + '/modules/viewmodule.page?module=' + moduleId);
            }
          }, {
            width: 30,
            right: 60,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="modules.searchModules.modulesTableEditModuleTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              redirectTo(GLOBAL_contextPath + '/modules/editmodule.page?module=' + moduleId);
            }
          }, {
            width: 30,
            right : 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="modules.searchModules.modulesTableCreateCourseTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              redirectTo(GLOBAL_contextPath + '/courses/createcourse.page?module=' + moduleId);
            }
          }, {
            width: 30,
            right : 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="modules.searchModules.modulesTableArchiveModuleTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              var moduleName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=modules.searchModules.moduleArchiveConfirmDialogContent&localeParams=" + encodeURIComponent(moduleName);
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="modules.searchModules.moduleArchiveConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="modules.searchModules.moduleArchiveConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="modules.searchModules.moduleArchiveConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener( function(event) {
                var dlg = event.dialog;
            
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("modules/archivemodule.json", {
                      parameters: {
                        moduleId: moduleId
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
            paramName: 'moduleId'
          }]
        });
      };
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="modules.searchModules.pageTitle" /></h1>
    
    <div id="searchModulesSearchFormContainer"> 
      <div class="genericFormContainer">
       
        <form id="searchForm" method="post" onsubmit="onSearchModules(event);">
          <input type="hidden" name="activeTab" id="activeTab" value="basic"/>

          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="modules.searchModules.tabLabelSearchModules"/>
            </a>
    
            <a class="tabLabel" href="#advanced">
              <fmt:message key="modules.searchModules.tabLabelSearchModulesAdvanced"/>
            </a>
          </div>
          
          <div id="basic" class="tabContent">
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.searchModules.textTitle"/>
                <jsp:param name="helpLocale" value="modules.searchModules.textHelp"/>
              </jsp:include>
              <input type="text" name="text" size="40">
            </div>
            
            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="modules.searchModules.searchButton"/>">
            </div>
          </div>
    
    
          <div id="advanced" class="tabContent">
            <div id="searchModulesAdvancedSearchCriterias">
    
              <div id="searchModulesAdvancedSearchLeft">
    
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.searchModules.nameTitle"/>
                    <jsp:param name="helpLocale" value="modules.searchModules.nameHelp"/>
                  </jsp:include>
                  <input type="text" name="name" size="40">
                </div>
    
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.searchModules.tagsTitle"/>
                    <jsp:param name="helpLocale" value="modules.searchModules.tagsHelp"/>
                  </jsp:include>
                  <input type="text" id="tags" name="tags" size="40"/>
                  <div id="tags_choices" class="autocomplete_choices"></div>
                </div>
    
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.searchModules.subjectTitle"/>
                    <jsp:param name="helpLocale" value="modules.searchModules.subjectHelp"/>
                  </jsp:include>
                  <select name="subject">
                    <option></option>           
                    <c:forEach var="educationType" items="${educationTypes}">
                      <c:if test="${subjectsByEducationType[educationType.id] ne null}">
                        <optgroup label="${educationType.name}">
                          <c:forEach var="subject" items="${subjectsByEducationType[educationType.id]}">
                            <c:choose>
                              <c:when test="${empty subject.code}">
                                <c:set var="subjectName">${subject.name}</c:set>
                              </c:when>
                              <c:otherwise>
                                <c:set var="subjectName">
                                  <fmt:message key="generic.subjectFormatterNoEducationType">
                                    <fmt:param value="${subject.code}"/>
                                    <fmt:param value="${subject.name}"/>
                                  </fmt:message>
                                </c:set>
                              </c:otherwise>
                            </c:choose>
    
                            <option value="${subject.id}">${subjectName}</option> 
                          </c:forEach>
                        </optgroup>
                      </c:if>
                    </c:forEach>
    
                    <c:forEach var="subject" items="${subjectsByNoEducationType}">
                      <c:choose>
                        <c:when test="${empty subject.code}">
                          <c:set var="subjectName">${subject.name}</c:set>
                        </c:when>
                        <c:otherwise>
                          <c:set var="subjectName">
                            <fmt:message key="generic.subjectFormatterNoEducationType">
                              <fmt:param value="${subject.code}"/>
                              <fmt:param value="${subject.name}"/>
                            </fmt:message>
                          </c:set>
                        </c:otherwise>
                      </c:choose>
    
                      <option value="${subject.id}">${subjectName}</option> 
                    </c:forEach>
                  </select>
                </div>
    
              </div>
              
              <div id="searchModulesAdvancedSearchRight">
    
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.searchModules.descriptionTitle"/>
                    <jsp:param name="helpLocale" value="modules.searchModules.descriptionHelp"/>
                  </jsp:include>
                  <input type="text" name="description" size="40">
                </div>
    
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.searchModules.educationTypeTitle"/>
                    <jsp:param name="helpLocale" value="modules.searchModules.educationTypeHelp"/>
                  </jsp:include>
                  <select name="educationType">
                    <option></option>           
                    <c:forEach var="educationType" items="${educationTypes}">
                      <option value="et:${educationType.id}">${educationType.name}</option>
                       
                      <c:if test="${educationSubtypes[educationType.id] ne null}">
                        <optgroup>
                          <c:forEach var="subtype" items="${educationSubtypes[educationType.id]}">
                            <option value="st:${subtype.id}">${subtype.name}</option> 
                          </c:forEach>
                        </optgroup>
                      </c:if>
                    </c:forEach>
                  </select>
                </div>
    
              </div>
    
            </div>
    
            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="modules.searchModules.advancedSearchButton"/>">
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