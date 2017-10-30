function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  setupUsersTable();
};

function setupUsersTable() {
  var usersTable = new IxTable($('usersTable'), {
    id : "usersTable",
    columns : [{
      dataType: 'hidden',
      paramName: 'userId'
    }, {
      header : 'Henkilö',
      left : 8,
      width: 250,
      dataType : 'text',
      editable: false,
      paramName: 'userName'
    }, {
      left: 270,
      width: 30,
      dataType: 'button',
      imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip: 'Poista käyttäjä',
      onclick: function (event) {
        event.tableComponent.deleteRow(event.row);
      } 
    }]        
  });
  if (typeof JSDATA != 'undefined' && JSDATA['users']) {
    var users = JSDATA["users"].evalJSON();
    for (var i = 0; i < users.length; i++) {
      usersTable.addRow([users[i].id, users[i].name, '']);
    }
  }
}

function openSearchUsersDialog() {
  var dialog = new IxDialog({
    id : 'searchUsersDialog',
    contentURL : GLOBAL_contextPath + '/users/searchusersdialog.page',
    centered : true,
    showOk : true,
    showCancel : true,
    title : 'Hae käyttäjiä',
    okLabel : 'OK', 
    cancelLabel : 'Peruuta' 
  });
  
  dialog.setSize("800px", "600px");
  dialog.addDialogListener(function(event) {
    var dlg = event.dialog;
    switch (event.name) {
      case 'okClick':
        var usersTable = getIxTableById('usersTable');
        usersTable.detachFromDom();
        for (var i = 0, len = event.results.users.length; i < len; i++) {
          var userId = event.results.users[i].id;
          var userName = event.results.users[i].name;
          var index = getUserRowIndex(userId);
          if (index == -1) {
            usersTable.addRow([userId, userName, '']);
          } 
        }
        usersTable.reattachToDom();
      break;
    }
  });
  dialog.open();
}

function getUserRowIndex(userId) {
  var table = getIxTableById('usersTable');
  if (table) {
    var userIdColumn = table.getNamedColumnIndex('userId');
    for (var i = 0; i < table.getRowCount(); i++) {
      var tableUserId = table.getCellValue(i, userIdColumn);
      if (tableUserId == userId) {
        return i;
      }
    }
  }
  return -1;
}
