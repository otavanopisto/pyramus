package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.LanguageDAO;

public class LanguagesSetupWizardViewController extends SetupWizardController {
  
  public LanguagesSetupWizardViewController() {
    super("languages");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    
    int rowCount = requestContext.getInteger("languagesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "languagesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      String code = requestContext.getString(colPrefix + ".code");
      languageDAO.create(code, name);
    }  
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    // TODO Auto-generated method stub
    return false;
  }

}
