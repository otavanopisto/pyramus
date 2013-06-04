package fi.pyramus.views.settings;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.sf.json.*;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the Manage Course Participation Types view of the application.
 * 
 * @see fi.pyramus.json.settings.SaveTimeUnitsJSONRequestController
 */
public class CourseParticipationTypesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    
    CourseParticipationType initialCourseParticipationType = defaultsDAO.getDefaults().getInitialCourseParticipationType();
    List<CourseParticipationType> courseParticipationTypes = participationTypeDAO.listUnarchived();

    Collections.sort(courseParticipationTypes, new Comparator<CourseParticipationType>() {
      public int compare(CourseParticipationType o1, CourseParticipationType o2) {
        return o1.getIndexColumn() == null ? -1 : o2.getIndexColumn() == null ? 1 : o1.getIndexColumn().compareTo(o2.getIndexColumn());
      }
    });
    
    String jsonCourseParticipationTypes = new JSONArrayExtractor("name", "id").extractString(courseParticipationTypes);
    
    JSONObject joInitialCourseParticipationType = new JSONObject();
    if (initialCourseParticipationType == null) {
      joInitialCourseParticipationType.put("name", "");
      joInitialCourseParticipationType.put("id", -1);
    } else {
      joInitialCourseParticipationType.put("name", initialCourseParticipationType.getName());
      joInitialCourseParticipationType.put("id", initialCourseParticipationType.getId());
    }
      
    this.setJsDataVariable(pageRequestContext, "courseParticipationTypes", jsonCourseParticipationTypes);
    this.setJsDataVariable(pageRequestContext, "initialCourseParticipationType", joInitialCourseParticipationType.toString());
    
    pageRequestContext.setIncludeJSP("/templates/settings/courseparticipationtypes.jsp");
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
    return Messages.getInstance().getText(locale, "settings.courseParticipationTypes.pageTitle");
  }

}
