package fi.otavanopisto.pyramus.views.courses;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseEnrolmentTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseTypeDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleComponentDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.modules.ModuleComponent;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Create Course view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.users.CreateGradingScaleJSONRequestController
 */
public class CreateCourseViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseStaffMemberRoleDAO courseStaffMemberRoleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    CourseEnrolmentTypeDAO enrolmentTypeDAO = DAOFactory.getInstance().getCourseEnrolmentTypeDAO();
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    ModuleComponentDAO moduleComponentDAO = DAOFactory.getInstance().getModuleComponentDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();

    // The module acting as the base of the new course
    
    Module module = moduleDAO.findById(NumberUtils.createLong(pageRequestContext.getRequest().getParameter("module")));
    pageRequestContext.getRequest().setAttribute("module",  module);
    
    // Create a hashmap of the education types and education subtypes selected in the module so that the
    // course to be created has them selected as well
    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    pageRequestContext.getRequest().setAttribute("educationTypes", educationTypes);
    Map<String, Boolean> enabledEducationTypes = new HashMap<>();
    for (CourseEducationType courseEducationType : module.getCourseEducationTypes()) {
      for (CourseEducationSubtype moduleEducationSubtype : courseEducationType.getCourseEducationSubtypes()) {
        enabledEducationTypes.put(courseEducationType.getEducationType().getId() + "."
            + moduleEducationSubtype.getEducationSubtype().getId(), Boolean.TRUE);
      }
    }
    pageRequestContext.getRequest().setAttribute("enabledEducationTypes", enabledEducationTypes);
    
    // Module tags for the new course
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = module.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }
    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    
    List<ModuleComponent> moduleComponents = moduleComponentDAO.listByModule(module);
    
    // Subjects
    Map<Long, List<Subject>> subjectsByEducationType = new HashMap<>();
    List<Subject> subjectsByNoEducationType = subjectDAO.listByEducationType(null);
    Collections.sort(subjectsByNoEducationType, new StringAttributeComparator("getName"));
    for (EducationType educationType : educationTypes) {
      List<Subject> subjectsOfType = subjectDAO.listByEducationType(educationType);
      if (subjectsOfType != null && !subjectsOfType.isEmpty()) {
        Collections.sort(subjectsOfType, new StringAttributeComparator("getName"));
        subjectsByEducationType.put(educationType.getId(), subjectsOfType);
      }
    }

    // Various lists of base entities from module, course, and resource DAOs
    
    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(educationalTimeUnits, new StringAttributeComparator("getName"));

    List<CourseParticipationType> courseParticipationTypes = participationTypeDAO.listUnarchived();
    Collections.sort(courseParticipationTypes, new Comparator<CourseParticipationType>() {
      public int compare(CourseParticipationType o1, CourseParticipationType o2) {
        return o1.getIndexColumn() == null ? -1 : o2.getIndexColumn() == null ? 1 : o1.getIndexColumn().compareTo(o2.getIndexColumn());
      }
    });
    
    Map<Long, List<EducationSubtype>> educationSubtypes = new HashMap<>();
    
    for (EducationType educationType : educationTypes) {
      List<EducationSubtype> subtypes = educationSubtypeDAO.listByEducationType(educationType);
      Collections.sort(subtypes, new StringAttributeComparator("getName"));
      educationSubtypes.put(educationType.getId(), subtypes);
    }

    // TODO: Support other currencies
    List<Currency> currencies = Arrays.asList(Currency.getInstance("EUR"));

    List<Curriculum> curriculums = curriculumDAO.listUnarchived();
    Collections.sort(curriculums, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("educationSubtypes", educationSubtypes);
    pageRequestContext.getRequest().setAttribute("states", courseStateDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("types", courseTypeDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("roles", courseStaffMemberRoleDAO.listAll());
    pageRequestContext.getRequest().setAttribute("subjectsByNoEducationType", subjectsByNoEducationType);
    pageRequestContext.getRequest().setAttribute("subjectsByEducationType", subjectsByEducationType);
    pageRequestContext.getRequest().setAttribute("courseParticipationTypes", courseParticipationTypes);
    pageRequestContext.getRequest().setAttribute("courseEnrolmentTypes", enrolmentTypeDAO.listAll());
    pageRequestContext.getRequest().setAttribute("courseLengthTimeUnits", educationalTimeUnits);
    pageRequestContext.getRequest().setAttribute("moduleComponents", moduleComponents);
    pageRequestContext.getRequest().setAttribute("courseDescriptions", descriptionDAO.listByCourseBase(module));
    pageRequestContext.getRequest().setAttribute("courseDescriptionCategories", descriptionCategoryDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("currencies", currencies);
    pageRequestContext.getRequest().setAttribute("curriculums", curriculums);
    
    pageRequestContext.setIncludeJSP("/templates/courses/createcourse.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating courses is available for users with
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
    return Messages.getInstance().getText(locale, "courses.createCourse.pageTitle");
  }

}
