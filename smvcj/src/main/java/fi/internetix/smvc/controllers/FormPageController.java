package fi.internetix.smvc.controllers;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageRequestContext;

/** The base class for all page controllers containing regular (non-JSON) forms.
 * 
 *
 */
public abstract class FormPageController implements PageController {
  
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
  }

  public void process(PageRequestContext pageRequestContext) {
    if ("POST".equals(pageRequestContext.getRequest().getMethod())) {
      processSend(pageRequestContext);
    }
    else {
      processForm(pageRequestContext);
    }
  }
  
  public abstract void processForm(PageRequestContext requestContext);
  public abstract void processSend(PageRequestContext requestContext);
}
