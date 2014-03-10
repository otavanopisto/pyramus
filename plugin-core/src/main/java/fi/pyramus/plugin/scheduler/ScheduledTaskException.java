package fi.pyramus.plugin.scheduler;

public class ScheduledTaskException extends Exception {

  private static final long serialVersionUID = -6720225634025855646L;

  public ScheduledTaskException(String message) {
    super(message);
  }
  
  public ScheduledTaskException(String message, Throwable cause) {
    super(message, cause);
  }
  
  
}
