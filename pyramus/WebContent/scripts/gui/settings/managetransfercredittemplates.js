var transferCreditTemplates = JSDATA["transferCreditTemplates"].evalJSON();

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));

  var transferCreditTemplatesTable = new IxTable($('transferCreditTemplatesTableContainer'), {
    id : "transferCreditTemplatesTable",
    columns : [
        {
          header : getLocale().getText("settings.manageTransferCreditTemplates.transferCreditTemplatesTableNameHeader"),
          left : 8,
          right : 76,
          dataType : 'text',
          editable : false,
          paramName : 'name'
        },
        {
          right : 30,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : getLocale().getText("settings.manageTransferCreditTemplates.transferCreditTemplatesTableEditButtonHeader"),
          onclick : function(event) {
            var table = event.tableComponent;
            var transferCreditTemplateId = table.getCellValue(event.row, table.getNamedColumnIndex('transferCreditTemplateId'));
            redirectTo(GLOBAL_contextPath + '/settings/edittransfercredittemplate.page?transferCreditTemplate=' + transferCreditTemplateId);
          }
        },
        {
          right : 4,
          width : 26,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.manageTransferCreditTemplates.transferCreditTemplatesTableDeleteButtonHeader"),
          onclick : function(event) {
            var table = event.tableComponent;
            var transferCreditTemplateName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var transferCreditTemplateId = table.getCellValue(event.row, table.getNamedColumnIndex('transferCreditTemplateId'));
            var url = GLOBAL_contextPath
                + "/simpledialog.page?localeId=settings.manageTransferCreditTemplates.transferCreditTemplateDeleteConfirmDialogContent&localeParams="
                + encodeURIComponent(transferCreditTemplateName);

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.manageTransferCreditTemplates.transferCreditTemplateDeleteConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.manageTransferCreditTemplates.transferCreditTemplateDeleteConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.manageTransferCreditTemplates.transferCreditTemplateDeleteConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              var dlg = event.dialog;

              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/deletetransfercredittemplate.json", {
                    parameters : {
                      transferCreditTemplateId : transferCreditTemplateId
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
          paramName : 'transferCreditTemplateId'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = transferCreditTemplates.length; i < l; i++) {
    rows.push([ jsonEscapeHTML(transferCreditTemplates[i].name), null, null, transferCreditTemplates[i].id ]);
  }
  transferCreditTemplatesTable.addRows(rows);

  if (rows.length > 0) {
    $('noTransferCreditTemapltesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
};