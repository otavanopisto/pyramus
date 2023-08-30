package fi.otavanopisto.pyramus.json.studentparents;

import java.util.UUID;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentRegistrationDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;

public class CreateStudentParentRegistrationJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentParentRegistrationDAO studentParentRegistrationDAO = DAOFactory.getInstance().getStudentParentRegistrationDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    Long loggedUserId = requestContext.getLoggedUserId();
    final StaffMember loggedUser = staffMemberDAO.findById(loggedUserId);
    
    Student student = studentDAO.findById(requestContext.getLong("studentId"));

    // TODO "permission" check
    
    if (student.getOrganization() != null) {
      // Check that the editing user has access to the organization
      if (!UserUtils.canAccessOrganization(loggedUser, student.getOrganization())) {
        throw new RuntimeException("Cannot access users' organization");
      }
    } else {
      // Check that the editing user has generic access when users' organization is null
      if (!UserUtils.canAccessAllOrganizations(loggedUser)) {
        throw new RuntimeException("Cannot access users' organization");
      }
    }

    String firstName = requestContext.getString("firstName");
    String lastName = requestContext.getString("lastName");
    String email = requestContext.getString("email");

    
    // TODO move to controller and send email about the thing
    String hash = UUID.randomUUID().toString();
    studentParentRegistrationDAO.create(firstName, lastName, email, student, hash);
    
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    // TODO
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
