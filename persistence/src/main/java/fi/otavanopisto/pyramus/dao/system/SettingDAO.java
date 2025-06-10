package fi.otavanopisto.pyramus.dao.system;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.domainmodel.system.Setting_;

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
