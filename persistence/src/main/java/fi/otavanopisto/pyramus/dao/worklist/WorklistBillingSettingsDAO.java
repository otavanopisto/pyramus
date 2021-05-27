package fi.otavanopisto.pyramus.dao.worklist;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistBillingSettings;

@Stateless
public class WorklistBillingSettingsDAO extends PyramusEntityDAO<WorklistBillingSettings> {

  public WorklistBillingSettings create(String settings) {
    WorklistBillingSettings worklistBillingSettings = new WorklistBillingSettings();
    worklistBillingSettings.setSettings(settings);
    return persist(worklistBillingSettings);
  }
  
  public WorklistBillingSettings update(WorklistBillingSettings worklistBillingSettings, String settings) {
    worklistBillingSettings.setSettings(settings);
    return persist(worklistBillingSettings);
  }

}
