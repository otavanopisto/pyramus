package fi.otavanopisto.pyramus.plugin.auth.internal;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.InternalAuthDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.otavanopisto.pyramus.plugin.auth.utils.EncodingUtils;

/**
 * An authentication provider using the credential storage of the application itself.
 */
public class InternalAuthenticationStrategy implements InternalAuthenticationProvider {

  private static Logger logger = Logger.getLogger(InternalAuthenticationStrategy.class.getName());
  
  /**
   * Returns the username of a user corresponding to the given identifier, or <code>null</code> if
   * not found.
   * 
   * @param externalId The user identifier
   * 
   * @return The username of the user corresponding to the given identifier, or <code>null</code> if
   * not found
   */
  public String getUsername(String externalId) {
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
    
    Long internalAuthId = NumberUtils.createLong(externalId);
    if (internalAuthId != null && internalAuthId > 0) {
      InternalAuth internalAuth = internalAuthDAO.findById(internalAuthId);
      return internalAuth == null ? null : internalAuth.getUsername();
    }
    
    return null;
  }

  /**
   * Returns the user corresponding to the given credentials. If no user cannot be found, returns
   * <code>null</code>.
   * 
   * @param username The username
   * @param password The password
   * 
   * @return The user corresponding to the given credentials, or <code>null</code> if not found
   */
  public User getUser(String username, String password) {
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();

    String passwordEncoded;
    try {
      passwordEncoded = EncodingUtils.md5EncodeString(password);
    }
    catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(e);
    }
    
    catch (NoSuchAlgorithmException e) {
      throw new SmvcRuntimeException(e);
    }

InternalAuth internalAuth = internalAuthDAO.findByUsernameAndPassword(username, passwordEncoded);
    if (internalAuth != null) {
      UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndExternalId(getName(), String.valueOf(internalAuth.getId()));
      if(userIdentification != null){
        return userIdentification.getPerson().getDefaultUser();
      }
      logger.warning("UserIdentification is null");
      return null;
    }
    else {
      return null;
    }
  }
  
  public User getUserByName(String username) {
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO(); 
    
    InternalAuth internalAuth = internalAuthDAO.findByUsername(username);
    if (internalAuth != null) {
      UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndExternalId(getName(), String.valueOf(internalAuth.getId()));
      if(userIdentification != null){
        
        return userIdentification.getPerson().getDefaultUser();
      }
      return null;
    }
    else {
      return null;
    }
    
  }
  
  @Override
  public String createCredentials(String username, String password) {
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
    try {
      String passwordEncoded = EncodingUtils.md5EncodeString(password);
      InternalAuth internalAuth = internalAuthDAO.create(username, passwordEncoded);
      String externalId = internalAuth.getId().toString();
      return externalId;
    }
    catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(e);
    }
    catch (NoSuchAlgorithmException e) {
      throw new SmvcRuntimeException(e);
    }
  }
  
  @Override
  public void updateUsername(String externalId, String username) {
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();

    InternalAuth internalAuth = internalAuthDAO.findById(NumberUtils.createLong(externalId));
    internalAuthDAO.updateUsername(internalAuth, username);
  }
  
  @Override
  public void updatePassword(String externalId, String password) {
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();

    try {
      InternalAuth internalAuth = internalAuthDAO.findById(NumberUtils.createLong(externalId));

      String passwordEncoded = EncodingUtils.md5EncodeString(password);
      internalAuthDAO.updatePassword(internalAuth, passwordEncoded);
    }
    catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(e);
    }
    catch (NoSuchAlgorithmException e) {
      throw new SmvcRuntimeException(e);
    }
  }

  @Override
  public void updateCredentials(String externalId, String username, String password) {
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
    try {
      InternalAuth internalAuth = internalAuthDAO.findById(NumberUtils.createLong(externalId));
      if (internalAuth == null) {
        throw new IllegalStateException(String.format("InternalAuth for id %s not found", externalId));
      }
      String passwordEncoded = EncodingUtils.md5EncodeString(password);
      internalAuthDAO.updateUsernameAndPassword(internalAuth, username, passwordEncoded);
    }
    catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(e);
    }
    catch (NoSuchAlgorithmException e) {
      throw new SmvcRuntimeException(e);
    }
  }

  /**
   * Returns whether this authentication provider is capable of updating the credentials of a user.
   * This provider is capable of that, so <code>true</code> is always returned.
   * 
   * @return Always <code>true</code>
   */
  public boolean canUpdateCredentials() {
    return true;
  }

  @Override
  public boolean validatePassword(String externalId, String password) {
    InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();

    try {
      InternalAuth internalAuth = internalAuthDAO.findById(NumberUtils.createLong(externalId));

      String passwordEncoded = EncodingUtils.md5EncodeString(password);
      
      return passwordEncoded.equals(internalAuth.getPassword());
    }
    catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(e);
    }
    catch (NoSuchAlgorithmException e) {
      throw new SmvcRuntimeException(e);
    }
  }

  /**
   * Returns the name of this authentication provider.
   * 
   * @return The name of this authentication provider
   */
  public String getName() {
    return "internal";
  }
  
  @Override
  public String logout(RequestContext requestContext) {
    return null;
  }

}
