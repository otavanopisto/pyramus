package fi.otavanopisto.pyramus.json.applications;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.InternalAuthDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;

public class CreateCredentialsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    
    // Validation of request data and basic necessities
    
    String applicationId = StringUtils.trim(requestContext.getString("applicationId"));
    String credentialToken = StringUtils.trim(requestContext.getString("token"));
    String username = StringUtils.trim(requestContext.getString("username"));
    String password = StringUtils.trim(requestContext.getString("password"));
    List<InternalAuthenticationProvider> providers = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
    InternalAuthenticationProvider provider = providers.size() == 1 ? providers.get(0) : null;
    if (provider == null ||
        !provider.canUpdateCredentials() ||
        StringUtils.isAnyBlank(applicationId, username, password, credentialToken)) {
      fail(requestContext, "Sisäinen virhe");
      return;
    }
    
    // Validate application
    
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Application application = applicationDAO.findByApplicationId(applicationId);
    if (application == null ||
        application.getStudent() == null ||
        !StringUtils.equals(credentialToken, application.getCredentialToken())) {
      fail(requestContext, "Hakemus ei mahdollista tunnusten luontia");
      return;
    }
    
    // Validate student
    
    Person person = application.getStudent().getPerson();
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(provider.getName(), person);
    if (userIdentification != null) {
      fail(requestContext, "Käyttäjätilillä on jo tunnukset");
      return;
    }
    InternalAuth internalAuth = internalAuthDAO.findByUsername(username);
    if (internalAuth != null) {
      fail(requestContext, "Valittu käyttäjätunnus on jo varattu");
      return;
    }
    String externalId = provider.createCredentials(username, password);
    userIdentificationDAO.create(person, provider.getName(), externalId);
    requestContext.addResponseParameter("status", "OK");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

  private void fail(JSONRequestContext requestContext, String reason) {
    requestContext.addResponseParameter("status", "FAIL");
    requestContext.addResponseParameter("reason", reason);
  }

}
