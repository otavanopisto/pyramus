package fi.otavanopisto.pyramus.json.students;

import java.util.List;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupUserDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveStudentJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    
    StaffMember loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());
    
    Long studentId = requestContext.getLong("student");
    Student student = studentDAO.findById(studentId);
    studentDAO.archive(student, loggedUser);
    
    // Archive the student projects of archived student
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();
    List<StudentProject> studentProjects = studentProjectDAO.listByStudent(student);
    if (!studentProjects.isEmpty()) {
      for (StudentProject studentProject : studentProjects) {
        studentProjectDAO.archive(studentProject);
      }
    }
    
    // Archive course students of archived student
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    List<CourseStudent> courseStudents = courseStudentDAO.listByStudent(student);
    if (!courseStudents.isEmpty()) {
      for (CourseStudent courseStudent : courseStudents) {
        courseStudentDAO.archive(courseStudent);
      }
    }
    
    // Archive student group users of archived student
    StudentGroupUserDAO studentGroupUserDAO = DAOFactory.getInstance().getStudentGroupUserDAO();
    List<StudentGroupUser> studentGroupUsers = studentGroupUserDAO.listByStudent(student, false, false);
    if (!studentGroupUsers.isEmpty()) {
      for (StudentGroupUser studentGroupUser : studentGroupUsers) {
        studentGroupUserDAO.delete(studentGroupUser);
      }
    }
    
    // #4226: Remove application of archived student (rare)
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Application application = applicationDAO.findByStudent(student);
    if (application != null) {
      ApplicationUtils.deleteApplication(requestContext.getLoggedUserId(), application, "archive student");
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
