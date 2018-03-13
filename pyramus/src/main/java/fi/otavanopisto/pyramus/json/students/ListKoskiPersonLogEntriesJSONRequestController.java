package fi.otavanopisto.pyramus.json.students;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonLog;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ListKoskiPersonLogEntriesJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ListKoskiPersonLogEntriesJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
      KoskiPersonLogDAO koskiPersonLogDAO = DAOFactory.getInstance().getKoskiPersonLogDAO();
      
      Long personId = requestContext.getLong("personId");
      
      if (personId == null) {
        logger.log(Level.WARNING, "Unable to load log entries due to missing personId.");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      
      Person person = personDAO.findById(personId);
      List<KoskiPersonLog> logEntries = koskiPersonLogDAO.listByPerson(person);
      logEntries.sort((a, b) -> Comparator.nullsFirst(Date::compareTo).reversed().compare(a.getDate(), b.getDate()));
      
      Locale locale = requestContext.getRequest().getLocale();
      
      List<Map<String, Object>> results = new ArrayList<>();
      for (KoskiPersonLog logEntry : logEntries) {
        String studyProgrammeName = logEntry.getStudent() != null && logEntry.getStudent().getStudyProgramme() != null ?
            logEntry.getStudent().getStudyProgramme().getName() : null;
        
        Map<String, Object> logEntryInfo = new HashMap<>();
        logEntryInfo.put("id", logEntry.getId());
        logEntryInfo.put("date", logEntry.getDate().getTime());
        logEntryInfo.put("state", logEntry.getState());
        logEntryInfo.put("stateType", getStateType(logEntry));
        logEntryInfo.put("text", getStateDisplayText(logEntry, locale));
        logEntryInfo.put("personId", logEntry.getPerson().getId());
        logEntryInfo.put("studentId", logEntry.getStudent() != null ? logEntry.getStudent().getId() : null);
        logEntryInfo.put("studyProgrammeName", studyProgrammeName);
        logEntryInfo.put("message", logEntry.getMessage());
        results.add(logEntryInfo);
      }
      
      requestContext.addResponseParameter("logEntries", results);
      requestContext.addResponseParameter("koskiStatus", getStatus(results));
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading log entries", e);
    }
  }

  private String getStatus(List<Map<String, Object>> results) {
    Set<String> states = results.stream().map(logEntryInfo -> (String) logEntryInfo.get("stateType")).collect(Collectors.toSet());
    if (states.contains("ERROR")) {
      return "ERROR";
    }
    
    if (states.contains("WARNING")) {
      return "WARNING";
    }
    
    if (states.contains("SUCCESS")) {
      return "SUCCESS";
    }
    
    if (states.contains("PENDING")) {
      return "PENDING";
    }
    
    return "UNKNOWN";
  }

  private String getStateType(KoskiPersonLog logEntry) {
    if (ArrayUtils.contains(KoskiPersonState.PENDING_STATES, logEntry.getState())) {
      return "PENDING";
    }
    
    if (ArrayUtils.contains(KoskiPersonState.SUCCESS_STATES, logEntry.getState())) {
      return "SUCCESS";
    }

    if (ArrayUtils.contains(KoskiPersonState.WARNING_STATES, logEntry.getState())) {
      return "WARNING";
    }
    
    if (ArrayUtils.contains(KoskiPersonState.ERROR_STATES, logEntry.getState())) {
      return "ERROR";
    }
    
    return "UNKNOWN";
  }
  
  private String getStateDisplayText(KoskiPersonLog logEntry, Locale locale) {
    String localeKey = "koski.states." + logEntry.getState().toString();
    try {
      return Messages.getInstance().getText(locale, localeKey);
    } catch (MissingResourceException mre) {
      return logEntry.getState().toString();
    }
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
