package fi.pyramus.dao.system;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.system.SettingKey;
import fi.pyramus.domainmodel.system.SettingKey_;

@Stateless
public class SettingKeyDAO extends PyramusEntityDAO<SettingKey> {

  public SettingKey create(String name) {
    EntityManager entityManager = getEntityManager();
    
    SettingKey settingKey = new SettingKey();
    settingKey.setName(name);
    
    entityManager.persist(settingKey);
    
    return settingKey;
  }
  
  public SettingKey findByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SettingKey> criteria = criteriaBuilder.createQuery(SettingKey.class);
    Root<SettingKey> root = criteria.from(SettingKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(SettingKey_.name), name)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
    
  }
  
  public void update(SettingKey settingKey, String name) {
    EntityManager entityManager = getEntityManager();
    
    settingKey.setName(name);
    
    entityManager.persist(settingKey);
  }
  
  @Override
  public void delete(SettingKey settingKey) {
    super.delete(settingKey);
  }
  
}
