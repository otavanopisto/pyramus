package fi.otavanopisto.pyramus.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Exclusion;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.util.artifact.ArtifacIdUtils;
import org.sonatype.aether.util.artifact.DefaultArtifact;

import sun.misc.Service;
import fi.internetix.smvc.logging.Logging;
import fi.otavanopisto.pyramus.domainmodel.plugins.PluginRepository;
import fi.otavanopisto.pyramus.plugin.maven.MavenClient;
import fi.otavanopisto.pyramus.plugin.scheduler.ScheduledPluginDescriptor;
import fi.otavanopisto.pyramus.plugin.scheduler.ScheduledPluginTask;
import fi.otavanopisto.pyramus.plugin.scheduler.ScheduledTaskInterval;

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
   * @param pluginRepositories The URL:s of the repositories containing the plugins.
   * @return The plugin manager instance.
   */
  public static final synchronized PluginManager initialize(ClassLoader parentClassLoader, List<PluginRepository> pluginRepositories) {
    if (INSTANCE != null)
      throw new PluginManagerException("Plugin manger is already initialized");
      
    INSTANCE = new PluginManager(parentClassLoader, pluginRepositories);
    
    return INSTANCE;
  }
  
  private static PluginManager INSTANCE = null;
  
  PluginManager(ClassLoader parentClassLoader, List<PluginRepository> pluginRepositories) {
    this.libraryLoader = new LibraryLoader(parentClassLoader);
    mavenClient = new MavenClient(getPluginDirectory(), System.getProperty("pyramus.workspace"));
    
    // Maven Central repository is always present
    mavenClient.addRepository(new RemoteRepository("central", "default", "https://repo.maven.apache.org/maven2"));

    for (PluginRepository repository : pluginRepositories) {
      mavenClient.addRepository(repository.getRepositoryId(), repository.getUrl());
    }
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
          throw new PluginManagerException("Cannot read or write into plugin directory '" +  pluginDirectory +"'");
        }
      } else {
        if (parentDirectory.canWrite()) {
          if (!pluginDirectory.mkdir()) {
            throw new PluginManagerException("Failed to create new plugin directory under parent directory '" + parentDirectory + "'");
          } else {
            return pluginDirectory;
          }
        } else {
          throw new PluginManagerException("Unable to create new plugin directory. Parent directory '" + parentDirectory + "' is write protected");
        }
      }
    } else {
      throw new PluginManagerException("Plugins parent directory '" + parentDirectory + "' does not exist");
    }
  }

  /** Loads a plugin using Maven.
   * 
   * @param groupId The Maven group ID of the plugin to load.
   * @param artifactId The Maven artifact ID of the plugin to load.
   * @param version The version of the plugin to load.
   */
  public void loadPlugin(String groupId, String artifactId, String version) {
//    String groupId = loadInfo.getGroupId();
//    String artifactId = loadInfo.getArtifactId();
//    String version = loadInfo.getVersion();
    
//    loadedPluginLibraryInfos.add(loadInfo);
    
    try {
      Artifact libraryArtifact = new DefaultArtifact(groupId, artifactId, "jar", version);
      List<Exclusion> applicationProvidedArtifacts = new ArrayList<>();
      
      List<ArtifactResult> resolvedDependencies = mavenClient.resolveDependencies(libraryArtifact, "compile", applicationProvidedArtifacts);
      for (ArtifactResult resolvedDependency : resolvedDependencies) {
        if (resolvedDependency.isResolved()) {
          File artifactFile = mavenClient.getArtifactJarFile(resolvedDependency.getArtifact());
          
          if (artifactFile.isDirectory()) {
            Logging.logInfo("Loading " + ArtifacIdUtils.toId(resolvedDependency.getArtifact()) + " plugin folder: " + artifactFile);
            try {
              libraryLoader.loadClassPath(artifactFile.toURI().toURL());          
            } catch (Exception e) {
              Logging.logException("Error occurred while loading plugin folder " + artifactFile, e);
            }
          } else {
            Logging.logInfo("Loading " + ArtifacIdUtils.toId(resolvedDependency.getArtifact()) + " plugin library jar: " + artifactFile);
            libraryLoader.loadJar(artifactFile);
          }
        } else {
          Logging.logError("Could not resolve " + ArtifacIdUtils.toId(libraryArtifact) + " -plugin dependency: " + ArtifacIdUtils.toId(resolvedDependency.getArtifact()));
        }
      }
    } catch (DependencyCollectionException | DependencyResolutionException e) {
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
      Artifact libraryArtifact = new DefaultArtifact(groupId, artifactId, "jar", version);
      File jarFile = mavenClient.getArtifactJarFile(libraryArtifact);
//      ArtifactDescriptorResult descriptorResult = mavenClient.describeArtifact(groupId, artifactId, version);
//      File jarFile = mavenClient.getArtifactJarFile(descriptorResult.getArtifact());
      return libraryLoader.isJarLoaded(jarFile);
    } catch (Exception e) {
      return false;
    }
  }
  
  /** Register the loaded plugins.
   * 
   */
  public void registerPlugins() {
    Iterator<PluginDescriptor> pluginDescriptors = Service.providers(PluginDescriptor.class, libraryLoader.getPluginsClassLoader());
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
    return libraryLoader.getPluginsClassLoader();
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
    List<ScheduledPluginTask> result = new ArrayList<>();
    
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
    List<ScheduledPluginDescriptor> result = new ArrayList<>();
    
    for (PluginDescriptor plugin : getPlugins()) {
      if (plugin instanceof ScheduledPluginDescriptor) {
        result.add((ScheduledPluginDescriptor) plugin); 
      }
    }
    
    return Collections.unmodifiableList(result);
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

//  private List<Exclusion> applicationProvidedArtifacts = new ArrayList<>();
  private LibraryLoader libraryLoader;
  private MavenClient mavenClient;
  private List<PluginDescriptor> plugins = new ArrayList<>();
}
 