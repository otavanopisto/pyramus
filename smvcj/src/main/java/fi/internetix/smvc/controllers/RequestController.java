package fi.internetix.smvc.controllers;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;

/** The interface implemented by all request controllers.
 * 
 */
public interface RequestController {

  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException;

}
