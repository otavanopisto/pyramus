package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;

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
