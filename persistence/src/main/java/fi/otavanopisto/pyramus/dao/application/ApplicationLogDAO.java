package fi.otavanopisto.pyramus.dao.application;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog_;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class ApplicationLogDAO extends PyramusEntityDAO<ApplicationLog> {

  public ApplicationLog create(Application application, ApplicationLogType type, String text, User user) {
    EntityManager entityManager = getEntityManager();

    ApplicationLog applicationLog = new ApplicationLog();
    
    applicationLog.setApplication(application);
    applicationLog.setType(type);
    applicationLog.setText(text);
    applicationLog.setUser(user);
    applicationLog.setDate(new Date());
    applicationLog.setArchived(Boolean.FALSE);
   
    entityManager.persist(applicationLog);

    return applicationLog;
  }

  public ApplicationLog update(ApplicationLog applicationLog, String text, User user) {
    EntityManager entityManager = getEntityManager();

    applicationLog.setText(text);
    applicationLog.setUser(user);
    applicationLog.setDate(new Date());
   
    entityManager.persist(applicationLog);

    return applicationLog;
  }
  
  public List<ApplicationLog> listByApplication(Application application) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationLog> criteria = criteriaBuilder.createQuery(ApplicationLog.class);
    Root<ApplicationLog> root = criteria.from(ApplicationLog.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(ApplicationLog_.application), application)
    );
    criteria.orderBy(criteriaBuilder.desc(root.get(ApplicationLog_.date)));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<ApplicationLog> listByApplicationAndArchived(Application application, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationLog> criteria = criteriaBuilder.createQuery(ApplicationLog.class);
    Root<ApplicationLog> root = criteria.from(ApplicationLog.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ApplicationLog_.application), application),
        criteriaBuilder.equal(root.get(ApplicationLog_.archived), archived)
      ));
    criteria.orderBy(criteriaBuilder.desc(root.get(ApplicationLog_.date)));
    
    return entityManager.createQuery(criteria).getResultList();
  }

}