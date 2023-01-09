package fi.otavanopisto.pyramus.json.applications;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
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
            String staffDocumentUrl = String.format("/applications/getdocument.binary?documentId=%s&filename=%s",
                applicationSignatures.getStaffDocumentId(),
                sanitizeFilename(
                    String.format("%s-%s-oppilaitos.pdf", applicationSignatures.getApplication().getFirstName(),
                        applicationSignatures.getApplication().getLastName())));
            requestContext.addResponseParameter("staffDocumentUrl", staffDocumentUrl);
          }
          if (StringUtils.isNotBlank(applicationSignatures.getApplicantInvitationId())) {
            String applicantDocumentUrl = String.format("/applications/getdocument.binary?documentId=%s&filename=%s",
                applicationSignatures.getApplicantDocumentId(),
                sanitizeFilename(
                    String.format("%s-%s-hakija.pdf", applicationSignatures.getApplication().getFirstName(),
                        applicationSignatures.getApplication().getLastName())));
            requestContext.addResponseParameter("applicantDocumentUrl", applicantDocumentUrl);
          }
          else if ((application.getState() == ApplicationState.APPROVED_BY_SCHOOL
              || application.getState() == ApplicationState.TRANSFERRED_AS_STUDENT)
              && ApplicationUtils.isUnderage(application)) {
            requestContext.addResponseParameter("applicantDocumentUrl",
                String.format("/1/applications/generateapplicantdocument?id=%d", application.getId()));
          }
        }
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

  private String sanitizeFilename(String filename) {
    filename = StringUtils.trim(filename);
    if (StringUtils.isEmpty(filename)) {
      return filename;
    }
    return StringUtils.replace(
        StringUtils.lowerCase(StringUtils.strip(RegExUtils.removePattern(filename, "[\\\\/:*?\"<>|]"), ".")), " ", "-");
  }

}
