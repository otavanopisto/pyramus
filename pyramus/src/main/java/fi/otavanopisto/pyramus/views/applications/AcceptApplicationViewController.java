package fi.otavanopisto.pyramus.views.applications;

import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
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
import net.sf.json.JSONObject;

public class AcceptApplicationViewController extends PyramusViewController {
  
  private static final Logger logger = Logger.getLogger(AcceptApplicationViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    
    // DEBUG START
    
    if (StringUtils.isNotBlank(pageRequestContext.getString("debug"))) {
      try {
        OnnistuuClient oc = OnnistuuClient.getInstance();
        byte[] bytes;
        if (pageRequestContext.getString("debug").equals("staff")) {
          StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
          StaffMember staffMember = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());
          bytes = oc.generateStaffSignatureDocument(pageRequestContext,
              "Kerkko Eemeli Perämetsä",
              "nettilukio",
              staffMember);
        }
        else {
          bytes = oc.generateApplicantSignatureDocument(pageRequestContext,
              "Nettilukio",
              "Kerkko Eemeli Perämetsä",
              "123456-1234",
              "Kerkkopolku 3, 33100 Kerkkola, Suomi",
              "Putaa",
              "Suomi",
              "0402583037",
              "kerkko@perametsa.fi");
        }
        pageRequestContext.getResponse().setContentType("application/pdf");
        IOUtils.write(bytes, pageRequestContext.getResponse().getOutputStream());
        return;
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    // DEBUG END
    
    
    
    
    String applicationId = pageRequestContext.getRequest().getParameter("application");
      
    // Ensure the application itself exists
    
    Application application = null;
    if (!StringUtils.isBlank(applicationId)) {
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      application = applicationDAO.findByApplicationIdAndArchived(applicationId, Boolean.FALSE);
    }
    if (application == null) {
      pageRequestContext.getRequest().setAttribute("invalidState", Boolean.TRUE);
      pageRequestContext.getRequest().setAttribute("invalidStateReason", "Hakemusta ei löytynyt järjestelmästä");
      pageRequestContext.setIncludeJSP("/templates/applications/application-accept.jsp");
      return;
    }
    
    // Ensure the application is in correct state
    
    if (application.getState() == ApplicationState.APPROVED_BY_APPLICANT) {
      logger.warning(String.format("Application %d has already been accepted by applicant", application.getId()));
      pageRequestContext.getRequest().setAttribute("invalidState", Boolean.TRUE);
      pageRequestContext.getRequest().setAttribute("invalidStateReason", "Opiskelupaikka on jo vastaanotettu");
      pageRequestContext.setIncludeJSP("/templates/applications/application-accept.jsp");
      return;
    }
    if (application.getState() != ApplicationState.APPROVED_BY_SCHOOL) {
      logger.warning(String.format("Application %d is in invalid state %s", application.getId(), application.getState().toString()));
      pageRequestContext.getRequest().setAttribute("invalidState", Boolean.TRUE);
      pageRequestContext.getRequest().setAttribute("invalidStateReason", "Opiskelupaikka ei ole vielä vastaanotettavissa");
      pageRequestContext.setIncludeJSP("/templates/applications/application-accept.jsp");
      return;
    }
    
    // Ensure the application signatures are ready for applicant signature
    
    ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
    ApplicationSignatures applicationSignatures = applicationSignaturesDAO.findByApplication(application);
    if (applicationSignatures == null || applicationSignatures.getApplicantDocumentState() != ApplicationSignatureState.INVITATION_CREATED) {
      logger.warning(String.format("Signatures for application %d is not ready for applicant signing", application.getId()));
      pageRequestContext.getRequest().setAttribute("invalidState", Boolean.TRUE);
      pageRequestContext.getRequest().setAttribute("invalidStateReason", "Opiskelupaikka ei ole vielä vastaanotettavissa");
      pageRequestContext.setIncludeJSP("/templates/applications/application-accept.jsp");
      return;
    }
    
    // Add application details to page request
    
    JSONObject formData = JSONObject.fromObject(application.getFormData());
    String applicantName = String.format("%s %s", getFormValue(formData, "field-first-names"), getFormValue(formData, "field-last-name"));
    String ssn = ApplicationUtils.constructSSN(getFormValue(formData, "field-birthday"), getFormValue(formData, "field-ssn-end"));
    String line = ApplicationUtils.applicationLineUiValue(application.getLine());
    String docUrl = String.format("https://www.onnistuu.fi/api/v1/invitation/%s/%s/files/0",
        applicationSignatures.getApplicantInvitationId(),
        applicationSignatures.getApplicantInvitationToken());
    
    pageRequestContext.getRequest().setAttribute("applicationEntityId", application.getId());
    pageRequestContext.getRequest().setAttribute("applicantName", applicantName);
    pageRequestContext.getRequest().setAttribute("applicantSsn", ssn);
    pageRequestContext.getRequest().setAttribute("applicantLine", line);
    pageRequestContext.getRequest().setAttribute("applicantDocumentUrl", docUrl);
    pageRequestContext.setIncludeJSP("/templates/applications/application-accept.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

  private String getFormValue(JSONObject object, String key) {
    return object.has(key) ? object.getString(key) : null;
  }

}
