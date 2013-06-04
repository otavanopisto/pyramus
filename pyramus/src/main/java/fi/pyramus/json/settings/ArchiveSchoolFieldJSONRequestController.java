package fi.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.SchoolFieldDAO;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveSchoolFieldJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();
    Long schoolFieldId = NumberUtils.createLong(requestContext.getRequest().getParameter("schoolFieldId"));
    SchoolField schoolField = schoolFieldDAO.findById(schoolFieldId);
    schoolFieldDAO.archive(schoolField);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
