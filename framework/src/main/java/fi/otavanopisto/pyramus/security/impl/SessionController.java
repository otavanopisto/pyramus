package fi.otavanopisto.pyramus.security.impl;

import java.util.Locale;

import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.security.ContextReference;

public interface SessionController {

  /**
   * Returns current locale
   * 
   * @return locale
   */
  public Locale getLocale();
  
  /**
   * Sets locale
   * 
   * @param locale
   */
  public void setLocale(Locale locale);
  
  /**
   * Returns logged user or null if user is not logged in
   * 
   * @return logged user or null if user is not logged in
   */
  public User getUser();
  
  /**
   * Returns whether user is logged in
   * 
   * @return whether user is logged in
   */
  public boolean isLoggedIn();
  
  /**
   * Logs user in
   * 
   * @param userId user id
   */
//  public void login(Long userId);
  
  /**
   * Logs user out 
   */
  public void logout();
  
  boolean hasPermission(String permission, ContextReference contextReference);
  
  /**
   * Returns whether logged user has specified environment permission.
   * @param permission requested permission
   * @return whether logged user has specified environment permission.
   */
  public boolean hasEnvironmentPermission(String permission);
  
}
