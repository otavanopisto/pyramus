package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;

public class CourseParticipationTypesSetupWizardViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    requestContext.getRequest().setAttribute("setupPhase", "courseparticipationtypes");
    requestContext.setIncludeJSP("/templates/system/setupwizard/courseparticipationtypes.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
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
          throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Two or more initial course participation types defined");
        }

        initialCourseParticipationType = courseParticipationType;
      } 
    }

    if (initialCourseParticipationType != null) {
      defaultsDAO.updateInitialCourseParticipationType(initialCourseParticipationType);
    } else {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Initial course participation is not defined");
    }
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
