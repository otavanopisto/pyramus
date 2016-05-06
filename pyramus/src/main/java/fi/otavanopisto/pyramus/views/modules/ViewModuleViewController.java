package fi.otavanopisto.pyramus.views.modules;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleComponentDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the View Module view of the application.
 */
public class ViewModuleViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    ModuleComponentDAO moduleComponentDAO = DAOFactory.getInstance().getModuleComponentDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();

    Module module = moduleDAO.findById(pageRequestContext.getLong("module"));
    List<Course> courses = courseDAO.listByModule(module);
    Collections.sort(courses, new StringAttributeComparator("getName", true));

    pageRequestContext.getRequest().setAttribute("module", module);
    pageRequestContext.getRequest().setAttribute("courses", courses);
    pageRequestContext.getRequest().setAttribute("moduleComponents", moduleComponentDAO.listByModule(module));
    pageRequestContext.getRequest().setAttribute("moduleDescriptions", descriptionDAO.listByCourseBase(module));
    
    pageRequestContext.setIncludeJSP("/templates/modules/viewmodule.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Editing courses is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
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
    return Messages.getInstance().getText(locale, "modules.viewModule.breadcrumb");
  }

}
