package fi.otavanopisto.pyramus.dao.application;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification_;
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
  
  public List<ApplicationNotification> listByNullOrLineAndState(String line, ApplicationState state) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationNotification> criteria = criteriaBuilder.createQuery(ApplicationNotification.class);
    Root<ApplicationNotification> root = criteria.from(ApplicationNotification.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.or(
          criteriaBuilder.isNull(root.get(ApplicationNotification_.line)),
          criteriaBuilder.equal(root.get(ApplicationNotification_.line), line)
        ),
        criteriaBuilder.equal(root.get(ApplicationNotification_.state), state)
      )
    );
    return entityManager.createQuery(criteria).getResultList();
  }

}