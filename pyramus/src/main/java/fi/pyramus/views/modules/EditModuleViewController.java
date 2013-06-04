package fi.pyramus.views.modules;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.dao.courses.CourseDescriptionDAO;
import fi.pyramus.dao.modules.ModuleComponentDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Edit Module view of the application.
 * 
 * @see fi.pyramus.json.users.EditModuleJSONRequestController
 */
public class EditModuleViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * <p/>
   * In order for the JSP page to build the module editing view, a list of all education types and
   * subjects are added as request attributes.
   * <p/>
   * In addition, a hashmap containing the education types and education subtypes checked in the
   * module is constructed. In that hashmap, the key is in the form of
   * <code>educationTypeId.educationSubtypeId</code> and the value is <code>Boolean.TRUE</code>. The
   * JSP page could probably figure out the checked education types and subtypes on its own but the
   * hashmap makes it a little bit easier and more streamlined.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    ModuleComponentDAO moduleComponentDAO = DAOFactory.getInstance().getModuleComponentDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();

    Long moduleId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("module"));
    Module module = moduleDAO.findById(moduleId);
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = module.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }

    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("educationTypes", educationTypes);
    Map<String, Boolean> enabledEducationTypes = new HashMap<String, Boolean>();
    for (CourseEducationType courseEducationType : module.getCourseEducationTypes()) {
      for (CourseEducationSubtype moduleEducationSubtype : courseEducationType.getCourseEducationSubtypes()) {
        enabledEducationTypes.put(courseEducationType.getEducationType().getId() + "."
            + moduleEducationSubtype.getEducationSubtype().getId(), Boolean.TRUE);
      }
    }
    
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

    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    pageRequestContext.getRequest().setAttribute("module", module);
    pageRequestContext.getRequest().setAttribute("subjectsByNoEducationType", subjectsByNoEducationType);
    pageRequestContext.getRequest().setAttribute("subjectsByEducationType", subjectsByEducationType);
    pageRequestContext.getRequest().setAttribute("moduleLengthTimeUnits", educationalTimeUnits);
    pageRequestContext.getRequest().setAttribute("moduleComponents", moduleComponentDAO.listByModule(module));
    pageRequestContext.getRequest().setAttribute("enabledEducationTypes", enabledEducationTypes);
    pageRequestContext.getRequest().setAttribute("courseDescriptions", descriptionDAO.listByCourseBase(module));
    pageRequestContext.getRequest().setAttribute("courseDescriptionCategories", descriptionCategoryDAO.listUnarchived());
    pageRequestContext.setIncludeJSP("/templates/modules/editmodule.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Editing modules is available for users with
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
    return Messages.getInstance().getText(locale, "modules.editModule.breadcrumb");
  }

}
