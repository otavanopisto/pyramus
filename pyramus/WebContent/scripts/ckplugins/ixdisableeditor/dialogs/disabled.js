(function(){
  CKEDITOR.dialog.add('ixdisableeditor_disabled', function(editor) {
    var config = CKEDITOR.config.IxDisableEditor||{};
    var messageStyle = config.messageStyle||"";
    
    return {
      title : '',
      minWidth : 200,
      minHeight : 100,
      contents : [{
        id: 'tab1',
        elements: [{
          type: 'html',
          html: '<div id="ckEditorDisabledDialogMessage" style="' + messageStyle + '"></div>'
        }]
      }],
      buttons : [ ],
      onShow: function (event) {
        var dialog = event.sender;
        var editor = dialog._.editor;
        dialog.parts.title.hide();
        dialog.parts.close.hide();
        
        var onEnableEditor = function (event) {
          this._.editor.removeListener('enableEditor', onEnableEditor);
          this.hide();
        };
        editor.on('enableEditor', onEnableEditor, dialog, null, 100);
        
        var messageElement = document.getElementById('ckEditorDisabledDialogMessage');
        messageElement.innerHTML = editor._disabledMessage;
        var dialogSize = dialog.getSize();
        var width = Math.max(messageElement.offsetWidth, dialogSize.width);
        var height = Math.max(messageElement.offsetHeight, dialogSize.height);
        
        dialog.resize(width, height);
        
        editor.fire("editorDisabled");
      }    
    };
  });
})();