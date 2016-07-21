package fi.otavanopisto.pyramus.rest;

import java.util.Date;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZonedDateTime;

public abstract class AbstractRESTService {
  
  protected Date toDate(ZonedDateTime dateTime) {
    if (dateTime != null) {
      return DateTimeUtils.toDate(dateTime.toInstant());
    }
    
    return null;
  }

}
