package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.NationalityDAO;

public class NationalitiesSetupWizardViewController extends SetupWizardController {
  
  public NationalitiesSetupWizardViewController() {
    super("nationalities");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    
    int rowCount = requestContext.getInteger("nationalitiesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "nationalitiesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      String code = requestContext.getString(colPrefix + ".code");
      nationalityDAO.create(name, code);
    }  
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    // TODO Auto-generated method stub
    return false;
  }

}
