package fi.pyramus.views.system;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;
import fi.pyramus.views.system.setupwizard.EducationTypesSetupWizardHandler;
import fi.pyramus.views.system.setupwizard.SetupWizardHandler;
import fi.pyramus.views.system.setupwizard.SetupWizardHandlerFactory;

public class SetupWizardViewController extends PyramusFormViewController {
  
  @Override
  public void processForm(PageRequestContext requestContext) {
    HttpServletRequest req = requestContext.getRequest();
    String setupPhase = req.getParameter("setupPhase");
    validateSetupPhase(setupPhase);
    populateSetupPhase(setupPhase, requestContext);
    req.setAttribute("setupPhase", req.getParameter("setupPhase"));
    requestContext.setIncludeJSP("/templates/system/setupwizard/" + setupPhase + ".jsp");
  }

  private void validateSetupPhase(String setupPhase) {
  }

  private void populateSetupPhase(String setupPhase, PageRequestContext pageRequestContext) {
    this.setJsDataVariable(pageRequestContext,
                           setupPhase,
                           SetupWizardHandlerFactory.INSTANCE
                                                  .getHandlerFor(setupPhase)
                                                  .populate(pageRequestContext));
  }

  @Override
  public void processSend(PageRequestContext arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
