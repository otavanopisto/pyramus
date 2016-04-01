package fi.otavanopisto.pyramus.dao.resources;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceCategory;
import fi.otavanopisto.pyramus.domainmodel.resources.WorkResource;
import fi.otavanopisto.pyramus.persistence.usertypes.MonetaryAmount;

@Stateless
public class WorkResourceDAO extends PyramusEntityDAO<WorkResource> {

  public WorkResource create(String name, ResourceCategory category, Double costPerUse, Double hourlyCost) {
    EntityManager entityManager = getEntityManager();

    WorkResource workResource = new WorkResource();
    workResource.setName(name);
    if (costPerUse != null) {
      MonetaryAmount monetaryAmount = new MonetaryAmount();
      monetaryAmount.setAmount(costPerUse);
      workResource.setCostPerUse(monetaryAmount);
    }
    if (hourlyCost != null) {
      MonetaryAmount monetaryAmount = new MonetaryAmount();
      monetaryAmount.setAmount(hourlyCost);
      workResource.setHourlyCost(monetaryAmount);
    }
    workResource.setCategory(category);
    entityManager.persist(workResource);

    return workResource;
  }

  public WorkResource update(WorkResource workResource, String name, ResourceCategory category,
      Double costPerUse, Double hourlyCost) {
    EntityManager entityManager = getEntityManager();

    workResource.setName(name);
    workResource.setCategory(category);

    if (costPerUse != null) {
      MonetaryAmount monetaryAmount = new MonetaryAmount();
      monetaryAmount.setAmount(costPerUse);
      workResource.setCostPerUse(monetaryAmount);
    }

    if (hourlyCost != null) {
      MonetaryAmount monetaryAmount = new MonetaryAmount();
      monetaryAmount.setAmount(hourlyCost);
      workResource.setHourlyCost(monetaryAmount);
    }

    entityManager.persist(workResource);

    return workResource;
  }
  
}
