package fi.pyramus.views.system;

import java.util.Date;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class SystemInfoViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    
    requestContext.getRequest().setAttribute("properties", System.getProperties());
    requestContext.getRequest().setAttribute("env", System.getenv());
    requestContext.getRequest().setAttribute("date", new Date());
    requestContext.getRequest().setAttribute("freeMemory", Runtime.getRuntime().freeMemory());
    requestContext.getRequest().setAttribute("totalMemory", Runtime.getRuntime().totalMemory());
    requestContext.getRequest().setAttribute("availableProcessors", Runtime.getRuntime().availableProcessors());

    
    requestContext.setIncludeJSP("/templates/system/systeminfo.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
