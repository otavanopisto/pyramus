package fi.pyramus.dao.resources;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.resources.MaterialResource;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.pyramus.persistence.usertypes.MonetaryAmount;

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
