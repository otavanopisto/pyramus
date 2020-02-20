package fi.otavanopisto.pyramus.json.students;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.CDI;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.koski.KoskiClient;

public class ChangeStudentStudyProgrammeJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ChangeStudentStudyProgrammeJSONRequestController.class.getName());
  
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();

    StaffMember loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());
    
    Long studentId = requestContext.getLong("studentId");
    Long studyProgrammeId = requestContext.getLong("studyProgrammeId");
    Student student = studentDAO.findById(studentId);
    StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
    
    if (!(UserUtils.canAccessOrganization(loggedUser, student.getOrganization()) && 
        UserUtils.canAccessOrganization(loggedUser, studyProgramme.getOrganization()))) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Invalid studyprogramme.");
    }

    KoskiClient client = CDI.current().select(KoskiClient.class).get();
    try {
      client.invalidateAllStudentOIDs(student);
    } catch (Exception ex) {
      logger.log(Level.SEVERE, String.format("Invalidation of study permits for student %d failed", studentId), ex);
    }
    
    studentDAO.updateStudyProgramme(student, studyProgramme);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}