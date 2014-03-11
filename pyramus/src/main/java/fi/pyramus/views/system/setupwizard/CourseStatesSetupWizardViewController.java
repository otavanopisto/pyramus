package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;

public class CourseStatesSetupWizardViewController extends PyramusFormViewController {
  
  @Override
  public void processForm(PageRequestContext requestContext) {
    requestContext.getRequest().setAttribute("setupPhase", "coursestates");    
    requestContext.setIncludeJSP("/templates/system/setupwizard/coursestates.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
