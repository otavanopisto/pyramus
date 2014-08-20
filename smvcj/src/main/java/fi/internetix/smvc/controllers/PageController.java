package fi.internetix.smvc.controllers;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestController;

/** The base class for page request controllers.
 * 
 */
public interface PageController extends RequestController {
  
  public void process(PageRequestContext pageRequestContext);

}
