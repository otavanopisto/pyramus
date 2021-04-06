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
          worklistItems[i].price,
          worklistItems[i].factor,
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
    }
  });
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
            JSONRequest.request("worklist/editworklistitem.json", {
              parameters : {
                itemId : itemId,
                entryDate : entryDate,
                description : description,
                price : price,
                factor : factor
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
        width : 300,
        dataType : 'text',
        editable : false,
        paramName : 'description',
        required : true
      },
      {
        header : getLocale().getText("worklist.listWorklistItems.price"),
        left : 8 + 30 + 150 + 8 + 300 + 8,
        width : 50,
        dataType : 'text',
        editable : false,
        paramName : 'price',
        required : true
      },
      {
        header : getLocale().getText("worklist.listWorklistItems.factor"),
        left : 8 + 30 + 150 + 8 + 300 + 8 + 50 + 8,
        width : 50,
        dataType : 'text',
        editable : false,
        paramName : 'factor',
        required : true
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
          var description = table.getCellValue(event.row, table.getNamedColumnIndex('description'));
          var itemId = table.getCellValue(event.row, table.getNamedColumnIndex('worklistItemId'));
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
                    table.deleteRow(event.row);
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
};