var gradingScales = JSDATA["gradingScales"].evalJSON();

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));

  var gradingScalesTable = new IxTable($('gradingScaleListTableContainer'), {
    id : "gradingScalesTable",
    columns : [
        {
          header : getLocale().getText("settings.listGradingScales.gradingScalesTableNameHeader"),
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
          tooltip : getLocale().getText("settings.listGradingScales.gradeTableEditTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var scaleId = table.getCellValue(event.row, table.getNamedColumnIndex('scaleId'));
            redirectTo(GLOBAL_contextPath + '/settings/editgradingscale.page?gradingScaleId=' + scaleId);
          }
        },
        {
          right : 4,
          width : 26,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("settings.listGradingScales.gradeTableArchiveTooltip"),
          onclick : function(event) {
            var table = event.tableComponent;
            var gradeName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var scaleId = table.getCellValue(event.row, table.getNamedColumnIndex('scaleId'));
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=settings.listGradingScales.gradingScaleArchiveConfirmDialogContent&localeParams="
                + encodeURIComponent(gradeName);

            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : getLocale().getText("settings.listGradingScales.gradingScaleArchiveConfirmDialogTitle"),
              okLabel : getLocale().getText("settings.listGradingScales.gradingScaleArchiveConfirmDialogOkLabel"),
              cancelLabel : getLocale().getText("settings.listGradingScales.gradingScaleArchiveConfirmDialogCancelLabel")
            });

            dialog.addDialogListener(function(event) {
              var dlg = event.dialog;

              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("settings/archivegradingscale.json", {
                    parameters : {
                      gradingScaleId : scaleId
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
          paramName : 'scaleId'
        } ]
  });

  var rows = new Array();
  for ( var i = 0, l = gradingScales.length; i < l; i++) {
    rows.push([ gradingScales[i].name, null, null, gradingScales[i].id ]);
  }
  gradingScalesTable.addRows(rows);
};