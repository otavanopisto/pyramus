package fi.otavanopisto.pyramus.dao.changelog;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.changelog.ChangeLogEntryEntity;
import fi.otavanopisto.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty;

@Stateless
public class ChangeLogEntryEntityPropertyDAO extends PyramusEntityDAO<ChangeLogEntryEntityProperty> {

  public ChangeLogEntryEntityProperty create(ChangeLogEntryEntity entity, String name) {
    EntityManager entityManager = getEntityManager();

    ChangeLogEntryEntityProperty changeLogEntryEntityProperty = new ChangeLogEntryEntityProperty();
    changeLogEntryEntityProperty.setEntity(entity);
    changeLogEntryEntityProperty.setName(name);
    
    entityManager.persist(changeLogEntryEntityProperty);
    
    return changeLogEntryEntityProperty;
  }
  
  public ChangeLogEntryEntityProperty findByEntityAndName(ChangeLogEntryEntity entity, String name) {
    EntityManager entityManager = getEntityManager();
    Query query = entityManager.createQuery("from ChangeLogEntryEntityProperty where entity = :entity and name = :name");
    query.setParameter("name", name);
    query.setParameter("entity", entity);
    @SuppressWarnings("unchecked") List<ChangeLogEntryEntityProperty> result = query.getResultList();
    return result.size() == 1 ? result.get(0) : null;
  }
  
}
