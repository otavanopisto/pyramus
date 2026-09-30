package fi.otavanopisto.pyramus.views.studentparents;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StudentParentInvitationDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentInvitation;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.util.StringUtils;

public class StudentParentRegistrationViewController extends PyramusViewController {

  @Override
  public void process(PageRequestContext requestContext) {
    // Cannot use the view if auth provider is not present 
    if (AuthenticationProviderVault.getInstance().getInternalAuthenticationProvider("internal") == null) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Operation not available.");
    }
    
    if (StringUtils.equals(requestContext.getString("status"), "ok")) {
      requestContext.getRequest().setAttribute("credentialsCreated", Boolean.TRUE);
    }
    else {
      StudentParentInvitationDAO studentParentInvitationDAO = DAOFactory.getInstance().getStudentParentInvitationDAO();
      String hash = StringUtils.trim(requestContext.getString("c"));
      StudentParentInvitation invitation = studentParentInvitationDAO.findByHash(hash);
      boolean invalidInvitation = invitation == null || invitation.isExpired();
      boolean invalidLogin = false;
      
      if (requestContext.isLoggedIn()) {
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        User loggedUser = userDAO.findById(requestContext.getLoggedUserId());
        // Invalid login if loggedUser is null (somehow conflicting with loggedIn) or not a StudentParent
        invalidLogin = !(loggedUser instanceof StudentParent);
      }

      requestContext.getRequest().setAttribute("hash", hash);
      requestContext.getRequest().setAttribute("invalidLogin", invalidLogin);
      requestContext.getRequest().setAttribute("invalidInvitation", invalidInvitation);
    }
    
    requestContext.setIncludeJSP("/templates/users/studentparentregistration.jsp");
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
