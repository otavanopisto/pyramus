var notifications = JSDATA['notifications'].evalJSON();

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));

  var notificationsTable = new IxTable($('notificationListTableContainer'), {
    id : "notificationsTable",
    columns : [
        {
          header : 'Linja',
          left : 8,
          right : 176,
          dataType : 'text',
          editable : false,
          paramName : 'line'
        },
        {
          header : 'Tila',
          left : 184,
          right : 460,
          dataType : 'text',
          editable : false,
          paramName : 'state'
        },
        {
          header : 'Käyttäjiä',
          left : 468,
          right : 488,
          dataType : 'text',
          editable : false,
          paramName : 'userCount'
        },
        {
          right : 30,
          width : 30,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          tooltip : 'Muokkaa herätettä',
          onclick : function(event) {
            var table = event.tableComponent;
            var notificationId = table.getCellValue(event.row, table.getNamedColumnIndex('notificationId'));
            redirectTo(GLOBAL_contextPath + '/applications/editnotification.page?notification=' + notificationId);
          }
        },
        {
          right : 4,
          width : 26,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : 'Poista heräte',
          onclick : function(event) {
            var table = event.tableComponent;
            var notificationId = table.getCellValue(event.row, table.getNamedColumnIndex('notificationId'));
            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : GLOBAL_contextPath + '/simpledialog.page?message=' + encodeURIComponent('Oletko varma, että haluat poistaa herätteen?'),
              centered : true,
              showOk : true,
              showCancel : true,
              autoEvaluateSize : true,
              title : 'Hakemusherätteen poisto',
              okLabel : 'Poista',
              cancelLabel : 'Peruuta'
            });
            dialog.addDialogListener(function(event) {
              if (event.name == 'okClick') {
                new Ajax.Request('/applications/deletenotification.json', {
                  method: 'post',
                  parameters: {
                    id: notificationId
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
          paramName : 'notificationId'
        } ]
  });

  var rows = new Array();
  for (var i = 0, l = notifications.length; i < l; i++) {
    rows.push([notifications[i].line, notifications[i].state, notifications[i].userCount, null, null, notifications[i].id]);
  }
  notificationsTable.addRows(rows);
};