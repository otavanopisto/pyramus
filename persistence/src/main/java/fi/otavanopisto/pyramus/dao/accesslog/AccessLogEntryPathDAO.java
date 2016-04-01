package fi.otavanopisto.pyramus.dao.accesslog;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.accesslog.AccessLogEntryPath;
import fi.otavanopisto.pyramus.domainmodel.accesslog.AccessLogEntryPath_;

@Stateless
public class AccessLogEntryPathDAO extends PyramusEntityDAO<AccessLogEntryPath> {

  public AccessLogEntryPath create(String path, boolean active) {
    EntityManager entityManager = getEntityManager();

    AccessLogEntryPath accessLogEntryPath = new AccessLogEntryPath();
    
    accessLogEntryPath.setPath(path);
    accessLogEntryPath.setActive(active);

    entityManager.persist(accessLogEntryPath);

    return accessLogEntryPath;
  }

  public AccessLogEntryPath findByPath(String path) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AccessLogEntryPath> criteria = criteriaBuilder.createQuery(AccessLogEntryPath.class);
    Root<AccessLogEntryPath> root = criteria.from(AccessLogEntryPath.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(AccessLogEntryPath_.path), path)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }


}
