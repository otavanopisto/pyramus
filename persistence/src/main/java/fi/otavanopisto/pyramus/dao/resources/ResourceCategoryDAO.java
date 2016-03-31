package fi.otavanopisto.pyramus.dao.resources;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceCategory;

@Stateless
public class ResourceCategoryDAO extends PyramusEntityDAO<ResourceCategory> {

  public ResourceCategory createResourceCategory(String name) {
    EntityManager entityManager = getEntityManager();
    ResourceCategory resourceCategory = new ResourceCategory();
    resourceCategory.setName(name);
    entityManager.persist(resourceCategory);

    return resourceCategory;
  }

  public void updateResourceCategory(ResourceCategory resourceCategory, String name) {
    EntityManager entityManager = getEntityManager();
    resourceCategory.setName(name);
    entityManager.persist(resourceCategory);
  }

  public void archiveResourceCategory(ResourceCategory resourceCategory) {
    EntityManager entityManager = getEntityManager();
    resourceCategory.setArchived(Boolean.TRUE);
    entityManager.persist(resourceCategory);
  }

  public void unarchiveResourceCategory(ResourceCategory resourceCategory) {
    EntityManager entityManager = getEntityManager();
    resourceCategory.setArchived(Boolean.FALSE);
    entityManager.persist(resourceCategory);
  }

  @Override
  public void delete(ResourceCategory resourceCategory) {
    super.delete(resourceCategory);
  }
  
}
