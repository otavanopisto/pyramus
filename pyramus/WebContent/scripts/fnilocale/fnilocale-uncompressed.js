if (!window.fni) {
  /**
   * @namespace fni package
   */
  var fni = {};
};if (!fni.locale) {
  /**
   * @namespace fni.locale package
   */
  fni.locale = {};
};if (!fni.locale.dateformat) {
  /**
   * @namespace fni.locale.dateformat package
   */
  fni.locale.dateformat = {};
};/*
 * FNITokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fni.locale.dateformat.FNITokenFormatter = Class.create(
  /** @lends fni.locale.dateformat.FNITokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Base class for all token formatters in FNIDateFormat
   * @constructs
   */
  initialize : function() {
  },
  /**
   * Returns the token this formatter handles
   * 
   * @returns token this formatter handles
   */
  getToken : function() {
  },
  /**
   * Returns formatted string
   * 
   * @param date
   *          date object
   * @returns formatted string
   */
  format : function(date) {
  }
});/*
 * fni.locale.dateformat.FNITokenFormatterVault
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

/**
 * Singleton class for holding token formatters
 * 
 * @constructor 
 */
fni.locale.dateformat.FNITokenFormatterVault = {
  /**
   * Registers new formatter
   * 
   * @param formatter {fni.locale.dateformat.FNITokenFormatter} formatter to be registered
   */
  register: function (formatter) {
    this._formatters.set(formatter.getToken(), formatter);
  },
  /**
   * Return all registered formatters
   * 
   * @type Hash
   * @returns hash map of registered formatters
   */
  getFormatters: function () {
    return this._formatters;
  },
  _formatters: new Hash()
};/*
 * FNIPaddedSecondTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fni.locale.dateformat.FNIPaddedSecondTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIPaddedSecondTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'ss' token formatter
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   */
  getToken: function () {
    return 'ss';
  },
  format: function (date) {
    return date.getSeconds().toPaddedString(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIPaddedSecondTokenFormatter());/*
 * FNIMonthTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIMonthTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIMonthTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'M' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken: function () {
    return 'M';
  },
  format: function (date) {
    return date.getMonth() + 1;
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIMonthTokenFormatter());/*
 * FNIMinuteTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIMinuteTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIMinuteTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'm' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken: function () {
    return 'm';
  },
  format: function (date) {
    return date.getMinutes();
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIMinuteTokenFormatter());/*
 * FNIPaddedMinuteTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIPaddedMinuteTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIPaddedMinuteTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'mm' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken: function () {
    return 'mm';
  },
  format: function (date) {
    return date.getMinutes().toPaddedString(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIPaddedMinuteTokenFormatter());/*
 * FNIHour24TokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIPaddedHour24TokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIPaddedHour24TokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'kk' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  initialize: function ($super) {
    $super();
  },
  getToken: function () {
    return 'kk';
  },
  format: function (date) {
    return date.getHours().toPaddedString(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIPaddedHour24TokenFormatter());/*
 * FNIPaddedMonthTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIPaddedMonthTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIPaddedMonthTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'MM' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken: function () {
    return 'MM';
  },
  format: function (date) {
    return (date.getMonth() + 1).toPaddedString(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIPaddedMonthTokenFormatter());/*
 * FNIHour12TokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fni.locale.dateformat.FNIHour12TokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIHour12TokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'h' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  initialize: function ($super) {
    $super();
  },
  getToken: function () {
    return 'h';
  },
  format: function (date) {
    return ((date.getHours() + 11) % 12) + 1;
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIHour12TokenFormatter());/*
 * FNIShortYearTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIShortYearTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIShortYearTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'yy' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken : function() {
    return 'yy';
  },
  format : function(date) {
    return new String(date.getFullYear()).substring(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIShortYearTokenFormatter());/*
 * FNIPaddedMillisecondTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIPaddedMillisecondTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIPaddedMillisecondTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'SSS' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken: function () {
    return 'SSS';
  },
  format: function (date) {
    return date.getMilliseconds().toPaddedString(3);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIPaddedMillisecondTokenFormatter());/*
 * FNIAmPmTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fni.locale.dateformat.FNIAmPmTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
    /** @lends fni.locale.dateformat.FNIAmPmTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'a' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  initialize: function ($super) {
    $super();
  },
  getToken: function () {
    return 'a';
  },
  format: function (date) {
    return (date.getHours() - 12) > -1 ? "p.m." : "a.m.";
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIAmPmTokenFormatter());/*
 * FNIDateTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fni.locale.dateformat.FNIDateTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIDateTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'd' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  initialize: function ($super) {
    $super();
  },
  getToken: function () {
    return 'd';
  },
  format: function (date) {
    return date.getDate();
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIDateTokenFormatter());/*
 * FNISecondTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNISecondTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNISecondTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 's' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken: function () {
    return 's';
  },
  format: function (date) {
    return date.getSeconds();
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNISecondTokenFormatter());/*
 * FNILocalizedLongDayTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNILocalizedLongDayTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNILocalizedLongDayTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'E' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  initialize: function ($super) {
    $super();
  },
  getToken: function () {
    return 'EE';
  },
  format: function (date) {
    return '__EL' + date.getDay().toPaddedString(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNILocalizedLongDayTokenFormatter());/*
 * FNIPaddedHour24TokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIHour24TokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIHour24TokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'k' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken: function () {
    return 'k';
  },
  format: function (date) {
    return date.getHours();
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIHour24TokenFormatter());/*
 * FNILongYearTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fni.locale.dateformat.FNILongYearTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNILongYearTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'yyyy' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  initialize: function ($super) {
    $super();
  },
  getToken: function () {
    return 'yyyy';
  },
  format: function (date) {
    return date.getFullYear();
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNILongYearTokenFormatter());/*
 * Millisecond
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fni.locale.dateformat.FNIMillisecondTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIMillisecondTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'S' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  initialize: function ($super) {
    $super();
  },
  getToken: function () {
    return 'S';
  },
  format: function (date) {
    return date.getMilliseconds();
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIMillisecondTokenFormatter());/*
 * FNIPaddedHour12TokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIPaddedHour12TokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIPaddedHour12TokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'hh' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken: function () {
    return 'hh';
  },
  format: function (date) {
    return (((date.getHours() + 11) % 12) + 1).toPaddedString(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIPaddedHour12TokenFormatter());/*
 * FNILocalizedMonthTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */
fni.locale.dateformat.FNILocalizedMonthTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNILocalizedMonthTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'MMMM' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  initialize: function ($super) {
    $super();
  },
  getToken: function () {
    return 'MMMM';
  },
  format: function (date) {
    return '__MM' + (date.getMonth() + 1).toPaddedString(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNILocalizedMonthTokenFormatter());/*
 * FNIPaddedDateTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIPaddedDateTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNIPaddedDateTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'dd' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  getToken: function () {
    return 'dd';
  },
  format: function (date) {
    return date.getDate().toPaddedString(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNIPaddedDateTokenFormatter());/*
 * FNILocalizedShortDayTokenFormatter
 *  
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNILocalizedShortDayTokenFormatter = Class.create(fni.locale.dateformat.FNITokenFormatter, 
  /** @lends fni.locale.dateformat.FNILocalizedShortDayTokenFormatter# */ 
  {
  /**
   * Class constructor
   * @class Implements 'EE' token formatter 
   * @extends fni.locale.dateformat.FNITokenFormatter
   * @constructs
   * @param $super super class 
   */
  initialize: function ($super) {
    $super();
  },
  getToken: function () {
    return 'E';
  },
  format: function (date) {
    return '__EE' + date.getDay().toPaddedString(2);
  }
});

fni.locale.dateformat.FNITokenFormatterVault.register(new fni.locale.dateformat.FNILocalizedShortDayTokenFormatter());/*
 * FNIDateFormat - Java style date formatter
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.dateformat.FNIDateFormat = Class.create(
  /** @lends fni.locale.dateformat.FNIDateFormat# */ 
  {
  /**
   * Constructor for FNIDateFormat class 
   * @class Class that handles localization
   * @param pattern format is specified by pattern of string. Following patterns may be used to format dates and times:
   * <table>
   *   <tr><td><b>Symbol</b></td><td><b>Component</b></td><td><b>Details</b></td></tr>
   *   <tr><td>y</td><td>year</td><td>yy = 09, yyyy = 2009</td></tr>
   *   <tr><td>M</td><td>month</td><td>M = 1, MM = 01, MMMM = localized month name</td></tr>
   *   <tr><td>d</td><td>day in month</td><td>d = 3, DD = 03</td></tr>
   *   <tr><td>E</td><td>day in week</td><td>E = localized day (short), EE = localized day (long)</td></tr> 
   *   <tr><td>a</td><td>a.m./p.m.</td><td></td></tr>
   *   <tr><td>k</td><td>Hour in day</td><td>k = 1, kk = 01</td></tr>
   *   <tr><td>h</td><td>Hour in day am/pm</td><td>h = 1, hh = 01</td></tr>
   *   <tr><td>m</td><td>Minute in hour</td><td>m = 1, mm = 01</td></tr>
   *   <tr><td>s</td><td>Second in minute</td><td>s = 1, ss = 01</td></tr>
   *   <tr><td>S</td><td>Millisecond</td><td>S = 5, SSS = 005</td></tr>
   * </table>
   * @constructs
   */
  initialize : function (pattern) {
    this._pattern = pattern;
    this._tokenFormatters = new Array();
    
    this._localizedMonth = false;
    this._localizedDayShort = false;
    this._localizedDayLong = false;
    
    var parsed = pattern;
    var formatters = fni.locale.dateformat.FNITokenFormatterVault.getFormatters();
    var keys = formatters.keys().sortBy(function(s) { return -s.length; });
    
    var cIndex = 0;
    
    while (cIndex < pattern.length) {
      var matched = false;
      
      if (pattern[cIndex] == '"') {
        cIndex++;
        while (pattern[cIndex] != '"') {
          this._tokenFormatters.push(pattern[cIndex]);
          cIndex++;
        }
        cIndex++;
      }
      
      for (var i = 0, len = keys.length; i < len; i++) {
        var formatter = formatters.get(keys[i]);
        var token = formatter.getToken();
        if (parsed.indexOf(token) == cIndex) {
          this._tokenFormatters.push(formatter);
          cIndex += token.length;
          matched = true;
          
          switch (token) {
            case 'E':
              this._localizedDayShort = true;
            break;
            case 'EE':
              this._localizedDayLong = true;
            break;
            case 'MMMM':
              this._localizedMonth = true;
            break;
          }  
          
          break;
        } 
      }
      
      if (!matched) {
        this._tokenFormatters.push(pattern[cIndex]);
        cIndex += 1;
      }
    }
  },
  /**
   * Formats date object into string
   * 
   * @param locale used locale
   * @param date date object
   * @returns formatted string
   */
  format: function (locale, date) {
    var result = '';
    
    for (var i = 0, len = this._tokenFormatters.length; i < len; i++) {
      var token = this._tokenFormatters[i];
      if (Object.isString(token))
        result += token;
      else
        result += token.format(date);
    }
    
    if (this._localizedDayLong) {
      var i = result.length;
      while ((i = result.lastIndexOf('__EL', i - 1)) >= 0) {
        var n = result.substring(i + 4, i + 6);
        result = result.substring(0, i) + locale.getText('weekdayLong_' + n) + result.substring(i + 6);
      } 
    }
    
    if (this._localizedDayShort) {
      var i = result.length;
      while ((i = result.lastIndexOf('__EE', i - 1)) >= 0) {
        var n = result.substring(i + 4, i + 6);
        result = result.substring(0, i) + locale.getText('weekdayShort_' + n) + result.substring(i + 6);
      } 
    }
    
    if (this._localizedMonth) {
      var i = result.length;
      while ((i = result.lastIndexOf('__MM', i - 1)) >= 0) {
        var n = result.substring(i + 4, i + 6);
        result = result.substring(0, i) + locale.getText('month_' + n) + result.substring(i + 6);
      } 
    }
    
    return result;
  },
  /**
   * Returns a pattern string describing this date format. 
   * 
   * @returns a pattern string describing this date format. 
   */
  getPattern: function () {
    return this._pattern;
  }
});/*
 * FNILocale - Localization library for JavaScript 
 * Copyright (C) 2008 - 2009 Antti Leppä / Foyt
 * http://www.foyt.fi
 * 
 * License: 
 * 
 * Licensed under GNU Lesser General Public License Version 2.1 or later (the "LGPL") 
 * http://www.gnu.org/licenses/lgpl.html
 */

