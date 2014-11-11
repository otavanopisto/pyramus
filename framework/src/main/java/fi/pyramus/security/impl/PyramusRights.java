package fi.pyramus.security.impl;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import fi.muikku.security.ContextReference;

/**
 * Class to hide EJB handling
 * 
 * TODO: It doesn't work. Yep, "LOL"
 * 
 */
public class PyramusRights {

  public static boolean hasPermission(String permission) throws NamingException {
    SessionController sessionController = (SessionController) new InitialContext().lookup("java:app/pyramus/SessionControllerImpl");
    
    ContextReference contextReference = null;

    return sessionController.hasPermission(permission, contextReference);
  }

  public static void login(Long userId) throws NamingException {
//    SessionController sessionController = (SessionController) new InitialContext().lookup("java:app/pyramus/SessionControllerImpl");
//    sessionController.login(userId);
  }

  public static void logout() throws NamingException {
//    SessionController sessionController = (SessionController) new InitialContext().lookup("java:app/pyramus/SessionControllerImpl");
//    sessionController.logout();
  }
  
}
