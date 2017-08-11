package fi.otavanopisto.pyramus.views.application;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class CreateApplicationViewController extends PyramusViewController {

  public void process(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/application/createapplication.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
