package fi.otavanopisto.pyramus.dao;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.http.HttpSession;

import fi.otavanopisto.pyramus.domainmodel.audit.AuditLog;
import fi.otavanopisto.pyramus.domainmodel.audit.AuditLogType;
import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.util.ReflectionApiUtils;
import fi.otavanopisto.pyramus.util.ThreadSessionContainer;

public abstract class GenericDAO<T> {
  
  GenericDAO() {
  }

  @SuppressWarnings("unchecked")
  public T findById(Long id) {
    EntityManager entityManager = getEntityManager();
    return (T) entityManager.find(getGenericTypeClass(), id);
  }

  public List<T> listAll() {
    return listAll(null, null);
  }

  @SuppressWarnings("unchecked")
  public List<T> listAll(Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager();
    Class<?> genericTypeClass = getGenericTypeClass();
    Query query = entityManager.createQuery("select o from " + genericTypeClass.getName() + " o");
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }
    
    return query.getResultList();
  }
  
  public void archive(ArchivableEntity entity) {
    archive(entity, null);
  }
  
  public void archive(ArchivableEntity entity, User user) {
    EntityManager entityManager = getEntityManager();
    entity.setArchived(Boolean.TRUE);
    if (entity instanceof ModificationTrackedEntity && user != null) {
      ((ModificationTrackedEntity) entity).setLastModified(new Date());
      ((ModificationTrackedEntity) entity).setLastModifier(user);
    }
    entityManager.persist(entity);
  }

  public void unarchive(ArchivableEntity entity) {
    unarchive(entity, null);
  }
  
  public void unarchive(ArchivableEntity entity, User user) {
    EntityManager entityManager = getEntityManager();
    entity.setArchived(Boolean.FALSE);
    if (entity instanceof ModificationTrackedEntity && user != null) {
      ((ModificationTrackedEntity) entity).setLastModified(new Date());
      ((ModificationTrackedEntity) entity).setLastModifier(user);
    }
    entityManager.persist(entity);
  }

  protected T persist(T object) {
    getEntityManager().persist(object);
    return object;
  }

  public long count() {
    EntityManager entityManager = getEntityManager();
    Class<?> genericTypeClass = getGenericTypeClass();
    Query query = entityManager.createQuery("select count(o) from " + genericTypeClass.getName() + " o");
    return (long) query.getSingleResult();
  }

  public void delete(T e) {
    getEntityManager().remove(e);
  }
  
  public void flush() {
    getEntityManager().flush();
  }
  
  protected T getSingleResult(Query query) {
    @SuppressWarnings("unchecked")
    List<T> list = query.getResultList();
    
    if (list.isEmpty())
      return null;
    
    if (list.size() == 1)
      return list.get(0);
    
    throw new NonUniqueResultException("SingleResult query returned " + list.size() + " elements");
  }
  
  @SuppressWarnings("rawtypes")
  public void auditCreate(Long personId, Long userId, T entity, SingularAttribute field, boolean logValueData) {
    try {
      Long entityId = (Long) ReflectionApiUtils.getObjectFieldValue(entity, "id", true);
      String data = logValueData ? String.valueOf(ReflectionApiUtils.getObjectFieldValue(entity, field.getName(), true)) : null;
      if (data != null && data.length() > 255) {
        data = data.substring(0, 250) + "...";
      }
      createAuditLogEntry(personId, userId, AuditLogType.ADD, getGenericTypeClass().getSimpleName(), entityId, field.getName(), data);
    }
    catch (Exception e) {
      // Reflection failure 
    }
  }
  
  public void auditUpdate(Long personId, Long userId, T entity) {
    try {
      Long entityId = (Long) ReflectionApiUtils.getObjectFieldValue(entity, "id", true);
      createAuditLogEntry(personId, userId, AuditLogType.MOD, getGenericTypeClass().getSimpleName(), entityId, null, null);
    }
    catch (Exception e) {
      // Reflection failure 
    }
  }

  @SuppressWarnings("rawtypes")
  public void auditUpdate(Long personId, Long userId, T entity, SingularAttribute field, Object newValue, boolean logValueData) {
    try {
      Object oldValue = ReflectionApiUtils.getObjectFieldValue(entity, field.getName(), true);
      if (!Objects.equals(oldValue, newValue)) {
        Long entityId = (Long) ReflectionApiUtils.getObjectFieldValue(entity, "id", true);
        String data = logValueData ? String.valueOf(oldValue + " -> " + newValue) : null;
        if (data != null && data.length() > 255) {
          data = data.substring(0, 250) + "...";
        }
        createAuditLogEntry(personId, userId, AuditLogType.MOD, getGenericTypeClass().getSimpleName(), entityId, field.getName(), data);
      }
    }
    catch (Exception e) {
      // Reflection failure 
    }
  }

  @SuppressWarnings("rawtypes")
  public void auditDelete(Long personId, Long userId, T entity, SingularAttribute field, boolean logValueData) {
    try {
      Long entityId = (Long) ReflectionApiUtils.getObjectFieldValue(entity, "id", true);
      String data = logValueData ? String.valueOf(ReflectionApiUtils.getObjectFieldValue(entity, field.getName(), true)) : null; 
      if (data != null && data.length() > 255) {
        data = data.substring(0, 250) + "...";
      }
      createAuditLogEntry(personId, userId, AuditLogType.DEL, getGenericTypeClass().getSimpleName(), entityId, field.getName(), data);
    }
    catch (Exception e) {
      // Reflection failure 
    }
  }

  public void auditView(Long personId, Long userId, String view) {
    createAuditLogEntry(personId, userId, AuditLogType.VIEW, view, null, null, null);
  }

  public void auditView(Long personId, Long userId, String view, T entity) {
    try {
      Long entityId = (Long) ReflectionApiUtils.getObjectFieldValue(entity, "id", true);
      createAuditLogEntry(personId, userId, AuditLogType.VIEW, view, entityId, null, null);
    }
    catch (Exception e) {
      // Reflection failure 
    }
  }

//  protected abstract EntityManager getEntityManager();
  
//  protected EntityManager getEntityManager() {
//    return THREAD_LOCAL.get();
//  }
//  
//  public static void setEntityManager(EntityManager entityManager) {
//    if (entityManager == null)
//      THREAD_LOCAL.remove();
//    else
//      THREAD_LOCAL.set(entityManager);
//  }
  
  protected Class<?> getGenericTypeClass() {
    ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
    return (Class<?>) parameterizedType.getActualTypeArguments()[0];
  }
  
  private void createAuditLogEntry(Long personId, Long userId, AuditLogType type, String target, Long entityId, String field, String data) {
    AuditLog auditLog = new AuditLog();
    
    auditLog.setAuthorId(getLoggedUserId());
    auditLog.setTarget(target);
    auditLog.setData(data);
    auditLog.setDate(new Date());
    auditLog.setEntityId(entityId);
    auditLog.setField(field);
    auditLog.setPersonId(personId);
    auditLog.setType(type);
    auditLog.setUserId(userId);
    
    getEntityManager().persist(auditLog);
  }

  private Long getLoggedUserId() {
    HttpSession httpSession = ThreadSessionContainer.getSession();
    if (httpSession != null) {
      return (Long) httpSession.getAttribute("loggedUserId");
    }
    
    return null;
  }
  
//  private static final ThreadLocal<EntityManager> THREAD_LOCAL = new ThreadLocal<EntityManager>();
  
//  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }
  
  @PersistenceContext
  private EntityManager entityManager;


}
