package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
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
    StudentEducationalLevelDAO studentEducationalLevelDAO = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    
    int rowCount = requestContext.getInteger("studentEducationalLevelsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studentEducationalLevelsTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      studentEducationalLevelDAO.create(name);
    }  
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    StudentEducationalLevelDAO studentEducationalLevelDAO = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    return !studentEducationalLevelDAO.listUnarchived().isEmpty();
  }

}
