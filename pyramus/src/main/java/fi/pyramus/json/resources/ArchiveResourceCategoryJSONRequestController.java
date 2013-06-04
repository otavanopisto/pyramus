package fi.pyramus.json.resources;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.resources.ResourceCategoryDAO;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving a resource category. 
 */
public class ArchiveResourceCategoryJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to archive a resource category.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();

    Long resourceCategoryId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("resourceCategoryId"));
    resourceCategoryDAO.archiveResourceCategory(resourceCategoryDAO.findById(resourceCategoryId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
