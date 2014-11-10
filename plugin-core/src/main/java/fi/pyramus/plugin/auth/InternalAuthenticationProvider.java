package fi.pyramus.plugin.auth;

import fi.pyramus.domainmodel.users.StaffMember;

/**
 * Defines requirements for a class capable of authorizing users with username & password combination.
 */
public interface InternalAuthenticationProvider extends AuthenticationProvider {

  /**
   * Returns the user corresponding to the given credentials. If no user cannot be found, returns
   * <code>null</code>.
   * 
   * @param username The username
   * @param password The password
   * 
   * @return The user corresponding to the given credentials, or <code>null</code> if not found
   */
  public StaffMember getUser(String username, String password) throws AuthenticationException;
  
  /**
   * Returns the username of a user corresponding to the given identifier, or <code>null</code> if
   * not found.
   * 
   * @param externalId The user identifier
   * 
   * @return The username of the user corresponding to the given identifier, or <code>null</code> if
   * not found
   */
  public String getUsername(String externalId);

  /**
   * Returns whether this authentication provider is capable of updating the credentials of a user.
   * 
   * @return <code>true</code> if this authentication provider can update credentials, otherwise <code>false</code>
   */
  public boolean canUpdateCredentials();
  
  /**
   * Creates new credentials
   * 
   * @param username The new username of the user
   * @param password The new password of the user
   * 
   * @return external user identifier
   */
  public String createCredentials(String username, String password);

  /**
   * Updates the username of the user corresponding to the given identifer.
   * 
   * @param externalId The user identifier
   * @param username The new username of the user
   */
  public void updateUsername(String externalId, String username);
  
  /**
   * Updates the password of the user corresponding to the given identifer.
   * 
   * @param externalId The user identifier
   * @param password The new password of the user
   */
  public void updatePassword(String externalId, String password);
}
