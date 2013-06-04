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
            modulesTable.addRow([ jsonEscapeHTML(moduleName), 0, '', moduleId ]);
          }
        }
        modulesTable.reattachToDom();
        if (modulesTable.getRowCount() > 0) {
          $('noModulesAddedMessageContainer').setStyle({
            display : 'none'
          });
          $('createProjectModulesTotalContainer').setStyle({
            display : ''
          });
          $('createProjectModulesTotalValue').innerHTML = modulesTable.getRowCount();
        } else {
          $('noModulesAddedMessageContainer').setStyle({
            display : ''
          });
          $('createProjectModulesTotalContainer').setStyle({
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

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  setupTags();
  var modulesTable = new IxTable($('modulesTableContainer'), {
    id : "modulesTable",
    columns : [ {
      header : getLocale().getText("projects.createProject.moduleTableNameHeader"),
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
      header : getLocale().getText("projects.createProject.moduleTableOptionalityHeader"),
      width : 150,
      right : 40,
      dataType : 'select',
      editable : true,
      paramName : 'optionality',
      options : [ {
        text : getLocale().getText("projects.createProject.optionalityMandatory"),
        value : 0
      }, {
        text : getLocale().getText("projects.createProject.optionalityOptional"),
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
      tooltip : getLocale().getText("projects.createProject.moduleTableDeleteRowTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noModulesAddedMessageContainer').setStyle({
            display : ''
          });
          $('createProjectModulesTotalContainer').setStyle({
            display : 'none'
          });
        } else {
          $('noModulesAddedMessageContainer').setStyle({
            display : 'none'
          });
          $('createProjectModulesTotalContainer').setStyle({
            display : ''
          });
          $('createProjectModulesTotalValue').innerHTML = event.tableComponent.getRowCount();
        }
      }
    }, {
      dataType : 'hidden',
      paramName : 'moduleId'
    } ]
  });
}
