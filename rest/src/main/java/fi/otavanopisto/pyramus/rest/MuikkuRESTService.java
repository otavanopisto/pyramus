package fi.otavanopisto.pyramus.rest;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.users.InternalAuthDAO;
import fi.otavanopisto.pyramus.dao.users.PasswordResetRequestDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Defaults;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.PasswordResetRequest;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.ClientApplicationController;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.PersonController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.MuikkuPermissions;
import fi.otavanopisto.pyramus.rest.model.muikku.CredentialResetPayload;
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
  public Response createUser(StaffMemberPayload payload) {
    
    // Prerequisites
    
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    Defaults defaults = defaultsDAO.getDefaults();
    if (defaults.getUserDefaultContactType() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("userDefaultContactType not set in Defaults").build();
    }

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
    if (loggedUser.getOrganization() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Current user lacks organization").build();
    }
    Person person = personController.createPerson(null,  null,  null,  null,  Boolean.FALSE);
    StaffMember staffMember = userController.createStaffMember(loggedUser.getOrganization(), payload.getFirstName(), payload.getLastName(), role, person);
    userController.addUserEmail(staffMember, defaults.getUserDefaultContactType(), address, Boolean.TRUE);
    payload.setIdentifier(staffMember.getId().toString());
    
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
  public Response resetCredentials(CredentialResetPayload payload) {

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
          return Response.status(Status.CONFLICT).entity(getMessage("error.usernameInUse")).build();
        }
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

  private String getMessage(String key) {
    Locale locale = Locale.forLanguageTag(httpRequest.getHeader("Accept-Language"));
    ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
    return bundle.getString(key);
  }

}
