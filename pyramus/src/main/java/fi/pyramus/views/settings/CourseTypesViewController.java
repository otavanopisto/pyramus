package fi.pyramus.views.settings;

import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseTypeDAO;
import fi.pyramus.domainmodel.courses.CourseType;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the CourseTypes view of the application.
 * 
 * @see fi.pyramus.json.settings.SaveCourseTypesJSONRequestController
 */
public class CourseTypesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    List<CourseType> courseTypes = courseTypeDAO.listUnarchived();
    
    String jsonCourseStates = new JSONArrayExtractor("name", "id").extractString(courseTypes);
    setJsDataVariable(pageRequestContext, "courseTypes", jsonCourseStates);
    
    pageRequestContext.setIncludeJSP("/templates/settings/coursetypes.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
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
    return Messages.getInstance().getText(locale, "settings.courseTypes.pageTitle");
  }

}
