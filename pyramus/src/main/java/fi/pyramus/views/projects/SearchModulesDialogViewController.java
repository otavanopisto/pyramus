package fi.pyramus.views.projects;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the Search Modules dialog of the application.
 */
public class SearchModulesDialogViewController extends PyramusViewController {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/projects/searchmodulesdialog.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating projects is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
