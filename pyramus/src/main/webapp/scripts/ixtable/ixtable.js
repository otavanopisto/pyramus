/* TODO: DatePicker locales */ 
var _ixTables = new Hash();

IxTableControllers = {
  registerController: function (controller) {
    this._controllers.set(controller.getDataType(), controller);
  },
  getController: function (dataType) {
    return this._controllers.get(dataType);
  },
  EDITMODE_EDITABLE: 0,
  EDITMODE_NOT_EDITABLE: 1, 
  EDITMODE_ONLY_EDITABLE: 2,
  _controllers: new Hash()  
};

IxTable = Class.create({
  initialize : function(parentNode, options) {
    this._rowClickListener = this._onRowClick.bindAsEventListener(this);
    this._headerClickListener = this._onHeaderClick.bindAsEventListener(this);
    this._activeRows = new Hash();
    this._rowElements = new Hash();
    this._filters = new Array();
  
    this._headerRowContent = new Element("div", { className : "ixTableRowContent" });
    
    this._headerRow = new Element("div", { className : "ixTableHeaderRow" });
    this._headerRow.appendChild(this._headerRowContent);

    this._content = new Element("div", { className : "ixTableContent" });
    
    this._rowCount = new Element("input", { type: 'hidden', name: options.id + '.rowCount', value: 0 });
    
    var classNames = "ixTable";
    
    if (options.rowHoverEffect === true)
      classNames += " ixTableRowHoverEffect";
    
    this.domNode = new Element("div", { className : classNames });
    this.domNode.appendChild(this._headerRow);
    this.domNode.appendChild(this._content);
    this.domNode.appendChild(this._rowCount);

    this._headerCells = new Object();
    this._cellEditors = new Hash();
    parentNode.appendChild(this.domNode);
    
    this._hasHeader = false;
    
    this.options = options;
    for (var i = 0; i < options.columns.length; i++) {
      var column = options.columns[i];
      
      this._hasHeader = this._hasHeader || !((column.header == '') || (!column.header));
      
      var headerContent = undefined;

      if (column.headerimg) {
        var headerImgClassNames = "ixTableHeaderCellImage";
        if (Object.isFunction(column.headerimg.onclick))
          headerImgClassNames = headerImgClassNames + " ixTableHeaderCellImageButton";
        
        headerContent = new Element("img", { 
          src: column.headerimg.imgsrc, 
          title: column.headerimg.tooltip ? column.headerimg.tooltip : '', 
          className: headerImgClassNames
        });
        
        if (Object.isFunction(column.headerimg.onclick)) {
          headerContent._headerOnClick = column.headerimg.onclick;
          headerContent._columnIndex = i;
          Event.observe(headerContent, "click", this._headerClickListener);
        }
      } else {
        headerContent = new Element("div", { className : "ixTableHeaderCellText" });
        headerContent.update(column.header);
      }
      
      var headerCell = new Element("div", { className : "ixTableHeaderCell", title: column.headerTooltip ? column.headerTooltip : '' });
      headerCell.update(headerContent);

      this._clearColumnFiltersClickListener = this._onClearColumnFiltersClickListener.bindAsEventListener(this);
      var clearFilterButton = new Element("span", { className : "ixTableClearFilterButton" });
      clearFilterButton._column = i;
      headerCell.appendChild(clearFilterButton);
      Event.observe(clearFilterButton, "click", this._clearColumnFiltersClickListener);
      
      if (column.sortAttributes) {
        this._sortColumnClickListener = this._onSortColumnClick.bindAsEventListener(this);

        if (column.sortAttributes.sortAscending) {
          var sortAscendingBtn = new Element("span", { className : "ixTableHeaderSortButton ixTableHeaderSortButtonAscending", title: column.sortAttributes.sortAscending.toolTip });
          
          sortAscendingBtn._sortAction = new column.sortAttributes.sortAscending.sortAction(i, "asc");
          headerCell.appendChild(sortAscendingBtn);

          Event.observe(sortAscendingBtn, "click", this._sortColumnClickListener);
        }
        if (column.sortAttributes.sortDescending) {
          var sortDescendingBtn = new Element("span", { className : "ixTableHeaderSortButton ixTableHeaderSortButtonDescending", title: column.sortAttributes.sortDescending.toolTip });
          
          sortDescendingBtn._sortAction = new column.sortAttributes.sortDescending.sortAction(i, "desc");
          headerCell.appendChild(sortDescendingBtn);

          Event.observe(sortDescendingBtn, "click", this._sortColumnClickListener);
        }
      }
      
      if ((column.left != undefined) && (column.left != NaN)) {
        headerCell.setStyle( {
          left : column.left + 'px'
        });
      };

      if ((column.right != undefined) && (column.right != NaN)) {
        headerCell.setStyle( {
          right : column.right + 'px'
        });
      };
      
      if ((column.width != undefined) && (column.width != NaN)) {
        headerCell.setStyle( {
          width : column.width + 'px'
        });
      };

      if (column.hidden == true)
        headerCell.hide();
      
      this._headerCells[i] = headerCell;

      this._headerRowContent.appendChild(headerCell);
    }
    
    this._headerRow.setStyle({
      display: 'none'
    });
    
    if (options.id) {
      _ixTables.set(options.id, this);
      document.fire("ix:tableAdd", {
        tableComponent: this 
      });
      
      this.domNode.setAttribute("ix:tableid", options.id);
    }

    this._contextMenuButtonClickListener = this._onContextMenuButtonClick.bindAsEventListener(this);
    this._contextMenuItemClickListener = this._onContextMenuItemClick.bindAsEventListener(this);
  },
  addRow : function(values, editable) {
    return this._addRows([values], editable);
  },
  addRows: function (rowDatas, editable) {
    this.detachFromDom();
    var rowCount = this._addRows(rowDatas, editable);
    this.reattachToDom();
    return rowCount;
  },
  _addRows: function (rowDatas, editable) {
    var rowNumber = this.getRowCount() - 1;
    var rowElements = new Array(); 
    var columnCount = this.options.columns.length;
    
    for (var rowIndex = 0, rowCount = rowDatas.length; rowIndex < rowCount; rowIndex++) {
      rowNumber++;
      
      var values = rowDatas[rowIndex];
      
      if (values.length != this.options.columns.length) {
        throw new Error("Value array length (" + values.length + ") != table column length (" + this.options.columns.length + ")");
      }
      
      var rowContent = new Element("div", { className : "ixTableRowContent" });
      var row = new Element("div", { className : "ixTableRow" });
      row._rowNumber = rowNumber;
      row.appendChild(rowContent);
      this._rowElements.set(rowNumber, row);
      
      if (this.options.rowClasses) {
        for (var i = 0, l = this.options.rowClasses.length; i < l; i++) {
          row.addClassName(this.options.rowClasses[i]);
        }
      }

      for (var i = 0; i < columnCount; i++) {
        var column = this.options.columns[i];
        var name = this.options.id ? this.options.id + '.' + rowNumber + '.' + (column.paramName ? column.paramName : i) : '';
        
        var cell = new Element("div", { className : "ixTableCell" });
        cell._column = i;
        
        var cellStyles = {};
        var hasStyles = false;

        if ((column.left != undefined) && (column.left != NaN)) {
          cellStyles.left = column.left + 'px';
          hasStyles = true;
        }
        
        if ((column.right != undefined) && (column.right != NaN)) {
          cellStyles.right = column.right + 'px';
          hasStyles = true;
        }
        
        if ((column.width != undefined) && (column.width != NaN)) {
          cellStyles.width = column.width + 'px';
          hasStyles = true;
        }

        var value = values[i];
        if (typeof value == 'object' && value instanceof Object && !(value instanceof Array)) {
          if (value.tooltip) {
            cell.title = value.tooltip;
          }
          if (value.extraClass) {
            cell.addClassName(value.extraClass);
          }
          value = value.value;
        }

        if (hasStyles) {
          cell.setStyle(cellStyles);
        }
        
        var cellContentHandler = this._createCellContentHandler(name, column, editable); 
        rowContent.appendChild(cell);
        var cellController = IxTableControllers.getController(column.dataType);
        cellController.attachContentHandler(this, cell, cellContentHandler);
        cellController.setEditorValue(cellContentHandler, value);
        
        if (this._hasHeader == true) {
          this._headerRow.setStyle({
            display: ''
          });
        }
      }    
      
      this._setRowCount(this.getRowCount() + 1);
      
      Event.observe(row, "click", this._rowClickListener);
      
      rowElements.push(row);
    }
    
    for (var i = 0, l = rowElements.length; i < l; i++) {
      this._content.appendChild(rowElements[i]);
      var rowNumber = rowElements[i]._rowNumber;
      
      this.fire("rowAdd", {
        tableComponent: this,
        row: rowNumber
      });
      
      for (var j = 0; j < columnCount; j++) {
        this.fire("cellValueChange", {
          tableComponent: this,
          row: rowNumber,
          column: j, 
          value: rowDatas[i][j]
        });
      }
    }
    
    return rowNumber;
  },
  deleteRow: function (rowNumber) {
    this._deleteRow(rowNumber);
    this._redoFilters();
    this._redoSort();
  },
  hideRow: function (rowNumber) {
    var rowElement = this.getRowElement(rowNumber);
    var doHide = rowElement.visible();

    if (doHide && this.fire("beforeRowVisibilityChange", {
      tableComponent: this,
      rows: [ rowNumber ],
      hidden: false
    })) {
      rowElement.hide();

      if (this.getVisibleRowCount() == 0 && this._hasHeader == true && this._filters.size() == 0) {
        this._headerRow.setStyle({
          display: 'none'
        });
      }
      
      this.fire("afterRowVisibilityChange", {
        tableComponent: this,
        rows: [ rowNumber ],
        hidden: true
      });
    }
  },
  hideRows: function (rowNumbers) {
    var hideRows = new Array();
    
    for (var i = 0, len = rowNumbers.length; i < len; i++) {
      var rowElem = this.getRowElement(rowNumbers[i]);
      if (rowElem.visible())
        hideRows.push(rowElem); 
    }
    
    var doHide = hideRows.length > 0;

    if (doHide && this.fire("beforeRowVisibilityChange", {
      tableComponent: this,
      rows: hideRows,
      hidden: false
    })) {
      this.detachFromDom();
      hideRows.invoke("hide");
      this.reattachToDom();
  
      if (this.getVisibleRowCount() == 0 && this._hasHeader == true && this._filters.size() == 0) {
        this._headerRow.setStyle({
          display: 'none'
        });
      }
  
      this.fire("afterRowVisibilityChange", {
        tableComponent: this,
        rows: rowNumbers,
        hidden: true
      });
    }
  },
  showRow: function (rowNumber) {
    var rowElement = this.getRowElement(rowNumber);
    var doShow = !rowElement.visible();
    
    if (doShow && this.fire("beforeRowVisibilityChange", {
      tableComponent: this,
      rows: [ rowNumber ],
      hidden: true
    })) {
      rowElement.show();

      this._headerRow.setStyle({
        display: ''
      });
      
      this.fire("afterRowVisibilityChange", {
        tableComponent: this,
        rows: [ rowNumber ],
        hidden: false
      });
    }
  },
  showAllRows: function () {
    var rowNumbers = new Array();
    var rowElements = new Array();

    for (var i = 0, len = this.getRowCount(); i < len; i++) {
      var rowElement = this.getRowElement(i); 
      if (!rowElement.visible()) {
        rowNumbers.push(i);
        rowElements.push(rowElement);
      }
    }

    var doShow = rowElements.length > 0;
    
    if (doShow && this.fire("beforeRowVisibilityChange", {
      tableComponent: this,
      rows: rowNumbers,
      hidden: true
    })) {
      this.detachFromDom();
      rowElements.invoke("show");
      this.reattachToDom();
  
      if (this.getVisibleRowCount() > 0 && this._hasHeader == true) {
        this._headerRow.setStyle({
          display: ''
        });
      }
  
      this.fire("afterRowVisibilityChange", {
        tableComponent: this,
        rows: rowNumbers,
        hidden: false
      });
    }
  },
  isRowVisible: function (rowNumber) {
    return this.getRowElement(rowNumber).visible();
  },
  getVisibleRowCount: function () {
    var result = 0;
    
    for (var i = 0, l = this.getRowCount(); i < l; i++) {
      if (this.isRowVisible(i))
        result++;
    }
    
    return result;
  },
  getNamedColumnIndex: function (name) {
    for (var i = 0; i < this.options.columns.length; i++) {
      var column = this.options.columns[i];
      if (column.paramName == name)
        return i;
    }
    
    return -1;
  },
  deleteAllRows: function () {
    while (this.getRowCount() > 0)
      this._deleteRow(this.getRowCount() - 1);
    this.clearFilters();
  },
  getCellEditor: function (row, column) {
    return this._cellEditors.get(row + '.' + column);
  },
  getCellValue: function (row, column) {
    var handlerInstance = this.getCellEditor(row, column);
    return IxTableControllers.getController(handlerInstance._dataType).getEditorValue(handlerInstance);
  },
  setCellValue: function (row, column, value) {
    var handlerInstance = this.getCellEditor(row, column);
    var controller = IxTableControllers.getController(handlerInstance._dataType);
    var oldValue = controller.getEditorValue(handlerInstance);
    
    if (oldValue != value) {
      controller.setEditorValue(handlerInstance, value);
      this.fire("cellValueChange", {
        tableComponent: this,
        row: row,
        column: column, 
        value: value
      });
    }
  },
  copyCellValue: function(column, fromRow, toRow) {
    var fromInstance = this.getCellEditor(fromRow, column);
    var toInstance = this.getCellEditor(toRow, column);
    
    IxTableControllers.getController(toInstance._dataType).copyCellValue(toInstance, fromInstance);
  },
  disableCellEditor: function (row, column) {
    var handlerInstance = this.getCellEditor(row, column);
    return IxTableControllers.getController(handlerInstance._dataType).disableEditor(handlerInstance);
  },
  enableCellEditor: function (row, column) {
    var handlerInstance = this.getCellEditor(row, column);
    return IxTableControllers.getController(handlerInstance._dataType).enableEditor(handlerInstance);
  },
  hideCell: function (row, column) {
    var handlerInstance = this.getCellEditor(row, column);
    IxTableControllers.getController(handlerInstance._dataType).hide(handlerInstance);
  },
  showCell: function (row, column) {
    var handlerInstance = this.getCellEditor(row, column);
    IxTableControllers.getController(handlerInstance._dataType).show(handlerInstance);
  },
  isCellVisible: function (row, column) {
    var handlerInstance = this.getCellEditor(row, column);
    return IxTableControllers.getController(handlerInstance._dataType).isVisible(handlerInstance);
  },
  isCellDisabled: function (row, column) {
    var handlerInstance = this.getCellEditor(row, column);
    return IxTableControllers.getController(handlerInstance._dataType).isDisabled(handlerInstance);
  },
  preventCellSelection: function (row, column) {
    var handlerInstance = this.getCellEditor(row, column);
    
  },
  allowCellSelection: function (row, column) {
    
  },
  getHeaderCell: function (column) {
    return this._headerCells[column];
  },
  getColumnCount: function () {
    return this.options.columns.length;
  },
  disableRow: function (row) {
    for (var column = 0; column < this.options.columns.length; column++)
      this.disableCellEditor(row, column);
  },
  enableRow: function (row) {
    for (var column = 0; column < this.options.columns.length; column++)
      this.enableCellEditor(row, column);
  },
  getRowCount: function () {
    return parseInt(this._rowCount.value);
  },
  isCellEditable: function (row, column) {
    var editor = this.getCellEditor(row, column);
    return editor._editable;
  },
  setCellEditable: function (row, column, editable) {
    var editor = this.getCellEditor(row, column);
    var controller = IxTableControllers.getController(editor._dataType);
    
    if (controller.getEditable(editor) != editable) {
      controller.setEditable(editor, editable);
      this.fire("cellEditableChanged", {
        tableComponent: this,
        row: row,
        column: column,
        editable: editable
      });
    }
  },
  setCellDataType: function (row, column, dataType) {
    var editor = this.getCellEditor(row, column);
    if (editor._dataType != dataType) {
      var oldController = IxTableControllers.getController(editor._dataType);
      var newController = IxTableControllers.getController(dataType);
      
      var value = oldController.getEditorValue(editor);
      var editable = editor._editable;
      var name = editor._name;
      var columnDefinition = editor._columnDefinition;
      var column = this._getCellEditorColumn(editor);
      var row = this._getCellEditorRow(editor);
      var cell = editor._cell;
      
      oldController.detachContentHandler(editor);
      oldController.destroyHandler(editor);
      
      if (editable) {
        var editor = newController.buildEditor(name, columnDefinition);
        newController.attachContentHandler(this, cell, editor);
        newController.setEditorValue(editor, value);
      } else {
        var viewer = newController.buildViewer(name, columnDefinition);
        newController.attachContentHandler(this, cell, viewer);
        newController.setEditorValue(viewer, value);
      }
      
      this.fire("cellDataTypeChanged", {
        tableComponent: this,
        row: row,
        column: column,
        dataType: dataType
      });
    }
  },
  focusCell: function (row, column) {
    var editor = this.getCellEditor(row, column);
    var controller = IxTableControllers.getController(editor._dataType);
    controller.focus(editor);
  },
  getCellDataType: function (row, column) {
    var editor = this.getCellEditor(row, column);
    return editor._dataType;
  },
  setActiveRows: function (rows) {
    while (this.getActiveRows().length > 0) {
      this.removeActiveRow(this.getActiveRows()[0]);
    }
    
    for (var i = 0; i < rows.length; i++) {
      this.addActiveRow(rows[i]);
    }
  },
  getActiveRows: function () {
    return this._activeRows.keys();
  },
  isActiveRow: function (rowNumber) {
    return this._activeRows.get(rowNumber) == true;
  },
  addActiveRow: function (rowNumber) {
    this._activeRows.set(rowNumber, true);
    this.getRowElement(rowNumber).addClassName("ixActiveTableRow");
  },
  removeActiveRow: function (rowNumber) {
    this._activeRows.unset(rowNumber);
    this.getRowElement(rowNumber).removeClassName("ixActiveTableRow");
  },
  getRowElement: function (rowNumber) {
    return this._rowElements.get(rowNumber);
  },
  isDetachedFromDom: function () {
    return this._detached == true;
  },
  detachFromDom: function() {
    if (!this.isDetachedFromDom()) {
      if (this.fire("beforeDetachFromDom", { tableComponent: this})) {
        this._detachedParent = this.domNode.parentNode;
        this._detachedNextSibling = this.domNode.next();
        
        this.domNode.remove();
        this._detached = true;
        this._detachedCount = 0;
        
        this.fire("afterDetachFromDom", { tableComponent: this});
      }
    }
    this._detachedCount++;
  },
  reattachToDom: function() {
    if (this.isDetachedFromDom()) {
      this._detachedCount--;
      
      if (this._detachedCount == 0) {
        if (this.fire("beforeReattachToDom", { tableComponent: this})) {
          if (this._detachedNextSibling) {
            this._detachedParent.insertBefore(this.domNode, this._detachedNextSibling);
          } else {
            this._detachedParent.appendChild(this.domNode);
          }
          this._detachedParent = undefined;
          this._detachedNextSibling = undefined;
          this._detached = false;
        
          this.fire("afterReattachToDom", { tableComponent: this}); 
        }
      }
    }
  },  
  addFilter: function (filter) {
    if (this.fire("beforeFiltering", { tableComponent: this })) {
      this._filters.push(filter);
      filter.execute({ 
        tableComponent: this 
      });
      
      if (Object.isFunction(filter.getColumn)) {
        var column = filter.getColumn();
        
        if ((column != undefined) && (column >= 0)) {
          var columnHeaderCell = this._headerCells[column];
          
          if (columnHeaderCell)
            columnHeaderCell.addClassName("ixTableColumnHeaderFiltered");
        }
      }
      this.fire("afterFiltering", { tableComponent: this });
    }
  },
  applyFilters: function () {
    this._redoFilters();
  },
  _redoFilters: function () {
    if (this.fire("beforeFiltering", { tableComponent: this })) {
      this.detachFromDom();
  
      this.showAllRows();
  
      var _this = this;
      this._filters.each(function(filter) {
        filter.execute({ 
          tableComponent: _this 
        });
      });
      
      this.reattachToDom();
      this.fire("afterFiltering", { tableComponent: this });
    }
  },
  clearFilters: function () {
    if (this.fire("beforeFiltering", { tableComponent: this })) {
      this._filters.clear();
      
      for (var i = 0, len = this.options.columns.length; i < len; i++)
        this._headerCells[i].removeClassName("ixTableColumnHeaderFiltered");
      
      this.showAllRows();
      this.fire("afterFiltering", { tableComponent: this });
    }
  },
  sort: function () {
    this._redoSort();
  },
  _clearColumnFilter: function (column) {
    if (this.fire("beforeFiltering", { tableComponent: this })) {
      for (var i = this._filters.size() - 1; i >= 0; i--) {
        var filter = this._filters[i];
        if (filter.getColumn() === column) {
          this._filters.splice(i, 1);
        }
      }
      var columnHeaderCell = this._headerCells[column];
      
      if (columnHeaderCell)
        columnHeaderCell.removeClassName("ixTableColumnHeaderFiltered");
      this.fire("afterFiltering", { tableComponent: this });
    }
  },
  _setSortMethod: function (sortMethod) {
    this._sortMethod = sortMethod;
    this._redoSort();
  },
  _redoSort: function () {
    if (this._sortMethod) {
      var rows = this._rowElements.values().clone();
      var event = { tableComponent: this };
      
      for (var i = 1, len = rows.length; i < len; i++) {
        var j = i;
        
        var row1 = rows[j - 1]._rowNumber;
        var row2 = rows[j]._rowNumber;
        
        while ((j > 0) && (this._sortMethod.compare(event, row1, row2) > 0)) {
          var row = rows[j];
          rows[j] = rows[j - 1]; 
          rows[j - 1] = row;
          j--;

          if (j > 0) {
            row1 = rows[j - 1]._rowNumber;
            row2 = rows[j]._rowNumber;
          }
        }
      }
    
      this.detachFromDom();
      
      for (var i = 0, len = rows.length; i < len; i++) {
        this._content.appendChild(rows[i]);
      }
      
      this.reattachToDom();
    }
  },
  _deleteRow: function (rowNumber) {
    if (rowNumber == undefined || rowNumber < 0 || rowNumber > this.getRowCount() - 1) {
      throw "Invalid row index " + rowNumber;
    }
    
    this.fire("beforeRowDelete", {
      tableComponent: this,
      row: rowNumber
    });
    
    for (var row = rowNumber; row < (this.getRowCount() - 1); row++) {
      for (var column = 0; column < this.options.columns.length; column++) {
        var cellEditor = this.getCellEditor(row, column);
        var nextCellEditor = this.getCellEditor(row + 1, column); 
        IxTableControllers.getController(cellEditor._dataType)._copyState(cellEditor, nextCellEditor);
      }
    }
    
    var rowNumber = this.getRowCount() - 1;
    var rowElement = this._rowElements.get(rowNumber);

    for (var i = 0, len = this.options.columns.length; i < len; i++) {
      if (this.options.columns[i].contextMenu) {
        var cell = this._getCellEditorCell(this.getCellEditor(rowNumber, i));
        var contextMenuButton = cell.down(".ixTableCellContextMenuButton");
        Event.stopObserving(contextMenuButton, "click", this._contextMenuButtonClickListener);
      }
    }

    rowElement.remove();
    
    this._setRowCount(this.getRowCount() - 1);
    
    this.fire("rowDelete", {
      tableComponent: this, 
      row: rowNumber
    });

    if (this.getVisibleRowCount() == 0 && this._hasHeader == true && this._filters.size() == 0) {
      this._headerRow.setStyle({
        display: 'none'
      });
    }
  },
  _getContextButtonCell: function (contextMenuButton) {
    return $(contextMenuButton.parentNode.parentNode);
  },
  _getCellEditorCell: function (editorInstance) {
    return $(editorInstance._cell);
  },
  _getCellEditorRow: function (editorInstance) {
    return this._getCellRow(this._getCellEditorCell(editorInstance));
  },
  _getCellEditorColumn: function (editorInstance) {
    return this._getCellColumn(this._getCellEditorCell(editorInstance));
  },
  _getCellRow: function (cell) {
    var rowContent = $(cell.parentNode);
    if (rowContent) {
      var rowElement = $(rowContent.parentNode);
      if (rowElement)
        return rowElement._rowNumber;
    }
    
    return -1;
  },
  _getCellColumn: function (cell) {
    return cell._column;
  },  
  changeTableId: function (newId) {
    var oldId = this.options.id;
    
    if (this.fire("tableIdChange", { oldId: oldId, newId: newId})) {
      // Update options id
      this.options.id = newId;
      // Update global hash
      _ixTables.unset(oldId);
      _ixTables.set(newId, this);
      // Update rowCount element
      this._rowCount.name = newId + '.rowCount';
      // Update ix:tableid attribute
      this.domNode.setAttribute("ix:tableid", newId);
      // Update field ids
      for (var i = 0; i < this.options.columns.length; i++) {
        var column = this.options.columns[i];
        for (var row = 0; row < this.getRowCount(); row++) {
          var cellEditor = this.getCellEditor(row, i);
          var name = newId ? newId + '.' + row + '.' + (column.paramName ? column.paramName : i) : '';
          IxTableControllers.getController(cellEditor._dataType).changeParamName(cellEditor, name);
        }
      }
    };
  },
  _setRowCount: function (rowCount) {
    this._rowCount.value = rowCount; 
  },
  _onRowClick: function (event) {
    var row = Event.element(event);
    if (!row.hasClassName("ixTableRow"))
      row = row.up(".ixTableRow");
    if (row) {
      this.fire("rowClick", {
        tableComponent: this,
        row: row._rowNumber
      });
    }
  },
  _onHeaderClick: function (event) {
    var elem = Event.element(event);

    if (Object.isFunction(elem._headerOnClick)) {
      elem._headerOnClick({
        tableComponent: this,
        columnIndex: elem._columnIndex
      });
    }
  },
  _createCellContentHandler: function (name, columnDefinition, editable) {
    var controller = IxTableControllers.getController(columnDefinition.dataType);
    
    var cellEditable = editable||columnDefinition.editable;
    
    if (controller.getMode() == IxTableControllers.EDITMODE_NOT_EDITABLE)
      cellEditable = false;
    else if (controller.getMode() == IxTableControllers.EDITMODE_ONLY_EDITABLE)
      cellEditable = true;
    
    if (cellEditable) {
      var editor = controller.buildEditor(name, columnDefinition);
      return editor;
    } else {
      var viewer = controller.buildViewer(name, columnDefinition);
      return viewer;
    }
  },
  _setCellContentHandler: function (row, column, handlerInstance) {
    this._cellEditors.set(row + '.' + column, handlerInstance);
  },
  _unsetCellContentHandler: function (row, column) {
    this._cellEditors.unset(row + '.' + column);
  },
  _onClearColumnFiltersClickListener: function (event) {
    var clearBtn = Event.element(event);
    if (clearBtn._column) {
      this._clearColumnFilter(clearBtn._column);
      
      
      this._redoFilters();
    }
  },
  _onSortColumnClick: function (event) {
    var sortButton = Event.element(event);
    if (sortButton._sortAction) {
      this._setSortMethod(sortButton._sortAction);
    }
  },
  _onContextMenuButtonClick: function (event) {
    var contextMenuButton = Event.element(event);
    var cell = this._getContextButtonCell(contextMenuButton);
    var row = this._getCellRow(cell);
    var column = this._getCellColumn(cell);
    
    var columnOptions = this.options.columns[column];
    if (columnOptions && columnOptions.contextMenu) {
      var menuContainer = new Element("div", {className: "ixTableCellContextMenu"} );
      
      for (var i = 0, l = columnOptions.contextMenu.length; i < l; i++) {
        var menuItem = columnOptions.contextMenu[i];
        var menuElement = new Element("div");
        menuElement._menuItem = menuItem;

        if (!("-" === menuItem.text)) {
          menuElement.addClassName("ixTableCellContextMenuItem");
          menuElement.update(menuItem.text);
          Event.observe(menuElement, "click", this._contextMenuItemClickListener);
        } else {
          menuElement.addClassName("ixTableCellContextMenuItemSpacer");
        }
        menuContainer.appendChild(menuElement);
      }
      
      var _this = this;
      var windowMouseMove = function (event) {
        var element = Event.element(event);
        var overMenu = element.hasClassName('ixTableCellContextMenu');
        if (!overMenu) {
          if (element.up('.ixTableCellContextMenu'))
            overMenu = true;
        }
      
        if (!overMenu) {
          $$('.ixTableCellContextMenu').forEach(function (menu) {
            $(menu).select('.ixTableCellContextMenuItem').forEach(function (menuItem) {
              if (!menuItem.hasClassName("ixTableCellContextMenuItemSpacer"))
                Event.stopObserving(menuItem, "click", _this._contextMenuItemClickListener);
            }); 
            
            $(menu).remove();
          });
          
          Event.stopObserving(Prototype.Browser.IE ? document : window, "mousemove", windowMouseMove);
        }
      };
      
      Event.observe(Prototype.Browser.IE ? document : window, "mousemove", windowMouseMove);
      
      cell.appendChild(menuContainer);
    }
  },
  _onContextMenuItemClick: function (event) {
    var menuElement = Event.element(event);
    var contextMenu = menuElement.parentNode;
    
    var menuItem = menuElement._menuItem;
    var cell = $(contextMenu.parentNode);
    var row = this._getCellRow(cell);
    var column = this._getCellColumn(cell);

    var _this = this;
    contextMenu.select('.ixTableCellContextMenuItem').forEach(function (menuItem) {
      Event.stopObserving(menuItem, "click", _this._contextMenuItemClickListener);
    }); 

    contextMenu.remove();
    
    menuItem.onclick.execute({
      tableComponent: this,
      row: row,
      column: column,
      menuItem: menuItem
    });
  }
});

