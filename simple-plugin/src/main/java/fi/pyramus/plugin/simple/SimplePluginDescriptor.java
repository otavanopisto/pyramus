package fi.pyramus.plugin.simple;

import java.util.HashMap;
import java.util.Map;

import fi.pyramus.plugin.PluginDescriptor;
import fi.pyramus.plugin.simple.auth.SimpleAuthenticationProvider;
import fi.pyramus.plugin.simple.hooks.EditCourseSimpleTabHook;
import fi.pyramus.plugin.simple.hooks.EditCourseSimpleTabLabelHook;
import fi.pyramus.plugin.simple.views.LoggedUserInfoViewController;

/** The descriptor that exposes the plugin's functionality to host program.
 *
 */
public class SimplePluginDescriptor implements PluginDescriptor {
  
  /** Returns the binary request controllers provided by this plugin.
   * 
   * @return The binary request controllers provided by this plugin.
   */
  public Map<String, Class<?>> getBinaryRequestControllers() {
    return null;
  }
  
  /** Returns the JSON request controllers provided by this plugin.
   * 
   * @return The JSON request controllers provided by this plugin.
   */
  public Map<String, Class<?>> getJSONRequestControllers() {
    return null;
  }
  
  /** Returns the name of this plugin.
   * 
   */
  public String getName() {
    return "simple";
  }
  
  
  /** Returns the page hook controllers provided by this plugin.
   * 
   * @return The page hook controllers provided by this plugin.
   */
  public Map<String, Class<?>> getPageHookControllers() {
    Map<String, Class<?>> hookControllers = new HashMap<String, Class<?>>();
    
    hookControllers.put("students.editStudent.tabs", EditCourseSimpleTabHook.class);
    hookControllers.put("students.editStudent.tabLabels", EditCourseSimpleTabLabelHook.class);

    return hookControllers;
  }
  
  /** Returns the page request controllers provided by this plugin.
   * 
   * @return The page request controllers provided by this plugin.
   */
  public Map<String, Class<?>> getPageRequestControllers() {
    Map<String, Class<?>> viewControllers = new HashMap<String, Class<?>>();
    
    viewControllers.put("users/loggeduserinfo", LoggedUserInfoViewController.class);
    
    return viewControllers;
  }
  
  /** Returns the authentication providers implemented by this plugin.
   * 
   * @return The authentication providers implemented by this plugin
   */
  public Map<String, Class<?>> getAuthenticationProviders() {
    Map<String, Class<?>> authenticationProviders = new HashMap<String, Class<?>>();
    
    authenticationProviders.put("simple", SimpleAuthenticationProvider.class);
    
    return authenticationProviders;
  }
  
  /** Returns the base path for the localization strings for this plugin.
   * 
   * @return The base path for the localization strings for this plugin.
   */
  public String getMessagesBundlePath() {
    return "fi.pyramus.plugin.simple.messages";
  }
}
