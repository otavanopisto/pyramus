package fi.otavanopisto.pyramus.security.impl;

import java.util.Locale;

import javax.enterprise.context.RequestScoped;

import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.security.ContextReference;

@RequestScoped
public class SessionControllerDelegateImpl implements SessionControllerDelegate {

  @Override
  public Locale getLocale() {
    return implementation.getLocale();
  }

  @Override
  public void setLocale(Locale locale) {
    implementation.setLocale(locale);
  }

  @Override
  public User getUser() {
    return implementation.getUser();
  }

  @Override
  public boolean isLoggedIn() {
    return implementation.isLoggedIn();
  }

  @Override
  public void logout() {
    implementation.logout();
  }
  
  @Override
  public boolean hasEnvironmentPermission(String permission) {
    return implementation.hasEnvironmentPermission(permission);
  }

//  @Override
//  public boolean hasCoursePermission(String permission, WorkspaceEntity course) {
//    return implementation.hasCoursePermission(permission, course);
//  }
//
//  @Override
//  public boolean hasResourcePermission(String permission, ResourceEntity resource) {
//    return implementation.hasResourcePermission(permission, resource);
//  }

//  @Override
//  public <T> List<T> filterResources(List<T> list, String permissions) {
//    return implementation.filterResources(list, permissions);
//  }

  public void setImplementation(SessionController implementation) {
    this.implementation = implementation;
  }

  @Override
  public boolean hasPermission(String permission, ContextReference contextReference) {
    return implementation.hasPermission(permission, contextReference);
  }

//  @Override
//  public void addOAuthAccessToken(String strategy, Date expiresAt, String accessToken) {
//    implementation.addOAuthAccessToken(strategy, expiresAt, accessToken);
//  }

//  @Override
//  public AccessToken getOAuthAccessToken(String strategy) {
//    return implementation.getOAuthAccessToken(strategy);
//  }
  
  private SessionController implementation;
}
