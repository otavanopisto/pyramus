package fi.pyramus.views.system.setupwizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.util.JSONArrayExtractor;

public class TimeUnitsSetupWizardViewController extends SetupWizardController {

  public TimeUnitsSetupWizardViewController() {
    super("timeunits");
  }

  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    
    final EducationalTimeUnit baseTimeUnit = defaultsDAO.getDefaults().getBaseTimeUnit();
    List<EducationalTimeUnit> timeUnits = new ArrayList<EducationalTimeUnit>(educationalTimeUnitDAO.listUnarchived());
    

    Collections.sort(timeUnits, new Comparator<EducationalTimeUnit>() {
      @Override
      public int compare(EducationalTimeUnit o1, EducationalTimeUnit o2) {
        Double units1 = o1.getBaseUnits();
        Double units2 = o2.getBaseUnits();

        if (units1 == units2)
          return 0;
        
        if (o1.equals(baseTimeUnit))
          return -1;
        if (o2.equals(baseTimeUnit))
          return 1;
        
        return units1 > units2 ? 1 : -1;
      }
    });
    
    String jsonTimeUnits = new JSONArrayExtractor("id", "baseUnits", "name").extractString(timeUnits);
    JSONObject joBaseTimeUnit = new JSONObject();
    if (baseTimeUnit != null) {
      joBaseTimeUnit.put("id", baseTimeUnit.getId());
    } else {
      joBaseTimeUnit.put("id", -1);
    }
    
    this.setJsDataVariable(requestContext, "timeUnits", jsonTimeUnits);
    this.setJsDataVariable(requestContext, "baseTimeUnit", joBaseTimeUnit.toString());
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    EducationalTimeUnit baseTimeUnit = null;

    int rowCount = requestContext.getInteger("timeUnitsTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      EducationalTimeUnit timeUnit;
      
      String colPrefix = "timeUnitsTable." + i;
      Long timeUnitId = requestContext.getLong(colPrefix + ".timeUnitId");
      
      Boolean baseUnit = "1".equals(requestContext.getString(colPrefix + ".baseUnit"));
      Double baseUnits = requestContext.getDouble(colPrefix + ".baseUnits");
      String name = requestContext.getRequest().getParameter(colPrefix + ".name");
      
      if (baseUnit) {
        baseUnits = new Double(1);
      }
        
      if(timeUnitId == null){
        throw new SetupWizardException("Initial course participation is not defined");
      }

      timeUnit = educationalTimeUnitDAO.create(baseUnits, name); 
 
      if (baseUnit) {
        if (baseTimeUnit != null)
          throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Two or more baseTimeUnits defined");
          
        baseTimeUnit = timeUnit;
      }
    }

  }

}
