function doList() {
  var filterForm = $("filterForm");
  JSONRequest.request("worklist/listworklistitems.json", {
    parameters : {
      staffMemberId : filterForm.staffMember.value,
      beginDate : filterForm.beginDate.value,
      endDate : filterForm.endDate.value
    },
    onSuccess : function(jsonResponse) {
      var table = getIxTableById('worklistItemsTable');
      table.detachFromDom();
      table.deleteAllRows();
      var worklistItems = jsonResponse.worklistItems;
      for (var i = 0; i < worklistItems.length; i++) {
        table.addRow([
          worklistItems[i].id,
          '', // edit button
          worklistItems[i].entryDate,
          worklistItems[i].description,
          worklistItems[i].price.toFixed(2),
          worklistItems[i].factor,
          worklistItems[i].billingNumber,
          worklistItems[i].state,
          '', // assessment button
          '', // remove button
        ]);
        if (!worklistItems[i].hasAssessment) {
          table.hideCell(table.getRowCount() - 1, table.getNamedColumnIndex("assessmentButton"));
        }
      }
      table.reattachToDom();
      $('worklistItemsWrapper').setStyle({
        display : ''
      });
      var urlParams = new URLSearchParams(window.location.search);
      if (urlParams.has('action')) {
        $('stateChangeDropdown').value = urlParams.get('action');
      }
    }
  });
}

function createNew() {
  var userId = $("staffMember").value;
  var templateId = $("worklistTemplate").value;
  JSONRequest.request("worklist/createworklistitem.json", {
    parameters : {
      userId : userId,
      templateId : templateId
    },
    onSuccess : function(jsonResponse) {
      var table = getIxTableById('worklistItemsTable');
      var worklistItem = jsonResponse.worklistItem;
      table.addRow([
        worklistItem.id,
        '', // edit button
        worklistItem.entryDate,
        worklistItem.description,
        worklistItem.price,
        worklistItem.factor,
        worklistItem.billingNumber,
        worklistItem.state,
        '', // assessment button
        '', // remove button
      ]);
      if (!worklistItem.hasAssessment) {
        table.hideCell(table.getRowCount() - 1, table.getNamedColumnIndex("assessmentButton"));
      }
      var row = table.getRowCount() - 1;
      for (var i = 0; i < table.getColumnCount(); i++) {
        table.setCellEditable(row, i, table.isCellEditable(row, i) == false);
      }
    }
  });
}

function changeState() {
  var table = getIxTableById('worklistItemsTable');
  var newState = $("stateChangeDropdown").value;
  for (var i = 0; i < table.getRowCount(); i++) {
    var itemId = table.getCellValue(i, table.getNamedColumnIndex('worklistItemId'));
    var entryDate = table.getCellValue(i, table.getNamedColumnIndex('entryDate'));
    var description = table.getCellValue(i, table.getNamedColumnIndex('description'));
    var price = table.getCellValue(i, table.getNamedColumnIndex('price'));
    var factor = table.getCellValue(i, table.getNamedColumnIndex('factor'));
    var billingNumber = table.getCellValue(i, table.getNamedColumnIndex('billingNumber'));
    JSONRequest.request("worklist/editworklistitem.json", {
      parameters : {
        itemId : itemId,
        entryDate : entryDate,
        description : description,
        price : price,
        factor : factor,
        billingNumber: billingNumber,
        state: newState
      }
    });
    table.setCellValue(i, table.getNamedColumnIndex('state'), newState);
  }
}

