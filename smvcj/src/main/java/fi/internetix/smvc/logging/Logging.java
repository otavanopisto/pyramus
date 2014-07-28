package fi.internetix.smvc.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Collection of methods for logging. */
public class Logging {
  
  /** Log an exception.
   * 
   * @param e The exception to log.
   */
  public static void logException(Exception e) {
    logException(null, e);
  } 

  /** Log an error message.
   * 
   * @param message The error message.
   * @param e The exception that produced the message.
   */
  public static void logException(String message, Exception e) {
    logger.log(Level.SEVERE, message, e);
  } 
  
  /** Log a debug message.
   * 
   * @param msg The debug message.
   */
  public static void logDebug(String msg) {
    logger.log(Level.FINEST, msg);
  }
  
  /** Log an informative message.
   * 
   * @param msg The informative message.
   */
  public static void logInfo(String msg) {
    logger.info(msg);
  }
  
  /** Log an error message.
   * 
   * @param msg The error message.
   */
  public static void logError(String msg) {
    logger.log(Level.SEVERE, msg);
  } 
  
  private static Logger logger = Logger.getLogger("smvcj");
}