Object.extend(IxTable.prototype,fni.events.FNIEventSupport);

function getIxTableById(id) {
  return _ixTables.get(id);
};

function getIxTables() {
  return _ixTables.values();
};

IxTableEditorController = Class.create({
  buildEditor: function (name, columnDefinition) { },
  buildViewer: function (name, columnDefinition) { },
  attachContentHandler: function (table, cell, handlerInstance) {
    handlerInstance._table = table;
    handlerInstance._cell = cell;
    
    if (handlerInstance._columnDefinition.contextMenu) {
      var contextMenuButtonContainer = new Element("div", {className: "ixTableCellContextMenuButtonContainer"});
      var contextMenuButton = new Element("span", {className: "ixTableCellContextMenuButton"});
      var editorContainer = new Element("div", {className: "ixTableCellEditorContainer"});
        cell.addClassName('ixTableContextMenuCell');
      
      Event.observe(contextMenuButton, "click", table._contextMenuButtonClickListener);

      contextMenuButtonContainer.appendChild(contextMenuButton);
      editorContainer.appendChild(handlerInstance);
      cell.appendChild(editorContainer);
      cell.appendChild(contextMenuButtonContainer);
    } else {
      cell.appendChild(handlerInstance);
    }
    
    var row = this.getEditorRow(handlerInstance);
    var column = this.getEditorColumn(handlerInstance);
    table._setCellContentHandler(row, column, handlerInstance);
    
    if (handlerInstance._columnDefinition.hidden == true) 
      this.hide(handlerInstance);
    else
      this.show(handlerInstance);
    
    var selectable = handlerInstance._columnDefinition.selectable;
    if (selectable == false)
      this.setSelectable(handlerInstance, false);
    
    return handlerInstance;
  },
  detachContentHandler: function (handlerInstance) {
    var row = this.getEditorRow(handlerInstance);
    var column = this.getEditorColumn(handlerInstance);
    handlerInstance._table._unsetCellContentHandler(row, column);
    handlerInstance._table = undefined;
    var cell = handlerInstance._cell;

    var children = cell.childElements();
    
    for (var i = children.length - 1; i >= 0; i--) {
      var child = children[i];
      child.parentNode.removeChild(child);
    }
    // For unknown reason the following line doesn't work in all cases.
//    cell.childElements().invoke('remove');
  },  
  destroyHandler: function (handlerInstance) { 
    handlerInstance._editable = undefined;
    handlerInstance._dataType = undefined;
    handlerInstance._name = undefined;
    handlerInstance._cell = undefined;
    handlerInstance._columnDefinition = undefined;
    
    if (handlerInstance._fieldValue) {
      handlerInstance.removeChild(handlerInstance._fieldValue);
      handlerInstance._fieldValue = undefined;
    }
    
    if (handlerInstance._fieldContent) {
      handlerInstance.removeChild(handlerInstance._fieldContent);
      handlerInstance._fieldContent = undefined;
    }
  },
  getEditable: function (handlerInstance) {
    return handlerInstance._editable;
  },
  setEditable: function (handlerInstance, editable) {
    if (handlerInstance._editable == editable)
      return handlerInstance;
    if ((this.getMode(handlerInstance) == IxTableControllers.EDITMODE_ONLY_EDITABLE) && (editable == false))
      return handlerInstance;
    if ((this.getMode(handlerInstance) == IxTableControllers.EDITMODE_NOT_EDITABLE) && (editable == true))
      return handlerInstance;
    
    var table = handlerInstance._table;
    var cell = handlerInstance._cell;
    var visible = this.isVisible(handlerInstance); 
    var cellValue = this.getEditorValue(handlerInstance);
    
    this.detachContentHandler(handlerInstance);
    
    var newHandler = editable == true ? this.buildEditor(handlerInstance._name, handlerInstance._columnDefinition) : this.buildViewer(handlerInstance._name, handlerInstance._columnDefinition);
    this.attachContentHandler(table, cell, newHandler);
    
    if (visible) 
      this.show(newHandler);
    else 
      this.hide(newHandler);
    
    this.setEditorValue(newHandler, cellValue);
    this.destroyHandler(handlerInstance);

    return newHandler;
  },
  getEditorValue: function (handlerInstance) {},
  setEditorValue: function (handlerInstance, value) {},
  getDisplayValue: function (handlerInstance) {
    if (this.getEditable(handlerInstance) != true)
      return handlerInstance._fieldContent.innerHTML;
    else
      return this.getEditorValue(handlerInstance);
  },
  disableEditor: function (handlerInstance) {},
  enableEditor: function (handlerInstance) {},
  enableEditor: function (handlerInstance) {},
  isDisabled: function (handlerInstance) {},
  hide: function (handlerInstance) {
    handlerInstance._cell.hide();
  },
  show: function (handlerInstance) {
    handlerInstance._cell.show();
  },
  isVisible: function (handlerInstance) {
    return handlerInstance._cell.visible();
  },
  setSelectable: function (handlerInstance, selectable) {
    if (selectable == true) {
      handlerInstance.onselectstart = undefined;
      handlerInstance.unselectable = "off";
      handlerInstance.style.MozUserSelect = "text";
    } else {
      handlerInstance.onselectstart = function(){ return false; };
      handlerInstance.unselectable = "on";
      handlerInstance.style.MozUserSelect = "none";
    }
  },
  getMode: function () { },
  getDataType: function () { },
  changeParamName: function (handlerInstance, name) {
    if (handlerInstance._editable) {
      handlerInstance.name = name;
    } else {
      handlerInstance._name = name;
      if (handlerInstance._fieldValue) {
        handlerInstance._fieldValue.name = name;
      }
    }
  },
  focus: function (handlerInstance) {
    Form.Element.focus(handlerInstance);
  },
  getTableComponent: function (handlerInstance) {
    return handlerInstance._table;
  },
  getEditorRow: function (handlerInstance) {
    return handlerInstance._table._getCellEditorRow(handlerInstance);
  },
  getEditorColumn: function (handlerInstance) {
    return handlerInstance._table._getCellEditorColumn(handlerInstance);
  },
  _createEditorElement: function (elementName, name, className, attributes, columnDefinition) {
    var editor = new Element(elementName, Object.extend(attributes||{}, {className: "ixTableCellEditor" + (className ? ' ' + className : '')}));
    
    if (columnDefinition.editorClassNames) {
      var classNames = columnDefinition.editorClassNames.split(' ');
      for (var i = 0, l = classNames.length; i < l; i++) {
        editor.addClassName(classNames[i]);
      }
    }
    
    editor._editable = true;
    editor._dataType = this.getDataType();
    editor._name = name;
    editor._columnDefinition = columnDefinition;
    return editor;
  },
  _createViewerElement: function (elementName, name, className, attributes, columnDefinition) {
    var viewer = new Element(elementName, Object.extend(attributes||{}, {className: "ixTableCellViewer" + (className ? ' ' + className : '')}));
    
    if (columnDefinition.viewerClassNames) {
      var classNames = columnDefinition.viewerClassNames.split(' ');
      for (var i = 0, l = classNames.length; i < l; i++) {
        viewer.addClassName(classNames[i]);
      }
    }
    
    viewer._editable = false;
    viewer._dataType = this.getDataType();
    viewer._name = name;
    viewer._columnDefinition = columnDefinition;
    
    viewer._fieldValue = new Element("input", {type: "hidden", name: name});
    viewer._fieldContent = new Element("span"); 
    viewer.appendChild(viewer._fieldValue);
    viewer.appendChild(viewer._fieldContent); 
    
    return viewer;
  },
  _setViewerValue: function (viewer, value, displayValue) {
    if (value == undefined||value==null) {
      viewer._fieldValue.value = '';
      viewer._fieldContent.innerHTML = '';
    } else {
      viewer._fieldValue.value = value;
      viewer._fieldContent.innerHTML = displayValue ? displayValue : String(value).escapeHTML();
    }
  },
  _getViewerValue: function (viewer) {
    return viewer._fieldValue.value;
  },
  _fireValueChange: function (handlerInstance, newValue) {
    handlerInstance._table.fire("cellValueChange", {
      tableComponent: handlerInstance._table,
      fieldType: handlerInstance._dataType,
      column: this.getEditorColumn(handlerInstance),
      row: this.getEditorRow(handlerInstance),
      value: newValue
    });
  },
  _addDisabledHiddenElement: function (handlerInstance) {
    if (handlerInstance.parentNode) {
      var value = this.getEditorValue(handlerInstance);
      if (handlerInstance._disabledHiddenElement) {
        handlerInstance._disabledHiddenElement.value = value;
      } else {
        var hiddenElement = new Element("input", {type: 'hidden', value: value, name: handlerInstance._name});
        handlerInstance._disabledHiddenElement = hiddenElement;
        handlerInstance._cell.appendChild(hiddenElement);
      }
    } else {
      // TODO: Onko tälläisiäkin tilanteita ????
    }
  },
  _removeDisabledHiddenElement: function (handlerInstance) {
    if (handlerInstance._disabledHiddenElement) {
      handlerInstance._disabledHiddenElement.remove();
      delete handlerInstance._disabledHiddenElement;
    }
  },
  _updateDisabledHiddenElement: function (handlerInstance, value) {
    if (handlerInstance._disabledHiddenElement) {
      handlerInstance._disabledHiddenElement.value = value;
    }
  },
  copyCellValue: function(target, source) {
    this.setEditorValue(target, this.getEditorValue(source));
  },
  _copyState: function (target, source) {
    this.copyCellValue(target, source);
    return this.setEditable(target, this.getEditable(source));
    // TODO: disabled, datatype yms tiedot
  },
  _unescapeHtmlEntities: function(value) {
    if (value) {
      var tmp = document.createElement("pre");
      tmp.innerHTML = value;
      value = tmp.firstChild.nodeValue;
    }
    return value;
  }
});

