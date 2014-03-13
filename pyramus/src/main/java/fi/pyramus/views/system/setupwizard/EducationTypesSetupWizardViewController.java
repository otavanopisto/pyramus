package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;

public class EducationTypesSetupWizardViewController extends SetupWizardController {
  
  public EducationTypesSetupWizardViewController() {
    super("educationtypes");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    int rowCount = requestContext.getInteger("educationTypesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "educationTypesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      String code = requestContext.getString(colPrefix + ".code");
      educationTypeDAO.create(name, code);
    }

  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    return !educationTypeDAO.listUnarchived().isEmpty();
  }
}
