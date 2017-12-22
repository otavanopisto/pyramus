package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatureState;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONObject;

public class GenerateAcceptanceDocumentJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger
      .getLogger(GenerateAcceptanceDocumentJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {

    // Ensure user has SSN to be able to eventually sign the generated document

    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
    if (staffMember == null) {
      logger.warning("Current user cannot be resolved");
      fail(requestContext, "Et ole kirjautunut sisään");
      return;
    }
    if (StringUtils.isBlank(staffMember.getPerson().getSocialSecurityNumber())) {
      logger.warning("Current user lacks social security number");
      fail(requestContext, "Allekirjoittamiseen vaadittua henkilötunnusta ei ole asetettu");
      return;
    }

    // Find application and ensure its state

    Long id = requestContext.getLong("id");
    if (id == null) {
      logger.warning("Missing application id");
      fail(requestContext, "Puuttuva hakemustunnus");
      return;
    }
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Application application = applicationDAO.findById(id);
    if (application == null) {
      logger.warning(String.format("Application with id %d not found", id));
      fail(requestContext, String.format("Hakemusta tunnuksella %d ei löytynyt", id));
      return;
    }
    if (application.getState() != ApplicationState.WAITING_STAFF_SIGNATURE) {
      logger.warning(String.format("Application with id %d in incorrect state (%s)", id, application.getState()));
      fail(requestContext, "Hakemus ei ole allekirjoitettavassa tilassa");
      return;
    }

    // Signatures tracking

    ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
    ApplicationSignatures signatures = applicationSignaturesDAO.findByApplication(application);
    if (signatures == null) {
      signatures = applicationSignaturesDAO.create(application);
    }
    if (signatures.getStaffDocumentState() == ApplicationSignatureState.SIGNED) {
      fail(requestContext, "Hyväksymisasiakirja on jo allekirjoitettu");
      return;
    }

    // Gather required dynamic data for the PDF document

    JSONObject formData = JSONObject.fromObject(application.getFormData());
    String applicantName = String.format("%s %s", getFormValue(formData, "field-first-names"),
        getFormValue(formData, "field-last-name"));
    String line = application.getLine();
    String documentName = String.format("Hyväksyntä: %s", applicantName);

    OnnistuuClient onnistuuClient = OnnistuuClient.getInstance();
    try {

      // Generate PDF

      byte[] pdf = onnistuuClient.generateStaffSignatureDocument(requestContext, applicantName, line, staffMember);
      String documentId = null;
      
      // Generate Onnistuu document
      if (signatures.getStaffDocumentId() == null) {
        documentId = onnistuuClient.createDocument(documentName);
        signatures = applicationSignaturesDAO.updateStaffDocument(signatures, documentId, null, null,
            ApplicationSignatureState.DOCUMENT_CREATED);
      }
      else {
        documentId = signatures.getStaffDocumentId();
      }

      // Attach PDF to Onnistuu document

      if (signatures.getStaffDocumentState() == ApplicationSignatureState.DOCUMENT_CREATED) {
        onnistuuClient.addPdf(documentId, pdf);
        signatures = applicationSignaturesDAO.updateStaffDocument(signatures, documentId,
            null, null, ApplicationSignatureState.PDF_UPLOADED);
      }
      
      // Create invitation

//      if (signatures.getStaffDocumentState() == ApplicationSignatureState.PDF_UPLOADED) {
//        onnistuuClient.createInvitation(documentId);
//        signatures = applicationSignaturesDAO.updateStaffDocument(signatures, documentId,
//            null, null, ApplicationSignatureState.PDF_UPLOADED);
//      }

      // Respond with URL to view the PDF

      requestContext.addResponseParameter("status", "OK");
      requestContext.addResponseParameter("documentUrl",
          String.format("/applications/onnistuudocument.binary?documentId=%s", documentId));
    }
    catch (OnnistuuClientException e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
      fail(requestContext, e.getMessage());
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

  private void fail(JSONRequestContext requestContext, String reason) {
    requestContext.addResponseParameter("status", "FAIL");
    requestContext.addResponseParameter("reason", reason);
  }

  private String getFormValue(JSONObject object, String key) {
    return object.has(key) ? object.getString(key) : null;
  }

}