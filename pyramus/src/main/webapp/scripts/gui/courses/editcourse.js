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
              roomId: event.results.roomId
            }
          });  
        break;
      }
    });
    
    dialog.open();
  }
  
  window.openStudentDetailsDialog = openStudentDetailsDialog;
  
}).call(this);