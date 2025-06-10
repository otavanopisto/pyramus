package fi.otavanopisto.pyramus.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import org.hibernate.CacheMode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

@Stateless
public class SystemDAO {
  
  public SystemDAO() {
  }
  
  // JPA methods

  @SuppressWarnings({ "unchecked", "rawtypes" }) 
  public Object findEntityById(Class referencedClass, Object id) {
    EntityManager entityManager = getEntityManager();
    return entityManager.find(referencedClass, id);
  }
  
  public SingularAttribute<?, ?> getEntityIdAttribute(Class<?> entityClass) {
    EntityType<?> entityType = getEntityManager().getMetamodel().entity(entityClass);
    return entityType.getId(entityType.getIdType().getJavaType());
  }

  public Set<EntityType<?>> getEntities() {
    return getEntityManager().getMetamodel().getEntities();
  }
  
  public jakarta.persistence.metamodel.Attribute<?, ?> getEntityAttribute(Class<?> entityClass, String fieldName) {
    EntityType<?> entityType = getEntityManager().getMetamodel().entity(entityClass);
    return entityType.getAttribute(fieldName);
  }
  
  public Set<jakarta.persistence.metamodel.Attribute<?, ?>> getEntityAttributes(Class<?> entityClass) {
    Set<jakarta.persistence.metamodel.Attribute<?, ?>> result = new HashSet<>();
    
    EntityType<?> entityType = getEntityManager().getMetamodel().entity(entityClass);
    for (jakarta.persistence.metamodel.Attribute<?, ?> attribute : entityType.getAttributes()) {
      result.add(attribute);
    }
    
    return result;
  }
  
  public void persistEntity(Object entity) {
    getEntityManager().persist(entity);
  }
  
  public Set<ConstraintViolation<Object>> validateEntity(Object entity) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator().validate(entity);
  }

  public Query createJPQLQuery(String jpql) {
    return getEntityManager().createQuery(jpql);
  }
  
  // Hibernate search methods
 
  public List<Class<?>> getIndexedEntities() {
    List<Class<?>> result = new ArrayList<>();
    
    EntityManager entityManager = getEntityManager();
    Metamodel metamodel = entityManager.getEntityManagerFactory().getMetamodel();
    for (EntityType<?> entityType : metamodel.getEntities()) {
      Class<?> entityClass = entityType.getJavaType();
      if (entityClass.isAnnotationPresent(Indexed.class)) {
        result.add(entityClass);
      }
    }
    
    return result;
  }

  public void reindexHibernateSearchObjects(Class<?> entity, int batchSize) throws InterruptedException {
    EntityManager em = getEntityManager();
    SearchSession session = Search.session(em);
    session
      .massIndexer(entity)
      .batchSizeToLoadObjects(batchSize)
      .threadsToLoadObjects(1)
      .cacheMode(CacheMode.IGNORE)
      .startAndWait();
  }
  
  public EntityManager getEntityManager() {
    return entityManager;
  }
  
  @PersistenceContext
  private EntityManager entityManager;
}
