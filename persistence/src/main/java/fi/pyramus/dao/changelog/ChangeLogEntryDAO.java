package fi.pyramus.dao.changelog;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.changelog.ChangeLogEntry;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryEntity;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryType;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class ChangeLogEntryDAO extends PyramusEntityDAO<ChangeLogEntry> {

  public ChangeLogEntry create(ChangeLogEntryEntity entity, ChangeLogEntryType type, String entityId, Date time, User user) {
    EntityManager entityManager = getEntityManager();

    ChangeLogEntry changeLogEntry = new ChangeLogEntry();
    changeLogEntry.setEntityId(entityId);
    changeLogEntry.setEntity(entity);
    changeLogEntry.setType(type);
    changeLogEntry.setTime(time);
    changeLogEntry.setUser(user);
    
    entityManager.persist(changeLogEntry);
    
    return changeLogEntry;
  }

}
