(function() {
  CKEDITOR.plugins.add('ixdisableeditor', {
    onLoad : function() {
      CKEDITOR.tools.extend(CKEDITOR.editor.prototype, {
        disableEditor: function(message, onLoad) {
          var onEditorDisabled = function (event) {
            var editor = event.sender;
            editor.removeListener("editorDisabled", onEditorDisabled);
            if (onLoad) {
              onLoad.call(editor);
            }
          };
          this.on("editorDisabled", onEditorDisabled);
          this._disabledMessage = message;
          this.openDialog('ixdisableeditor_disabled');
        },
        enableEditor: function () {
          this.fire("enableEditor");
        }
      }, true);
    },
    init : function(editor) {
      CKEDITOR.dialog.add('ixdisableeditor_disabled', this.path + 'dialogs/disabled.js');
    }
  });

})();