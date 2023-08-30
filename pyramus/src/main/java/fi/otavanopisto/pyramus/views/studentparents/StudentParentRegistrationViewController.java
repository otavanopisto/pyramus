package fi.otavanopisto.pyramus.views.studentparents;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StudentParentRegistrationDAO;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class StudentParentRegistrationViewController extends PyramusViewController {

  @Override
  public void process(PageRequestContext requestContext) {
    String hash = StringUtils.trim(requestContext.getString("c"));
    
    StudentParentRegistrationDAO studentParentRegistrationDAO = DAOFactory.getInstance().getStudentParentRegistrationDAO();
    
    boolean valid = StringUtils.isNotBlank(hash) && studentParentRegistrationDAO.findByHash(hash) != null;
    
    requestContext.getRequest().setAttribute("valid", valid);
    requestContext.getRequest().setAttribute("hash", hash);
    
    requestContext.setIncludeJSP("/templates/users/studentparentregistration.jsp");
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
