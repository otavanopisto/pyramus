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
import fi.otavanopisto.pyramus.dao.courses.CourseComponentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseEnrolmentTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseComponent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Edit Course view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.users.CreateGradingScaleJSONRequestController
 */
public class EditCourseViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    CourseStaffMemberDAO courseStaffMemberDAO = DAOFactory.getInstance().getCourseStaffMemberDAO();
    CourseStaffMemberRoleDAO courseStaffMemberRoleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();
    CourseComponentDAO courseComponentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    CourseEnrolmentTypeDAO enrolmentTypeDAO = DAOFactory.getInstance().getCourseEnrolmentTypeDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();

    // The course to be edited
    
    Course course = courseDAO.findById(NumberUtils.createLong(pageRequestContext.getRequest().getParameter("course")));
    pageRequestContext.getRequest().setAttribute("course", course);
    
    // Create a hashmap of the education types and education subtypes selected in the course
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    pageRequestContext.getRequest().setAttribute("educationTypes", educationTypes);
    Map<String, Boolean> enabledEducationTypes = new HashMap<>();
    for (CourseEducationType courseEducationType : course.getCourseEducationTypes()) {
      for (CourseEducationSubtype courseEducationSubtype : courseEducationType.getCourseEducationSubtypes()) {
        enabledEducationTypes.put(courseEducationType.getEducationType().getId() + "."
            + courseEducationSubtype.getEducationSubtype().getId(), Boolean.TRUE);
      }
    }
    pageRequestContext.getRequest().setAttribute("enabledEducationTypes", enabledEducationTypes);
    
    // Various lists of base entities from module, course, and resource DAOs 

    List<CourseStudent> courseStudents = courseStudentDAO.listByCourse(course);
    Collections.sort(courseStudents, new Comparator<CourseStudent>() {
      @Override
      public int compare(CourseStudent o1, CourseStudent o2) {
        int cmp = o1.getStudent().getLastName().compareToIgnoreCase(o2.getStudent().getLastName());
        if (cmp == 0)
          cmp = o1.getStudent().getFirstName().compareToIgnoreCase(o2.getStudent().getFirstName());
        return cmp;
      }
    });
    
    List<CourseStaffMember> courseUsers = courseStaffMemberDAO.listByCourse(course);
    Collections.sort(courseUsers, new Comparator<CourseStaffMember>() {
      @Override
      public int compare(CourseStaffMember o1, CourseStaffMember o2) {
        int cmp = o1.getStaffMember().getLastName().compareToIgnoreCase(o2.getStaffMember().getLastName());
        if (cmp == 0)
          cmp = o1.getStaffMember().getFirstName().compareToIgnoreCase(o2.getStaffMember().getFirstName());
        return cmp;
      }
    });
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = course.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }
    
    List<CourseComponent> courseComponents = courseComponentDAO.listByCourse(course);
    
    // course students students
    
    Map<Long, List<Student>> courseStudentsStudents = new HashMap<>();
    
    for (CourseStudent courseStudent : courseStudents) {
      courseStudentsStudents.put(courseStudent.getId(), studentDAO.listByPerson(courseStudent.getStudent().getPerson()));
    }
    
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
    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    pageRequestContext.getRequest().setAttribute("states", courseStateDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("types", courseTypeDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("roles", courseStaffMemberRoleDAO.listAll());
    pageRequestContext.getRequest().setAttribute("subjectsByNoEducationType", subjectsByNoEducationType);
    pageRequestContext.getRequest().setAttribute("subjectsByEducationType", subjectsByEducationType);
    pageRequestContext.getRequest().setAttribute("courseParticipationTypes", courseParticipationTypes);
    pageRequestContext.getRequest().setAttribute("courseEnrolmentTypes", enrolmentTypeDAO.listAll());
    pageRequestContext.getRequest().setAttribute("courseStudents", courseStudents);
    pageRequestContext.getRequest().setAttribute("courseUsers", courseUsers);
    pageRequestContext.getRequest().setAttribute("courseLengthTimeUnits", educationalTimeUnits);
    pageRequestContext.getRequest().setAttribute("courseComponents", courseComponents);
    pageRequestContext.getRequest().setAttribute("courseStudentsStudents", courseStudentsStudents);
    pageRequestContext.getRequest().setAttribute("courseDescriptions", descriptionDAO.listByCourseBase(course));
    pageRequestContext.getRequest().setAttribute("courseDescriptionCategories", descriptionCategoryDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("currencies", currencies);
    pageRequestContext.getRequest().setAttribute("curriculums", curriculums);
    
    pageRequestContext.setIncludeJSP("/templates/courses/editcourse.jsp");
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
    return Messages.getInstance().getText(locale, "courses.editCourse.breadcrumb");
  }

}
