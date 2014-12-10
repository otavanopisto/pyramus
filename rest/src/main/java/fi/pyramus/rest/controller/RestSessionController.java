package fi.pyramus.rest.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.User;

@RequestScoped
public class RestSessionController {

  @Inject
  private UserDAO userDAO;
  
  @PostConstruct
  public void init() {
    loggedUserId = null;
  }
  
  public User getLoggedUser() {
    return userDAO.findById(loggedUserId);
  }
  
  public void setLoggedUserId(Long userId) {
    if (loggedUserId == null) {
      loggedUserId = userId;
    }
  }
  
  
  private Long loggedUserId;
}
