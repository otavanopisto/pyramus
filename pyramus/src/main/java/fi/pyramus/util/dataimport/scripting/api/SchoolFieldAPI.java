package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;

public class SchoolFieldAPI {
  
  public SchoolFieldAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(String name) {
    return (DAOFactory.getInstance().getSchoolFieldDAO().create(name).getId());
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
