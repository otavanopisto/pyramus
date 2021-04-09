package fi.otavanopisto.pyramus.dao.worklist;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemEditableFields;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplateType;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate_;

@Stateless
public class WorklistItemTemplateDAO extends PyramusEntityDAO<WorklistItemTemplate> {

  public WorklistItemTemplate createTemplate(WorklistItemTemplateType templateType, String description, Double price, Double factor,
      String billingNumber, Set<WorklistItemEditableFields> editableFields, Boolean removable) {
    WorklistItemTemplate template = new WorklistItemTemplate();
    template.setTemplateType(templateType);
    template.setDescription(description);
    template.setPrice(price);
    template.setFactor(factor);
    template.setBillingNumber(billingNumber);
    template.setEditableFields(editableFields);
    template.setRemovable(removable);
    return persist(template);
  }

  public WorklistItemTemplate updateTemplate(WorklistItemTemplate template, WorklistItemTemplateType templateType, String description, Double price, Double factor,
      String billingNumber, Set<WorklistItemEditableFields> editableFields, Boolean removable) {
    template.setTemplateType(templateType);
    template.setDescription(description);
    template.setPrice(price);
    template.setFactor(factor);
    template.setBillingNumber(billingNumber);
    template.setEditableFields(editableFields);
    template.setRemovable(removable);
    return persist(template);
  }

  public List<WorklistItemTemplate> listByTemplateTypesAndArchived(Set<WorklistItemTemplateType> templateTypes, Boolean archived) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<WorklistItemTemplate> criteria = criteriaBuilder.createQuery(WorklistItemTemplate.class);
    Root<WorklistItemTemplate> root = criteria.from(WorklistItemTemplate.class);
    In<WorklistItemTemplateType> inClause = criteriaBuilder.in(root.get(WorklistItemTemplate_.templateType));
    for (WorklistItemTemplateType templateType : templateTypes) {
      inClause.value(templateType);
    }
    criteria.where(
      criteriaBuilder.and(
        inClause,
        criteriaBuilder.equal(root.get(WorklistItemTemplate_.archived), archived)
      )
    );
    return entityManager.createQuery(criteria).getResultList();
  }

}
