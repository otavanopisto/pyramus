package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveEducationSubtypeJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    

    Long educationSubtypeId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("educationSubtypeId"));
    educationSubtypeDAO.archive(educationSubtypeDAO.findById(educationSubtypeId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
