package fi.otavanopisto.pyramus.dao.worklist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem_;

@Stateless
public class WorklistItemDAO extends PyramusEntityDAO<WorklistItem> {

  public WorklistItem create(WorklistItemTemplate template, User owner, Date entryDate,
      String description, Double price, Double factor, String billingNumber, CourseAssessment courseAssessment, User currentUser) {
    WorklistItem worklistItem = new WorklistItem();
    worklistItem.setTemplate(template);
    worklistItem.setOwner(owner);
    worklistItem.setEntryDate(entryDate);
    worklistItem.setDescription(description);
    worklistItem.setPrice(price);
    worklistItem.setFactor(factor);
    worklistItem.setBillingNumber(billingNumber);
    worklistItem.setCourseAssessment(courseAssessment);
    worklistItem.setEditableFields(template.getEditableFields());
    worklistItem.setLocked(Boolean.FALSE);
    Date now = new Date();
    worklistItem.setCreator(currentUser);
    worklistItem.setCreated(now);
    worklistItem.setModifier(currentUser);
    worklistItem.setModified(now);
    worklistItem.setArchived(Boolean.FALSE);
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
    List<Predicate> predicates = new ArrayList<>();
    if (owner != null) {
      predicates.add(criteriaBuilder.equal(root.get(WorklistItem_.owner), owner));
    }
    if (startDate != null) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(WorklistItem_.entryDate), startDate));
    }
    if (endDate != null) {
      predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(WorklistItem_.entryDate), endDate));
    }
    if (archived != null) {
      predicates.add(criteriaBuilder.equal(root.get(WorklistItem_.archived), archived));
    }
    criteria.where(criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new)));
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

  public WorklistItem update(WorklistItem worklistItem, Date entryDate, String description, Double price, Double factor, String billingNumber, User currentUser) {
    worklistItem.setEntryDate(entryDate);
    worklistItem.setDescription(description);
    worklistItem.setPrice(price);
    worklistItem.setFactor(factor);
    worklistItem.setBillingNumber(billingNumber);
    worklistItem.setModified(new Date());
    worklistItem.setModifier(currentUser);
    return persist(worklistItem);
  }

}
