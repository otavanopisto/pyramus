package fi.pyramus.views.projects;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Create Project view of the application.
 */
public class CreateProjectViewController extends PyramusViewController implements Breadcrumbable {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();

    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(educationalTimeUnits, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("users", userDAO.listAll());
    pageRequestContext.getRequest().setAttribute("optionalStudiesLengthTimeUnits", educationalTimeUnits);
    pageRequestContext.setIncludeJSP("/templates/projects/createproject.jsp");
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

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "projects.createProject.pageTitle");
  }

}
