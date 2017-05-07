IxHoverPanel = Class.create(
  /** @lends IxHoverPanel.prototype */
  {
  /** Creates a new hover panel.
   * @class A hover panel UI element.
   * @constructs
   * @param options An object containing the following properties:
   * <dl>
   *   <dt><code>contentURL</code></dt>
   *   <dd>The URL pointing to the content of the hover panel.</dd>
   * </dl>
   */
  initialize : function(options) {
    this.domNode = new Element("div", {className: "IxHoverPanel"});
    this._content = new Element("div", {className: "IxHoverPanelContent"}); 
    this._options = options;
    
    if (options.content) {
      this._updateContent(options.content);
    } 
    
    if ((options.width != undefined) && !isNaN(options.width)) {
      this.domNode.setStyle({
        width: options.width + 'px'
      });
    }
    
    if ((options.height != undefined) && !isNaN(options.height)) {
      this.domNode.setStyle({
        height: options.height + 'px'
      });
    }
    
    this._windowMouseDownListener = this._onWindowMouseDown.bindAsEventListener(this);
  },
  /** Shows the hover panel over a DOM element.
   * 
   * @param element The DOM element to show this hover panel on.
   */
  showOverElement: function (element) {
    if (this._options.contentURL) {
      var _this = this;
      new Ajax.Request(this._options.contentURL, {  
        onSuccess: function(transport){
          _this._updateContent(transport.responseText);
          _this._showOverElement(element);
        },
        onFailure: function(transport) {
          throw new Error(transport.responseText);
        }
      });
    } else {
      this._showOverElement(element);
    }
  },
  _showOverElement: function (element) {
    var elementOffset = element.cumulativeOffset();
    
    var top = elementOffset.top;
    var left = elementOffset.left; 
    
    this.domNode.setStyle({
      opacity: 0
    });
    
    document.body.appendChild(this.domNode);
    
    var panelDimensions = this.domNode.getDimensions();
    var panelBottom = panelDimensions.height + elementOffset.top;
    
    if (panelBottom > document.viewport.getHeight()) {
      var elementDimensions = element.getDimensions();
      top = elementOffset.top + elementDimensions.height - panelDimensions.height; 
    } 
    
    this.domNode.setStyle({
      top: top + 'px',
      left: left + 'px',
      opacity: 1
    });
    
    if (Prototype.Browser.IE)
      Event.observe(document, "mousedown", this._windowMouseDownListener);
    else
      Event.observe(window, "mousedown", this._windowMouseDownListener);
  },
  /** Hides the hover panel.
   * 
   */
  hide: function () {
    if (this.domNode.parentNode) {
      if (Prototype.Browser.IE)
        Event.stopObserving(document, "mousedown", this._windowMouseDownListener);
      else
        Event.stopObserving(window, "mousedown", this._windowMouseDownListener);
      
      document.body.removeChild(this.domNode);
    }
  },
  _onWindowMouseDown: function (event) {
     this.hide();
  },
  _updateContent: function (content) {
    this.domNode.update(content);
  }
});
