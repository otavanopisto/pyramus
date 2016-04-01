package fi.otavanopisto.pyramus.views.system.setupwizard;

import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

public class StudentActivityTypeSetupWizardViewController extends SetupWizardController {
  
  public StudentActivityTypeSetupWizardViewController() {
    super("studentactivitytypes");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException{
    StudentActivityTypeDAO studentActivityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    List<StudentActivityType> studentActivityTypes = studentActivityTypeDAO.listUnarchived();
    setJsDataVariable(requestContext, "studentActivityTypes", new JSONArrayExtractor("name", "id").extractString(studentActivityTypes));
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    StudentActivityTypeDAO studentActivityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    
    int rowCount = requestContext.getInteger("studentActivityTypesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studentActivityTypesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      Long id = requestContext.getLong(colPrefix + ".id");
      if (id == -1l) {
        studentActivityTypeDAO.create(name);
      }
    }
    
    if (studentActivityTypeDAO.listUnarchived().isEmpty()) {
      throw new SetupWizardException("No Student Activity Types defined");
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    StudentActivityTypeDAO studentActivityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    return !studentActivityTypeDAO.listUnarchived().isEmpty();
  }
  
}
