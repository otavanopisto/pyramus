package fi.otavanopisto.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;

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
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    return !nationalityDAO.listUnarchived().isEmpty();
  }

}
