package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

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
