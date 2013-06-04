package fi.pyramus.views.modules;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Create Module view of the application.
 * 
 * @see fi.pyramus.json.users.CreateGradingScaleJSONRequestController
 */
public class CreateModuleViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * <p/>
   * In order for the JSP page to build the module creation view, a list of all education types and
   * subjects are added as request attributes.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();

    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));

    // Subjects
    Map<Long, List<Subject>> subjectsByEducationType = new HashMap<Long, List<Subject>>();
    List<Subject> subjectsByNoEducationType = subjectDAO.listByEducationType(null);
    Collections.sort(subjectsByNoEducationType, new StringAttributeComparator("getName"));
    for (EducationType educationType : educationTypes) {
      List<Subject> subjectsOfType = subjectDAO.listByEducationType(educationType);
      if ((subjectsOfType != null) && (subjectsOfType.size() > 0)) {
        Collections.sort(subjectsOfType, new StringAttributeComparator("getName"));
        subjectsByEducationType.put(educationType.getId(), subjectsOfType);
      }
    }

    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(educationalTimeUnits, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("educationTypes", educationTypes);
    pageRequestContext.getRequest().setAttribute("subjectsByNoEducationType", subjectsByNoEducationType);
    pageRequestContext.getRequest().setAttribute("subjectsByEducationType", subjectsByEducationType);
    pageRequestContext.getRequest().setAttribute("moduleLengthTimeUnits", educationalTimeUnits);
    pageRequestContext.getRequest().setAttribute("courseDescriptionCategories", descriptionCategoryDAO.listUnarchived());
    pageRequestContext.setIncludeJSP("/templates/modules/createmodule.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating modules is available for users with
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
    return Messages.getInstance().getText(locale, "modules.createModule.pageTitle");
  }

}
