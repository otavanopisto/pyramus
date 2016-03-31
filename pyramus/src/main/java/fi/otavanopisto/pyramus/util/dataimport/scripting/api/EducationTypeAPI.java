package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.util.dataimport.scripting.InvalidScriptException;

public class EducationTypeAPI {
  
  public EducationTypeAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String code, String name) throws InvalidScriptException {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();
    EducationType educationType = educationTypeDAO.create(name, code);
    return educationType.getId();
  }
  
  public Long findIdByCode(String code) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();
    EducationType educationType = educationTypeDAO.findByCode(code);
    return educationType != null ? educationType.getId() : null;
  }
  
  @SuppressWarnings("unused")
  private Long loggedUserId;
}
