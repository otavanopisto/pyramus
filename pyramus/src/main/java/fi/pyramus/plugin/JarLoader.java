package fi.pyramus.plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/** A loader class for plugin JARs fetched with Maven. */
public class JarLoader {

  /** Creates a new class loader as a child of the given class loader.
   * 
   * @param parentClassLoader The parent class loader.
   */
  public JarLoader(ClassLoader parentClassLoader) {
    this.parentClassLoader = parentClassLoader;
  }

  /** Creates a new class loader as a child of the default class loader.
   * 
   */
  public JarLoader() {
    this(JarLoader.class.getClassLoader());
  }

  /** Returns the class loader used for loading plugins.
   * 
   * @return The class loader used for loading plugins.
   */
  public URLClassLoader getPluginsClassLoader() {
    if (classLoader == null) {
      classLoader = new URLClassLoader(new URL[] {}, parentClassLoader);
    }

    return classLoader;
  }

  /** Loads the given JAR file.
   * 
   * @param jarFile The JAR file to load.
   */
  public void loadJar(File jarFile) {
    try {
      loadJar(jarFile.toURI().toURL());
    } catch (MalformedURLException e) {
    }
  }

  /** Loads the JAR file located in the given URL.
   * 
   * @param jarUrl The URL of the JAR file.
   */
  public void loadJar(URL jarUrl) {
    if (isJarLoaded(jarUrl))
      return;

    try {
      // TODO: Miksi reflektio?
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
      method.setAccessible(true);
      method.invoke(getPluginsClassLoader(), new Object[] { jarUrl });
    } catch (SecurityException e) {
      throw new PluginManagerException(e);
    } catch (NoSuchMethodException e) {
      throw new PluginManagerException(e);
    } catch (IllegalArgumentException e) {
      throw new PluginManagerException(e);
    } catch (IllegalAccessException e) {
      throw new PluginManagerException(e);
    } catch (InvocationTargetException e) {
      throw new PluginManagerException(e);
    }
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

  private ClassLoader parentClassLoader = null;
  private URLClassLoader classLoader = null;
}
