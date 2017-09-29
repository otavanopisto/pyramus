package fi.otavanopisto.pyramus.views.applications;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

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

  public static String applicationLineUiValue(String value) {
    switch (value) {
    case "aineopiskelu":
      return "Aineopiskelu";
    case "nettilukio":
      return "Nettilukio";
    case "nettipk":
      return "Nettiperuskoulu";
    case "aikuislukio":
      return "Aikuislukio";
    case "bandilinja":
      return "Bändilinja";
    case "mk":
      return "Maahanmuuttajakoulutukset";
    default:
      return null;
    }
  }

  public static String constructSSN(String birthday, String ssnEnd) {
    try {
      StringBuffer ssn = new StringBuffer();
      Date d = new SimpleDateFormat("d.M.yyyy").parse(birthday);
      ssn.append(new SimpleDateFormat("ddMMyy").format(d));
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(d);
      ssn.append(calendar.get(Calendar.YEAR) >= 2000 ? 'A' : '-');
      ssn.append(StringUtils.upperCase(ssnEnd));
      return ssn.toString();
    }
    catch (ParseException e) {
      return null;
    }
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
