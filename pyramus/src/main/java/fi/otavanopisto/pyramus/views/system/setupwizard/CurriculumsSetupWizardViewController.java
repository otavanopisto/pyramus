package fi.otavanopisto.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;

public class CurriculumsSetupWizardViewController extends SetupWizardController {
  
  public CurriculumsSetupWizardViewController(String phase) {
    super(phase);
  }

  public CurriculumsSetupWizardViewController() {
    super("curriculums");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    
    int rowCount = requestContext.getInteger("curriculumsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "curriculumsTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      curriculumDAO.create(name);
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    return !curriculumDAO.listUnarchived().isEmpty();
  }

}