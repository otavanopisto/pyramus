package fi.pyramus.views.system;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class ElementCheatSheetViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    requestContext.setIncludeJSP("/templates/system/elementcheatsheet.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
