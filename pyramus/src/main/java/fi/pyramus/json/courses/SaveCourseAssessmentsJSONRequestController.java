package fi.pyramus.json.courses;

import java.util.Date;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.grading.CourseAssessmentDAO;
import fi.pyramus.dao.grading.GradeDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.grading.CourseAssessment;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of saving course assessments.
 * 
 * @see fi.pyramus.views.modules.EditCourseViewController
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
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();

    int rowCount = requestContext.getInteger("studentsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studentsTable." + i;

      Long modified = requestContext.getLong(colPrefix + ".modified");
      if ((modified == null) || (modified.intValue() != 1))
        continue;
      
      Long courseStudentId = requestContext.getLong(colPrefix + ".courseStudentId");
      CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);

      if (courseStudent != null) {
        Long assessingUserId = requestContext.getLong(colPrefix + ".assessingUserId");
        User assessingUser = userDAO.findById(assessingUserId);
        Long gradeId = requestContext.getLong(colPrefix + ".gradeId");
        Grade grade = gradeId == null ? null : gradeDAO.findById(gradeId);
        Date assessmentDate = requestContext.getDate(colPrefix + ".assessmentDate");

        Long participationTypeId = requestContext.getLong(colPrefix + ".participationType");
        CourseParticipationType participationType = participationTypeDAO.findById(participationTypeId);
        String verbalAssessment = null;

        CourseAssessment assessment = courseAssessmentDAO.findByCourseStudent(courseStudent);

        Long verbalModified = requestContext.getLong(colPrefix + ".verbalModified");
        if ((verbalModified != null) && (verbalModified.intValue() == 1)) {
          verbalAssessment = requestContext.getString(colPrefix + ".verbalAssessment");
        } else {
          if (assessment != null)
            verbalAssessment = assessment.getVerbalAssessment();
        }

        if (assessment != null) {
          assessment = courseAssessmentDAO.update(assessment, assessingUser, grade, assessmentDate, verbalAssessment);
        } else {
          assessment = courseAssessmentDAO.create(courseStudent, assessingUser, grade, assessmentDate, verbalAssessment);
        }

        // Update Participation type
        courseStudentDAO.update(courseStudent, courseStudent.getStudent(), 
            courseStudent.getCourseEnrolmentType(), participationType, courseStudent.getEnrolmentTime(), 
            courseStudent.getLodging(), courseStudent.getOptionality());
      } else
        throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "CourseStudent was not defined");
    }
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
