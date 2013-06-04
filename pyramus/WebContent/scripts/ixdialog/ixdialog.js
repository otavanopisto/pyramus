_Dialogs = new Hash();

IxDialog = Class.create(
  /** @lends IxDialog.prototype */
  {
  /** Creates a new modal dialog box (in the DOM).
   * @class A modal dialog box, displayed on the page (not on a separate window),
   * that shows the contents of a Web page.
   * @constructs
   * 
   * @param options The options that describe the dialog, as an object containing the following properties:
   * <dl>
   *   <dt><code>id</code></dt>
   *   <dd>The unique identifier for the dialog.</dd>
   *   <dt><code>contentURL</code></dt>
   *   <dd>The URL of the Web page to be shown in the dialog.</dd>
   *   <dt><code>centered</code></dt>
   *   <dd>Set this to <code>true</code> to center the dialog.</dd>
   *   <dt><code>showOk</code></dt>
   *   <dd>Set this to <code>true</code> to show the OK button in the dialog.</dd>
   *   <dt><code>showCancel</code></dt>
   *   <dd>Set this to <code>true</code> to show the Cancel button in the dialog.</dd>
   *   <dt><code>autoEvaluateSize</code></dt>
   *   <dd>Set this to <code>true</code> to calculate the size of the dialog automagically.</dd>
   *   <dt><code>title</code></dt>
   *   <dd>The title of the dialog box.</dd>
   *   <dt><code>okLabel</code></dt>
   *   <dd>The label of the OK button.</dd>
   *   <dt><code>cancelLabel</code></dt>
   *   <dd>The label of the Cancel button.</dd>
   * </dl>
   */
  initialize : function(options) {
    this._id = options.id;
    this._isOpen = false;
    this._dragging = false;
    this._isHideOnly = options.hideOnly||false;
    this._autoEvaluateSize = options.autoEvaluateSize == true;
    this._appearDuration = options.appearDuration||0.2;
    this._contentLoaded = false;
    
    this._listeners = new Array();

    this._dialogNode = new Element("div");
    this._dialogNode.addClassName('IxDialog');
    this._dialogNode.writeAttribute('id',this._id);

    // Create a div representing the title bar of the dialog. Make its text
    // unselectable since it will be used to drag the dialog around.
    
    this._titleBar = new Element("div");
    this._titleBar.addClassName('IxDialogTitleBar');
    this._titleBar.onselectstart = function(){
      return false;
    };
    this._titleBar.unselectable = "on";
    
    this._title = new Element("div");
    if (options.title)
      this.setTitle(options.title);

    this._title.addClassName('IxDialogTitle');
    
    // TODO _titleBarButtonsContainer does nothing at all? Future in the making?
    
    this._titleBarButtonsContainer = new Element("div");
    this._titleBarButtonsContainer.addClassName('IxDialogTitleBarButtons');

    this._titleBar.appendChild(this._titleBarButtonsContainer);
    this._titleBar.appendChild(this._title);

    this._dialogNode.appendChild(this._titleBar);
    
    if (options.content)
      this._pendingContent = options.content;
    
    var contentURL = options.contentURL ? options.contentURL : 'about:blank';
    
    this._dialogContent = new Element("iframe", { frameborder: 0, border: 0, className: "IxDialogContent", src: contentURL });
    
    if (this._autoEvaluateSize != true) {
      this._dialogContent.setStyle({
        width: "100%",
        height: "100%"
      });
    } 
    
    this._dialogNode.setStyle({
      opacity: 0 
    });
    
    this._dialogContentLoadListener = this._onDialogContentLoad.bindAsEventListener(this);
    Event.observe(this._dialogContent, "load", this._dialogContentLoadListener);
    this._dialogNode.appendChild(this._dialogContent);
    
    this._buttonsContainer = new Element("div", {className: "IxDialogButtonsContainer"});
    
    if (options.showOk) {
      this._okButton = new Element("button", { className: "IxDialogButton IxDialogOkButton" });
      this._okButton.update(options.okLabel);
      this._buttonsContainer.appendChild(this._okButton);
      this._okButtonClickListener = this._onOkButtonClick.bindAsEventListener(this);
      Event.observe(this._okButton, "click", this._okButtonClickListener);

      if (options.disableOk) {
        this.disableOkButton();
      }
    }
    
    if (options.showCancel) {
      this._cancelButton = new Element("button", {className: "IxDialogButton IxDialogCancelButton"});
      this._cancelButton.update(options.cancelLabel);
      this._buttonsContainer.appendChild(this._cancelButton);
      this._cancelButtonClickListener = this._onCancelButtonClick.bindAsEventListener(this);
      Event.observe(this._cancelButton, "click", this._cancelButtonClickListener);

      if (options.disableCancel) {
        this.disableCancelButton();
      }
    }
    
    this._dialogNode.appendChild(this._buttonsContainer);
    this._parentNode = document.getElementsByTagName('body')[0];
    this._centered = options.centered;

    this._windowResizedListener = this._onWindowResized.bindAsEventListener(this);
    this._windowMouseMoveListener = this._onWindowMouseMove.bindAsEventListener(this);
    this._titleBarMouseDownListener = this._onTitleBarMouseDown.bindAsEventListener(this);
    this._windowMouseUpListener = this._onWindowMouseUp.bindAsEventListener(this);
    
    Event.observe(window, "mousemove", this._windowMouseMoveListener);
    Event.observe(this._titleBar, "mousedown", this._titleBarMouseDownListener);
    Event.observe(window, "resize", this._windowResizedListener);

    _Dialogs.set(this._id, this);
  },
  /** Changes the content URL of the dialog box.
   * 
   * @param contentURL The new URL to point the dialog into.
   */
  setContentUrl : function (contentURL) {
    this._dialogContent.setAttribute("src", contentURL);
  },
  /** Opens the dialog box.
   * 
   */
  open : function() {
    this._glassPane = new Element("div", {className: "dialogGlassPane"});
    document.body.appendChild(this._glassPane);
    this._parentNode.appendChild(this._dialogNode);
    this._isOpen = true;
    this._recalculateSize();
  },
  /** Closes the dialog box.
   * 
   */
  close : function() {
    this._glassPane.remove();
    this._parentNode.removeChild(this._dialogNode);
    Event.stopObserving(window, "scroll", this._windowScrollListener);
    this._isOpen = false;
    this._dialogNode = undefined;
    Event.stopObserving(window, "resize", this._windowResizedListener);
    Event.stopObserving(window, "mousemove", this._windowMouseMoveListener);
    Event.stopObserving(window, "mouseup", this._windowMouseUpListener);
    Event.stopObserving(document, "mouseup", this._windowMouseUpListener);
    Event.stopObserving(this._frameDocument , "mouseup", this._windowMouseUpListener);
   
    // Event.stopObserving(this._frameWindow, "mouseup", this._windowMouseUpListener);
    Event.stopObserving(this._titleBar, "mousedown", this._titleBarMouseDownListener);
    _Dialogs.unset(this._id);

    while (this._listeners.length > 0)
      this._listeners.pop();
  },
  /** Shows the dialog box.
   * 
   */
  show: function() {
    if (this._isHidden) {
      this._dialogNode.show();
	    this._glassPane = new Element("div", {className: "dialogGlassPane"});
	    document.body.appendChild(this._glassPane);
//	    this._parentNode.appendChild(this._dialogNode); // Causes a reload of dialog contents
	    this._isHidden = false;
	    this._recalculateSize();
    }
  },
  /** Hides the dialog box.
   * 
   */
  hide : function() {
    if (!this._isHidden) {
      this._glassPane.remove();
      Event.stopObserving(window, "scroll", this._windowScrollListener);
      this._isHidden = true;
      this._dialogNode.hide();
    }
  },
  /** Centers the dialog box.
   * 
   */
  center : function() {
    var dialogDims = this._dialogNode.getDimensions();
    var viewportDims = document.viewport.getDimensions();
    var scrollOffs = document.viewport.getScrollOffsets();
    var left = (viewportDims.width / 2) - (dialogDims.width / 2);
    var top = Math.max((viewportDims.height / 2) - (dialogDims.height / 2) + scrollOffs.top, 0);
    
    this._dialogNode.setStyle( {
      top :top + 'px',
      left :left + 'px'
    });
  },
  /** Returns the DOM element representing the content of this dialog box.
   * 
   * @returns {Element} The DOM element representing the content of this dialog box.
   */
  getContentElement : function() {
    return this._dialogContent;
  },
  /** Returns the DOM element representing this dialog box.
   * 
   * @returns {Element} The DOM element representing this dialog box.
   */
  getDialogElement: function () {
    return this._dialogNode;
  },
  /** Sets the size of the dialog box.
   * 
   * @param width The new width of the dialog box, in CSS markup.
   * @param height The new height of the dialog box, in CSS markup.
   */
  setSize : function(width, height) {
    this._dialogNode.setStyle( {
      width :width,
      height :height
    });

    this._recalculateSize();
  },
  /** Adds a new event listener for the dialog box.
   * 
   * @param listener The event listener to add.
   */
  addDialogListener : function(listener) {
    this._listeners.push(listener);
  },
  /** Sets the title of the dialog box.
   * 
   * @param title The new title for the dialog box.
   */
  setTitle : function(title) {
    this._title.update(title);
    this._recalculateSize();
  },
  /** Returns the <code>document</code> element for the dialog box content.
   * 
   * @returns the <code>document</code> element for the dialog box content.
   */
  getContentDocument: function () {
    return this._frameDocument;  
  },
  /** Returns the <code>window</code> element for the dialog box content.
   * 
   * @returns the <code>window</code> element for the dialog box content.
   */
  getContentWindow: function () {
    return this._frameWindow;  
  },
  /** Sets the hide-only attribute of the dialog box.
   * 
   * @param value Set this to <code>true</code> to hide the dialog when
   * one of the buttons is clicked, and <code>false</code> to close the dialog when
   * one of the buttons is clicked.
   */
  setHideOnly: function(value) {
    this._isHideOnly = value;
  },
  /** Invoke a click on the OK button. */
  clickOk: function () {
    var resultsFunc = this._frameWindow.getResults;
    var results;
    if (resultsFunc)
      results = resultsFunc();
    if (this._fire("okClick", { results: results })) {
      if (this._isHideOnly) {
        this.hide();
      } else {
        this.close();
      }
    }
  },
  /** Invoke a click on the Cancel button */
  clickCancel: function () {
    if (this._fire("cancelClick", { })) {
      if (this._isHideOnly) {
        this.hide();
      } else {
        this.close();
      }
    }
  },
  /** Returns the DOM element for the OK button.
   * 
   * @returns {Element} The DOM element for the OK button.
   */
  getOkButtonElement: function () {
    return this._okButton;
  },
  /** Returns the DOM element for the Cancel button.
   * 
   * @returns {Element} The DOM element for the Cancel button.
   */
  getCancelButtonElement: function () {
    return this._cancelButton;
  },
  /** Enable the OK button.
   * 
   */
  enableOkButton: function () {
    this._okButton.removeAttribute("disabled");
  },
  /** Disable the OK button.
   * 
   */
  disableOkButton: function () {
    this._okButton.setAttribute("disabled", "disabled");
  },
  /** Enable the Cancel button.
   * 
   */
  enableCancelButton: function () {
    this._cancelButton.removeAttribute("disabled");
  },
  /** Disable the Cancel button.
   * 
   */
  disableCancelButton: function () {
    this._cancelButton.setAttribute("disabled", "disabled");
  },
  _onOkButtonClick: function (event) {
    this.clickOk();
  },
  _onCancelButtonClick: function (event) {
    this.clickCancel();
  },
  _onDialogContentLoad: function (event) {
    if (!this._contentLoaded) {
      this._contentLoaded = true;
      this._frameDocument = this.getContentElement().contentDocument;
      this._frameWindow = this._frameDocument.parentWindow;
      if (!this._frameWindow)
        this._frameWindow = this._frameDocument.defaultView;
      
      var _this = this;
      this._frameWindow.getDialog = function () {
        return _this;
      };
      
      Event.observe(window, "mouseup", this._windowMouseUpListener);
      Event.observe(document, "mouseup", this._windowMouseUpListener);
      Event.observe(this._frameDocument , "mouseup", this._windowMouseUpListener);
      Event.observe(this._frameWindow, "mouseup", this._windowMouseUpListener);
      
      if (this._pendingContent)
        this._frameDocument.body.innerHTML = this._pendingContent;
      
      if (this._autoEvaluateSize == true) {
        var windowFramesHeight = this._titleBar.getHeight() + this._titleBar.getHorizontalPaddings() + this._titleBar.getHorizontalMargins() + this._buttonsContainer.getHeight() + this._buttonsContainer.getHorizontalPaddings() + this._buttonsContainer.getHorizontalMargins();
      
        var contentHeight = 0;
        var contentWidth = 0;
        var children = this._frameDocument.body.childNodes;
        for (var i = 0; i < children.length; i++) {
          var node = $(children[i]);
          if (node.nodeType == 1) {
            if (node.getReservedHeight) {
              contentHeight += node.getReservedHeight();
              contentWidth += node.getReservedWidth();
            }
          }
        }
       
        $(this._frameDocument.body).setStyle({
          height: contentHeight + 'px',
          width: contentWidth + 'px',
          overflow: 'hidden'
        });
       
        this.getContentElement().setStyle({
          height: contentHeight + 'px',
          width: contentWidth + 'px' 
        });
       
        this._dialogNode.setStyle( {
          height: (contentHeight + windowFramesHeight) + 'px'
        });
        
        this._recalculateSize();
      }
  
      // Event.stopObserving(this._dialogContent, "load", this._dialogContentLoadListener);
      
      if (this._appearDuration > 0) {
        var _this = this;
        new Effect.Appear(this._dialogNode, {
          duration: this._appearDuration,
          afterFinish: function () {
            _this._fire("onLoad", { });
          }
        });
      } else {
        this._fire("onLoad", { });
      }
    } else {
      Event.stopObserving(window, "mouseup", this._windowMouseUpListener);
      Event.stopObserving(document, "mouseup", this._windowMouseUpListener);
      Event.stopObserving(this._frameDocument , "mouseup", this._windowMouseUpListener);
      Event.stopObserving(this._frameWindow, "mouseup", this._windowMouseUpListener);
      
      this._frameDocument = this.getContentElement().contentDocument;
      this._frameWindow = this._frameDocument.parentWindow;
      if (!this._frameWindow)
        this._frameWindow = this._frameDocument.defaultView;
      
      Event.observe(window, "mouseup", this._windowMouseUpListener);
      Event.observe(document, "mouseup", this._windowMouseUpListener);
      Event.observe(this._frameDocument , "mouseup", this._windowMouseUpListener);
      Event.observe(this._frameWindow, "mouseup", this._windowMouseUpListener);
    }
  },
  _onWindowResized : function(event) {
    this._recalculateSize();
  },
  _fire : function(eventName, extraInfo) {
    var eventObj = Object.extend( {
      name: eventName,
      dialog: this,
      _preventDefault: false,
      preventDefault: function (b) {
        this._preventDefault = b || true;
      }
    }, extraInfo);
    
    for ( var i = 0; i < this._listeners.length; i++) {
      this._listeners[i].call(this, eventObj);
    }
    
    return !eventObj._preventDefault;
  },
  _recalculateSize : function() {
    if (this._isOpen == true) {
      if (this._centered == true) {
        this.center();
      }

      var contentElement = $(this.getContentElement());

      if (contentElement) {
        var maxHeight = Element.getMaxHeight(contentElement);
        contentElement.setStyle({
          height: maxHeight + 'px'
        });
      }
      
      this._fire("resized", {});
    }
  },
  _onWindowMouseMove: function(event) {
    if (this._dragging == true) {
      var mx = Event.pointerX(event);
      var my = Event.pointerY(event);

      this._dialogNode.setStyle({
        top: (this._dialogNode.getStyleInPixels('top') + (my - this._dragY)) + 'px',
        left: (this._dialogNode.getStyleInPixels('left') + (mx - this._dragX)) + 'px'
      });
      
      this._dragX = mx;
      this._dragY = my;
    }
  },
  _onTitleBarMouseDown: function(event) {
    if (this._dragging == false) {
      this._dragging = true;
      this._dragX = Event.pointerX(event);
      this._dragY = Event.pointerY(event);
      
      // Temp glasspane is needed due to dialog content being in an event eating iframe that
      // easily stops dragging when the dialog is dragged downwards quickly enough for the
      // cursor to get on top of the iframe.
      
      var glassPane = new Element("div");
      glassPane.setStyle({
        position: 'absolute',
        top: '0px',
        left: '0px',
        height: this._dialogNode.offsetHeight + 'px',
        width: '100%'
      });
      glassPane.id = 'dialogDragGlasspane';
      this._dialogNode.appendChild(glassPane);
    }
  },
  _onWindowMouseUp: function(event) {
    this._dragging = false;
      
    // Remove the glasspane inserted in _onTitleBarMouseDown
      
    var glassPane = $('dialogDragGlasspane');
    if (glassPane) {
      glassPane.remove();
    }
  }
});

/** Return the dialog with the given ID.
 * 
 * @param id The ID of the dialog.
 * @returns The dialog whose ID is <code>id</code>
 */
function getDialog(id) {
  return _Dialogs.get(id);
}
