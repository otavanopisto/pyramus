package fi.otavanopisto.pyramus.views.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatureState;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.json.applications.OnnistuuClient;

public class SignedAcceptanceDocumentViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(SignedAcceptanceDocumentViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    try {    

      // Ensure logged in user
      
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());
      if (staffMember == null) {
        logger.warning(String.format("Not logged in"));
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
      }
      
      // invitationId refers to the signed document
      // mode refers to the final redirect address 
      
      String invitationId = pageRequestContext.getString("invitationId"); 
      if (StringUtils.isBlank(invitationId)) {
        logger.warning("Missing invitationId");
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }
      String mode = pageRequestContext.getString("mode");
      if (StringUtils.isBlank(mode)) {
        logger.warning("Missing mode");
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }
      
      // Find document based on invitationId and ensure the signing process is in the correct state
      
      ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
      ApplicationSignatures applicationSignatures = applicationSignaturesDAO.findByStaffInvitationId(invitationId);
      if (applicationSignatures == null) {
        logger.warning(String.format("Application with staffInvitationId %s not found", invitationId));
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      if (applicationSignatures.getStaffDocumentState() != ApplicationSignatureState.INVITATION_CREATED) {
        logger.warning(String.format("Invalid staff document state %s", applicationSignatures.getStaffDocumentState()));
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }
      
      // Ensure application state
      
      Application application = applicationSignatures.getApplication();
      if (application.getState() != ApplicationState.WAITING_STAFF_SIGNATURE) {
        logger.warning(String.format("Invalid application state %s", application.getState()));
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }
      
      OnnistuuClient onnistuuClient = OnnistuuClient.getInstance();
      if (onnistuuClient.isSigned(invitationId)) {
        
        // Update signature state
        
        applicationSignatures = applicationSignaturesDAO.updateStaffDocumentState(applicationSignatures, ApplicationSignatureState.SIGNED);
        
        // Update application state
        
        ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
        application = applicationDAO.updateApplicationState(application, ApplicationState.STAFF_SIGNED, staffMember);
        
        // Send email and add notification about application state having changed
        
        String documentUrl = String.format("https://www.onnistuu.fi/api/v1/invitation/%s/%s/files/0",
            applicationSignatures.getStaffInvitationId(),
            applicationSignatures.getStaffInvitationToken());
        String notificationPostfix = String.format("<a href=\"%s\" target=\"_blank\">Hyväksymisasiakirja</a>", documentUrl); 
        ApplicationUtils.sendNotifications(application, pageRequestContext.getRequest(), staffMember, false, notificationPostfix);
      }
      else {
        logger.severe(String.format("Staff signature for application %d was not completed successfully", application.getId()));
      }
      
      // Redirect to application management (view or edit)
      
      String contextPath = pageRequestContext.getRequest().getContextPath();
      if ("edit".equals(mode)) {
        pageRequestContext.setRedirectURL(String.format("%s/applications/manage.page?application=%d", contextPath, application.getId()));
      }
      else {
        pageRequestContext.setRedirectURL(String.format("%s/applications/view.page?application=%d", contextPath, application.getId()));
      }
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Unable to serve error response", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
