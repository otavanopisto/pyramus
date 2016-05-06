package fi.otavanopisto.pyramus.views.settings;

import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the CourseTypes view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.settings.SaveCourseTypesJSONRequestController
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
    return Messages.getInstance().getText(locale, "settings.courseTypes.pageTitle");
  }

}