Object.extend(IxTableEditorController.prototype,fni.events.FNIEventSupport);

IxNumberTableEditorController = Class.create(IxTableEditorController, {
  buildEditor: function ($super, name, columnDefinition) {
    var editor = this._createEditorElement("input", name, "ixTableCellEditorNumber", {type: "text", name: name}, columnDefinition);

    editor.addClassName("float");
    if (columnDefinition.required)
      editor.addClassName("required");
    
    this._editorValueChangeListener = this._onEditorValueChange.bindAsEventListener(this);
    Event.observe(editor, "change", this._editorValueChangeListener);
    return editor;
  },
  buildViewer: function ($super, name, columnDefinition) {
    return this._createViewerElement("div", name, "ixTableCellViewerNumber", {}, columnDefinition);
  },
  disableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable == false)
      handlerInstance.addClassName("ixTableCellViewerDisabled");
    else {
      this._addDisabledHiddenElement(handlerInstance);
      handlerInstance.disabled = true;
    }
  },
  enableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable == false)
      handlerInstance.removeClassName("ixTableCellViewerDisabled");
    else {
      handlerInstance.disabled = false;
      this._removeDisabledHiddenElement(handlerInstance);
    }
  },
  getEditorValue: function ($super, handlerInstance) {
    if (handlerInstance._editable != true) 
      return this._getViewerValue(handlerInstance);
    else
      return handlerInstance.value;
  },
  setEditorValue: function ($super, handlerInstance, value) {
    if (handlerInstance._editable != true) 
      this._setViewerValue(handlerInstance, value);
    else {
      if (this.isDisabled(handlerInstance))
        this._updateDisabledHiddenElement(handlerInstance, value);
      
      handlerInstance.value = value;
    }
  },
  destroyEditor: function ($super, handlerInstance) {
    handlerInstance.remove();
  },
  isDisabled: function ($super, handlerInstance) {
    return handlerInstance.disabled == true;
  },
  getDataType: function () {
    return "number";  
  },
  getMode: function () { 
    return IxTableControllers.EDITMODE_EDITABLE;
  },
  _onEditorValueChange: function (event) {
    var handlerInstance = Event.element(event);
    this._fireValueChange(handlerInstance, handlerInstance.value);
  }
});

