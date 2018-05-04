
function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var organizationsTable = initializeOrganizationsTable();
  
  axios.get("/organizations").then(function (response) {
    var organizations = response.data;
    
    if (organizations) {
      var rows = new Array();
      for (var i = 0, l = organizations.length; i < l; i++) {
        var organization = organizations[i];
        rows.push([jsonEscapeHTML(organization.name), '', '',  organization.id]);
      }
      organizationsTable.addRows(rows);
    }
  });
}

function initializeOrganizationsTable() {
  var organizationsTable = new IxTable($('organizationsTable'), {
  id : "organizationsTable",
  columns : [
      {
        header : getLocale().getText("settings.organizations.tableNameHeader"),
        left : 8,
        width : 300,
        dataType : 'text',
        editable : false,
        paramName : 'name',
        required : true,
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
        right : 8 + 22 + 8,
        width : 22,
        dataType : 'button',
        imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
        tooltip : getLocale().getText("settings.organizations.tableEditTooltip"),
        onclick : function(event) {
          var table = event.tableComponent;
          var organizationId = table.getCellValue(event.row, table.getNamedColumnIndex('organizationId'));
          redirectTo(GLOBAL_contextPath + '/settings/editorganization.page?organizationId=' + organizationId);
        }
      }, {
        right : 8,
        width : 22,
        dataType : 'button',
        imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
        tooltip : getLocale().getText("settings.organizations.tableArchiveTooltip"),
        onclick : function(event) {
          var table = event.tableComponent;
          var organizationId = table.getCellValue(event.row, table.getNamedColumnIndex('organizationId'));
          var organizationName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
          var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.subjects.subjectArchiveConfirmDialogContent&localeParams="
              + encodeURIComponent(organizationName);
  
          var archivedRowIndex = event.row;
  
          var dialog = new IxDialog({
            id : 'confirmRemoval',
            contentURL : url,
            centered : true,
            showOk : true,
            showCancel : true,
            autoEvaluateSize : true,
            title : getLocale().getText("settings.organizations.archiveConfirmDialogTitle"),
            okLabel : getLocale().getText("settings.organizations.archiveConfirmDialogOkLabel"),
            cancelLabel : getLocale().getText("settings.organizations.archiveConfirmDialogCancelLabel")
          });
  
          dialog.addDialogListener(function(event) {
            switch (event.name) {
              case 'okClick':
                axios.delete("/organizations/{0}".format(organizationId)).then(function (response) {
                  getIxTableById('organizationsTable').deleteRow(archivedRowIndex);
                });
              break;
            }
          });
  
          dialog.open();
        },
        paramName : 'archiveButton'
      }, {
        dataType : 'hidden',
        paramName : 'organizationId'
      } ]
  });
  
  return organizationsTable;
}