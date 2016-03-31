package fi.otavanopisto.pyramus.plugin.auth;

import fi.internetix.smvc.controllers.RequestContext;

/**
 * Defines a base interface for all authentication interfaces
 */
public interface AuthenticationProvider {

  /**
   * Returns the name of this authentication provider.
   * 
   * @return The name of this authentication provider
   */
  public String getName();

  /**
   * Authentication provider can perform provider specific logout procedure
   * 
   * @return redirect url or null if logging out can proceed
   */
  public String logout(RequestContext requestContext);
}
