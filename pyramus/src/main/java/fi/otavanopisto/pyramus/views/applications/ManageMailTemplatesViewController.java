package fi.otavanopisto.pyramus.views.applications;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ManageMailTemplatesViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/applications/management-mail-templates.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
