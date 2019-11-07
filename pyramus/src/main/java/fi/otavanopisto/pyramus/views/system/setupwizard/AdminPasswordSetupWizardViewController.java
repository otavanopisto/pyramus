package fi.otavanopisto.pyramus.views.system.setupwizard;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.users.InternalAuthDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Defaults;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.UserRole;

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
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    Defaults defaults = defaultsDAO.getDefaults();
    
    InternalAuth internalAuth = internalAuthDAO.create(username, passwordMD5);
    Person person = personDAO.create(null, null, null, null, Boolean.FALSE);
    userIdentificationDAO.create(person, "internal", String.valueOf(internalAuth.getId()));
    User user = userDAO.create(defaults.getOrganization(), firstName, lastName, Role.ADMINISTRATOR, person, false);
    personDAO.updateDefaultUser(person, user);
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    return !userDAO.listAll().isEmpty();
  }

  @Override
  public UserRole[] getAllowedRoles() {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    List<StaffMember> users = userDAO.listAll();
    if (users.isEmpty()) {
      return new UserRole[] { UserRole.EVERYONE }; 
    } else {
      return super.getAllowedRoles();
    }
  }
}
