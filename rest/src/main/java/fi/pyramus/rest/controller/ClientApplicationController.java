package fi.pyramus.rest.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import fi.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;

@RequestScoped
public class ClientApplicationController {

  @Inject
  private HttpServletRequest httpRequest;
  
  @Inject
  private OauthController oauthController;
  
  public ClientApplication getClientApplication() {
    try {
      OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(httpRequest, ParameterStyle.HEADER);
      String accessToken = oauthRequest.getAccessToken();
  
      ClientApplicationAccessToken clientApplicationAccessToken = oauthController.findByAccessToken(accessToken);
      
      return clientApplicationAccessToken.getClientApplication();
    } catch (Exception ex) {
      return null;
    }
  }

}
