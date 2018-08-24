package fi.otavanopisto.pyramus.framework;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.RequestContext;

public abstract class JSONRequestController2 implements fi.internetix.smvc.controllers.JSONRequestController {

  public JSONRequestController2(boolean requireLoggedIn) {
    this.requireLoggedIn = requireLoggedIn;
  }
  
  protected abstract boolean checkAccess(RequestContext requestContext);

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    if (requireLoggedIn && !requestContext.isLoggedIn()) {
      throw new LoginRequiredException();
    }
    
    if (!checkAccess(requestContext)) {
      throw new AccessDeniedException(requestContext.getRequest().getLocale());
    }
  }

  private boolean requireLoggedIn;
}
