package fi.pyramus.views.system.setupwizard;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.PersonDAO;
import fi.pyramus.dao.users.InternalAuthDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.dao.users.UserIdentificationDAO;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.users.InternalAuth;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.UserRole;

public class AdminPasswordSetupWizardViewController extends SetupWizardController {
  
  public AdminPasswordSetupWizardViewController() {
    super("adminpassword");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    String username = requestContext.getString("username");
    String password = requestContext.getString("password1");
    String firstName = requestContext.getString("firstName");
    String lastName = requestContext.getString("lastName");
    String passwordMD5 = DigestUtils.md5Hex(password);
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    
    InternalAuth internalAuth = internalAuthDAO.create(username, passwordMD5);
    Person person = personDAO.create(null, null, null, null, Boolean.FALSE);
    userIdentificationDAO.create(person, "internal", String.valueOf(internalAuth.getId()));
    User user = userDAO.create(firstName, lastName, Role.ADMINISTRATOR, person);
    personDAO.updateDefaultUser(person, user);
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    return userDAO.listAll().size() > 0;
  }

  @Override
  public UserRole[] getAllowedRoles() {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    List<StaffMember> users = userDAO.listAll();
    if (users.size() == 0) {
      return new UserRole[] { UserRole.EVERYONE }; 
    } else {
      return super.getAllowedRoles();
    }
  }
}
