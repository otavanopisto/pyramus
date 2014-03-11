package fi.pyramus.views.system.setupwizard;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.dao.base.SchoolFieldDAO;
import fi.pyramus.domainmodel.base.SchoolField;

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

}
