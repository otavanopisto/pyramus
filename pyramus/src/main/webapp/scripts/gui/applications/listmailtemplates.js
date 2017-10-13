var mailTemplates = JSDATA["mailTemplates"].evalJSON();

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));

  var mailTemplatesTable = new IxTable($('mailTemplateListTableContainer'), {
    id : "mailTemplatesTable",
    columns : [
        {
          header : 'Nimi',
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
          tooltip : 'Muokkaa pohjaa',
          onclick : function(event) {
            var table = event.tableComponent;
            var mailTemplateId = table.getCellValue(event.row, table.getNamedColumnIndex('templateId'));
            redirectTo(GLOBAL_contextPath + '/applications/editmailtemplate.page?template=' + mailTemplateId);
          }
        },
        {
          right : 4,
          width : 26,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : 'Poista pohja',
          onclick : function(event) {
            var table = event.tableComponent;
            var templateName = table.getCellValue(event.row, table.getNamedColumnIndex('name'));
            var mailTemplateId = table.getCellValue(event.row, table.getNamedColumnIndex('templateId'));
            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : GLOBAL_contextPath + '/simpledialog.page?message=' + encodeURIComponent('Oletko varma, että haluat poistaa sähköpostipohjan ' + templateName + '?'),
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : 'Sähköpostipohjan poisto',
              okLabel : 'Poista',
              cancelLabel : 'Peruuta'
            });
            dialog.addDialogListener(function(event) {
              if (event.name == 'okClick') {
                new Ajax.Request('/applications/archivemailtemplate.json', {
                  method: 'post',
                  parameters: {
                    id: mailTemplateId
                  },
                  onSuccess: function(response) {
                    window.location.reload();
                  }
                });
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
  for (var i = 0, l = mailTemplates.length; i < l; i++) {
    rows.push([mailTemplates[i].name, null, null, mailTemplates[i].id]);
  }
  mailTemplatesTable.addRows(rows);
};