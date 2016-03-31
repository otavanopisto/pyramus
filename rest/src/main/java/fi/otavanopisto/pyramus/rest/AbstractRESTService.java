package fi.otavanopisto.pyramus.rest;

import java.util.Date;

import org.joda.time.DateTime;

public abstract class AbstractRESTService {
  
  protected Date toDate(DateTime dateTime) {
    if (dateTime != null) {
      return dateTime.toDate();
    }
    
    return null;
  }

}
