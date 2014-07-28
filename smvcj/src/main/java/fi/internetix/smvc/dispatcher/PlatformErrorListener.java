package fi.internetix.smvc.dispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.PageNotFoundException;
import fi.internetix.smvc.SmvcRuntimeException;

/**
 * Interface that describes listener for smvcj platform errors
 */
public interface PlatformErrorListener {

  /**
   * Method run when platform encounters a login required exception
   * 
   * @param e original exception
   */
  void onLoginRequiredException(HttpServletRequest request, HttpServletResponse response, LoginRequiredException e);
  
  /**
   * Method run when platform encounters a page not found exception
   * 
   * @param e original exception
   */
  void onPageNotFoundException(HttpServletRequest request, HttpServletResponse response, PageNotFoundException e);

  /**
   * Method run when platform encounters an access denied exception
   * 
   * @param e original exception
   */
  void onAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e);

  /**
   * Method run when platform encounters smvc runtime exception
   * 
   * @param e original exception
   */
  void onSmvcRuntimeException(HttpServletRequest request, HttpServletResponse response, SmvcRuntimeException e);

  /**
   * Method run when platform encounters exception that is not derived from SmvcRuntimeException
   * 
   * @param e original exception
   */
  void onUncontrolledException(HttpServletRequest request, HttpServletResponse response, Exception e);

  /**
   * Method run when platform fails to run commit phase
   * 
   * @param e original exception
   */
  void onTransactionCommitException(HttpServletRequest request, HttpServletResponse response, Exception e);
  
}
