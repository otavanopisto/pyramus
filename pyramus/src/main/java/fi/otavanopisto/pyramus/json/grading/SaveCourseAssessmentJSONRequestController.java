package fi.otavanopisto.pyramus.json.grading;

import java.util.Date;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveCourseAssessmentJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    
    Long courseStudentId = jsonRequestContext.getLong("courseStudentId");
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    
    for (CourseModule courseModule : courseStudent.getCourse().getCourseModules()) {
      Long courseModuleId = courseModule.getId();
      Date assessmentDate = jsonRequestContext.getDate("assessmentDate." + courseModuleId);
      Long assessingUserId = jsonRequestContext.getLong("assessingUserId." + courseModuleId);
      Long gradeId = jsonRequestContext.getLong("gradeId." + courseModuleId);
      String verbalAssessment = jsonRequestContext.getString("verbalAssessment." + courseModuleId);
      
      StaffMember assessingUser = staffMemberDAO.findById(assessingUserId);
      Grade grade = gradeId == null ? null : gradeDAO.findById(gradeId);

      if (grade != null && assessmentDate != null && assessingUser != null) {
        CourseAssessment courseAssessment = courseAssessmentDAO.findLatestByCourseStudentAndCourseModuleAndArchived(courseStudent, courseModule, Boolean.FALSE);
        if (courseAssessment == null) {
          courseAssessment = courseAssessmentDAO.create(courseStudent, courseModule, assessingUser, grade, assessmentDate, verbalAssessment);
        }
        else {
          courseAssessment = courseAssessmentDAO.update(courseAssessment, assessingUser, grade, assessmentDate, verbalAssessment);
        }
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
