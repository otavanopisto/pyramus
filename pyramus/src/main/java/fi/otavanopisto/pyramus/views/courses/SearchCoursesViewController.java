package fi.otavanopisto.pyramus.views.courses;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

public class SearchCoursesViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext pageRequestContext) {
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    

    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));

    // Subjects
    Map<Long, List<Subject>> subjectsByEducationType = new HashMap<>();
    List<Subject> subjectsByNoEducationType = subjectDAO.listByEducationType(null);
    Collections.sort(subjectsByNoEducationType, new StringAttributeComparator("getName"));
    
    Map<Long, List<EducationSubtype>> educationSubtypesByEduType = new HashMap<>();
    
    for (EducationType educationType : educationTypes) {
      List<Subject> subjectsOfType = subjectDAO.listByEducationType(educationType);
      if ((subjectsOfType != null) && (subjectsOfType.size() > 0)) {
        Collections.sort(subjectsOfType, new StringAttributeComparator("getName"));
        subjectsByEducationType.put(educationType.getId(), subjectsOfType);
      }

      List<EducationSubtype> educationSubtypes = educationSubtypeDAO.listByEducationType(educationType);
      educationSubtypesByEduType.put(educationType.getId(), educationSubtypes);
    }

    pageRequestContext.getRequest().setAttribute("educationTypes", educationTypes);
    pageRequestContext.getRequest().setAttribute("educationSubtypes", educationSubtypesByEduType);
    pageRequestContext.getRequest().setAttribute("subjectsByNoEducationType", subjectsByNoEducationType);
    pageRequestContext.getRequest().setAttribute("subjectsByEducationType", subjectsByEducationType);
    pageRequestContext.getRequest().setAttribute("states", courseStateDAO.listUnarchived());
    pageRequestContext.setIncludeJSP("/templates/courses/searchcourses.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "courses.searchCourses.pageTitle");
  }
  
}
