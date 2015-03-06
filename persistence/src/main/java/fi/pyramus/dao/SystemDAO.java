package fi.pyramus.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.hibernate.CacheMode;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

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
  
  public javax.persistence.metamodel.Attribute<?, ?> getEntityAttribute(Class<?> entityClass, String fieldName) {
    EntityType<?> entityType = getEntityManager().getMetamodel().entity(entityClass);
    return entityType.getAttribute(fieldName);
  }
  
  public Set<javax.persistence.metamodel.Attribute<?, ?>> getEntityAttributes(Class<?> entityClass) {
    Set<javax.persistence.metamodel.Attribute<?, ?>> result = new HashSet<javax.persistence.metamodel.Attribute<?,?>>();
    
    EntityType<?> entityType = getEntityManager().getMetamodel().entity(entityClass);
    for (javax.persistence.metamodel.Attribute<?, ?> attribute : entityType.getAttributes()) {
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
    List<Class<?>> result = new ArrayList<Class<?>>();
    
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
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
    MassIndexer massIndexer = fullTextEntityManager.createIndexer(entity);
    massIndexer.batchSizeToLoadObjects(batchSize);
    massIndexer.threadsToLoadObjects(1);
    massIndexer.cacheMode(CacheMode.IGNORE);
    massIndexer.startAndWait();
  }
  
  private EntityManager getEntityManager() {
    return entityManager;
  }
  
  @PersistenceContext
  private EntityManager entityManager;
}
