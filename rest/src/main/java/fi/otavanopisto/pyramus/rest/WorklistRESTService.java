package fi.otavanopisto.pyramus.rest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemEditableFields;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemState;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplateType;
import fi.otavanopisto.pyramus.framework.StaffMemberProperties;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.controller.AssessmentController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.WorklistController;
import fi.otavanopisto.pyramus.rest.controller.permissions.WorklistPermissions;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistApproverRestModel;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistBasePriceRestModel;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistItemBilledPriceRestModel;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistItemCourseAssessmentRestModel;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistItemRestModel;
import fi.otavanopisto.pyramus.rest.model.worklist.WorklistItemStateChangeRestModel;
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
  private Logger logger;

  @Inject
  private SessionController sessionController;

  @Inject
  private UserController userController;

  @Inject
  private CourseController courseController;

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
  public Response createWorklistItem(WorklistItemRestModel payload) {
    
    // Payload validation
    
    if (payload.getTemplateId() == null) {
      return Response.status(Status.BAD_REQUEST).entity("Missing templateId").build(); 
    }
    WorklistItemTemplate template = worklistController.findTemplateById(payload.getTemplateId());
    if (template == null) {
      return Response.status(Status.NOT_FOUND).entity("Template not found").build();
    }
    if (template.getTemplateType() != WorklistItemTemplateType.DEFAULT) {
      return Response.status(Status.BAD_REQUEST).entity("Template is not user-creatable").build();
    }
    
    // Get values from template or payload, depending on editability
    
    Set<WorklistItemEditableFields> fields = WorklistController.editableFieldsFromString(template.getEditableFields());
    String description = fields.contains(WorklistItemEditableFields.DESCRIPTION)
        ? payload.getDescription()
        : template.getDescription();
    Date entryDate = fields.contains(WorklistItemEditableFields.ENTRYDATE)
        ? java.sql.Date.valueOf(payload.getEntryDate())
        : new Date();
    Double price = fields.contains(WorklistItemEditableFields.PRICE)
        ? payload.getPrice()
        : template.getPrice();
    Double factor = fields.contains(WorklistItemEditableFields.FACTOR)
        ? payload.getFactor()
        : template.getFactor();
    String billingNumber = fields.contains(WorklistItemEditableFields.BILLING_NUMBER)
        ? payload.getBillingNumber()
        : template.getBillingNumber();
    
    // Create a worklist item based on the template
    
    WorklistItem worklistItem = worklistController.create(
        sessionController.getUser(),
        template,
        entryDate,
        description,
        price,
        factor,
        billingNumber,
        null,
        sessionController.getUser());
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
    if (worklistItem.getTemplate().getTemplateType() != WorklistItemTemplateType.DEFAULT) { 
      return Response.status(Status.FORBIDDEN).entity("Item is based on a non-editable template").build();
    }
    if (worklistItem.getState() == WorklistItemState.APPROVED || worklistItem.getState() == WorklistItemState.PAID) {
      return Response.status(Status.FORBIDDEN).entity("Item is already approved or paid").build();
    }
    
    // Get values from item or payload, depending on editability
    
    Set<WorklistItemEditableFields> fields = WorklistController.editableFieldsFromString(worklistItem.getEditableFields());
    String description = fields.contains(WorklistItemEditableFields.DESCRIPTION)
        ? payload.getDescription()
        : worklistItem.getDescription();
    Date entryDate = fields.contains(WorklistItemEditableFields.ENTRYDATE)
        ? java.sql.Date.valueOf(payload.getEntryDate())
        : worklistItem.getEntryDate();
    Double price = fields.contains(WorklistItemEditableFields.PRICE)
        ? payload.getPrice()
        : worklistItem.getPrice();
    Double factor = fields.contains(WorklistItemEditableFields.FACTOR)
        ? payload.getFactor()
        : worklistItem.getFactor();
    String billingNumber = fields.contains(WorklistItemEditableFields.BILLING_NUMBER)
        ? payload.getBillingNumber()
        : worklistItem.getBillingNumber();
        
    // Update the worklist item
    
    worklistItem = worklistController.update(
        worklistItem,
        entryDate,
        description,
        price,
        factor,
        billingNumber,
        WorklistItemState.ENTERED,
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
    if (worklistItem.getState() == WorklistItemState.APPROVED || worklistItem.getState() == WorklistItemState.PAID) {
      return Response.status(Status.FORBIDDEN).entity("Item is already approved or paid").build();
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
      if (!StringUtils.isEmpty(beginDate)) {
        begin = java.sql.Timestamp.valueOf(LocalDate.parse(beginDate).atStartOfDay());
      }
      if (!StringUtils.isEmpty(endDate)) {
        end = java.sql.Timestamp.valueOf(LocalDate.parse(endDate).atTime(23, 59, 59));
      }
      if (begin != null && end != null && begin.after(end)) {
        return Response.status(Status.BAD_REQUEST).entity("Invalid timeframe").build();
      }
    }
    catch (DateTimeParseException e) {
      return Response.status(Status.BAD_REQUEST).entity("Invalid time").build();
    }

    List<WorklistItem> worklistItems = worklistController.listWorklistItemsByOwnerAndTimeframe(user, begin, end);
    
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
      SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL", request.getLocale());
      String month = StringUtils.capitalize(dateFormat.format(c.getTime()));
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

  /*
   * Batch updates the state of all worklist items belonging to the specified user and timeframe.
   * Only updates items that follow the proper change state flow. 
   */
  @Path("/changeItemsState")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  public Response updateWorklistItemsState(WorklistItemStateChangeRestModel stateChange) {
    
    Long ownerId = Long.valueOf(stateChange.getUserIdentifier());
    
    // Access check; suitable permission or updating your own items
    
    if (!sessionController.hasEnvironmentPermission(WorklistPermissions.UPDATE_WORKLISTITEM)) {
      if (!Objects.equals(sessionController.getUser().getId(), ownerId)) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    
    // Payload validation
    
    User user = userController.findUserById(ownerId);
    if (user ==  null) {
      return Response.status(Status.NOT_FOUND).entity("User not found").build();
    }
    Date beginDate = java.sql.Timestamp.valueOf(stateChange.getBeginDate().atStartOfDay());
    Date endDate = java.sql.Timestamp.valueOf(stateChange.getEndDate().atTime(23, 59, 59));
    WorklistItemState state = WorklistItemState.valueOf(stateChange.getState());
    
    // Update the items that follow the proper state change flow of ENTERED -> PROPOSED -> APPROVED -> PAID
    
    List<WorklistItem> worklistItems = worklistController.listWorklistItemsByOwnerAndTimeframe(user, beginDate, endDate);
    worklistController.updateState(worklistItems, state, true);
    return Response.noContent().build();    
  }
  
  @Path("/approvers")
  @GET
  @RESTPermit (WorklistPermissions.LIST_WORKLISTAPPROVERS)
  public Response listWorklistApprovers() {
    List<WorklistApproverRestModel> approvers = new ArrayList<>();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    List<StaffMember> staffMembers = staffMemberDAO.listByProperty(StaffMemberProperties.WORKLIST_APPROVER.getKey(), "1");
    for (StaffMember staffMember : staffMembers) {
      approvers.add(createRestModel(staffMember));
    }
    return Response.ok(approvers).build();
  }
  
  @Path("/basePrice")
  @GET
  @RESTPermit (WorklistPermissions.ACCESS_WORKLIST_PRICING)
  public Response getCourseBasePrice(@QueryParam("course") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    else {
      WorklistBasePriceRestModel basePrices = new WorklistBasePriceRestModel();
      for (CourseModule courseModule : course.getCourseModules()) {
        Double basePrice = worklistController.getCourseModuleBasePrice(courseModule, sessionController.getUser());
        if (basePrice != null) {
          basePrices.put(courseModule.getId(), basePrice);
        }
      }
      return Response.ok(basePrices).build();
    }
  }

  @Path("/billedPrice")
  @GET
  @RESTPermit (WorklistPermissions.ACCESS_WORKLIST_BILLING)
  public Response getCourseBilledPrice(@QueryParam("courseAssessment") Long courseAssessmentId) {
    CourseAssessment courseAssessment = assessmentController.findCourseAssessmentById(courseAssessmentId);
    if (courseAssessment != null) {
      List<WorklistItem> items = worklistController.listByCourseAssessment(courseAssessment);
      if (items.size() > 1) {
        logger.severe(String.format("Course assessment %d is associated with multiple worklist items", courseAssessmentId));
      }
      else if (items.size() == 1) {
        WorklistItem item = items.get(0);
        WorklistItemBilledPriceRestModel billedPrice = new WorklistItemBilledPriceRestModel();
        billedPrice.setAssessmentIdentifier(courseAssessment.getId().toString());
        billedPrice.setPrice(item.getPrice());
        billedPrice.setEditable(item.getState() == WorklistItemState.ENTERED || item.getState() == WorklistItemState.PROPOSED);
        return Response.ok(billedPrice).build();
      }
    }
    return Response.status(Status.NOT_FOUND).build();
  }

  @Path("/billedPrice")
  @PUT
  @RESTPermit (WorklistPermissions.ACCESS_WORKLIST_BILLING)
  public Response updateCourseBilledPrice(WorklistItemBilledPriceRestModel payload) {
    CourseAssessment courseAssessment = assessmentController.findCourseAssessmentById(Long.valueOf(payload.getAssessmentIdentifier()));
    if (courseAssessment != null) {
      List<WorklistItem> items = worklistController.listByCourseAssessment(courseAssessment);
      if (items.size() > 1) {
        logger.severe(String.format("Course assessment %s is associated with multiple worklist items", payload.getAssessmentIdentifier()));
      }
      else if (items.size() == 1) {
        WorklistItem item = items.get(0);
        if (item.getState() == WorklistItemState.APPROVED || item.getState() == WorklistItemState.PAID) {
          logger.warning(String.format("Attempted to update price of worklist item %d that is already approved or paid", item.getId()));
          return Response.status(Status.BAD_REQUEST).build();
        }
        worklistController.updateBilledPrice(item, payload.getPrice());
        return Response.ok(payload).build(); 
      }
    }
    return Response.status(Status.NOT_FOUND).build();
  }

  private WorklistApproverRestModel createRestModel(StaffMember staffMember) {
    WorklistApproverRestModel restModel = new WorklistApproverRestModel();
    restModel.setName(staffMember.getFullName());
    restModel.setEmail(staffMember.getPrimaryEmail() == null ? null : staffMember.getPrimaryEmail().getAddress());
    return restModel;
  }
  
  private WorklistItemTemplateRestModel createRestModel(WorklistItemTemplate template) {
    WorklistItemTemplateRestModel restModel = new WorklistItemTemplateRestModel();
    restModel.setId(template.getId());
    restModel.setDescription(template.getDescription());
    restModel.setPrice(template.getPrice());
    restModel.setFactor(template.getFactor());
    restModel.setBillingNumber(template.getBillingNumber());
    restModel.setEditableFields(StringUtils.isEmpty(template.getEditableFields())
        ? Collections.emptySet()
        : Stream.of(template.getEditableFields().split(",")).collect(Collectors.toSet()));
    return restModel;
  }
  
  private WorklistItemRestModel createRestModel(WorklistItem worklistItem) {
    WorklistItemRestModel restModel = new WorklistItemRestModel();
    restModel.setId(worklistItem.getId());
    restModel.setTemplateId(worklistItem.getTemplate().getId());
    restModel.setState(worklistItem.getState().toString());
    restModel.setEntryDate(new Date(worklistItem.getEntryDate().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    restModel.setDescription(worklistItem.getDescription());
    restModel.setPrice(worklistItem.getPrice());
    restModel.setFactor(worklistItem.getFactor());
    restModel.setBillingNumber(worklistItem.getBillingNumber());
    if (worklistItem.getCourseAssessment() != null) {
      restModel.setCourseAssessment(createRestModel(worklistItem.getCourseAssessment()));
    }
    if (worklistItem.getState() == WorklistItemState.APPROVED || worklistItem.getState() == WorklistItemState.PAID) {
      restModel.setEditableFields(Collections.emptySet());
      restModel.setRemovable(Boolean.FALSE);
    }
    else {
      restModel.setEditableFields(StringUtils.isEmpty(worklistItem.getEditableFields())
          ? Collections.emptySet()
          : Stream.of(worklistItem.getEditableFields().split(",")).collect(Collectors.toSet()));
      restModel.setRemovable(worklistItem.getTemplate().getRemovable());
    }
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
