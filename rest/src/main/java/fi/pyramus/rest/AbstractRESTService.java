package fi.pyramus.rest;

import java.util.Date;

import javax.inject.Inject;

import org.joda.time.DateTime;

import fi.pyramus.domainmodel.users.User;
import fi.pyramus.rest.controller.RestSessionController;

public abstract class AbstractRESTService {
  
  @Inject
  private RestSessionController restSessionController;
  
  protected User getLoggedUser() {
    return restSessionController.getLoggedUser();
  }
  
  protected Date toDate(DateTime dateTime) {
    if (dateTime != null) {
      return dateTime.toDate();
    }
    
    return null;
  }

}
