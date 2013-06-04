package fi.pyramus.dao.system;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.system.Setting;
import fi.pyramus.domainmodel.system.SettingKey;
import fi.pyramus.domainmodel.system.Setting_;

@Stateless
public class SettingDAO extends PyramusEntityDAO<Setting> {

  public Setting create(SettingKey settingKey, String value) {
    EntityManager entityManager = getEntityManager();
    
    Setting setting = new Setting();
    setting.setKey(settingKey);
    setting.setValue(value);
    
    entityManager.persist(setting);
    
    return setting;
  }
  
  public Setting findByKey(SettingKey key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Setting> criteria = criteriaBuilder.createQuery(Setting.class);
    Root<Setting> root = criteria.from(Setting.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Setting_.key), key)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public void update(Setting setting, SettingKey settingKey, String value) {
    EntityManager entityManager = getEntityManager();
    
    setting.setKey(settingKey);
    setting.setValue(value);
    
    entityManager.persist(setting);
  }
  
  @Override
  public void delete(Setting setting) {
    super.delete(setting);
  }
  
}
