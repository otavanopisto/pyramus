package fi.otavanopisto.pyramus.views.applications;

import java.util.Comparator;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ChangeHandlerViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    List<StaffMember> staffMembers = staffMemberDAO.listUnarchived();
    staffMembers.sort(Comparator.comparing(StaffMember::getLastName).thenComparing(StaffMember::getFirstName));
    pageRequestContext.getRequest().setAttribute("staffMembers", staffMembers);
    pageRequestContext.setIncludeJSP("/templates/applications/changehandler.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
