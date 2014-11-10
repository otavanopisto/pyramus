package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;

public class ActivityTypeAPI {
  
  public ActivityTypeAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long createType(String name) {
    return (DAOFactory.getInstance().getStudentActivityTypeDAO().create(name).getId());
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