IxTableControllers.registerController(new IxNumberTableEditorController());

IxHiddenTableEditorController = Class.create(IxTableEditorController, {
  buildEditor: function ($super, name, columnDefinition) {
    // Event.observe(cellEditor, "change", this._fieldValueChangeListener);
    return this._createEditorElement("input", name, undefined, {type: "hidden", name: name}, columnDefinition);
  },
  getEditorValue: function ($super, handlerInstance) {
    return handlerInstance.value;
  },
  setEditorValue: function ($super, handlerInstance, value) {
    handlerInstance.value = value == undefined ? '' : this._unescapeHtmlEntities(value);
  },
  destroyEditor: function ($super, handlerInstance) {
    handlerInstance.remove();
  },
  getDataType: function ($super) {
    return "hidden";  
  },
  isDisabled: function ($super, handlerInstance) {
    return handlerInstance.disabled == true;
  },
  getMode: function () { 
    return IxTableControllers.EDITMODE_ONLY_EDITABLE;
  }
});

IxTableControllers.registerController(new IxHiddenTableEditorController());

IxSelectTableEditorController = Class.create(IxTableEditorController, {
  buildEditor: function ($super, name, columnDefinition) {
    var cellEditor = this._createEditorElement("select", name, "ixTableCellEditorSelect", {name: name}, columnDefinition);
    
    if (columnDefinition.required)
      cellEditor.addClassName("required");
    
    this._editorValueChangeListener = this._onEditorValueChange.bindAsEventListener(this);
    
    if (columnDefinition.options) {
      this._addOptionsFromArray(cellEditor, columnDefinition.options);
    }
    
    return cellEditor;
  },
  removeAllOptions: function (handlerInstance) {
    if (handlerInstance._editable) {
      for (var i = handlerInstance.options.length - 1; i >= 0; i--) {
        $(handlerInstance.options[i]).remove();
      }  
    }
  },
  addOption: function (handlerInstance, value, text) {
    return this.addOption(handlerInstance, value, text, false);
  },
  addOption: function (handlerInstance, value, text, selected) {
    if (handlerInstance._editable) {
      var optionNode = this._createOption(value, text, selected);
      handlerInstance.appendChild(optionNode);
      return optionNode;
    } else {
      handlerInstance._options.push({
        text: text,
        value: value
      });
      
      if (this.getEditorValue(handlerInstance) == value) {
        this._setViewerValue(handlerInstance, value, text);
      }
    }
  },
  _createOptionGroup: function (text) {
    return new Element("optgroup", {label:text});
  },
  _createOption: function (value, text, selected) {
    var optionNode;
    
    if (!selected)
      optionNode = new Element("option", {value: value === undefined ? '' : value});
    else
      optionNode = new Element("option", {value: value === undefined ? '' : value, selected: "selected"});
    
    if (text)
      optionNode.update(text);
    
    return optionNode;
  },
  attachContentHandler: function ($super, table, cell, handlerInstance) {
    var result = $super(table, cell, handlerInstance);
    if (this.getEditable(handlerInstance))
      Event.observe(result, "change", this._editorValueChangeListener);
    return result;
  },
  detachContentHandler: function ($super, handlerInstance) {
    if (this.getEditable(handlerInstance))
      Event.stopObserving(handlerInstance, "change", this._editorValueChangeListener);
    $super(handlerInstance);
  },
  buildViewer: function ($super, name, columnDefinition) {
    var cellViewer = this._createViewerElement("div", name, "ixTableCellViewerSelect", {}, columnDefinition);
    if (this.isDynamicOptions(cellViewer))
      cellViewer._options = new Array();
    
    return cellViewer;
  },
  disableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable != true)
      handlerInstance.addClassName("ixTableCellViewerDisabled");
    else {
      this._addDisabledHiddenElement(handlerInstance);
      handlerInstance.disabled = true;
    }
  },
  enableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable != true)
      handlerInstance.removeClassName("ixTableCellViewerDisabled");
    else {
      handlerInstance.disabled = false;
      this._removeDisabledHiddenElement(handlerInstance);
    }
  },
  getEditorValue: function ($super, handlerInstance) {
    if (handlerInstance._editable != true) 
      return this._getViewerValue(handlerInstance);
    else
      return handlerInstance.value;
  },
  getDisplayValue: function ($super, handlerInstance) {
    if (handlerInstance._editable != true) 
      return $super(handlerInstance);
    else {
      var index = handlerInstance.selectedIndex;
      var option = handlerInstance.options[index];
      if (option)
        return option.innerHTML;
      else
        return $super(handlerInstance);
    }
  },
  setEditorValue: function ($super, handlerInstance, value) {
    if (handlerInstance._editable != true) {
      var displayValue = value;
      var options = this.isDynamicOptions(handlerInstance) ? handlerInstance._options : handlerInstance._columnDefinition.options;
      
      if (options) {
        for (var i = 0; i < options.length; i++) {
          if (options[i].optionGroup == true) {
            for (var j = 0; j < options[i].options.length;j++) {
              if (options[i].options[j].value == value) {
                displayValue = options[i].options[j].text;
                break;
              }
            }
          } else {
            if (options[i].value == value) {
              displayValue = options[i].text;
              break;
            }
          }
        }
      }
      this._setViewerValue(handlerInstance, value, displayValue);
    } else {
      if (this.isDisabled(handlerInstance))
        this._updateDisabledHiddenElement(handlerInstance, value);
      
      handlerInstance.value = value;
    }
  },
  destroyEditor: function ($super, handlerInstance) {
    handlerInstance.remove();
  },
  isDisabled: function ($super, handlerInstance) {
    return handlerInstance.disabled == true;
  },
  isDynamicOptions: function (handlerInstance) {
    return handlerInstance._columnDefinition.dynamicOptions || false;
  },
  getDataType: function ($super) {
    return "select";  
  },
  getMode: function ($super) { 
    return IxTableControllers.EDITMODE_EDITABLE;
  },
  getOptions: function (handlerInstance) {
    if (this.getEditable(handlerInstance)) {
      return this._readOptionsToArray(handlerInstance);
    } else {
      return handlerInstance._options;
    }
  },
  setOptions: function (handlerInstance, options) {
    if (this.getEditable(handlerInstance)) {
      this.removeAllOptions(handlerInstance);
      this._addOptionsFromArray(handlerInstance, options);
    } else {
      handlerInstance._options = options;
    }
  },
  _onEditorValueChange: function (event) {
    var handlerInstance = Event.element(event);
    this._fireValueChange(handlerInstance, handlerInstance.value);
  },
  setEditable: function ($super, handlerInstance, editable) {
    if (this.getEditable(handlerInstance) == editable)
      return handlerInstance;
    
    if (!this.isDynamicOptions(handlerInstance))
      return $super(handlerInstance, editable);
    
    var value = this.getEditorValue(handlerInstance);
    var options;
    
    if (this.getEditable(handlerInstance)) {
      options = this._readOptionsToArray(handlerInstance);
    } else {
      options = handlerInstance._options;
    }

    var newInstance = $super(handlerInstance, editable);
      
    if (editable) {
      this._addOptionsFromArray(newInstance, options);
    } else {
      newInstance._options = options;
    }
    
    this.setEditorValue(newInstance, value);
    
    return newInstance;
  },
  copyCellValue: function($super, target, source) {
    if (!this.isDynamicOptions(source)) {
      $super(target, source);
      this._fireValueChange(target, target.value);
    } else {
      var value = this.getEditorValue(source);
      var options;
      
      if (this.getEditable(source)) {
        options = this._readOptionsToArray(source);
      } else {
        options = source._options;
      }
  
      if (this.getEditable(target)) {
        this.removeAllOptions(target);
        this._addOptionsFromArray(target, options);
      } else {
        target._options = options;
      }
      
      this.setEditorValue(target, value);
    }
  },
  _addOptionsFromArray: function (cellEditor, options) {
    var elements = new Array();

    for (var j = 0, l = options.length; j < l; j++) {
      var option = options[j];
      if (option.optionGroup == true) {
        
        var optionGroup = this._createOptionGroup(option.text);

        var groupOptions = option.options;
        for (var groupIndex = 0; groupIndex < groupOptions.length; groupIndex++) {
          var optionElement = this._createOption(groupOptions[groupIndex].value, groupOptions[groupIndex].text, false);
          optionGroup.appendChild(optionElement);
        }
        
        elements.push(optionGroup);
      } else {
        elements.push(this._createOption(option.value, option.text, false));
      }
    }

    for (var i = 0, l = elements.length; i < l;i++) {
      cellEditor.appendChild(elements[i]);
    }
  },
  _readOptionsToArray: function (cellEditor) {
    var options = new Array();
    
    for (var i = 0, sourceChildNodeLen = cellEditor.childNodes.length; i < sourceChildNodeLen; i++) {
      var editorChildNode = cellEditor.childNodes[i];
      if (editorChildNode.tagName == 'OPTGROUP') {
        var groupOptions = new Array();
        
        for (var j = 0, groupChildNodeLen = editorChildNode.childNodes.length; j < groupChildNodeLen; j++) {
          var groupOption = editorChildNode.childNodes[k];
          groupOptions.push({
            text: groupOptionNode.text,
            value: groupOptionNode.value
          });
        }
        
        options.push({
          text: editorChildNode.text,
          optionGroup: true,
          options: groupOptions
        });
        
      } else if (editorChildNode.tagName == 'OPTION') {
        options.push({
          text: editorChildNode.text,
          value: editorChildNode.value
        });
      }
    }
    
    return options;
  } 
});

