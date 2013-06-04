package fi.pyramus.plugin;

import java.util.Map;

/** The entry point for a plugin. Pyramus uses the methods of
 * this class to discover the functionality of the plugin.
 * The methods of this class should return the controllers
 * and providers the plugin implements.
 *
 */
public interface PluginDescriptor {

  public String getName();
  
  /** Returns the page request controllers implemented by this plugin.
   * 
   * @return The page request controllers implemented by this plugin.
   */
  public Map<String, Class<?>> getPageRequestControllers();
  /** Returns the JSON request controllers implemented by this plugin.
   * 
   * @return The JSON request controllers implemented by this plugin.
   */
  public Map<String, Class<?>> getJSONRequestControllers();
  /** Returns the binary request controllers implemented by this plugin.
   * 
   * @return The binary request controllers implemented by this plugin.
   */
  public Map<String, Class<?>> getBinaryRequestControllers();
  /** Returns the page hook controllers implemented by this plugin.
   * 
   * @return The page hook controllers implemented by this plugin.
   */
  public Map<String, Class<?>> getPageHookControllers();
  /** Returns the authentication providers implemented by this plugin.
   * 
   * @return The authentication providers implemented by this plugin.
   */
  public Map<String, Class<?>> getAuthenticationProviders();
  public String getMessagesBundlePath();
}