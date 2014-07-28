package fi.pyramus.views.users;

import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class SearchUsersViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Returns roles that are allowed to use this resource.
   *  
   * @see fi.fi.pyramus.framework.PyramusViewController#getAllowedRoles()
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Processes the page request.
   * 
   * @param pageRequestContext Request context
   */
  public void process(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/users/searchusers.jsp");
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "users.searchUsers.pageTitle");
  }

}
