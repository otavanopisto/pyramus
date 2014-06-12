package fi.pyramus.rest;

import javax.inject.Inject;

import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.User;

public abstract class AbstractRESTService {
  
  @Inject
  private UserDAO userDAO;

  // TODO: Implement this
  protected Long getLoggedUserId() {
    return 1l;
  }
  
  protected User getLoggedUser() {
    return userDAO.findById(getLoggedUserId());
  }

}
