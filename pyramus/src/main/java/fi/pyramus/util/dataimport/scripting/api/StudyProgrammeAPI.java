package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class StudyProgrammeAPI {

  public StudyProgrammeAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(String name, Long categoryId, String code) throws InvalidScriptException {
    StudyProgrammeCategory category = null;
    if (categoryId != null) {
      category = DAOFactory.getInstance().getStudyProgrammeCategoryDAO().findById(categoryId);
      if (category == null) {
        throw new InvalidScriptException("StudyProgrammeCategory #" + categoryId + " not found.");
      }
    }
    
    return (DAOFactory.getInstance().getStudyProgrammeDAO().create(name, category, code).getId());
  }
  
  public Long findIdByCode(String code) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    
    StudyProgramme studyProgramme = studyProgrammeDAO.findByCode(code);
    return studyProgramme != null ? studyProgramme.getId() : null;
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
