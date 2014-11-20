package fi.pyramus.security.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.muikku.security.ContextReference;
import fi.muikku.security.Identity;

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
    return sessionController.isSuperuser();
  }

  @Override
  public boolean hasPermission(String permission, ContextReference contextReference) {
    return sessionController.hasPermission(permission, contextReference);
  }

}
