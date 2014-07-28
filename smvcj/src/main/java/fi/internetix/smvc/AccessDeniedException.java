package fi.internetix.smvc;

import java.util.Locale;

import fi.internetix.smvc.i18n.Messages;

/** An exception that's thrown whenever a privilege-restricted
 * action is attempted without the necessary privileges.
 * 
 */
public class AccessDeniedException extends SmvcRuntimeException {

  /** The serial version UID of the class */
  private static final long serialVersionUID = -7202576899357656774L;

  public AccessDeniedException(Locale locale) {
    super(StatusCode.UNAUTHORIZED, Messages.getInstance().getText(locale, "exception.101.unauthorized"));
  }

  public AccessDeniedException(String message) {
    super(StatusCode.UNAUTHORIZED, message);
  }
}
