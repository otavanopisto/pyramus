package fi.pyramus.dao.users;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.users.SystemRoleEntity;
import fi.pyramus.domainmodel.users.SystemRoleEntity_;
import fi.pyramus.domainmodel.users.SystemRoleType;

@Dependent
@Stateless
public class SystemRoleEntityDAO extends PyramusEntityDAO<SystemRoleEntity> {

  public SystemRoleEntity create(String name, SystemRoleType roleType) {
    SystemRoleEntity systemRoleEntity = new SystemRoleEntity();
    systemRoleEntity.setName(name);
    systemRoleEntity.setRoleType(roleType);
    
    getEntityManager().persist(systemRoleEntity);
    
    return systemRoleEntity;
  }

  public SystemRoleEntity findByRoleType(SystemRoleType roleType) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SystemRoleEntity> criteria = criteriaBuilder.createQuery(SystemRoleEntity.class);
    Root<SystemRoleEntity> root = criteria.from(SystemRoleEntity.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(SystemRoleEntity_.roleType), roleType)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

}
