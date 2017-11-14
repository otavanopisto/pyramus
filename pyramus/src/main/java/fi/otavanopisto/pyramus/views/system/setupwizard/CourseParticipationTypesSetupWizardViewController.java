package fi.otavanopisto.pyramus.views.system.setupwizard;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;

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

      Boolean initialType = "true".equals(requestContext.getString(colPrefix + ".initialType"));
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
