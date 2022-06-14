package fi.otavanopisto.pyramus.binary.applications;

import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.json.applications.OnnistuuClient;

public class GetDocumentBinaryRequestController extends BinaryRequestController {

  private static final Logger logger = Logger.getLogger(GetDocumentBinaryRequestController.class.getName());

  public void process(BinaryRequestContext requestContext) {
    String documentId = requestContext.getString("documentId");
    if (!StringUtils.isEmpty(documentId)) {
      OnnistuuClient onnistuuClient = OnnistuuClient.getInstance();
      try {
        requestContext.setFileName(requestContext.getString("filename"));
        requestContext.getResponse().setContentType("application/pdf");
        ServletOutputStream outputStream = requestContext.getResponse().getOutputStream();
        outputStream.write(onnistuuClient.getDocument(documentId));
      }
      catch (Exception e) {
        logger.severe(e.getMessage());
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
