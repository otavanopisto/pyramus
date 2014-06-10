var _ixSearchNavigations = new Hash();

IxSearchNavigation = Class.create(
  /** @lends IxSearchNavigation.prototype */
  {
  /** Creates a new search results navigation bar.
   * @class A search results navigator. Search can provide a large number
   * of results, that are broken down to pages. This element provides buttons to
   * navigate those pages.
   * @constructs
   * 
   * @param containerNode The DOM node to place the navigation bar into.
   * @param options The options that describe the navigation element, as an object containing the following properties:
   * <dl>
   *   <dt><code>maxNavigationPages</code></dt>
   *   <dd>The maximum number of numbered buttons visible.</dd>
   *   <dt><code>onClick</code></dt>
   *   <dd>An event handler (<code>function(event) {}</code>) that's invoked
   *   whenever the page is changed.</dd>
   *   <dt><code>firstText</code></dt>
   *   <dd>The text for the button that leads to the first page,
   *   defaults to <code>"<<"</code>.</dd>
   * </dl>
   * 
   */
  initialize : function(containerNode, options) {
    this._containerNode = containerNode;
    this._options = options;
    this._pages = new Array();

    var centerPage = this.getMaxNavigationPages() / 2;
    this._centerNavigationPage = centerPage % 2 == 0 ? centerPage : (centerPage)|0;
    
    if (options.id) {
      _ixSearchNavigations.set(options.id, this);
    }

    this.domNode = new Element("div", {className: "IxSearchNavigation"});
    containerNode.appendChild(this.domNode);
    
    this._firstPage = new IxSearchNavigationPage(this.domNode);
    this._firstPage.setPage(0);
    this._firstPage.setText(options.firstText ? options.firstText : "&lt;&lt;");
    this._firstPage.addListener("linkClick", this, this._onPageClick);

    this._previousPage = new IxSearchNavigationPage(this.domNode);
    this._previousPage.setText(options.previousText ? options.previousText : "&lt;");
    this._previousPage.addListener("linkClick", this, this._onPageClick);
    
    for (var i = 0; i < options.maxNavigationPages; i++) {
      var page = new IxSearchNavigationPage(this.domNode);
      page.addListener("linkClick", this, this._onPageClick);
      this._pages.push(page);
    }

    this._nextPage = new IxSearchNavigationPage(this.domNode);
    this._nextPage.setText(options.nextText ? options.nextText : "&gt;");
    this._nextPage.addListener("linkClick", this, this._onPageClick);

    this._lastPage = new IxSearchNavigationPage(this.domNode);
    this._lastPage.setText(options.lastText ? options.lastText : "&gt;&gt;");
    this._lastPage.addListener("linkClick", this, this._onPageClick);
  },
  /** Returns the first page number whose button is visible.
   * 
   * @returns {Number} the first page number whose button is visible.
   */
  getFirstVisiblePage: function() {
    if (this.getTotalPages() <= this.getMaxNavigationPages() || this.getCurrentPage() < this.getCenterNavigationPage()) {
      return 0;
    }
    else if (this.getCurrentPage() >= this.getTotalPages() - this.getCenterNavigationPage()) {
      return this.getTotalPages() - this.getMaxNavigationPages();
    }
    else {
      return this.getCurrentPage() - this.getCenterNavigationPage();
    }
  },
  /** Returns the number of the page whose button is in the middle of the nav bar.
   * 
   * @returns {Number} the number of the page whose button 
   */
  getCenterNavigationPage: function() {
    return this._centerNavigationPage;
  },
  /** Returns the last page number whose button is visible.
   * 
   * @returns {Number} The last page number whose button is visible.
   */
  getLastVisiblePage: function() {
    if (this.getTotalPages() <= this.getMaxNavigationPages() || this.getCurrentPage() >= this.getTotalPages() - this.getCenterNavigationPage()) {
      return this.getTotalPages() - 1;
    }
    else if (this.getCurrentPage() < this.getCenterNavigationPage()) {
      return this.getMaxNavigationPages() - 1;
    }
    else {
      return this.getCurrentPage() + this.getCenterNavigationPage();
    }
  },
  /** Navigates to <code>currentPage</code>
   * 
   * @param {Number} currentPage The number of the page to navigate to.
   */
  setCurrentPage: function(currentPage) {
    if (this._currentPage != currentPage) {
      this._currentPage = currentPage;
      this._previousPage.setPage(currentPage > 0 ? currentPage - 1 : 0);
      this._nextPage.setPage(currentPage < this.getTotalPages() - 1 ? currentPage + 1 : currentPage);
      this._updateNavigation();
    }
  },
  /** Returns the currently visible page number.
   * 
   * @returns {Number} The currently visible page number.
   */
  getCurrentPage: function() {
    return this._currentPage;
  },
  /** Sets the total number of pages.
   * 
   * @param {Number} totalPages The total number of pages. 
   */
  setTotalPages: function(totalPages) {
    if (this._totalPages != totalPages) {
      this._totalPages = totalPages;
      this._lastPage.setPage(totalPages - 1);
      this._currentPage = null;
    }
  },
  /** Returns the total number of pages.
   * 
   * @returns {Number} The total number of pages.
   */
  getTotalPages: function() {
    return this._totalPages;
  },
  /** Returns the maximum number of numbered buttons visible.
   * 
   * @returns {Number} The maximum number of numbered buttons visible.
   */
  getMaxNavigationPages: function() {
    return this._options.maxNavigationPages;
  },
  _updateNavigation: function() {
    var firstVisiblePage = this.getFirstVisiblePage();
    var lastVisiblePage = this.getLastVisiblePage();
    
    this._firstPage.setVisible(this.getTotalPages() > 0);
    this._previousPage.setVisible(this.getTotalPages() > 0);
    if (this.getTotalPages() > 0) {
      this._firstPage.setEnabled(this.getCurrentPage() > 0);
      this._previousPage.setEnabled(this.getCurrentPage() > 0);
    }
    
    var page = firstVisiblePage;
    for (var i = 0; i < this.getMaxNavigationPages(); i++) {
      this._pages[i].setVisible(page <= lastVisiblePage);
      if (this._pages[i].isVisible()) {
        this._pages[i].setPage(page);
        this._pages[i].setText("" + (page + 1));
        this._pages[i].setActive(page == this.getCurrentPage());
      }
      page++;
    }
    
    this._nextPage.setVisible(this.getTotalPages() > 0);
    this._lastPage.setVisible(this.getTotalPages() > 0);
    if (this.getTotalPages() > 0) {
      this._nextPage.setEnabled(this.getCurrentPage() < this.getTotalPages() - 1);
      this._lastPage.setEnabled(this.getCurrentPage() < this.getTotalPages() - 1);
    }
  },
  _onPageClick: function (event) {
    this._options.onclick.call(window, {
      page: event.page
    });
  }
});

