package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.pyramus.domainmodel.courses.CourseParticipationType;

public class CourseParticipationTypesSetupWizardViewController extends SetupWizardController {
  
  public CourseParticipationTypesSetupWizardViewController() {
    super("courseparticipationtypes");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException{

  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    CourseParticipationType initialCourseParticipationType = null;

    int rowCount = requestContext.getInteger("courseParticipationTypesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "courseParticipationTypesTable." + i;

      Boolean initialType = "1".equals(requestContext.getString(colPrefix + ".initialType"));
      String name = requestContext.getString(colPrefix + ".name");

      CourseParticipationType courseParticipationType = participationTypeDAO.create(name);

      if (initialType) {
        if (initialCourseParticipationType != null) {
          throw new SetupWizardException("Two or more initial course participation types defined");
        }

        initialCourseParticipationType = courseParticipationType;
      } 
    }

    if (initialCourseParticipationType != null) {
      defaultsDAO.updateInitialCourseParticipationType(initialCourseParticipationType);
    } else {
      throw new SetupWizardException("Initial course participation is not defined");
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    return !participationTypeDAO.listUnarchived().isEmpty();
  }
  
}
