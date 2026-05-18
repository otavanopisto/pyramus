package fi.otavanopisto.pyramus.rest.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.ws.rs.WebApplicationException;

/**
 * Wrapper for LocalDate to be usable in JAX-RS endpoint parameters.
 * Uses yyyy-mm-dd format.
 */
public class ISO8601Date {
   
  private LocalDate localDate;

  public ISO8601Date(String dateStr) throws WebApplicationException {
    try {
      localDate = LocalDate.parse(dateStr);
    } catch (DateTimeParseException ex) {
      throw new WebApplicationException(ex);
    }
  }

  public LocalDate getLocalDate() {
    return localDate;
  }
  
  @Override
  public String toString() {
    if (localDate != null) {
      return localDate.toString();
    } else {
      return "";
    }
  }
}
