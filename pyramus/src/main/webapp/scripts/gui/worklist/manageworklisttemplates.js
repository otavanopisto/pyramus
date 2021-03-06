var templates = JSDATA["templates"].evalJSON();

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));

  var worklistTemplatesTable = new IxTable($('worklistTemplatesTableContainer'), {
    id : "worklistTemplatesTable",
    columns : [
        {
          header : getLocale().getText("worklist.manageWorklistTemplates.description"),
          left : 8,
          width : 300,
          dataType : 'text',
          editable : false,
          paramName : 'description'
        },
        {
          header : getLocale().getText("worklist.manageWorklistTemplates.price"),
          left : 8 + 300 + 8,
          width : 50,
          dataType : 'text',
          editable : false,
          paramName : 'price'
        },
        {
          header : getLocale().getText("worklist.manageWorklistTemplates.factor"),
          left : 8 + 300 + 8 + 50 + 8,
          width : 50,
          dataType : 'text',
          editable : false,
          paramName : 'factor'
        },
        {
          header : getLocale().getText("worklist.manageWorklistTemplates.billingNumber"),
          left : 8 + 300 + 8 + 50 + 8 + 50 + 8,
          width : 150,
          dataType : 'text',
          editable : false,
          paramName : 'billingNumber'
        },
        {
          right : 30,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("worklist.manageWorklistTemplates.editTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var templateId = table.getCellValue(event.row, table.getNamedColumnIndex('templateId'));
            redirectTo(GLOBAL_contextPath + '/worklist/editworklisttemplate.page?template=' + templateId);
          }
        },
        {
          right : 4,
          width : 26,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("worklist.manageWorklistTemplates.removeTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var description = table.getCellValue(event.row, table.getNamedColumnIndex('description'));
            var templateId = table.getCellValue(event.row, table.getNamedColumnIndex('templateId'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=worklist.manageWorklistTemplates.archiveConfirmDialogContent&localeParams="
                + encodeURIComponent(description);

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("worklist.manageWorklistTemplates.archiveTitle"),
              okLabel : getLocale().getText("terms.remove"),
              cancelLabel : getLocale().getText("terms.cancel")
            });

            dialog.addDialogListener(function(event) {
              var dlg = event.dialog;

              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("worklist/archiveworklisttemplate.json", {
                    parameters : {
                      templateId : templateId
                    },
                    onSuccess : function(jsonResponse) {
                      window.location.reload();
                    }
                  });
                break;
              }
            });

            dialog.open();
          }
        }, {
          dataType : 'hidden',
          paramName : 'templateId'
        } ]
  });

  var rows = new Array();
  for (var i = 0, l = templates.length; i < l; i++) {
    rows.push([templates[i].description, templates[i].price, templates[i].factor, templates[i].billingNumber, null, null, templates[i].id]);
  }
  worklistTemplatesTable.addRows(rows);
};