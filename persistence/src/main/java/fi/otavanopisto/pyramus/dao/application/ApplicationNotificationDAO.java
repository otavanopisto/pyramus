package fi.otavanopisto.pyramus.dao.application;

import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class ApplicationNotificationDAO extends PyramusEntityDAO<ApplicationNotification> {

  public ApplicationNotification create(String line, ApplicationState state) {
   EntityManager entityManager = getEntityManager();
   ApplicationNotification applicationNotification = new ApplicationNotification();   
   applicationNotification.setLine(line);
   applicationNotification.setState(state);
   entityManager.persist(applicationNotification);
   return applicationNotification;
  }
  
  public ApplicationNotification update(ApplicationNotification applicationNotification, String line, ApplicationState state) {
    EntityManager entityManager = getEntityManager();
    applicationNotification.setLine(line);
    applicationNotification.setState(state);
    entityManager.persist(applicationNotification);
    return applicationNotification;
  }

  public ApplicationNotification setUsers(ApplicationNotification applicationNotification, Set<User> users) {
    EntityManager entityManager = getEntityManager();
    applicationNotification.setUsers(users);
    entityManager.persist(applicationNotification);
    return applicationNotification;
  }

}