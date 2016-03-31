package fi.otavanopisto.pyramus.framework;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

public abstract class JSONRequestController implements fi.internetix.smvc.controllers.JSONRequestController {

  public abstract UserRole[] getAllowedRoles();

  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    UserRole[] roles = getAllowedRoles();
    if (!contains(roles, UserRole.EVERYONE)) {
      if (!requestContext.isLoggedIn())
        throw new LoginRequiredException();
      else {
        Long loggedUserId = requestContext.getLoggedUserId();
        
        StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
        StaffMember user = userDAO.findById(loggedUserId);
        
        Role role = user.getRole();
        
        // TODO Ugly hax
        UserRole userRole = UserRole.getRole(role.getValue());
        
        if (!contains(roles, userRole))
          throw new AccessDeniedException(requestContext.getRequest().getLocale());
      }
    }
  }

  /**
   * Returns whether the given role is included in the given role array.
   * 
   * @param roles The roles
   * @param role The role
   * 
   * @return <code>true</code> if the roles array contains the given role, otherwise
   * <code>false</code>
   */
  private boolean contains(UserRole[] roles, UserRole role) {
    for (int i = 0; i < roles.length; i++) {
      if (roles[i] == role) {
        return true;
      }
    }
    return false;
  }
}
