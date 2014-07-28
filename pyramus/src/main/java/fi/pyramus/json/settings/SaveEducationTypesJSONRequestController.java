package fi.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveEducationTypesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("educationTypesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "educationTypesTable." + i;
      Long educationTypeId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".educationTypeId"));
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      String code = jsonRequestContext.getRequest().getParameter(colPrefix + ".code");
      boolean modified = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter(colPrefix + ".modified")) == 1;
      if (educationTypeId == -1) {
        educationTypeDAO.create(name, code); 
      }
      else if (modified) {
        EducationType educationType = educationTypeDAO.findById(educationTypeId);
        educationTypeDAO.update(educationType, name, code);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
