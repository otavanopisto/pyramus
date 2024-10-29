package fi.otavanopisto.pyramus.dao.security;

import java.util.List;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.security.Permission_;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Dependent
@Stateless
public class PermissionDAO extends PyramusEntityDAO<Permission> {

  public Permission create(String name, String scope) {
    Permission permission = new Permission();
    
    permission.setName(name);
    permission.setScope(scope);
    permission.setReset(false);
    
    getEntityManager().persist(permission);
    
    return permission;
  }
  
  public Permission findByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Permission> criteria = criteriaBuilder.createQuery(Permission.class);
    Root<Permission> root = criteria.from(Permission.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Permission_.name), name)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<Permission> listByScope(String scope) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Permission> criteria = criteriaBuilder.createQuery(Permission.class);
    Root<Permission> root = criteria.from(Permission.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Permission_.scope), scope)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<Permission> listPermissionsSetToReset() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Permission> criteria = criteriaBuilder.createQuery(Permission.class);
    Root<Permission> root = criteria.from(Permission.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Permission_.reset), true)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