IxTableControllers.registerController(new IxSelectTableEditorController());

IxCheckboxTableEditorController = Class.create(IxTableEditorController, {
  buildEditor: function ($super, name, columnDefinition) {
    var cellEditor = this._createEditorElement("input", name, "ixTableCellEditorCheckbox", {name: name, value: "1", type: "checkbox"}, columnDefinition);
    this._editorValueChangeListener = this._onEditorValueChange.bindAsEventListener(this);
    Event.observe(cellEditor, "change", this._editorValueChangeListener);
    return cellEditor;
  },
  buildViewer: function ($super, name, columnDefinition) {
    return this._createViewerElement("div", name, "ixTableCellViewerCheckbox", {}, columnDefinition);
  },
  disableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable != true)
      handlerInstance.addClassName("ixTableCellViewerDisabled");
    else {
      this._addDisabledHiddenElement(handlerInstance);
      handlerInstance.disabled = true;
    }
  },
  enableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable != true)
      handlerInstance.removeClassName("ixTableCellViewerDisabled");
    else {
      handlerInstance.disabled = false;
      this._removeDisabledHiddenElement(handlerInstance);
    }
  },
  getEditorValue: function ($super, handlerInstance) {
    if (handlerInstance._editable != true)
      return this._getViewerValue(handlerInstance) == 1;
    else
      return handlerInstance.checked;
  },
  setEditorValue: function ($super, handlerInstance, value) {
    var isChecked;
    if (Object.isString(value))
      isChecked = value == '1';
    else if (Object.isNumber(value))
      isChecked = value == 1;
    else 
      isChecked = value;
    
    if (handlerInstance._editable != true)
      this._setViewerValue(handlerInstance, isChecked ? 1 : 0, '<div class="ixTableViewerCheckbox' + (isChecked ? 'Checked' : 'NotChecked') + '"/>');
    else {
      if (this.isDisabled(handlerInstance))
        this._updateDisabledHiddenElement(handlerInstance, isChecked ? 1 : 0);
      
      handlerInstance.checked = isChecked;
    }
  },
  destroyEditor: function ($super, handlerInstance) {
    handlerInstance.remove();
  },
  isDisabled: function ($super, handlerInstance) {
    return handlerInstance.disabled == true;
  },
  getDataType: function ($super) {
    return "checkbox";  
  },
  getMode: function ($super) { 
    return IxTableControllers.EDITMODE_EDITABLE;
  },
  _onEditorValueChange: function (event) {
    var handlerInstance = Event.element(event);
    this._fireValueChange(handlerInstance, handlerInstance.value);
  }
});

