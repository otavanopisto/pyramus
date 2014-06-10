package fi.pyramus.breadcrumbs;

/** A breadcrumb object. */
public class Breadcrumb {
  
  /** Creates a new breadcrumb.
   * 
   * @param url The URL of the breadcrumb.
   * @param name The name of the breadcrumb.
   */
  public Breadcrumb(String url, String name) {
    this.url = url;
    this.name = name;
  }

  /** Sets the URL of the breadcrumb.
   * 
   * @param url The new URL of the breadcrumb.
   */
  public void setUrl(String url) {
    this.url = url;
  }
  /** Returns the URL of the breadcrumb.
   * 
   * @return The URL of the breadcrumb.
   */
  public String getUrl() {
    return url;
  }
  /** Sets the name of the breadcrumb.
   * 
   * @param name The name of the breadcrumb.
   */
  public void setName(String name) {
    this.name = name;
  }
  /** Returns the name of the breadcrumb.
   * 
   * @return The name of the breadcrumb.
   */
  public String getName() {
    return name;
  }
  
  private String url;
  
  private String name;

}
