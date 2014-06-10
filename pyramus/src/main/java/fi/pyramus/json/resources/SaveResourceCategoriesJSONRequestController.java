package fi.pyramus.json.resources;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.resources.ResourceCategoryDAO;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveResourceCategoriesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("resourceCategoriesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "resourceCategoriesTable." + i;
      Long resourceCategoryId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".resourceCategoryId"));
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      boolean modified = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter(colPrefix + ".modified")) == 1;
      if (resourceCategoryId == -1) {
        resourceCategoryDAO.createResourceCategory(name); 
      }
      else if (modified) {
        ResourceCategory resourceCategory = resourceCategoryDAO.findById(resourceCategoryId);
        resourceCategoryDAO.updateResourceCategory(resourceCategory, name);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
