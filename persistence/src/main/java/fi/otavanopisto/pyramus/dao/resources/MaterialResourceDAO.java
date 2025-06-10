package fi.otavanopisto.pyramus.dao.resources;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.resources.MaterialResource;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceCategory;
import fi.otavanopisto.pyramus.persistence.usertypes.MonetaryAmount;

@Stateless
public class MaterialResourceDAO extends PyramusEntityDAO<MaterialResource> {

  public MaterialResource create(String name, ResourceCategory category, Double unitCost) {
    EntityManager entityManager = getEntityManager();

    MaterialResource materialResource = new MaterialResource();
    materialResource.setName(name);
    if (unitCost != null) {
      MonetaryAmount monetaryAmount = new MonetaryAmount();
      monetaryAmount.setAmount(unitCost);
      materialResource.setUnitCost(monetaryAmount);
    }
    materialResource.setCategory(category);
    entityManager.persist(materialResource);

    return materialResource;
  }

  public MaterialResource update(MaterialResource materialResource, String name,
      ResourceCategory category, Double unitCost) {
    EntityManager entityManager = getEntityManager();

    materialResource.setName(name);
    materialResource.setCategory(category);

    if (unitCost != null) {
      MonetaryAmount monetaryAmount = new MonetaryAmount();
      monetaryAmount.setAmount(unitCost);
      materialResource.setUnitCost(monetaryAmount);
    }

    entityManager.persist(materialResource);

    return materialResource;
  }
  
}
