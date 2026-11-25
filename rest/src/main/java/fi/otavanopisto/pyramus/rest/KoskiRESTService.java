package fi.otavanopisto.pyramus.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonLog;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.koski.KoskiController;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.rest.annotation.AuthScope;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.controller.PersonController;
import fi.otavanopisto.pyramus.rest.controller.permissions.PyramusViewPermissions;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/koski")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
@AuthScope(AuthScope.LEGACY)
public class KoskiRESTService extends AbstractRESTService {

  @Inject
  private Logger logger;
  
  @Inject
  private PersonController personController;

  @Inject
  private SessionController sessionController;
  
  @Inject
  private KoskiController koskiController;
  
  @Inject
  private KoskiPersonLogDAO koskiPersonLogDAO;

  @Path("/persons/{ID:[0-9]*}/oppija")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response getOppija(@PathParam("ID") Long id) {
    if (sessionController.getUser() == null || !sessionController.getUser().hasRole(Role.ADMINISTRATOR)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    try {
      Oppija oppija = koskiController.personToOppija(person);
      
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

      return Response.ok(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oppija)).build();
    } catch (Exception e) {
      e.printStackTrace();
      logger.log(Level.SEVERE, "Couldn't produce output", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Path("/persons/{ID:[0-9]*}/queuestatus")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response getQueueStatus(@PathParam("ID") Long personId) {
    if (!sessionController.isLoggedIn() || !sessionController.hasEnvironmentPermission(PyramusViewPermissions.VIEW_STUDENT)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Person person = personController.findPersonById(personId);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(getKoskiQueueStatus(person)).build();
  }

  @Path("/persons/{ID:[0-9]*}/queuestatus")
  @PUT
  @RESTPermit (handling = Handling.INLINE)
  public Response putQueueStatus(@PathParam("ID") Long personId) {
    if (!sessionController.isLoggedIn() || !sessionController.hasEnvironmentPermission(PyramusViewPermissions.VIEW_STUDENT)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Person person = personController.findPersonById(personId);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    koskiController.markForUpdate(person);
    
    return Response.ok(getKoskiQueueStatus(person)).build();
  }

  private Map<String, Object> getKoskiQueueStatus(Person person) {
    List<KoskiPersonLog> logEntries = koskiPersonLogDAO.listByPerson(person);
    logEntries.sort((a, b) -> Comparator.nullsFirst(Date::compareTo).reversed().compare(a.getDate(), b.getDate()));
    
    Set<String> stateTypes = new HashSet<>();
    List<Map<String, Object>> logEntryRestModels = new ArrayList<>();
    for (KoskiPersonLog logEntry : logEntries) {
      String studyProgrammeName = logEntry.getStudent() != null && logEntry.getStudent().getStudyProgramme() != null ?
          logEntry.getStudent().getStudyProgramme().getName() : null;

      String stateType = getStateType(logEntry);
      stateTypes.add(stateType);
      
      Map<String, Object> logEntryInfo = new HashMap<>();
      logEntryInfo.put("id", logEntry.getId());
      logEntryInfo.put("date", logEntry.getDate().getTime());
      logEntryInfo.put("state", logEntry.getState());
      logEntryInfo.put("stateType", stateType);
      logEntryInfo.put("personId", logEntry.getPerson().getId());
      logEntryInfo.put("studentId", logEntry.getStudent() != null ? logEntry.getStudent().getId() : null);
      logEntryInfo.put("studyProgrammeName", studyProgrammeName);
      logEntryInfo.put("message", logEntry.getMessage());
      logEntryRestModels.add(logEntryInfo);
    }

    Map<String, Object> result = new HashMap<>();
    result.put("koskiStatus", getStatus(logEntryRestModels));
    result.put("logEntries", logEntryRestModels);
    return result;
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
  
}
