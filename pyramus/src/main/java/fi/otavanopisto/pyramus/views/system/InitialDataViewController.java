package fi.otavanopisto.pyramus.views.system;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class InitialDataViewController extends PyramusViewController {
  
  public void process(PageRequestContext requestContext) {
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}