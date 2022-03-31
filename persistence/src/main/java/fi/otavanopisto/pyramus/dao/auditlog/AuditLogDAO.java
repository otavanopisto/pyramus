package fi.otavanopisto.pyramus.dao.auditlog;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.audit.AuditLog;
import fi.otavanopisto.pyramus.domainmodel.audit.AuditLog_;

@Stateless
public class AuditLogDAO extends PyramusEntityDAO<AuditLog> {

  public List<AuditLog> listLatestByAuthorAndPersonAndUserAndCount(Long authorId, Long personId, Long userId, Integer count) {
    EntityManager entityManager = getEntityManager();
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AuditLog> criteria = criteriaBuilder.createQuery(AuditLog.class);
    Root<AuditLog> root = criteria.from(AuditLog.class);
    criteria.select(root);

    List<Predicate> predicates = new ArrayList<Predicate>();
    if (authorId != null) {
      predicates.add(criteriaBuilder.equal(root.get(AuditLog_.authorId), authorId));
    }
    if (personId != null) {
      predicates.add(criteriaBuilder.equal(root.get(AuditLog_.personId), personId));
    }
    if (userId != null) {
      predicates.add(criteriaBuilder.equal(root.get(AuditLog_.userId), userId));
    }
    
    criteria.where(predicates.toArray(new Predicate[0]));
    criteria.orderBy(criteriaBuilder.desc(root.get(AuditLog_.id)));

    TypedQuery<AuditLog> query = entityManager.createQuery(criteria);
    query.setMaxResults(count);
    
    return query.getResultList();
  }
  
  
}