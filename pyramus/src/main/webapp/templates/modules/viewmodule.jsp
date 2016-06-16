<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="modules.viewModule.pageTitle">
        <fmt:param value="${module.name}"/>
      </fmt:message>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/datefield_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/studentinfopopup_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>

    <script type="text/javascript">
      function onLoad(event) {
        new IxProtoTabs($('tabs'));
        
        // Related actions
        
        var basicTabRelatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="modules.viewModule.basicTabRelatedActionsLabel"/>'
        });
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="modules.viewModule.basicTabRelatedActionsEditModuleLabel"/>',
          link: GLOBAL_contextPath + '/modules/editmodule.page?module=${module.id}'  
        }));
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/list-add.png',
          text: '<fmt:message key="modules.viewModule.basicTabRelatedActionsCreateCourseLabel"/>',
          link: GLOBAL_contextPath + '/courses/createcourse.page?module=${module.id}'  
        }));
        
        // Courses table

        var coursesTable = new IxTable($('viewModuleCoursesTableContainer'), {
          id : "coursesTable",
          columns : [{
            dataType: 'hidden', 
            paramName: 'courseId'
          }, {
            header : '<fmt:message key="modules.viewModule.coursesTableNameHeader"/>',
            left : 8,
            right : 900,
            dataType : 'text',
            paramName: 'courseName',
            editable: false,
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
            width: 30,
            right: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: '<fmt:message key="modules.viewModule.coursesTableViewCourseTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
              redirectTo(GLOBAL_contextPath + '/courses/viewcourse.page?course=' + courseId);
            }
          }, {
            width: 30,
            right: 0,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="modules.viewModule.coursesTableEditCourseTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var courseId = table.getCellValue(event.row, table.getNamedColumnIndex('courseId'));
              redirectTo(GLOBAL_contextPath + '/courses/editcourse.page?course=' + courseId);
            }
          }]        
        });
        var rows = new Array();
        <c:forEach var="course" items="${courses}">
          <c:choose>
            <c:when test="${fn:length(course.nameExtension) gt 0}">
              <c:set var="courseName">${course.name} (${course.nameExtension})</c:set>
            </c:when>
            <c:otherwise>
              <c:set var="courseName">${course.name}</c:set>
            </c:otherwise>
          </c:choose>
          rows.push([
            ${course.id},
            "${fn:escapeXml(courseName)}",
            '',
            ''
          ]);
        </c:forEach>
        coursesTable.addRows(rows);
        if (coursesTable.getRowCount() > 0) {
          $('viewModuleCoursesTotalValue').innerHTML = coursesTable.getRowCount(); 
        }
      }
    </script>

  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    <h1 class="genericPageHeader">
      <fmt:message key="modules.viewModule.pageTitle">
        <fmt:param value="${module.name}"/>
      </fmt:message>
    </h1>
  
    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic"><fmt:message key="modules.viewModule.basicTabTitle" /></a>
        <a class="tabLabel" href="#components"><fmt:message key="modules.viewModule.componentsTabTitle" /></a> 
        <a class="tabLabel" href="#courses"><fmt:message key="modules.viewModule.coursesTabTitle" /></a>
      </div>
        
        <div id="basic" class="tabContent">
          <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
          
          <!--  Module Basic Info Starts -->

          <div class="genericViewInfoWapper" id="moduleViewBasicInfoWrapper">
          
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.viewModule.createdTitle" />
                <jsp:param name="helpLocale" value="modules.viewModule.createdHelp" />
              </jsp:include>
              <div class="genericViewFormDataText"><i>${module.creator.fullName} <fmt:formatDate type="both" value="${module.created}"/></i></div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.viewModule.modifiedTitle" />
                <jsp:param name="helpLocale" value="modules.viewModule.modifiedHelp" />
              </jsp:include>
              <div class="genericViewFormDataText"><i>${module.lastModifier.fullName} <fmt:formatDate type="both" value="${module.lastModified}"/></i></div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.viewModule.nameTitle" />
                <jsp:param name="helpLocale" value="modules.viewModule.nameHelp" />
              </jsp:include>
              <div class="genericViewFormDataText">${module.name}</div>
            </div>
            
            <c:if test="${module.maxParticipantCount ne null}">
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="modules.viewModule.maxParticipantsTitle"/>
                  <jsp:param name="helpLocale" value="modules.viewModule.maxParticipantsHelp"/>
                </jsp:include>    
              
                <div class="genericViewFormDataText">${module.maxParticipantCount}</div>
              </div>
            </c:if>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.viewModule.descriptionTitle" />
                <jsp:param name="helpLocale" value="modules.viewModule.descriptionHelp" />
              </jsp:include>
              <div class="genericViewFormDataText">${module.description}</div>
            </div>

            <c:forEach var="mDesc" items="${moduleDescriptions}">
              <div class="genericFormSection">
                <c:set var="descLocaleText">
                  <fmt:message key="modules.viewModule.descriptionTitle"/> (${mDesc.category.name})
                </c:set>
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleText" value="${descLocaleText}" />
                  <jsp:param name="helpLocale" value="modules.viewModule.descriptionHelp" />
                </jsp:include>
                <div class="genericViewFormDataText">${mDesc.description}</div>
              </div>
            </c:forEach>
            
          </div>

          <!--  Module Basic Info Ends, Module Detailed Info Starts -->

          <div class="genericViewInfoWapper" id="moduleViewDetailedInfoWrapper">
            
            <c:choose>
              <c:when test="${not empty module.tags}">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.viewModule.tagsTitle" />
                    <jsp:param name="helpLocale" value="modules.viewModule.tagsHelp" />
                  </jsp:include>
                  <div class="genericViewFormDataText">
                  <c:forEach var="tag" items="${module.tags}" varStatus="vs">
                    <c:out value="${tag.text}"/>
                    <c:if test="${not vs.last}"><c:out value=" "/></c:if>
                  </c:forEach>
                  </div>
                </div>
              </c:when>
            </c:choose> 
    
            <c:choose>
              <c:when test="${fn:length(module.courseEducationTypes) gt 0}">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.viewModule.educationTypesTitle" />
                    <jsp:param name="helpLocale" value="modules.viewModule.educationTypesHelp" />
                  </jsp:include>
                  <div class="genericViewFormDataText">
                  <c:forEach var="courseEducationType" items="${module.courseEducationTypes}">
                    <c:forEach var="courseEducationSubtype" items="${courseEducationType.courseEducationSubtypes}">
                      <div>${courseEducationType.educationType.name} - ${courseEducationSubtype.educationSubtype.name}</div>
                    </c:forEach>
                  </c:forEach>
                  </div>
                </div>
              </c:when>
            </c:choose>
    
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.viewModule.subjectTitle" />
                <jsp:param name="helpLocale" value="modules.viewModule.subjectHelp" />
              </jsp:include>
              <div class="genericViewFormDataText">
                <c:choose>
                  <c:when test="${module.subject.educationType ne null and not empty module.subject.code}">
                    <fmt:message key="generic.subjectFormatterWithEducationType">
                      <fmt:param value="${module.subject.code}"/>
                      <fmt:param value="${module.subject.name}"/>
                      <fmt:param value="${module.subject.educationType.name}"/>
                    </fmt:message>
                  </c:when>
                  <c:when test="${module.subject.educationType ne null and empty module.subject.code}">
                    <fmt:message key="generic.subjectFormatterNoSubjectCode">
                      <fmt:param value="${module.subject.name}"/>
                      <fmt:param value="${module.subject.educationType.name}"/>
                    </fmt:message>
                  </c:when>
                  <c:when test="${course.subject.educationType eq null and not empty course.subject.code}">
                    <fmt:message key="generic.subjectFormatterNoEducationType">
                      <fmt:param value="${module.subject.code}"/>
                      <fmt:param value="${module.subject.name}"/>
                    </fmt:message>
                  </c:when>
                  <c:otherwise>
                    ${module.subject.name}
                  </c:otherwise>
                </c:choose>
              </div>
            </div>

            <c:choose>
              <c:when test="${module.curriculum ne null}">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.viewModule.curriculumTitle" />
                    <jsp:param name="helpLocale" value="modules.viewModule.curriculumHelp" />
                  </jsp:include>
                  <div class="genericViewFormDataText">${module.curriculum.name}</div>
                </div>
              </c:when>
            </c:choose> 
    
            <c:choose>
              <c:when test="${module.courseNumber ne null}">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.viewModule.courseNumberTitle" />
                    <jsp:param name="helpLocale" value="modules.viewModule.courseNumberHelp" />
                  </jsp:include>
                  <div class="genericViewFormDataText">${module.courseNumber}</div>
                </div>
              </c:when>
            </c:choose> 
            
            <c:choose>
              <c:when test="${module.courseLength ne null}">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="modules.viewModule.lengthTitle" />
                    <jsp:param name="helpLocale" value="modules.viewModule.lengthHelp" />
                  </jsp:include>
                  <div class="genericViewFormDataText">${module.courseLength.units} ${module.courseLength.unit.name}</div>
                </div>
              </c:when>
            </c:choose> 
  
        </div>

        <!--  Module Detailed Info Ends -->

      </div>
  
      <div id="components" class="tabContent hiddenTab">
        <c:choose>
          <c:when test="${fn:length(moduleComponents) le 0}">
            <div class="genericTableNotAddedMessageContainer">
              <fmt:message key="modules.viewModule.noModuleComponents" />
            </div>
          </c:when>
          <c:otherwise>
            <div class="viewModuleComponentsContainer">
              <div class="viewModuleComponentsTitlesContainer">
                <div class="viewModuleComponentsNameTitle">
                  <fmt:message key="modules.viewModule.componentsNameHeader"/>
                </div>
                <div class="viewModuleComponentsLengthTitle">
                  <fmt:message key="modules.viewModule.componentsLengthHeader"/>
                </div>
                <div class="viewModuleComponentsDescriptionTitle">
                  <fmt:message key="modules.viewModule.componentsDescriptionHeader"/>
                </div>
              </div>
            
              <div class="viewModuleComponentsInnerContainer">
                <c:forEach var="component" items="${moduleComponents}" varStatus="componentsVs">
                  <div class="viewModuleComponentContainer">
                    <div class="viewModuleComponentHeaderRow">
                      <div class="viewModuleComponentName">${fn:escapeXml(component.name)}</div>
                      <div class="viewModuleComponentLengthTitle">${fn:escapeXml(component.length.units)}</div>
                      <div class="viewModuleComponentDescriptionTitle">${fn:escapeXml(component.description)}</div>
                    </div>
                  </div>
                </c:forEach>
              </div>
            </div>
          
          </c:otherwise>
        </c:choose>
      </div>
  
      <div id="courses" class="tabContent hiddenTab">
        <div id="viewModuleCoursesTableContainer"></div>
        <c:choose>
          <c:when test="${fn:length(courses) le 0}">
            <div class="genericTableNotAddedMessageContainer">
              <fmt:message key="modules.viewModule.noCourses" />
            </div>
          </c:when>
          <c:otherwise>
            <div id="viewModuleCoursesTotalContainer">
              <fmt:message key="modules.viewModule.coursesTotal"/> <span id="viewModuleCoursesTotalValue"></span>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>
