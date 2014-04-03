package fi.pyramus.views.system.setupwizard;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.InternalAuthDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.InternalAuth;
import fi.pyramus.domainmodel.users.Role;
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
    String passwordMD5 = DigestUtils.md5Hex(password);
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
    
    InternalAuth internalAuth = internalAuthDAO.create(username, passwordMD5);
    User user = userDAO.create("Admin", "Admin", String.valueOf(internalAuth.getId()), "internal", Role.ADMINISTRATOR);
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    return userDAO.listAll().size() > 0;
  }

  @Override
  public UserRole[] getAllowedRoles() {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    List<User> users = userDAO.listAll();
    if (users.size() == 0) {
      return new UserRole[] { UserRole.EVERYONE }; 
    } else {
      return super.getAllowedRoles();
    }
  }
}
