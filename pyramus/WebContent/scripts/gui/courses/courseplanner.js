CoursePlanner = Class.create({
  initialize : function(container, options) {
    this._options = Object.extend({
      showMonthLines: true,
      showWeekLines: true,
      showDayLines: true,
      showMonthLabels: true,
      showWeekLabels: false,
      showDayLabels: false,
      zoomingEnabled: true,
      showYearLabel: true,
      showPointerLabel: true,
      courseHeight: 60,
      trackHeight: 70,
      trackSpacing: 8,
      trackIntersectOffset: 4,
      courseBaseZIndex: 500,
      initialTimeFrame: 1000 * 60 * 60 * 24 * 30,
      courseBackgroundColors: [ "#800080", "#8000ff", "#808000", "#8080ff", "#80ff00", "#80ff80", "#80ffff", "#ff0000", "#ff0080", "#800000", "#00ffff", "#00ff80", "#00ff00", "#0000ff", "#000080", "#0080ff", "#008080", "#008000", "#ff00ff", "#ff8000", "#ff8080", "#ff80ff" ]
    }, options||{});

    this._courseColorIndex = 0;
    this._msPixelRatio = 0;
    this._filters = new Array();
    this._courses = new Array();
    this._filteredCourses = null;
    this._zeroTime = new Date();
    this._zeroTime.setFullYear(2010, 1 - 1, 1);
    this._timeFrame = this._options.initialTimeFrame;
    this._viewOffsetX = 0;
    this._viewOffsetY = 0;
    
    this._domNode = new Element("div", {
      className : "coursePlanner"
    });
    this._view = new Element("div", {
      className : "coursePlannerView"
    });
    this._labels = new Element("div", {
      className : "coursePlannerLabels"
    });
    this._verticalPointer = new Element("div", {
      className : "coursePlannerVerticalPointer"
    });
    
    if (this._options.showPointerLabel) {
      this._pointerLabel = new Element("div", {
        className : "coursePlannerPointerLabel"
      });
      this._domNode.appendChild(this._pointerLabel);
    }
    
    this._dragHightlight = new Element("div", {className: "coursePlannerTrackHighlight"});
    
    if (this._options.showYearLabel) {
      this._yearLabelContainer = new Element("div", {
        className : "coursePlannerYearLabelContainer"
      });
      this._yearLabel = new Element("div", {
        className : "coursePlannerYearLabel"
      });
      this._yearLabelContainer.appendChild(this._yearLabel);
      this._domNode.appendChild(this._yearLabelContainer);
      this._yearLabelClickListener = this._onYearLabelClick.bindAsEventListener(this);
      Event.observe(this._yearLabel, "click", this._yearLabelClickListener);
    }
    
    this._view.appendChild(this._verticalPointer);
    this._view.appendChild(this._dragHightlight);
    this._domNode.appendChild(this._view);
    this._domNode.appendChild(this._labels);
    
    this._domNode.ondragstart = function(event) { 
      Event.stop(event);
      return false; 
    };
    
    this._domNode.onselectstart = function(event) { 
      Event.stop(event);
      return false; 
    };
    
    this._domNode.unselectable = "on";
    this._domNode.style.MozUserSelect = "none";
    
    this._viewMouseDownListener = this._onViewMouseDown.bindAsEventListener(this);
    this._viewMouseUpListener = this._onViewMouseUp.bindAsEventListener(this);
    this._viewMouseMoveListener = this._onViewMouseMove.bindAsEventListener(this);
    this._monthLabelClickListener = this._onMonthLabelClick.bindAsEventListener(this);
    
    Event.observe(this._view, "mousedown", this._viewMouseDownListener);
    Event.observe(this._view, "mouseup", this._viewMouseUpListener);
    Event.observe(this._view, "mousemove", this._viewMouseMoveListener);
    
    if (this._options.zoomingEnabled) {
      this._viewScrollListener = this._onViewScroll.bindAsEventListener(this);
      Event.observe(this._view, "mousewheel", this._viewScrollListener);
      Event.observe(this._view, "DOMMouseScroll", this._viewScrollListener); 
    }

    container.appendChild(this._domNode);

    this._updateMsPixelRatio();
    this._renderBackground();
    this._refreshVisibleCourses();
    this._renderCourses();
    this._refreshYear();
  },
  addCourse : function(course, movable) {
    this.addCourses([course], movable);
  },
  addCourses : function(courses, movable) {
    for (var i = 0, l = courses.length; i < l; i++) {
      var course = courses[i];
      
      course.setBackgroundColor(this._nextNextBackgroundColor());
      course.setHeight(this._options.courseHeight);
  
      var courseDomNode = course.getDomNode();
      if (movable) {
        new Draggable(courseDomNode, {
          onDrag : this._onCourseDrag.bind(this),
          onEnd : this._onCourseDrop.bind(this),
          onStart : this._onCourseDragStart.bind(this),
          ghosting : true
        });
        courseDomNode.addClassName('coursePlannerCourseMovable');
      }
      this._courses.push(course);
  
      this._view.appendChild(courseDomNode);
      
      course.setOffset(0);
      course.setZIndex(this._options.courseBaseZIndex);
    }
    
    this._filteredCourses = null;
    // TODO: no need to refresh all
    this._refreshVisibleCourses();
    this._renderCourses();
  },
  _arrageCourseToTracks: function (courses, course) {
    var intersectingCourses = this._getIntersectingCourses(courses, course);
    for (var i = 0, l = intersectingCourses.length; i < l; i++) {
      var intersectingCourse = intersectingCourses[i];
      if (intersectingCourse.getTrack() == course.getTrack()) {
        intersectingCourse.setTrack(intersectingCourse.getTrack() + 1);
        this._arrageCourseToTracks(intersectingCourses, intersectingCourse);
      }
    }
  },
  arrageCoursesToTracks: function () {
    var courses = this._getFilteredCourses().clone();
    courses.sort(function (course1, course2) {
      return course2.getStartDate().getTime() - course1.getStartDate().getTime();
    });  

    for (var i = 0, l = courses.length; i < l; i++) {
      courses[i].setTrack(0);
    }
    
    for (var i = courses.length - 1; i >= 0; i--) {
      this._arrageCourseToTracks(courses, courses[i]);
    }
    
    this._renderCourses();
  },
  setDateRange: function (from, to) {
    if (to.getTime() > from.getTime()) {
      this._timeFrame = to.getTime() - from.getTime();
      this._updateMsPixelRatio();
      this._viewOffsetX = this._getDateInPixels(from);
      this._renderBackground();
      this._refreshVisibleCourses();
      this._renderCourses();
      this._refreshYear();
    }
  },
  addFilter: function (filter) {
    this._filters.push(filter);
    this._filteredCourses = null;
    this._refreshVisibleCourses();
    this._renderCourses();
  },
  clearFilters: function () {
    this._filters.clear();
    this._filteredCourses = null;
    this._refreshVisibleCourses();
    this._renderCourses();
  },
  _getFilteredCourses: function () {
    if (this._filteredCourses === null) {
      this._filteredCourses = this._courses.clone();
      
      for (var i = this._filteredCourses.length - 1; i >= 0; i--) {
        if (!this._filteredCourses[i].isDetachedFromDom()) {
          this._filteredCourses[i].detachFromDom();
        }
      }
      
      for (var filterIndex = 0, filtersLength = this._filters.length; filterIndex < filtersLength; filterIndex++) {
        var filter = this._filters[filterIndex];
        for (var i = this._filteredCourses.length - 1; i >= 0; i--) {
          if (filter.filter(this._filteredCourses[i])) {
            this._filteredCourses.splice(i, 1)[0];
          }
        }
      }
      
      for (var i = this._filteredCourses.length - 1; i >= 0; i--) {
        this._filteredCourses[i].reattachToDom();
      }
    }
    
    return this._filteredCourses;
  },
  _getWeekNumber: function (date) {
    // Thanks from this method goes to Stephen Chapman (http://javascript.about.com/library/blweekyear.htm)
    var onejan = new Date(date.getFullYear(),0,1);
    return (Math.ceil((((date - onejan) / 86400000) + onejan.getDay()+1)/7) - 1);
  },
  _dateRangesIntersect: function (dateRange1Start, dateRange1End, dateRange2Start, dateRange2End) {
    return (dateRange2Start <= dateRange1End) && (dateRange2End >= dateRange1Start);
  },
  _coursesIntersect: function (course1, course2) {
    return this._dateRangesIntersect(course1.getStartDate(), course1.getEndDate(), course2.getStartDate(), course2.getEndDate());
  },
  _getIntersectingCourses: function (courses, course) {
    var result = new Array();
    for (var i = 0, l = courses.length; i < l; i++) {
      if (this._coursesIntersect(courses[i], course)) {
        if (courses[i] != course)
          result.push(courses[i]);
      }
    }
    return result;
  },
  _getIntersectingCoursesAfter: function (courses, course) {
    var result = new Array();
    for (var i = 0, l = courses.length; i < l; i++) {
      if (courses[i] != course) {
        if ((courses[i].getStartDate() > course.getStartDate()) && (courses[i].getStartDate() < course.getEndDate())) {
          result.push(courses[i]);
        }
      }
    }
    
    return result;
  },
  _getTrackCoursesInView: function (track) {
    var result = new Array();
    for (var i = 0, l = this._visibleCourses.length; i < l; i++) {
      var course = this._visibleCourses[i];
      if (course.getTrack() == track) 
        result.push(course);
    }
    return result;
  },
  _getTrackCount: function () {
    var result = 0;
    for (var i = 0, l = this._getFilteredCourses().length; i < l; i++) {
      result = Math.max(result, this._getFilteredCourses()[i].getTrack() + 1);
    }
    return result;
  },
  _getCourseById : function(id) {
    for ( var i = 0, l = this._courses.length; i < l; i++) {
      if (this._courses[i].getId() == id)
        return this._courses[i];
    }

    return null;
  },
  _getCourseByElement : function(element) {
    return this._getCourseById(element.id);
  },
  _updateMsPixelRatio : function() {
    var plannerWidth = this._domNode.getWidth();
    this._msPixelRatio = plannerWidth / this._timeFrame;
  },
  _getDateInPixels : function(date) {
    return this._msPixelRatio * (date.getTime() - this._zeroTime.getTime());
  },
  _getPixelsInDate : function(left) {
    return new Date(this._zeroTime.getTime() + (left / this._msPixelRatio));
  },
  _getViewStartDate : function() {
    return this._getPixelsInDate(this._viewOffsetX);
  },
  _getViewEndDate : function() {
    return this._getPixelsInDate(this._viewOffsetX + this._domNode.getWidth());
  },
  _renderBackground : function() {
    // TODO: Also stop observing...
    
    if (this._options.showMonthLines)
      this._view.select('.coursePlannerMonthLine').invoke('remove');
    if (this._options.showWeekLines)
      this._view.select('.coursePlannerWeekLine').invoke('remove');
    if (this._options.showDayLines)
      this._view.select('.coursePlannerDayLine').invoke('remove');
    if (this._options.showMonthLabels) 
      this._labels.select('.coursePlannerMonthLabel').invoke('remove');
    if (this._options.showWeekLabels) 
      this._labels.select('.coursePlannerWeekLabel').invoke('remove');
    if (this._options.showDayLabels) 
      this._labels.select('.coursePlannerDayLabel').invoke('remove');

    var days = new Array();
    var weeks = new Array();
    var months = new Array();

    var viewStartDateTime = this._getViewStartDate();
    var viewEndDateTime = this._getViewEndDate();
    var date = new Date(viewStartDateTime);
    var timeOffset = 100 / this._msPixelRatio; 

    while ((date.getTime() - timeOffset) < (viewEndDateTime.getTime() + timeOffset)) {
      var day = new Date();
      day.setFullYear(date.getFullYear(), date.getMonth(), date.getDate());

      if (date.getDate() == 1) {
        if (this._options.showMonthLines||this._options.showMonthLabels)
          months.push(day);
      }

      if (date.getDay() == 0) {
        if (this._options.showWeekLines||this._options.showWeekLabels)
          weeks.push(day);
      }

      if (this._options.showDayLines||this._options.showDayLabels)
        days.push(day);

      date.setDate(date.getDate() + 1);
    }

    for (var i = 0, l = days.length; i < l; i++) {
      var date = days[i];
      var left = (date.getTime() - viewStartDateTime) * this._msPixelRatio;
      
      if (this._options.showDayLines) {
        this._view.appendChild(new Element("div", {
          className : "coursePlannerDayLine",
          style : "left: " + left + 'px'
        }));
      }

      if (this._options.showDayLabels) {
        this._labels.appendChild(new Element("div", {
          className : "coursePlannerDayLabel",
          style : "left: " + left + 'px'
        }).update(this._formatDay(date)));      
      }
    }

    for ( var i = 0, l = weeks.length; i < l; i++) {
      var date = weeks[i];
      var left = (date.getTime() - viewStartDateTime) * this._msPixelRatio;
      
      if (this._options.showWeekLines) {
        this._view.appendChild(new Element("div", {
          className : "coursePlannerWeekLine",
          style : "left: " + left + 'px'
        }));
      }

      if (this._options.showWeekLabels) {
        this._labels.appendChild(new Element("div", {
          className : "coursePlannerWeekLabel",
          style : "left: " + left + 'px'
        }).update(this._getWeekNumber(date)));
      }
    }

    for (var i = 0, l = months.length; i < l; i++) {
      var date = months[i];
      var left = (date.getTime() - viewStartDateTime) * this._msPixelRatio;
      
      if (this._options.showMonthLines) {
        this._view.appendChild(new Element("div", {
          className : "coursePlannerMonthLine",
          style : "left: " + left + 'px'
        }));
      }
        
      if (this._options.showMonthLabels) {
        var monthLabelElement = new Element("div", {
          className : "coursePlannerMonthLabel",
          style : "left: " + left + 'px'
        }).update(this._formatMonth(date));
        
        monthLabelElement._year = date.getFullYear();
        monthLabelElement._month = date.getMonth();
        
        Event.observe(monthLabelElement, "click", this._monthLabelClickListener);
        
        this._labels.appendChild(monthLabelElement);
      }
    }
  },
  _refreshYear: function () {
    this._yearLabel.update(this._getViewEndDate().getFullYear());
  },
  _refreshVisibleCourses: function () {
    var viewStartDateTime = this._getViewStartDate();
    var viewEndDateTime = this._getViewEndDate();
    
    this._visibleCourses = new Array();
    
    for ( var i = 0, l = this._getFilteredCourses().length; i < l; i++) {
      var course = this._getFilteredCourses()[i];
      var courseStartDateTime = course.getStartDate();
      var courseEndDateTime = course.getEndDate();
      
      if (this._dateRangesIntersect(viewStartDateTime, viewEndDateTime, courseStartDateTime, courseEndDateTime)) {
        this._visibleCourses.push(course); 
        if (course.isDetachedFromDom()) {
          course.reattachToDom();
        }
      } else {
        if (!course.isDetachedFromDom()) {
          course.detachFromDom();
        }
      }
    }
  },
  _renderCourses : function() {
    var viewStartDateTime = this._getViewStartDate().getTime();
    var viewEndDateTime = this._getViewEndDate().getTime();
    var trackCourses = new Object();
    var trackCount = this._getTrackCount();
    if (trackCount > 0) {
      for (var track = 0; track < trackCount; track++) {
        trackCourses[track] = this._getTrackCoursesInView(track).sortBy(function (course) {
          return course.getStartDate().getTime();  
        });
      }
  
      for ( var i = 0, l = this._visibleCourses.length; i < l; i++) {
        var course = this._visibleCourses[i];
        var courseStartDateTime = course.getStartDate().getTime();
        var courseEndDateTime = course.getEndDate().getTime();
        
        var left = (courseStartDateTime - viewStartDateTime) * this._msPixelRatio;
        var width = ((courseEndDateTime - viewStartDateTime) * this._msPixelRatio) - left;
        var top = ((this._options.trackHeight * course.getTrack()) + this._options.trackSpacing) - this._viewOffsetY;
        
        if (trackCourses[course.getTrack()]) {
          var intersectingCourses = this._getIntersectingCourses(trackCourses[course.getTrack()], course); 
          if (intersectingCourses.length > 0) {
            var courseIndex = trackCourses[course.getTrack()].indexOf(course);
            if (courseIndex > 0) {
              var previousCourse = trackCourses[course.getTrack()][courseIndex - 1];
              var offset = previousCourse.getOffset() + this._options.trackIntersectOffset;
              course.setOffset(offset);
              top += offset;
              course.setZIndex(previousCourse.getZIndex() + 1);
            } else {
              course.setOffset(0);
              course.setZIndex(this._options.courseBaseZIndex);
            }
          }
        }
        
        course.draw(left, width, top);
      }
    }
  },
  _nextNextBackgroundColor : function() {
    var backgroundColor = this._options.courseBackgroundColors[this._courseColorIndex];
    this._courseColorIndex = (this._courseColorIndex + 1) % this._options.courseBackgroundColors.length;
    return backgroundColor;
  },
  _onCourseDrag : function(draggable, event) {
  },
  _onCourseDrop : function(draggable, event) {
    draggable.element.removeClassName("coursePlannerCourseDragging");

    var left = this._cursorPosX + this._viewOffsetX - draggable.offset[0];
    var newDate = this._getPixelsInDate(left);
    var course = this._getCourseByElement(draggable.element);
    course.moveStartDate(newDate);
    course.setTrack(this._selectedTrack);
    // TODO: no need to refresh all
    this._refreshVisibleCourses();
    this._renderCourses();
  },
  _onCourseDragStart : function(draggable, event) {
    draggable.element.addClassName("coursePlannerCourseDragging");
  },
  _onViewMouseDown : function(event) {
    this._draggingView = true;
  },
  _onViewMouseUp : function(event) {
    this._draggingView = false;
    
    this._renderCourses();
  },
  _onViewMouseMove : function(event) {
    var mouseX = event.pointerX();
    var mouseY = event.pointerY();
    
    var viewOffset = this._domNode.cumulativeOffset();
    
    this._oldCursorPosX = this._cursorPosX;
    this._oldCursorPosY = this._cursorPosY;
    this._cursorPosX = mouseX - (9 + viewOffset.left);
    this._cursorPosY = mouseY - viewOffset.top;
    
    if (this._draggingView) {
      this._viewOffsetX += this._oldCursorPosX - this._cursorPosX;
      this._viewOffsetY += this._oldCursorPosY - this._cursorPosY;
      if (this._viewOffsetY < 0)
        this._viewOffsetY = 0;
      this._renderBackground();
      this._refreshVisibleCourses();
      this._renderCourses();
      this._refreshYear();
    }
        
    var trackOffset = this._viewOffsetY % this._options.trackHeight;
    var top = this._cursorPosY + trackOffset;
    var drawTrack = Math.floor((top - (this._options.trackSpacing * 2)) / this._options.trackHeight); 
    var hightlightTop = ((drawTrack * this._options.trackHeight) + this._options.trackSpacing / 2) - trackOffset;
    
    this._dragHightlight.setStyle({
      top: hightlightTop + 'px',
      height: this._options.trackHeight + 'px'
    }); 
    
    this._selectedTrack = drawTrack + Math.round(this._viewOffsetY / (this._options.trackHeight + (this._options.trackSpacing / 2)));
    if (this._selectedTrack < 0)
      this._selectedTrack = 0;

    this._verticalPointer.setStyle({
      left : this._cursorPosX + 'px'
    });
    
    if (this._options.showPointerLabel) {
      this._pointerLabel.setStyle({
        left : this._cursorPosX + 'px'
      });
      this._pointerLabel.update(getLocale().getDate(this._getPixelsInDate(this._viewOffsetX + this._cursorPosX).getTime(), false));
    }
  },
  _onViewScroll: function (event) {
    Event.stop(event);
    var endDate = new Date(this._getViewEndDate().getTime() + ((1000 * 60 * 60 * 24 * 10) * -this._wheelDelta(event)));
    this.setDateRange(this._getViewStartDate(), endDate);
  },
  _onYearLabelClick: function (event) {
    if (Object.isFunction(this._options.onYearLabelClick)) {
      var element = Event.element(event);
      this._options.onYearLabelClick.call(this, {
        year: this._getViewEndDate().getFullYear()
      });
    }
  },
  _onMonthLabelClick: function (event) {
    if (Object.isFunction(this._options.onMonthLabelClick)) {
      var element = Event.element(event);
      this._options.onMonthLabelClick.call(this, {
        year: element._year,
        month: element._month
      });
    }
  },
  _wheelDelta: function (event) {
    // http://code.google.com/p/fni/source/browse/trunk/fni/WebContent/scripts/fniprototypeext/fniprototypeext.js
    if (navigator.userAgent.indexOf('Chrome') > 0) 
      return event.wheelDelta / 360;
    else    
      return ((Prototype.Browser.Gecko == true)||(Prototype.Browser.Opera == true))?(event.detail / 3) * -1: event.wheelDelta ? event.wheelDelta / 120 : (event.detail / 3) * -1;  
  },
  _formatMonth: function (date) {
    var dateFormatter = new fni.locale.dateformat.FNIDateFormat("MMMM yyyy");
    return dateFormatter.format(getLocale(), date);
  },
  _formatDay: function (date) {
    return date.getDate() + '.' + (date.getMonth() + 1) + '.';
  }
});

