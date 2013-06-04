package fi.pyramus.json.resources;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.resources.ResourceDAO;
import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveResourceJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    ResourceDAO resourceDAO = DAOFactory.getInstance().getResourceDAO();

    Long resourceId = NumberUtils.createLong(requestContext.getRequest().getParameter("resource"));
    Resource resource = resourceDAO.findById(resourceId);
    resourceDAO.archiveResource(resource);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
