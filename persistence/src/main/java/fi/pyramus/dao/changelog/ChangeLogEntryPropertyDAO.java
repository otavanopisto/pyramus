package fi.pyramus.dao.changelog;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.changelog.ChangeLogEntry;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryProperty;

@Stateless
public class ChangeLogEntryPropertyDAO extends PyramusEntityDAO<ChangeLogEntryProperty> {

  public ChangeLogEntryProperty create(ChangeLogEntry entry, ChangeLogEntryEntityProperty property, String value) {
    EntityManager entityManager = getEntityManager();

    ChangeLogEntryProperty changeLogEntryProperty = new ChangeLogEntryProperty();
    changeLogEntryProperty.setEntry(entry);
    changeLogEntryProperty.setProperty(property);
    changeLogEntryProperty.setValue(value);
    
    entityManager.persist(changeLogEntryProperty);
    
    return changeLogEntryProperty;
  }
  
  public ChangeLogEntryProperty findLatestByEntryEntityProperty(ChangeLogEntryEntityProperty entryEntityProperty, String entityId) {
    EntityManager entityManager = getEntityManager();
    
    Query query = entityManager.createQuery(
      "select " +  
      "  p " +
      "from " +   
      "  fi.pyramus.domainmodel.changelog.ChangeLogEntryProperty p " +   
      "where " +   
      "  p.property = :property AND " +
      "  p.entry.entityId = :entityId " +
      "order by " + 
      "  p.entry.time desc");
    
    query.setParameter("property", entryEntityProperty);
    query.setParameter("entityId", entityId);
    query.setMaxResults(1);
    @SuppressWarnings("unchecked") List<ChangeLogEntryProperty> result = query.getResultList();
    return result.size() == 1 ? result.get(0) : null;
  }
  

}
