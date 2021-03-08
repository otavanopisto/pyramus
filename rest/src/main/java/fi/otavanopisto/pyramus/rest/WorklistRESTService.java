package fi.otavanopisto.pyramus.rest;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplateType;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.controller.AssessmentController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.WorklistController;
import fi.otavanopisto.pyramus.rest.controller.permissions.WorklistPermissions;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistItemCourseAssessmentRestModel;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistItemRestModel;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistItemTemplateRestModel;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistSummaryItemRestModel;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/worklist")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class WorklistRESTService {

  @Inject
  private SessionController sessionController;

  @Inject
  private UserController userController;

  @Inject
  private AssessmentController assessmentController;
  
  @Inject
  private WorklistController worklistController;
  
  /*
   * Return templates from which worklist items can be created from 
   */
  @Path("/templates")
  @GET
  @RESTPermit (WorklistPermissions.LIST_WORKLISTITEMTEMPLATES)
  public Response listWorklistItemTemplates(@QueryParam("includeNonCreatable") @DefaultValue("false") Boolean includeNonCreatable) {
    List<WorklistItemTemplate> templates = worklistController.listWorklistItemTemplates(includeNonCreatable);
    List<WorklistItemTemplateRestModel> restTemplates = new ArrayList<>();
    for (WorklistItemTemplate template : templates) {
      restTemplates.add(createRestModel(template));
    }
    return Response.ok(restTemplates).build();
  }
  
  /**
   * Creates a new worklist item based on the given template.
   */
  @Path("/worklistItems")
  @POST
  @RESTPermit (WorklistPermissions.CREATE_WORKLISTITEM)
  public Response createWorklistItem(WorklistItemTemplateRestModel payload) {
    
    // Payload validation
    
    if (payload.getId() == null) {
      return Response.status(Status.BAD_REQUEST).entity("Missing templateId").build(); 
    }
    WorklistItemTemplate template = worklistController.findTemplateById(payload.getId());
    if (template == null) {
      return Response.status(Status.NOT_FOUND).entity("Template not found").build();
    }
    if (!template.isUserCreatable()) {
      return Response.status(Status.BAD_REQUEST).entity("Template is not user-creatable").build();
    }
    
    // Create a worklist item based on the template
    
    WorklistItem worklistItem = worklistController.create(sessionController.getUser(), template, null, sessionController.getUser());
    return Response.ok(createRestModel(worklistItem)).build();
  }
  
  /**
   * Updates an existing worklist item.
   */
  @Path("/worklistItems")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  public Response updateWorklistItem(WorklistItemRestModel payload) {
    
    // Access check; suitable permission or updating your own item (if template allows it)

    WorklistItem worklistItem = worklistController.findItemById(payload.getId());
    if (worklistItem == null) {
      return Response.status(Status.NOT_FOUND).entity("Item not found").build();
    }
    if (!sessionController.hasEnvironmentPermission(WorklistPermissions.UPDATE_WORKLISTITEM)) {
      if (!Objects.equals(sessionController.getUser().getId(), worklistItem.getOwner().getId())) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    if (!worklistItem.getTemplate().isUserEditable()) { 
      return Response.status(Status.FORBIDDEN).entity("Item is based on a non-editable template").build();
    }
    if (worklistItem.getLocked()) {
      return Response.status(Status.FORBIDDEN).entity("Item is locked").build();
    }
    worklistItem = worklistController.update(
        worklistItem,
        java.sql.Date.valueOf(payload.getEntryDate()),
        payload.getDescription(),
        payload.getPrice(),
        payload.getFactor(),
        sessionController.getUser());
    return Response.ok(createRestModel(worklistItem)).build();
  }

  /**
   * Removes an existing worklist item.
   */
  @Path("/worklistItems/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(handling = Handling.INLINE)
  public Response removeWorklistItem(@PathParam("ID") Long id) {
    
    // Access check; suitable permission or deleting your own item (if template allows it)

    WorklistItem worklistItem = worklistController.findItemById(id);
    if (worklistItem == null) {
      return Response.status(Status.NOT_FOUND).entity("Item not found").build();
    }
    if (!sessionController.hasEnvironmentPermission(WorklistPermissions.DELETE_WORKLISTITEM)) {
      if (!Objects.equals(sessionController.getUser().getId(), worklistItem.getOwner().getId())) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    if (!worklistItem.getTemplate().getRemovable()) { 
      return Response.status(Status.FORBIDDEN).entity("Item is based on a non-removable template").build();
    }
    if (worklistItem.getLocked()) {
      return Response.status(Status.FORBIDDEN).entity("Item is locked").build();
    }
    worklistController.remove(worklistItem, false);
    return Response.noContent().build();
  }
  
  /*
   * Return all worklist items of a person (preferably limited to an optional timeframe)
   */
  @Path("/worklistItems")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listWorklistItemsByOwnerAndTimeframe(@QueryParam("owner") Long ownerId, @QueryParam("beginDate") String beginDate, @QueryParam("endDate") String endDate) {
    
    // Access check; suitable permission or querying for your own items
    
    if (!sessionController.hasEnvironmentPermission(WorklistPermissions.LIST_WORKLISTITEMS)) {
      if (!Objects.equals(sessionController.getUser().getId(), ownerId)) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    
    // Request params validation
    
    if (ownerId == null) {
      return Response.status(Status.BAD_REQUEST).entity("Missing owner").build();
    }
    User user = userController.findUserById(ownerId);
    if (user ==  null) {
      return Response.status(Status.NOT_FOUND).entity("User not found").build();
    }
    Date begin = null;
    Date end = null;
    try {
      if (!StringUtils.isAnyEmpty(beginDate, endDate)) {
        begin = java.sql.Timestamp.valueOf(LocalDate.parse(beginDate).atStartOfDay());
        end = java.sql.Timestamp.valueOf(LocalDate.parse(endDate).atTime(23, 59, 59));
        if (begin.after(end)) {
          return Response.status(Status.BAD_REQUEST).entity("Invalid timeframe").build();
        }
      }
    }
    catch (DateTimeParseException e) {
      return Response.status(Status.BAD_REQUEST).entity("Invalid time").build();
    }

    List<WorklistItem> worklistItems;
    if (beginDate == null) {
      worklistItems = worklistController.listWorklistItemsByOwner(user);
    }
    else {
      worklistItems = worklistController.listWorklistItemsByOwnerAndTimeframe(user, begin, end);
    }
    
    List<WorklistItemRestModel> restItems = new ArrayList<>();
    for (WorklistItem worklistItem : worklistItems) {
      restItems.add(createRestModel(worklistItem));
    }
    return Response.ok(restItems).build();
  }

  /**
   * Provides a monthly summary object for one person's worklist entries.
   */
  @Path("/worklistItemsSummary")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response getWorklistItemSummaryByPerson(@Context HttpServletRequest request, @QueryParam("owner") Long ownerId) {
    
    // Request params validation
    
    if (ownerId == null) {
      return Response.status(Status.BAD_REQUEST).entity("Missing owner").build();
    }
    User user = userController.findUserById(ownerId);
    if (user == null) {
      return Response.status(Status.NOT_FOUND).entity("User not found").build();
    }

    // Access check; suitable permission or querying for your own
    
    if (!sessionController.hasEnvironmentPermission(WorklistPermissions.LIST_WORKLISTITEMS)) {
      if (!Objects.equals(sessionController.getUser().getId(), ownerId)) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    
    // Construct a monthly statistics summary of the user's worklist items
    
    Calendar c = Calendar.getInstance();
    WorklistSummaryItemRestModel monthlyItem;
    List<WorklistSummaryItemRestModel> monthlyItems = new ArrayList<>();
    Map<String, WorklistSummaryItemRestModel> monthlyMap = new HashMap<>();
    List<WorklistItem> worklistItems = worklistController.listWorklistItemsByOwner(user);
    for (WorklistItem worklistItem : worklistItems) {
      c.setTime(worklistItem.getEntryDate());
      String month = Month.of(c.get(Calendar.MONTH) + 1).getDisplayName(TextStyle.FULL_STANDALONE, request.getLocale());
      int year = c.get(Calendar.YEAR);
      String displayName = String.format("%s %d", month, year);
      if (monthlyMap.containsKey(displayName)) {
        monthlyItem = monthlyMap.get(displayName);  
      }
      else {
        monthlyItem = new WorklistSummaryItemRestModel();
        monthlyItem.setDisplayName(displayName);
        c.set(Calendar.DAY_OF_MONTH, 1);
        monthlyItem.setBeginDate(new java.sql.Date(c.getTimeInMillis()).toLocalDate());
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthlyItem.setEndDate(new java.sql.Date(c.getTimeInMillis()).toLocalDate());
        monthlyMap.put(displayName,  monthlyItem);
        monthlyItems.add(monthlyItem);
      }
      monthlyItem.incCount();
    }
    
    return Response.ok(monthlyItems).build();
  }
  
  private WorklistItemTemplateRestModel createRestModel(WorklistItemTemplate template) {
    WorklistItemTemplateRestModel restModel = new WorklistItemTemplateRestModel();
    restModel.setId(template.getId());
    restModel.setDescription(template.getDescription());
    restModel.setPrice(template.getPrice());
    restModel.setFactor(template.getFactor());
    restModel.setTemplateType(template.getTemplateType().toString());
    restModel.setRemovable(template.getRemovable());
    return restModel;
  }
  
  private WorklistItemRestModel createRestModel(WorklistItem worklistItem) {
    WorklistItemRestModel restModel = new WorklistItemRestModel();
    restModel.setId(worklistItem.getId());
    restModel.setEntryDate(new Date(worklistItem.getEntryDate().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    restModel.setDescription(worklistItem.getDescription());
    restModel.setPrice(worklistItem.getPrice());
    restModel.setFactor(worklistItem.getFactor());
    if (worklistItem.getCourseAssessment() != null) {
      restModel.setCourseAssessment(createRestModel(worklistItem.getCourseAssessment()));
    }
    restModel.setEditable(worklistItem.getTemplate().getTemplateType() == WorklistItemTemplateType.EDITABLE && !worklistItem.getLocked());
    restModel.setRemovable(worklistItem.getTemplate().getRemovable() && !worklistItem.getLocked());
    return restModel;
  }
  
  private WorklistItemCourseAssessmentRestModel createRestModel(CourseAssessment courseAssessment) {
    WorklistItemCourseAssessmentRestModel restModel = new WorklistItemCourseAssessmentRestModel();
    
    // Student display name
    
    StringBuilder sb = new StringBuilder();
    Student student = courseAssessment.getStudent();
    sb.append(student.getFirstName());
    if (!StringUtils.isEmpty(student.getNickname())) {
      sb.append(String.format(" \"%s\"", student.getNickname()));
    }
    if (!StringUtils.isEmpty(student.getLastName())) {
      sb.append(String.format(" %s", student.getLastName()));
    }
    if (student.getStudyProgramme() != null) {
      sb.append(String.format(" (%s)", student.getStudyProgramme().getName()));
    }
    restModel.setStudentName(sb.toString());
    
    // Course display name
    
    sb = new StringBuilder();
    Course course = courseAssessment.getCourseStudent().getCourse();
    sb.append(course.getName());
    if (!StringUtils.isEmpty(course.getNameExtension())) {
      sb.append(String.format(" (%s)", course.getNameExtension()));
    }
    restModel.setCourseName(sb.toString());
    
    // Grade information
    
    restModel.setGrade(courseAssessment.getGrade().getName());
    restModel.setRaisedGrade(assessmentController.isRaisedGrade(courseAssessment));
    
    return restModel;
  }

}
