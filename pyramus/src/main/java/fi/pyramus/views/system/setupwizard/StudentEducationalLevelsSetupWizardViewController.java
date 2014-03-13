package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.dao.students.StudentEducationalLevelDAO;

public class StudentEducationalLevelsSetupWizardViewController extends SetupWizardController {
  
  public StudentEducationalLevelsSetupWizardViewController() {
    super("studenteducationallevels");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    StudentEducationalLevelDAO dao = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    
    int rowCount = requestContext.getInteger("studentEducationalLevelsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studentEducationalLevelsTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      dao.create(name);
    }  
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    // TODO Auto-generated method stub
    return false;
  }

}
