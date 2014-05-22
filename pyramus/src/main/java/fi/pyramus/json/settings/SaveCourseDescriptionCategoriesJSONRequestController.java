package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveCourseDescriptionCategoriesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();

    int rowCount = jsonRequestContext.getInteger("courseDescriptionCategoriesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      CourseDescriptionCategory category;
      
      String colPrefix = "courseDescriptionCategoriesTable." + i;
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      Long categoryId = jsonRequestContext.getLong(colPrefix + ".categoryId");
      
      if (categoryId == null) {
        category = descriptionCategoryDAO.create(name); 
      } else {
        category = descriptionCategoryDAO.findById(categoryId);
        descriptionCategoryDAO.update(category, name);
      }
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
