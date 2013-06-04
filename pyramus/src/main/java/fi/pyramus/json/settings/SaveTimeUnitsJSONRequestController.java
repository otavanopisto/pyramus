package fi.pyramus.json.settings;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;

public class SaveTimeUnitsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    EducationalTimeUnit baseTimeUnit = null;

    int rowCount = jsonRequestContext.getInteger("timeUnitsTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      EducationalTimeUnit timeUnit;
      
      String colPrefix = "timeUnitsTable." + i;
      Long timeUnitId = jsonRequestContext.getLong(colPrefix + ".timeUnitId");
      
      Boolean baseUnit = "1".equals(jsonRequestContext.getString(colPrefix + ".baseUnit"));
      Double baseUnits = jsonRequestContext.getDouble(colPrefix + ".baseUnits");
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      
      if (baseUnit) {
        baseUnits = new Double(1);
      }
        
      if (timeUnitId == -1) {
        timeUnit = educationalTimeUnitDAO.create(baseUnits, name); 
      } else {
        timeUnit = educationalTimeUnitDAO.findById(timeUnitId);
        educationalTimeUnitDAO.update(timeUnit, baseUnits, name);
      }
      
      if (baseUnit) {
        if (baseTimeUnit != null)
          throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Two or more baseTimeUnits defined");
          
        baseTimeUnit = timeUnit;
      }
    }
    
    if (baseTimeUnit != null) {
      if (!baseTimeUnit.equals(defaultsDAO.getDefaults().getBaseTimeUnit())) {
        defaultsDAO.updateDefaultBaseTimeUnit(baseTimeUnit);
      }
        
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