fni.locale.FNILocale = Class.create(
  /** @lends fni.locale.FNILocale# */ 
  {
  /**
   * Class constructor
   * @class Class that handles localization
   * @constructs
   */
  initialize : function() {
    this._locales = new Object();
    this._settings = new Object();
    this._loadedLocales = new Hash();
    this._defaultLocale = 'en_US';
  },
  /**
   * Returns timestamp as string formatted by pattern specified in "dateFormatLong" or "dateFormatShort" setting. 
   * 
   * @param {Number} timestamp timestamp
   * @param {Boolean} longFormat if true "dateFormatLong" setting should be used otherwise "dateFormatShort" is used
   * 
   * @returns {String} formatted date string
   */
  getDate: function (timestamp, longFormat) {
    var d = new Date(timestamp);
    return this.getDateFormat(longFormat).format(this, d);
  },  
  /**
   * Returns timestamp as string formatted by pattern specified in "timeFormat" setting. 
   * 
   * @param {Number} timestamp timestamp
   * 
   * @returns {String} formatted time string
   */
  getTime: function (timestamp) {
    var d = new Date(timestamp);
    return this.getTimeFormat().format(this, d);
  },
  /**
   * Returns timestamp as string formatted by pattern specified in "timeFormat" setting. 
   * 
   * @param {Number} timestamp timestamp
   * 
   * @returns {String} formatted time string
   */
  getDateTime: function (timestamp, longFormat) {    
    return this.getDate(timestamp, longFormat) + ' ' + this.getTime(timestamp);
  },
  /**
   * Returns localized text for key in current locale
   * 
   * @param {String} key 
   * @param {String} param1&#44;&#32;param2&#44;&#32;param3&#46;&#46;&#46; locale parameters  
   * 
   * @returns {String} localized string
   */
  getText: function() {
    var params = new Array();
    params.push(this.getLocale());
    var args = $A(arguments);
    for (var i = 0, l = args.length; i < l; i++) {
      params.push(args[i]); 
    }
    
    return this.getLocalizedText.apply(this, params);
  },
  /**
   * Returns localized text for key in specified locale
   * 
   * @param {Locale} locale locale in where text is returned  
   * @param {String} key 
   * @param {String} param1&#44;&#32;param2&#44;&#32;param3&#46;&#46;&#46; locale parameters  
   * 
   * @returns {String} localized string
   */
  getLocalizedText: function () {
    var args = $A(arguments);
    var locale = args[0];
    var text = args[1]; 
    
    var localeVault = this._getLocaleVault(locale);

    if (localeVault) {
      var localizedText = localeVault[text];
      if (localizedText == undefined) {
        localizedText = '[[' + text + ']]';
      } else {
        for (var i = 2; i < args.length; i++) {
          localizedText = localizedText.replace('\{' + (i - 2) + '\}', arguments[i]);
        }
      }
        
      return localizedText;  
    } else {
      return '[[' + text + ']]';
    }
  },
  /**
   * Sets current language (e.g. en in en_US)
   * 
   * @param language {String} language 
   */
  setLanguage: function (language) {
    this._dateFormatShort = undefined;
    this._dateFormatLong = undefined;
    this._timeFormat = undefined;

    this._currentLanguage = language;
  },
  /**
   * Sets current country (e.g. US in en_US)
   * 
   * @param language {String} language 
   */
  setCountry: function (country) {
    this._dateFormatShort = undefined;
    this._dateFormatLong = undefined;
    this._timeFormat = undefined;
    
    this._currentCountry = country;
  },
  /**
   * Returns current country code
   * 
   * @returns {String} current country code
   */
  getCountry: function () {
    return this._currentCountry;
  },
  /**
   * Sets current locale
   * 
   * @param locale {Locale}
   */
  setLocale: function (locale) {
    var splitted = locale.split('_');
    if (splitted.length == 2) {
      this.setLanguage(splitted[0]);
      this.setCountry(splitted[1]);
    } else {
      this.setLanguage(splitted[0]);
      this.setCountry(null);
    }
  },
  /**
   * Returns current language
   * 
   * @returns {String} current language
   */
  getLanguage: function () {
    return this._currentLanguage;
  },
  /**
   * Returns current locale
   * 
   * @returns {Locale} current locale
   */
  getLocale: function () {
    return this.getLanguage() + '_' + this.getCountry();
  },
  /**
   * Returns current date format.  
   * 
   * @param longFormat {Boolean} if true "dateFormatLong" setting should be used otherwise "dateFormatShort" is used 
   * 
   * @returns Current date format
   */
  getDateFormat: function (longFormat) {
    if (longFormat == true) {
      if (!this._dateFormatLong) {
        var dateFormatString = this.getSetting("dateFormatLong");
        if (!dateFormatString||dateFormatString.blank()||(dateFormatString == '[[dateFormatLong]]'))
          dateFormatString = 'dd.MMMM.yyyy';
        
        this._dateFormatLong = new fni.locale.dateformat.FNIDateFormat(dateFormatString);
      }
      
      return this._dateFormatLong;
    } else {
      if (!this._dateFormatShort) {
        var dateFormatString = this.getSetting("dateFormatShort");
        if (!dateFormatString||dateFormatString.blank()||(dateFormatString == '[[dateFormatShort]]'))
          dateFormatString = 'dd.MM.yyyy';
        
        this._dateFormatShort = new fni.locale.dateformat.FNIDateFormat(dateFormatString);
      }
      
      return this._dateFormatShort;
    }
  },
  /**
   * Returns current time format.  
   * 
   * @returns Current time format
   */
  getTimeFormat: function () {
    if (!this._timeFormat) {
      var timeFormatString = this.getSetting("timeFormat");
      if (!timeFormatString||timeFormatString.blank()||(timeFormatString == '[[timeFormat]]'))
        timeFormatString = 'kk:mm';
      
      this._timeFormat = new  fni.locale.dateformat.FNIDateFormat(timeFormatString);
    }
        
    return this._timeFormat;
  },
  /**
   * Returns loaded locales
   * 
   * @returns {Locale[]} loaded locales
   */
  getLocales: function () {
    return this._loadedLocales.keys();
  },
  /**
   * Returns loaded languages
   * 
   * @returns {String[]} loaded languages
   */
  getLanguages: function () {
    var result = new Array();
    var locales = this.getLocales();
    for (var i = 0, l = locales.length; i < l; i++) {
      result.push(locales[i].split('_')[0]);
    }
    
    return result;
  },
  /**
   * Returns setting value for current locale
   * 
   * @param name setting name
   * @returns {String} setting
   */
  getSetting: function (name) {
    var settingsVault = this._getSettingVault(this.getLocale());
    if (settingsVault)
      return settingsVault[name];
    else 
      return "[[" + name + "]]";
  },
  /**
   * Sets setting for current locale
   * 
   * @param name {String} setting name
   * @param value {String} setting value 
   */
  setSetting: function (name, value) {
    var settingsVault = this._getSettingVault(this.getLocale());
    if (settingsVault)
      settingsVault[name] = value;
  },
  /**
   * Loads locales from JSON.
   * 
   * JSON format should be: 
   * 
   * <pre>{ 
   *   localeStrings: {
   *     key: value
   *   },
   *   settings: {
   *     key: value
   *   }
   * }</pre> 
   * 
   * @param locale locale to be loaded
   * @param file url to JSON file
   */
  loadLocale: function (locale, file) {
    var _this = this;
    new Ajax.Request(file, {
      onSuccess: function (transport) {
        try {
          var json = transport.responseText.evalJSON();
          
          for (var key in json.localeStrings) {
            _this._setLocaleString(locale, key, json.localeStrings[key]);
          }
          
          for (var key in json.settings) {
            _this._setLocaleSetting(locale, key, json.settings[key]);
          }
          
          _this._loadedLocales.set(locale, true);
        } catch (e) {
          alert("Locale " + file + " loading failed:" + e);
        }
      },
      asynchronous: false
    });
  },
  /**
   * @private
   */ 
  _getLocaleVault: function (locale) {
    var localeVault = this._locales[locale];
    if (!localeVault) {
      var s = locale.split("_");
      if (s.length == 2)
        localeVault = this._locales[s[0]];
    }
    
    if (!localeVault)
      localeVault = this._locales[this._defaultLocale];
    
    return localeVault;
  },
  /**
   * @private
   */
  _getSettingVault: function (locale) {
    var settingsVault = this._settings[locale];
    if (!settingsVault) {
      var s = locale.split("_");
      if (s.length == 2)
        settingsVault = this._settings[s[0]];
    }
    
    if (!settingsVault)
      settingsVault = this._settings[this._defaultLocale];
    
    return settingsVault;
  },
  /**
   * @private
   */
  _setLocaleString: function (locale, key, string) {
    if (!this._locales[locale])
      this._locales[locale] = new Object();
    
    var s = locale.split('_');
    
    if (s.length == 2) {
      var lang = s[0];
      if (!this._locales[lang])
        this._locales[lang] = new Object();
      
      this._locales[locale][key] = string;
      this._locales[lang][key] = string;      
    } else {
      this._locales[locale][key] = string;
    }
  },
  /**
   * @private
   */
  _setLocaleSetting: function (locale, key, setting) {
    if (!this._settings[locale])
      this._settings[locale] = new Object();
    
    var s = locale.split('_');
    if (s.lenght == 2) {
      if (!this._settings[lang])
        this._settings[lang] = new Object();
      
      this._settings[locale][key] = setting;
      this._settings[lang][key] = setting;
    } else {
      this._settings[locale][key] = setting;
    }
  }
});

/**
 * TODO
 */
Object.extend(fni.locale.FNILocale.prototype, fni.events.FNIEventSupport);