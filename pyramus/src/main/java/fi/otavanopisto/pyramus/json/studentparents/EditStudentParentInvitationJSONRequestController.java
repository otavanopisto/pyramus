package fi.otavanopisto.pyramus.json.studentparents;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentInvitationDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentInvitation;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.mailer.Mailer;

public class EditStudentParentInvitationJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentParentInvitationDAO studentParentInvitationDAO = DAOFactory.getInstance().getStudentParentInvitationDAO();
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

    if (student.getPerson().getBirthday() == null || !student.getPerson().isUnderAge()) {
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "Student is not underage.");
    }
    
    String firstName = StringUtils.trim(requestContext.getString("firstName"));
    String lastName = StringUtils.trim(requestContext.getString("lastName"));
    String email = StringUtils.trim(requestContext.getString("email"));

    String hash = UUID.randomUUID().toString();
    StudentParentInvitation guardian = studentParentInvitationDAO.create(firstName, lastName, email, student, hash);

    // Send mail
    
    try {
      HttpServletRequest request = requestContext.getRequest();
      String guardianEmailContent = IOUtils.toString(request.getServletContext().getResourceAsStream(
          "/templates/applications/mails/mail-credentials-guardian-create.html"), "UTF-8");
      
      StringBuffer guardianCreateCredentialsLink = new StringBuffer(ApplicationUtils.getRequestURIRoot(request));
      guardianCreateCredentialsLink.append("/parentregister.page?c=");
      guardianCreateCredentialsLink.append(guardian.getHash());
      
      String subject = "Muikku-oppimisympäristön tunnukset";
      String content = String.format(guardianEmailContent, guardian.getFirstName(), guardianCreateCredentialsLink.toString());
      
      Mailer.sendMail(
          Mailer.JNDI_APPLICATION,
          Mailer.HTML,
          null,
          guardian.getEmail(),
          subject,
          content);
    } catch (Exception ex) {
      throw new SmvcRuntimeException(ex);
    }
    
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    // TODO
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
