package fi.otavanopisto.pyramus.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/** The class responsible for managing plugins.
 * 
 */
public class PluginManager {
  
  /** Returns the (singleton) instance of the plugin manager.
   * 
   * @return The (singleton) instance of the plugin manager. 
   */
  public static final synchronized PluginManager getInstance() {
    return INSTANCE;
  }

  /** Initializes the plugin manager. Call this before
   * any other methods.
   * 
   * @param parentClassLoader The parent class loader of the plugin manager.
   * @param pluginRepositories The URL:s of the repositories containing the plugins.
   * @return The plugin manager instance.
   */
  public static final synchronized PluginManager initialize(ClassLoader parentClassLoader) {
    if (INSTANCE != null)
      throw new PluginManagerException("Plugin manger is already initialized");
      
    INSTANCE = new PluginManager(parentClassLoader);
    
    return INSTANCE;
  }
  
  private static PluginManager INSTANCE = null;
  
  PluginManager(ClassLoader parentClassLoader) {
    this.classLoader = parentClassLoader;
  }
  
  /** Register the loaded plugins.
   * 
   */
  public void registerPlugins() {
    Iterator<PluginDescriptor> pluginDescriptors = java.util.ServiceLoader.load(PluginDescriptor.class, classLoader).iterator();
    while (pluginDescriptors.hasNext()) {
      PluginDescriptor pluginDescriptor = pluginDescriptors.next();
      registerPlugin(pluginDescriptor);
    }
  }

  /** Returns the class loader that loads the plugins.
   * 
   * @return the class loader that loads the plugins. 
   */
  public ClassLoader getPluginsClassLoader() {
    return classLoader;
  }
  
  /** Returns the currently loaded plugins.
   * 
   * @return the currently loaded plugins.
   */
  public synchronized List<PluginDescriptor> getPlugins() {
    return plugins;
  }

  /** Register a loaded plugin.
   * 
   * @param plugin The plugin to register.
   */
  public synchronized void registerPlugin(PluginDescriptor plugin) {
    for (PluginDescriptor pluginDescriptor : plugins) {
      if (pluginDescriptor.getName().equals(plugin.getName()))
        return;
    }
    
    plugins.add(plugin);
  }

  public List<CustomLoginScreenPlugin> getCustomLoginScreenPlugins() {
    List<CustomLoginScreenPlugin> result = new ArrayList<>();
    
    for (PluginDescriptor plugin : getPlugins()) {
      if (plugin instanceof CustomLoginScreenPlugin) {
        result.add((CustomLoginScreenPlugin) plugin); 
      }
    }
    
    return Collections.unmodifiableList(result);
  }
  
  public String getCustomLoginScreen(String contextType, String contextId) {
    List<CustomLoginScreenPlugin> customLoginScreenPlugins = getCustomLoginScreenPlugins();
    if (customLoginScreenPlugins != null) {
      for (CustomLoginScreenPlugin customLoginScreenPlugin : customLoginScreenPlugins) {
        String loginFtl = customLoginScreenPlugin.getContextLoginFtl(contextType, contextId);
        if (StringUtils.isNotBlank(loginFtl)) {
          return loginFtl;
        }
      }
    }
    
    return null;
  }

  private ClassLoader classLoader;
  private List<PluginDescriptor> plugins = new ArrayList<>();
}
 