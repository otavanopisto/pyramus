package fi.pyramus.security.impl;

public interface SessionControllerDelegate extends SessionController, MutableSessionController {

  public void setImplementation(SessionController implementation);
  
}
