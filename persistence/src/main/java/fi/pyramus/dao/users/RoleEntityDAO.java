package fi.pyramus.dao.users;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.users.RoleEntity;
import fi.pyramus.domainmodel.users.RoleEntity_;

@Dependent
@Stateless
public class RoleEntityDAO extends PyramusEntityDAO<RoleEntity> {

  public RoleEntity findByName(String roleName) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<RoleEntity> criteria = criteriaBuilder.createQuery(RoleEntity.class);
    Root<RoleEntity> root = criteria.from(RoleEntity.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(RoleEntity_.name), roleName)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

}
