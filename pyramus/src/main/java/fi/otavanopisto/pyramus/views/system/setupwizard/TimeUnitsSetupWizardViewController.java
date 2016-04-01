package fi.otavanopisto.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;

public class TimeUnitsSetupWizardViewController extends SetupWizardController {

  public TimeUnitsSetupWizardViewController() {
    super("timeunits");
  }

  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    EducationalTimeUnit baseTimeUnit = null;

    int rowCount = requestContext.getInteger("timeUnitsTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "timeUnitsTable." + i;
      Boolean baseUnit = "1".equals(requestContext.getString(colPrefix + ".baseUnit"));
      Double baseUnits = requestContext.getDouble(colPrefix + ".baseUnits");
      String name = requestContext.getRequest().getParameter(colPrefix + ".name");
      String symbol = requestContext.getRequest().getParameter(colPrefix + ".symbol");
      
      if (baseUnit) {
        baseUnits = new Double(1);
      }

      EducationalTimeUnit timeUnit = educationalTimeUnitDAO.create(baseUnits, name, symbol); 
      
      if (baseUnit) {
        if (baseTimeUnit != null) {
          throw new SetupWizardException("Two or more base time units defined");
        } else {
          baseTimeUnit = timeUnit; 
        }
      }
    }
    
    if (baseTimeUnit != null) {
      defaultsDAO.updateDefaultBaseTimeUnit(baseTimeUnit);
    } else {
      throw new SetupWizardException("Base time unit is not defined");    
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    return !educationalTimeUnitDAO.listUnarchived().isEmpty();
  }

}
