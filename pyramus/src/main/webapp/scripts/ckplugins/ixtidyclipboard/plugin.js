(function() {
  var getFirstAndLastDifferingElements = function (dataBefore, dataAfter) {
    var beforeDiv = document.createElement("div");
    beforeDiv.innerHTML = dataBefore;
    
    var afterDiv = document.createElement("div");
    afterDiv.innerHTML = dataAfter;
    
    var beforeChildren = beforeDiv.childNodes;
    var beforeChildrenLen = beforeChildren.length;
    var afterChildren = afterDiv.childNodes;
    var afterChildrenLen = afterChildren.length;
    
    var firstDiff = null;
    var lastDiff = null;
    var i = 0;
    var firstDiffStop = false;
    var lastDiffStop = false;
    
    while ((lastDiffStop != true)||(firstDiffStop != true)) {
      if (firstDiff == null) {
        if ((i < beforeChildrenLen) && (i < afterChildrenLen)) {
          var beforeChild = beforeChildren[i];
          var afterChild = afterChildren[i];
          if (beforeChild.innerHTML != afterChild.innerHTML) {
            firstDiff = i;
            firstDiffStop = true;
          }
        } else {
          firstDiffStop = true;
        }
      } else {
        firstDiffStop = true; 
      }
      
      var beforeCurrent = ((beforeChildrenLen - 1) - i); 
      var afterCurrent = ((afterChildrenLen - 1) - i);
      
      if ((beforeCurrent >= 0) && (afterCurrent >= 0) && (lastDiff == null)) {
        var beforeChild = beforeChildren[beforeCurrent];
        var afterChild = afterChildren[afterCurrent];
        if (beforeChild.innerHTML != afterChild.innerHTML) {
          lastDiff = afterCurrent;
          lastDiffStop = true;
        }
      } else {
        lastDiffStop = true;
      }
      
      i++;
    }
    
    if (firstDiff == null)
      firstDiff = 0;
    if (lastDiff == null)
      lastDiff = afterChildrenLen - 1;
    
    return [firstDiff, lastDiff];
  };
  
  CKEDITOR.plugins.add('ixtidyclipboard', {
    lang : [ 'fi', 'en' ],
    requires: ['ixajax', 'ixdisableeditor'],
    onLoad : function() {
      CKEDITOR.tools.extend(CKEDITOR.editor.prototype, {
        includesUntidyData:function(htmlData) {
          var untidyRegExps = this.config.IxTidyClipboard.untidyContent;
          for (var i = 0, l = untidyRegExps.length; i < l; i++) {
            if (untidyRegExps[i].test(htmlData)) {
              return true;
            } 
          }
          
          return false;
        }
      }, true);
    },
    init : function(editor) {
      var config = editor.config.IxTidyClipboard;
      var lang = editor.lang.ixtidyclipboard;
      
      editor.on("beforePaste", function(event) {
        var editor = event.sender;
        var dataBeforePaste = editor.getSnapshot();
        
        var onAfterPasteSnapshot = function (event) {
          var editor = event.sender;
          editor.removeListener("getSnapshot", onAfterPasteSnapshot);
          editor.fire("afterPaste", {
            dataBefore: dataBeforePaste
          });
        }
        
        editor.on("getSnapshot", onAfterPasteSnapshot);
      });
      
      editor.on("afterPaste", function (event) {
        var editor = event.sender;
        var dataBefore = event.data.dataBefore;
        var dataAfter = editor.getSnapshot();
        if (dataBefore != dataAfter) {
          var flElements = getFirstAndLastDifferingElements(dataBefore, dataAfter);
          var bodyChildren = editor.document.getBody().getChildren();
          if (bodyChildren.count() > 0) {
            var firstChanged = bodyChildren.getItem(flElements[0]);
            var lastChanged = bodyChildren.getItem(flElements[1]);
            
            var pastedRange = new CKEDITOR.dom.range(editor.document);
            pastedRange.setStartBefore(firstChanged ? firstChanged : bodyChildren.getItem(0));
            pastedRange.setEndAfter(lastChanged ? lastChanged : bodyChildren.getItem(bodyChildren.count() - 1));
            
            var pastedContents = pastedRange.cloneContents();
            var pastedHTML = '';
            
            for (var i = 0, l = pastedContents.getChildCount(); i < l; i++) {
              var child = pastedContents.getChild(i);
              if (child.type == 1)
                pastedHTML += child.getOuterHtml();
              else if (child.type == 3)
                pastedHTML += child.$.wholeText;
            }
            
            if (editor.includesUntidyData(pastedHTML)) {
              editor.disableEditor(lang.cleanupMessage, function () {
                pastedRange.deleteContents();
                var params = new Object();
                params[config.serverDataParam] = encodeURIComponent(pastedHTML);
                
                var _this = this;
                CKEDITOR.ajax.post(config.serverURL, params, function(transport) {
                  if (transport.status == 200 || transport.status == 304) {
                    var jsonResponse = eval('(' + transport.responseText + ')');
                    var fixedContent = jsonResponse[config.serverResponseParam];
                    _this.insertHtml(fixedContent);
                    _this.enableEditor();
                  }
                });
              });
            }
          }
        }
      });
    }
  });

})();