package fi.otavanopisto.pyramus.views.system.setupwizard;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.SchoolFieldDAO;

public class SchoolFieldsSetupWizardViewController extends SetupWizardController {
  
  public SchoolFieldsSetupWizardViewController() {
    super("schoolfields");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();

    int rowCount = NumberUtils.createInteger(requestContext.getRequest().getParameter("schoolFieldsTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "schoolFieldsTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      schoolFieldDAO.create(name); 
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();
    return !schoolFieldDAO.listUnarchived().isEmpty();
  }

}
