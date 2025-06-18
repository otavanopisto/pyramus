package fi.otavanopisto.pyramus.rest.oauth;

import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class CXFOAuthInitializer {
  
  @Inject
  private PyramusOAuthDataProvider pyramusOAuthDataProvider;

  @Produces
  public AuthorizationCodeGrantService authorizationCodeGrantService() {
    System.out.println("INITIALIZING authorizationCodeGrantService()");

    AuthorizationCodeGrantService service = new AuthorizationCodeGrantService();
    service.setDataProvider(pyramusOAuthDataProvider);
    return service;
  }

  @Produces
  public AccessTokenService accessTokenService() {
    System.out.println("INITIALIZING accessTokenService()");

    AccessTokenService service = new AccessTokenService();
    service.setDataProvider(pyramusOAuthDataProvider);
    return service;
  }
}
