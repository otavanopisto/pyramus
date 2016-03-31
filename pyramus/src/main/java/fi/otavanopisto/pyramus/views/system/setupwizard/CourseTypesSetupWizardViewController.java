package fi.otavanopisto.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseTypeDAO;

public class CourseTypesSetupWizardViewController extends SetupWizardController {
  
  public CourseTypesSetupWizardViewController() {
    super("coursetypes");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    
    int rowCount = requestContext.getInteger("courseTypesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "courseTypesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      courseTypeDAO.create(name, Boolean.FALSE);
    }  
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    return !courseTypeDAO.listUnarchived().isEmpty();
  }

}
