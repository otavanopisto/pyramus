package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

public class EducationTypesSetupWizardViewController extends PyramusFormViewController {
  
  @Override
  public void processForm(PageRequestContext requestContext) {
    HttpServletRequest req = requestContext.getRequest();
    String setupPhase = "educationtypes";
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    String jsonEducationTypes = new JSONArrayExtractor("name", "code", "id").extractString(educationTypes);
    req.setAttribute("setupPhase", "educationtypes");
    requestContext.setIncludeJSP("/templates/system/setupwizard/educationtypes.jsp");
  }

  @Override
  public void processSend(PageRequestContext arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
