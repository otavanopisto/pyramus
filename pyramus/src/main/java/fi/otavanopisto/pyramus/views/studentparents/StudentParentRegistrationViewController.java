package fi.otavanopisto.pyramus.views.studentparents;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StudentParentDAO;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.framework.PyramusRequestControllerAccess;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.PyramusViewController2;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;

public class StudentParentRegistrationViewController extends PyramusViewController2 {

  public StudentParentRegistrationViewController() {
    super(PyramusRequestControllerAccess.EVERYONE);
  }

  @Override
  public void process(PageRequestContext requestContext) {
    // Cannot use the view if auth provider is not present 
    if (AuthenticationProviderVault.getInstance().getInternalAuthenticationProvider("internal") == null) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Operation not available.");
    }
    
    String hash = StringUtils.trim(requestContext.getString("c"));
    boolean invalidLogin = false;
    
    if (requestContext.isLoggedIn()) {
      StudentParentDAO studentParentDAO = DAOFactory.getInstance().getStudentParentDAO();
      
      invalidLogin = studentParentDAO.findById(requestContext.getLoggedUserId()) == null;
    }
    
    requestContext.getRequest().setAttribute("hash", hash);
    requestContext.getRequest().setAttribute("invalidLogin", invalidLogin);
    
    requestContext.setIncludeJSP("/templates/users/studentparentregistration.jsp");
  }

  @Override
  protected boolean checkAccess(RequestContext requestContext) {
    if (requestContext.isLoggedIn()) {
      // Logged in user for this view must be a StudentParent
      StudentParentDAO studentParentDAO = DAOFactory.getInstance().getStudentParentDAO();
      StudentParent studentParent = studentParentDAO.findById(requestContext.getLoggedUserId());
      return studentParent != null;
    }
    
    return true;
  }

}
