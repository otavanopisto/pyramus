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
          worklistItems[i].ownerId,
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
        dataType : 'hidden',
        paramName : 'ownerId'
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
          // TODO show assessment info
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
          // archive
        },
        paramName : 'removeButton'
      }
    ]
  });
};