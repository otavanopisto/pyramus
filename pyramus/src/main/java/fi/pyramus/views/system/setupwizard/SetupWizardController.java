package fi.pyramus.views.system.setupwizard;

import org.apache.commons.lang3.StringUtils;

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
    try {
      if (isInitialized(requestContext)) {
        nextPage(requestContext);
        return;
      }
    } catch (SetupWizardException e) {
      throw new SmvcRuntimeException(e);
    }
    
    SetupWizardPageFlowController flowPageController = SetupWizardPageFlowController.getInstance();
    
    requestContext.getRequest().setAttribute("setupPhase", phase);
    
    requestContext.getRequest().setAttribute("phaseIndex", flowPageController.getPhaseIndex(phase));
    requestContext.getRequest().setAttribute("phaseCount", flowPageController.getPhaseCount());
    requestContext.getRequest().setAttribute("phases", flowPageController.getPhases());
    
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
    
    nextPage(requestContext);
  }
  
  private void nextPage(PageRequestContext requestContext) {
    SetupWizardPageFlowController flowPageController = SetupWizardPageFlowController.getInstance();
    
    String redirectPage = "/";
    String next = flowPageController.next(phase);
    if (StringUtils.isNotBlank(next)) {
      redirectPage = "/system/setupwizard/" + next + ".page";
    }
    
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + redirectPage);
  }
  
  public abstract void setup(PageRequestContext requestContext) throws SetupWizardException;
  public abstract void save(PageRequestContext requestContext) throws SetupWizardException;
  public abstract boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException;

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    // TODO: Implement
    super.authorize(requestContext);
  }
  
  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }
  
  private String phase;
}
