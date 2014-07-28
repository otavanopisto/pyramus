package fi.pyramus.views.generic;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class JSONErrorViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    String errorLevel = requestContext.getRequest().getParameter("errorLevel");
    if (!"1".equals(errorLevel) && !"2".equals(errorLevel) && !"3".equals(errorLevel) && !"4".equals(errorLevel)) {
      errorLevel = "4";
    }
    
    requestContext.getRequest().setAttribute("errorCode", requestContext.getRequest().getParameter("errorCode"));
    requestContext.getRequest().setAttribute("errorMessage", requestContext.getRequest().getParameter("errorMessage"));
    requestContext.getRequest().setAttribute("errorLevel", errorLevel);
    requestContext.getRequest().setAttribute("isHttpError", requestContext.getRequest().getParameter("isHttpError"));
    
    requestContext.setIncludeJSP("/templates/generic/jsonerror.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
