(function() {
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
  
  window.openStudentDetailsDialog = openStudentDetailsDialog;
  
}).call(this);