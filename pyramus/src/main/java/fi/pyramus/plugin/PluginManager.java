package fi.pyramus.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.resolution.ArtifactDescriptorException;
import org.sonatype.aether.resolution.ArtifactDescriptorResult;
import org.sonatype.aether.resolution.ArtifactResolutionException;

import sun.misc.Service;
import fi.internetix.smvc.logging.Logging;
import fi.pyramus.plugin.maven.MavenClient;
import fi.pyramus.plugin.scheduler.ScheduledPluginDescriptor;
import fi.pyramus.plugin.scheduler.ScheduledPluginTask;
import fi.pyramus.plugin.scheduler.ScheduledTaskInterval;

/** The class responsible for managing plugins.
 * 
 */
@SuppressWarnings("restriction")
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
   * @param repositories The URL:s of the repositories containing the plugins.
   * @return The plugin manager instance.
   */
  public static final synchronized PluginManager initialize(ClassLoader parentClassLoader, List<String> repositories) {
    if (INSTANCE != null)
      throw new PluginManagerException("Plugin manger is already initialized");
      
    INSTANCE = new PluginManager(parentClassLoader, repositories);
    
    return INSTANCE;
  }
  
  private static PluginManager INSTANCE = null;
  
  PluginManager(ClassLoader parentClassLoader, List<String> repositories) {
    this.jarLoader = new JarLoader(parentClassLoader);
    mavenClient = new MavenClient(getPluginDirectory(), System.getProperty("pyramus.workspace"));
    for (String repository : repositories) {
      mavenClient.addRepository(repository);
    }
  }
  
  /** Adds a repository to fetch plugins from.
   * 
   * @param url The URL of the repository to add.
   */
  public void addRepository(String url) {
    mavenClient.addRepository(url);
  }
  
  /** Removes a plugin repository from the plugin manager.
   * 
   * @param url The URL of the repository to remove.
   */
  public void removeRepository(String url) {
    mavenClient.removeRepository(url);
  }
  
  private File getPluginDirectory() {
    // TODO: Proper error handling
    String absoluteParent = new File(".").getAbsolutePath();
    absoluteParent = absoluteParent.substring(0, absoluteParent.length() - 1);
    File parentDirectory = new File(absoluteParent);
    if (parentDirectory.exists()) {
      File pluginDirectory = new File(parentDirectory, "PyramusPlugins");
      if (pluginDirectory.exists()) {
        if (pluginDirectory.canRead() && pluginDirectory.canWrite()) {
          return pluginDirectory;
        } else {
          throw new PluginManagerException("Cannot read or write into plugin directory");
        }
      } else {
        if (parentDirectory.canWrite()) {
          if (!pluginDirectory.mkdir()) {
            throw new PluginManagerException("Failed to create new plugin directory");
          } else {
            return pluginDirectory;
          }
        } else {
          throw new PluginManagerException("Unable to create new plugin directory. Parent folder is write protected");
        }
      }
    } else {
      throw new PluginManagerException("Plugins parent directory does not exist");
    }
  }

  /** Loads a plugin using Maven.
   * 
   * @param groupId The Maven group ID of the plugin to load.
   * @param artifactId The Maven artifact ID of the plugin to load.
   * @param version The version of the plugin to load.
   */
  public void loadPlugin(String groupId, String artifactId, String version) {
    try {
      ArtifactDescriptorResult descriptorResult = mavenClient.describeLocalArtifact(groupId, artifactId, version);
      if (descriptorResult == null) {
        descriptorResult = mavenClient.describeArtifact(groupId, artifactId, version);
      }
      
      for (Dependency dependency : descriptorResult.getDependencies()) {
        if ("compile".equals(dependency.getScope())) {
          File file = mavenClient.getArtifactJarFile(dependency.getArtifact());
          Logging.logInfo("Loading " + groupId + "." + artifactId + ":" + version + " dependecy: " + file);
          jarLoader.loadJar(file);
        }
      }
      
      File jarFile = mavenClient.getArtifactJarFile(descriptorResult.getArtifact());
      Logging.logInfo("Loading " + groupId + "." + artifactId + ":" + version + " plugin jar: " + jarFile);
      jarLoader.loadJar(jarFile);
    } catch (ArtifactResolutionException e) {
      throw new PluginManagerException(e);
    } catch (ArtifactDescriptorException e) {
      throw new PluginManagerException(e);
    }
  }
 
  /** Returns <code>true</code> if the given Maven artifact
   * is loaded as a plugin, and <code>false</code> otherwise.
   * 
   * @param groupId The Maven group ID of the plugin to check for.
   * @param artifactId The Maven artifact ID of the plugin to check for.
   * @param version The version of the plugin to check for.
   * @return <code>true</code> if the given Maven artifact is
   * loaded as a plugin, and <code>false</code> otherwise.
   */
  public boolean isLoaded(String groupId, String artifactId, String version) {
    try {
      ArtifactDescriptorResult descriptorResult = mavenClient.describeArtifact(groupId, artifactId, version);
      File jarFile = mavenClient.getArtifactJarFile(descriptorResult.getArtifact());
      return jarLoader.isJarLoaded(jarFile);
    } catch (Exception e) {
      return false;
    }
  }
  
  /** Register the loaded plugins.
   * 
   */
  public void registerPlugins() {
    @SuppressWarnings("unchecked")
    Iterator<PluginDescriptor> pluginDescriptors = Service.providers(PluginDescriptor.class, jarLoader.getPluginsClassLoader());
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
    return jarLoader.getPluginsClassLoader();
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

  public List<ScheduledPluginTask> getScheduledTasks(ScheduledTaskInterval internal) {
    List<ScheduledPluginTask> result = new ArrayList<ScheduledPluginTask>();
    
    for (ScheduledPluginDescriptor sceduledPlugin : getScheduledPlugins()) {
      List<ScheduledPluginTask> tasks = sceduledPlugin.getScheduledTasks();
      if (tasks != null) {
        for (ScheduledPluginTask task : tasks) {
          if (internal.equals(task.getInternal())) {
            result.add(task); 
          }
        }
      }
    }
    
    return result;
  }
  
  public List<ScheduledPluginDescriptor> getScheduledPlugins() {
    List<ScheduledPluginDescriptor> result = new ArrayList<ScheduledPluginDescriptor>();
    
    for (PluginDescriptor plugin : getPlugins()) {
      if (plugin instanceof ScheduledPluginDescriptor) {
        result.add((ScheduledPluginDescriptor) plugin); 
      }
    }
    
    return Collections.unmodifiableList(result);
  }

  private JarLoader jarLoader;
  private MavenClient mavenClient;
  private List<PluginDescriptor> plugins = new ArrayList<PluginDescriptor>();
}
 