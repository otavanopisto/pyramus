package fi.otavanopisto.pyramus.dao.application;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationAttachment;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationAttachment_;

@Stateless
public class ApplicationAttachmentDAO extends PyramusEntityDAO<ApplicationAttachment> {

  public ApplicationAttachment create(String applicationId, String name, int size) {
    EntityManager entityManager = getEntityManager();
    ApplicationAttachment applicationAttachment = new ApplicationAttachment();
    applicationAttachment.setApplicationId(applicationId);
    applicationAttachment.setName(name);
    applicationAttachment.setSize(size);
    entityManager.persist(applicationAttachment);
    return applicationAttachment;
  }
  
  public ApplicationAttachment findByApplicationIdAndName(String applicationId, String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationAttachment> criteria = criteriaBuilder.createQuery(ApplicationAttachment.class);
    Root<ApplicationAttachment> root = criteria.from(ApplicationAttachment.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ApplicationAttachment_.applicationId), applicationId),
        criteriaBuilder.equal(root.get(ApplicationAttachment_.name), name)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<ApplicationAttachment> listByApplicationId(String applicationId) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationAttachment> criteria = criteriaBuilder.createQuery(ApplicationAttachment.class);
    Root<ApplicationAttachment> root = criteria.from(ApplicationAttachment.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(ApplicationAttachment_.applicationId), applicationId)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public ApplicationAttachment updateDescription(ApplicationAttachment applicationAttachment, String description) {
    EntityManager entityManager = getEntityManager();
    applicationAttachment.setDescription(description);
    entityManager.persist(applicationAttachment);
    return applicationAttachment;
  }

}
