package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.UserRole;

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
