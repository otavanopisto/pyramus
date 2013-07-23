/** The extension methods for <code>document</code> DOM element.
 *  @name document
 *  @class
 */


Object.extend(document,
  /** @lends document.prototype */
  {
  /** Returns a the value of a cookie whose key is <code>key</code>
   * 
   * @param key The key of the cookie to retrieve.
   * @returns The value of the cookie, or <code>null</code> if there is none.
   */
  getCookie: function (key) {
    var cookie = document.cookie.match(new RegExp('(^|;)\\s*' + escape(key) + '=([^;\\s]*)'));
    return (cookie ? unescape(cookie[2]) : null);
  },
  /** Sets a cookie whose key is <code>key</code>.
   * 
   * @param key The key of the cookie
   * @param value The value of the cookie
   * @param path The path of the cookie
   * @param expire The time for the cookie to expire
   */
  setCookie: function (key, value, path, expire) {
    var cookieStr = key + '=' + escape(value);
    
    if (expire) {
      var date = new Date();
      date.setTime(date.getTime() + expire);
      cookieStr += "; expires=" + date.toGMTString();
    }
    
    if (path) {
      cookieStr += '; path=' + escape(path);
    }
    
    document.cookie = cookieStr;
  }
});