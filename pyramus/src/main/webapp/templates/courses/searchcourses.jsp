<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title><fmt:message key="courses.searchCourses.pageTitle"/></title>

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
        
        var educationTypeParam = searchForm.educationType.value;
        var educationType = ""; 
        var educationSubtype = "";
        
        if (educationTypeParam.startsWith("et:")) {
          educationType = educationTypeParam.substring(3); 
        } else if (educationTypeParam.startsWith("st:")) {
          educationSubtype = educationTypeParam.substring(3);
        }
        
        JSONRequest.request("courses/searchcourses.json", {
          parameters: {
            text: searchForm.text.value,
            name: searchForm.name.value,
            tags: searchForm.tags.value,
            nameExtension: searchForm.nameExtension.value,
            state: searchForm.state.value,
            subject: searchForm.subject.value,
            description: searchForm.description.value,
            timeframeMode: searchForm.timeframeMode.value,
            timeframeStart: searchForm.timeframeStart.value,
            timeframeEnd: searchForm.timeframeEnd.value,
            educationType: educationType, 
            educationSubtype: educationSubtype, 
            courseTemplateFilter: searchForm.courseTemplateFilter.value,
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
              resultsTable.addRow([name.escapeHTML(), results[i].beginDate, results[i].endDate, '', '', results[i].id]);
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
     function onSearchCourses(event) {
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
            header : '<fmt:message key="courses.searchCourses.courseTableNameHeader"/>',
            left: 8,
            right: 476,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            header : '<fmt:message key="courses.searchCourses.courseTableBeginDateHeader"/>',
            right: 218,
            width : 150,
            dataType : 'date',
            editable: false
          }, {
            header : '<fmt:message key="courses.searchCourses.courseTableEndDateHeader"/>',
            width: 150,
            right : 60,
            dataType : 'date',
            editable: false
          }, {
            width: 30,
            right : 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="courses.searchCourses.courseTableViewRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
              redirectTo(GLOBAL_contextPath + '/courses/viewcourse.page?course=' + courseId);
            } 
          }, {
            width: 30,
            right : 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="courses.searchCourses.courseTableEditRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
              redirectTo(GLOBAL_contextPath + '/courses/editcourse.page?course=' + courseId);
            } 
          }, {
            dataType: 'hidden',
            paramName: 'courseId'
          }]
        });
      };
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="courses.searchCourses.pageTitle" /></h1>
    
    <div id="searchCoursesSearchFormContainer"> 
      <div class="genericFormContainer"> 
        <form id="searchForm" method="post" onsubmit="onSearchCourses(event);">

          <input type="hidden" name="activeTab" id="activeTab" value="basic"/>
  
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
             <fmt:message key="courses.searchCourses.tabLabelBasicSearch"/>
            </a>
            <a class="tabLabel" href="#advanced">
             <fmt:message key="courses.searchCourses.tabLabelAdvancedSearch"/>
            </a>
          </div>
  
          <div id="basic" class="tabContent">

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.searchCourses.textTitle"/>
                <jsp:param name="helpLocale" value="courses.searchCourses.textHelp"/>
              </jsp:include>
              <input type="text" name="text" size="40">
            </div>

            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="courses.searchCourses.basicSearchButton"/>">
            </div>

          </div>
           
          <div id="advanced" class="tabContent">
            <div id="searchCoursesAdvancedSearchCriterias">

              <div id="searchCoursesAdvancedSearchLeft">

                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.nameTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.nameHelp"/>
                  </jsp:include>
                  <input type="text" name="name" size="40">
                </div>

                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.tagsTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.tagsHelp"/>
                  </jsp:include>
		              <input type="text" id="tags" name="tags" size="40"/>
		              <div id="tags_choices" class="autocomplete_choices"></div>
                </div>

                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.nameExtensionTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.nameExtensionHelp"/>
                  </jsp:include>
                  <input type="text" name="nameExtension" size="40">
                </div>

                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.stateTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.stateHelp"/>
                  </jsp:include>    
                  <select name="state">           
                    <option></option>           
                    <c:forEach var="state" items="${states}">
                      <option value="${state.id}">${state.name}</option> 
                    </c:forEach>
                  </select>
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.subjectTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.subjectHelp"/>
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
              
              <div id="searchCoursesAdvancedSearchRight">

                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.descriptionTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.descriptionHelp"/>
                  </jsp:include>
                  <input type="text" name="description" size="40">
                </div>

                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.timeFilterModeTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.timeFilterModeHelp"/>
                  </jsp:include>
    
                  <div class="searchCoursesTimeFilterModeContainer">
                    <select name="timeframeMode">
                      <option value="INCLUSIVE"><fmt:message key="courses.searchCourses.timeframeModeInclusive"/></option>
                      <option value="EXCLUSIVE"><fmt:message key="courses.searchCourses.timeframeModeExclusive"/></option>
                    </select>
                  </div>
                </div>
                
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.timeframeTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.timeframeHelp"/>
                  </jsp:include>
    
                  <div class="searchCoursesTimeFrameContainer">
                    <div class="searchCoursesTimeFrameStartContainer"> <input type="text" name="timeframeStart" class="ixDateField"/> </div> 
                    <div class="searchCoursesTimeFrameHyphenContainer">-</div> 
                    <div class="searchCoursesTimeFrameEndContainer"> <input type="text" name="timeframeEnd" class="ixDateField"/> </div>
                  </div>
                </div>

                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.educationTypeTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.educationTypeHelp"/>
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

                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="courses.searchCourses.courseTemplateFilterTitle"/>
                    <jsp:param name="helpLocale" value="courses.searchCourses.courseTemplateFilterHelp"/>
                  </jsp:include>    
                  <select name="courseTemplateFilter">
                    <option value="LIST_ALL"><fmt:message key="courses.searchCourses.filter.listAll" /></option>
                    <option value="LIST_COURSES"><fmt:message key="courses.searchCourses.filter.listCourses" /></option>
                    <option value="LIST_TEMPLATES"><fmt:message key="courses.searchCourses.filter.listTemplates" /></option>
                  </select>
                </div>
                
              </div>

            </div>

            <div class="genericFormSubmitSection">
              <input type="submit" value="<fmt:message key="courses.searchCourses.advancedSearchButton"/>">
            </div>
      
          </div>
        </form>
      </div>
    </div>
    
    <div id="searchResultsWrapper" style="display:none;">
      <div class="searchResultsTitle"><fmt:message key="courses.searchCourses.resultsTitle"/></div>
      <div id="searchResultsContainer" class="searchResultsContainer">
        <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
        <div id="searchResultsTableContainer"></div>
        <div id="searchResultsPagesContainer" class="searchResultsPagesContainer"></div>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>