package fi.otavanopisto.pyramus.dao.changelog;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.changelog.ChangeLogEntryEntity;

@Stateless
public class ChangeLogEntryEntityDAO extends PyramusEntityDAO<ChangeLogEntryEntity> {

  public ChangeLogEntryEntity create(String name) {
    EntityManager entityManager = getEntityManager();

    ChangeLogEntryEntity changeLogEntryEntity = new ChangeLogEntryEntity();
    changeLogEntryEntity.setName(name);
    
    entityManager.persist(changeLogEntryEntity);
    
    return changeLogEntryEntity;
  }
  
  public ChangeLogEntryEntity findByName(String name) {
    EntityManager entityManager = getEntityManager();
    Query query = entityManager.createQuery("from ChangeLogEntryEntity where name = :name");
    query.setParameter("name", name);
    @SuppressWarnings("unchecked") List<ChangeLogEntryEntity> result = query.getResultList();
    return result.size() == 1 ? result.get(0) : null;
  }
  
}
