package fi.pyramus.rest;

import java.util.Date;

import javax.inject.Inject;

import org.joda.time.DateTime;

import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.users.User;

public abstract class AbstractRESTService {
  
  @Inject
  private StaffMemberDAO userDAO;

  // TODO: Implement this
  protected Long getLoggedUserId() {
    return 1l;
  }
  
  protected User getLoggedUser() {
    return userDAO.findById(getLoggedUserId());
  }
  
  protected Date toDate(DateTime dateTime) {
    if (dateTime != null) {
      return dateTime.toDate();
    }
    
    return null;
  }

}
