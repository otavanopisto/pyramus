package fi.otavanopisto.pyramus.json.students;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.CDI;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.koski.KoskiClient;
import fi.otavanopisto.pyramus.koski.KoskiController;

public class ArchiveStudentJSONRequestController extends JSONRequestController {
  
  private static final Logger logger = Logger.getLogger(ArchiveStudentJSONRequestController.class.getName());
  
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    
    StaffMember loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());
    
    Long studentId = requestContext.getLong("student");
    Student student = studentDAO.findById(studentId);

    boolean koskiInvalidationOk = false;
    
    KoskiClient koskiClient = CDI.current().select(KoskiClient.class).get();
    KoskiController koskiController = CDI.current().select(KoskiController.class).get();
    
    // If there are Koski references, we need to invalidate them 
    // and if there's errors, we need to abort the archiving
    if (koskiController.hasKoskiOIDs(student)) {
      try {
        koskiInvalidationOk = koskiClient.invalidateAllStudentOIDs(student);
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Invalidation threw an error", e);
      }
  
      if (!koskiInvalidationOk) {
        Locale locale = requestContext.getRequest().getLocale();
        throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, 
            Messages.getInstance().getText(locale, "students.editStudent.archiveStudentKoskiInvalidationFailedError"));
      }
    }
    
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
    StudentGroupStudentDAO studentGroupStudentDAO = DAOFactory.getInstance().getStudentGroupStudentDAO();
    List<StudentGroupStudent> studentGroupStudents = studentGroupStudentDAO.listByStudent(student, false);
    if (!studentGroupStudents.isEmpty()) {
      for (StudentGroupStudent studentGroupStudent : studentGroupStudents) {
        studentGroupStudentDAO.delete(studentGroupStudent);
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
