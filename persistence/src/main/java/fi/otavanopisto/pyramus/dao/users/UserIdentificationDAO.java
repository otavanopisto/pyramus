package fi.otavanopisto.pyramus.dao.users;

import java.util.List;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification_;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class UserIdentificationDAO extends PyramusEntityDAO<UserIdentification> {

	public UserIdentification create(Person person, String authSource, String externalId) {
    UserIdentification userIdentification = new UserIdentification();
    userIdentification.setAuthSource(authSource);
    userIdentification.setExternalId(externalId);
    userIdentification.setPerson(person);

    return persist(userIdentification);
  }
  
  public UserIdentification findByAuthSourceAndExternalId(String authSource, String externalId) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserIdentification> criteria = criteriaBuilder.createQuery(UserIdentification.class);
    Root<UserIdentification> root = criteria.from(UserIdentification.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(UserIdentification_.externalId), externalId),
            criteriaBuilder.equal(root.get(UserIdentification_.authSource), authSource)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public UserIdentification findByAuthSourceAndPerson(String authSource, Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserIdentification> criteria = criteriaBuilder.createQuery(UserIdentification.class);
    Root<UserIdentification> root = criteria.from(UserIdentification.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(UserIdentification_.person), person),
            criteriaBuilder.equal(root.get(UserIdentification_.authSource), authSource)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public List<UserIdentification> listByPerson(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserIdentification> criteria = criteriaBuilder.createQuery(UserIdentification.class);
    Root<UserIdentification> root = criteria.from(UserIdentification.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(UserIdentification_.person), person)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public UserIdentification updateAuthSource(UserIdentification userIdentification, String authSource){
    userIdentification.setAuthSource(authSource);
    return persist(userIdentification);
  }
  
  public UserIdentification updateExternalId(UserIdentification userIdentification, String externalId){
    userIdentification.setExternalId(externalId);
    return persist(userIdentification);
  }

}
