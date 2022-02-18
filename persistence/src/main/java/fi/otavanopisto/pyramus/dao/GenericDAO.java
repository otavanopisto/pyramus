package fi.otavanopisto.pyramus.dao;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fi.otavanopisto.pyramus.dao.auditlog.AuditLogDAO;
import fi.otavanopisto.pyramus.domainmodel.audit.AuditLogType;
import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.util.ReflectionApiUtils;

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
  
  public void auditCreate(Long personId, Long userId, T entity, String field, boolean logValueData) {
    try {
      AuditLogDAO auditLogDAO = DAOFactory.getInstance().getAuditLogDAO();
      Long entityId = (Long) ReflectionApiUtils.getObjectFieldValue(entity, "id", true);
      String data = logValueData ? String.valueOf(ReflectionApiUtils.getObjectFieldValue(entity, field, true)) : null; 
      auditLogDAO.create(personId, userId, AuditLogType.ADD, getGenericTypeClass().getSimpleName(), entityId, field, data);
    }
    catch (Exception e) {
      // Reflection failure 
    }
  }
  
  public void auditUpdate(Long personId, Long userId, T entity, String field, Object newValue, boolean logValueData) {
    try {
      AuditLogDAO auditLogDAO = DAOFactory.getInstance().getAuditLogDAO();
      Object oldValue = ReflectionApiUtils.getObjectFieldValue(entity, field, true);
      if (!Objects.equals(oldValue, newValue)) {
        Long entityId = (Long) ReflectionApiUtils.getObjectFieldValue(entity, "id", true);
        String data = logValueData ? String.valueOf(oldValue + " -> " + newValue) : null;
        auditLogDAO.create(personId, userId, AuditLogType.MOD, getGenericTypeClass().getSimpleName(), entityId, field, data);
      }
    }
    catch (Exception e) {
      // Reflection failure 
    }
  }

  public void auditDelete(Long personId, Long userId, T entity, String field, boolean logValueData) {
    try {
      AuditLogDAO auditLogDAO = DAOFactory.getInstance().getAuditLogDAO();
      Long entityId = (Long) ReflectionApiUtils.getObjectFieldValue(entity, "id", true);
      String data = logValueData ? String.valueOf(ReflectionApiUtils.getObjectFieldValue(entity, field, true)) : null; 
      auditLogDAO.create(personId, userId, AuditLogType.DEL, getGenericTypeClass().getSimpleName(), entityId, field, data);
    }
    catch (Exception e) {
      // Reflection failure 
    }
  }

  public void auditView(Long personId, Long userId, Class<?> c) {
    AuditLogDAO auditLogDAO = DAOFactory.getInstance().getAuditLogDAO();
    auditLogDAO.create(personId, userId, AuditLogType.VIEW, c.getSimpleName(), null, null, null);
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
  
//  private static final ThreadLocal<EntityManager> THREAD_LOCAL = new ThreadLocal<EntityManager>();
  
//  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }
  
  @PersistenceContext
  private EntityManager entityManager;
}
