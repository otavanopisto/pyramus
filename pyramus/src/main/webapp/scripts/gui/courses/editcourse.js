'use strict';

function openStudentDetailsDialog(courseStudentId) {
  var dialog = new IxDialog({
    id : 'courseStudentDetailsDialog',
    contentURL : GLOBAL_contextPath + '/courses/studentdetailsdialog.page?courseStudentId=' + courseStudentId,
    centered : true,
    showOk : true,
    showCancel : true,
    title : getLocale().getText("courses.studentDetailsDialog.dialogTitle"),
    okLabel : getLocale().getText("courses.studentDetailsDialog.okLabel"), 
    cancelLabel : getLocale().getText("courses.studentDetailsDialog.cancelLabel") 
  });
  
  dialog.setSize("800px", "600px");
  dialog.addDialogListener(function(event) {
    var dlg = event.dialog;
    switch (event.name) {
      case 'okClick':
        JSONRequest.request("courses/savestudentdetails.json", {
          parameters: {
            courseStudentId: courseStudentId,
            organization: event.results.organization,
            additionalInfo: event.results.additionalInfo,
            roomId: event.results.roomId,
            roomAdditionalInfo: event.results.roomAdditionalInfo,
            lodgingFee: event.results.lodgingFee,
            lodgingFeeCurrency: event.results.lodgingFeeCurrency,
            reservationFee: event.results.reservationFee,
            reservationFeeCurrency: event.results.reservationFeeCurrency,
            billingDetailsId: event.results.billingDetailsId,
            billingDetailsPersonName: event.results.billingDetailsPersonName,
            billingDetailsCompanyName: event.results.billingDetailsCompanyName,
            billingDetailsStreetAddress1: event.results.billingDetailsStreetAddress1,
            billingDetailsStreetAddress2: event.results.billingDetailsStreetAddress2,
            billingDetailsPostalCode: event.results.billingDetailsPostalCode,
            billingDetailsCity: event.results.billingDetailsCity,
            billingDetailsRegion: event.results.billingDetailsRegion,
            billingDetailsCountry: event.results.billingDetailsCountry,
            billingDetailsPhoneNumber: event.results.billingDetailsPhoneNumber,
            billingDetailsEmailAddress: event.results.billingDetailsEmailAddress,
            billingDetailsCompanyIdentifier: event.results.billingDetailsCompanyIdentifier,
            billingDetailsReferenceNumber: event.results.billingDetailsReferenceNumber,
            billingDetailsElectronicBillingAddress: event.results.billingDetailsElectronicBillingAddress,
            billingDetailsElectronicBillingOperator: event.results.billingDetailsElectronicBillingOperator,
            billingDetailsNotes: event.results.billingDetailsNotes
          }
        });  
      break;
    }
  });
  
  dialog.open();
}

function initializeSignupStudyProgrammesTable(tableContainerElement, courseId) {
  var signupStudyProgrammeTable = new IxTable(tableContainerElement, {
  id : "signupStudyProgrammesTable",
  columns : [
      {
        header : getLocale().getText("terms.name"),
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
        right : 8,
        width : 22,
        dataType : 'button',
        imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
        tooltip : getLocale().getText("terms.remove"),
        onclick : function(event) {
          var table = event.tableComponent;
          var archivedRowIndex = event.row;
          table.deleteRow(archivedRowIndex);
        },
        paramName : 'removeButton'
      }, {
        dataType : 'hidden',
        paramName : 'studyProgrammeId'
      } ]
  });

  axios.get("/courses/courses/{0}/signupStudyProgrammes".format(courseId)).then(function (response) {
    var ret = response.data;
    
    if (ret) {
      var rows = [];
      
      for (var i = 0; i < ret.length; i++) {
        rows.push([jsonEscapeHTML(ret[i].studyProgrammeName), '', ret[i].studyProgrammeId]);
      }
      
      if (rows.length > 0) {
        signupStudyProgrammeTable.addRows(rows);
      }
    }
  });

  return signupStudyProgrammeTable;
}  

function initializeSignupStudentGroupsTable(tableContainerElement, courseId) {
  var signupStudentGroupsTable = new IxTable(tableContainerElement, {
    id : "signupStudentGroupsTable",
    columns : [
        {
          header : getLocale().getText("terms.name"),
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
          right : 8,
          width : 22,
          dataType : 'button',
          imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
          tooltip : getLocale().getText("terms.remove"),
          onclick : function(event) {
            var table = event.tableComponent;
            var archivedRowIndex = event.row;
            table.deleteRow(archivedRowIndex);
        },
        paramName : 'removeButton'
      }, {
        dataType : 'hidden',
        paramName : 'studentGroupId'
      } ]
  });

  axios.get("/courses/courses/{0}/signupStudentGroups".format(courseId)).then(function (response) {
    var ret = response.data;
    
    if (ret) {
      var rows = [];
      
      for (var i = 0; i < ret.length; i++) {
        rows.push([ret[i].studentGroupName, '', ret[i].studentGroupId]);
      }
      
      if (rows.length > 0) {
        signupStudentGroupsTable.addRows(rows);
      }
    }
  });

  return signupStudentGroupsTable;
}
  
function initializeSignupStudyProgrammeSearchField(courseId, inputElement, autoCompleterElement, autoCompleteIndicatorElement) {
  new Ajax.Autocompleter(inputElement, autoCompleterElement, '../students/studyprogrammesautocomplete.binary', {
    paramName: 'text', 
    minChars: 1, 
    indicator: autoCompleteIndicatorElement,
    afterUpdateElement : function getSelectionId(text, li) {
      var id = $(li).down('input[name="studyProgrammeId"]').value;
      var name = $(li).down('input[name="studyProgrammeName"]').value;

      getIxTableById("signupStudyProgrammesTable").addRow([
        name,
        '',
        id
      ]);
      
      inputElement.value = "";
    }
  });
}

function initializeSignupStudentGroupSearchField(courseId, inputElement, autoCompleterElement, autoCompleteIndicatorElement) {
  new Ajax.Autocompleter(inputElement, autoCompleterElement, '../students/studentgroupsautocomplete.binary', {
    paramName: 'text', 
    minChars: 1, 
    indicator: autoCompleteIndicatorElement,
    afterUpdateElement : function getSelectionId(text, li) {
      var id = $(li).down('input[name="studentGroupId"]').value;
      var name = $(li).down('input[name="studentGroupName"]').value;

      getIxTableById("signupStudentGroupsTable").addRow([
        name,
        '',
        id
      ]);
      
      inputElement.value = "";
    }
  });
}

