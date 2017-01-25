package fi.otavanopisto.pyramus.views.settings;

import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the Report Categories view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.settings.SaveReportCategoriesJSONRequestController
 */
public class ManageCourseUserRolesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
	CourseStaffMemberRoleDAO roleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();

    List<CourseStaffMemberRole> roles = roleDAO.listAll();
    
    String jsonRoles = new JSONArrayExtractor("name", "id").extractString(roles);
    
    this.setJsDataVariable(pageRequestContext, "courseUserRoles", jsonRoles);
    pageRequestContext.setIncludeJSP("/templates/settings/courseuserroles.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "settings.courseUserRoles.pageTitle");
  }

}
