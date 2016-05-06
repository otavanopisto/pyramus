package fi.otavanopisto.pyramus.views.settings;

import java.util.List;
import java.util.Locale;

import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the Manage Time Units view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.settings.SaveTimeUnitsJSONRequestController
 */
public class CourseStatesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    CourseState initialCourseState = defaultsDAO.getDefaults().getInitialCourseState();
    List<CourseState> courseStates = courseStateDAO.listUnarchived();
    
    String jsonCourseStates = new JSONArrayExtractor("name", "id").extractString(courseStates);
    JSONObject joInitialCourseState = new JSONObject();
    if (initialCourseState == null) {
      joInitialCourseState.put("name", "");
      joInitialCourseState.put("id", -1);
    } else {
      joInitialCourseState.put("name", initialCourseState.getName());
      joInitialCourseState.put("id", initialCourseState.getId());
    }
    
    this.setJsDataVariable(pageRequestContext, "courseStates", jsonCourseStates);
    this.setJsDataVariable(pageRequestContext, "initialCourseState", joInitialCourseState.toString());
    
    pageRequestContext.setIncludeJSP("/templates/settings/coursestates.jsp");
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
    return Messages.getInstance().getText(locale, "settings.courseStates.pageTitle");
  }

}
