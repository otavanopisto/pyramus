package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.students.StudentActivityTypeDAO;

public class SchoolsSetupWizardViewController extends SetupWizardController {
  
  public SchoolsSetupWizardViewController() {
    super("schools");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException{

  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    
    int rowCount = requestContext.getInteger("schoolsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "schoolsTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
    }    
  }
  
}
