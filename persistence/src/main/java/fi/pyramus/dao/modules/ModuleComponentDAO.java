package fi.pyramus.dao.modules;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.modules.ModuleComponent;
import fi.pyramus.domainmodel.modules.ModuleComponent_;

@Stateless
public class ModuleComponentDAO extends PyramusEntityDAO<ModuleComponent> {

  public ModuleComponent create(Module module, Double length, EducationalTimeUnit lengthTimeUnit, String name, String description) {
    EntityManager entityManager = getEntityManager();

    ModuleComponent moduleComponent = new ModuleComponent();
    moduleComponent.getLength().setUnit(lengthTimeUnit);
    moduleComponent.getLength().setUnits(length);
    moduleComponent.setName(name);
    moduleComponent.setDescription(description);

    entityManager.persist(moduleComponent);

    module.addModuleComponent(moduleComponent);

    entityManager.persist(module);

    return moduleComponent;
  }

  public ModuleComponent update(ModuleComponent moduleComponent, Double length, EducationalTimeUnit lengthTimeUnit, String name,
      String description) {
    EntityManager entityManager = getEntityManager();

    moduleComponent.setName(name);
    moduleComponent.getLength().setUnit(lengthTimeUnit);
    moduleComponent.getLength().setUnits(length);
    moduleComponent.setDescription(description);

    entityManager.persist(moduleComponent);

    return moduleComponent;
  }

  public List<ModuleComponent> listByModule(Module module) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ModuleComponent> criteria = criteriaBuilder.createQuery(ModuleComponent.class);
    Root<ModuleComponent> root = criteria.from(ModuleComponent.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(ModuleComponent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(ModuleComponent_.module), module)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @Override
  public void delete(ModuleComponent moduleComponent) {
    if (moduleComponent.getModule() != null)
      moduleComponent.getModule().removeModuleComponent(moduleComponent);
    super.delete(moduleComponent);
  }
}
