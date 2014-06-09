package fi.pyramus.rest;

import javax.inject.Inject;

import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.User;

public abstract class AbstractRESTService {

  @Inject
  UserDAO userDAO;

  protected User getUser() {
    Role role = Role.getRole(4);
    User user = userDAO.create("Master", "Splinter", "Hamato Yoshi", "Turtles", role);
    return user;
  }

}
