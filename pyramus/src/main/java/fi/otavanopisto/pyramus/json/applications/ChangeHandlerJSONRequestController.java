package fi.otavanopisto.pyramus.json.applications;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ChangeHandlerJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember source = staffMemberDAO.findById(requestContext.getLong("sourceStaffMember"));
    StaffMember target = staffMemberDAO.findById(requestContext.getLong("targetStaffMember"));
    List<ApplicationState> states = Stream.of(
        ApplicationState.PROCESSING,
        ApplicationState.WAITING_STAFF_SIGNATURE,
        ApplicationState.STAFF_SIGNED,
        ApplicationState.APPROVED_BY_SCHOOL,
        ApplicationState.APPROVED_BY_APPLICANT).collect(Collectors.toList());
    List<Application> applications = applicationDAO.listByHandlerAndStatesAndArchived(source, states, Boolean.FALSE);
    for (Application application : applications) {
      applicationDAO.updateApplicationHandler(application, target);
    }
    String redirectURL = requestContext.getRequest().getContextPath() + "/applications/changehandler.page";
    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
