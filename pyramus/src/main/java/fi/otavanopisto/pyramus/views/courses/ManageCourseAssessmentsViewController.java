package fi.otavanopisto.pyramus.views.courses;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * The controller responsible of the Edit Course view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.users.CreateGradingScaleJSONRequestController
 */
public class ManageCourseAssessmentsViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();

    Course course = courseDAO.findById(NumberUtils.createLong(pageRequestContext.getRequest().getParameter("course")));
    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();
    
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
    
    List<CourseParticipationType> courseParticipationTypes = participationTypeDAO.listUnarchived();
    Collections.sort(courseParticipationTypes, new Comparator<CourseParticipationType>() {
      public int compare(CourseParticipationType o1, CourseParticipationType o2) {
        return o1.getIndexColumn() == null ? -1 : o2.getIndexColumn() == null ? 1 : o1.getIndexColumn().compareTo(o2.getIndexColumn());
      }
    });
    
    JSONObject jsonAllCourseModuleCourseStudents = new JSONObject();

    for (CourseModule courseModule : course.getCourseModules()) {
      JSONArray jsonCourseModuleCourseStudents = new JSONArray();
      
      for (CourseStudent courseStudent : courseStudents) {
        CourseAssessment courseAssessment = courseAssessmentDAO.findLatestByCourseStudentAndCourseModuleAndArchived(courseStudent, courseModule, Boolean.FALSE);

        String verbalAssessment = courseAssessment != null ? courseAssessment.getVerbalAssessment() : null;
        if (verbalAssessment != null) {
          verbalAssessment = StringEscapeUtils.unescapeHtml4(verbalAssessment.replaceAll("\\<.*?>",""));
          verbalAssessment = verbalAssessment.replaceAll("\\n", "");
        }

        JSONObject jsonCourseStudent = new JSONObject();
        
        jsonCourseStudent.put("courseStudentId", courseStudent.getId());
        jsonCourseStudent.put("personId", courseStudent.getStudent().getPerson().getId());
        jsonCourseStudent.put("assessmentId", courseAssessment != null ? courseAssessment.getId() : null);
        jsonCourseStudent.put("assessmentDate", (courseAssessment != null && courseAssessment.getDate() != null) ? courseAssessment.getDate().getTime() : null);
        jsonCourseStudent.put("verbalAssessment", verbalAssessment);
        jsonCourseStudent.put("assessorId", (courseAssessment != null && courseAssessment.getAssessor() != null) ? courseAssessment.getAssessor().getId() : null);
        jsonCourseStudent.put("assessorName", (courseAssessment != null && courseAssessment.getAssessor() != null) ? courseAssessment.getAssessor().getFullName() : null);
        jsonCourseStudent.put("gradeId", (courseAssessment != null && courseAssessment.getGrade() != null) ? courseAssessment.getGrade().getId() : null);
        jsonCourseStudent.put("participationTypeId", courseStudent.getParticipationType() != null ? courseStudent.getParticipationType().getId() : null);
        jsonCourseStudent.put("fullName", courseStudent.getStudent().getLastName() + ", " + courseStudent.getStudent().getFirstName());
        jsonCourseStudent.put("studyProgrammeName", courseStudent.getStudent().getStudyProgramme() != null ? courseStudent.getStudent().getStudyProgramme().getName() : null);
        
        jsonCourseModuleCourseStudents.add(jsonCourseStudent);
      }

      jsonAllCourseModuleCourseStudents.put(courseModule.getId(), jsonCourseModuleCourseStudents);
    }
    
    setJsDataVariable(pageRequestContext, "courseModuleCourseStudents", jsonAllCourseModuleCourseStudents.toString());
    
    pageRequestContext.getRequest().setAttribute("course", course);
    pageRequestContext.getRequest().setAttribute("courseModules", course.getCourseModules());
    pageRequestContext.getRequest().setAttribute("courseStudents", courseStudents);
    pageRequestContext.getRequest().setAttribute("courseParticipationTypes", courseParticipationTypes);
    pageRequestContext.getRequest().setAttribute("gradingScales", gradingScales);
    
    pageRequestContext.setIncludeJSP("/templates/courses/managecourseassessments.jsp");
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
    return Messages.getInstance().getText(locale, "courses.manageCourseAssessments.breadcrumb");
  }

}
