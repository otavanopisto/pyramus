package fi.otavanopisto.pyramus.dao.system;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Configuration;
import fi.otavanopisto.pyramus.domainmodel.system.Configuration_;

@Stateless
public class ConfigurationDAO extends PyramusEntityDAO<Configuration> {

  public Configuration create(String name, String value) {
    EntityManager entityManager = getEntityManager();
    
    Configuration configuration = new Configuration();
    configuration.setName(name);
    configuration.setValue(value);
    
    entityManager.persist(configuration);
    
    return configuration;
  }
  
  public Configuration findByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Configuration> criteria = criteriaBuilder.createQuery(Configuration.class);
    Root<Configuration> root = criteria.from(Configuration.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Configuration_.name), name)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public void update(Configuration configuration, String value) {
    EntityManager entityManager = getEntityManager();
    
    configuration.setValue(value);
    
    entityManager.persist(configuration);
  }
  
}
