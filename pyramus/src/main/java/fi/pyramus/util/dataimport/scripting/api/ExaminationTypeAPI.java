package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;

public class ExaminationTypeAPI {

  public ExaminationTypeAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String name) {
    return (DAOFactory.getInstance().getStudentExaminationTypeDAO().create(name).getId());
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
