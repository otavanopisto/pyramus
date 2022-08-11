package fi.otavanopisto.pyramus.json.courses;

import java.util.Date;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of saving course assessments.
 * 
 * @see fi.otavanopisto.pyramus.views.modules.EditCourseViewController
 */
public class SaveCourseAssessmentsJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit a course.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>studentsTable.rowCount</code>
   *   <dd>The number of students in this course assessment.</dd>
   *   <dt><code>studentsTable.*.modified</code></dt>
   *   <dd><code>true</code> if the student was modified.</dd>
   *   <dt><code>studentsTable.*.courseStudentId</code></dt>
   *   <dd>The ID of the student in the course.</dd>
   *   <dt><code>studentsTable.*.assessingUserId</code></dt>
   *   <dd>The User ID of the assesser.</dd>
   *   <dt><code>studentsTable.*.gradeId</code></dt>
   *   <dd>The ID of the student's grade.</dd>
   *   <dt><code>studentsTable.*.assessmentDate</code></dt>
   *   <dd>The date the student was assessed, as a timestamp in ms.</dd>
   *   <dt><code>studentsTable.*.participationType</code></dt>
   *   <dd>The student's participation type.</dd>
   *   <dt><code>studentsTable.*.verbalModified</code></dt>
   *   <dd><code>true</code> if the verbal assessment is modified.
   *   <dt><code>studentsTable.*.verbalAssessment</code></dt>
   *   <dd>The verbal assessment of the student.</dd>
   * </dl>
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();

    Long courseId = requestContext.getLong("courseId");
    Course course = courseDAO.findById(courseId);
    
    for (CourseModule courseModule : course.getCourseModules()) {
      int rowCount = requestContext.getInteger(String.format("courseModule.%d.studentsTable.rowCount", courseModule.getId()));
      for (int i = 0; i < rowCount; i++) {
        String colPrefix = String.format("courseModule.%d.studentsTable.%d", courseModule.getId(), i);
  
        Long modified = requestContext.getLong(colPrefix + ".modified");
        if (modified == null || modified.intValue() != 1)
          continue;
        
        Long courseStudentId = requestContext.getLong(colPrefix + ".courseStudentId");
        CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
  
        if (courseStudent != null) {
          Long assessingUserId = requestContext.getLong(colPrefix + ".assessingUserId");
          StaffMember assessingUser = staffMemberDAO.findById(assessingUserId);
          Long gradeId = requestContext.getLong(colPrefix + ".gradeId");
          Grade grade = gradeId == null ? null : gradeDAO.findById(gradeId);
          Date assessmentDate = requestContext.getDate(colPrefix + ".assessmentDate");
  
          Long participationTypeId = requestContext.getLong(colPrefix + ".participationType");
          CourseParticipationType participationType = participationTypeDAO.findById(participationTypeId);
          String verbalAssessment = null;
  
          CourseAssessment courseAssessment = courseAssessmentDAO.findLatestByCourseStudentAndCourseModuleAndArchived(courseStudent, courseModule, Boolean.FALSE);
          Long verbalModified = requestContext.getLong(colPrefix + ".verbalModified");
          if (verbalModified != null && verbalModified.intValue() == 1) {
            verbalAssessment = requestContext.getString(colPrefix + ".verbalAssessment");
          }
          else {
            if (courseAssessment != null) {
              verbalAssessment = courseAssessment.getVerbalAssessment();
            }
          }
  
          if (courseAssessment != null) {
            courseAssessment = courseAssessmentDAO.update(courseAssessment, assessingUser, grade, assessmentDate, verbalAssessment);
          }
          else {
            courseAssessment = courseAssessmentDAO.create(courseStudent, courseModule, assessingUser, grade, assessmentDate, verbalAssessment);
          }
  
          // Update Participation type
          courseStudentDAO.updateParticipationType(courseStudent, participationType);
        }
        else {
          throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "CourseStudent was not defined");
        }
      }
    }
    
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
