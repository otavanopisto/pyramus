package fi.otavanopisto.pyramus.rest.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;

@RequestScoped
public class ClientApplicationController {

  @Inject
  private HttpServletRequest httpRequest;
  
  @Inject
  private OauthController oauthController;
  
  public ClientApplication getClientApplication() {
    ClientApplicationAccessToken clientApplicationAccessToken = oauthController.getAccessTokenFromRequest(httpRequest);
    return clientApplicationAccessToken != null ? clientApplicationAccessToken.getClientApplication() : null;
  }

}
