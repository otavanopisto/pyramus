package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveCurriculumsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();

    Long rowCountLong = jsonRequestContext.getLong("curriculumsTable.rowCount");
    if (rowCountLong == null)
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "Row count was not defined.");
    
    int rowCount = rowCountLong.intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "curriculumsTable." + i;
      Long curriculumId = jsonRequestContext.getLong(colPrefix + ".curriculumId");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      
      boolean modified = new Integer(1).equals(jsonRequestContext.getInteger(colPrefix + ".modified"));
      if (curriculumId == -1) {
        curriculumDAO.create(name);
      }
      else if (modified) {
        Curriculum curriculum = curriculumDAO.findById(curriculumId);
        
        if (curriculum != null)
          curriculumDAO.updateName(curriculum, name);
        else
          throw new SmvcRuntimeException(StatusCode.UNDEFINED, "Modified curriculum was not found.");
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
