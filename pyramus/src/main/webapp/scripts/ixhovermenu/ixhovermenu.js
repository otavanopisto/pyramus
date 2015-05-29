IxHoverMenu = Class.create(
  /** @lends IxHoverMenu.prototype */
  {
  /** Creates a new hover menu.
   * @class A hover dropdown menu.
   * @constructs
   * 
   * @param parentNode The DOM node to attach this menu to.
   * @param options Additional options for the hover menu.
   */
  initialize : function(parentNode, options) {
    this.domNode = new Element("div", {className: "IxHoverMenu"});
    this._items = new Array();
    this._parentNode = parentNode;
    this._parentNode.appendChild(this.domNode);
    this._textNode = new Element("div", {className: "IxHoverMenuText"}).update(options.text);
    this._arrowNode = new Element("div", {className: "IxHoverMenuTextArrow"}).update("▼");
    this._textContainer = new Element("div", {className: "IxHoverMenuTextContainer"});
    
    this._itemsContainer = new Element("div", {className: "IxHoverMenuItemsContainer"});
    this._textContainer.appendChild(this._textNode);
    this._textContainer.appendChild(this._arrowNode);
    this.domNode.appendChild(this._textContainer);
    this.domNode.appendChild(this._itemsContainer);
  },
  /** Adds an item to this hover menu. The item is
   * an instance of one of the <code>IxHoverMenu*</code>
   * classes.
   * @see IxHoverMenuSpacer
   * @see IxHoverMenuItem
   * @see IxHoverMenuLinkItem
   * @param item The item to add to this hover menu.
   */
  addItem: function (item) {
    this._itemsContainer.appendChild(item.domNode);
    this._items.push(item);
  }
});

IxHoverMenuSpacer = Class.create(
  /** @lends IxHoverMenuSpacer.prototype */
  {
  /** Creates a new spacer for <code>IxHoverMenu</code>.
   * @class A spacer for <code>IxHoverMenu</code>
   * @constructs
   * @see IxHoverMenu
   */
  initialize : function() {
    this.domNode = new Element("div", {className: "IxHoverMenuSpacer"});
  }
});

IxHoverMenuItem = Class.create(
  /** @lends IxHoverMenuItem.prototype */
  {
  /** Base constructor for hover menu items. Do not call this directly.
   * @class Base class for hover menu items.
   * @constructs
   * @see IxHoverMenuLinkItem
   * @see IxHoverMenuClickableItem
   */
  initialize : function(options) {
    this._options = options;
    this.domNode = new Element("div", {className: "IxHoverMenuItem"});
    this._textNode = new Element("div", {className: "IxHoverMenuItemText"}).update(options.text);
    this.domNode.appendChild(this._textNode);
    
    if (options.iconURL) {
      if (options.iconOpacity)
        this._iconNode = new Element("div", {className: "IxHoverMenuItemIcon", style: "background-image: url(" + options.iconURL + "); opacity: " + options.iconOpacity});
      else
        this._iconNode = new Element("div", {className: "IxHoverMenuItemIcon", style: "background-image: url(" + options.iconURL + ')'});
      this.domNode.appendChild(this._iconNode);
    }
  }
});

IxHoverMenuLinkItem = Class.create(IxHoverMenuItem,
  /** @lends IxHoverMenuLinkItem.prototype */
  {
  /** Creates a new hover menu item containing a link.
   * @class A hover menu item containing a link.
   * @constructs
   * 
   * @param $super The super object of the new object.
   * @param options An object containing the following property:
   * <dl>
   *   <dt><code>link</code></dt>
   *   <dd>The URL of the link's target</dd>
   * </dl>
   */
  initialize : function($super, options) {
    $super(options);
    this._itemClickListener = this._onItemClick.bindAsEventListener(this);
    Event.observe(this.domNode, "click", this._itemClickListener);
  },
  _onItemClick: function (event) {
    window.location.href = this._options.link;
  }
});

IxHoverMenuClickableItem = Class.create(IxHoverMenuItem, 
  /** @lends IxHoverMenuClickableItem.prototype */
  {
  /** Creates a new hover menu item that can be clicked.
   * @class A hover menu item that can be clicked.
   * @constructs
   * 
   * @param $super The super object of the new object.
   * @param options An object containing the following property:
   * <dl>
   *   <dt><code>click</code></dt>
   *   <dd>An event handler (<code>function(event) {}</code>) that is
   *   called whenever the menu item is clicked.</dd>
   * </dl>
   */
  initialize : function($super, options) {
    $super(options);
    this._itemClickListener = this._onItemClick.bindAsEventListener(this);
    Event.observe(this.domNode, "click", this._itemClickListener);
  },
  _onItemClick: function (event) {
    this._options.onclick.call(window, {
      
    });
  }
});

