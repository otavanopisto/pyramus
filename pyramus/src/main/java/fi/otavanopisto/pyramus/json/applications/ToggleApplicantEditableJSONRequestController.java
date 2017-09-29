package fi.otavanopisto.pyramus.json.applications;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ToggleApplicantEditableJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Long id = NumberUtils.toLong(requestContext.getRequest().getParameter("id"));
    Application application = applicationDAO.findById(id);
    Boolean applicantEditable = Boolean.valueOf(requestContext.getRequest().getParameter("applicantEditable"));
    applicationDAO.updateApplicantEditable(application, applicantEditable, staffMember);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