IxTableControllers.registerController(new IxCheckboxTableEditorController());

IxRadioButtonTableEditorController = Class.create(IxTableEditorController, {
  buildEditor: function ($super, name, columnDefinition) {
    var cellEditor = this._createEditorElement("input", name, "ixTableCellEditorCheckbox", {name: name, value: 'true', type: "radio", title: columnDefinition.tooltip ? columnDefinition.tooltip : ''}, columnDefinition);
    this._editorValueChangeListener = this._onEditorValueChange.bindAsEventListener(this);
    Event.observe(cellEditor, "change", this._editorValueChangeListener);
    return cellEditor;
  },
  buildViewer: function ($super, name, columnDefinition) {
    return this._createViewerElement("div", name, "ixTableCellViewerCheckbox", {}, columnDefinition);
  },
  disableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable != true)
      handlerInstance.addClassName("ixTableCellViewerDisabled");
    else {
      this._addDisabledHiddenElement(handlerInstance);
      handlerInstance.disabled = true;
    }
  },
  enableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable != true)
      handlerInstance.removeClassName("ixTableCellViewerDisabled");
    else {
      handlerInstance.disabled = false;
      this._removeDisabledHiddenElement(handlerInstance);
    }
  },
  getEditorValue: function ($super, handlerInstance) {
    if (handlerInstance._editable != true)
      return this._getViewerValue(handlerInstance) == 1;
    else
      return handlerInstance.checked;
  },
  setEditorValue: function ($super, handlerInstance, value) {
    var isChecked;
    if (Object.isString(value))
      isChecked = value == '1';
    else if (Object.isNumber(value))
      isChecked = value == 1;
    else 
      isChecked = value;
    
    if (handlerInstance._editable != true)
      this._setViewerValue(handlerInstance, isChecked ? 1 : 0, '<div class="ixTableViewerCheckbox' + (isChecked ? 'Checked' : 'NotChecked') + '"/>');
    else {
      if (this.isDisabled(handlerInstance))
        this._updateDisabledHiddenElement(handlerInstance, isChecked ? 1 : 0);
      
      handlerInstance.checked = isChecked;
    }
  },
  destroyEditor: function ($super, handlerInstance) {
    handlerInstance.remove();
  },
  isDisabled: function ($super, handlerInstance) {
    return handlerInstance.disabled == true;
  },
  getDataType: function ($super) {
    return "radiobutton";  
  },
  getMode: function ($super) { 
    return IxTableControllers.EDITMODE_EDITABLE;
  },
  _onEditorValueChange: function (event) {
    var handlerInstance = Event.element(event);
    var row = this.getEditorRow(handlerInstance);
    var column = this.getEditorColumn(handlerInstance);
    
    if (this.getEditorValue(handlerInstance)) {
      var tableComponent = handlerInstance._table;
      var rows = tableComponent.getRowCount();
      for (var i = 0; i < rows; i++) {
        if (i != row) {
          if ((tableComponent.getCellDataType(i, column) == 'radiobutton') && (tableComponent.isCellEditable(i, column))) {
            tableComponent.setCellValue(i, column, false);
          }
        }
      }
    }
    
    this._fireValueChange(handlerInstance, handlerInstance.value);
  }/**,
  _onCellValueChanged: function (event) {
    var tableComponent = event.tableComponent;
    var handlerInstance = tableComponent.getCellEditor(event.row, event.column);
    if (this._isChecked(event.value)) {
      for (var i = 0; i < tableComponent.getRowCount(); i++) {
        if (i != event.row) {
          tableComponent.setCellValue(i, column, false);
        }
      }
    }
  },
  _isChecked: function(value) {
    if (Object.isString(value)) {
      return value == '1';
    }
    else if (Object.isNumber(value)) {
      return value == 1;
    }
    else {
      return value == true;
    }
  }**/
});

IxTableControllers.registerController(new IxRadioButtonTableEditorController());
IxDateTableEditorController = Class.create(IxTableEditorController, {
  buildEditor: function ($super, name, columnDefinition) {
    var cellEditor = this._createEditorElement("input", name, "ixTableCellEditorDate", {name: name, type: "text"}, columnDefinition);
    return cellEditor;
  },
  buildViewer: function ($super, name, columnDefinition) {
    return this._createViewerElement("div", name, "ixTableCellViewerDate", {}, columnDefinition);
  },
  disableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable == false) {
      handlerInstance.addClassName("ixTableCellViewerDisabled");
    } else {
      this._getEditorComponent(handlerInstance).disable();
    }
  },
  enableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable == false)
      handlerInstance.removeClassName("ixTableCellViewerDisabled");
    else {
      this._getEditorComponent(handlerInstance).enable();
    }
  },
  getEditorValue: function ($super, handlerInstance) {
    if (handlerInstance._editable != true) {
      return this._getViewerValue(handlerInstance);
    } else {
      return this._getEditorComponent(handlerInstance).getTimestamp();
    }
  },
  setEditorValue: function ($super, handlerInstance, value) {
    if (handlerInstance._editable != true) {
      if (value && value !== '') {
        var date = new Date();
        date.setTime(value);
        // TODO: move dateformatting to conf ...
        this._setViewerValue(handlerInstance, value, date.getDate().toPaddedString(2) + '.' + (date.getMonth() + 1).toPaddedString(2) + '.' + date.getFullYear());
      } else {
        this._setViewerValue(handlerInstance, value, '');
      }
    } else {
      if (value && value !== '') {
        this._getEditorComponent(handlerInstance).setTimestamp(value);
      } else {
        this._getEditorComponent(handlerInstance).clearValue();
      }
    }
  },
  attachContentHandler: function ($super, table, cell, handlerInstance) {
    var handlerInstance = $super(table, cell, handlerInstance);
    
    if (handlerInstance._editable == true) {
      // TODO: Click support for editor
      replaceDateField(handlerInstance);
    } else {
      handlerInstance._clickListener = this._onClick.bindAsEventListener(this);
      Event.observe(handlerInstance, "click", handlerInstance._clickListener); 
    }
  },
  detachContentHandler: function ($super, handlerInstance) {
    if (handlerInstance._editable == true) {
      // TODO: Click support for editor
      this._getEditorComponent(handlerInstance).destroy();
      $super(handlerInstance);
    } else {    
      Event.stopObserving(handlerInstance, "click", handlerInstance._clickListener);
      handlerInstance._clickListener = undefined;
      $super(handlerInstance);
    }
  }, 
  isDisabled: function ($super, handlerInstance) {
    if (handlerInstance._editable == true) {
      return this._getEditorComponent(handlerInstance).isDisabled();
    } else {    
      return handlerInstance.hasClassName("ixTableCellViewerDisabled");
    }
  },
  getDataType: function ($super) {
    return "date";  
  },
  getMode: function ($super) { 
    return IxTableControllers.EDITMODE_EDITABLE;
  },
  _onClick: function (event) {
    // TODO: Click support for editor
    var handlerInstance = Event.element(event);
    if (!handlerInstance.hasClassName("ixTableCellViewerDate")) {
      handlerInstance = handlerInstance.up(".ixTableCellViewerDate");
    }
    
    if (handlerInstance) {
      if (Object.isFunction(handlerInstance._columnDefinition.onclick)) {
        Event.stop(event);
        
        if (this.isDisabled(handlerInstance) != true) { 
          handlerInstance._columnDefinition.onclick.call(window, {
            tableComponent: handlerInstance._table,
            row: this.getEditorRow(handlerInstance),
            column: this.getEditorColumn(handlerInstance)
          });
        }
      }
    }
  },
  _getEditorComponent: function (handlerInstance) {
    var fieldId = handlerInstance.parentNode.getAttribute('ix:datefieldid');
    if (fieldId)
      return getIxDateField(fieldId);
    else
      return null;
  }
});

IxTableControllers.registerController(new IxDateTableEditorController());

IxButtonTableEditorButtonController = Class.create(IxTableEditorController, {
  buildViewer: function ($super, name, columnDefinition) {
    if (columnDefinition.imgsrc) {  
      var cellViewer = new Element("img", { src: columnDefinition.imgsrc, title: columnDefinition.tooltip ? columnDefinition.tooltip : '', className: "ixTableCellViewer ixTableCellEditorButton"});
      
      if (columnDefinition.viewerClassNames) {
        var classNames = columnDefinition.viewerClassNames.split(' ');
        for (var i = 0, l = classNames.length; i < l; i++) {
          cellViewer.addClassName(classNames[i]);
        }
      }
      
      cellViewer._editable = false;
      cellViewer._dataType = this.getDataType();
      cellViewer._name = name;
      cellViewer._columnDefinition = columnDefinition;
      
      return cellViewer;
    }
    else if (columnDefinition.value) {
      var cellViewer = new Element("input", {type: 'button', value: columnDefinition.value, style:'width:100%;display:block'});
      if (columnDefinition.viewerClassNames) {
        var classNames = columnDefinition.viewerClassNames.split(' ');
        for (var i = 0, l = classNames.length; i < l; i++) {
          cellViewer.addClassName(classNames[i]);
        }
      }
      cellViewer._editable = false;
      cellViewer._dataType = this.getDataType();
      cellViewer._name = name;
      cellViewer._columnDefinition = columnDefinition;
      
      return cellViewer;
    }
    else {
      throw new Error("Unable to build button without image or value");
    }
  },
  attachContentHandler: function ($super, table, cell, handlerInstance) {
    var handlerInstance = $super(table, cell, handlerInstance);
    handlerInstance._clickListener = this._onClick.bindAsEventListener(this);
    Event.observe(handlerInstance, "click", handlerInstance._clickListener); 
  },
  detachContentHandler: function ($super, handlerInstance) {
    Event.stopObserving(handlerInstance, "click", handlerInstance._clickListener);
    handlerInstance._clickListener = undefined;
    $super(handlerInstance);
  }, 
  disableEditor: function ($super, handlerInstance) {
    handlerInstance._disabled = true;
    handlerInstance.addClassName("ixTableButtonDisabled");
  },
  enableEditor: function ($super, handlerInstance) {
    handlerInstance._disabled = false;
    handlerInstance.removeClassName("ixTableButtonDisabled");
  },
  destroyEditor: function ($super, handlerInstance) {
    handlerInstance.remove();
  },
  isDisabled: function ($super, handlerInstance) {
    return handlerInstance._disabled == true;
  },
  getDataType: function ($super) {
    return "button";  
  },
  getMode: function ($super) {
    return IxTableControllers.EDITMODE_NOT_EDITABLE;
  },
  _onClick: function (event) {
    var handlerInstance = Event.element(event);
    if (Object.isFunction(handlerInstance._columnDefinition.onclick)) {
      Event.stop(event);
      
      if (this.isDisabled(handlerInstance) != true) { 
        handlerInstance._columnDefinition.onclick.call(window, {
          tableComponent: handlerInstance._table,
          row: this.getEditorRow(handlerInstance),
          column: this.getEditorColumn(handlerInstance)
        });
      }
    }
  }
});

