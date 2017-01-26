package fi.otavanopisto.pyramus.rest.util;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;

import javax.ws.rs.WebApplicationException;

public class ISO8601Timestamp {
   
  private Instant instant;

  public ISO8601Timestamp(String timestamp) throws WebApplicationException {
    try {
      instant = Instant.parse(timestamp);
    } catch (DateTimeParseException ex) {
      throw new WebApplicationException(ex);
    }
  }

  public Instant getInstant() {
    return instant;
  }
  
  public Date getDate() {
    return Date.from(instant);
  }

  @Override
  public String toString() {
    if (instant != null) {
      return instant.toString();
    } else {
      return "";
    }
  }
}
