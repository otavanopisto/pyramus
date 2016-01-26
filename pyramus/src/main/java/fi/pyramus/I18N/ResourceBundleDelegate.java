package fi.pyramus.I18N;

import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

public class ResourceBundleDelegate extends ResourceBundle {

  public ResourceBundleDelegate(Locale locale) {
    super();
    this.locale = locale;
  }
  
  @Override
  protected Object handleGetObject(String key) {
    for (ResourceBundle bundle : bundles) {
      if (bundle.containsKey(key))
        return bundle.getObject(key);
    }
    return null;
  }

  @Override
  public Enumeration<String> getKeys() {
    return mergedKeys.elements();
  }
  
  @Override
  public Locale getLocale() {
    return locale;
  }
  
  public void addResourceBundle(ResourceBundle resourceBundle) {
    bundles.add(resourceBundle);
    mergedKeys.removeAll(resourceBundle.keySet());
    mergedKeys.addAll(resourceBundle.keySet());
  } 

  private Set<ResourceBundle> bundles = new LinkedHashSet<ResourceBundle>();
  private Vector<String> mergedKeys = new Vector<String>();
  private Locale locale;
}
