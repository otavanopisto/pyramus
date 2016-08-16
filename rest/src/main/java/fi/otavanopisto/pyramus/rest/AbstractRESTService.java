package fi.otavanopisto.pyramus.rest;

import java.util.Date;


import java.time.OffsetDateTime;

public abstract class AbstractRESTService {
  
  protected Date toDate(OffsetDateTime dateTime) {
    if (dateTime != null) {
      return Date.from(dateTime.toInstant());
    }
    
    return null;
  }

}
