package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.framework.PyramusViewController;

public interface SetupWizardHandler {
  
  public String getPhaseName();
  
  public String populate(PageRequestContext requestContext);

}
