package fi.internetix.smvc;

/** An exception that's thrown whenever a member-only action
 *  is attempted without logging in. 
 */
public class LoginRequiredException extends SmvcRuntimeException {

  /** The serial version UID of the class */
  private static final long serialVersionUID = -585018511223881722L;

  public LoginRequiredException() {
    super(StatusCode.NOT_LOGGED_IN, null);
  }
  
  public LoginRequiredException(String redirectUrl) {
    super(StatusCode.NOT_LOGGED_IN, null);
    this.redirectUrl = redirectUrl;
  }

  public LoginRequiredException(String redirectUrl, String contextType, String contextId) {
    super(StatusCode.NOT_LOGGED_IN, null);
    this.redirectUrl = redirectUrl;
    this.contextType = contextType;
    this.contextId = contextId;
  }
  
  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public String getContextType() {
    return contextType;
  }

  public void setContextType(String contextType) {
    this.contextType = contextType;
  }

  public String getContextId() {
    return contextId;
  }

  public void setContextId(String contextId) {
    this.contextId = contextId;
  }

  private String redirectUrl;
  private String contextType;
  private String contextId;

}
