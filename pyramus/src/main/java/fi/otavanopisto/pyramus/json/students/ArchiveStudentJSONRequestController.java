package fi.otavanopisto.pyramus.json.students;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveStudentJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    Long studentId = requestContext.getLong("student");
    Student student = studentDAO.findById(studentId);
    studentDAO.archive(student);
    // #4226: Remove application of archived student (rare)
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Application application = applicationDAO.findByStudent(student);
    if (application != null) {
      ApplicationUtils.deleteApplication(application);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
