package fi.otavanopisto.pyramus.rest.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

//import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
//import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;

@RequestScoped
public class ClientApplicationController {

  @Inject
  private HttpServletRequest httpRequest;
  
  @Inject
  private OauthController oauthController;
  
  public ClientApplication getClientApplication() {
//    try {
//      OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(httpRequest, ParameterStyle.HEADER);
//      String accessToken = oauthRequest.getAccessToken();
//  
//      ClientApplicationAccessToken clientApplicationAccessToken = oauthController.findByAccessToken(accessToken);
//      
//      return clientApplicationAccessToken.getClientApplication();
//    } catch (Exception ex) {
      return null;
//    }
  }

}
