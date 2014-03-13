package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.domainmodel.courses.CourseState;

public class CourseStatesSetupWizardViewController extends SetupWizardController {
  
  public CourseStatesSetupWizardViewController() {
    super("coursestates");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    CourseState initialCourseState = null;

    int rowCount = requestContext.getInteger("courseStatesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "courseStatesTable." + i;
      
      Boolean initialState = "1".equals(requestContext.getString(colPrefix + ".initialState"));
      String name = requestContext.getRequest().getParameter(colPrefix + ".name");
      CourseState courseState = courseStateDAO.create(name);
      
      if (initialState) {
        if (initialCourseState != null) {
          throw new SetupWizardException("Two or more initialCourseStates defined");
        }
        
        initialCourseState = courseState;
      }
    }
    
    if (initialCourseState != null) {
      defaultsDAO.updateDefaultInitialCourseState(initialCourseState);
    } else {
      throw new SetupWizardException("initialCourseState not defined");
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    return !courseStateDAO.listUnarchived().isEmpty();
  }

}
