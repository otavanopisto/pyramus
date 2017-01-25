var courseUserRoles = JSDATA["courseUserRoles"].evalJSON();

var deletedRowIndex;

function addCourseUserRoleTableRow () {
  var table = getIxTableById('courseUserRolesTable');
  var rowIndex = table.addRow([ '', '', '', '', -1, 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }
  $('noCourseUserRolesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
  table.hideCell(rowIndex, table.getNamedColumnIndex('deleteButton'));
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));
  
  var courseUserRolesTable = new IxTable($('courseUserRolesTableContainer'), {
    id : "courseUserRolesTable",
    columns : [
        {
          left : 8,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.courseUserRoles.courseUserRolesTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            for ( var i = 0; i < table.getColumnCount(); i++) {
              table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
            }
            table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
          }
        },
        {
          header : getLocale().getText("settings.courseUserRoles.courseUserRolesTableNameHeader"),
          left : 38,
          width : 150,
          dataType : 'text',
          editable : false,
          paramName : 'name',
          required : true
        },
        {
          left : 346,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.courseUserRoles.courseUserRolesTableDeleteTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var courseUserRoleId = table.getCellValue(event.row, table.getNamedColumnIndex('courseUserRoleId'));
            var courseUserRoleName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.courseUserRoles.courseUserRoleDeleteConfirmDialogContent&localeParams="
                + encodeURIComponent(courseUserRoleName);

            deletedRowIndex = event.row;

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.courseUserRoles.courseUserRoleDeleteConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.courseUserRoles.courseUserRoleDeleteConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.courseUserRoles.courseUserRoleDeleteConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/deletecourseuserrole.json", {
                    parameters : {
                      courseUserRole : courseUserRoleId
                    },
                    onSuccess : function(jsonResponse) {
                      getIxTableById('courseUserRolesTable').deleteRow(deletedRowIndex);
                    }
                  });
                break;
              }
            });

            dialog.open();
          },
          paramName : 'deleteButton'
        }, {
          left : 346,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
          tooltip : getLocale().getText("settings.courseUserRoles.courseUserRolesTableRemoveTooltip"),
          onclick : function(event) {
            event.tableComponent.deleteRow(event.row);
            if (event.tableComponent.getRowCount() == 0) {
              $('noCourseUserRolesAddedMessageContainer').setStyle({
                display : ''
              });
            }
          },
          paramName : 'removeButton',
          hidden : true
        }, {
          dataType : 'hidden',
          paramName : 'courseUserRoleId'
        }, {
          dataType : 'hidden',
          paramName : 'modified'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = courseUserRoles.length; i < l; i++) {
    rows.push([ '', jsonEscapeHTML(courseUserRoles[i].name), '', '', courseUserRoles[i].id, 0]);
  }
  courseUserRolesTable.addRows(rows);

  if (courseUserRolesTable.getRowCount() > 0) {
    $('noCourseUserRolesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
