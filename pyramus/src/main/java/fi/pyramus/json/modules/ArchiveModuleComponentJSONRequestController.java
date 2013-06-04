package fi.pyramus.json.modules;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.modules.ModuleComponentDAO;
import fi.pyramus.domainmodel.modules.ModuleComponent;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveModuleComponentJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    ModuleComponentDAO moduleComponentDAO = DAOFactory.getInstance().getModuleComponentDAO();

    Long componentId = requestContext.getLong("componentId");
    ModuleComponent component = moduleComponentDAO.findById(componentId);
    moduleComponentDAO.archive(component);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
