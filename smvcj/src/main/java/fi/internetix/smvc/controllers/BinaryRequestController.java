package fi.internetix.smvc.controllers;

/** Controllers responding to binary requests implement this interface.
 * 
 */
public interface BinaryRequestController extends RequestController {
  
  public void process(BinaryRequestContext binaryRequestContext);

}
