function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));
  $('showSourceStats').observe('click', function(event) {
    var staffMemberId = $('sourceStaffMember').value;
    var url = GLOBAL_contextPath + "/applications/handlerstatistics.page?staffMemberId=" + staffMemberId;
    var dialog = new IxDialog({
      id : 'sourceStats',
      contentURL : url,
      centered : true,
      showOk : true,
      showCancel : false,
      autoEvaluateSize : true,
      title : 'Käsittelystatistiikka',
      okLabel : 'Sulje'
    });
    dialog.open();
  });
  $('showTargetStats').observe('click', function(event) {
    var staffMemberId = $('targetStaffMember').value;
    var url = GLOBAL_contextPath + "/applications/handlerstatistics.page?staffMemberId=" + staffMemberId;
    var dialog = new IxDialog({
      id : 'sourceStats',
      contentURL : url,
      centered : true,
      showOk : true,
      showCancel : false,
      autoEvaluateSize : true,
      title : 'Käsittelystatistiikka',
      okLabel : 'Sulje'
    });
    dialog.open();
  });
}