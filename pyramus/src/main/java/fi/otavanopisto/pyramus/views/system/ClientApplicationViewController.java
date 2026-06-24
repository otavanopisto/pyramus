package fi.otavanopisto.pyramus.views.system;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.OauthClientSecretGenerator;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableFacade;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableRowFacade;

public class ClientApplicationViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    ClientApplicationDAO clientApplicationDAO = DAOFactory.getInstance().getClientApplicationDAO();
    
    Long clientApplicationId = requestContext.getLong("clientApplicationId");
    if (clientApplicationId != null) {
      ClientApplication clientApplication = clientApplicationDAO.findById(clientApplicationId);
      requestContext.getRequest().setAttribute("clientApplication", clientApplication);
    }
    
    requestContext.setIncludeJSP("/templates/system/clientapplication.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    ClientApplicationDAO clientApplicationDAO = DAOFactory.getInstance().getClientApplicationDAO();

    Long id = requestContext.getLong("clientApplicationId");
    String clientName = requestContext.getString("clientName");
    boolean active = "1".equals(requestContext.getString("active"));
    boolean skipPrompt = "1".equals(requestContext.getString("skipPrompt"));
    boolean allowAllRedirectURIs = "1".equals(requestContext.getString("allowAllRedirectURIs"));
    boolean regenerateSecret = "1".equals(requestContext.getString("regenerateSecret"));
    String[] scopesArr = StringUtils.split(requestContext.getString("scopes"), ',');
    Set<String> scopes = scopesArr != null ? Set.of(scopesArr) : new HashSet<>();
    Set<String> redirectURIs = new HashSet<>();

    PyramusIxTableFacade redirectURIsTable = PyramusIxTableFacade.from(requestContext, "redirectURIsTable");
    for (PyramusIxTableRowFacade redirectURIRow : redirectURIsTable.rows()) {
      String redirectURI = StringUtils.trim(redirectURIRow.getString("redirectURI"));
      if (StringUtils.isNotBlank(redirectURI)) {
        redirectURIs.add(redirectURI);
      }
    }

    if (id == null) {
      String clientId = UUID.randomUUID().toString();
      String clientSecret = new OauthClientSecretGenerator(80).nextString();
      clientApplicationDAO.create(clientName, active, clientId, clientSecret, skipPrompt, scopes, allowAllRedirectURIs, redirectURIs);
    }
    else {
      ClientApplication clientApplication = clientApplicationDAO.findById(id);
      clientApplication = clientApplicationDAO.updateActive(clientApplication, active);
      clientApplication = clientApplicationDAO.updateName(clientApplication, clientName);
      clientApplication = clientApplicationDAO.updateSkipPrompt(clientApplication, skipPrompt);
      clientApplication = clientApplicationDAO.updateScopes(clientApplication, scopes);
      clientApplication = clientApplicationDAO.updateRedirectURIs(clientApplication, allowAllRedirectURIs, redirectURIs);
      
      if (regenerateSecret) {
        String clientSecret = new OauthClientSecretGenerator(80).nextString();
        clientApplicationDAO.updateClientSecret(clientApplication, clientSecret);
      }
    }
    
    processForm(requestContext);
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }
}
