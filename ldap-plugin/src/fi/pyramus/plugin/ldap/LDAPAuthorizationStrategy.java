package fi.pyramus.plugin.ldap;

import java.io.UnsupportedEncodingException;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.plugin.auth.AuthenticationException;
import fi.pyramus.plugin.auth.InternalAuthenticationProvider;

/**
 * An authorization provider using a LDAP directory
 */
@SuppressWarnings("unused")
public class LDAPAuthorizationStrategy implements InternalAuthenticationProvider {

  /**
   * Returns the user corresponding to the given credentials. If no user cannot be found, returns
   * <code>null</code>.
   * 
   * @param username The username
   * @param password The password
   * 
   * @return The user corresponding to the given credentials, or <code>null</code> if not found
   * @throws AuthenticationException 
   */
  public User getUser(String username, String password) throws AuthenticationException {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    LDAPConnection connection;
    try {
      connection = LDAPUtils.getLDAPConnection();
      
      final String searchFilter = "(" + System.getProperty("authentication.ldap.usernameAttr") + "=" + username + ")";
      
      final LDAPSearchResults searchResults = connection.search(System.getProperty("authentication.ldap.authdn"), LDAPConnection.SCOPE_SUB, searchFilter, null, false);
      if (searchResults != null && searchResults.hasMore()) {
        LDAPEntry entry = searchResults.next();
        
        try {
          String uniqueIdAttr = System.getProperty("authentication.ldap.uniqueIdAttr");
          boolean idEncoded = "1".equals(System.getProperty("authentication.ldap.uniqueIdEncoded"));
          connection.bind(Integer.parseInt(System.getProperty("authentication.ldap.version")), entry.getDN(), password.getBytes("UTF8"));
          String id = idEncoded ? LDAPUtils.getAttributeBinaryValue(entry.getAttribute(uniqueIdAttr)) : entry.getAttribute(uniqueIdAttr).getStringValue();
          User user = userDAO.findByExternalIdAndAuthProvider(id, getName());
          if (user == null)
            throw new AuthenticationException(AuthenticationException.LOCAL_USER_MISSING);
          return user;
        } catch (UnsupportedEncodingException e) {
          throw new LDAPException();
        } 
      }
    } catch (LDAPException e) {
      throw new SmvcRuntimeException(e);
    }
   
    return null;
  }
  
  public String getUsername(String externalId) {
    return null;
  }
  
  public String createCredentials(String username, String password) {
    throw new SmvcRuntimeException(StatusCode.UNDEFINED, "NOT SUPPORTED");
  }
  
  public void updateUsername(String externalId, String username) {
    throw new SmvcRuntimeException(StatusCode.UNDEFINED, "NOT SUPPORTED");
  }
  
  public void updatePassword(String externalId, String password) {
    throw new SmvcRuntimeException(StatusCode.UNDEFINED, "NOT SUPPORTED");
  }

  /**
   * Returns whether this authorization provider is capable of updating the credentials of a user.
   * This provider is not capable of that, so <code>false</code> is always returned.
   * 
   * 
   * @return Always <code>true</code>
   */
  public boolean canUpdateCredentials() {
    return false;
  }

  /**
   * Returns the name of this authorization provider.
   * 
   * @return The name of this authorization provider
   */
  public String getName() {
    return "LDAP";
  }

}
