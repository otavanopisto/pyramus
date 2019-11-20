package fi.otavanopisto.pyramus.security.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.Identity;

@ApplicationScoped
public class IdentityImpl implements Identity {

  @Inject
  private SessionController sessionController;
  
  @Override
  public boolean isLoggedIn() {
    return sessionController.isLoggedIn();
  }

  @Override
  public boolean isAdmin() {
    return false;
  }

  @Override
  public boolean hasPermission(String permission, ContextReference contextReference) {
    return sessionController.hasPermission(permission, contextReference);
  }

}
