package fi.pyramus.util.dataimport.scripting.api;

import java.util.ArrayList;
import java.util.List;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class StudyProgrammeCategoryAPI {

  public StudyProgrammeCategoryAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(String name, Long educationTypeId) throws InvalidScriptException {
    EducationType educationType = null;
    
    if (educationTypeId != null) {
      educationType = DAOFactory.getInstance().getEducationTypeDAO().findById(educationTypeId);
      if (educationType == null) {
        throw new InvalidScriptException("EducationType #'" + educationTypeId + "' not found.");
      }
    }
    
    return (DAOFactory.getInstance().getStudyProgrammeCategoryDAO().create(name, educationType).getId());
  }
  
  public Long[] listIdsByName(String name) {
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();
    
    List<Long> result = new ArrayList<>();
    
    List<StudyProgrammeCategory> studyProgrammeCategories = studyProgrammeCategoryDAO.listByName(name);
    for (StudyProgrammeCategory studyProgrammeCategory : studyProgrammeCategories) {
      result.add(studyProgrammeCategory.getId());
    }
    
    return result.toArray(new Long[0]);
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
