package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;

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
      String symbol = jsonRequestContext.getRequest().getParameter(colPrefix + ".symbol");
      
      if (baseUnit) {
        baseUnits = new Double(1);
      }
        
      if (timeUnitId == -1) {
        timeUnit = educationalTimeUnitDAO.create(baseUnits, name, symbol); 
      } else {
        timeUnit = educationalTimeUnitDAO.findById(timeUnitId);
        educationalTimeUnitDAO.update(timeUnit, baseUnits, name, symbol);
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
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
