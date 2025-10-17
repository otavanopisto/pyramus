package fi.otavanopisto.pyramus.rest.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.users.StudentParentChildDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentInvitationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentInvitation;
import fi.otavanopisto.pyramus.mailer.Mailer;

@Dependent
@Stateless
public class StudentParentController {

  @Inject
  private StudentParentDAO studentParentDAO;

  @Inject
  private StudentParentChildDAO studentParentChildDAO;

  @Inject
  private StudentParentInvitationDAO studentParentInvitationDAO;

  /* StudentParent */
  
  public StudentParent findStudentParentById(Long id) {
    return studentParentDAO.findById(id);
  }
  
  public StudentParent findStudentParentByEmail(String email) {
    return studentParentDAO.findByUniqueEmail(email);
  }
  
  public List<StudentParent> listStudentParents(Integer firstResult, Integer maxResults) {
    return studentParentDAO.listAll(firstResult, maxResults);
  }

  public List<StudentParent> listStudentParentsByPerson(Person person) {
    return studentParentDAO.listBy(person);
  }

  /* StudentParentChild */
  
  public StudentParentChild findStudentParentChild(StudentParent studentParent, Student student) {
    return studentParentChildDAO.findBy(studentParent, student);
  }

  public void deleteStudentParentChild(StudentParentChild studentParentChild) {
    studentParentChildDAO.delete(studentParentChild);
  }

  /* StudentParentInvitation */

  public StudentParentInvitation findInvitationById(Long invitationId) {
    return studentParentInvitationDAO.findById(invitationId);
  }

  public void deleteInvitation(StudentParentInvitation invitation) {
    studentParentInvitationDAO.delete(invitation);
  }

  /**
   * Refreshes the hash and date of the invitation. Typically you
   * then send the information to the invitee.
   * 
   * @param invitation invitation
   * @return updated invitation
   */
  public StudentParentInvitation refreshInvitation(StudentParentInvitation invitation) {
    return studentParentInvitationDAO.updateHashAndDate(invitation, generateHash(), new Date());
  }

  /**
   * Sends the given invitation as an email.
   * 
   * @param invitation the invitation
   * @param request for determining the path to the registration form
   * @throws Exception if something goes wrong
   */
  public void sendInvitationEmail(StudentParentInvitation invitation, HttpServletRequest request) throws Exception {
    String guardianEmailContent = IOUtils.toString(request.getServletContext().getResourceAsStream(
        "/templates/applications/mails/mail-credentials-guardian-create.html"), "UTF-8");
    
    StringBuffer guardianCreateCredentialsLink = new StringBuffer(ApplicationUtils.getRequestURIRoot(request));
    guardianCreateCredentialsLink.append("/parentregister.page?c=");
    guardianCreateCredentialsLink.append(invitation.getHash());
    
    String subject = "Muikku-oppimisympäristön tunnukset";
    String content = String.format(guardianEmailContent, invitation.getFirstName(), guardianCreateCredentialsLink.toString());
    
    Mailer.sendMail(
        Mailer.JNDI_APPLICATION,
        Mailer.HTML,
        null,
        Collections.singleton(invitation.getEmail()),
        Collections.emptySet(),
        Collections.emptySet(),
        subject,
        content,
        Collections.emptyList(),
        null);
  }
  
  private String generateHash() {
    return UUID.randomUUID().toString();
  }

}
