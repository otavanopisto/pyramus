package fi.otavanopisto.pyramus.json.locale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.JavaScriptMessages;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class GetJavaScriptLocaleJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    Map<String, String> localeStrings = new HashMap<String, String>();
    
    ResourceBundle resourceBundle = JavaScriptMessages.getInstance().getResourceBundle(requestContext.getRequest().getLocale());
    Enumeration<String> keys = resourceBundle.getKeys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      String value = resourceBundle.getString(key);
      localeStrings.put(key, value.trim());
    }

    Map<String, String> settingStrings = new HashMap<String, String>();
    DateFormat shortDate = SimpleDateFormat.getDateInstance(DateFormat.SHORT, requestContext.getRequest().getLocale());
    DateFormat longDate = SimpleDateFormat.getDateInstance(DateFormat.LONG, requestContext.getRequest().getLocale());
    DateFormat time = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, requestContext.getRequest().getLocale());
    
    if (shortDate instanceof SimpleDateFormat)
      settingStrings.put("dateFormatShort", ((SimpleDateFormat) shortDate).toPattern());
    if (longDate instanceof SimpleDateFormat)
      settingStrings.put("dateFormatLong", ((SimpleDateFormat) longDate).toPattern());
    if (time instanceof SimpleDateFormat)
      settingStrings.put("timeFormat", ((SimpleDateFormat) time).toPattern());

    requestContext.addResponseParameter("localeStrings", localeStrings);
    requestContext.addResponseParameter("settings", settingStrings);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }
}

