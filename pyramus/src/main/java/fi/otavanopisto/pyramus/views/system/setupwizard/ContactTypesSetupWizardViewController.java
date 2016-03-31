package fi.otavanopisto.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;

public class ContactTypesSetupWizardViewController extends SetupWizardController {
  
  public ContactTypesSetupWizardViewController() {
    super("contacttypes");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    
    int rowCount = requestContext.getInteger("contactTypesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "contactTypesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      contactTypeDAO.create(name, Boolean.FALSE);
    }  
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    return !contactTypeDAO.listUnarchived().isEmpty();
  }

}
