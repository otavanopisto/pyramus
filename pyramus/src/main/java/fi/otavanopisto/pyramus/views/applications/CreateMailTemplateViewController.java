package fi.otavanopisto.pyramus.views.applications;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.Feature;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class CreateMailTemplateViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/applications/createmailtemplate.jsp");
  }

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    if (!requestContext.hasFeature(Feature.APPLICATION_MANAGEMENT)) {
      throw new AccessDeniedException(requestContext.getRequest().getLocale());
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
