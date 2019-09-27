package fi.otavanopisto.pyramus.rest;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.PersonController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.MuikkuPermissions;
import fi.otavanopisto.pyramus.rest.model.muikku.StaffMemberPayload;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/muikku")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class MuikkuRESTService {

  @Inject
  private HttpServletRequest httpRequest;
  
  @Inject
  private CommonController commonController;

  @Inject
  private PersonController personController;

  @Inject
  private UserController userController;

  @Inject
  private SessionController sessionController;

  @Path("/users")
  @POST
  @RESTPermit(MuikkuPermissions.MUIKKU_CREATE_STAFF_MEMBER)
  public Response createUser(StaffMemberPayload payload) {
    
    // Basic payload validation
    
    if (StringUtils.isAnyBlank(payload.getFirstName(), payload.getLastName(), payload.getEmail(), payload.getRole())) {
      return Response.status(Status.BAD_REQUEST).entity("Empty fields in payload").build();
    }
    
    // Endpoint only supports creation of managers and teachers
    
    Role role = Role.valueOf(payload.getRole());
    if (role != Role.MANAGER && role != Role.TEACHER) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Unsupported role %s", payload.getRole())).build();
    }
    
    // Check if user exists based on email
    
    String address = StringUtils.trim(StringUtils.lowerCase(payload.getEmail()));
    Email email = commonController.findEmailByAddress(address);
    if (email != null) {
      return Response.status(Status.CONFLICT).entity(getMessage("error.emailInUse")).build();
    }
    
    // User creation
    
    User loggedUser = sessionController.getUser();    
    Person person = personController.createPerson(null,  null,  null,  null,  Boolean.FALSE);
    StaffMember staffMember = userController.createStaffMember(loggedUser.getOrganization(), payload.getFirstName(), payload.getLastName(), role, person);
    // TODO Configurable default contact types for? In this case, 3 = Work 
    ContactType contactType = commonController.findContactTypeById(3L);
    userController.addUserEmail(staffMember, contactType, address, Boolean.TRUE);
    payload.setIdentifier(staffMember.getId().toString());
    
    return Response.ok(payload).build();
  }
  
  private String getMessage(String key) {
    Locale locale = Locale.forLanguageTag(httpRequest.getHeader("Accept-Language"));
    ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
    return bundle.getString(key);
  }

}
