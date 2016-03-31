package fi.otavanopisto.pyramus.freemarker;

import java.net.URL;

import freemarker.cache.URLTemplateLoader;

public class ClassLoaderTemplateLoader extends URLTemplateLoader {
  
  public ClassLoaderTemplateLoader(ClassLoader classLoader, String path) {
    this.classLoader = classLoader;
    this.path = canonicalizePrefix(path);
    if (this.path.startsWith("/")) {
      this.path = this.path.substring(1);
    }
  }

  protected URL getURL(String name) {
    return classLoader.getResource(path + name);
  }

  private ClassLoader classLoader;
  private String path;
}
