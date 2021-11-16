<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="projects.viewProject.pageTitle">
        <fmt:param value="${project.name}"/>
      </fmt:message>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/datefield_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <c:set var="projectOptionalModuleCount">0</c:set>
    <c:set var="projectMandatoryModuleCount">0</c:set>
    
    <c:forEach var="module" items="${project.projectModules}">
      <c:choose>
        <c:when test="${module.optionality eq 'MANDATORY'}">
          <c:set var="projectMandatoryModuleCount">${projectMandatoryModuleCount + 1}</c:set>
        </c:when>
        <c:when test="${module.optionality eq 'OPTIONAL'}">
          <c:set var="projectOptionalModuleCount">${projectOptionalModuleCount + 1}</c:set>
        </c:when>
      </c:choose>
    </c:forEach>

    <script type="text/javascript">
      function setupRelatedCommandsBasic(projectId) {
        var relatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="projects.viewProject.basicTabRelatedActionsLabel"/>'
        });
    
        relatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="projects.viewProject.basicTabRelatedActionEditProjectLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/projects/editproject.page?project=' + projectId);
          }
        }));
      }

      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        var projectId = ${project.id};
        setupRelatedCommandsBasic(projectId);
        
        var modulesTable = new IxTable($('modulesTableContainer'), {
          id : "modulesTable",
          columns : [ {
            header : '<fmt:message key="projects.viewProject.moduleTableNameHeader"/>',
            left : 8,
            dataType: 'text',
            editable: false,
            paramName: 'name',
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSTRINGSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSTRINGSORT
              }
            }
          }, {
            header : '<fmt:message key="projects.viewProject.moduleTableOptionalityHeader"/>',
            right : 8 + 22 + 8,
            width : 150,
            dataType : 'select',
            paramName: 'optionality',
            editable: false,
            options: [
              {text: '<fmt:message key="projects.viewProject.optionalityMandatory"/>', value: 0},
              {text: '<fmt:message key="projects.viewProject.optionalityOptional"/>', value: 1}
            ],
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSELECTSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSELECTSORT
              }
            },
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="projects.viewProject.modulesTableViewModuleTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
              redirectTo(GLOBAL_contextPath + '/modules/viewmodule.page?module=' + moduleId);
            }
          }, {
            dataType: 'hidden',
            paramName: 'moduleId'
          }, {
            dataType: 'hidden',
            paramName: 'projectModuleId'
          }]
        });

        var subjectCoursesTable = setupSubjectCoursesTable();
        
        JSONRequest.request("projects/getprojectmodules.json", {
          parameters: {
            project: projectId
          },
          onSuccess: function(jsonResponse) {
            var projectModules = jsonResponse.projectModules;
            var rows = new Array();
            for (var i = 0; i < projectModules.length; i++) {
              rows.push([
                  projectModules[i].name.escapeHTML(),
                  projectModules[i].optionality,
                  '',
                  projectModules[i].moduleId,
                  projectModules[i].id]);
            }
            modulesTable.addRows(rows);

            var subjectCourses = jsonResponse.projectSubjectCourses;
            rows = new Array();
            for (var i = 0; i < subjectCourses.length; i++) {
              rows.push([
                  subjectCourses[i].subjectId,
                  subjectCourses[i].courseNumber,
                  subjectCourses[i].optionality]);
            }
            subjectCoursesTable.addRows(rows);
          } 
        });

        var studentProjectsTable = new IxTable($('studentProjectsTableContainer'), {
          id : "studentProjectsTable",
          columns : [ {
            header : '<fmt:message key="projects.viewProject.studentProjectsTableStudentNameHeader"/>',
            left : 8,
            right: 8 + 22 + 8 + 22 + 8 + 110 + 8 + 110 + 8 + 150 + 8 + 150 + 8 + 110 + 8 + 150 + 8,
            dataType: 'text',
            editable: false,
            paramName: 'name',
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSTRINGSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSTRINGSORT
              }
            }
          }, {
            header : '<fmt:message key="projects.viewProject.studentProjectsTableStudyProgrammeNameHeader"/>',
            right: 8 + 22 + 8 + 22 + 8 + 110 + 8 + 110 + 8 + 150 + 8 + 150 + 8 + 110 + 8,
            width: 150,
            dataType: 'text',
            editable: false,
            paramName: 'studyProgrammeName',
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSTRINGSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSTRINGSORT
              }
            },
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.byNotValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(undefined, false)
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]
          }, {
            header : '<fmt:message key="projects.viewProject.studentProjectsTableOptionalityHeader"/>',
            right: 8 + 22 + 8 + 22 + 8 + 110 + 8 + 110 + 8 + 150 + 8 + 150 + 8,
            width : 110,
            dataType : 'select',
            paramName: 'optionality',
            editable: false,
            options: [
              {text: '<fmt:message key="projects.viewProject.optionalityMandatory"/>', value: "MANDATORY"},
              {text: '<fmt:message key="projects.viewProject.optionalityOptional"/>', value: "OPTIONAL"}
            ],
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSELECTSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSELECTSORT
              }
            },
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]
          }, {
            header : '<fmt:message key="projects.viewProject.studentProjectsTableAssessmentDateHeader"/>',
            right: 8 + 22 + 8 + 22 + 8 + 110 + 8 + 110 + 8 + 150 + 8,
            width: 150,
            dataType: 'text',
            editable: false,
            paramName: 'projectAssessment',
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSTRINGSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSTRINGSORT
              }
            }
          }, {
            header : '<fmt:message key="projects.viewProject.studentProjectsTableAssessmentGradeHeader"/>',
            right: 8 + 22 + 8 + 22 + 8 + 110 + 8 + 110 + 8,
            width: 150,
            dataType: 'text',
            editable: false,
            paramName: 'projectAssessment',
            sortAttributes: {
              sortAscending: {
                toolTip: '<fmt:message key="generic.sort.ascending"/>',
                sortAction: IxTable_ROWSTRINGSORT 
              },
              sortDescending: {
                toolTip: '<fmt:message key="generic.sort.descending"/>',
                sortAction: IxTable_ROWSTRINGSORT
              }
            }
          }, {
            header : '<fmt:message key="projects.viewProject.studentProjectsTableMandatoryCoursesHeader"><fmt:param value="${projectMandatoryModuleCount}"/></fmt:message>',
            headerTooltip: '<fmt:message key="projects.viewProject.studentProjectsTableMandatoryCoursesHeaderTooltip"/>',
            right: 8 + 22 + 8 + 22 + 8 + 110 + 8,
            width: 110,
            dataType: 'text',
            editable: false,
            paramName: 'name',
//             sortAttributes: {
//               sortAscending: {
//                 toolTip: '<fmt:message key="generic.sort.ascending"/>',
//                 sortAction: IxTable_ROWNUMBERSORT 
//               },
//               sortDescending: {
//                 toolTip: '<fmt:message key="generic.sort.descending"/>',
//                 sortAction: IxTable_ROWNUMBERSORT
//               }
//             },
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.byNotValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(undefined, false)
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]
          }, {
            header : '<fmt:message key="projects.viewProject.studentProjectsTableOptionalCoursesHeader"><fmt:param value="${projectOptionalModuleCount}"/></fmt:message>',
            headerTooltip: '<fmt:message key="projects.viewProject.studentProjectsTableOptionalCoursesHeaderTooltip"/>',
            right: 8 + 22 + 8 + 22 + 8,
            width: 110,
            dataType: 'text',
            editable: false,
            paramName: 'name',
//             sortAttributes: {
//               sortAscending: {
//                 toolTip: '<fmt:message key="generic.sort.ascending"/>',
//                 sortAction: IxTable_ROWNUMBERSORT 
//               },
//               sortDescending: {
//                 toolTip: '<fmt:message key="generic.sort.descending"/>',
//                 sortAction: IxTable_ROWNUMBERSORT
//               }
//             },
            contextMenu: [
              {
                text: '<fmt:message key="generic.filter.byValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER()
              },
              {
                text: '<fmt:message key="generic.filter.byNotValue"/>',
                onclick: new IxTable_ROWSTRINGFILTER(undefined, false)
              },
              {
                text: '<fmt:message key="generic.filter.clear"/>',
                onclick: new IxTable_ROWCLEARFILTER()
              }
            ]
          }, {
            width: 22,
            right: 8 + 22 + 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="projects.viewProject.studentProjectsTableViewStudentTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var personId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              redirectTo(GLOBAL_contextPath + '/students/viewstudent.page?person=' + personId);
            }
          }, {
            width: 22,
            right: 8,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="projects.viewProject.studentProjectsTableEditStudentProjectTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var studentProjectId = table.getCellValue(event.row, table.getNamedColumnIndex('studentProjectId'));
              redirectTo(GLOBAL_contextPath + '/projects/editstudentproject.page?studentproject=' + studentProjectId);
            }
          }, {
            dataType: 'hidden',
            paramName: 'personId'
          }, {
            dataType: 'hidden',
            paramName: 'studentProjectId'
          }]
        });

        // Student Projects paging
        
        new IxSearchNavigation($('searchResultsPagesContainer'), {
          id: 'studentProjectsNavigation',
          maxNavigationPages: 19,
          onclick: function(event) {
            doSearch(projectId, event.page);
          }
        }); 

        doSearch(projectId, 0);
      }

      function setupSubjectCoursesTable() {
        var subjects = JSDATA["subjects"].evalJSON();
        var subjectGroups = { "null": { subjects: [] } };
        var subjectGroupsIds = [ "null" ];
        
        // Group subjects under education types
        for (var i = 0, l = subjects.length; i < l; i++) {
          var educationTypeId = subjects[i].educationTypeId ? subjects[i].educationTypeId : "null";
          if (!subjectGroups[educationTypeId]) {
            subjectGroups[educationTypeId] = { name: subjects[i].educationTypeName, subjects: [] };
            subjectGroupsIds.push(educationTypeId);
          }
          subjectGroups[educationTypeId].subjects.push(subjects[i]);
        }
        
        var subjectOptions = [];
        for (var i = 0, l = subjectGroupsIds.length; i < l; i++) {
          var subjectGroupId = subjectGroupsIds[i];
          var subjectGroup = subjectGroups[subjectGroupId];
          if (subjectGroup && subjectGroup.subjects && subjectGroup.subjects.length > 0) {
            if (subjectGroupId != "null" && subjectGroup.name) {
              var subjectOptGroup = { optionGroup: true, text: subjectGroup.name, options: [] };
              
              for (var j = 0; j < subjectGroup.subjects.length; j++) {
                var option = subjectGroup.subjects[j];
                subjectOptGroup.options.push({
                  value: option.id,
                  text: getLocale().getText("generic.subjectFormatterNoEducationType", option.code, option.name)
                });
              }
              subjectOptions.push(subjectOptGroup);
            } else {
              // No opt group
              
              for (var j = 0; j < subjectGroup.subjects.length; j++) {
                var option = subjectGroup.subjects[j];
                subjectOptions.push({
                  value: option.id,
                  text: getLocale().getText("generic.subjectFormatterNoEducationType", option.code, option.name)
                });
              }
            }
          }
        }
        
        var subjectCoursesTable = new IxTable($('subjectCoursesTableContainer'), {
          id : "subjectCoursesTable",
          columns : [ {
            header : getLocale().getText("projects.generic.subjectCourseTable.subjectHeader"),
            left : 8,
            width : 200,
            dataType : 'select',
            editable : false,
            paramName : 'subjectId',
            options: subjectOptions
          }, {
            header : getLocale().getText("projects.generic.subjectCourseTable.courseNumberHeader"),
            left : 8 + 200 + 8,
            width : 100,
            dataType : 'number',
            editable : false,
            paramName : 'courseNumber'
          }, {
            header : getLocale().getText("projects.generic.table.optionalityHeader"),
            width : 150,
            right : 40,
            dataType : 'select',
            editable : false,
            paramName : 'optionality',
            options : [ {
              text : getLocale().getText("projects.generic.table.optionalityMandatory"),
              value : 0
            }, {
              text : getLocale().getText("projects.generic.table.optionalityOptional"),
              value : 1
            } ],
            sortAttributes : {
              sortAscending : {
                toolTip : getLocale().getText("generic.sort.ascending"),
                sortAction : IxTable_ROWSELECTSORT
              },
              sortDescending : {
                toolTip : getLocale().getText("generic.sort.descending"),
                sortAction : IxTable_ROWSELECTSORT
              }
            },
            contextMenu : [ {
              text : getLocale().getText("generic.action.copyValues"),
              onclick : new IxTable_COPYVALUESTOCOLUMNACTION(true)
            } ]
          } ]
        });

        return subjectCoursesTable;
      }
      
      function doSearch(projectId, page) {
        JSONRequest.request("projects/liststudentprojects.json", {
          parameters: {
            project: projectId,
            page: page,
            maxResults: 20
          },
          onSuccess: function(jsonResponse) {
            var studentProjects = jsonResponse.studentProjects;
            
            var studentProjectsArr = new Array();

            for (var i = 0; i < studentProjects.length; i++) {
              var studentProject = studentProjects[i];
              var datesText = "";
              for (var j = 0; j < studentProject.dates.length; j++) {
                if (datesText.length > 0)
                  datesText += ", ";

                var date = new Date();
                date.setTime(studentProject.dates[j]);
                // TODO: dateformatting is bad but the same as in ixtable
                datesText += date.getDate().toPaddedString(2) + '.' + (date.getMonth() + 1).toPaddedString(2) + '.' + date.getFullYear();
              }
              
              studentProjectsArr.push([
                studentProject.studentName,
                studentProject.studyProgrammeName,
                studentProject.optionality,
                datesText,
                studentProject.grades,
                studentProject.passedMandatory,
                studentProject.passedOptional,
                '',
                '',
                studentProject.personId,
                studentProject.studentProjectId
             ]);
            }

            var studentProjectsTable = getIxTableById('studentProjectsTable');

            studentProjectsTable.detachFromDom();
            studentProjectsTable.deleteAllRows();
            studentProjectsTable.addRows(studentProjectsArr);
            studentProjectsTable.reattachToDom();

            getSearchNavigationById('studentProjectsNavigation').setTotalPages(jsonResponse.pages);
            getSearchNavigationById('studentProjectsNavigation').setCurrentPage(jsonResponse.page);
            $('searchResultsStatusMessageContainer').innerHTML = jsonResponse.statusMessage;
          } 
        });
      }
      
    </script>
  </head>
  
  <body onLoad="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">
      <fmt:message key="projects.viewProject.pageTitle">
        <fmt:param value="${project.name}"/>
      </fmt:message>
    </h1>
      
    <div id="viewProjectEditFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#basic">
            <fmt:message key="projects.viewProject.tabLabelBasic"/>
          </a>
          <a class="tabLabel" href="#modules">
            <fmt:message key="projects.viewProject.tabLabelModules"/>
          </a>
          <a class="tabLabel" href="#studentProjects">
            <fmt:message key="projects.viewProject.tabLabelStudentProjects"/>
          </a>
        </div>

        <!--  Basic tab -->

        <div id="basic" class="tabContent">
          <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
          <!--  TODO italic tags to css -->

          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="projects.viewProject.createdTitle"/>
              <jsp:param name="helpLocale" value="projects.viewProject.createdHelp"/>
            </jsp:include>
            <span><i>${project.creator.fullName} <fmt:formatDate type="both" value="${project.created}"/></i></span>    
          </div>

          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="projects.viewProject.modifiedTitle"/>
              <jsp:param name="helpLocale" value="projects.viewProject.modifiedHelp"/>
            </jsp:include>
            <span><i>${project.lastModifier.fullName} <fmt:formatDate type="both" value="${project.lastModified}"/></i></span>    
          </div>

          <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="projects.viewProject.nameTitle"/>
                <jsp:param name="helpLocale" value="projects.viewProject.nameHelp"/>
              </jsp:include>
            <span>${fn:escapeXml(project.name)}</span>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="projects.viewProject.tagsTitle"/>
              <jsp:param name="helpLocale" value="projects.viewProject.tagsHelp"/>
            </jsp:include>
            <span>${fn:escapeXml(tags)}</span>
          </div>
      
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="projects.viewProject.descriptionTitle"/>
              <jsp:param name="helpLocale" value="projects.viewProject.descriptionHelp"/>
            </jsp:include>
            <span>${project.description}</span>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="projects.viewProject.optionalStudiesTitle"/>
              <jsp:param name="helpLocale" value="projects.viewProject.optionalStudiesHelp"/>
            </jsp:include>
            
            <span>${project.optionalStudiesLength.units} ${optionalStudiesLengthTimeUnit.name}</span> 
          </div>
        </div>

        <!-- Modules tab -->
        
        <div id="modules" class="tabContentixTableFormattedData">
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="projects.viewProject.modulesTitle"/>
              <jsp:param name="helpLocale" value="projects.viewProject.modulesHelp"/>
            </jsp:include>

            <div id="modulesContainer">
              <div id="modulesTableContainer"></div>
            </div>

            <div id="viewProjectModulesTotalContainer">
              <fmt:message key="projects.viewProject.modulesTotal"/> <span id="viewProjectModulesTotalValue"></span>
            </div>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="projects.viewProject.subjectCoursesTitle"/>
              <jsp:param name="helpLocale" value="projects.viewProject.subjectCoursesHelp"/>
            </jsp:include>

            <div id="subjectCoursesContainer">
              <div id="subjectCoursesTableContainer"></div>
            </div>
          </div>
          
        </div>
        
        <!-- StudentProjects tab -->
        
        <div id="studentProjects" class="tabContentixTableFormattedData">
          <div id="searchResultsWrapper">
            <div id="searchResultsContainer" class="tabbedSearchResultsContainer">
              <div id="searchResultsStatusMessageContainer" class="searchResultsMessageContainer"></div>
              <div id="studentProjectsTableContainer"></div>
              <div id="searchResultsPagesContainer" class="tabbedSearchResultsPagesContainer"></div>
            </div>
          </div>
        </div>

      </div>
    </div>

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>