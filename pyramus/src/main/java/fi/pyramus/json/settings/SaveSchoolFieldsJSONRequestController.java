package fi.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.SchoolFieldDAO;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveSchoolFieldsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("schoolFieldsTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "schoolFieldsTable." + i;
      Long schoolFieldId = jsonRequestContext.getLong(colPrefix + ".id");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      boolean modified = new Long(1).equals(jsonRequestContext.getLong(colPrefix + ".modified"));
      if (schoolFieldId == -1) {
        schoolFieldDAO.create(name); 
      }
      else if (modified) {
        SchoolField schoolField = schoolFieldDAO.findById(schoolFieldId);
        schoolFieldDAO.update(schoolField, name);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