IxTableControllers.registerController(new IxButtonTableEditorButtonController());

IxTextTableEditorController = Class.create(IxTableEditorController, {
  buildEditor: function ($super, name, columnDefinition) {
    var editor = this._createEditorElement("input", name, "ixTableCellEditorText", {name: name, type: "text"}, columnDefinition);

    if (columnDefinition.required)
      editor.addClassName("required");

    this._editorValueChangeListener = this._onEditorValueChange.bindAsEventListener(this);
    Event.observe(editor, "change", this._editorValueChangeListener);
    return editor;
  },
  buildViewer: function ($super, name, columnDefinition) {
    return this._createViewerElement("div", name, "ixTableCellViewerText", {}, columnDefinition);
  },
  attachContentHandler: function ($super, table, cell, handlerInstance) {
    var handlerInstance = $super(table, cell, handlerInstance);
    handlerInstance._clickListener = this._onClick.bindAsEventListener(this);
    Event.observe(handlerInstance, "click", handlerInstance._clickListener); 
  },
  detachContentHandler: function ($super, handlerInstance) {
    Event.stopObserving(handlerInstance, "click", handlerInstance._clickListener);
    handlerInstance._clickListener = undefined;
    $super(handlerInstance);
  }, 
  disableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable == false)
      handlerInstance.addClassName("ixTableCellViewerDisabled");
    else {
      this._addDisabledHiddenElement(handlerInstance);
    }
    handlerInstance.disabled = true;
  },
  enableEditor: function ($super, handlerInstance) {
    if (handlerInstance._editable != true)
      handlerInstance.removeClassName("ixTableCellViewerDisabled");
    else {
      this._removeDisabledHiddenElement(handlerInstance);
    }
    handlerInstance.disabled = false;
  },
  getEditorValue: function ($super, handlerInstance) {
    if (handlerInstance._editable != true) 
      return this._getViewerValue(handlerInstance);
    else
      return handlerInstance.value;
  },
  setEditorValue: function ($super, handlerInstance, value) {
    value = this._unescapeHtmlEntities(value);
    if (handlerInstance._editable != true) {
      this._setViewerValue(handlerInstance, value);
    } else {
      if (this.isDisabled(handlerInstance))
        this._updateDisabledHiddenElement(handlerInstance, value);
      handlerInstance.value = value;
    }
  },
  destroyEditor: function ($super, handlerInstance) {
    handlerInstance.remove();
  },
  isDisabled: function ($super, handlerInstance) {
    return handlerInstance.disabled == true;
  },
  getDataType: function ($super) {
    return "text";  
  },
  getMode: function ($super) { 
    return IxTableControllers.EDITMODE_EDITABLE;
  },
  _onClick: function (event) {
    var handlerInstance = Event.element(event);
    
    if (!handlerInstance.hasClassName("ixTableCellEditorText") && !handlerInstance.hasClassName("ixTableCellViewerText")) {
      var e = handlerInstance.up(".ixTableCellViewerText");
      if (e)
        handlerInstance = e;
      else {
        var e = handlerInstance.up(".ixTableCellEditorText");
        if (e)
          handlerInstance = e;
      }
    }
    
    if (Object.isFunction(handlerInstance._columnDefinition.onclick)) {
      if (this.isDisabled(handlerInstance) != true) { 
        handlerInstance._columnDefinition.onclick.call(window, {
          tableComponent: handlerInstance._table,
          row: this.getEditorRow(handlerInstance),
          column: this.getEditorColumn(handlerInstance)
        });
      }
    }
  },
  _onEditorValueChange: function (event) {
    var handlerInstance = Event.element(event);
    this._fireValueChange(handlerInstance, handlerInstance.value);
  }
});

IxTableControllers.registerController(new IxTextTableEditorController());

IxAutoCompleteSelectTableEditorController = Class.create(IxTableEditorController, {
  buildEditor: function ($super, name, columnDefinition) {
    var cellEditor = this._createEditorElement("div", name, "ixTableCellEditorAutoCompleteSelect tableAutoCompleteSelect", {}, columnDefinition);
    
    var classNames = "ixTableCellEditorAutoCompleteSelectText";
    if (columnDefinition.required)
      classNames += ' required';
    
    var inputElement = new Element("input", {type: "text", className: classNames, name: name + '.text'});
    var idElement = new Element("input", {type: "hidden", name: name, className: "ixTableCellEditorAutoCompleteSelectId"});
    var indicatorElement = new Element("span", {className: "autocomplete_progress_indicator", style: "display: none"}).update('<img src="' + columnDefinition.autoCompleteProgressUrl + '"/>');
    var choicesElement = new Element("div", {className: "autocomplete_choices"});  
    
    if (columnDefinition.displayValue)
      inputElement.value = columnDefinition.displayValue;
     
    cellEditor.appendChild(inputElement);
    cellEditor.appendChild(idElement);
    cellEditor.appendChild(indicatorElement);
    cellEditor.appendChild(choicesElement);
    
    var _this = this;
    new Ajax.Autocompleter(inputElement, choicesElement, columnDefinition.autoCompleteUrl, {
      paramName: 'text', 
      minChars: 1, 
      indicator: indicatorElement,
      afterUpdateElement : function getSelectionId(text, li) {
        var row = _this.getEditorRow(cellEditor);
        var column = _this.getEditorColumn(cellEditor);
        var table = _this.getTableComponent(cellEditor);

        idElement.value = $(li).down('input[name="' + (columnDefinition.autoCompleteParamName || 'id') + '"]').value;
        inputElement.validate(true, true);

        table.fire("cellValueChange", {
          tableComponent: table,
          row: row,
          column: column, 
          value: idElement.value
        });
      }
    });
    
    return cellEditor;
  },
  buildViewer: function ($super, name, columnDefinition) {
    return this._createViewerElement("div", name, "ixTableCellViewerAutoCompleteSelect", {}, columnDefinition);
  },
  attachContentHandler: function ($super, table, cell, handlerInstance) {
    var handlerInstance = $super(table, cell, handlerInstance);
    
    if (handlerInstance._editable == true) {
      var textInput = handlerInstance.down('input.ixTableCellEditorAutoCompleteSelectText');
      textInput._keyUpListener = this._onTextInputKeyUp.bindAsEventListener(this);
      Event.observe(textInput, "keyup", textInput._keyUpListener);
      
      textInput._pasteListener = this._onTextInputPaste.bindAsEventListener(this);
      Event.observe(textInput, "paste", textInput._pasteListener);
    } 
  },
  detachContentHandler: function ($super, handlerInstance) {
    if (handlerInstance._editable == true) {
      var textInput = handlerInstance.down('input.ixTableCellEditorAutoCompleteSelectText');
      Event.stopObserving(textInput, "keyup", textInput._keyUpListener);
      textInput._keyUpListener = undefined;
      
      Event.stopObserving(textInput, "paste", textInput._pasteListener);
      textInput._pasteListener = undefined;
    } 

    $super(handlerInstance);
  }, 
  getEditorValue: function ($super, handlerInstance) {
    if (handlerInstance._editable != true) 
      return this._getViewerValue(handlerInstance);
    else {
      return handlerInstance.down('input.ixTableCellEditorAutoCompleteSelectId').value;
    }
  },
  setEditorValue: function ($super, handlerInstance, value) {
    if (handlerInstance._editable != true) {
      this._setViewerValue(handlerInstance, value);
    } else {
      if (this.isDisabled(handlerInstance))
        this._updateDisabledHiddenElement(handlerInstance, value);
      
      var idInput = handlerInstance.down('input.ixTableCellEditorAutoCompleteSelectId');
      idInput.value = value;
    }
  },
  getDisplayValue: function (handlerInstance) {
    if (handlerInstance._editable != true) {
      return handlerInstance._fieldContent.innerHTML;
    } else {
      return handlerInstance.down('input.ixTableCellEditorAutoCompleteSelectText').value;
    }
  },
  setDisplayValue: function (handlerInstance, displayValue) {
    if (handlerInstance._editable) {
      var textInput = handlerInstance.down('input.ixTableCellEditorAutoCompleteSelectText');
      textInput.value = displayValue;
      textInput.validate(true);
    } else {
      this._setViewerValue(handlerInstance, this.getEditorValue(handlerInstance), displayValue); 
    }
  },
  destroyEditor: function ($super, handlerInstance) {
    handlerInstance.remove();
  },
  isDisabled: function ($super, handlerInstance) {
    // TODO: 
    return false;
//     return handlerInstance.disabled;
  },
  getDataType: function ($super) {
    return "autoCompleteSelect";  
  },
  getMode: function ($super) { 
    return IxTableControllers.EDITMODE_EDITABLE;
  },
  copyCellValue: function($super, target, source) {
    this.setEditorValue(target, this.getEditorValue(source));
    this.setDisplayValue(target, this.getDisplayValue(source));
  },
  setEditable: function ($super, handlerInstance, editable) {
    if (handlerInstance._editable == editable)
      return handlerInstance;
    if ((this.getMode(handlerInstance) == IxTableControllers.EDITMODE_ONLY_EDITABLE) && (editable == false))
      return handlerInstance;
    if ((this.getMode(handlerInstance) == IxTableControllers.EDITMODE_NOT_EDITABLE) && (editable == true))
      return handlerInstance;
    
    var table = handlerInstance._table;
    var cell = handlerInstance._cell;
    var visible = this.isVisible(handlerInstance); 
    var displayValue = this.getDisplayValue(handlerInstance);
    
    this.detachContentHandler(handlerInstance);
    
    var newHandler = editable == true ? this.buildEditor(handlerInstance._name, handlerInstance._columnDefinition) : this.buildViewer(handlerInstance._name, handlerInstance._columnDefinition);
    this.attachContentHandler(table, cell, newHandler);
    
    if (visible) 
      this.show(newHandler);
    else 
      this.hide(newHandler);
    
    this.setEditorValue(newHandler, this.getEditorValue(handlerInstance));
    this.destroyHandler(handlerInstance);
    
    this.setDisplayValue(newHandler, displayValue);

    return newHandler;
  },
  _onTextInputKeyUp: function (event) {
    var textInput = Event.element(event);
    var handlerInstance = textInput.parentNode;
    var hiddenInput = handlerInstance.down('input.ixTableCellEditorAutoCompleteSelectId');
    hiddenInput.value = -1;
  },
  _onTextInputPaste: function (event) {
    Event.stop(event);
    return false;
  }
});

IxTableControllers.registerController(new IxAutoCompleteSelectTableEditorController());

