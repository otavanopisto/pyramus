package fi.pyramus.json.settings;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;

public class SaveCourseStatesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    CourseState initialCourseState = null;

    int rowCount = jsonRequestContext.getInteger("courseStatesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      CourseState courseState;
      
      String colPrefix = "courseStatesTable." + i;
      Long courseStateId = jsonRequestContext.getLong(colPrefix + ".courseStateId");
      
      Boolean initialState = "1".equals(jsonRequestContext.getString(colPrefix + ".initialState"));
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      
        
      if (courseStateId == -1) {
        courseState = courseStateDAO.create(name);
      } else {
        courseState = courseStateDAO.findById(courseStateId);
        courseStateDAO.update(courseState, name);
      }
      
      if (initialState) {
        if (initialCourseState != null)
          throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Two or more initialCourseStates defined");
          
        initialCourseState = courseState;
      }
    }
    
    if (initialCourseState != null) {
      if (!initialCourseState.equals(defaultsDAO.getDefaults().getInitialCourseState())) {
        defaultsDAO.updateDefaultInitialCourseState(initialCourseState);
      }
        
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
