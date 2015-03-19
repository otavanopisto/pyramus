package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseTypeDAO;
import fi.pyramus.domainmodel.courses.CourseType;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveCourseTypesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    
    int rowCount = jsonRequestContext.getInteger("courseTypesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      CourseType courseType;
      
      String colPrefix = "courseTypesTable." + i;
      Long courseTypeId = jsonRequestContext.getLong(colPrefix + ".courseTypeId");
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");

      if (courseTypeId == -1) {
        courseType = courseTypeDAO.create(name);
      } else {
        courseType = courseTypeDAO.findById(courseTypeId);
        courseTypeDAO.updateName(courseType, name);
      }
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