function onListWorklistItems(event) {
  Event.stop(event);
  doList();
}

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  var worklistItemsTable = new IxTable($('worklistItemsTableContainer'), {
    id : 'worklistItemsTable',
    columns : [
      {
        dataType : 'hidden',
        paramName : 'worklistItemId'
      },
      {
        left : 8,
        width : 30,
        dataType : 'button',
        imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
        tooltip : getLocale().getText("worklist.listWorklistItems.edit"),
        onclick : function(event) {
          var table = event.tableComponent;
          for (var i = 0; i < table.getColumnCount(); i++) {
            table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
          }
          var edited = table.isCellEditable(event.row, table.getNamedColumnIndex("entryDate")) == false;
          if (edited) {
            var itemId = table.getCellValue(event.row, table.getNamedColumnIndex('worklistItemId'));
            var entryDate = table.getCellValue(event.row, table.getNamedColumnIndex('entryDate'));
            var description = table.getCellValue(event.row, table.getNamedColumnIndex('description'));
            var price = table.getCellValue(event.row, table.getNamedColumnIndex('price'));
            var factor = table.getCellValue(event.row, table.getNamedColumnIndex('factor'));
            var billingNumber = table.getCellValue(event.row, table.getNamedColumnIndex('billingNumber'));
            var state = table.getCellValue(event.row, table.getNamedColumnIndex('state'));
            JSONRequest.request("worklist/editworklistitem.json", {
              parameters : {
                itemId : itemId,
                entryDate : entryDate,
                description : description,
                price : price,
                factor : factor,
                billingNumber: billingNumber,
                state: state
              }
            });
          }
        }
      },
      {
        header : getLocale().getText("worklist.listWorklistItems.entryDate"),
        left : 8 + 30,
        width : 150,
        dataType : 'date',
        editable : false,
        paramName : 'entryDate',
        required : true
      },
      {
        header : getLocale().getText("worklist.listWorklistItems.description"),
        left : 8 + 30 + 150 + 8,
        width : 800,
        dataType : 'text',
        editable : false,
        paramName : 'description',
        required : true
      },
      {
        header : getLocale().getText("worklist.listWorklistItems.price"),
        right : 8 + 30 + 30 + 150 + 8 + 150 + 8 + 50 + 8,
        width : 50,
        dataType : 'text',
        editable : false,
        paramName : 'price',
        required : true
      },
      {
        header : getLocale().getText("worklist.listWorklistItems.factor"),
        right : 8 + 30 + 30 + 150 + 8 + 150 + 8,
        width : 50,
        dataType : 'text',
        editable : false,
        paramName : 'factor',
        required : true
      },
      {
        header : getLocale().getText("worklist.listWorklistItems.billingNumber"),
        right: 8 + 30 + 30 + 150 + 8,
        width : 150,
        dataType : 'text',
        editable : false,
        paramName : 'billingNumber',
        required : true
      },
      {
        header : getLocale().getText("worklist.listWorklistItems.state"),
        right: 8 + 30 + 30,
        width : 150,
        dataType : 'select',
        editable : false,
        paramName : 'state',
        required : true,
        options : (function() {
          var result = [];
          result.push({
            text : getLocale().getText("worklist.listWorklistItems.state.entered"),
            value : 'ENTERED'
          });
          result.push({
            text : getLocale().getText("worklist.listWorklistItems.state.proposed"),
            value : 'PROPOSED'
          });
          result.push({
            text : getLocale().getText("worklist.listWorklistItems.state.approved"),
            value : 'APPROVED'
          });
          result.push({
            text : getLocale().getText("worklist.listWorklistItems.state.paid"),
            value : 'PAID'
          });
          return result;
        })()
      },
      {
        right : 8 + 30,
        width : 30,
        dataType : 'button',
        imgsrc : GLOBAL_contextPath + '/gfx/kdb_form.png',
        tooltip : getLocale().getText("worklist.listWorklistItems.assessment"),
        onclick : function(event) {
          var table = event.tableComponent;
          var itemId = table.getCellValue(event.row, table.getNamedColumnIndex('worklistItemId'));
          var url = GLOBAL_contextPath + "/worklist/assessmentinfo.page?itemId=" + itemId;
          var dialog = new IxDialog({
            id : 'assessmentInfo',
            contentURL : url,
            centered : true,
            showOk : true,
            showCancel : false,
            autoEvaluateSize : true,
            title : getLocale().getText("worklist.listWorklistItems.assessmentInfo"),
            okLabel : getLocale().getText("terms.close")
          });
          dialog.open();
        },
        paramName : 'assessmentButton'
      },    
      {
        right : 8,
        width : 30,
        dataType : 'button',
        imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
        tooltip : getLocale().getText("worklist.listWorklistItems.delete"),
        onclick : function(event) {
          var table = event.tableComponent;
          var rowIndex = event.row;
          var description = table.getCellValue(rowIndex, table.getNamedColumnIndex('description'));
          var itemId = table.getCellValue(rowIndex, table.getNamedColumnIndex('worklistItemId'));
          var url = GLOBAL_contextPath + "/simpledialog.page?localeId=worklist.listWorklistItems.archiveConfirmDialogContent&localeParams="
              + encodeURIComponent(description);

          var dialog = new IxDialog({
            id : 'confirmRemoval',
            contentURL : url,
            centered : true,
            showOk : true,
            showCancel : true,
            autoEvaluateSize : true,
            title : getLocale().getText("worklist.listWorklistItems.archiveTitle"),
            okLabel : getLocale().getText("terms.remove"),
            cancelLabel : getLocale().getText("terms.cancel")
          });

          dialog.addDialogListener(function(event) {
            var dlg = event.dialog;

            switch (event.name) {
              case 'okClick':
                JSONRequest.request("worklist/archiveworklistitem.json", {
                  parameters : {
                    itemId : itemId
                  },
                  onSuccess : function(jsonResponse) {
                    table.deleteRow(rowIndex);
                  }
                });
              break;
            }
          });

          dialog.open();
        },
        paramName : 'removeButton'
      }
    ]
  });
  
  // Presearch with query parameters
  
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams.has('user') && urlParams.has('begin') && urlParams.has('end')) {
    filterForm.staffMember.value = urlParams.get('user');
    getIxDateField('beginDate').setTimestamp(new Date(urlParams.get('begin')).getTime());
    getIxDateField('endDate').setTimestamp(new Date(urlParams.get('end')).getTime());
    doList();
  }
  
};