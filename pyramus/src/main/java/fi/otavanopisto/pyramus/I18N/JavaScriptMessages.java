package fi.otavanopisto.pyramus.I18N;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/** An access singleton for localized JavaScript strings.
 * The strings are stored as key/value pairs in
 * <code>src/main/resources/fi/pyramus/I18N/javascriptlocale_*.properties</code>
 * 
 * 
 *
 */
public class JavaScriptMessages {

  /** Returns the instance of this singleton class.
   * 
   * @return the instance of this singleton class.
   */
  public static JavaScriptMessages getInstance() {
    return instance;
  }
  
  private static JavaScriptMessages instance;
  
  /** Returns the string whose key is <code>key</code>, localized to <code>locale</code>.
   * 
   * @param locale The target locale.
   * @param key The key of the string to be localized.
   * @return The string whose key is <code>key</code>, localized to <code>locale</code>.
   */
  public String getText(Locale locale, String key) {
    return getResourceBundle(locale).getString(key);
  }
  
  /** Returns the format string whose key is <code>key</code>, localized to <code>locale</code>,
   * formatted with <code>params</code>.
   * 
   * @see MessageFormat
   * @param locale The target locale.
   * @param key The key of the string to be localized.
   * @param params The formatting parameters, to be passed to <code>MessageFormat</code>.
   * @return The string whose key is <code>key</code>, localized to <code>locale</code>.
   */
  public String getText(Locale locale, String key, Object[] params) {
    return MessageFormat.format(getText(locale, key), params);
  }
  
  /** Returns the resource bundle that contains the strings of the specified locale.
   * 
   * @param locale The locale whose bundle is returned.
   * @return The bundle that contains the strings of the specified locale.
   */
  public ResourceBundle getResourceBundle(Locale locale) {
    if (!bundles.containsKey(locale)) {
      ResourceBundleDelegate resourceBundle = new ResourceBundleDelegate(locale);
      
      for (String bundleName : bundleNames) {
        ResourceBundle localeBundle = ResourceBundle.getBundle(bundleName, locale); 
        if (localeBundle != null) {
          resourceBundle.addResourceBundle(localeBundle);
        }
      }
      
      bundles.put(locale, resourceBundle);
    }

    return bundles.get(locale);
  }
  
  private void loadBundleNames() {
    bundleNames.add("fi.otavanopisto.pyramus.I18N.javascriptlocale");
    // TODO: how about plugins
  }
  
  private List<String> bundleNames = new ArrayList<>();
  private Map<Locale, ResourceBundle> bundles = new HashMap<>();
  
  static {
    instance = new JavaScriptMessages();
    instance.loadBundleNames();
  }
}

