package fi.otavanopisto.pyramus.plugin.simple.auth;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.otavanopisto.pyramus.plugin.simple.dao.SimpleAuthDAO;
import fi.otavanopisto.pyramus.plugin.simple.domainmodel.users.SimpleAuth;

/** A simple user/password authentication provider. */
@SuppressWarnings("unused")
public class SimpleAuthenticationProvider implements InternalAuthenticationProvider {

  /** Returns the name of this authentication provider.
   * 
   * @return The name of this authentication provider.
   */
  public String getName() {
    return "simple";
  }

  /** Returns the user object corresponding to the specified
   * username and password.
   * 
   * @param username The username of the user to retrieve.
   * @param password The password of the user to retrieve.
   * @return The user with the specified username and password, or <code>null</code>
   * if the username and/or password is incorrect.
   */
  public User getUser(String username, String password) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    SimpleAuthDAO simpleAuthDAO = new SimpleAuthDAO();
    
    SimpleAuth simpleAuth = simpleAuthDAO.findByUserNameAndPassword(username, password);
    
    if (simpleAuth != null) {
      User user = userDAO.findByExternalIdAndAuthProvider(String.valueOf(simpleAuth.getId()), getName());
      return user;
    } else {
      return null;
    }
  }
  
  /** Returns the username of the user with the specified external ID.
   * 
   * @param externalId The external ID of the user to retrieve.
   * @return The user whose external ID is <code>externalId</code>
   */
  public String getUsername(String externalId) {
    SimpleAuthDAO simpleAuthDAO = new SimpleAuthDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    
    SimpleAuth simpleAuth = simpleAuthDAO.findById(Long.parseLong(externalId));
    
    if (simpleAuth != null)
      return  simpleAuth.getUsername();
    
    return null;
  }

  /** Returns <code>true</code> if user credentials can be updated.
   *  @return <code>true</code> if user credentials can be updated.
   */
  public boolean canUpdateCredentials() {
    return true;
  }
  
  /** Add a new user with specified username and password.
   * 
   * @param username The username of the new user.
   * @param password The password of the new user.
   * @return The external ID of the new user.
   */
  public String createCredentials(String username, String password) {
    SimpleAuthDAO simpleAuthDAO = new SimpleAuthDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    
    SimpleAuth simpleAuth = simpleAuthDAO.create(username, password);
    
    String externalId = simpleAuth.getId().toString();
  
    return externalId;
  }
  
  /** Changes a user's password to <code>password</code>.
   * 
   * @param externalId The external ID of the user to modify.
   * @param password The new password for the user.
   */
  public void updatePassword(String externalId, String password) {
    SimpleAuthDAO simpleAuthDAO = new SimpleAuthDAO();
    
    SimpleAuth simpleAuth = simpleAuthDAO.findById(Long.parseLong(externalId));
    
    simpleAuthDAO.updatePassword(simpleAuth, password);
  }
  
  /** Changes a user's username to <code>username</code>.
   * 
   * @param externalId The external ID of the user to modify.
   * @param username The new username for the user.
   */
  public void updateUsername(String externalId, String username) {
    SimpleAuthDAO simpleAuthDAO = new SimpleAuthDAO();
    
    SimpleAuth simpleAuth = simpleAuthDAO.findById(Long.parseLong(externalId));
    
    simpleAuthDAO.updateUsername(simpleAuth, username);
  }

}
