package fi.otavanopisto.pyramus.views.applications;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;

public class CreateCredentialsViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(CreateCredentialsViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    try {
      
      if (StringUtils.equals(pageRequestContext.getString("status"), "ok")) {
        pageRequestContext.getRequest().setAttribute("credentialsCreated", Boolean.TRUE);
      }
      else {
        
        // Validate request data
        
        String applicationId = pageRequestContext.getString("a");
        String credentialToken = pageRequestContext.getString("t");
        if (StringUtils.isAnyBlank(applicationId, credentialToken)) {
          logger.log(Level.WARNING, "Missing applicationId and/or credentialToken");
          pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
          return;
        }
        pageRequestContext.getRequest().setAttribute("applicationId", applicationId);
        pageRequestContext.getRequest().setAttribute("credentialToken", credentialToken);
        
        // Validate application
        
        ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
        Application application = applicationDAO.findByApplicationId(applicationId);
        if (application == null) {
          logger.log(Level.WARNING, String.format("Application %s not found", applicationId));
          pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
          return;
        }
        if (application.getStudent() == null) {
          logger.log(Level.WARNING, String.format("Application %s lacks student", applicationId));
          pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
          return;
        }
        if (!StringUtils.equals(credentialToken,  application.getCredentialToken())) {
          logger.log(Level.SEVERE, String.format("Application %s token mismatch. Expected %s got %s",
              applicationId, credentialToken, application.getCredentialToken()));
          pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
          return;
        }
        
        // Check credential state
  
        List<InternalAuthenticationProvider> providers = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
        InternalAuthenticationProvider provider = providers.size() == 1 ? providers.get(0) : null;
        if (provider == null || !provider.canUpdateCredentials()) {
          logger.log(Level.WARNING, "Unable to resolve InternalAuthenticationProvid");
          pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
          return;
        }
        UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
        UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(
            provider.getName(),
            application.getStudent().getPerson());
        if (userIdentification != null) {
          pageRequestContext.getRequest().setAttribute("credentialsAlreadyExist", Boolean.TRUE); 
        }
      }
      pageRequestContext.setIncludeJSP("/templates/applications/createcredentials.jsp");
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Error serving errors", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
