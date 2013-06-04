package fi.pyramus.json.modules;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveModuleJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();

    Long moduleId = NumberUtils.createLong(requestContext.getRequest().getParameter("moduleId"));
    Module module = moduleDAO.findById(moduleId);
    moduleDAO.archive(module);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
