package fi.otavanopisto.pyramus.json.users;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;

public class PoseJSONRequestController extends JSONRequestController {
  
  private static final Logger logger = Logger.getLogger(PoseJSONRequestController.class.getName());
  
  public void process(JSONRequestContext jsonRequestContext) {
    Long userId = jsonRequestContext.getLong("userId"); 
    if (userId == null) {
      throw new SmvcRuntimeException(PyramusStatusCode.VALIDATION_FAILURE, "Invalid request"); 
    }
    
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    
    HttpSession session = jsonRequestContext.getRequest().getSession(false);
    if (session == null) {
      logger.log(Level.WARNING, String.format("Someone without session tried to pose user %d", userId));
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Unauthorized"); 
    }

    logger.log(Level.INFO, String.format("User %d attempting to pose user %d", jsonRequestContext.getLoggedUserId(), userId));

    User user = userDAO.findById(userId);
    if (user == null) {
      logger.log(Level.WARNING, String.format("User %d tried to pose non-existing user %d", jsonRequestContext.getLoggedUserId(), userId));
      throw new SmvcRuntimeException(PyramusStatusCode.PAGE_NOT_FOUND, "Requested user could not be found");
    }

    if (user.getArchived()) {
      logger.log(Level.WARNING, String.format("User %d tried to pose archived user %d", jsonRequestContext.getLoggedUserId(), userId));
      throw new SmvcRuntimeException(PyramusStatusCode.PAGE_NOT_FOUND, "Requested user could not be found");
    }
    
    /**
     * Logging in is restricted to default users so we try to represent the default user instead
     */
    if ((user.getPerson() != null) && (user.getPerson().getDefaultUser() != null))
      user = user.getPerson().getDefaultUser();
    
    if (user instanceof StaffMember) {
      Role role = ((StaffMember) user).getRole();
      switch (role) {
        case EVERYONE:
        case ADMINISTRATOR:
        case TRUSTED_SYSTEM:
          logger.log(Level.WARNING, String.format("User %d tried to pose user %d who is in forbidden role %s", jsonRequestContext.getLoggedUserId(), userId, role.name()));
          throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Unauthorized"); 
        default:
        break;
      }
      logger.log(Level.INFO, String.format("User %d posing staff member %d", jsonRequestContext.getLoggedUserId(), user.getId()));
      session.setAttribute("loggedUserRole", UserRole.valueOf(role.name()));
      session.setAttribute("loggedUserId", user.getId());
      session.setAttribute("loggedUserName", user.getFullName());
    } else if (user instanceof Student) {
      logger.log(Level.INFO, String.format("User %d posing student %d", jsonRequestContext.getLoggedUserId(), user.getId()));
      session.setAttribute("loggedUserId", user.getId());
      session.setAttribute("loggedUserName", user.getFullName()); 
    } else {
      logger.log(Level.SEVERE, String.format("User %d was not not a student or a staffMember", userId));
      throw new SmvcRuntimeException(PyramusStatusCode.PAGE_NOT_FOUND, "Requested user could not be found");
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getRequest().getContextPath() + "/index.page");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
