package fi.otavanopisto.pyramus.dao.users;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.PasswordResetRequest;
import fi.otavanopisto.pyramus.domainmodel.users.PasswordResetRequest_;

@Stateless
public class PasswordResetRequestDAO extends PyramusEntityDAO<PasswordResetRequest> {

  public PasswordResetRequest create(Person person, String secret, Date date) {
    EntityManager entityManager = getEntityManager();
    
    PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
    passwordResetRequest.setPerson(person);
    passwordResetRequest.setSecret(secret);
    passwordResetRequest.setDate(date);
    
    entityManager.persist(passwordResetRequest);
  
    return passwordResetRequest;
  }
  
  public PasswordResetRequest findBySecret(String secret) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PasswordResetRequest> criteria = criteriaBuilder.createQuery(PasswordResetRequest.class);
    Root<PasswordResetRequest> root = criteria.from(PasswordResetRequest.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(PasswordResetRequest_.secret), secret)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<PasswordResetRequest> listByPerson(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PasswordResetRequest> criteria = criteriaBuilder.createQuery(PasswordResetRequest.class);
    Root<PasswordResetRequest> root = criteria.from(PasswordResetRequest.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(PasswordResetRequest_.person), person)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  @Override
  public void delete(PasswordResetRequest request) {
    super.delete(request);
  }

  public int deleteExpired(Date expiryDate) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaDelete<PasswordResetRequest> criteria = criteriaBuilder.createCriteriaDelete(PasswordResetRequest.class);
    Root<PasswordResetRequest> root = criteria.from(PasswordResetRequest.class);

    criteria.where(
        criteriaBuilder.lessThan(root.get(PasswordResetRequest_.date), expiryDate)
    );
    
    return entityManager.createQuery(criteria).executeUpdate();
  }
  
}
