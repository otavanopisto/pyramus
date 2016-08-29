package fi.otavanopisto.pyramus.rest;

import java.util.Date;


import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public abstract class AbstractRESTService {
  
  protected Date toDate(OffsetDateTime dateTime) {
    if (dateTime != null) {
      return Date.from(dateTime.toInstant());
    }
    
    return null;
  }

}
