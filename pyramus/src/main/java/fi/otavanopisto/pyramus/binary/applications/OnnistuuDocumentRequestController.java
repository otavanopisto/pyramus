package fi.otavanopisto.pyramus.binary.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.json.applications.OnnistuuClient;

public class OnnistuuDocumentRequestController extends BinaryRequestController {

  private static final Logger logger = Logger.getLogger(OnnistuuDocumentRequestController.class.getName());
  
  public void process(BinaryRequestContext binaryRequestContext) {
    try {
      String documentId = binaryRequestContext.getString("documentId");
      OnnistuuClient onnistuuClient = OnnistuuClient.getInstance();
      binaryRequestContext.setResponseContent(onnistuuClient.getPdf(documentId), "application/pdf");
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
