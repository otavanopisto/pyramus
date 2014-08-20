package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentExaminationTypeDAO;

public class ExaminationTypesSetupWizardViewController extends SetupWizardController {

  public ExaminationTypesSetupWizardViewController() {
    super("examinationtypes");
  }

  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    StudentExaminationTypeDAO studentExaminationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();

    int rowCount = requestContext.getInteger("examinationTypesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "examinationTypesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      
      studentExaminationTypeDAO.create(name);
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    StudentExaminationTypeDAO studentExaminationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();
    return !studentExaminationTypeDAO.listAll().isEmpty();
  }

}