CoursePlannerCourse = Class.create({
  initialize : function(options) {
    this._options = options;

    this._id = 'cpc-' + new Date().getTime();
    
    this._startDate = options.courseStartDate;
    this._endDate = options.courseEndDate;
    this._track = options.track;
    this._index = options.index;
    this._offset = 0;

    this._domNode = new Element("div", {
      className : "coursePlannerCourse",
      id : this._id
    });
    
    this._datesElement = new Element("div", {
      className : "coursePlannerCourseDates"
    });
    this._nameElement = new Element("div", {
      className : "coursePlannerCourseName"
    });

    this._domNode.appendChild(this._nameElement);
    this._domNode.appendChild(this._datesElement);
    
    if (options.resizable) {
      this._resizeHandle = new Element("div", {
        className : "coursePlannerCourseResizeHandle"
      });
      this._domNode.appendChild(this._resizeHandle);
      /**
      this._resizeHandleMouseDownListener = this._onResizeHandleMouseDown.bindAsEventListener(this);
      this._resizeHandleMouseUpListener = this._onResizeHandleMouseUp.bindAsEventListener(this);
      this._resizeHandleMouseMoveListener = this._onResizeHandleMouseMove.bindAsEventListener(this);
      **/
    }
    
    this.setName(options.name);
    this._drawDates();
  },
  getId : function() {
    return this._id;
  },
  getDomNode : function() {
    return this._domNode;
  },
  getStartDate : function() {
    return this._startDate;
  },
  getEndDate : function() {
    return this._endDate;
  },
  setStartDate : function(date) {
    this._startDate = date;
    this._drawDates();
  },
  setEndDate : function(date) {
    this._endDate = date;
    this._drawDates();
  },
  moveStartDate : function(date) {
    var diff = date.getTime() - this.getStartDate().getTime();
    this._startDate.setTime(date.getTime());
    this._endDate.setTime(this.getEndDate().getTime() + diff);
    this._drawDates();
  },
  getZIndex: function () {
    return this._zIndex;
  },
  setZIndex: function (zIndex) {
    this._zIndex = zIndex;
    // TODO: Why this does not work with Firefox 4 ?
    this._domNode.setStyle({
      zIndex: zIndex
    }); 
  },
  getTrack: function () {
    return this._track;
  },
  setTrack: function (track) {
    this._track = track;
  },
  setName: function (name) {
    this._nameElement.update(name);
    this._domNode.setAttribute("title", name);
  },
  getOffset: function () {
    return this._offset;
  },
  setOffset: function (offset) {
    this._offset = offset;
  },
  draw : function(left, width, top) {
    this._domNode.setStyle({
      left : left + 'px',
      width : width + 'px',
      top : top + 'px'
    });
  },
  isDetachedFromDom: function () {
    return this._detached == true;
  },
  detachFromDom: function() {
    if (!this.isDetachedFromDom()) {
      this._detachedParent = this._domNode.parentNode;
      this._detachedNextSibling = this._domNode.next();
      
      this._domNode.remove();
      this._detached = true;
      this._detachedCount = 0;
    }

    this._detachedCount++;
  },
  reattachToDom: function() {
    if (this.isDetachedFromDom()) {
      this._detachedCount--;
      
      if (this._detachedCount == 0) {
        if (this._detachedNextSibling && this._detachedNextSibling.parentNode) {
          this._detachedParent.insertBefore(this._domNode, this._detachedNextSibling);
        } else {
          this._detachedParent.appendChild(this._domNode);
        }
        this._detachedParent = undefined;
        this._detachedNextSibling = undefined;
        this._detached = false;
      }
    }
  },   
  setBackgroundColor: function (backgroundColor) {
    this._domNode.setStyle({
      backgroundColor : backgroundColor
    });
  },  
  setHeight: function(height) {
    this._domNode.setStyle({
      height: height + 'px'
    });
  },
  getEducationTypes: function (educationTypes) {
    return this._educationTypes;
  },
  setEducationTypes: function (educationTypes) {
    this._educationTypes = educationTypes;
  },
  getEducationSubtypes: function () {
    return this._educationSubtypes;
  },
  setEducationSubtypes: function (educationSubtypes) {
    this._educationSubtypes = educationSubtypes;
  },
  _drawDates : function() {
    this._datesElement.update(getLocale().getDate(this.getStartDate().getTime(), false) + ' - ' + getLocale().getDate(this.getEndDate().getTime(), false));
  }
});

CoursePlannerFilter = Class.create({
  initialize: function() {
  },
  filter: function (course) {
    return false;
  }
});

CoursePlannerEducationSubtypeFilter = Class.create(CoursePlannerFilter, {
  initialize: function($super, educationSubtypes) {
    this._educationSubtypes = educationSubtypes;
  },
  filter: function ($super, course) {
    for (var i = 0, l = this._educationSubtypes.length; i < l; i++) {
      for (var j = 0, jl = course.getEducationSubtypes().length; j < jl; j++) {
        if (course.getEducationSubtypes()[j] == this._educationSubtypes[i]) {
          return false;
        }
      }
    }
    
    return true;
  }
});