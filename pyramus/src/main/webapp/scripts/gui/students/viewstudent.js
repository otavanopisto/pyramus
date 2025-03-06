function openEditStudentParentInvitationDialog(studentId, invitationId) {
  var dialog = new IxDialog({
    id : 'editStudentParentInvitationDialog',
    contentURL : GLOBAL_contextPath + '/studentparents/editstudentparentinvitation.page?studentId=' + studentId + '&invitationId=' + invitationId,
    centered : true,
    showOk : true,
    showCancel : true,
    title : getLocale().getText("students.viewStudent.parentInvitationDialog.title"),
    okLabel : getLocale().getText("generic.dialog.ok"), 
    cancelLabel : getLocale().getText("generic.dialog.cancel") 
  });
  
  dialog.setSize("550px", "330px");
  dialog.addDialogListener(function(event) {
    var dlg = event.dialog;
    switch (event.name) {
      case 'okClick':
        var params = {
          studentId: event.results.studentId,
          invitationId: event.results.invitationId,
          firstName: event.results.firstName,
          lastName: event.results.lastName,
          email: event.results.email
        };
      
        JSONRequest.request("studentparents/editstudentparentinvitation.json", {
          parameters: params,
          onSuccess: function (jsonResponse) {
            window.location.reload(true);
          }
        });
      break;
    }
  });
  
  dialog.open();
}

function openEditSubjectGradesDialog(studentId) {
  var dialog = new IxDialog({
    id : 'editStudentSubjectGradesDialog',
    contentURL : GLOBAL_contextPath + '/students/editstudentsubjectgradesdialog.page?studentId=' + studentId,
    centered : true,
    showOk : true,
    showCancel : false,
    title : getLocale().getText("students.viewStudent.studentSubjecGradesDialog.title"),
    okLabel : getLocale().getText("terms.close"), 
    cancelLabel : getLocale().getText("terms.close") 
  });
  
  dialog.setSize("900px", "500px");
  dialog.open();
}