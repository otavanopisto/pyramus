package fi.otavanopisto.pyramus.dao.users;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.users.EmailSignature;
import fi.otavanopisto.pyramus.domainmodel.users.EmailSignature_;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class EmailSignatureDAO extends PyramusEntityDAO<EmailSignature> {
  
  public EmailSignature create(User user, String signature) {
    EntityManager entityManager = getEntityManager();
    
    EmailSignature emailSignature = new EmailSignature();
    emailSignature.setUser(user);
    emailSignature.setSignature(signature);
    
    entityManager.persist(emailSignature);
    
    return emailSignature;
  }

  public EmailSignature findByUser(User user) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<EmailSignature> criteria = criteriaBuilder.createQuery(EmailSignature.class);
    Root<EmailSignature> root = criteria.from(EmailSignature.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(EmailSignature_.user), user)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public EmailSignature updateSignature(EmailSignature emailSignature, String signature) {
    EntityManager entityManager = getEntityManager();
    emailSignature.setSignature(signature);
    entityManager.persist(emailSignature);
    return emailSignature;
  }

  @Override
  public void delete(EmailSignature emailSignature) {
    super.delete(emailSignature);
  }
  
}
