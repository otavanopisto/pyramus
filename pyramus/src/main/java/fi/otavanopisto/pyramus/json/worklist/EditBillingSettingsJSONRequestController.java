package fi.otavanopisto.pyramus.json.worklist;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.worklist.WorklistBillingSettingsDAO;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistBillingSettings;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.rest.model.worklist.CourseBillingRestModel;

public class EditBillingSettingsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    WorklistBillingSettingsDAO worklistBillingSettingsDAO = DAOFactory.getInstance().getWorklistBillingSettingsDAO();
    String settings = requestContext.getString("settings");
    
    // Delete existing document (mostly for easier manual testing)
    
    if (StringUtils.isEmpty(settings)) {
      List<WorklistBillingSettings> billingSettings = worklistBillingSettingsDAO.listAll();
      if (!billingSettings.isEmpty()) {
        for (WorklistBillingSettings billingSetting : billingSettings) {
          worklistBillingSettingsDAO.delete(billingSetting);
        }
      }
      return;
    }
    
    // Validate JSON
    
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.readValue(settings, CourseBillingRestModel.class);
    }
    catch (Exception e) {
      throw new SmvcRuntimeException(500, String.format("Malformatted settings document: %s", e.getMessage()));
    }
    
    // Create or update settings
    
    List<WorklistBillingSettings> billingSettings = worklistBillingSettingsDAO.listAll();
    if (billingSettings.isEmpty()) {
      worklistBillingSettingsDAO.create(settings);
    }
    else {
      worklistBillingSettingsDAO.update(billingSettings.get(0), settings);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
