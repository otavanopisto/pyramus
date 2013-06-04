package fi.pyramus.json.settings;

import java.util.Date;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveAcademicTermsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();

    int rowCount = jsonRequestContext.getInteger("termsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "termsTable." + i;
      Long termId = jsonRequestContext.getLong(colPrefix + ".termId");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      Date startDate =  jsonRequestContext.getDate(colPrefix + ".startDate");
      Date endDate = jsonRequestContext.getDate(colPrefix + ".endDate");
      boolean modified = jsonRequestContext.getInteger(colPrefix + ".modified") == 1; 
      if (termId == -1) {
        academicTermDAO.create(name, startDate, endDate); 
      }
      else if (modified) {
        AcademicTerm term = academicTermDAO.findById(termId);
        academicTermDAO.update(term, name, startDate, endDate);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
