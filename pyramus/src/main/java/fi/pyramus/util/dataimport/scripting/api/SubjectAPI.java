package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class SubjectAPI {
  
  public SubjectAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String code, String name, String educationTypeCode) throws InvalidScriptException {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();
    
    EducationType educationType = educationTypeDAO.findByCode(educationTypeCode);
    if (educationType != null) {
      return subjectDAO.create(code, name, educationType).getId();
    } else{
      throw new InvalidScriptException("Education type doesn't exist for "+educationTypeCode);
    }
  }
  
  public Long findIdByCode(String code) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    Subject subject = subjectDAO.findByCode(code);
    return subject != null ? subject.getId() : null;
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
