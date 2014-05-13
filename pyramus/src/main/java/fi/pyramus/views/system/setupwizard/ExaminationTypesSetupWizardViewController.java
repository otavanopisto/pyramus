package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;

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
