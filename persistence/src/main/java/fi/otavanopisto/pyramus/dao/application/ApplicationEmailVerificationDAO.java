package fi.otavanopisto.pyramus.dao.application;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationEmailVerification;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationEmailVerification_;

@Stateless
public class ApplicationEmailVerificationDAO extends PyramusEntityDAO<ApplicationEmailVerification> {

  public ApplicationEmailVerification create(Application application, String token, String email) {
   EntityManager entityManager = getEntityManager();
   ApplicationEmailVerification verification = new ApplicationEmailVerification();
   verification.setApplication(application);
   verification.setEmail(email);
   verification.setToken(token);
   verification.setVerified(false);
   entityManager.persist(verification);
   return verification;
  }
  
  public ApplicationEmailVerification updateVerified(ApplicationEmailVerification verification, boolean verified) {
    EntityManager entityManager = getEntityManager();
    verification.setVerified(verified);
    entityManager.persist(verification);
    return verification;
  }
  
  public ApplicationEmailVerification findByApplicationAndToken(Application application, String token) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationEmailVerification> criteria = criteriaBuilder.createQuery(ApplicationEmailVerification.class);
    Root<ApplicationEmailVerification> root = criteria.from(ApplicationEmailVerification.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ApplicationEmailVerification_.application), application),
        criteriaBuilder.equal(root.get(ApplicationEmailVerification_.token), token)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public ApplicationEmailVerification findByApplicationAndEmail(Application application, String email) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationEmailVerification> criteria = criteriaBuilder.createQuery(ApplicationEmailVerification.class);
    Root<ApplicationEmailVerification> root = criteria.from(ApplicationEmailVerification.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ApplicationEmailVerification_.application), application),
        criteriaBuilder.equal(root.get(ApplicationEmailVerification_.email), email)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public List<ApplicationEmailVerification> listByApplication(Application application) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationEmailVerification> criteria = criteriaBuilder.createQuery(ApplicationEmailVerification.class);
    Root<ApplicationEmailVerification> root = criteria.from(ApplicationEmailVerification.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(ApplicationEmailVerification_.application), application)
    );
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<ApplicationEmailVerification> listByApplicationAndUnverified(Application application) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationEmailVerification> criteria = criteriaBuilder.createQuery(ApplicationEmailVerification.class);
    Root<ApplicationEmailVerification> root = criteria.from(ApplicationEmailVerification.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ApplicationEmailVerification_.application), application),
        criteriaBuilder.equal(root.get(ApplicationEmailVerification_.verified), false)
      )
    );
    return entityManager.createQuery(criteria).getResultList();
  }

}