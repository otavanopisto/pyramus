package fi.otavanopisto.pyramus.framework;

import fi.internetix.smvc.controllers.PageRequestContext;

public abstract class PyramusFormViewController extends PyramusViewController {

  public void process(PageRequestContext pageRequestContext) {
    if ("POST".equals(pageRequestContext.getRequest().getMethod())) {
      processSend(pageRequestContext);
    }
    else {
      processForm(pageRequestContext);
    }
  }
  
  public abstract UserRole[] getAllowedRoles();
  public abstract void processForm(PageRequestContext requestContext);
  public abstract void processSend(PageRequestContext requestContext);
}
