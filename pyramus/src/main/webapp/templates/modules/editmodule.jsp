<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title>
      <fmt:message key="modules.editModule.pageTitle">
        <fmt:param value="${module.name}"/>
      </fmt:message>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
 
    <script type="text/javascript">

      var archivedComponentRowIndex;

      function addComponentsTableRow() {
        var table = getIxTableById('componentsTable');
        table.addRow([-1, '', 0, '', '', '']);
        table.showCell(table.getRowCount() - 1, table.getNamedColumnIndex("removeButton"));
        table.hideCell(table.getRowCount() - 1, table.getNamedColumnIndex("archiveButton"));
        $('noComponentsAddedMessageContainer').setStyle({
          display: 'none'
        });
        $('componentHoursTotalContainer').setStyle({
          display: ''
        });
      } 

      function updateComponentHours() {
        var table = getIxTableById('componentsTable');
        var sum = 0;
        for (var row = 0; row < table.getRowCount(); row++) {
          sum += parseFloat(table.getCellValue(row, table.getNamedColumnIndex('length')).replace(',','.'));
        }
        $('componentHoursTotalValueContainer').innerHTML = sum;
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
        var descTabControl = new IxProtoTabs($('descriptionTabs'), {
          <c:if test="${fn:length(courseDescriptionCategories) gt 0}">
          tabAddContextMenu: [
            <c:forEach var="category" varStatus="vs" items="${courseDescriptionCategories}">
            <c:if test="${not vs.first}">,</c:if>
            {
              text: '${category.name}',
              onclick: function (event) {
                var descName = 'courseDescription.' + '${category.id}';
                var tabContent = descTabControl.addTab(descName, '${category.name}');
                tabContent.update('<input type="hidden" name="' + descName + '.catId" id="' + descName + '.catId" value="${category.id}"/><textarea name="' + descName + '.text" ix:ckeditor="true"></textarea>');
                CKEDITOR.replace(descName + '.text', { toolbar: "moduleDescription", language: document.getCookie('pyramusLocale') });
                descTabControl.setActiveTab(descName);
              },
              isEnabled: function () {
                var catIdElement = $('courseDescription.${category.id}.catId');
                return catIdElement ? catIdElement.value != ${category.id} : true;
              }
            }
            </c:forEach>
          ]
          </c:if>
        });
        
        setupTags();
        var componentsTable = new IxTable($('componentsTable'), {
          id : "componentsTable",
          columns : [ { 
            dataType: 'hidden',
            paramName: 'componentId'
          }, {
            header : '<fmt:message key="modules.editModule.componentsTableNameHeader"/>',
            left : 8,
            width : 236,
            dataType: 'text',
            editable: true,
            paramName: 'name',
            required: true
          }, {
            header : '<fmt:message key="modules.editModule.componentsTableLengthHeader"/>',
            left : 248,
            width : 60,
            dataType : 'number',
            editable: true,
            paramName: 'length',
            required: true
          }, {
            header : '<fmt:message key="modules.editModule.componentsTableDescriptionHeader"/>',
            left: 312,
            right : 30,
            dataType: 'text',
            editable: true,
            paramName: 'description'
          }, {
            width: 26,
            right: 0,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="modules.editModule.componentsTableRemoveRowTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
              if (event.tableComponent.getRowCount() == 0) {
                $('noComponentsAddedMessageContainer').setStyle({
                  display: ''
                });
                $('componentHoursTotalContainer').setStyle({
                  display: 'none'
                });
              }
            } 
          }, {
            width: 26,
            right: 0,
            dataType: 'button',
            paramName: 'archiveButton',
            imgsrc: GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip: '<fmt:message key="modules.editModule.componentsTableArchiveRowTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var componentId = table.getCellValue(event.row, table.getNamedColumnIndex('componentId'));
              var componentName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
              var url = GLOBAL_contextPath + "/simpledialog.page?localeId=modules.editModule.archiveComponentConfirmDialogContent&localeParams=" + encodeURIComponent(componentName);

              archivedComponentRowIndex = event.row; 
                 
              var dialog = new IxDialog({
                id : 'confirmRemoval',
                contentURL : url,
                centered : true,
                showOk : true,  
                showCancel : true,
                autoEvaluateSize: true,
                title : '<fmt:message key="modules.editModule.archiveComponentConfirmDialogTitle"/>',
                okLabel : '<fmt:message key="modules.editModule.archiveComponentConfirmDialogOkLabel"/>',
                cancelLabel : '<fmt:message key="modules.editModule.archiveComponentConfirmDialogCancelLabel"/>'
              });
            
              dialog.addDialogListener(function(event) {
                switch (event.name) {
                  case 'okClick':
                    JSONRequest.request("modules/archivemodulecomponent.json", {
                      parameters: {
                        componentId: componentId
                      },
                      onSuccess: function (jsonResponse) {
                        var table = getIxTableById('componentsTable');
                        table.deleteRow(archivedComponentRowIndex);
                        if (table.getRowCount() == 0) {
                          $('noComponentsAddedMessageContainer').setStyle({
                            display: ''
                          });
                          $('componentHoursTotalContainer').setStyle({
                            display: 'none'
                          });
                        }
                      }
                    });   
                  break;
                }
              });
            
              dialog.open();
            } 
          }]
        });
        componentsTable.addListener("cellValueChange", function (event) {
          updateComponentHours();
        });
        componentsTable.addListener("rowAdd", function (event) {
          updateComponentHours();
        });
        componentsTable.addListener("rowDelete", function(event) {
          updateComponentHours();
        });

        // TODO component length units > double (hours)

        var rows = new Array();
        <c:forEach var="component" items="${moduleComponents}">
          rows.push([
            ${component.id},
            '${fn:escapeXml(component.name)}',
            ${component.length.units},
            '${fn:escapeXml(component.description)}',
            '',
            ''
          ]);
        </c:forEach>
        componentsTable.addRows(rows);
        
        if (componentsTable.getRowCount() > 0) {
          $('noComponentsAddedMessageContainer').setStyle({
            display: 'none'
          });
          $('componentHoursTotalContainer').setStyle({
            display: ''
          });
        }

        // Related actions hover menu (basic tab)

        var basicTabRelatedActionsHoverMenu = new IxHoverMenu($('basicTabRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="modules.editModule.basicTabRelatedActionsLabel"/>'
        });
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/eye.png',
          text: '<fmt:message key="modules.editModule.basicTabRelatedActionsViewModuleLabel"/>',
          link: GLOBAL_contextPath + '/modules/viewmodule.page?module=${module.id}'  
        }));
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/list-add.png',
          text: '<fmt:message key="modules.editModule.basicTabRelatedActionsCreateCourseLabel"/>',
          link: GLOBAL_contextPath + '/courses/createcourse.page?module=${module.id}'  
        }));
      }
    </script>
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">
      <fmt:message key="modules.editModule.pageTitle">
        <fmt:param value="${module.name}"/>
      </fmt:message>
    </h1>
    
    <div id="editModuleEditFormContainer"> 
      <div class="genericFormContainer"> 
        <form action="editmodule.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <input type="hidden" name="moduleId" value="${module.id}"></input>
          <input type="hidden" name="version" value="${module.version}"/>
        
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="modules.editModule.tabLabelBasic"/>
            </a>
            <a class="tabLabel" href="#components">
              <fmt:message key="modules.editModule.tabLabelComponents"/>
            </a>
            <ix:extensionHook name="modules.editModule.tabLabels"/>
          </div>
         
          <div id="basic" class="tabContent">
            <div id="basicTabRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
            
            <!--  TODO italic tags to css -->

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.editModule.createdTitle"/>
                <jsp:param name="helpLocale" value="modules.editModule.createdHelp"/>
              </jsp:include>
              <span><i>${module.creator.fullName} <fmt:formatDate type="both" value="${module.created}"/></i></span>    
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.editModule.modifiedTitle"/>
                <jsp:param name="helpLocale" value="modules.editModule.modifiedHelp"/>
              </jsp:include>
              <span><i>${module.lastModifier.fullName} <fmt:formatDate type="both" value="${module.lastModified}"/></i></span>    
            </div>

             <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.editModule.nameTitle"/>
                <jsp:param name="helpLocale" value="modules.editModule.nameHelp"/>
              </jsp:include>
              <input type="text" name="name" class="required" value="${fn:escapeXml(module.name)}" size="40">
            </div>
             
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.editModule.tagsTitle"/>
                <jsp:param name="helpLocale" value="modules.editModule.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40" value="${fn:escapeXml(tags)}"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.editModule.educationTypesTitle"/>
                <jsp:param name="helpLocale" value="modules.editModule.educationTypesHelp"/>
              </jsp:include>
              <div class="editModuleFormSectionEducationType">
                <c:forEach var="educationType" items="${educationTypes}">
                  <div class="editModuleFormSectionEducationTypeCell">
                    <div class="editModuleFormSectionEducationTypeTitle">
                      <div class="editModuleFormSectionEducationTypeTitleText">${educationType.name}</div>
                    </div>
                    <c:forEach var="educationSubtype" items="${educationSubtypes[educationType.id]}">
                      <c:set var="key" value="${educationType.id}.${educationSubtype.id}"/>
                      <c:choose>
                        <c:when test="${enabledEducationTypes[key]}">
                          <input type="checkbox" name="educationType.${key}" checked="checked"/>                      
                        </c:when>
                        <c:otherwise>
                          <input type="checkbox" name="educationType.${key}"/>                      
                        </c:otherwise>
                      </c:choose>
                      ${educationSubtype.name}<br/>
                    </c:forEach>
                  </div>
                </c:forEach>
              </div>
            </div>
            
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.editModule.subjectTitle"/>
                <jsp:param name="helpLocale" value="modules.editModule.subjectHelp"/>
              </jsp:include>
                
              <select name="subject">           
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

                        <c:choose>
                          <c:when test="${subject.id eq module.subject.id}">
                            <option value="${subject.id}" selected="selected">${subjectName}</option>
                          </c:when>
                          <c:otherwise>
                            <option value="${subject.id}">${subjectName}</option> 
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>

                      <c:if test="${module.subject.archived and module.subject.educationType.id eq educationType.id}">
                        <option value="${module.subject.id}" selected="selected">${subjectName}*</option>
                      </c:if>
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

                  <c:choose>
                    <c:when test="${subject.id eq module.subject.id}">
                      <option value="${subject.id}" selected="selected">${subjectName}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${subject.id}">${subjectName}</option> 
                    </c:otherwise>
                  </c:choose>
                </c:forEach>

                <c:if test="${module.subject.archived and module.subject.educationType.id eq null}">
                  <option value="${module.subject.id}" selected="selected">${module.subject.name} (${module.subject.code})*</option>
                </c:if>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.editModule.courseNumberTitle"/>
                <jsp:param name="helpLocale" value="modules.editModule.courseNumberHelp"/>
              </jsp:include>
                    
              <input type="text" name="courseNumber" value="${module.courseNumber}" size="2">
            </div>
            
            <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="modules.editModule.lengthTitle"/>
                  <jsp:param name="helpLocale" value="modules.editModule.lengthHelp"/>
                </jsp:include>
              <input type="text" name="moduleLength" class="float required" value="${module.courseLength.units}" size="15"/>
              <select name="moduleLengthTimeUnit">           
                <c:forEach var="moduleLengthTimeUnit" items="${moduleLengthTimeUnits}">
                  <option value="${moduleLengthTimeUnit.id}" <c:if test="${module.courseLength.unit.id eq moduleLengthTimeUnit.id}">selected="selected"</c:if>>${moduleLengthTimeUnit.name}</option> 
                </c:forEach>
              </select>            
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.editModule.maxParticipantsTitle"/>
                <jsp:param name="helpLocale" value="modules.editModule.maxParticipantsHelp"/>
              </jsp:include>    
            
              <input type="text" name="maxParticipantCount" size="3" value="${module.maxParticipantCount}">
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.editModule.descriptionTitle"/>
                <jsp:param name="helpLocale" value="modules.editModule.descriptionHelp"/>
              </jsp:include>    

              <div class="tabLabelsContainer" id="descriptionTabs">
                <a class="tabLabel" href="#descGeneric"><fmt:message key="modules.editModule.genericDescriptionTabTitle" /></a>

                <c:forEach var="cDesc" items="${courseDescriptions}">              
                  <a class="tabLabel" href="#courseDescription.${cDesc.category.id}">${cDesc.category.name}</a>
                </c:forEach>
              </div>
      
              <div id="descGeneric" class="tabContent">
                <textarea ix:cktoolbar="moduleDescription" name="description" ix:ckeditor="true">${module.description}</textarea>
              </div>

              <c:forEach var="cDesc" items="${courseDescriptions}">              
                <div id="courseDescription.${cDesc.category.id}" class="tabContent">
                  <input type="hidden" name="courseDescription.${cDesc.category.id}.catId" id="courseDescription.${cDesc.category.id}.catId" value="${cDesc.category.id}"/>
                  <textarea ix:cktoolbar="moduleDescription" name="courseDescription.${cDesc.category.id}.text" ix:ckeditor="true">${cDesc.description}</textarea>
                </div>
              </c:forEach>
            </div>
            
            <ix:extensionHook name="modules.editModule.tabs.basic"/>
          </div>
          
          <div id="components" class="tabContentixTableFormattedData">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addComponentsTableRow();"><fmt:message key="modules.createModule.addComponentLink"/></span>
            </div>
              
            <div id="noComponentsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span><fmt:message key="modules.createModule.noComponentsAddedPreFix"/> <span onclick="addComponentsTableRow();" class="genericTableAddRowLink"><fmt:message key="modules.createModule.noComponentsAddedClickHereLink"/></span>.</span>
            </div>
            
            <div id="componentsTable"></div>
            <!-- TODO Lankinen taitaa komponenttien yhteistunnit kauniiksi -->
            <div id="componentHoursTotalContainer" style="display:none;">
              <span><fmt:message key="modules.editModule.totalComponentHoursLabel"/></span>
              <span id="componentHoursTotalValueContainer">0</span>
            </div>
            <ix:extensionHook name="modules.editModule.tabs.components"/>
          </div>

          <ix:extensionHook name="modules.editModule.tabs"/>
          
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" name="editmodule" value="<fmt:message key="modules.editModule.saveButton"/>">
          </div>
        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>