package fi.otavanopisto.pyramus.framework;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageController;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.users.User;

public abstract class PyramusViewController implements PageController {

  // TODO: Remove this and UserRole
  public abstract UserRole[] getAllowedRoles();

  // TODO: Declare abstract
  public String getPermission() {
    return null;
  }
  
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    
    // TODO: Below for permission framework
//    if (getPermission() != null) {
//      try {
//        if (!PyramusRights.hasPermission(getPermission())) {
//          throw new AccessDeniedException(requestContext.getRequest().getLocale());
//        }
//      } catch (NamingException e) {
//        e.printStackTrace();
//        throw new AccessDeniedException(requestContext.getRequest().getLocale());
//      }
//    }
 
    // TODO: Remove old rights code
    
    UserRole[] roles = getAllowedRoles();
    if (!contains(roles, UserRole.EVERYONE)) {
      if (!requestContext.isLoggedIn()) {
        HttpServletRequest request = requestContext.getRequest();
        StringBuilder currentUrl = new StringBuilder(request.getRequestURL());
        String queryString = request.getQueryString();
        if (!StringUtils.isBlank(queryString)) {
          currentUrl.append('?');
          currentUrl.append(queryString);
        }
        throw new LoginRequiredException(currentUrl.toString());
      }
      else {
        Long loggedUserId = requestContext.getLoggedUserId();
        
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        User user = userDAO.findById(loggedUserId);
        UserRole userRole = null;
        
        switch (user.getRole()) {
          case ADMINISTRATOR:
            userRole = UserRole.ADMINISTRATOR;
            break;
          case EVERYONE:
            userRole = UserRole.EVERYONE;
            break;
          case MANAGER:
            userRole = UserRole.MANAGER;
            break;
          case GUEST:
          case STUDENT:
            userRole = UserRole.GUEST;
            break;
          case USER:
            userRole = UserRole.USER;
            break;
          case TEACHER:
            userRole = UserRole.TEACHER;
            break;
          case STUDY_GUIDER:
            userRole = UserRole.STUDY_GUIDER;
            break;
          case STUDY_PROGRAMME_LEADER:
            userRole = UserRole.STUDY_PROGRAMME_LEADER;
            break;
          default:
            break;
        }

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
  
  protected void setJsDataVariable(PageRequestContext pageRequestContext, String name, String value) {
    @SuppressWarnings("unchecked")
    Map<String, String> jsData = (Map<String, String>) pageRequestContext.getRequest().getAttribute("jsData");
    if (jsData == null) {
      jsData = new HashMap<>();
      pageRequestContext.getRequest().setAttribute("jsData", jsData);
    }
    
    jsData.put(name, value);
  }
  
}
