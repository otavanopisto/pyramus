package fi.pyramus.rest;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import fi.pyramus.dao.users.PasswordResetRequestDAO;
import fi.pyramus.dao.users.UserIdentificationDAO;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.users.PasswordResetRequest;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.domainmodel.users.UserIdentification;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.pyramus.rest.annotation.RESTPermit;
import fi.pyramus.rest.annotation.RESTPermit.Handling;
import fi.pyramus.rest.annotation.RESTPermit.Style;
import fi.pyramus.rest.annotation.Unsecure;
import fi.pyramus.rest.controller.ClientApplicationController;
import fi.pyramus.rest.controller.PersonController;
import fi.pyramus.rest.controller.StudentController;
import fi.pyramus.rest.controller.UserController;
import fi.pyramus.rest.controller.permissions.PersonPermissions;
import fi.pyramus.rest.controller.permissions.StudentPermissions;
import fi.pyramus.rest.model.UserCredentials;
import fi.pyramus.rest.model.UserCredentialReset;
import fi.pyramus.rest.security.RESTSecurity;
import fi.pyramus.security.impl.SessionController;

@Path("/persons")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class PersonRESTService extends AbstractRESTService {

  @Inject
  private RESTSecurity restSecurity;
  
  @Inject
  private UserController userController;

  @Inject
  private StudentController studentController;
  
  @Inject
  private PersonController personController;

  @Inject
  private UserIdentificationDAO userIdentificationDAO;
  
  @Inject
  private PasswordResetRequestDAO passwordResetRequestDAO;
  
  @Inject
  private SessionController sessionController;
  
  @Inject
  private ObjectFactory objectFactory;

  @Inject
  private ClientApplicationController clientApplicationController;
  
  @Path("/persons")
  @POST
  @RESTPermit (PersonPermissions.CREATE_PERSON)
  public Response createPerson(fi.pyramus.rest.model.Person entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if ((entity.getSex() == null) || (entity.getSecureInfo() == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Sex sex = null;
    
    switch (entity.getSex()) {
      case FEMALE:
        sex = Sex.FEMALE;
      break;
      case MALE:
        sex = Sex.MALE;
      break;
    }
    
    return Response.ok(objectFactory.createModel(personController.createPerson(toDate(entity.getBirthday()), 
        entity.getSocialSecurityNumber(), sex, entity.getBasicInfo(), entity.getSecureInfo()))).build();
  }
  
  @Path("/persons")
  @GET
  @RESTPermit (PersonPermissions.LIST_PERSONS)
  public Response findPersons(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Person> persons;
    if (filterArchived) {
      persons = personController.findUnarchivedPersons();
    } else {
      persons = personController.findPersons();
    }
    
    return Response.ok(objectFactory.createModel(persons)).build();
  }

  @Path("/persons/{ID:[0-9]*}")
  @GET
  @RESTPermit (handling = Handling.INLINE)
//@RESTPermit ({PersonPermissions.FIND_PERSON, PersonPermissions.PERSON_OWNER })
  public Response findPersonById(@PathParam("ID") Long id) {
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!restSecurity.hasPermission(new String[] { PersonPermissions.FIND_PERSON, PersonPermissions.PERSON_OWNER }, person, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    return Response.ok(objectFactory.createModel(person)).build();
  }

  @Path("/persons/{ID:[0-9]*}")
  @PUT
  @RESTPermit (handling = Handling.INLINE)
//  @RESTPermit ({PersonPermissions.UPDATE_PERSON, PersonPermissions.PERSON_OWNER })
  public Response updatePerson(@PathParam("ID") Long id, fi.pyramus.rest.model.Person entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if ((entity.getSex() == null) || (entity.getSecureInfo() == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Sex sex = null;
    
    switch (entity.getSex()) {
      case FEMALE:
        sex = Sex.FEMALE;
      break;
      case MALE:
        sex = Sex.MALE;
      break;
    }

    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { PersonPermissions.UPDATE_PERSON, PersonPermissions.PERSON_OWNER }, person, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    User defaultUser = null;
    if (entity.getDefaultUserId() != null) {
      defaultUser = userController.findUserById(entity.getDefaultUserId());
      if (!defaultUser.getPerson().getId().equals(person.getId())) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }
    
    personController.updatePersonDefaultUser(person, defaultUser);
    
    return Response.ok(objectFactory.createModel(personController.updatePerson(person, toDate(entity.getBirthday()), 
        entity.getSocialSecurityNumber(), sex, entity.getBasicInfo(), entity.getSecureInfo()))).build();
  }

  @Path("/persons/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (PersonPermissions.DELETE_PERSON)
  public Response deletePerson(@PathParam("ID") Long id) {
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    personController.deletePerson(person);
    
    return Response.noContent().build();
  }

  @Path("/persons/{ID:[0-9]*}/students")
  @GET
  @RESTPermit (StudentPermissions.LIST_STUDENTSBYPERSON)
  public Response listStudentsByPerson(@PathParam("ID") Long id) {
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(studentController.listStudentByPerson(person))).build();
  }
  
  @Path("/persons/{ID:[0-9]*}/credentials")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response getCredentials(@PathParam("ID") Long id) {
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    // Check that logged in user is the same we're modifying
    
    User user = sessionController.getUser();
    
    // User needs to be logged in for password change
    if (user == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    // Persons must match
    if (!user.getPerson().getId().equals(person.getId())) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, person, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    // TODO: Support for multiple internal authentication providers
    List<InternalAuthenticationProvider> internalAuthenticationProviders = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
    if (internalAuthenticationProviders.size() == 1) {
      InternalAuthenticationProvider internalAuthenticationProvider = internalAuthenticationProviders.get(0);
      if (internalAuthenticationProvider != null) {
        UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(internalAuthenticationProvider.getName(), person);
        
        String username = internalAuthenticationProvider.getUsername(userIdentification.getExternalId());
        
        if (username != null) {
          UserCredentials credentials = new UserCredentials(null, username, null);

          return Response.ok(credentials).build();
        }
      }
    }
    
    return Response.status(Status.NOT_FOUND).build();
  }
  
  @Path("/persons/{ID:[0-9]*}/credentials")
  @PUT
  @RESTPermit (handling = Handling.INLINE)
  public Response updateCredentials(@PathParam("ID") Long id, UserCredentials userCredentialChange) {
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    // Check that logged in user is the same we're modifying
    
    User user = sessionController.getUser();
    
    // User needs to be logged in for password change
    if (user == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    // Persons must match
    if (!user.getPerson().getId().equals(person.getId())) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, person, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    String newUserName = userCredentialChange.getUsername();
    String newPassword = userCredentialChange.getNewPassword();
    String oldPassword = userCredentialChange.getOldPassword();
    
    boolean usernameBlank = StringUtils.isBlank(newUserName);
    boolean passwordBlank = StringUtils.isBlank(newPassword);

    if (!usernameBlank||!passwordBlank) {
      // TODO: Support for multiple internal authentication providers
      List<InternalAuthenticationProvider> internalAuthenticationProviders = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
      if (internalAuthenticationProviders.size() == 1) {
        InternalAuthenticationProvider internalAuthenticationProvider = internalAuthenticationProviders.get(0);
        if (internalAuthenticationProvider != null) {
          UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(internalAuthenticationProvider.getName(), person);
          
          if (internalAuthenticationProvider.canUpdateCredentials()) {
            if (userIdentification == null) {
              String externalId = internalAuthenticationProvider.createCredentials(newUserName, newPassword);
              userIdentificationDAO.create(person, internalAuthenticationProvider.getName(), externalId);
            } else {
              if ("-1".equals(userIdentification.getExternalId())) {
                String externalId = internalAuthenticationProvider.createCredentials(newUserName, newPassword);
                userIdentificationDAO.updateExternalId(userIdentification, externalId);
              } else {
                // Check that old password matches

                if (internalAuthenticationProvider.validatePassword(userIdentification.getExternalId(), oldPassword)) {
                  if (!StringUtils.isBlank(newUserName))
                    internalAuthenticationProvider.updateUsername(userIdentification.getExternalId(), newUserName);
                
                  if (!StringUtils.isBlank(newPassword))
                    internalAuthenticationProvider.updatePassword(userIdentification.getExternalId(), newPassword);
                } else {
                  return Response.status(Status.FORBIDDEN).build();
                }
              }
            }
          }
        }
      }
    }
    
    return Response.noContent().build();
  }
  
//  @Path("/resetpasswordbyemail")
//  @GET
//  @Unsecure
//  public Response resetPasswordByEmail(@QueryParam(value = "email") String email) {
//    Person person = personController.findUniquePersonByEmail(email);
//    if (person == null) {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//
//    byte[] sec = new byte[4096];
//    SecureRandom R = new SecureRandom();
//    R.nextBytes(sec);
//    
//    Date date = new Date();
//    
//    // Secret is for the communication purposes which will be authenticated by clientapplication with it's own secret
//    String secret = DigestUtils.md5Hex(sec);
//
//    // ConfirmSecret is the hash of secret + clientapplications secret
//    ClientApplication clientApplication = clientApplicationController.getClientApplication();
//    
//    if (clientApplication != null) {
//      String confirmSecret = DigestUtils.md5Hex(secret + clientApplication.getClientSecret());
//      
//      passwordResetRequestDAO.create(person, confirmSecret, date);
//  
//      // We return secret which cannot validate a reset by itself because it needs the client secret as authentication
//      return Response.ok(secret).build();
//    } else {
//      return Response.status(Status.BAD_REQUEST).build();
//    }
//  }
//
//  @Path("/resetpasswordbyemail")
//  @POST
//  @Unsecure
//  public Response confirmResetPasswordByEmail(UserCredentialReset reset) {
//    PasswordResetRequest resetRequest = passwordResetRequestDAO.findBySecret(reset.getSecret());
//    
//    if (resetRequest == null) {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//    
//    String newPassword = reset.getNewPassword();
//    Person person = resetRequest.getPerson();
//    
//    boolean passwordBlank = StringUtils.isBlank(newPassword);
//
//    if (!passwordBlank) {
//      // TODO: Support for multiple internal authentication providers
//      List<InternalAuthenticationProvider> internalAuthenticationProviders = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
//      if (internalAuthenticationProviders.size() == 1) {
//        InternalAuthenticationProvider internalAuthenticationProvider = internalAuthenticationProviders.get(0);
//        if (internalAuthenticationProvider != null) {
//          UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(internalAuthenticationProvider.getName(), person);
//          
//          if (internalAuthenticationProvider.canUpdateCredentials()) {
//            if (userIdentification != null) {
//              if (!"-1".equals(userIdentification.getExternalId())) {
//                if (!StringUtils.isBlank(newPassword))
//                  internalAuthenticationProvider.updatePassword(userIdentification.getExternalId(), newPassword);
//              }
//            }
//          }
//        }
//      }
//    }
//    
//    passwordResetRequestDAO.delete(resetRequest);
//    
//    return Response.noContent().build();
//  }
  
}
