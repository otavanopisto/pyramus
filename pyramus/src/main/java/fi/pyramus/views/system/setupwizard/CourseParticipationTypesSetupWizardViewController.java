package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;

public class CourseParticipationTypesSetupWizardViewController extends PyramusFormViewController {
  
  @Override
  public void processForm(PageRequestContext requestContext) {
    requestContext.getRequest().setAttribute("setupPhase", "courseparticipationtypes");    
    requestContext.setIncludeJSP("/templates/system/setupwizard/courseparticipationtypes.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
