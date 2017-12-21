package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONObject;

public class ListSignatureSourcesJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ListSignatureSourcesJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      OnnistuuClient onnistuuClient = OnnistuuClient.getInstance();
      JSONObject jsonObject = onnistuuClient.listSignatureSources();
      requestContext.addResponseParameter("sources", jsonObject);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading signature soruces", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}