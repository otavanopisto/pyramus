package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.file.FileTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.file.FileType;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveFileTypesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    FileTypeDAO fileTypeDAO = DAOFactory.getInstance().getFileTypeDAO();

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("fileTypesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "fileTypesTable." + i;
      Long fileTypeId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".id"));
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      boolean modified = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter(colPrefix + ".modified")) == 1;
      if (fileTypeId == -1) {
        fileTypeDAO.create(name); 
      }
      else if (modified) {
        FileType fileType = fileTypeDAO.findById(fileTypeId);
        fileTypeDAO.update(fileType, name);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
