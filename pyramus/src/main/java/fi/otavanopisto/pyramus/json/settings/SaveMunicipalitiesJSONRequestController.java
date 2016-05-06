package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveMunicipalitiesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("municipalitiesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "municipalitiesTable." + i;
      Long municipalityId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".id"));
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      String code = jsonRequestContext.getRequest().getParameter(colPrefix + ".code");
      boolean modified = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter(colPrefix + ".modified")) == 1;
      if (municipalityId == -1) {
        municipalityDAO.create(name, code); 
      }
      else if (modified) {
        Municipality municipality = municipalityDAO.findById(municipalityId);
        municipalityDAO.update(municipality, name, code);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
