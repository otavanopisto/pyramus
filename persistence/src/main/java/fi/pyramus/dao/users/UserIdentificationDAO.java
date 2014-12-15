package fi.pyramus.dao.users;

import java.util.List;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.users.UserIdentification;
import fi.pyramus.domainmodel.users.UserIdentification_;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class UserIdentificationDAO extends PyramusEntityDAO<UserIdentification> {

	private static final long serialVersionUID = -3862910101039448995L;

	public UserIdentification create(Person person, String authSource, String externalId) {
    fi.pyramus.domainmodel.users.UserIdentification userIdentification = new UserIdentification();
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

}
