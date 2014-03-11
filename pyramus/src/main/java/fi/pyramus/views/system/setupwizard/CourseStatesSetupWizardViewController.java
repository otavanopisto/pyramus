package fi.pyramus.views.system.setupwizard;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;

public class CourseStatesSetupWizardViewController extends PyramusFormViewController {
  
  @Override
  public void processForm(PageRequestContext requestContext) {
    requestContext.getRequest().setAttribute("setupPhase", "coursestates");    
    requestContext.setIncludeJSP("/templates/system/setupwizard/coursestates.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
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
          throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Two or more initialCourseStates defined");
        }
        
        initialCourseState = courseState;
      }
    }
    
    if (initialCourseState != null) {
      defaultsDAO.updateDefaultInitialCourseState(initialCourseState);
    } else {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "initialCourseState not defined");
    }
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
