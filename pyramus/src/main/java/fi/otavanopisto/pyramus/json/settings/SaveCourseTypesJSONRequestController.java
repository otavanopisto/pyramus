package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

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
