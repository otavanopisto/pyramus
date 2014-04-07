package fi.pyramus.util.dataimport.scripting;

public class InvalidScriptException extends Exception {
  public InvalidScriptException(String msg) {
    super(msg);
  }
  public InvalidScriptException(Exception ex) {
    super(ex);
  }
}
