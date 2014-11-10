package fi.pyramus.json.grading;

import java.util.Date;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.grading.CourseAssessmentDAO;
import fi.pyramus.dao.grading.GradeDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.grading.CourseAssessment;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveCourseAssessmentJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    
    Long courseStudentId = jsonRequestContext.getLong("courseStudentId");
    Date assessmentDate = jsonRequestContext.getDate("assessmentDate");
    Long assessingUserId = jsonRequestContext.getLong("assessingUserId");
    Long gradeId = jsonRequestContext.getLong("gradeId");
    String verbalAssessment = jsonRequestContext.getString("verbalAssessment");
    
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    StaffMember assessingUser = staffMemberDAO.findById(assessingUserId);
    Grade grade = gradeId == null ? null : gradeDAO.findById(gradeId);

    CourseAssessment assessment = courseAssessmentDAO.findByCourseStudent(courseStudent);
    
    if (assessment == null) {
      assessment = courseAssessmentDAO.create(courseStudent, 
          assessingUser, grade, assessmentDate, verbalAssessment);
    } else {
      assessment = courseAssessmentDAO.update(assessment, 
          assessingUser, grade, assessmentDate, verbalAssessment);
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
