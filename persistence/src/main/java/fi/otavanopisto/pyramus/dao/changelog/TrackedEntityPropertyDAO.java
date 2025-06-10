package fi.otavanopisto.pyramus.dao.changelog;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.changelog.TrackedEntityProperty;

@Stateless
public class TrackedEntityPropertyDAO extends PyramusEntityDAO<TrackedEntityProperty> {

  public TrackedEntityProperty create(String entity, String property) {
    EntityManager entityManager = getEntityManager();
    
    TrackedEntityProperty trackedEntityProperty = new TrackedEntityProperty();
    trackedEntityProperty.setEntity(entity);
    trackedEntityProperty.setProperty(property);
    
    entityManager.persist(trackedEntityProperty);
    
    return trackedEntityProperty;
  }
  
  public TrackedEntityProperty findByEntityAndProperty(String entity, String property) {
    EntityManager entityManager = getEntityManager();
    Query query = entityManager.createQuery("from TrackedEntityProperty where entity = :entity and property = :property");
    query.setParameter("entity", entity);
    query.setParameter("property", property);
    @SuppressWarnings("unchecked") List<TrackedEntityProperty> result = query.getResultList();
    return result.size() == 1 ? result.get(0) : null;
  }
  
  public TrackedEntityProperty updateEntity(TrackedEntityProperty trackedEntityProperty, String entity) {
    EntityManager entityManager = getEntityManager();
    trackedEntityProperty.setEntity(entity);
    entityManager.persist(trackedEntityProperty);
    return trackedEntityProperty;
  }

  public TrackedEntityProperty updateProperty(TrackedEntityProperty trackedEntityProperty, String property) {
    EntityManager entityManager = getEntityManager();
    trackedEntityProperty.setProperty(property);
    entityManager.persist(trackedEntityProperty);
    return trackedEntityProperty;
  }

  @Override
  public void delete(TrackedEntityProperty trackedEntityProperty) {
    super.delete(trackedEntityProperty);
  }
  
}