IxSearchNavigationPage = Class.create(
  /** @lends IxSearchNavigationPage.prototype */
  {
  /** Creates a new page button for search navigation.
   * @class A page button for search navigation.
   * @constructs
   * 
   * @param containerNode The DOM node to place the page button into.
   */
  initialize : function(containerNode) {
    this._containerNode = containerNode;
    this.domNode = new Element("div", {className: "IxSearchNavigationPageContainer"});
    this._pageLink = new Element("span", {className: "IxSearchNavigationPageLink"});
    this.domNode.appendChild(this._pageLink);
    this._pageLinkClickListener = this._onPageLinkClick.bindAsEventListener(this);
    Event.observe(this.domNode, "click", this._pageLinkClickListener);
    this.setVisible(false);
    containerNode.appendChild(this.domNode);
  },
  /** Sets the page number of this button.
   * 
   * @param {Number} page The page number of this page.
   */
  setPage: function(page) {
    this._page = page;
  },
  /** Sets the text of this button.
   *
   * @param {String} text The text of this page.
   */
  setText: function(text) {
    this._pageLink.innerHTML = text;
  },
  /** Sets the active state of this button.
   * 
   * @param {Boolean} active Set this to <code>true</code> to activate this
   * button, and <code>false</code> to deactivate this button.
   */
  setActive: function(active) {
    if (active == true) {
      this._pageLink.addClassName('IxSearchNavigationPageLinkActive');
    }
    else {
      this._pageLink.removeClassName('IxSearchNavigationPageLinkActive');
    }
  },
  /** Sets the enabled state of this button.
   * 
   * @param {Boolean} enabled Set this to <code>true</code> to enable this
   * button, and <code>false</code> to disable this button.
   */
  setEnabled: function(enabled) {
    this._enabled = enabled;
    if (enabled == true) {
      this.domNode.removeClassName("IxSearchNavigationPageContainerDisabled");
    }
    else {
      this.domNode.addClassName("IxSearchNavigationPageContainerDisabled");
    }
  },
  /** Sets the visibility state of this button.
   * 
   * @param {Boolean} visible The new visibility state of this button. 
   */
  setVisible: function(visible) {
    this._visible = visible;
    if (visible == true) {
      this.domNode.setStyle({
        visibility: 'visible',
        display: ''
      });
    }
    else {
      this.domNode.setStyle({
        visibility: '',
        display: 'none'
      });
    }
  },
  isVisible: function() {
    return this._visible;
  },
  _onPageLinkClick: function (event) {
    this.fire("linkClick", {
      page: this._page
    });
  }
});

Object.extend(IxSearchNavigationPage.prototype, fni.events.FNIEventSupport);

function getSearchNavigationById(id) {
  return _ixSearchNavigations.get(id);
}
