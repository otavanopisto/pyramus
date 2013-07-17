/*
 * Extensions to PrototypeJS library 
 * 
 * Antti Leppa
 * Foyt 
 * 
 * http://www.foyt.fi
 * 
 * Licensed under 
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 */

Element.addMethods({
    getMaxHeight: function (element) {
      var e = $(element);
      var pE = $(e.parentNode); 
      if (pE) {
        var childNodes = pE.childElements();
        var siblingsHeight = 0; 
        for (var i = 0; i < childNodes.length; i++) {
          if (childNodes[i] != element) {
            var rh = $(childNodes[i]).getReservedHeight();  
            siblingsHeight += rh;
          }
            
        }
        
        var pePaddings = pE.getHorizontalPaddings();
        var ePaddings = e.getHorizontalPaddings();
        var pEH = pE.getHeight();
        return (pEH - (pePaddings + ePaddings)) - siblingsHeight;
      }
    },
    getHorizontalPaddings: function(element){
      var e = $(element);
      return e.getStyleInPixels('padding-top') + e.getStyleInPixels('padding-bottom') + e.getStyleInPixels('border-top-width') + e.getStyleInPixels('border-bottom-width');
    },
    getHorizontalMargins: function(element){
      var e = $(element);
      
      return e.getStyleInPixels('margin-top') + e.getStyleInPixels('margin-bottom');
    },
    getVerticalPaddings: function(element){
      var e = $(element);
      return e.getStyleInPixels('padding-left') + e.getStyleInPixels('padding-right') + e.getStyleInPixels('border-left-width') + e.getStyleInPixels('border-right-width');
    },
    getVerticalMargins: function(element){
      var e = $(element);
      
      return e.getStyleInPixels('margin-left') + e.getStyleInPixels('margin-right');
    },
    getReservedHeight: function (element) {
      var e = $(element);
      return e.getHeight() + e.getHorizontalMargins();
    },
    getReservedWidth: function (element) {
      var e = $(element);
      return e.getWidth() + e.getVerticalMargins();
    },
    getStyleInPixels: function (element, property) {
      var e = $(element);
      var result = 0;
      var value = e.getStyle(property);
      
      if (value != undefined && value != null) {
        if (value.endsWith('px')) 
          result = new Number(value.substring(0, value.indexOf('px'))); 
        else {
          /* border width fix applied by Otavan Opisto 10.12.2008 */
          if (property.startsWith('border') && property.endsWith('width')) {
            var borderStyle = element.getStyle(property.substring(0, property.indexOf('width')) + 'style');
            if ((borderStyle == 'none')||(borderStyle == 'hidden')) 
              return 0;
            else {            
              switch (value) {
                case 'thin':
                  result = 2;
                break;
                case 'medium':
                  result = 4; 
                break;
                case 'thick':
                  result = 6;
                break;
              }
            } 
          } 
        }
      }
      
      return result;
    },
    /**
     * Repaints element. 
     * 
     * Method added by Otavan Opisto 10.12.2008
     */
    repaint: function (element) {
      var e = $(element);
       
      var parent = e.parentNode;
      var nextSibling = e.next();
      
      e.remove();
      
      if (nextSibling) {
        parent.insertBefore(e, nextSibling);
      }  else {
        parent.appendChild(e);
      }
    },
    getImageNaturalWidth: function (element) {
      /* TODO: Doesn't work on Opera, returns scaled width */
      // TODO: Safari ?
      if (element.naturalWidth)
        return element.naturalWidth;
      else 
        return element.width;
    },
    getImageNaturalHeight: function (element) {
      /* TODO: Doesn't work on Opera, returns scaled height */
      // TODO: Safari ?
      if (element.naturalHeight)
        return element.naturalHeight;
      else 
        return element.height;
    }
});

Object.extend(Prototype.BrowserFeatures, {
  _testBrowserSupport: function(mimeType, activeXClass) {
    var mimeTypes = navigator.mimeTypes; 
    if ((mimeTypes != null) && (mimeTypes.length > 0)) {
      // Firefox, Opera, Chrome, Safari
      for (var i = 0; i < mimeTypes.length; i++) {
        if (mimeTypes[i].type == mimeType)
          return true;        
        return false;
      }
    }
    else {
      try {
        if (new ActiveXObject(activeXClass))
          return true;
      } catch (e) {
        return false;
      }
    }
  },
  supportsPDF: function() {
    if (Prototype.Browser.WebKit) // TODO: What version of WebKit gained PDF native support?
      return true;
    else
      return this._testBrowserSupport("application/pdf", "AcroPDF.PDF");
  },
  supportsSVG: function() {
    /* TODO: Only Safari 3.0+ support SVG without plugin */
	if (Prototype.Browser.WebKit||Prototype.Browser.Gecko)
      return true;
    else
      return this._testBrowserSupport("image/svg-xml", "Adobe.SVGCtl");
  }
});

Object.extend(Event, {
  /**
   when wheel scrolled up: 
      Gecko, Opera, Konqueror: event.detail = -3
      Safari: event.wheelDelta = 120
      Chrome: event.wheelDelta = 360
  */
  wheelDelta: function (event) {
    if (navigator.userAgent.indexOf('Chrome') > 0) 
      return event.wheelDelta / 360;
    else    
      return ((Prototype.Browser.Gecko == true)||(Prototype.Browser.Opera == true))?(event.detail / 3) * -1: event.wheelDelta ? event.wheelDelta / 120 : (event.detail / 3) * -1;  
  }
});

Object.extend(window.location, {
  paramsHash: function () {
    if (!this._urlParamsHash) { 
      this._urlParamsHash = new Hash(); 
      var qps = this.search;                    
      if (qps.length > 1) {
        var params = qps.substring(1).split('&');
        while (params.length > 0) {
          var param = params.pop().split('=');
          if (param.length == 2) {
            var value = param.pop();
            var name = param.pop();
            this._urlParamsHash.set(name, value);
          } 
        }
      }
    }
    return this._urlParamsHash;   
  }
});

function resolveRelativePath(baseDir, path) {
  if ((path[0] == '/')||(path.indexOf("http://") == 0)||(path.indexOf("https://") == 0)) 
    return path;
  else {    
    if (baseDir[baseDir.length - 1] != '/')   
      baseDir = baseDir.substring(0, baseDir.lastIndexOf('/') + 1);

    if (path.indexOf("..") == 0) {
      var dpTokens = baseDir.split("/");
      var pTokens = path.split("/");

      if (dpTokens[dpTokens.length - 1] == '')
        dpTokens.splice(dpTokens.length - 1, 1);

      if (pTokens[pTokens.length - 1] == '')
        pTokens.splice(pTokens.length - 1, 1);

      while (pTokens[0] == '..') {
        pTokens.splice(0, 1);
        dpTokens.splice(dpTokens.length - 1, 1);
      }

      var result = "";
      for (var i = 0; i < dpTokens.length; i++)
        result += dpTokens[i] + '/';
      for (var i = 0; i < pTokens.length; i++) {
        result += pTokens[i]; 
        if ((path[path.length -1] == '/')||(i < (pTokens.length - 1)))
          result += '/';
      }

      return result;
    } else
      return baseDir + path;
  }
}
