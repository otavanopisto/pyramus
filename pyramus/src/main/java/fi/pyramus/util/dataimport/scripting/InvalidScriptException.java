package fi.pyramus.util.dataimport.scripting;

public class InvalidScriptException extends Exception {
  
  private static final long serialVersionUID = -8216498793811230928L;
  
  public InvalidScriptException(String msg) {
    super(msg);
  }
  public InvalidScriptException(Exception ex) {
    super(ex);
  }
}
