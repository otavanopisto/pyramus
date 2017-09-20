package fi.otavanopisto.pyramus.views.applications;

import java.util.Date;

import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;

public class ApplicationUtils {

  public static String applicationStateUiValue(ApplicationState applicationState) {
    switch (applicationState) {
    case PENDING:
      return "Jätetty";
    case PROCESSING:
      return "Käsittelyssä";
    case WAITING_STAFF_SIGNATURE:
      return "Odottaa virallista hyväksyntää";
    case STAFF_SIGNED:
      return "Hyväksyntä allekirjoitettu";
    case APPROVED_BY_SCHOOL:
      return "Hyväksytty";
    case APPROVED_BY_APPLICANT:
      return "Opiskelupaikka vastaanotettu";
    case TRANSFERRED_AS_STUDENT:
      return "Siirretty opiskelijaksi";
    case REGISTERED_AS_STUDENT:
      return "Rekisteröitynyt aineopiskelijaksi";
    case REJECTED:
      return "Hylätty";
    }
    return null;
  }

  public static Date getLatest(Date...dates) {
    Date result = null;
    for (Date date : dates) {
      if (result == null || date.after(result)) {
        result = date;
      }
    }
    return result;
  }
  

}
