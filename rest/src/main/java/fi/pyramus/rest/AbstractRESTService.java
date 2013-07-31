package fi.pyramus.rest;

import java.util.Collection;

import javax.inject.Inject;

import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.User;
import fi.tranquil.TranquilModelEntity;
import fi.tranquil.TranquilityBuilderFactory;

public abstract class AbstractRESTService {

  protected abstract TranquilityBuilderFactory getTranquilityBuilderFactory();
  @Inject
  UserDAO userDAO;
  protected TranquilModelEntity tranqualise(Object object) {
    return getTranquilityBuilderFactory()
        .createBuilder()
        .createTranquility()
        .entity(object);
  }

  protected Collection<TranquilModelEntity> tranqualise(Collection<?> collection) {
    return getTranquilityBuilderFactory()
        .createBuilder()
        .createTranquility()
        .entities(collection);
  }

  protected User getUser() {
    Role role = Role.getRole(4);
    User user = userDAO.create("Master", "Splinter", "Hamato Yoshi", "Turtles", role);
    return user;
  }

}
