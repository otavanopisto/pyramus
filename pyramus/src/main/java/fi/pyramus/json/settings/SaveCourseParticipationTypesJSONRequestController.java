package fi.pyramus.json.settings;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;

public class SaveCourseParticipationTypesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    CourseParticipationType initialCourseParticipationType = null;
    
    int rowCount = jsonRequestContext.getInteger("courseParticipationTypesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      CourseParticipationType courseParticipationType = null;
      
      String colPrefix = "courseParticipationTypesTable." + i;
      
      Boolean initialType = "1".equals(jsonRequestContext.getString(colPrefix + ".initialType"));
      Long id = jsonRequestContext.getLong(colPrefix + ".courseParticipationTypeId");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      
      if (id == -1) {
        courseParticipationType = participationTypeDAO.create(name); 
      }
      else {
        courseParticipationType = participationTypeDAO.findById(id);
        participationTypeDAO.update(courseParticipationType, name);
      }
      

      if (initialType) {
        if (initialCourseParticipationType != null)
          throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Two or more initial course participation types defined");
        
        initialCourseParticipationType = courseParticipationType;
      }
    }
    
    if (initialCourseParticipationType != null) {
      if (!initialCourseParticipationType.equals(defaultsDAO.getDefaults().getInitialCourseParticipationType())) {
        defaultsDAO.updateInitialCourseParticipationType(initialCourseParticipationType);
      }
        
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
