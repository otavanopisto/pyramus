package fi.pyramus.plugin.auth;

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
}
