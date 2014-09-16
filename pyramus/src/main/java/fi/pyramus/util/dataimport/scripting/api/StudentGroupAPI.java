package fi.pyramus.util.dataimport.scripting.api;

import java.util.Date;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class StudentGroupAPI {
  
  public StudentGroupAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String name, String description, Date beginDate) throws InvalidScriptException {
    User loggedUser = DAOFactory.getInstance().getUserDAO().findById(loggedUserId);
    if (loggedUser == null) {
      throw new InvalidScriptException("Logged user could not be found");  
    }
    
    return (DAOFactory.getInstance().getStudentGroupDAO().create(name, description, beginDate, loggedUser).getId());
  }

  private Long loggedUserId;
}
