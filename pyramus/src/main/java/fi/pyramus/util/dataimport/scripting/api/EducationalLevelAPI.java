package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;

public class EducationalLevelAPI {
  
  public EducationalLevelAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(String name) {
    return (DAOFactory.getInstance().getStudentEducationalLevelDAO().create(name).getId());
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
