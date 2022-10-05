package fi.internetix.smvc.controllers;

/** The base class for page request controllers.
 * 
 */
public interface PageController extends RequestController {
  
  public void process(PageRequestContext pageRequestContext);

}
