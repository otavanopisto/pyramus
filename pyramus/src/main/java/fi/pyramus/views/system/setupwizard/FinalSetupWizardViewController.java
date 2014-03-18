package fi.pyramus.views.system.setupwizard;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.SystemDAO;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.DataImporter;

public class FinalSetupWizardViewController extends SetupWizardController {
  
  public FinalSetupWizardViewController() {
    super("final");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    return false;
  }
  
  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE }; 
  }
}
