package fi.otavanopisto.pyramus.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Class used for loading plugin libarries
 */
public class LibraryLoader {

  /** Creates a new class loader as a child of the given class loader.
   * 
   * @param parentClassLoader The parent class loader.
   */
  public LibraryLoader(ClassLoader parentClassLoader) {
    this.classLoader = new PluginClassLoader(parentClassLoader);
  }

  /** 
   * Creates a new class loader as a child of the default class loader.
   */
  public LibraryLoader() {
    this(LibraryLoader.class.getClassLoader());
  }

  /** 
   * Returns the class loader used for loading plugins.
   * 
   * @return The class loader used for loading plugins.
   */
  public URLClassLoader getPluginsClassLoader() {
    return classLoader;
  }
  
  public void loadClassPath(URL classPath) {
  	classLoader.addPath(classPath);
  }

  /** 
   * Loads the given JAR file.
   * 
   * @param jarFile The JAR file to load.
   * @throws PluginManagerException 
   */
  public void loadJar(File jarFile) throws PluginManagerException {
    try {
      loadJar(jarFile.toURI().toURL());
    } catch (MalformedURLException e) {
    }
  }

  /** Loads the JAR file located in the given URL.
   * 
   * @param jarUrl The URL of the JAR file.
   * @throws PluginManagerException 
   */
  public void loadJar(URL jarUrl) throws PluginManagerException {
    if (isJarLoaded(jarUrl))
      return;

    classLoader.addJar(jarUrl);
  }
  
  /** Returns <code>true</code> if the specified JAR file is already loaded,
   * <code>false</code> otherwise.
   * 
   * @param jarFile The JAR file to check.
   * @return <code>true</code> if the filer is loaded, <code>false</code> otherwise.
   */
  public boolean isJarLoaded(File jarFile) {
    try {
      return isJarLoaded(jarFile.toURI().toURL());
    } catch (MalformedURLException e) {
    }
    
    return false;
  }
  
  /** Returns <code>true</code> if the specified JAR is already loaded,
   * <code>false</code> otherwise.
   * 
   * @param jarURl the URL of the JAR to check.
   * @return <code>true</code> if the filer is loaded, <code>false</code> otherwise.
   */
  public boolean isJarLoaded(URL jarUrl) {
    for (URL url : getPluginsClassLoader().getURLs()) {
      if (url.equals(jarUrl))
        return true;
    }
    
    return false;
  }

  private PluginClassLoader classLoader = null;
}