IxAutoCompleteTextTableEditorController = Class.create(IxTableEditorController, {
  buildEditor: function ($super, name, columnDefinition) {
    var cellEditor = this._createEditorElement("div", name, "ixTableCellEditorAutoComplete tableAutoComplete", {}, columnDefinition);
    
    var classNames = "ixTableCellEditorAutoCompleteText";
    if (columnDefinition.required)
      classNames += ' required';
    
    var inputElement = new Element("input", {type: "text", className: classNames, name: name});
    var indicatorElement = new Element("span", {className: "autocomplete_progress_indicator", style: "display: none"}).update('<img src="' + columnDefinition.autoCompleteProgressUrl + '"/>');
    var choicesElement = new Element("div", {className: "autocomplete_choices"});  
    
    if (columnDefinition.displayValue)
      inputElement.value = columnDefinition.displayValue;
     
    cellEditor.appendChild(inputElement);
    cellEditor.appendChild(indicatorElement);
    cellEditor.appendChild(choicesElement);
    
    var _this = this;
    new Ajax.Autocompleter(inputElement, choicesElement, columnDefinition.autoCompleteUrl, {
      paramName: 'text', 
      minChars: 1, 
      indicator: indicatorElement,
      afterUpdateElement : function getSelectionId(text, li) {
        var row = _this.getEditorRow(cellEditor);
        var column = _this.getEditorColumn(cellEditor);
        var table = _this.getTableComponent(cellEditor);
        var li = $(li);

        if (columnDefinition.onAutoCompleteValueSelect) {
          columnDefinition.onAutoCompleteValueSelect({
            liElement: li,
            row: row,
            column: column
          });
        }

        table.fire("cellValueChange", {
          tableComponent: table,
          row: row,
          column: column, 
          value: inputElement.value
        });
      }
    });
    
    return cellEditor;
  },
  buildViewer: function ($super, name, columnDefinition) {
    return this._createViewerElement("div", name, "ixTableCellViewerAutoComplete", {}, columnDefinition);
  },
  getEditorValue: function ($super, handlerInstance) {
    if (handlerInstance._editable != true) 
      return this._getViewerValue(handlerInstance);
    else {
      return handlerInstance.down('input.ixTableCellEditorAutoCompleteText').value;
    }
  },
  setEditorValue: function ($super, handlerInstance, value) {
    if (handlerInstance._editable != true) {
      this._setViewerValue(handlerInstance, value);
    } else {
      if (this.isDisabled(handlerInstance))
        this._updateDisabledHiddenElement(handlerInstance, value);
      
      var textInput = handlerInstance.down('input.ixTableCellEditorAutoCompleteText');
      textInput.value = value;
    }
  },
  destroyEditor: function ($super, handlerInstance) {
    handlerInstance.remove();
  },
  isDisabled: function ($super, handlerInstance) {
    // TODO: 
    return false;
//     return handlerInstance.disabled;
  },
  getDataType: function ($super) {
    return "autoComplete";  
  },
  getMode: function ($super) { 
    return IxTableControllers.EDITMODE_EDITABLE;
  }
});

IxTableControllers.registerController(new IxAutoCompleteTextTableEditorController());

_IxTable_ROWSORT = Class.create({
  initialize : function(column, sortDirection) {
    this._column = column;
    this._sortDirection = sortDirection;
  },
  getColumn: function() {
    return this._column;
  },
  getSortDirection: function() {
    return this._sortDirection;
  },
  compare: function (sortEvent, rowIndex1, rowIndex2) {
    return 0;
  }
});

IxTable_ROWSTRINGSORT = Class.create(_IxTable_ROWSORT, {
  compare: function (sortEvent, rowIndex1, rowIndex2) {
    var table = sortEvent.tableComponent;
    var s1 = new String(table.getCellValue(rowIndex1, this.getColumn())).toLowerCase();
    var s2 = new String(table.getCellValue(rowIndex2, this.getColumn())).toLowerCase();

    var result = s1 == s2 ? 0 : s1 < s2 ? -1 : 1; 
    
    if (this.getSortDirection() == "desc")
      return result * -1;
    return result;
  }
});

IxTable_ROWNUMBERSORT = Class.create(_IxTable_ROWSORT, {
  compare: function (sortEvent, rowIndex1, rowIndex2) {
    var table = sortEvent.tableComponent;
    var n1 = 0 + table.getCellValue(rowIndex1, this.getColumn());
    var n2 = 0 + table.getCellValue(rowIndex2, this.getColumn());

    var result = n1 == n2 ? 0 : n1 < n2 ? -1 : 1; 
    
    if (this.getSortDirection() == "desc")
      return result * -1;
    return result;
  }
});

IxTable_ROWSELECTSORT = Class.create(_IxTable_ROWSORT, {
  compare: function (sortEvent, rowIndex1, rowIndex2) {
    var table = sortEvent.tableComponent;
    var controller = IxTableControllers.getController("select");

    var s1 = new String(controller.getDisplayValue(table.getCellEditor(rowIndex1, this.getColumn()))).toLowerCase();
    var s2 = new String(controller.getDisplayValue(table.getCellEditor(rowIndex2, this.getColumn()))).toLowerCase();

    var result = s1 == s2 ? 0 : s1 < s2 ? -1 : 1; 
    
    if (this.getSortDirection() == "desc")
      return result * -1;
    return result;
  }
});

_IxTable_FILTER = Class.create({
  initialize : function(column) {
    this._column = column; 
  },
  execute: function (event) {
  },
  getColumn: function() {
    return this._column;
  }
});

_IxTable_TABLESTRINGFILTER = Class.create(_IxTable_FILTER, {
  initialize : function($super, column, filterValue, rowFilterableFunc, inclusive) {
    $super(column);
    this._filterValue = filterValue;
    this._rowFilterableFunc = rowFilterableFunc;
    this._inclusive = inclusive;
  },
  execute: function ($super, event) {
    var table = event.tableComponent;
    var filterFunc = this._rowFilterableFunc;
    var hasFilterFunc = !(this._rowFilterableFunc == undefined);
    
    var hideArray = new Array();
    
    for (var i = table.getRowCount() - 1; i >= 0; i--) {
      var rowValue = table.getCellValue(i, this.getColumn());
      var match = this._inclusive ? rowValue != this._filterValue : rowValue == this._filterValue; 

      // TODO: ???
      if (match) {
        if ((!hasFilterFunc) || (filterFunc(table, i) === true))
          hideArray.push(i);
      }
    }

    if (hideArray.size() > 0)
      table.hideRows(hideArray.toArray());
  }  
});

IxTable_ROWSTRINGFILTER = Class.create({
  initialize : function(rowFilterableFunc, inclusive) {
    this._rowFilterableFunc = rowFilterableFunc;
    if (inclusive != undefined)
      this._inclusive = inclusive === false ? false : true;
    else
      this._inclusive = true;
  },
  execute: function (event) {
    var table = event.tableComponent;
    var row = event.row;
    var column = event.column;
    var filterValue = table.getCellValue(row, column);
    var filter = new _IxTable_TABLESTRINGFILTER(column, filterValue, this._rowFilterableFunc, this._inclusive);

    table.addFilter(filter);
  }
});

_IxTable_TABLEEMPTYFILTER = Class.create(_IxTable_FILTER, {
  initialize : function($super, column, inclusive) {
    $super(column);
    this._inclusive = inclusive;
  },
  execute: function ($super, event) {
    var table = event.tableComponent;
    var hideArray = new Array();
    
    for (var i = table.getRowCount() - 1; i >= 0; i--) {
      var rowValue = table.getCellValue(i, this.getColumn());
      var match = this._inclusive ? 
          ((rowValue != "") && (rowValue != undefined)) : 
            ((rowValue == "") || (rowValue == undefined)); 

      if (match) {
        hideArray.push(i);
      }
    }

    if (hideArray.size() > 0)
      table.hideRows(hideArray.toArray());
  }  
});

IxTable_ROWEMPTYFILTER = Class.create({
  initialize : function(inclusive) {
    if (inclusive != undefined)
      this._inclusive = inclusive === false ? false : true;
    else
      this._inclusive = true;
  },
  execute: function (event) {
    var table = event.tableComponent;
    var column = event.column;
    var filter = new _IxTable_TABLEEMPTYFILTER(column, this._inclusive);

    table.addFilter(filter);
  }
});

_IxTable_TABLEDATEFILTER = Class.create(_IxTable_FILTER, {
  initialize : function($super, column, filterValue, filterEarlier, rowFilterableFunc) {
    $super(column);
    this._filterValue = filterValue;
    this._filterEarlier = filterEarlier;
    this._rowFilterableFunc = rowFilterableFunc;
  },
  execute: function ($super, event) {
    var table = event.tableComponent;
    var filterFunc = this._rowFilterableFunc;
    var hasFilterFunc = !((filterFunc == undefined) || (filterFunc == null));
    
    var hideArray = new Array();

    if (this._filterEarlier) {
      for (var i = table.getRowCount() - 1; i >= 0; i--) {
        var rowValue = table.getCellValue(i, this.getColumn());
        if ((rowValue) && (rowValue > this._filterValue)) { 
          if ((!hasFilterFunc) || (filterFunc(table, i) === true))
            hideArray.push(i);
        }
      }
    } else {
      for (var i = table.getRowCount() - 1; i >= 0; i--) {
        var rowValue = table.getCellValue(i, this.getColumn());
        if ((rowValue) && (rowValue < this._filterValue)) { 
          if ((!hasFilterFunc) || (filterFunc(table, i) === true))
            hideArray.push(i);
        }
      }
    }
  
    if (hideArray.size() > 0)
      table.hideRows(hideArray.toArray());
  }
});

IxTable_ROWDATEFILTER = Class.create({
  initialize : function(filterEarlier, rowFilterableFunc) {
    this._filterEarlier = filterEarlier;
    this._rowFilterableFunc = rowFilterableFunc;
  },
  execute: function (event) {
    var table = event.tableComponent;
    var row = event.row;
    var column = event.column;
    var filterValue = table.getCellValue(row, column);

    var filter = new _IxTable_TABLEDATEFILTER(column, filterValue, this._filterEarlier, this._rowFilterableFunc);
    table.addFilter(filter);
  }
});

IxTable_ROWCLEARFILTER = Class.create({
  initialize : function() {
  },
  execute: function (event) {
    var table = event.tableComponent;
    table.clearFilters();
  }  
});

IxTable_COPYVALUESTOCOLUMNACTION = Class.create({
  initialize : function(onlyModifiable) {
    this._onlyModifiable = onlyModifiable;
  },
  execute: function (event) {
    var table = event.tableComponent;
    var column = event.column;
    var row = event.row;
    
    var sourceEditor = table.getCellEditor(row, column);
    var controller = IxTableControllers.getController(table.getCellEditor(row, column)._dataType);
    
    for (var i = 0, len = table.getRowCount(); i < len; i++) {
      if (i != row) {
        var cellEditor = table.getCellEditor(i, column);
        var settable = this._onlyModifiable ? controller.getEditable(cellEditor) : true; 
  
        if (settable)
          controller.copyCellValue(cellEditor, sourceEditor);
      }
    }
  }
});
