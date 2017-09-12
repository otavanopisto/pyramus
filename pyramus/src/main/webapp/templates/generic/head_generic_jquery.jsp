<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.Map"%>
<%
  @SuppressWarnings("unchecked")
  Map<String, String> jsData = (Map<String, String>) request.getAttribute("jsData");
  if (jsData != null) {
    out.append("<script type=\"text/javascript\">");
    out.append("var JSDATA={");
    
    Iterator<String> keys = jsData.keySet().iterator();
    while (keys.hasNext()) {
      String key = keys.next();
      String value = jsData.get(key);
      out.append("'");
      out.append(StringEscapeUtils.escapeJavaScript(key));
      out.append("':'");
      out.append(StringEscapeUtils.escapeJavaScript(value));
      out.append("'");
      if (keys.hasNext())
        out.append(',');
    }
    
    out.append("};</script>");
  }
%>

<!-- Global javascript variables -->

<script type="text/javascript">
  var GLOBAL_contextPath = '${pageContext.request.contextPath}';

  function setLocale(locale) {
    var date = new Date();
    date.setTime(date.getTime() + (3650*24*60*60*1000));
    var expires = "; expires=" + date.toGMTString();
    document.cookie = "pyramusLocale=" + locale + expires + "; path=/";
    window.location.reload();
  }

  function redirectTo(url) {
    if (url.indexOf("#") > 0) {
      // Url contains an anchor
      var splittedOld = window.location.href.split('#');
      var splittedNew = url.split('#');

      var oldHref = splittedOld[0];
      var newHref = splittedNew[0];
      
      if (oldHref === newHref) {
        var oldAnchor = splittedOld[1];
        var newAnchor = splittedNew[1];
        // We are tring to redirect to same url where we came from 
        if (newAnchor === oldAnchor) {
          // And same anchor that we came from
          location.reload();
        } else {
          location.hash = '#' + newAnchor;
          location.reload();
        }
      } else {
        // Url does not point to same url that where we came from so we just redirect it
        location.assign(url);
      }
    } else {
      // Url does not contain anchor reference, so we just do the redirect 
      location.assign(url);
    }
  }
  
  function jsonEscapeHTML(v) {
    return v ? v.escapeHTML() : "";
  }
  
</script>

<link href="${pageContext.request.contextPath}/css/theme.css" rel="stylesheet">
<script defer="defer" type="text/javascript" src="//code.jquery.com/jquery-1.12.4.min.js"></script>
<script defer="defer" type="text/javascript" src="//code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>