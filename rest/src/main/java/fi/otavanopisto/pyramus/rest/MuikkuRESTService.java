package fi.otavanopisto.pyramus.rest;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.users.InternalAuthDAO;
import fi.otavanopisto.pyramus.dao.users.PasswordResetRequestDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Defaults;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.PasswordResetRequest;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.framework.UserEmailInUseException;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.ClientApplicationController;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.PersonController;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.StudentGroupController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.MuikkuPermissions;
import fi.otavanopisto.pyramus.rest.model.muikku.CredentialResetPayload;
import fi.otavanopisto.pyramus.rest.model.muikku.StaffMemberPayload;
import fi.otavanopisto.pyramus.rest.model.muikku.StudentGroupMembersPayload;
import fi.otavanopisto.pyramus.rest.model.muikku.StudentGroupPayload;
import fi.otavanopisto.pyramus.rest.model.muikku.StudentPayload;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/muikku")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class MuikkuRESTService {

  @Resource
  private SessionContext sessionContext;

  @Inject
  private Logger logger;
  
  @Inject
  private CommonController commonController;

  @Inject
  private PersonController personController;

  @Inject
  private UserController userController;

  @Inject
  private StudentController studentController;

  @Inject
  private StudentGroupController studentGroupController;

  @Inject
  private SessionController sessionController;

  @Inject
  private InternalAuthDAO internalAuthDAO;
  
  @Inject
  private UserIdentificationDAO userIdentificationDAO;

  @Inject
  private ClientApplicationController clientApplicationController;

  @Inject
  private PasswordResetRequestDAO passwordResetRequestDAO;

  @Path("/users")
  @POST
  @RESTPermit(MuikkuPermissions.MUIKKU_CREATE_STAFF_MEMBER)
  public Response createUser(@Context HttpServletRequest request, StaffMemberPayload payload) {
    
    // Prerequisites
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    Defaults defaults = defaultsDAO.getDefaults();
    if (defaults.getUserDefaultContactType() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("userDefaultContactType not set in Defaults").build();
    }
    User loggedUser = sessionController.getUser();    
    if (loggedUser.getOrganization() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Current user lacks organization").build();
    }

    // Basic payload validation
    
    if (StringUtils.isAnyBlank(payload.getFirstName(), payload.getLastName(), payload.getEmail(), payload.getRole())) {
      return Response.status(Status.BAD_REQUEST).entity("Empty fields in payload").build();
    }
    
    // Endpoint only supports creation of managers and teachers
    
    Role role = Role.valueOf(payload.getRole());
    if (role != Role.MANAGER && role != Role.TEACHER && role != Role.STUDY_GUIDER && role != Role.STUDY_PROGRAMME_LEADER) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Unsupported role %s", payload.getRole())).build();
    }
    
    // Check if user exists based on email
    
    String address = StringUtils.trim(StringUtils.lowerCase(payload.getEmail()));
    if (!commonController.isEmailAvailable(address)) {
      return Response.status(Status.CONFLICT).entity(getMessage(request.getLocale(), "error.emailInUse")).build();
    }
    
    // User creation
    
    Person person = personController.createPerson(null,  null,  null,  null,  Boolean.FALSE);
    StaffMember staffMember = userController.createStaffMember(loggedUser.getOrganization(), payload.getFirstName(), payload.getLastName(), role, person);
    personController.updatePersonDefaultUser(person, staffMember);
    userController.addUserEmail(staffMember, defaults.getUserDefaultContactType(), address, Boolean.TRUE);
    payload.setIdentifier(staffMember.getId().toString());
    
    return Response.ok(payload).build();
  }

  @Path("/users/{IDENTIFIER}")
  @PUT
  @RESTPermit(MuikkuPermissions.MUIKKU_UPDATE_STAFF_MEMBER)
  public Response updateUser(@Context HttpServletRequest request, @PathParam("IDENTIFIER") String identifier, StaffMemberPayload payload) {
    
    // Basic payload validation
    
    if (!StringUtils.equals(payload.getIdentifier(), identifier)) {
      return Response.status(Status.BAD_REQUEST).entity("Payload identifier doesn't match path identifier").build();
    }
    
    if (StringUtils.isAnyBlank(payload.getFirstName(), payload.getLastName(), payload.getEmail(), payload.getRole())) {
      return Response.status(Status.BAD_REQUEST).entity("Empty fields in payload").build();
    }
    
    // Test allowed roles
    
    Role role;
    try {
      role = Role.valueOf(payload.getRole());
      if (role != Role.MANAGER && role != Role.TEACHER && role != Role.STUDY_GUIDER && role != Role.STUDY_PROGRAMME_LEADER) {
        return Response.status(Status.BAD_REQUEST).entity(String.format("Unsupported role %s", payload.getRole())).build();
      }
    }
    catch (Exception e) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Unsupported role %s", payload.getRole())).build();
    }
    
    // Find user
    
    Long staffMemberId = Long.valueOf(payload.getIdentifier());
    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null || !UserUtils.canAccessOrganization(sessionController.getUser(), staffMember.getOrganization())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    Role existingRole = staffMember.getRole();
    if (existingRole != Role.MANAGER && existingRole != Role.TEACHER && existingRole != Role.STUDY_GUIDER && existingRole != Role.STUDY_PROGRAMME_LEADER) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Unsupported role %s", existingRole)).build();
    }

    List<Email> staffMemberEmails = userController.listStaffMemberEmails(staffMember);
    
    if (staffMemberEmails.size() != 1) {
      return Response.status(Status.BAD_REQUEST).entity("User has several emails").build();
    }
    
    Email email = staffMemberEmails.get(0);
    
    String address = StringUtils.trim(StringUtils.lowerCase(payload.getEmail()));
    if (!UserUtils.isAllowedEmail(address, email.getContactType(), staffMember.getPerson().getId())) {
      return Response.status(Status.CONFLICT).entity(getMessage(request.getLocale(), "error.emailInUse")).build();
    }

    // Update user
    
    staffMember = userController.updateStaffMember(staffMember, staffMember.getOrganization(), payload.getFirstName(), payload.getLastName(), role);
    
    // Update email
    try {
      email = userController.updateStaffMemberEmail(staffMember, email, email.getContactType(), address, email.getDefaultAddress());
    } catch (UserEmailInUseException e) {
      // Set the transaction as rollback only
      sessionContext.setRollbackOnly();
      return Response.status(Status.CONFLICT).entity(getMessage(request.getLocale(), "error.emailInUse")).build();
    }

    return Response.ok(toRestModel(staffMember, email)).build();
  }

  @Path("/studentgroups")
  @POST
  @RESTPermit(MuikkuPermissions.MUIKKU_CREATE_STUDENT_GROUP)
  public Response createStudentGroup(@Context HttpServletRequest request, StudentGroupPayload payload) {
    
    // Prerequisites
    User loggedUser = sessionController.getUser();    
    if (loggedUser.getOrganization() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Current user lacks organization").build();
    }

    // Basic payload validation
    
    if (StringUtils.isBlank(payload.getName()) || payload.getIsGuidanceGroup() == null) {
      return Response.status(Status.BAD_REQUEST).entity("Empty fields in payload").build();
    }
    
    // Student group creation
    
    StudentGroup studentGroup = studentGroupController.createStudentGroup(loggedUser.getOrganization(), payload.getName(), null, null, loggedUser, payload.getIsGuidanceGroup());
    payload.setIdentifier(studentGroup.getId().toString());
    
    return Response.ok(payload).build();
  }

  @Path("/studentgroups")
  @PUT
  @RESTPermit(MuikkuPermissions.MUIKKU_UPDATE_STUDENT_GROUP)
  public Response updateStudentGroup(@Context HttpServletRequest request, StudentGroupPayload payload) {
    
    // Prerequisites
    User loggedUser = sessionController.getUser();    
    if (loggedUser.getOrganization() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Current user lacks organization").build();
    }

    // Basic payload validation
    
    if (StringUtils.isAnyBlank(payload.getIdentifier(), payload.getName()) || payload.getIsGuidanceGroup() == null) {
      return Response.status(Status.BAD_REQUEST).entity("Empty fields in payload").build();
    }

    // Find student group
    
    Long studentGroupId = Long.valueOf(payload.getIdentifier());
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(studentGroupId); 
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).entity(String.format("No student group for identifier %s", payload.getIdentifier())).build();
    }
    else if (!UserUtils.canAccessOrganization(loggedUser, studentGroup.getOrganization())) {
      logger.log(Level.SEVERE, String.format("Organization mismatch. User %d attempted to update user group %d", loggedUser.getId(), studentGroupId));
      return Response.status(Status.BAD_REQUEST).entity("No student group access").build();
    }
    
    // Student group update
    
    studentGroup = studentGroupController.updateStudentGroup(
        studentGroup,
        studentGroup.getOrganization(),
        payload.getName(),
        studentGroup.getDescription(),
        studentGroup.getBeginDate(),
        payload.getIsGuidanceGroup(), 
        loggedUser);
    payload.setIdentifier(studentGroup.getId().toString());
    
    return Response.ok(payload).build();
  }

  @Path("/studentgroups/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(MuikkuPermissions.MUIKKU_DELETE_STUDENT_GROUP)
  public Response deleteStudentGroup(@Context HttpServletRequest request, @PathParam("ID") Long id) {
    
    // Prerequisites
    User loggedUser = sessionController.getUser();    
    if (loggedUser.getOrganization() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Current user lacks organization").build();
    }

    // Basic payload validation
    
    if (id == null) {
      return Response.status(Status.BAD_REQUEST).entity("Empty fields in payload").build();
    }

    // Find student group
    
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id); 
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).entity(String.format("No student group for identifier %d", id)).build();
    }
    else if (!UserUtils.canAccessOrganization(loggedUser, studentGroup.getOrganization())) {
      logger.log(Level.SEVERE, String.format("Organization mismatch. User %d attempted to delete user group %d", loggedUser.getId(), id));
      return Response.status(Status.BAD_REQUEST).entity("No student group access").build();
    }
    
    // Student group archive
    
    studentGroupController.archiveStudentGroup(studentGroup, loggedUser);
    
    return Response.noContent().build();
  }

  @Path("/addstudentgroupmembers")
  @PUT
  @RESTPermit(MuikkuPermissions.MUIKKU_ADD_STUDENT_GROUP_MEMBERS)
  public Response addStudentGroupMembers(@Context HttpServletRequest request, StudentGroupMembersPayload payload) {
    
    // Prerequisites
    User loggedUser = sessionController.getUser();    
    if (loggedUser.getOrganization() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Current user lacks organization").build();
    }

    // Basic payload validation
    
    Long groupId = new Long(payload.getGroupIdentifier());
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(groupId);
    if (studentGroup == null) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Student group %d not found", groupId)).build();
    }
    else if (!UserUtils.canAccessOrganization(loggedUser, studentGroup.getOrganization())) {
      logger.log(Level.SEVERE, String.format("Organization mismatch. User %d attempted to access group %d", loggedUser.getId(), groupId));
      return Response.status(Status.BAD_REQUEST).entity("No student group access").build();
    }
    for (String userIdentifier : payload.getUserIdentifiers()) {
      Long userId = new Long(userIdentifier);
      User user = userController.findUserById(userId);
      if (!UserUtils.canAccessOrganization(loggedUser, user.getOrganization())) {
        logger.log(Level.SEVERE, String.format("Organization mismatch. User %d attempted to add user %d to group %d", loggedUser.getId(), userId, groupId));
        return Response.status(Status.BAD_REQUEST).entity("No user access").build();
      }
    }
    
    // Add user group members
    
    for (String userIdentifier : payload.getUserIdentifiers()) {
      Long userId = new Long(userIdentifier);
      User user = userController.findUserById(userId);
      if (user instanceof Student) {
        StudentGroupStudent studentGroupStudent = studentGroupController.findStudentGroupStudentByStudentGroupAndStudent(studentGroup, (Student) user);
        if (studentGroupStudent == null) {
          studentGroupController.createStudentGroupStudent(studentGroup, (Student) user, loggedUser);
        }
      }
      else if (user instanceof StaffMember) {
        StudentGroupUser studentGroupUser = studentGroupController.findStudentGroupUserByStudentGroupAndUser(studentGroup, (StaffMember) user);
        if (studentGroupUser == null) {
          studentGroupController.createStudentGroupStaffMember(studentGroup, (StaffMember) user, loggedUser);
        }
      }
    }
    
    return Response.noContent().build();
  }

  @Path("/removestudentgroupmembers")
  @PUT
  @RESTPermit(MuikkuPermissions.MUIKKU_REMOVE_STUDENT_GROUP_MEMBERS)
  public Response removeStudentGroupMembers(@Context HttpServletRequest request, StudentGroupMembersPayload payload) {
    
    // Prerequisites
    User loggedUser = sessionController.getUser();    
    if (loggedUser.getOrganization() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Current user lacks organization").build();
    }

    // Basic payload validation
    
    Long groupId = new Long(payload.getGroupIdentifier());
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(groupId);
    if (studentGroup == null) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Student group %d not found", groupId)).build();
    }
    else if (!UserUtils.canAccessOrganization(loggedUser, studentGroup.getOrganization())) {
      logger.log(Level.SEVERE, String.format("Organization mismatch. User %d attempted to access group %d", loggedUser.getId(), groupId));
      return Response.status(Status.BAD_REQUEST).entity("No student group access").build();
    }
    for (String userIdentifier : payload.getUserIdentifiers()) {
      Long userId = new Long(userIdentifier);
      User user = userController.findUserById(userId);
      if (!UserUtils.canAccessOrganization(loggedUser, user.getOrganization())) {
        logger.log(Level.SEVERE, String.format("Organization mismatch. User %d attempted to remove user %d from group %d", loggedUser.getId(), userId, groupId));
        return Response.status(Status.BAD_REQUEST).entity("No user access").build();
      }
    }
    
    // Remove user group members
    
    for (String userIdentifier : payload.getUserIdentifiers()) {
      Long userId = new Long(userIdentifier);
      User user = userController.findUserById(userId);
      if (user instanceof Student) {
        StudentGroupStudent studentGroupStudent = studentGroupController.findStudentGroupStudentByStudentGroupAndStudent(studentGroup, (Student) user);
        if (studentGroupStudent != null) {
          studentGroupController.deleteStudentGroupStudent(studentGroupStudent);
        }
      }
      else if (user instanceof StaffMember) {
        StudentGroupUser studentGroupUser = studentGroupController.findStudentGroupUserByStudentGroupAndUser(studentGroup, (StaffMember) user);
        if (studentGroupUser != null) {
          studentGroupController.deleteStudentGroupUser(studentGroupUser);
        }
      }
    }
    
    return Response.noContent().build();
  }

  private StaffMemberPayload toRestModel(StaffMember staffMember, Email email) {
    StaffMemberPayload payload = new StaffMemberPayload();
    payload.setIdentifier(staffMember.getId().toString());
    payload.setFirstName(staffMember.getFirstName());
    payload.setLastName(staffMember.getLastName());
    payload.setEmail(email.getAddress());
    payload.setRole(staffMember.getRole().toString());
    return payload;
  }

  @Path("/students")
  @POST
  @RESTPermit(MuikkuPermissions.MUIKKU_CREATE_STUDENT)
  public Response createStudent(@Context HttpServletRequest request, StudentPayload payload) {
    
    // Prerequisites
    
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    Defaults defaults = defaultsDAO.getDefaults();
    if (defaults.getStudentDefaultContactType() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("studentDefaultContactType not set in Defaults").build();
    }
    User loggedUser = sessionController.getUser();    
    if (loggedUser.getOrganization() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Current user lacks organization").build();
    }

    // Basic payload validation
    
    if (StringUtils.isAnyBlank(payload.getFirstName(), payload.getLastName(), payload.getEmail())) {
      return Response.status(Status.BAD_REQUEST).entity("Empty fields in payload").build();
    }
    Sex sex = null;
    if (!StringUtils.isBlank(payload.getGender())) {
      try {
        sex = Sex.valueOf(payload.getGender());
      }
      catch (IllegalArgumentException e) {
        return Response.status(Status.BAD_REQUEST).entity(String.format("Invalid payload gender %s", payload.getGender())).build();
      }
    }
    
    // Study programme validation
    
    if (StringUtils.isBlank(payload.getStudyProgrammeIdentifier()) || !NumberUtils.isNumber(payload.getStudyProgrammeIdentifier())) {
      return Response.status(Status.BAD_REQUEST).entity("Invalid payload study programme").build();
    }
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudyProgramme studyProgramme = studyProgrammeDAO.findById(Long.valueOf(payload.getStudyProgrammeIdentifier()));
    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studyProgramme.getOrganization())) {
      return Response.status(Status.BAD_REQUEST).entity("No study programme access").build();
    }
    
    // Birthday generation if SSN present

    Date birthday = null;
    if (!StringUtils.isBlank(payload.getSsn())) {
      try {
        birthday = new SimpleDateFormat("ddMMyy").parse(payload.getSsn().substring(0, 6));
      }
      catch (Exception e) {
        return Response.status(Status.BAD_REQUEST).entity("Invalid payload SSN").build();
      }
    }
    
    // Check if user exists based on email or (possible) SSN
    
    String address = StringUtils.trim(StringUtils.lowerCase(payload.getEmail()));
    if (!commonController.isEmailAvailable(address)) {
      return Response.status(Status.CONFLICT).entity(getMessage(request.getLocale(), "error.emailInUse")).build();
    }
    String ssn = null;
    if (!StringUtils.isBlank(payload.getSsn())) {
      ssn = StringUtils.upperCase(payload.getSsn());
      Person person = personController.findBySsn(ssn);
      if (person != null) {
        return Response.status(Status.CONFLICT).entity(getMessage(request.getLocale(), "error.ssnInUse")).build();
      }
    }
    
    // Student creation
    
    Person person = personController.createPerson(birthday, ssn, sex, null, Boolean.FALSE);
    Student student = studentController.createStudent(
        person,
        payload.getFirstName(),
        payload.getLastName(),
        null, // nickname
        null, // additionalInfo
        null, // studyTimeEnd
        null, // activityType
        null, // examinationType
        null, // educationalLevel
        null, // education
        null, // nationality
        null, // municipality
        null, // language
        null, // school
        studyProgramme,
        null, // curriculum
        null, // previousStudies
        new Date(), // studyStartDate 
        null, // studyEndDate
        null, // studyEndReason
        null); // studyEndText
    personController.updatePersonDefaultUser(person, student);
    userController.addUserEmail(student, defaults.getStudentDefaultContactType(), address, Boolean.TRUE);
    payload.setIdentifier(student.getId().toString());
    
    return Response.ok(payload).build();
  }
  
  @Path("/students/{IDENTIFIER}")
  @PUT
  @RESTPermit(MuikkuPermissions.MUIKKU_UPDATE_STUDENT)
  public Response updateStudent(@Context HttpServletRequest request, @PathParam("IDENTIFIER") String identifier, StudentPayload payload) {
    
    // Prerequisites
    
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    Defaults defaults = defaultsDAO.getDefaults();
    if (defaults.getStudentDefaultContactType() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("studentDefaultContactType not set in Defaults").build();
    }
    User loggedUser = sessionController.getUser();    
    if (loggedUser.getOrganization() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Current user lacks organization").build();
    }

    // Find student
    
    Long studentId = Long.valueOf(payload.getIdentifier());
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).entity(String.format("No student for identifier %s", identifier)).build();
    }
    if (student.getPerson().getDefaultUser() == null) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Student %s has no default user set", identifier)).build();
    }
    if (!studentId.equals(student.getPerson().getDefaultUser().getId())) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Student %s is not set as default", identifier)).build();
    }

    // Basic payload validation
    
    if (!StringUtils.equals(payload.getIdentifier(), identifier)) {
      return Response.status(Status.BAD_REQUEST).entity("Payload identifier doesn't match path identifier").build();
    }
    if (StringUtils.isAnyBlank(payload.getFirstName(), payload.getLastName(), payload.getEmail())) {
      return Response.status(Status.BAD_REQUEST).entity("Empty fields in payload").build();
    }
    Sex sex = null;
    if (!StringUtils.isBlank(payload.getGender())) {
      try {
        sex = Sex.valueOf(payload.getGender());
      }
      catch (IllegalArgumentException e) {
        return Response.status(Status.BAD_REQUEST).entity(String.format("Invalid payload gender %s", payload.getGender())).build();
      }
    }
    
    // Study programme validation
    
    if (!UserUtils.canAccessOrganization(sessionController.getUser(), student.getStudyProgramme().getOrganization())) {
      return Response.status(Status.BAD_REQUEST).entity("No study programme access").build();
    }
    if (StringUtils.isBlank(payload.getStudyProgrammeIdentifier()) || !NumberUtils.isNumber(payload.getStudyProgrammeIdentifier())) {
      return Response.status(Status.BAD_REQUEST).entity("Invalid payload study programme").build();
    }
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudyProgramme studyProgramme = studyProgrammeDAO.findById(Long.valueOf(payload.getStudyProgrammeIdentifier()));
    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studyProgramme.getOrganization())) {
      return Response.status(Status.BAD_REQUEST).entity("No study programme access").build();
    }
    
    // Birthday generation if SSN present

    Date birthday = null;
    if (!StringUtils.isBlank(payload.getSsn())) {
      try {
        birthday = new SimpleDateFormat("ddMMyy").parse(payload.getSsn().substring(0, 6));
      }
      catch (Exception e) {
        return Response.status(Status.BAD_REQUEST).entity("Invalid payload SSN").build();
      }
    }
    
    // Check that email and (possible) SSN belong to the student being edited
    
    String address = StringUtils.trim(StringUtils.lowerCase(payload.getEmail()));
    boolean emailAvailable = commonController.isEmailAvailable(address);
    if (!emailAvailable) {
      Person person = personController.findUniquePersonByEmail(address);
      if (person != null && !person.getId().equals(student.getPerson().getId())) {
        return Response.status(Status.CONFLICT).entity(getMessage(request.getLocale(), "error.emailInUse")).build();
      }
    }
    String ssn = null;
    if (!StringUtils.isBlank(payload.getSsn())) {
      ssn = StringUtils.upperCase(payload.getSsn());
      Person person = personController.findBySsn(ssn);
      if (person != null && !person.getId().equals(student.getPerson().getId())) {
        return Response.status(Status.CONFLICT).entity(getMessage(request.getLocale(), "error.ssnInUse")).build();
      }
    }
    
    // Update
    
    Person person = student.getPerson();
    person = personController.updatePerson(person, birthday, ssn, sex, person.getBasicInfo(), person.getSecureInfo());
    student = studentController.updateStudent(
        student,
        payload.getFirstName(),
        payload.getLastName(),
        student.getNickname(),
        student.getAdditionalInfo(),
        student.getStudyTimeEnd(),
        student.getActivityType(),
        student.getExaminationType(),
        student.getEducationalLevel(),
        student.getEducation(),
        student.getNationality(),
        student.getMunicipality(),
        student.getLanguage(),
        student.getSchool(),
        student.getCurriculum(),
        student.getPreviousStudies(),
        student.getStudyStartDate(),
        student.getStudyEndDate(),
        student.getStudyEndReason(),
        student.getStudyEndText());
    if (!student.getStudyProgramme().getId().equals(studyProgramme.getId())) {
      student = studentController.updateStudyProgramme(student, studyProgramme);
    }
    if (emailAvailable) {
      EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
      Email existingEmail = emailDAO.findByContactInfoAndDefaultAddress(student.getContactInfo(), Boolean.TRUE);
      if (existingEmail != null) {
        emailDAO.delete(existingEmail);
      }
      userController.addUserEmail(student, defaults.getStudentDefaultContactType(), address, Boolean.TRUE);
    }

    return Response.ok(payload).build();    
  }
  
  @Path("/requestCredentialReset")
  @GET
  @RESTPermit(MuikkuPermissions.MUIKKU_RESET_CREDENTIALS)
  public Response requestCredentialReset(@QueryParam("email") String email) {
    Person person = personController.findUniquePersonByEmail(email);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    byte[] sec = new byte[4096];
    SecureRandom R = new SecureRandom();
    R.nextBytes(sec);
    
    Date date = new Date();
    
    // Secret is for the communication purposes which will be authenticated by clientapplication with it's own secret
    String secret = DigestUtils.md5Hex(sec);
    
    // ConfirmSecret is the hash of secret + clientapplications secret
    ClientApplication clientApplication = clientApplicationController.getClientApplication();
    
    if (clientApplication != null) {
      String confirmSecret = DigestUtils.md5Hex(secret + clientApplication.getClientSecret());
      
      passwordResetRequestDAO.create(person, confirmSecret, date);
  
      // We return secret which cannot validate a reset by itself because it needs the client secret as authentication
      return Response.ok(secret).build();
    }
    else {
      return Response.status(Status.BAD_REQUEST).entity("Invalid client application").build();
    }
  }

  @Path("/resetCredentials/{HASH}")
  @GET
  @RESTPermit(MuikkuPermissions.MUIKKU_RESET_CREDENTIALS)
  public Response resetCredentials(@PathParam("HASH") String hash) {

    // Resolve internal authentication provider

    List<InternalAuthenticationProvider> providers = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
    InternalAuthenticationProvider provider = providers.size() == 1 ? providers.get(0) : null;
    if (provider == null || !provider.canUpdateCredentials()) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid authentication provider").build();
    }

    // Validate reset request
    
    PasswordResetRequest resetRequest = passwordResetRequestDAO.findBySecret(hash);
    if (resetRequest == null || isExpired(resetRequest)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    // Create payload with (possible) username
    
    CredentialResetPayload payload = new CredentialResetPayload();
    UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(provider.getName(), resetRequest.getPerson());
    if (userIdentification != null) {
      InternalAuth internalAuth = internalAuthDAO.findById(Long.valueOf(userIdentification.getExternalId()));
      if (internalAuth != null) {
        payload.setUsername(internalAuth.getUsername());
      }
    }
    return Response.ok(payload).build();
  }
  
  @Path("/resetCredentials")
  @POST
  @RESTPermit(MuikkuPermissions.MUIKKU_RESET_CREDENTIALS)
  public Response resetCredentials(@Context HttpServletRequest request, CredentialResetPayload payload) {

    // Resolve internal authentication provider

    List<InternalAuthenticationProvider> providers = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
    InternalAuthenticationProvider provider = providers.size() == 1 ? providers.get(0) : null;
    if (provider == null || !provider.canUpdateCredentials()) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid authentication provider").build();
    }
    
    // Validate reset request
    
    PasswordResetRequest resetRequest = passwordResetRequestDAO.findBySecret(payload.getSecret());
    if (resetRequest == null || isExpired(resetRequest)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    // Validate username uniqueness
    
    InternalAuth internalAuth = internalAuthDAO.findByUsername(payload.getUsername());
    if (internalAuth != null) {
      UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndExternalId(provider.getName(), internalAuth.getId().toString());
      if (userIdentification != null) {
        if (!userIdentification.getPerson().getId().equals(resetRequest.getPerson().getId())) {
          return Response.status(Status.CONFLICT).entity(getMessage(request.getLocale(), "error.usernameInUse")).build();
        }
      }
    }
    else {
      UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(provider.getName(), resetRequest.getPerson());
      if (userIdentification != null) {
        internalAuth = internalAuthDAO.findById(Long.valueOf(userIdentification.getExternalId()));
      }
    }

    // Change the credentials
    
    try {
      if (internalAuth == null) {
        String externalId = provider.createCredentials(payload.getUsername(), payload.getPassword());
        userIdentificationDAO.create(resetRequest.getPerson(), provider.getName(), externalId);
      }
      else {
        provider.updateCredentials(internalAuth.getId().toString(), payload.getUsername(), payload.getPassword());
      }
    }
    catch (Exception e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }
    
    // Clear reset request(s)
    
    List<PasswordResetRequest> passwordResetRequests = passwordResetRequestDAO.listByPerson(resetRequest.getPerson());
    for (PasswordResetRequest passwordResetRequest : passwordResetRequests) {
      passwordResetRequestDAO.delete(passwordResetRequest);
    }
    
    return Response.noContent().build();
  }
  
  private boolean isExpired(PasswordResetRequest resetRequest) {
    Date expiryDate = DateUtils.addHours(new Date(), -2);
    return resetRequest == null || resetRequest.getDate() == null || expiryDate.after(resetRequest.getDate());
  }

  private String getMessage(Locale locale, String key) {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
    return bundle.getString(key);
  }

}
