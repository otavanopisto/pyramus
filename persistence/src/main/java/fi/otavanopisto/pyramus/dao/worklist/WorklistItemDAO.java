package fi.otavanopisto.pyramus.dao.worklist;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem_;

public class WorklistItemDAO extends PyramusEntityDAO<WorklistItem> {

  public WorklistItem create(WorklistItemTemplate template, User owner, Date entryDate,
      String description, Double price, Double factor, CourseAssessment courseAssessment) {
    WorklistItem worklistItem = new WorklistItem();
    worklistItem.setTemplate(template);
    worklistItem.setOwner(owner);
    worklistItem.setEntryDate(entryDate);
    worklistItem.setDescription(description);
    worklistItem.setPrice(price);
    worklistItem.setFactor(factor);
    worklistItem.setCourseAssessment(courseAssessment);
    return persist(worklistItem);
  }
  
  public List<WorklistItem> listByOwnerAndArchived(User owner, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<WorklistItem> criteria = criteriaBuilder.createQuery(WorklistItem.class);
    Root<WorklistItem> root = criteria.from(WorklistItem.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(WorklistItem_.owner), owner),
      criteriaBuilder.equal(root.get(WorklistItem_.archived), archived)
    );
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<WorklistItem> listByOwnerAndTimeframeAndArchived(User owner, Date startDate, Date endDate, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<WorklistItem> criteria = criteriaBuilder.createQuery(WorklistItem.class);
    Root<WorklistItem> root = criteria.from(WorklistItem.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(WorklistItem_.owner), owner),
        criteriaBuilder.greaterThanOrEqualTo(root.get(WorklistItem_.entryDate), startDate),
        criteriaBuilder.lessThanOrEqualTo(root.get(WorklistItem_.entryDate), endDate),
        criteriaBuilder.equal(root.get(WorklistItem_.archived), archived)
      )
    );
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<WorklistItem> listByCourseAssessment(CourseAssessment courseAssessment) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<WorklistItem> criteria = criteriaBuilder.createQuery(WorklistItem.class);
    Root<WorklistItem> root = criteria.from(WorklistItem.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(WorklistItem_.courseAssessment), courseAssessment)
    );
    return entityManager.createQuery(criteria).getResultList();
  }


  public List<WorklistItem> listByCourseAssessmentAndArchived(CourseAssessment courseAssessment, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<WorklistItem> criteria = criteriaBuilder.createQuery(WorklistItem.class);
    Root<WorklistItem> root = criteria.from(WorklistItem.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(WorklistItem_.courseAssessment), courseAssessment),
        criteriaBuilder.equal(root.get(WorklistItem_.archived), archived)
      )
    );
    return entityManager.createQuery(criteria).getResultList();
  }

  public WorklistItem update(WorklistItem worklistItem, String description, Double price, Double factor) {
    worklistItem.setDescription(description);
    worklistItem.setPrice(price);
    worklistItem.setFactor(factor);
    return persist(worklistItem);
  }

}
