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

public abstract class PyramusViewController2 implements PageController {

  public PyramusViewController2(PyramusRequestControllerAccess access) {
    this.access = access;
  }
  
  protected abstract boolean checkAccess(RequestContext requestContext);

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    if (access != PyramusRequestControllerAccess.EVERYONE) {
      // REQUIRELOGIN is implied at this point unless new Access' are introduced
      if (!requestContext.isLoggedIn()) {
        throwLoginRequiredException(requestContext);
      }
      
      Long loggedUserId = requestContext.getLoggedUserId();
      UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
      User user = userDAO.findById(loggedUserId);
      UserUtils.checkManagementOrganizationPermission(user, requestContext.getRequest().getLocale());

      if (!checkAccess(requestContext)) {
        throw new AccessDeniedException(requestContext.getRequest().getLocale());
      }
    }
  }

  protected void throwLoginRequiredException(RequestContext requestContext) {
    HttpServletRequest request = requestContext.getRequest();
    StringBuilder currentUrl = new StringBuilder(request.getRequestURL());
    String queryString = request.getQueryString();
    if (!StringUtils.isBlank(queryString)) {
      currentUrl.append('?');
      currentUrl.append(queryString);
    }
    throw new LoginRequiredException(currentUrl.toString());
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
  
  private PyramusRequestControllerAccess access;
}
