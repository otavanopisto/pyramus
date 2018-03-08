package fi.otavanopisto.pyramus.views.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatureState;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.json.applications.OnnistuuClient;

public class CompleteApplicationViewController extends PyramusViewController {
  
  private static final Logger logger = Logger.getLogger(CompleteApplicationViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    try {
      
      // Invitation identifier of the signature
      
      String invitationId = pageRequestContext.getString("invitationId"); 
      if (StringUtils.isBlank(invitationId)) {
        logger.warning("Missing invitationId");
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }

      // Find document based on invitationId and ensure the signing process is in the correct state
      
      ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
      ApplicationSignatures applicationSignatures = applicationSignaturesDAO.findByApplicantInvitationId(invitationId);
      if (applicationSignatures == null) {
        logger.warning(String.format("Application with applicantInvitationId %s not found", invitationId));
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      if (applicationSignatures.getApplicantDocumentState() != ApplicationSignatureState.INVITATION_CREATED) {
        logger.warning(String.format("Invalid applicant document state %s", applicationSignatures.getApplicantDocumentState()));
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }

      // Ensure application state
      
      Application application = applicationSignatures.getApplication();
      if (application.getState() != ApplicationState.APPROVED_BY_SCHOOL) {
        logger.warning(String.format("Invalid application state %s", application.getState()));
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }

      // Check that the signing process completed successfully
      
      OnnistuuClient onnistuuClient = OnnistuuClient.getInstance();
      if (onnistuuClient.isSigned(invitationId)) {
        
        // Update signature state
        
        applicationSignatures = applicationSignaturesDAO.updateApplicantDocumentState(applicationSignatures, ApplicationSignatureState.SIGNED);
        
        // Update application state
        
        ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
        application = applicationDAO.updateApplicationStateAsApplicant(application, ApplicationState.APPROVED_BY_APPLICANT);
        
        // Send email and add notification about application state having changed
        
        String documentUrl = String.format("https://www.onnistuu.fi/api/v1/invitation/%s/%s/files/0",
            applicationSignatures.getApplicantInvitationId(),
            applicationSignatures.getApplicantInvitationToken());
        String notificationPostfix = String.format("<a href=\"%s\" target=\"_blank\">Opiskelupaikan vastaanottoasiakirja</a>", documentUrl); 
        ApplicationUtils.sendNotifications(application, pageRequestContext.getRequest(), null, false, notificationPostfix);
      }
      else {
        logger.severe(String.format("Applicant signature for application %d was not completed successfully", application.getId()));
        pageRequestContext.getRequest().setAttribute("invalidState", Boolean.TRUE);
        pageRequestContext.getRequest().setAttribute("invalidStateReason", "Sähköinen allekirjoitus peruutettiin tai se epäonnistui");
      }
      pageRequestContext.setIncludeJSP("/templates/applications/application-done.jsp");
    }
    catch (Exception e) { 
      logger.log(Level.SEVERE, "Unable to serve error response", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
