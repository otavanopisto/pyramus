package fi.pyramus.rest.controller;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import fi.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;

@Dependent
@Stateless
public class ClientApplicationController {

  @Context
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
