package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

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
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
