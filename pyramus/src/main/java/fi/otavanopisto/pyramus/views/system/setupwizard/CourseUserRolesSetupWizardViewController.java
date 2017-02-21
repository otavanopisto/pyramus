package fi.otavanopisto.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberRoleDAO;

public class CourseUserRolesSetupWizardViewController extends SetupWizardController {
  
  public CourseUserRolesSetupWizardViewController(String phase) {
    super(phase);
  }

  public CourseUserRolesSetupWizardViewController() {
    super("courseuserroles");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    CourseStaffMemberRoleDAO courseStaffMemberRoleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();
    
    int rowCount = requestContext.getInteger("courseUserRolesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "courseUserRolesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      courseStaffMemberRoleDAO.create(name);
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    CourseStaffMemberRoleDAO courseStaffMemberRoleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();
    return courseStaffMemberRoleDAO.count() > 0;
  }

}
