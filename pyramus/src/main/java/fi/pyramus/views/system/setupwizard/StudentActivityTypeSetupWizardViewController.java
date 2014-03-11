package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentActivityTypeDAO;

public class StudentActivityTypeSetupWizardViewController extends SetupWizardController {
  
  public StudentActivityTypeSetupWizardViewController() {
    super("studentactivitytypes");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException{

  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    StudentActivityTypeDAO studentActivityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    
    int rowCount = requestContext.getInteger("studentActivityTypesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studentActivityTypesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      studentActivityTypeDAO.create(name);
    }    
  }
  
}
