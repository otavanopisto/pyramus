package fi.pyramus.breadcrumbs;

import java.util.Locale;

/** Page request controllers that show up in the breadcrumb
 * implement this interface.
 *
 */
public interface Breadcrumbable {

  /** Return the name of the page.
   * 
   * @param locale The locale of the page name.
   * @return The name of the page, localized to <code>locale</code>.
   */
  public String getName(Locale locale);
  
}
