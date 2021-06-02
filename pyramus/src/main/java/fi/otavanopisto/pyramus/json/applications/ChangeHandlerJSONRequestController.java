package fi.otavanopisto.pyramus.json.applications;

import java.util.List;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ChangeHandlerJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember source = staffMemberDAO.findById(requestContext.getLong("sourceStaffMember"));
    StaffMember target = staffMemberDAO.findById(requestContext.getLong("targetStaffMember"));
    List<Application> applications = applicationDAO.listByHandlerAndArchived(source, Boolean.FALSE);
    for (Application application : applications) {
      switch (application.getState()) {
        case PROCESSING:
        case WAITING_STAFF_SIGNATURE:
        case STAFF_SIGNED:
        case APPROVED_BY_SCHOOL:
        case APPROVED_BY_APPLICANT:
          applicationDAO.updateApplicationHandler(application, target);
          break;
        default:
          break;
      }
    }
    String redirectURL = requestContext.getRequest().getContextPath() + "/applications/changehandler.page";
    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
