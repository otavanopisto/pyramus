package fi.internetix.smvc.controllers;

import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.internetix.smvc.controllers.RequestController;

/** Controllers responding to binary requests implement this interface.
 * 
 */
public interface BinaryRequestController extends RequestController {
  
  public void process(BinaryRequestContext binaryRequestContext);

}
