package fi.otavanopisto.pyramus.dao.security;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.security.EnvironmentRolePermission;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.security.EnvironmentRolePermission_;

@Dependent
@Stateless
public class EnvironmentRolePermissionDAO extends PyramusEntityDAO<EnvironmentRolePermission> {

  public EnvironmentRolePermission create(Role role, Permission permission) {
		EnvironmentRolePermission eurpermission = new EnvironmentRolePermission();

		eurpermission.setRole(role);
		eurpermission.setPermission(permission);

		getEntityManager().persist(eurpermission);

		return eurpermission;
	}

	// TODO: Not a DAO method
	public boolean hasEnvironmentPermissionAccess(Role role, Permission permission) {
		return findByUserRoleAndPermission(role, permission) != null;
	}

	public EnvironmentRolePermission findByUserRoleAndPermission(Role role, Permission permission) {
		EntityManager entityManager = getEntityManager();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EnvironmentRolePermission> criteria = criteriaBuilder.createQuery(EnvironmentRolePermission.class);
		Root<EnvironmentRolePermission> root = criteria.from(EnvironmentRolePermission.class);
		criteria.select(root);
		criteria.where(criteriaBuilder.and(criteriaBuilder.equal(root.get(EnvironmentRolePermission_.role), role),
				criteriaBuilder.equal(root.get(EnvironmentRolePermission_.permission), permission)));

		return getSingleResult(entityManager.createQuery(criteria));
	}
	
	public List<EnvironmentRolePermission> listByUserRole(Role role) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<EnvironmentRolePermission> criteria = criteriaBuilder.createQuery(EnvironmentRolePermission.class);
    Root<EnvironmentRolePermission> root = criteria.from(EnvironmentRolePermission.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(EnvironmentRolePermission_.role), role)
    );

    return entityManager.createQuery(criteria).getResultList();
	}

	@Override
	public void delete(EnvironmentRolePermission environmentUserRolePermission) {
		super.delete(environmentUserRolePermission);
	}
}
