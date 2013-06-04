IxProtoTabs = Class.create(
  /** @lends IxProtoTabs.prototype */
  {
  /** Create a new tab bar.
   * @class A tab bar that groups the a web page visually into tabs that
   * are displayed one at a time.
   * @constructs
   * 
   * @param tabId The DOM ID of the tab bar's placeholder.
   * @param options Additional options for this tab bar.
   */
  initialize: function (tabId, options) {
    this._tabContainer = $(tabId);
    this._listeners = null;
    this._options = options;
    this._labelClickListener = this._onLabelClick.bindAsEventListener(this); 
    this._tabNames = new Array(); 
    this._tabs = new Hash();
    this._labels = new Hash();
		this._initializedTabs = new Array();

    this._labelElements = this._tabContainer.getElementsByTagName("a"); 
    for (var i = 0; i < this._labelElements.length; i++) { 
      var element = this._labelElements[i];   
      var tabName = this._getLinkkedTabName(element);
       
      Event.observe(element, "click", this._labelClickListener);
      this._tabNames.push(tabName);
      this._labels.set(tabName, element);
    }
    
    for (var i = 0; i < this._tabNames.length; i++) {
      var tabElement = $(this._tabNames[i]);
      if ((tabElement != null) && (tabElement != undefined)) {
        this._tabs.set(this._tabNames[i], tabElement);
      } else 
        throw new Error(this._tabNames[i] + " cannot be found");
    }
    
    this._activeTab = null; 
    this._activeLabel = null;
    
    if (this._tabNames.length > 0) {
      var selectedTab = this._tabNames[0];
      try {
        var hash = location.hash;
        if (hash && hash.length > 4) {
          if (hash.startsWith('#at-')) {
            selectedTab = hash.substring(4);
            if (!this._hasTab(selectedTab)) {
              selectedTab = this._tabNames[0];
            }
          }
        }
      } catch (e) {
        selectedTab = this._tabNames[0];
      }
       
      this.setActiveTab(selectedTab);
    }

    if (this._options) {
      if (this._options.tabAddContextMenu || (Object.isFunction(this._options.tabAddAction))) {
        this._addTabButton = new Element("div", {className: "tabAdd tabLabel"} );
        this._addTabButton.update("+");
        this._labelAddClickListener = this._onLabelAddClick.bindAsEventListener(this); 
        Event.observe(this._addTabButton, "click", this._labelAddClickListener);
        this._tabContainer.appendChild(this._addTabButton);
        
        if (this._options.tabAddContextMenu)
          this._contextMenuItemClickListener = this._onContextMenuItemClick.bindAsEventListener(this);
      }
    }
  },
  /** Add a new tab to this tab bar.
   * 
   * @param name The name of the UI element containing the tab's content.
   * @param caption The caption for the new tab.
   * @returns {Element} The newly-created tab content UI element.
   */
  addTab: function (name, caption) {
    var newTabLabel = new Element("a", {className: "tabLabel", href: "#" + name} );
    newTabLabel.update(caption);
    
    if (!this._addTabButton)
      this._tabContainer.appendChild(newTabLabel);
    else
      this._tabContainer.insertBefore(newTabLabel, this._addTabButton);

    var afterElement = this._tabContainer.parentNode;
    var newTabContent = new Element("div", { id: name, className: "tabContent hiddenTab" });
    
    if (this._tabNames.length > 0) {
      afterElement = $(this._tabNames[this._tabNames.length - 1]);
    }
    
    if (afterElement.nextSibling) {
      this._tabContainer.parentNode.insertBefore(newTabContent, afterElement.nextSibling);
    } else {
      this._tabContainer.parentNode.appendChild(newTabContent);
    }
    
    Event.observe(newTabLabel, "click", this._labelClickListener);
    this._tabNames.push(name);
    this._labels.set(name, newTabLabel);
    this._tabs.set(name, newTabContent);
    
    return newTabContent;
  },
  /** Sets the tab named <code>name</code> active.
   * 
   * @param name The name of the tab to set active.
   */
  setActiveTab: function (name) {
    var tab = this._tabs.get(name);
    var label = this._labels.get(name);
    
    if (this._activeTab != null) {
      this._activeTab.removeClassName("activeTab");
    }
      
    if (this._activeLabel != null) {
      this._activeLabel.removeClassName("activeTabLabel");
    }
    
    tab.addClassName("activeTab");
    label.addClassName("activeTabLabel");
     
    var _this = this;
    this._tabs.each(function(pair) {
      pair.value.addClassName("hiddenTab");
    });
    
    tab.removeClassName("hiddenTab");
    this._activeTab = tab;
    this._activeLabel = label;
		
		if (this._initializedTabs.indexOf(name) == -1) {
			this._initializedTabs.push(name);
			this._fire({ 
			  action: "tabInitialized",
				name: name
			});
		}
    
    this._fire({ 
     action: "tabActivated",
        name: name
    });
    
    location.hash = 'at-' + name;
  },
  /** Returns the name of the active tab, or null if no tab is active.
   * 
   * @returns the name of the active tab, or null if no tab is active.
   */
  getActiveTab: function () {
    return this._activeTab ? this._activeTab.id : null;
  },
  _hasTab: function (name) {
    return this._tabs.get(name);
  },
  /** Add an event listener to this tab bar.
   * 
   * @param listener The listener (<code>function(event) {}</code>) to add.
   */
	addListener: function (listener) {
		if (this._listeners == null)
		  this._listeners = new Array();
			
		this._listeners.push(listener);
	},
  _onLabelClick: function (event) {
    Event.stop(event);
    var element = Event.element(event);
    if (element.tagName != 'A')
      element = element.up("a");
    
    var tabName = this._getLinkkedTabName(element);
    this.setActiveTab(tabName);
  },
  _onLabelAddClick: function (event) {
    Event.stop(event);
    
    if (Object.isFunction(this._options.tabAddAction))
      this._options.tabAddAction();
    else {
      if (this._options.tabAddContextMenu) {
        var contextMenuButton = Event.element(event);
        var contextMenu = this._options.tabAddContextMenu;
        
        var menuContainer = new Element("div", {className: "ixPrototabsContextMenu"} );
        
        for (var i = 0, l = contextMenu.length; i < l; i++) {
          var menuItem = contextMenu[i];
          var menuElement = new Element("div");
          menuElement._menuItem = menuItem;

          if (!("-" === menuItem.text)) {
            menuElement.addClassName("ixPrototabsContextMenuItem");
            menuElement.update(menuItem.text);

            if ((!Object.isFunction(contextMenu[i].isEnabled)) || (contextMenu[i].isEnabled()))
              Event.observe(menuElement, "click", this._contextMenuItemClickListener);
            else
              menuElement.addClassName("ixPrototabsContextMenuItemDisabled");
          } else {
            menuElement.addClassName("ixPrototabsContextMenuItemSpacer");
          }
          menuContainer.appendChild(menuElement);
        }
        
        var _this = this;
        var windowMouseMove = function (event) {
          var element = Event.element(event);
          var overMenu = element.hasClassName('ixPrototabsContextMenu');
          if (!overMenu) {
            if (element.up('.ixPrototabsContextMenu'))
              overMenu = true;
          }
          if (!overMenu) {
            if (element.hasClassName('tabAdd') || element.up('.tabAdd'))
              overMenu = true;
          }
        
          if (!overMenu) {
            $$('.ixPrototabsContextMenu').forEach(function (menu) {
              $(menu).select('.ixPrototabsContextMenuItem').forEach(function (menuItem) {
                if (!menuItem.hasClassName("ixPrototabsContextMenuItemSpacer"))
                  Event.stopObserving(menuItem, "click", _this._contextMenuItemClickListener);
              }); 
              
              $(menu).remove();
            });
            
            Event.stopObserving(Prototype.Browser.IE ? document : window, "mousemove", windowMouseMove);
          }
        };
        
        Event.observe(Prototype.Browser.IE ? document : window, "mousemove", windowMouseMove);
        
        this._addTabButton.appendChild(menuContainer);
      }
    }
    
    this._fire({ 
      action: "afterTabAdd",
      event: event
    });
  },
  _onContextMenuItemClick: function (event) {
    Event.stop(event);
    var menuElement = Event.element(event);
    var contextMenuElement = menuElement.parentNode;
    
    var menuItem = menuElement._menuItem;
    var _this = this;
    contextMenuElement.select('.ixTableCellContextMenuItem').forEach(function (menuItem) {
      Event.stopObserving(menuItem, "click", _this._contextMenuItemClickListener);
    }); 

    contextMenuElement.remove();
    
    menuItem.onclick({
      tabComponent: this,
      menuItem: menuItem
    });
  },
  _getLinkkedTabName: function (element) {
    var ind = element.href.indexOf("#");
    var result = "";
    if (ind != -1) 
      result = element.href.substring(ind + 1);
    
    return result;
  },
	_fire: function (event) {
		if (this._listeners != null) {
			for (var i = 0; i < this._listeners.length; i++) 
				this._listeners[i].call(this, event);
		};
	}
});