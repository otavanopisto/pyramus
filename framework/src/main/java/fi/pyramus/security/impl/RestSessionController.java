package fi.pyramus.security.impl;

public interface RestSessionController extends SessionController, MutableSessionController {
  
  public void setAuthentication(RestAuthentication authentication);
  
}
