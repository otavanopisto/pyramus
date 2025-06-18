package fi.otavanopisto.pyramus.rest;

import java.util.HashSet;
import java.util.Set;

import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/1")
public class JaxRsActivator extends Application {
  
//  @Override
//  public Set<Class<?>> getClasses() {
//    Set<Class<?>> classes = new HashSet<>();
//    classes.add(AuthorizationCodeGrantService.class);
//    classes.add(AccessTokenService.class);
//    // Add other endpoints if needed
//    return classes;
//  }
  
}
