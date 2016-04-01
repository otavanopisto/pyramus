package fi.otavanopisto.pyramus.persistence.events;

public class EventException extends RuntimeException {

  private static final long serialVersionUID = 9088947052759635971L;

  public EventException(String message) {
    super(message);
  }
  
  public EventException(Exception e) {
    super(e);
  }
}
