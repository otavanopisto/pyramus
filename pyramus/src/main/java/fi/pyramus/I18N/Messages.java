package fi.pyramus.I18N;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import fi.pyramus.plugin.PluginDescriptor;
import fi.pyramus.plugin.PluginManager;

public class Messages {

  /** Returns the message repository singleton instance.
   * 
   * @return The message repository singleton instance.
   */
  public static Messages getInstance() {
    return instance;
  }
  
  private static Messages instance;
  
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
        ResourceBundle localeBundle = ResourceBundle.getBundle(bundleName, locale, PluginManager.getInstance().getPluginsClassLoader()); 
        if (localeBundle != null) {
          resourceBundle.addResourceBundle(localeBundle);
        }
      }
      
      bundles.put(locale, resourceBundle);
    }

    return bundles.get(locale);
  }
  
  private void loadBundleNames() {
    List<PluginDescriptor> plugins = PluginManager.getInstance().getPlugins();
    for (PluginDescriptor plugin : plugins) {
      if (!StringUtils.isBlank(plugin.getMessagesBundlePath())) {
        bundleNames.add(plugin.getMessagesBundlePath());
      }
    }

    bundleNames.add("fi.pyramus.I18N.pyramuslocale");
  }
  
  private List<String> bundleNames = new ArrayList<String>();
  private Map<Locale, ResourceBundle> bundles = new HashMap<Locale, ResourceBundle>();
  
  static {
    instance = new Messages();
    instance.loadBundleNames();
  }
}

