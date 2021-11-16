var projectId = JSDATA["projectId"].evalJSON();

function setupRelatedCommandsBasic() {
  var relatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
    text : getLocale().getText("projects.editProject.basicTabRelatedActionsLabel")
  });

  relatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
    iconURL : GLOBAL_contextPath + '/gfx/eye.png',
    text : getLocale().getText("projects.editProject.basicTabRelatedActionViewProjectLabel"),
    onclick : function(event) {
      redirectTo(GLOBAL_contextPath + '/projects/viewproject.page?project=' + projectId);
    }
  }));
}

function openSearchModulesDialog() {

  var selectedModules = new Array();
  var modulesTable = getIxTableById('modulesTable');
  for ( var i = 0; i < modulesTable.getRowCount() - 1; i++) {
    var moduleName = modulesTable.getCellValue(i, modulesTable.getNamedColumnIndex('name'));
    var moduleId = modulesTable.getCellValue(i, modulesTable.getNamedColumnIndex('moduleId'));
    selectedModules.push({
      name : moduleName,
      id : moduleId
    });
  }
  // TODO selectedModules -> dialog

  var dialog = new IxDialog({
    id : 'searchModulesDialog',
    contentURL : GLOBAL_contextPath + '/projects/searchmodulesdialog.page',
    centered : true,
    showOk : true,
    showCancel : true,
    title : getLocale().getText("projects.searchModulesDialog.searchModulesDialog.dialogTitle"),
    okLabel : getLocale().getText("projects.searchModulesDialog.okLabel"),
    cancelLabel : getLocale().getText("projects.searchModulesDialog.cancelLabel")
  });

  dialog.setSize("800px", "660px");
  dialog.addDialogListener(function(event) {
    var dlg = event.dialog;
    switch (event.name) {
      case 'okClick':
        var modulesTable = getIxTableById('modulesTable');
        modulesTable.detachFromDom();
        for ( var i = 0, len = event.results.modules.length; i < len; i++) {
          var moduleId = event.results.modules[i].id;
          var moduleName = event.results.modules[i].name;
          var index = getModuleRowIndex('modulesTable', moduleId);
          if (index == -1) {
            modulesTable.addRow([ jsonEscapeHTML(moduleName), 0, '', '', moduleId, -1 ]);
          }
        }
        modulesTable.reattachToDom();
        if (modulesTable.getRowCount() > 0) {
          $('noModulesAddedMessageContainer').setStyle({
            display : 'none'
          });
          $('editProjectModulesTotalContainer').setStyle({
            display : ''
          });
          $('editProjectModulesTotalValue').innerHTML = modulesTable.getRowCount();
        } else {
          $('noModulesAddedMessageContainer').setStyle({
            display : ''
          });
          $('editProjectModulesTotalContainer').setStyle({
            display : 'none'
          });
        }
      break;
    }
  });
  dialog.open();
}

function getModuleRowIndex(tableId, moduleId) {
  var table = getIxTableById(tableId);
  if (table) {
    for ( var i = 0; i < table.getRowCount(); i++) {
      var tableModuleId = table.getCellValue(i, table.getNamedColumnIndex('moduleId'));
      if (tableModuleId == moduleId) {
        return i;
      }
    }
  }
  return -1;
}

function setupTags() {
  JSONRequest.request("tags/getalltags.json", {
    onSuccess : function(jsonResponse) {
      new Autocompleter.Local("tags", "tags_choices", jsonResponse.tags, {
        tokens : [ ',', '\n', ' ' ]
      });
    }
  });
}

