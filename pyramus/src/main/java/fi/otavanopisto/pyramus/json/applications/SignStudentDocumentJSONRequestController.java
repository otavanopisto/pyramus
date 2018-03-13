package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatureState;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SignStudentDocumentJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(SignStudentDocumentJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    
    // Validate request parameters

    String applicationId = requestContext.getString("id");
    if (StringUtils.isBlank(applicationId)) {
      logger.warning("Missing application id");
      fail(requestContext, "Puuttuva hakemustunnus");
      return;
    }
    String ssn = requestContext.getString("ssn");
    if (StringUtils.isBlank(ssn)) {
      logger.warning("Missing social security number");
      fail(requestContext, "Puuttuva henkilötunnus");
      return;
    }
    String authService = requestContext.getString("authService");
    if (StringUtils.isBlank(authService)) {
      logger.warning("Missing authService");
      fail(requestContext, "Puuttuva tunnistustapa");
      return;
    }
    
    // Ensure application state
    
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Application application = applicationDAO.findByApplicationIdAndArchived(applicationId, Boolean.FALSE);
    if (application == null) {
      logger.warning(String.format("Application with id %s not found", applicationId));
      fail(requestContext, String.format("Hakemusta tunnuksella %s ei löytynyt", applicationId));
      return;
    }
    if (application.getState() != ApplicationState.APPROVED_BY_SCHOOL) {
      logger.warning(String.format("Application with id %s in incorrect state (%s)", applicationId, application.getState()));
      fail(requestContext, "Hakemus ei ole allekirjoitettavassa tilassa");
      return;
    }
    if (!StringUtils.equals(ssn, ApplicationUtils.extractSSN(application))) {
      logger.warning("Social security number mismatch");
      fail(requestContext, "Virheellinen henkilötunnus");
      return;
    }

    // Signatures tracking

    ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
    ApplicationSignatures signatures = applicationSignaturesDAO.findByApplication(application);
    if (signatures == null || signatures.getApplicantDocumentState() != ApplicationSignatureState.INVITATION_CREATED) {
      fail(requestContext, "Opiskelupaikan vastaanottoasiakirja ei ole allekirjoitettavassa tilassa");
      return;
    }

    // Return URL after signing
    
    StringBuilder returnUrl = new StringBuilder();
    returnUrl.append(requestContext.getRequest().getScheme());
    returnUrl.append("://");
    returnUrl.append(requestContext.getRequest().getServerName());
    returnUrl.append(":");
    returnUrl.append(requestContext.getRequest().getServerPort());
    returnUrl.append("/applications/done.page?invitationId=");
    returnUrl.append(signatures.getApplicantInvitationId());
    
    // Signing

    OnnistuuClient onnistuuClient = OnnistuuClient.getInstance();
    try {
      String completionUrl = onnistuuClient.getSignatureUrl(
          signatures.getApplicantInvitationId(), returnUrl.toString(), ssn, authService);

      // Respond with URL to complete the signature

      requestContext.addResponseParameter("status", "OK");
      requestContext.addResponseParameter("completionUrl", completionUrl);
    }
    catch (OnnistuuClientException e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
      fail(requestContext, e.getMessage());
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

  private void fail(JSONRequestContext requestContext, String reason) {
    requestContext.addResponseParameter("status", "FAIL");
    requestContext.addResponseParameter("reason", reason);
  }

}