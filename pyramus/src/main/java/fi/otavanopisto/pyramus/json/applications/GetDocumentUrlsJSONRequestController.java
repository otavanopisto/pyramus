package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class GetDocumentUrlsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    Long id = requestContext.getLong("id");
    if (id != null) {
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(id);
      if (application != null) {
        ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
        ApplicationSignatures applicationSignatures = applicationSignaturesDAO.findByApplication(application);
        if (applicationSignatures != null) {
          if (StringUtils.isNotBlank(applicationSignatures.getStaffInvitationId())) {
            String staffDocumentUrl = String.format("https://www.onnistuu.fi/api/v1/invitation/%s/%s/files/0",
                applicationSignatures.getStaffInvitationId(),
                applicationSignatures.getStaffInvitationToken());
            requestContext.addResponseParameter("staffDocumentUrl", staffDocumentUrl);
          }
          if (StringUtils.isNotBlank(applicationSignatures.getApplicantInvitationId())) {
            String applicantDocumentUrl = String.format("https://www.onnistuu.fi/api/v1/invitation/%s/%s/files/0",
                applicationSignatures.getApplicantInvitationId(),
                applicationSignatures.getApplicantInvitationToken());
            requestContext.addResponseParameter("applicantDocumentUrl", applicantDocumentUrl);
          }
        }
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
