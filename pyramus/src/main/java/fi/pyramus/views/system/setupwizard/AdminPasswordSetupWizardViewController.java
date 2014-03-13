package fi.pyramus.views.system.setupwizard;

import org.apache.commons.codec.digest.DigestUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.NationalityDAO;
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
    // ADMIN_INTERNALAUTH_ID_HERE defined in initialdata.xml
    User user = userDAO.findByExternalIdAndAuthProvider("ADMIN_INTERNALAUTH_ID_HERE", "internal");
    userDAO.updateExternalId(user, String.valueOf(internalAuth.getId()));
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    return userDAO.findByExternalIdAndAuthProvider("ADMIN_INTERNALAUTH_ID_HERE", "internal") == null;
  }
}
