package fi.pyramus.rest;

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

import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.rest.annotation.RESTPermit;
import fi.pyramus.rest.annotation.RESTPermit.Handling;
import fi.pyramus.rest.annotation.RESTPermit.Style;
import fi.pyramus.rest.controller.PersonController;
import fi.pyramus.rest.controller.StudentController;
import fi.pyramus.rest.controller.UserController;
import fi.pyramus.rest.controller.permissions.PersonPermissions;
import fi.pyramus.rest.controller.permissions.StudentPermissions;
import fi.pyramus.rest.security.RESTSecurity;

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
  private ObjectFactory objectFactory;

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
  @RESTPermit (PersonPermissions.FIND_PERSON)
  public Response findPersonById(@PathParam("ID") Long id) {
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
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
}
