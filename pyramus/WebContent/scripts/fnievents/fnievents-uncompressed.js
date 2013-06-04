if (!window.fni) {
  /**
   * @namespace fni package
   */
  var fni = {};
};if (!fni.events) {
  /**
   * @namespace fni.events package
   */
  fni.events = {};
};/*
 * FNIEventSupport - Event handling routines 
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
/**
 * Set of methods that can be used to provide event handling support for classes.
 * 
 * Event support is applied to class by following command: 
 * <pre>Object.extend(myclass.prototype, fni.events.FNIEventSupport);</pre>
 * 
 * After that extended class has addListener, removeListener and fire methods as non-static members
 * just like they would be if they were just normal class members 
 * 
 * @class fni.events.FNIEventSupport 
 */
fni.events.FNIEventSupport = {
  /**
   * Adds event listener<br/><br/>
   * <b>Example:</b> 
   * @example myobject.addListener("load", this, this._onMyObjectLoad);
   * @param {String} eventName event name which is been listened
   * @param {Object} invoking Object
   * @param {Function} function to be launched on event
   * @param {Number} priority, defaults to 100
   */
  addListener : function(eventName) {
    var args = $A(arguments);

    var object = null;
    var method = null;
    var priority = 100;

    if (args.length == 2) {
      object = this;
      method = args[1];
    } else if (args.length == 3) {
      object = args[1];
      method = args[2];
    } else if (args.length == 4) {
      object = args[1];
      method = args[2];
      priority = args[3];
    }

    var l = new Object();
    l.eventName = eventName;
    l.method = method;
    l.object = object;
    l.priority = priority;
    
    if (!this._listeners)
      this._listeners = new Array();
    
    this._listeners.push(l);

    this._listeners = this._listeners.sortBy(function(l) {
      return -l.priority;
    });
  },
  /**
   * Removes event listener<br/><br/>
   * <b>Example:</b> 
   * @example myobject.removeListener("load", this);
   * @param {String} eventName
   * @param {Object} invoking Object
   */
  removeListener : function(eventName, object) {
    if (this._listeners) {
      for (var i = 0; i < this._listeners.length; i++) {
        var listener = this._listeners[i];
        if ((listener.eventName == eventName) && (listener.object === object))
          this._listeners.splice(i, 1);
      }
    }
  },
  /**
   * Removes all listeners
   */
  removeAllListeners : function() {
    this._listeners.clear();
  },
  /**
   * Fires an event<br/><br/>
   * <b>Example:</b> 
   * @example this.fire("load", {status: 0});
   * @param {Object} eventName event name
   * @param {Object} event event object
   */
  fire : function(eventName, event) {
    var result = true;
    
    var e = Object.extend(event||{}, {
      stop: function () {
        this.stopped = true;
      },
      stopped: false
    });
    
    if (this._listeners) {
      var listenerLength = this._listeners.length;
      for (var i = listenerLength; (i >= 0) && result; i--) {
        if (this._listeners != null) {
          var listener = this._listeners[i];
          if (listener && (listener != null)) {
            if (listener.eventName == eventName) {
              if (Object.isFunction(listener.method)) {
                listener.method.call(listener.object, e);
                if (e.stopped == true) {
                  result = false;
                } 
              } else {
                if ((typeof console) != 'undefined') {
                  console.log("Warning: listener method does not exist for event '" + eventName + "'");
                }
              }
            }
          }
        }
      }
    };
    
    return result;
  }
};