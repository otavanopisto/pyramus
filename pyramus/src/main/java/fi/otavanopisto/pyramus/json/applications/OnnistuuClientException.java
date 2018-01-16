package fi.otavanopisto.pyramus.json.applications;

public class OnnistuuClientException extends Exception {

  private static final long serialVersionUID = 3290031904241606812L;

  public OnnistuuClientException(String message) {
    super(message);
  }

  public OnnistuuClientException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
