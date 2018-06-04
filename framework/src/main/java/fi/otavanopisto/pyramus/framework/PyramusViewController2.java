package fi.otavanopisto.pyramus.framework;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageController;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;

public abstract class PyramusViewController2 implements PageController {

  public PyramusViewController2(boolean requireLoggedIn) {
    this.requireLoggedIn = requireLoggedIn;
  }
  
  protected abstract boolean checkAccess(RequestContext requestContext);

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    if (requireLoggedIn && !requestContext.isLoggedIn()) {
      throwLoginRequiredException(requestContext);
    }
    
    if (!checkAccess(requestContext)) {
      throw new AccessDeniedException(requestContext.getRequest().getLocale());
    }
  }

  protected void throwLoginRequiredException(RequestContext requestContext) {
    HttpServletRequest request = requestContext.getRequest();
    StringBuilder currentUrl = new StringBuilder(request.getRequestURL());
    String queryString = request.getQueryString();
    if (!StringUtils.isBlank(queryString)) {
      currentUrl.append('?');
      currentUrl.append(queryString);
    }
    throw new LoginRequiredException(currentUrl.toString());
  }
  
  protected void setJsDataVariable(PageRequestContext pageRequestContext, String name, String value) {
    @SuppressWarnings("unchecked")
    Map<String, String> jsData = (Map<String, String>) pageRequestContext.getRequest().getAttribute("jsData");
    if (jsData == null) {
      jsData = new HashMap<>();
      pageRequestContext.getRequest().setAttribute("jsData", jsData);
    }
    
    jsData.put(name, value);
  }
  
  protected void sendError(RequestContext requestContext, int response) {
    try {
      requestContext.getResponse().sendError(response);
    }
    catch (IOException e) {
    }
  }

  private boolean requireLoggedIn;
}
