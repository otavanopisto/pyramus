package fi.otavanopisto.pyramus.json.studentparents;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentRegistrationDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentRegistration;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;

/**
 * The controller responsible of logging in the user with the credentials he has provided. 
 * 
 * @see fi.otavanopisto.pyramus.views.users.LoginViewController
 */
public class RegisterStudentParentJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    String hash = requestContext.getString("hash");
    String username = StringUtils.trim(requestContext.getString("username"));
    String password1 = StringUtils.trim(requestContext.getString("password1"));
    String password2 = StringUtils.trim(requestContext.getString("password2"));
    String ssnConfirm = StringUtils.trim(requestContext.getString("ssnConfirm"));

    if (!StringUtils.equals(password1, password2)) {
      throw new SmvcRuntimeException(PyramusStatusCode.PASSWORD_MISMATCH, "Password confirmation didn't match.");
    }

    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    StudentParentDAO studentParentDAO = DAOFactory.getInstance().getStudentParentDAO();
    StudentParentRegistrationDAO studentParentRegistrationDAO = DAOFactory.getInstance().getStudentParentRegistrationDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();

    StudentParentRegistration studentParentRegistration = studentParentRegistrationDAO.findByHash(hash);

    // Validate
    if (!StringUtils.equals(ssnConfirm, studentParentRegistration.getStudent().getPerson().getSocialSecurityNumber())) {
      throw new SmvcRuntimeException(PyramusStatusCode.VALIDATION_FAILURE, "SSN confirmation failed.");
    }

    // Require email to be unique
    boolean unique = true;
    if (!UserUtils.isAllowedEmail(studentParentRegistration.getEmail(), unique)) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Email is already in use.");
    }
    
    StudentParent studentParent = studentParentDAO.create(studentParentRegistration.getFirstName(), studentParentRegistration.getLastName(), Arrays.asList(studentParentRegistration.getStudent()));
    ContactType contactType = contactTypeDAO.findById(1L);
    emailDAO.create(studentParent.getContactInfo(), contactType, true, studentParentRegistration.getEmail()).getId(); 

    InternalAuthenticationProvider internalAuthenticationProvider = AuthenticationProviderVault.getInstance().getInternalAuthenticationProvider("internal");
    if (internalAuthenticationProvider != null) {
      String externalId = internalAuthenticationProvider.createCredentials(username, password1);
      userIdentificationDAO.create(studentParent.getPerson(), internalAuthenticationProvider.getName(), externalId);
    }
    
    studentParentRegistrationDAO.delete(studentParentRegistration);
    
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
