package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;

public class SchoolAPI {
  
  public SchoolAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(String code, String name, Long schoolField) {
    SchoolField schoolFieldEntity = null;
    
    if (schoolField != null) {
      schoolFieldEntity = DAOFactory.getInstance().getSchoolFieldDAO().findById(schoolField);
    }
    
    return (DAOFactory.getInstance().getSchoolDAO().create(code, name, schoolFieldEntity).getId());
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
