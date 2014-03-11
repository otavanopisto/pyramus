package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;

public abstract class SetupWizardController extends PyramusFormViewController {
  
  public SetupWizardController(String phase) {
    this.phase = phase;
  }
  
  @Override
  public void processForm(PageRequestContext requestContext) {
    requestContext.getRequest().setAttribute("setupPhase", phase);
    requestContext.setIncludeJSP("/templates/system/setupwizard/" + phase + ".jsp");
    
    try {
      setup(requestContext);
    } catch (SetupWizardException e) {
      throw new SmvcRuntimeException(e);
    }
    
  }
  
  @Override
  public void processSend(PageRequestContext requestContext) {
    try {
      save(requestContext);
    } catch (SetupWizardException e) {
      throw new SmvcRuntimeException(e);
    }
    
    nextPage();
  }
  
  private void nextPage() {
    // TODO: Implement 
  }
  
  public abstract void setup(PageRequestContext requestContext) throws SetupWizardException;
  public abstract void save(PageRequestContext requestContext) throws SetupWizardException;

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    // TODO: Implement
    super.authorize(requestContext);
  }
  
  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }
  
  private String phase;
}