function addSubjectCourseTableRow() {
  var table = getIxTableById("subjectCoursesTable");
  var row = table.addRow(['', '', '', '', -1]);
  table.setCellEditable(row, table.getNamedColumnIndex('subjectId'), true);
  table.setCellEditable(row, table.getNamedColumnIndex('courseNumber'), true);
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
      required: true,
      paramName : 'subjectId',
      options: subjectOptions
    }, {
      header : getLocale().getText("projects.generic.subjectCourseTable.courseNumberHeader"),
      left : 8 + 200 + 8,
      width : 100,
      dataType : 'number',
      editable : false,
      required: true,
      paramName : 'courseNumber'
    }, {
      header : getLocale().getText("projects.generic.table.optionalityHeader"),
      width : 150,
      right : 40,
      dataType : 'select',
      editable : true,
      required: true,
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
    }, {
      width : 30,
      right : 0,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("projects.generic.table.removeButtonTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
      }
    }, {
      dataType : 'hidden',
      paramName : 'projectSubjectCourseId'
    } ]
  });
  
  return subjectCoursesTable;
}

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  setupTags();
  setupRelatedCommandsBasic();
  var modulesTable = new IxTable($('modulesTableContainer'), {
    id : "modulesTable",
    columns : [ {
      header : getLocale().getText("projects.editProject.moduleTableNameHeader"),
      left : 8,
      dataType : 'text',
      editable : false,
      paramName : 'name',
      sortAttributes : {
        sortAscending : {
          toolTip : getLocale().getText("generic.sort.ascending"),
          sortAction : IxTable_ROWSTRINGSORT
        },
        sortDescending : {
          toolTip : getLocale().getText("generic.sort.descending"),
          sortAction : IxTable_ROWSTRINGSORT
        }
      }
    }, {
      header : getLocale().getText("projects.editProject.moduleTableOptionalityHeader"),
      right : 8 + 22 + 8 + 22 + 8,
      width : 150,
      dataType : 'select',
      paramName : 'optionality',
      editable : true,
      options : [ {
        text : getLocale().getText("projects.editProject.optionalityMandatory"),
        value : 0
      }, {
        text : getLocale().getText("projects.editProject.optionalityOptional"),
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
        text : getLocale().getText("generic.filter.byValue"),
        onclick : new IxTable_ROWSTRINGFILTER()
      }, {
        text : getLocale().getText("generic.filter.clear"),
        onclick : new IxTable_ROWCLEARFILTER()
      }, {
        text : '-'
      }, {
        text : getLocale().getText("generic.action.copyValues"),
        onclick : new IxTable_COPYVALUESTOCOLUMNACTION(true)
      } ]
    }, {
      width : 22,
      right : 8 + 22 + 8,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
      tooltip : getLocale().getText("projects.editProject.moduleTableEditModuleTooltip"),
      onclick : function(event) {
        var table = event.tableComponent;
        var moduleId = table.getCellValue(event.row, table.getNamedColumnIndex('moduleId'));
        redirectTo(GLOBAL_contextPath + '/modules/editmodule.page?module=' + moduleId);
      }
    }, {
      width : 22,
      right : 8,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("projects.generic.table.removeButtonTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noModulesAddedMessageContainer').setStyle({
            display : ''
          });
          $('editProjectModulesTotalContainer').setStyle({
            display : 'none'
          });
        } else {
          $('noModulesAddedMessageContainer').setStyle({
            display : 'none'
          });
          $('editProjectModulesTotalContainer').setStyle({
            display : ''
          });
          $('editProjectModulesTotalValue').innerHTML = event.tableComponent.getRowCount();
        }
      }
    }, {
      dataType : 'hidden',
      paramName : 'moduleId'
    }, {
      dataType : 'hidden',
      paramName : 'projectModuleId'
    } ]
  });
  
  var subjectCoursesTable = setupSubjectCoursesTable();
  
  JSONRequest.request("projects/getprojectmodules.json", {
    parameters : {
      project : projectId
    },
    onSuccess : function(jsonResponse) {
      var projectModules = jsonResponse.projectModules;
      var rows = new Array();
      for ( var i = 0; i < projectModules.length; i++) {
        rows.push([ jsonEscapeHTML(projectModules[i].name), projectModules[i].optionality, '', '',
            projectModules[i].moduleId, projectModules[i].id ]);
      }
      modulesTable.addRows(rows);
      if (modulesTable.getRowCount() > 0) {
        $('noModulesAddedMessageContainer').setStyle({
          display : 'none'
        });
        $('editProjectModulesTotalContainer').setStyle({
          display : ''
        });
        $('editProjectModulesTotalValue').innerHTML = modulesTable.getRowCount();
      } else {
        $('noModulesAddedMessageContainer').setStyle({
          display : ''
        });
        $('editProjectModulesTotalContainer').setStyle({
          display : 'none'
        });
      }
      
      var subjectCourses = jsonResponse.projectSubjectCourses;
      rows = new Array();
      for (var i = 0; i < subjectCourses.length; i++) {
        rows.push([
            subjectCourses[i].subjectId,
            subjectCourses[i].courseNumber,
            subjectCourses[i].optionality,
            '',
            subjectCourses[i].id]);
      }
      subjectCoursesTable.addRows(rows);
    }
  });
}
