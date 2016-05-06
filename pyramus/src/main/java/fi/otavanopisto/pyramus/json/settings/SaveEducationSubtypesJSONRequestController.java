package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveEducationSubtypesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("educationSubtypesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "educationSubtypesTable." + i;
      Long educationTypeId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".educationTypeId"));
      Long educationSubtypeId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".educationSubtypeId"));
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      String code = jsonRequestContext.getRequest().getParameter(colPrefix + ".code");
      boolean modified = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter(colPrefix + ".modified")) == 1;
      if (educationSubtypeId == -1) {
        educationSubtypeDAO.create(educationTypeDAO.findById(educationTypeId), name, code); 
      }
      else if (modified) {
        EducationSubtype educationSubtype = educationSubtypeDAO.findById(educationSubtypeId);
        educationSubtypeDAO.update(educationSubtype, name, code);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
